package com.example.movierecommender.model;

import java.util.List;

public enum Mood {
    HAPPY(List.of(Genre.COMEDY, Genre.ANIMATION, Genre.ROMANCE)),
    SAD(List.of(Genre.DRAMA, Genre.ROMANCE)),
    THRILLED(List.of(Genre.THRILLER, Genre.ACTION, Genre.HORROR)),
    RELAXED(List.of(Genre.DOCUMENTARY, Genre.COMEDY)),
    INSPIRED(List.of(Genre.DRAMA, Genre.DOCUMENTARY, Genre.SCI_FI)),
    SCARED(List.of(Genre.HORROR, Genre.THRILLER)),
    MIND_BLOWN(List.of(Genre.SCI_FI, Genre.THRILLER, Genre.CRIME));

    private final List<Genre> mappedGenres;

    Mood(List<Genre> genres) {
        this.mappedGenres = genres;
    }

    public List<Genre> getGenres() {
        return List.copyOf(mappedGenres);
    }
}
