package com.example.movierecommender.strategy;

import com.example.movierecommender.model.Movie;
import com.example.movierecommender.repository.RatingRegister;

import java.util.List;

public interface RecommendationStrategy {
    List<Movie> recommend(long userId, int limit, RatingRegister register);
    String name();
}