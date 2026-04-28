package com.example.movierecommender.tmdb;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Bulk importer for TMDB. Supports two modes:
 * - ids: comma-separated TMDB ids
 * - discover: by TMDB discover endpoint with a query string
 */
public class TmdbBulkImporter {
    private final TmdbClient client;
    private final com.example.movierecommender.tmdb.CatalogManager catalog;

    public TmdbBulkImporter() throws Exception {
        this.client = new TmdbClient();
        this.catalog = new com.example.movierecommender.tmdb.CatalogManager();
    }

    public void importByIds(List<Integer> ids) throws Exception {
        for (int id : ids) {
            JSONObject movie = client.fetchMovie(id);
            JSONObject credits = client.fetchCredits(id);
            JSONObject mapped = map(movie, credits);
            catalog.addMovie(mapped);
            System.out.println("Imported TMDB:" + id + " -> " + mapped.optString("title"));
        }
        catalog.saveIndexAndVectors();
    }

    public void importByDiscover(String query) throws Exception {
        // query is raw query like "with_genres=28&sort_by=popularity.desc&language=en-US"
        String path = "/discover/movie?" + query;
        JSONObject result = client.getJson(path);
        JSONArray arr = result.optJSONArray("results");
        if (arr == null) return;
        List<Integer> ids = new ArrayList<>();
        for (int i = 0; i < arr.length(); i++) {
            ids.add(arr.getJSONObject(i).getInt("id"));
        }
        importByIds(ids);
    }

    private JSONObject map(JSONObject movie, JSONObject credits) {
        JSONObject mapped = new JSONObject();
        mapped.put("title", movie.optString("title"));
        String release = movie.optString("release_date", "");
        if (!release.isEmpty() && release.length() >= 4) mapped.put("year", Integer.parseInt(release.substring(0,4))); else mapped.put("year", 0);
        mapped.put("description", movie.optString("overview", ""));
        mapped.put("runtime", movie.optInt("runtime", 0));
        mapped.put("language", movie.optString("original_language", ""));
        String poster = movie.optString("poster_path", "");
        if (!poster.isEmpty()) mapped.put("posterUrl", "https://image.tmdb.org/t/p/w300" + poster); else mapped.put("posterUrl", "https://via.placeholder.com/300x450?text="+movie.optString("title"));

        JSONArray genres = movie.optJSONArray("genres");
        List<String> g = new ArrayList<>();
        if (genres != null) {
            for (int i = 0; i < genres.length(); i++) {
                String name = genres.getJSONObject(i).optString("name");
                g.add(mapGenreName(name));
            }
        }
        mapped.put("genres", g);

        // director
        String director = "";
        JSONArray crew = credits.optJSONArray("crew");
        if (crew != null) {
            for (int i = 0; i < crew.length(); i++) {
                JSONObject c = crew.getJSONObject(i);
                if ("Director".equals(c.optString("job"))) { director = c.optString("name"); break; }
            }
        }
        mapped.put("director", director);

        mapped.put("avgRating", 0.0);
        mapped.put("ratingCount", 0);
        return mapped;
    }

    private String mapGenreName(String name) {
        if (name == null) return "DRAMA";
        String key = name.toUpperCase().replaceAll("[ &-]","_").replaceAll("[^A-Z0-9_]","_");
        try {
            java.lang.Enum.valueOf(com.example.movierecommender.model.Genre.class, key);
            return key;
        } catch (IllegalArgumentException e) {
            // fallback mapping for common names
            if (key.contains("SCI")) return "SCI_FI";
            if (key.contains("DOCUMENTARY")) return "DOCUMENTARY";
            if (key.contains("ANIMATION")) return "ANIMATION";
            if (key.contains("ROMANCE")) return "ROMANCE";
            if (key.contains("HORROR")) return "HORROR";
            if (key.contains("ACTION")) return "ACTION";
            if (key.contains("THRILL")) return "THRILLER";
            if (key.contains("CRIME")) return "CRIME";
            return "DRAMA";
        }
    }

    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.err.println("Usage: TmdbBulkImporter --ids 550,551 or --discover 'with_genres=28&language=en-US' ");
            System.exit(2);
        }
        TmdbBulkImporter importer = new TmdbBulkImporter();
        String arg = args[0];
        if (arg.startsWith("--ids")) {
            String list = arg.substring(arg.indexOf(' ')+1).trim();
            String[] parts = list.split(",");
            List<Integer> ids = new ArrayList<>();
            for (String p : parts) ids.add(Integer.parseInt(p.trim()));
            importer.importByIds(ids);
        } else if (arg.startsWith("--discover")) {
            String query = arg.substring(arg.indexOf(' ')+1).trim();
            importer.importByDiscover(query);
        } else {
            System.err.println("Unknown mode");
        }
    }
}
