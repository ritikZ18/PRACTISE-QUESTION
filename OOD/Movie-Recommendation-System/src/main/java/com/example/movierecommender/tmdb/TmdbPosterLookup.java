package com.example.movierecommender.tmdb;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Optional;

public class TmdbPosterLookup {
    private final TmdbClient client;

    public TmdbPosterLookup() {
        this.client = new TmdbClient();
    }

    public Optional<String> findPosterUrl(String title, Integer year, String language) throws Exception {
        if (title == null || title.isBlank()) {
            return Optional.empty();
        }

        String lang = (language == null || language.isBlank()) ? "en-US" : language.trim();
        String query = URLEncoder.encode(title.trim(), StandardCharsets.UTF_8);
        StringBuilder path = new StringBuilder("/search/movie?include_adult=false&page=1");
        path.append("&language=").append(URLEncoder.encode(lang, StandardCharsets.UTF_8));
        path.append("&query=").append(query);
        if (year != null && year > 0) {
            path.append("&year=").append(year);
        }

        JSONObject payload = client.getJson(path.toString());
        JSONArray results = payload.optJSONArray("results");
        if (results == null || results.isEmpty()) {
            return Optional.empty();
        }

        JSONObject best = pickBest(results, title, year);
        if (best == null) {
            return Optional.empty();
        }

        String posterPath = best.optString("poster_path", "").trim();
        if (posterPath.isEmpty()) {
            return Optional.empty();
        }

        if (posterPath.startsWith("/")) {
            return Optional.of("https://image.tmdb.org/t/p/w342" + posterPath);
        }
        return Optional.of("https://image.tmdb.org/t/p/w342/" + posterPath);
    }

    private JSONObject pickBest(JSONArray results, String title, Integer year) {
        String needle = normalizeTitle(title);
        JSONObject exact = null;
        JSONObject best = null;
        long bestVotes = -1;

        for (int i = 0; i < results.length(); i++) {
            JSONObject item = results.optJSONObject(i);
            if (item == null) continue;

            String candidateTitle = item.optString("title", item.optString("name", ""));
            if (candidateTitle.isBlank()) continue;

            String candidateNorm = normalizeTitle(candidateTitle);
            if (exact == null && candidateNorm.equals(needle)) {
                exact = item;
            }

            long voteCount = item.optLong("vote_count", 0);
            if (voteCount > bestVotes) {
                bestVotes = voteCount;
                best = item;
            }
        }

        // If year was provided, prefer exact-title matches that also match year (when available).
        if (year != null && year > 0 && exact != null) {
            Integer candidateYear = parseYear(exact.optString("release_date", ""));
            if (candidateYear != null && candidateYear.equals(year)) {
                return exact;
            }
        }

        return exact != null ? exact : best;
    }

    private Integer parseYear(String releaseDate) {
        if (releaseDate == null || releaseDate.length() < 4) return null;
        try {
            return Integer.parseInt(releaseDate.substring(0, 4));
        } catch (NumberFormatException ignored) {
            return null;
        }
    }

    private String normalizeTitle(String value) {
        String s = value == null ? "" : value.trim().toLowerCase(Locale.ROOT);
        s = s.replaceAll("[^a-z0-9]+", " ").trim();
        return s;
    }
}

