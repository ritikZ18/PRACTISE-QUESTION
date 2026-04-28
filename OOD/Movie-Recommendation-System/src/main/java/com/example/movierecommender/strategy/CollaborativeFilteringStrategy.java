package com.example.movierecommender.strategy;

import com.example.movierecommender.model.Movie;
import com.example.movierecommender.repository.RatingRegister;

import java.util.*;
import java.util.stream.Collectors;

public class CollaborativeFilteringStrategy implements RecommendationStrategy {

    @Override
    public String name() {
        return "collaborative-filtering";
    }

    @Override
    public List<Movie> recommend(long userId, int limit, RatingRegister register) {
        Map<Long, Double> targetVector = register.getRatingVector(userId);
        
        if (targetVector.isEmpty()) {
            return List.of();
        }

        Set<Long> alreadyWatched = register.getUser(userId)
            .map(u -> new HashSet<>(u.getWatchHistory()))
            .orElse(new HashSet<>());

        List<Long> similarUsers = register.getAllUsers().stream()
            .filter(u -> u.getId() != userId)
            .map(u -> Map.entry(u.getId(),
                cosineSimilarity(targetVector, register.getRatingVector(u.getId()))))
            .filter(e -> e.getValue() > 0)
            .sorted(Map.Entry.<Long, Double>comparingByValue().reversed())
            .limit(5)
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());

        Map<Long, Double> candidateScores = new LinkedHashMap<>();
        for (long simUserId : similarUsers) {
            register.getRatingVector(simUserId).forEach((movieId, score) -> {
                if (!alreadyWatched.contains(movieId) && score >= 4.0) {
                    candidateScores.merge(movieId, score, Double::sum);
                }
            });
        }

        return candidateScores.entrySet().stream()
            .sorted(Map.Entry.<Long, Double>comparingByValue().reversed())
            .limit(limit)
            .map(e -> register.getMovie(e.getKey()))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .collect(Collectors.toList());
    }

    private double cosineSimilarity(Map<Long, Double> v1, Map<Long, Double> v2) {
        double dot = 0, norm1 = 0, norm2 = 0;
        for (Map.Entry<Long, Double> e : v1.entrySet()) {
            dot += e.getValue() * v2.getOrDefault(e.getKey(), 0.0);
            norm1 += e.getValue() * e.getValue();
        }
        for (double val : v2.values()) {
            norm2 += val * val;
        }
        if (norm1 == 0 || norm2 == 0) {
            return 0;
        }
        return dot / (Math.sqrt(norm1) * Math.sqrt(norm2));
    }
}
