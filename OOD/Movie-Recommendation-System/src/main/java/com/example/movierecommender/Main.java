package com.example.movierecommender;

import com.example.movierecommender.model.*;
import com.example.movierecommender.repository.JsonPersistence;
import com.example.movierecommender.repository.RatingRegister;
import com.example.movierecommender.service.MovieRecommender;
import com.example.movierecommender.service.SearchService;
import com.example.movierecommender.strategy.CollaborativeFilteringStrategy;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("🎬 Movie Recommendation System");
        System.out.println("==============================\n");

        // Initialize register and load data
        RatingRegister register = new RatingRegister();
        JsonPersistence persistence = new JsonPersistence();

        // Load all data from JSON files
        System.out.println("📂 Loading data from JSON files...");
        persistence.loadAll(register);
        System.out.println("✅ Data loaded: "
            + register.getAllMovies().size() + " movies, "
            + register.getAllUsers().size() + " users\n");

        // Initialize services
        MovieRecommender recommender = new MovieRecommender(register);
        SearchService searchService = new SearchService(register);

        // ──── DEMO 1: Get recommendations for Alice ────
        System.out.println("👤 Recommendations for Alice (User 1):");
        System.out.println("---");
        recommender.recommend(1, 5).forEach(m ->
            System.out.println("  • " + m.getTitle() + " (" + m.getYear() + ") - ⭐ "
                + String.format("%.1f", m.getAverageRating())));
        System.out.println();

        // ──── DEMO 2: Search with mood ("feeling" text) ────
        System.out.println("🎯 Search: Alice feeling 'mind-blowing and philosophical':");
        System.out.println("---");
        Filter baseFilter = Filter.builder()
            .minRating(4.0)
            .sortBy(Filter.SortBy.RATING)
            .build();
        searchService.search(1, "mind blown philosophical think", baseFilter)
            .stream()
            .limit(5)
            .forEach(m -> System.out.println("  • " + m.getTitle() + " (" + m.getYear() + ")"));
        System.out.println();

        // ──── DEMO 3: Get trending movies ────
        System.out.println("🔥 Trending Movies (by rating):");
        System.out.println("---");
        searchService.getTrending(5).forEach(m ->
            System.out.println("  • " + m.getTitle() + " - ⭐ "
                + String.format("%.1f", m.getAverageRating())
                + " (" + m.getRatingCount() + " ratings)"));
        System.out.println();

        // ──── DEMO 4: Filter by genre ────
        System.out.println("🎭 Movies in Comedy genre:");
        System.out.println("---");
        searchService.searchByGenre(2, Genre.COMEDY, 5)
            .forEach(m -> System.out.println("  • " + m.getTitle() + " (" + m.getYear() + ")"));
        System.out.println();

        // ──── DEMO 5: Search by mood ────
        System.out.println("😨 Scary mood (Horror & Thriller):");
        System.out.println("---");
        searchService.getByMood(Mood.SCARED, 5)
            .forEach(m -> System.out.println("  • " + m.getTitle() + " (" + m.getYear() + ")"));
        System.out.println();

        // ──── DEMO 6: Show user preferences ────
        System.out.println("👥 User Profiles:");
        System.out.println("---");
        register.getAllUsers().stream().limit(3).forEach(user -> {
            int ratingCount = register.getRatingCountForUser(user.getId());
            System.out.println("  • " + user.getName() + " - "
                + user.getPreferredGenres() + " (rated " + ratingCount + " movies)");
        });
        System.out.println();

        // ──── DEMO 7: Using different strategy ────
        System.out.println("📊 Changing strategy to Collaborative Filtering:");
        System.out.println("---");
        recommender.setStrategy(new CollaborativeFilteringStrategy());
        recommender.recommend(2, 3).forEach(m ->
            System.out.println("  • " + m.getTitle() + " (" + m.getYear() + ")"));
        System.out.println();

        System.out.println("✨ Backend system ready for frontend integration!");
        System.out.println("📡 API endpoints should expose:");
        System.out.println("   - GET /movies");
        System.out.println("   - GET /users");
        System.out.println("   - GET /recommend?userId=X&limit=Y");
        System.out.println("   - GET /search?userId=X&feeling=TEXT&filters=...");
        System.out.println("   - POST /rate (userId, movieId, rating)");
    }
}