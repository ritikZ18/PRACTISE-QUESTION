package com.example.movierecommender.service;

import com.example.movierecommender.model.Movie;
import com.example.movierecommender.repository.RatingRegister;
import com.example.movierecommender.strategy.HybridStrategy;
import com.example.movierecommender.strategy.RecommendationStrategy;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MovieRecommender {

    private final RatingRegister register;
    private RecommendationStrategy strategy;

    public MovieRecommender(RatingRegister register) {
        this.register = register;
        this.strategy = new HybridStrategy(); // default
    }

    public void setStrategy(RecommendationStrategy strategy) {
        this.strategy = strategy;
    }

    public List<Movie> recommend(long userId, int limit) {
        int fetchLimit = Math.max(limit, limit * 3);
        List<Movie> raw = strategy.recommend(userId, fetchLimit, register);
        return dedupeByFingerprint(raw, limit);
    }

    public List<Movie> recommend(long userId) {
        return recommend(userId, 10);
    }

    private static List<Movie> dedupeByFingerprint(List<Movie> movies, int limit) {
        if (movies == null || movies.isEmpty()) {
            return List.of();
        }

        Map<String, Movie> unique = new LinkedHashMap<>();
        for (Movie movie : movies) {
            if (movie == null) {
                continue;
            }
            unique.putIfAbsent(fingerprint(movie), movie);
            if (unique.size() >= limit) {
                break;
            }
        }
        return new ArrayList<>(unique.values());
    }

    private static String fingerprint(Movie movie) {
        String title = movie.getTitle() == null ? "" : movie.getTitle().trim().toLowerCase(Locale.ROOT);
        String language = movie.getLanguage() == null ? "" : movie.getLanguage().trim().toLowerCase(Locale.ROOT);
        return title + "|" + movie.getYear() + "|" + movie.getRuntime() + "|" + language;
    }
}
