package com.example.movierecommender.model;

import java.util.*;

public class User {
    private final long id;
    private final String name;
    private final Set<Genre> preferredGenres;
    private final List<Long> watchHistory;

    public User(long id, String name, Set<Genre> preferredGenres) {
        this.id = id;
        this.name = name;
        this.preferredGenres = EnumSet.copyOf(preferredGenres);
        this.watchHistory = new ArrayList<>();
    }

    public void addToWatchHistory(long movieId) {
        if (!watchHistory.contains(movieId)) {
            watchHistory.add(movieId);
        }
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Set<Genre> getPreferredGenres() {
        return Collections.unmodifiableSet(preferredGenres);
    }

    public List<Long> getWatchHistory() {
        return Collections.unmodifiableList(watchHistory);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User u)) return false;
        return id == u.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("User{id=%d, name='%s', preferredGenres=%s}", 
                id, name, preferredGenres);
    }
}
