package com.example.movierecommender.model;

import java.time.Instant;

public class WatchEvent {
    private final long userId;
    private final long movieId;
    private final Instant timestamp;
    private final double watchedPercent;

    public WatchEvent(long userId, long movieId, double watchedPercent) {
        this.userId = userId;
        this.movieId = movieId;
        this.timestamp = Instant.now();
        this.watchedPercent = watchedPercent;
    }

    public long getUserId() {
        return userId;
    }

    public long getMovieId() {
        return movieId;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public double getWatchedPercent() {
        return watchedPercent;
    }

    public boolean isSignificantWatch() {
        return watchedPercent >= 0.5;
    }

    @Override
    public String toString() {
        return String.format("WatchEvent{userId=%d, movieId=%d, watched=%.1f%%}", 
                userId, movieId, watchedPercent * 100);
    }
}
