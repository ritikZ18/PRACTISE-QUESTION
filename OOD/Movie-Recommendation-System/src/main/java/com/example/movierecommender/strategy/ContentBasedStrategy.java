package com.example.movierecommender.strategy;

import com.example.movierecommender.model.Genre;
import com.example.movierecommender.model.Movie;
import com.example.movierecommender.repository.RatingRegister;

import java.util.*;
import java.util.stream.Collectors;

public class ContentBasedStrategy implements RecommendationStrategy {

    @Override
    public String name() {
        return "content-based";
    }

    @Override
    public List<Movie> recommend(long userId, int limit, RatingRegister register) {
        Set<Long> watched = register.getUser(userId)
            .map(u -> new HashSet<>(u.getWatchHistory()))
            .orElse(new HashSet<>());

        Map<Genre, Double> genreWeights = buildGenreProfile(userId, register);

        if (genreWeights.isEmpty()) {
            return List.of();
        }

        return register.getAllMovies().stream()
            .filter(m -> !watched.contains(m.getId()))
            .map(m -> Map.entry(m, scoreByGenres(m, genreWeights)))
            .filter(e -> e.getValue() > 0)
            .sorted(Map.Entry.<Movie, Double>comparingByValue().reversed())
            .limit(limit)
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());
    }

    private Map<Genre, Double> buildGenreProfile(long userId, RatingRegister register) {
        Map<Genre, Double> weights = new EnumMap<>(Genre.class);
        register.getRatingVector(userId).forEach((movieId, score) ->
            register.getMovie(movieId).ifPresent(movie ->
                movie.getGenres().forEach(genre ->
                    weights.merge(genre, score, Double::sum))));
        return weights;
    }

    private double scoreByGenres(Movie movie, Map<Genre, Double> genreWeights) {
        return movie.getGenres().stream()
            .mapToDouble(g -> genreWeights.getOrDefault(g, 0.0))
            .sum();
    }
}
