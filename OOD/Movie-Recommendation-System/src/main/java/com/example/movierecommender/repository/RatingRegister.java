package com.example.movierecommender.repository;

import com.example.movierecommender.model.Movie;
import com.example.movierecommender.model.MovieRating;
import com.example.movierecommender.model.User;
import com.example.movierecommender.model.WatchEvent;

import java.util.*;

public class RatingRegister {

    // userId → (movieId → rating)
    private final Map<Long, Map<Long, MovieRating>> userRatings = new HashMap<>();
    // movieId → list of ratings
    private final Map<Long, List<MovieRating>> movieRatings = new HashMap<>();
    private final Map<Long, User> users = new HashMap<>();
    private final Map<Long, Movie> movies = new HashMap<>();
    private final List<WatchEvent> watchEvents = new ArrayList<>();

    public void addUser(User user) {
        users.put(user.getId(), user);
    }

    public void addMovie(Movie movie) {
        movies.put(movie.getId(), movie);
    }

    public void addRating(long userId, long movieId, MovieRating rating) {
        if (!users.containsKey(userId)) {
            throw new IllegalArgumentException("Unknown user: " + userId);
        }
        if (!movies.containsKey(movieId)) {
            throw new IllegalArgumentException("Unknown movie: " + movieId);
        }

        userRatings.computeIfAbsent(userId, k -> new HashMap<>()).put(movieId, rating);
        movieRatings.computeIfAbsent(movieId, k -> new ArrayList<>()).add(rating);
        movies.get(movieId).updateAverageRating(rating.getValue());
        users.get(userId).addToWatchHistory(movieId);
    }

    public void recordWatchEvent(WatchEvent event) {
        watchEvents.add(event);
        if (event.isSignificantWatch()) {
            users.get(event.getUserId()).addToWatchHistory(event.getMovieId());
        }
    }

    public Map<Long, Double> getRatingVector(long userId) {
        Map<Long, Double> vector = new HashMap<>();
        userRatings.getOrDefault(userId, Map.of())
            .forEach((movieId, r) -> vector.put(movieId, r.getValue()));
        return vector;
    }

    public int getRatingCountForUser(long userId) {
        return userRatings.getOrDefault(userId, Map.of()).size();
    }

    public Optional<MovieRating> getRating(long userId, long movieId) {
        return Optional.ofNullable(
            userRatings.getOrDefault(userId, Map.of()).get(movieId));
    }

    public Collection<User> getAllUsers() {
        return Collections.unmodifiableCollection(users.values());
    }

    public Collection<Movie> getAllMovies() {
        return Collections.unmodifiableCollection(movies.values());
    }

    public Optional<User> getUser(long id) {
        return Optional.ofNullable(users.get(id));
    }

    public Optional<Movie> getMovie(long id) {
        return Optional.ofNullable(movies.get(id));
    }

    public Map<Long, Movie> getAllMoviesMap() {
        return Collections.unmodifiableMap(movies);
    }

    public Map<Long, User> getAllUsersMap() {
        return Collections.unmodifiableMap(users);
    }

    public Map<Long, Map<Long, MovieRating>> getUserRatingsMap() {
        return Collections.unmodifiableMap(userRatings);
    }
}