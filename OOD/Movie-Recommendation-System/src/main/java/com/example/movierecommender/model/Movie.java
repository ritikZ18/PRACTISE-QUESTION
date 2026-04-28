package com.example.movierecommender.model;

import java.util.List;
import java.util.Objects;

public class Movie {
    private final long id;
    private final String title;
    private final int year;
    private final List<Genre> genres;
    private final String director;
    private final String description;
    private final String language;
    private final int runtime;
    private final String posterUrl;
    private double averageRating;
    private int ratingCount;

    public Movie(long id, String title, int year, List<Genre> genres, 
                 String director, String description, String language, 
                 int runtime, String posterUrl) {
        this.id = id;
        this.title = title;
        this.year = year;
        this.genres = List.copyOf(genres);
        this.director = director;
        this.description = description;
        this.language = language;
        this.runtime = runtime;
        this.posterUrl = posterUrl;
        this.averageRating = 0.0;
        this.ratingCount = 0;
    }

    public void updateAverageRating(double newScore) {
        averageRating = ((averageRating * ratingCount) + newScore) / (++ratingCount);
    }

    public long getId() { return id; }
    public String getTitle() { return title; }
    public int getYear() { return year; }
    public List<Genre> getGenres() { return genres; }
    public String getDirector() { return director; }
    public String getDescription() { return description; }
    public String getLanguage() { return language; }
    public int getRuntime() { return runtime; }
    public String getPosterUrl() { return posterUrl; }
    public double getAverageRating() { return averageRating; }
    public int getRatingCount() { return ratingCount; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Movie m)) return false;
        return id == m.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("Movie{id=%d, title='%s', year=%d, runtime=%d, avg=%.1f, ratingCount=%d}",
                id, title, year, runtime, averageRating, ratingCount);
    }
}