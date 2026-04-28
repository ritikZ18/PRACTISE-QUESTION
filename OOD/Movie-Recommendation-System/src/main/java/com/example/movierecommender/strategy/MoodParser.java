package com.example.movierecommender.strategy;

import com.example.movierecommender.model.Mood;

import java.util.Map;
import java.util.Optional;

public class MoodParser {
    private static final Map<String, Mood> KEYWORDS = Map.ofEntries(
        Map.entry("happy", Mood.HAPPY),
        Map.entry("fun", Mood.HAPPY),
        Map.entry("laugh", Mood.HAPPY),
        Map.entry("cheerful", Mood.HAPPY),
        Map.entry("smile", Mood.HAPPY),
        Map.entry("cry", Mood.SAD),
        Map.entry("sad", Mood.SAD),
        Map.entry("emotional", Mood.SAD),
        Map.entry("down", Mood.SAD),
        Map.entry("depressed", Mood.SAD),
        Map.entry("scary", Mood.SCARED),
        Map.entry("scary", Mood.SCARED),
        Map.entry("horror", Mood.SCARED),
        Map.entry("afraid", Mood.SCARED),
        Map.entry("terrified", Mood.SCARED),
        Map.entry("exciting", Mood.THRILLED),
        Map.entry("thrilling", Mood.THRILLED),
        Map.entry("action", Mood.THRILLED),
        Map.entry("intense", Mood.THRILLED),
        Map.entry("adrenaline", Mood.THRILLED),
        Map.entry("think", Mood.MIND_BLOWN),
        Map.entry("mind", Mood.MIND_BLOWN),
        Map.entry("philosophical", Mood.MIND_BLOWN),
        Map.entry("puzzle", Mood.MIND_BLOWN),
        Map.entry("complex", Mood.MIND_BLOWN),
        Map.entry("relax", Mood.RELAXED),
        Map.entry("chill", Mood.RELAXED),
        Map.entry("calm", Mood.RELAXED),
        Map.entry("peaceful", Mood.RELAXED),
        Map.entry("soothing", Mood.RELAXED),
        Map.entry("inspired", Mood.INSPIRED),
        Map.entry("inspiration", Mood.INSPIRED),
        Map.entry("motivate", Mood.INSPIRED),
        Map.entry("uplifting", Mood.INSPIRED),
        Map.entry("educational", Mood.INSPIRED)
    );

    public static Optional<Mood> parse(String input) {
        if (input == null || input.trim().isEmpty()) {
            return Optional.empty();
        }

        String lower = input.toLowerCase().trim();
        return KEYWORDS.entrySet().stream()
            .filter(e -> lower.contains(e.getKey()))
            .map(Map.Entry::getValue)
            .findFirst();
    }

    public static Mood parseOrDefault(String input, Mood defaultMood) {
        return parse(input).orElse(defaultMood);
    }
}
