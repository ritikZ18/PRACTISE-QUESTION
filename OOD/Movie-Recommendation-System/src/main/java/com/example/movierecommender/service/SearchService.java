package com.example.movierecommender.service;

import com.example.movierecommender.model.Filter;
import com.example.movierecommender.model.Movie;
import com.example.movierecommender.model.Mood;
import com.example.movierecommender.repository.RatingRegister;
import com.example.movierecommender.strategy.FilterEngine;
import com.example.movierecommender.strategy.MoodParser;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class SearchService {
    private final RatingRegister register;
    private final FilterEngine filterEngine;
    private final MovieRecommender recommender;

    public SearchService(RatingRegister register) {
        this.register = register;
        this.filterEngine = new FilterEngine();
        this.recommender = new MovieRecommender(register);
    }

    /**
     * Main search entry point — combines mood parsing + filtering + recommendation
     */
    public List<Movie> search(long userId, String feelingText, Filter baseFilter) {
        // Parse feeling text into mood
        Filter filter = baseFilter;
        if (feelingText != null && !feelingText.trim().isEmpty()) {
            filter = MoodParser.parse(feelingText)
                .map(mood -> Filter.builder()
                    .mood(mood)
                    .yearFrom(baseFilter.getYearFrom())
                    .yearTo(baseFilter.getYearTo())
                    .minRating(baseFilter.getMinRating())
                    .genres(baseFilter.getGenres())
                    .language(baseFilter.getLanguage())
                    .maxRuntime(baseFilter.getMaxRuntime())
                    .director(baseFilter.getDirector())
                    .sortBy(baseFilter.getSortBy())
                    .build())
                .orElse(baseFilter);
        }

        // Apply filters to full catalog
        List<Movie> filtered = filterEngine.apply(register.getAllMovies(), filter);

        // If user has rating history, rerank using hybrid recommendation strategy
        try {
            int userRatingCount = register.getRatingCountForUser(userId);
            if (userRatingCount > 0) {
                filtered = rerankByRecommendations(userId, filtered);
            }
        } catch (Exception e) {
            System.err.println("Search rerank failed, returning filtered results: " + e.getMessage());
        }

        return dedupeByFingerprint(filtered, filtered.size());
    }

    /**
     * Rerank movies by putting recommendations at the top
     */
    private List<Movie> rerankByRecommendations(long userId, List<Movie> movies) {
        // Get top 30 recommendations
        List<Movie> recs = recommender.recommend(userId, 30);
        Set<Long> recIds = recs.stream()
            .map(Movie::getId)
            .collect(Collectors.toSet());

        // Sort: recommended first (0), then others (1)
        return movies.stream()
            .sorted(Comparator.comparingInt(m -> recIds.contains(m.getId()) ? 0 : 1))
            .collect(Collectors.toList());
    }

    /**
     * Get trending movies (by average rating and rating count)
     */
    public List<Movie> getTrending(int limit) {
        List<Movie> trending = register.getAllMovies().stream()
            .filter(m -> m.getRatingCount() > 0)
            .sorted(
                Comparator.comparingDouble(Movie::getAverageRating)
                    .thenComparingInt(Movie::getRatingCount)
                    .reversed()
            )
            .limit(limit)
            .collect(Collectors.toList());
        return dedupeByFingerprint(trending, limit);
    }

    /**
     * Get personalized recommendations for a user
     */
    public List<Movie> getRecommendationsFor(long userId, int limit) {
        return recommender.recommend(userId, limit);
    }

    /**
     * Get recommendations by a specific mood
     */
    public List<Movie> getByMood(Mood mood, int limit) {
        Filter filter = Filter.builder().mood(mood).build();
        List<Movie> raw = filterEngine.apply(register.getAllMovies(), filter).stream()
            .limit(limit * 3L)
            .collect(Collectors.toList());
        return dedupeByFingerprint(raw, limit);
    }

    /**
     * Get recommendations by genre
     */
    public List<Movie> searchByGenre(long userId, com.example.movierecommender.model.Genre genre, int limit) {
        Filter filter = Filter.builder().genre(genre).build();
        List<Movie> filtered = filterEngine.apply(register.getAllMovies(), filter);

        // If user has history, boost recommendations
        int userRatingCount = register.getRatingCountForUser(userId);
        if (userRatingCount > 0) {
            filtered = rerankByRecommendations(userId, filtered);
        }

        return dedupeByFingerprint(filtered, limit);
    }

    private static List<Movie> dedupeByFingerprint(List<Movie> movies, int limit) {
        if (movies == null || movies.isEmpty() || limit <= 0) {
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
