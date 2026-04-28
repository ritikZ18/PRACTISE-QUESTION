package com.example.movierecommender.model;

import java.util.ArrayList;
import java.util.List;

public class Filter {
    private List<Genre> genres = new ArrayList<>();
    private int yearFrom = 1900;
    private int yearTo = 2030;
    private double minRating = 0.0;
    private String language;
    private int maxRuntime = Integer.MAX_VALUE;
    private Mood mood;
    private String director;
    private SortBy sortBy = SortBy.RATING;

    public enum SortBy {
        RATING, YEAR, TITLE
    }

    private Filter() {}

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final Filter f = new Filter();

        public Builder genre(Genre g) {
            f.genres.add(g);
            return this;
        }

        public Builder genres(List<Genre> genres) {
            f.genres.addAll(genres);
            return this;
        }

        public Builder yearFrom(int y) {
            f.yearFrom = y;
            return this;
        }

        public Builder yearTo(int y) {
            f.yearTo = y;
            return this;
        }

        public Builder yearRange(int from, int to) {
            f.yearFrom = from;
            f.yearTo = to;
            return this;
        }

        public Builder minRating(double r) {
            f.minRating = r;
            return this;
        }

        public Builder language(String lang) {
            f.language = lang;
            return this;
        }

        public Builder maxRuntime(int mins) {
            f.maxRuntime = mins;
            return this;
        }

        public Builder mood(Mood m) {
            f.mood = m;
            return this;
        }

        public Builder director(String dir) {
            f.director = dir;
            return this;
        }

        public Builder sortBy(SortBy s) {
            f.sortBy = s;
            return this;
        }

        public Filter build() {
            return f;
        }
    }

    // Getters
    public List<Genre> getGenres() {
        return List.copyOf(genres);
    }

    public int getYearFrom() {
        return yearFrom;
    }

    public int getYearTo() {
        return yearTo;
    }

    public double getMinRating() {
        return minRating;
    }

    public String getLanguage() {
        return language;
    }

    public int getMaxRuntime() {
        return maxRuntime;
    }

    public Mood getMood() {
        return mood;
    }

    public String getDirector() {
        return director;
    }

    public SortBy getSortBy() {
        return sortBy;
    }

    public boolean hasGenreFilter() {
        return !genres.isEmpty();
    }

    public boolean hasYearFilter() {
        return yearFrom > 1900 || yearTo < 2030;
    }

    public boolean hasDirectorFilter() {
        return director != null && !director.isEmpty();
    }

    @Override
    public String toString() {
        return String.format(
            "Filter{genres=%s, yearRange=%d-%d, minRating=%.1f, mood=%s, sortBy=%s}",
            genres, yearFrom, yearTo, minRating, mood, sortBy
        );
    }
}
