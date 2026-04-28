package com.example.movierecommender.model;

public enum MovieRating {
    ONE_STAR(1.0), TWO_STARS(2.0), THREE_STARS(3.0),
    FOUR_STARS(4.0), FIVE_STARS(5.0);

    private final double value;

    MovieRating(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    public static MovieRating fromDouble(double score) {
        int rounded = (int) Math.round(score);
        return values()[Math.max(0, Math.min(rounded - 1, 4))];
    }
}