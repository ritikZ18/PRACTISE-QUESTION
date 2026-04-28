package com.example.movierecommender.tmdb;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.stream.Collectors;

public class TmdbImporterMain {
    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.err.println("Usage: TmdbImporterMain <tmdbMovieId>");
            System.exit(2);
        }
        int id = Integer.parseInt(args[0]);
        TmdbClient client = new TmdbClient();
        JSONObject movie = client.fetchMovie(id);
        JSONObject credits = client.fetchCredits(id);

        JSONObject mapped = new JSONObject();
        mapped.put("id", movie.optInt("id"));
        mapped.put("title", movie.optString("title"));
        String release = movie.optString("release_date", "");
        if (!release.isEmpty() && release.length() >= 4) {
            mapped.put("year", Integer.parseInt(release.substring(0,4)));
        } else {
            mapped.put("year", 0);
        }
        mapped.put("description", movie.optString("overview", ""));
        mapped.put("runtime", movie.optInt("runtime", 0));
        mapped.put("language", movie.optString("original_language", ""));
        mapped.put("posterUrl", movie.optString("poster_path", ""));

        JSONArray genres = movie.optJSONArray("genres");
        if (genres != null) {
            mapped.put("genres", genres.toList().stream()
                .map(o -> ((java.util.Map)o).get("name"))
                .collect(Collectors.toList()));
        } else {
            mapped.put("genres", new JSONArray());
        }

        // Extract director if present
        String director = credits.optJSONArray("crew") == null ? "" :
            credits.optJSONArray("crew").toList().stream()
                .map(o -> (java.util.Map)o)
                .filter(m -> "Director".equals(m.get("job")))
                .map(m -> String.valueOf(m.get("name")))
                .findFirst().orElse("");
        mapped.put("director", director);

        System.out.println(mapped.toString(2));
    }
}
