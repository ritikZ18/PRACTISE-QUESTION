package com.example.movierecommender.strategy;

import com.example.movierecommender.model.Filter;
import com.example.movierecommender.model.Genre;
import com.example.movierecommender.model.Movie;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class FilterEngine {

    /**
     * Apply all filters in sequence to a collection of movies
     */
    public List<Movie> apply(Collection<Movie> movies, Filter filter) {
        if (movies == null || movies.isEmpty()) {
            return List.of();
        }

        return movies.stream()
            .filter(byGenre(filter))
            .filter(byYearRange(filter))
            .filter(byMinRating(filter))
            .filter(byLanguage(filter))
            .filter(byRuntime(filter))
            .filter(byMood(filter))
            .filter(byDirector(filter))
            .sorted(sortBy(filter))
            .collect(Collectors.toList());
    }

    private Predicate<Movie> byGenre(Filter f) {
        return m -> f.getGenres().isEmpty()
            || m.getGenres().stream().anyMatch(f.getGenres()::contains);
    }

    private Predicate<Movie> byYearRange(Filter f) {
        return m -> m.getYear() >= f.getYearFrom()
            && m.getYear() <= f.getYearTo();
    }

    private Predicate<Movie> byMinRating(Filter f) {
        return m -> m.getAverageRating() >= f.getMinRating();
    }

    private Predicate<Movie> byLanguage(Filter f) {
        String lang = f.getLanguage();
        if (lang == null || lang.isEmpty()) {
            return m -> true;
        }
        return m -> m.getLanguage() != null
            && m.getLanguage().equalsIgnoreCase(lang);
    }

    private Predicate<Movie> byRuntime(Filter f) {
        return m -> m.getRuntime() <= f.getMaxRuntime();
    }

    private Predicate<Movie> byMood(Filter f) {
        if (f.getMood() == null) {
            return m -> true;
        }
        List<Genre> moodGenres = f.getMood().getGenres();
        return m -> m.getGenres().stream().anyMatch(moodGenres::contains);
    }

    private Predicate<Movie> byDirector(Filter f) {
        String director = f.getDirector();
        if (director == null || director.isEmpty()) {
            return m -> true;
        }
        return m -> m.getDirector() != null
            && m.getDirector().toLowerCase().contains(director.toLowerCase());
    }

    private Comparator<Movie> sortBy(Filter f) {
        return switch (f.getSortBy()) {
            case RATING -> Comparator.comparingDouble(Movie::getAverageRating)
                .thenComparingInt(Movie::getRatingCount).reversed();
            case YEAR -> Comparator.comparingInt(Movie::getYear).reversed();
            case TITLE -> Comparator.comparing(Movie::getTitle);
        };
    }
}
