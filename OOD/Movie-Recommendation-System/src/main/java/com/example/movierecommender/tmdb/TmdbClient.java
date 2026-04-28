package com.example.movierecommender.tmdb;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class TmdbClient {
    private final String apiKey;
    private final String base = "https://api.themoviedb.org/3";

    public TmdbClient() {
        String key = System.getenv("TMDB_API_KEY");
        if (key == null || key.isBlank()) {
            throw new IllegalStateException("TMDB_API_KEY not set in environment");
        }
        this.apiKey = key;
    }

    public JSONObject getJson(String path) throws Exception {
        String url = base + path + (path.contains("?") ? "&" : "?") + "api_key=" + apiKey;
        HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
        con.setRequestMethod("GET");
        int status = con.getResponseCode();
        BufferedReader in = new BufferedReader(new InputStreamReader(
            status >= 200 && status < 300 ? con.getInputStream() : con.getErrorStream()
        ));
        StringBuilder content = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null) content.append(line);
        in.close();
        return new JSONObject(content.toString());
    }

    public JSONObject fetchMovie(int tmdbId) throws Exception {
        return getJson("/movie/" + tmdbId + "?language=en-US");
    }

    public JSONObject fetchCredits(int tmdbId) throws Exception {
        return getJson("/movie/" + tmdbId + "/credits");
    }

    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.err.println("Usage: TmdbClient <tmdbMovieId>");
            System.exit(2);
        }
        int id = Integer.parseInt(args[0]);
        TmdbClient client = new TmdbClient();
        JSONObject movie = client.fetchMovie(id);
        JSONObject credits = client.fetchCredits(id);
        JSONObject out = new JSONObject();
        out.put("tmdb", movie);
        out.put("credits", credits);
        System.out.println(out.toString(2));
    }
}
