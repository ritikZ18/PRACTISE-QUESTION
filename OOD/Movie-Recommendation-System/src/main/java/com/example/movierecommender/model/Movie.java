package com.example.movierecommender.model;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

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
        this(id, title, year, genres, director, description, language, runtime, posterUrl, 0.0, 0);
    }

    public Movie(long id, String title, int year, List<Genre> genres,
                 String director, String description, String language,
                 int runtime, String posterUrl, double averageRating, int ratingCount) {
        this.id = id;
        this.title = title;
        this.year = year;
        this.genres = normalizeGenres(genres);
        this.director = director;
        this.description = description;
        this.language = language;
        this.runtime = runtime;
        this.posterUrl = normalizePosterUrl(posterUrl, title);
        this.averageRating = Math.max(0.0, Math.min(5.0, averageRating));
        this.ratingCount = Math.max(0, ratingCount);
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

    private static List<Genre> normalizeGenres(List<Genre> genres) {
        if (genres == null || genres.isEmpty()) {
            return List.of();
        }
        Set<Genre> unique = new LinkedHashSet<>();
        for (Genre genre : genres) {
            if (genre != null) {
                unique.add(genre);
            }
        }
        return List.copyOf(unique);
    }

    private static String normalizePosterUrl(String posterUrl, String title) {
        String value = posterUrl == null ? "" : posterUrl.trim();
        if (value.isEmpty()) {
            return "";
        }

        if (value.startsWith("/")) {
            return "https://image.tmdb.org/t/p/w342" + value;
        }

        if (value.startsWith("http://") || value.startsWith("https://")) {
            // Avoid external placeholder providers in the API payload (frontend will render a local SVG fallback).
            if (value.contains("via.placeholder.com")) {
                return "";
            }
            return value;
        }

        // Some ingestors store bare TMDB paths without leading slash.
        if (value.endsWith(".jpg") || value.endsWith(".png")) {
            return "https://image.tmdb.org/t/p/w342/" + value.replaceFirst("^/+", "");
        }

        return "";
    }

    private static String placeholderPoster(String title) {
        String safeTitle = title == null ? "Movie" : title.trim();
        if (safeTitle.isEmpty()) {
            safeTitle = "Movie";
        }
        String encoded = URLEncoder.encode(safeTitle, StandardCharsets.UTF_8);
        return "https://via.placeholder.com/342x513?text=" + encoded;
    }
}
