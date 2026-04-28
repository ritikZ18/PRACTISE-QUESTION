package com.example.movierecommender.tmdb;

import com.example.movierecommender.model.Genre;
import com.example.movierecommender.model.Movie;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TmdbChartsService {
    private static final Map<Integer, Genre> TMDB_GENRE_MAP = Map.ofEntries(
        Map.entry(28, Genre.ACTION),
        Map.entry(12, Genre.ADVENTURE),
        Map.entry(16, Genre.ANIMATION),
        Map.entry(35, Genre.COMEDY),
        Map.entry(80, Genre.CRIME),
        Map.entry(99, Genre.DOCUMENTARY),
        Map.entry(18, Genre.DRAMA),
        Map.entry(27, Genre.HORROR),
        Map.entry(10749, Genre.ROMANCE),
        Map.entry(878, Genre.SCI_FI),
        Map.entry(53, Genre.THRILLER),
        Map.entry(14, Genre.FANTASY)
    );

    public List<Movie> trendingMovies(String timeWindow, int limit) throws Exception {
        String window = (timeWindow == null || timeWindow.isBlank()) ? "week" : timeWindow.trim().toLowerCase();
        if (!window.equals("day") && !window.equals("week")) {
            window = "week";
        }

        JSONObject payload = client().getJson("/trending/movie/" + window + "?language=en-US");
        return mapResults(payload, limit);
    }

    public List<Movie> topRatedMovies(int limit) throws Exception {
        JSONObject payload = client().getJson("/movie/top_rated?language=en-US&page=1");
        return mapResults(payload, limit);
    }

    private List<Movie> mapResults(JSONObject payload, int limit) {
        JSONArray results = payload.optJSONArray("results");
        if (results == null) {
            return List.of();
        }

        int capped = Math.min(limit, results.length());
        List<Movie> out = new ArrayList<>(capped);
        for (int i = 0; i < results.length() && out.size() < capped; i++) {
            JSONObject item = results.optJSONObject(i);
            if (item == null) {
                continue;
            }
            out.add(mapMovie(item));
        }
        return out;
    }

    private Movie mapMovie(JSONObject item) {
        long id = item.optLong("id", 0);
        String title = item.optString("title", item.optString("name", "Untitled"));

        String releaseDate = item.optString("release_date", "");
        int year = 0;
        if (releaseDate.length() >= 4) {
            try {
                year = Integer.parseInt(releaseDate.substring(0, 4));
            } catch (NumberFormatException ignored) {
                year = 0;
            }
        }

        String language = item.optString("original_language", "");
        String overview = item.optString("overview", "");
        String posterPath = item.optString("poster_path", "");

        List<Genre> genres = new ArrayList<>();
        JSONArray genreIds = item.optJSONArray("genre_ids");
        if (genreIds != null) {
            for (int i = 0; i < genreIds.length(); i++) {
                Genre mapped = TMDB_GENRE_MAP.get(genreIds.optInt(i));
                if (mapped != null) {
                    genres.add(mapped);
                }
            }
        }

        // TMDB uses a 0..10 vote_average; we map to 0..5 for UI consistency.
        double tmdbScore = item.optDouble("vote_average", 0.0);
        int tmdbVotes = item.optInt("vote_count", 0);
        double mappedRating = Math.max(0.0, Math.min(5.0, tmdbScore / 2.0));

        return new Movie(
            id,
            title,
            year,
            genres,
            "",               // director not available in chart payload
            overview,
            language,
            0,                // runtime not available in chart payload
            posterPath,        // normalized by Movie constructor (supports TMDB paths)
            mappedRating,
            tmdbVotes
        );
    }

    private TmdbClient client() {
        return new TmdbClient();
    }
}
