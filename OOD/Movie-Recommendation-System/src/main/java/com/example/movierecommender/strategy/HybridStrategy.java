package com.example.movierecommender.strategy;

import com.example.movierecommender.model.Movie;
import com.example.movierecommender.repository.RatingRegister;

import java.util.*;

public class HybridStrategy implements RecommendationStrategy {

    private final RecommendationStrategy cfStrategy = new CollaborativeFilteringStrategy();
    private final RecommendationStrategy contentStrategy = new ContentBasedStrategy();

    @Override
    public String name() {
        return "hybrid";
    }

    @Override
    public List<Movie> recommend(long userId, int limit, RatingRegister register) {
        int count = register.getRatingCountForUser(userId);

        if (count < 5) {
            // New user: use content-based
            return contentStrategy.recommend(userId, limit, register);
        } else if (count < 20) {
            // Moderate user: mix both 60% content + 40% CF
            int cLimit = (int) Math.ceil(limit * 0.6);
            return merge(
                contentStrategy.recommend(userId, cLimit, register),
                cfStrategy.recommend(userId, limit - cLimit, register));
        } else {
            // Active user: prioritize CF 80% + 20% content
            int cfLimit = (int) Math.ceil(limit * 0.8);
            return merge(
                cfStrategy.recommend(userId, cfLimit, register),
                contentStrategy.recommend(userId, limit - cfLimit, register));
        }
    }

    private List<Movie> merge(List<Movie> primary, List<Movie> secondary) {
        Map<Long, Movie> seen = new LinkedHashMap<>();
        primary.forEach(m -> seen.put(m.getId(), m));
        secondary.forEach(m -> seen.putIfAbsent(m.getId(), m));
        return new ArrayList<>(seen.values());
    }
}
