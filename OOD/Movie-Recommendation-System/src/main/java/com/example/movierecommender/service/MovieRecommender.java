package com.example.movierecommender.service;

import com.example.movierecommender.model.Movie;
import com.example.movierecommender.repository.RatingRegister;
import com.example.movierecommender.strategy.HybridStrategy;
import com.example.movierecommender.strategy.RecommendationStrategy;

import java.util.List;

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
        return strategy.recommend(userId, limit, register);
    }

    public List<Movie> recommend(long userId) {
        return recommend(userId, 10);
    }
}
