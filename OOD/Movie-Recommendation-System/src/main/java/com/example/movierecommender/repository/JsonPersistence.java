package com.example.movierecommender.repository;

import com.example.movierecommender.model.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.Year;
import java.util.*;
import java.util.stream.Collectors;

import org.json.JSONObject;
import org.json.JSONArray;

public class JsonPersistence {

    private static final String DATA_DIR = "data/";
    private static final String CATALOG_DIR = DATA_DIR + "catalog/";
    private static final String USERS_DIR = DATA_DIR + "users/";
    private static final String RATINGS_DIR = DATA_DIR + "ratings/";
    private static final String META_DIR = DATA_DIR + "meta/";

    public JsonPersistence() {
        createDirectories();
    }

    private void createDirectories() {
        try {
            Files.createDirectories(Paths.get(CATALOG_DIR));
            Files.createDirectories(Paths.get(USERS_DIR));
            Files.createDirectories(Paths.get(RATINGS_DIR));
            Files.createDirectories(Paths.get(META_DIR));
        } catch (IOException e) {
            System.err.println("Error creating data directories: " + e.getMessage());
        }
    }

    // ── READ ────────────────────────────────────────────────────────

    public void loadAll(RatingRegister register) {
        try {
            loadMovies(register);
            loadUsers(register);
            loadRatings(register);
        } catch (Exception e) {
            System.err.println("Error loading data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadMovies(RatingRegister register) throws IOException {
        Path indexPath = Paths.get(CATALOG_DIR + "movies_index.json");
        if (!Files.exists(indexPath)) {
            System.out.println("No movies index found");
            return;
        }

        String content = Files.readString(indexPath);
        JSONObject index = new JSONObject(content);
        JSONObject movieIndex = index.getJSONObject("index");

        Set<String> files = new HashSet<>();
        for (String key : movieIndex.keySet()) {
            files.add(movieIndex.getString(key));
        }

        for (String file : files) {
            Path movieFile = Paths.get(CATALOG_DIR + file);
            if (Files.exists(movieFile)) {
                loadMoviesFromFile(movieFile, register);
            }
        }
    }

    private void loadMoviesFromFile(Path filePath, RatingRegister register) throws IOException {
        String content = Files.readString(filePath);
        JSONObject movieFile = new JSONObject(content);
        JSONArray movies = movieFile.getJSONArray("movies");

        for (int i = 0; i < movies.length(); i++) {
            JSONObject movieJson = movies.getJSONObject(i);
            Movie movie = jsonToMovie(movieJson);
            register.addMovie(movie);
        }
    }

    private void loadUsers(RatingRegister register) throws IOException {
        Path usersPath = Paths.get(USERS_DIR + "users.json");
        if (!Files.exists(usersPath)) {
            return;
        }

        String content = Files.readString(usersPath);
        JSONObject data = new JSONObject(content);
        JSONObject usersJson = data.getJSONObject("users");

        for (String userId : usersJson.keySet()) {
            JSONObject userJson = usersJson.getJSONObject(userId);
            User user = jsonToUser(userJson);
            register.addUser(user);
        }
    }

    private void loadRatings(RatingRegister register) throws IOException {
        int currentYear = Year.now().getValue();
        for (int y = currentYear - 1; y <= currentYear + 1; y++) {
            Path ratingPath = Paths.get(RATINGS_DIR + "ratings_" + y + ".json");
            if (Files.exists(ratingPath)) {
                loadRatingsFromFile(ratingPath, register);
            }
        }
    }

    private void loadRatingsFromFile(Path filePath, RatingRegister register) throws IOException {
        String content = Files.readString(filePath);
        JSONObject data = new JSONObject(content);
        JSONObject ratings = data.getJSONObject("ratings");

        for (String userId : ratings.keySet()) {
            JSONObject userRatings = ratings.getJSONObject(userId);
            for (String movieId : userRatings.keySet()) {
                JSONObject ratingEntry = userRatings.getJSONObject(movieId);
                double score = ratingEntry.getDouble("score");
                MovieRating rating = MovieRating.fromDouble(score);
                try {
                    register.addRating(Long.parseLong(userId), Long.parseLong(movieId), rating);
                } catch (IllegalArgumentException e) {
                    // Skip if user or movie doesn't exist
                }
            }
        }
    }

    // ── WRITE ───────────────────────────────────────────────────────

    public void saveAll(RatingRegister register) {
        try {
            saveMovies(register.getAllMoviesMap());
            saveUsers(register.getAllUsersMap());
            saveRatings(register);
            refreshStats(register);
        } catch (IOException e) {
            System.err.println("Error saving data: " + e.getMessage());
        }
    }

    private void saveMovies(Map<Long, Movie> movies) throws IOException {
        // Group movies by primary genre
        Map<String, List<Movie>> byGenre = new HashMap<>();
        for (Movie movie : movies.values()) {
            String genre = movie.getGenres().isEmpty() 
                ? "MISC" 
                : movie.getGenres().get(0).toString();
            byGenre.computeIfAbsent(genre, k -> new ArrayList<>()).add(movie);
        }

        Map<String, String> index = new HashMap<>();
        for (Map.Entry<String, List<Movie>> entry : byGenre.entrySet()) {
            String filename = "movies_" + entry.getKey().toLowerCase() + ".json";
            saveMovieGenreFile(filename, entry.getValue());
            for (Movie m : entry.getValue()) {
                index.put(String.valueOf(m.getId()), filename);
            }
        }

        // Save index
        JSONObject indexObj = new JSONObject();
        indexObj.put("version", "1.0");
        indexObj.put("lastUpdated", Instant.now().toString());
        indexObj.put("index", index);
        indexObj.put("totalMovies", movies.size());
        Files.writeString(Paths.get(CATALOG_DIR + "movies_index.json"), indexObj.toString(2));
    }

    private void saveMovieGenreFile(String filename, List<Movie> movies) throws IOException {
        JSONObject genreFile = new JSONObject();
        String genre = movies.isEmpty() ? "MISC" : movies.get(0).getGenres().get(0).toString();
        genreFile.put("genre", genre);

        JSONArray moviesArray = new JSONArray();
        for (Movie m : movies) {
            moviesArray.put(movieToJson(m));
        }
        genreFile.put("movies", moviesArray);

        Files.writeString(Paths.get(CATALOG_DIR + filename), genreFile.toString(2));
    }

    private void saveUsers(Map<Long, User> users) throws IOException {
        JSONObject usersData = new JSONObject();
        usersData.put("version", "1.0");

        JSONObject usersObj = new JSONObject();
        long maxId = 0;
        for (User u : users.values()) {
            usersObj.put(String.valueOf(u.getId()), userToJson(u));
            maxId = Math.max(maxId, u.getId());
        }
        usersData.put("users", usersObj);
        usersData.put("nextId", maxId + 1);

        Files.writeString(Paths.get(USERS_DIR + "users.json"), usersData.toString(2));
    }

    private void saveRatings(RatingRegister register) throws IOException {
        Map<Integer, JSONObject> ratingsByYear = new HashMap<>();
        int currentYear = Year.now().getValue();

        for (Map.Entry<Long, Map<Long, MovieRating>> userEntry : 
             register.getUserRatingsMap().entrySet()) {
            long userId = userEntry.getKey();
            JSONObject userRatings = new JSONObject();

            for (Map.Entry<Long, MovieRating> ratingEntry : userEntry.getValue().entrySet()) {
                long movieId = ratingEntry.getKey();
                MovieRating rating = ratingEntry.getValue();

                JSONObject ratingObj = new JSONObject();
                ratingObj.put("score", rating.getValue());
                ratingObj.put("ratedAt", Instant.now().toString());
                userRatings.put(String.valueOf(movieId), ratingObj);
            }

            JSONObject yearData = ratingsByYear.computeIfAbsent(currentYear, k -> {
                JSONObject obj = new JSONObject();
                obj.put("year", k);
                obj.put("ratings", new JSONObject());
                return obj;
            });
            yearData.getJSONObject("ratings").put(String.valueOf(userId), userRatings);
        }

        for (Map.Entry<Integer, JSONObject> entry : ratingsByYear.entrySet()) {
            Files.writeString(
                Paths.get(RATINGS_DIR + "ratings_" + entry.getKey() + ".json"),
                entry.getValue().toString(2)
            );
        }
    }

    public void appendRating(long userId, long movieId, double score) throws IOException {
        int year = Year.now().getValue();
        Path ratingPath = Paths.get(RATINGS_DIR + "ratings_" + year + ".json");

        JSONObject data;
        if (Files.exists(ratingPath)) {
            data = new JSONObject(Files.readString(ratingPath));
        } else {
            data = new JSONObject();
            data.put("year", year);
            data.put("ratings", new JSONObject());
        }

        JSONObject ratings = data.getJSONObject("ratings");
        JSONObject userRatings = ratings.optJSONObject(String.valueOf(userId));
        if (userRatings == null) {
            userRatings = new JSONObject();
            ratings.put(String.valueOf(userId), userRatings);
        }

        JSONObject ratingEntry = new JSONObject();
        ratingEntry.put("score", score);
        ratingEntry.put("ratedAt", Instant.now().toString());
        userRatings.put(String.valueOf(movieId), ratingEntry);

        Files.writeString(ratingPath, data.toString(2));
    }

    private void refreshStats(RatingRegister register) throws IOException {
        JSONObject stats = new JSONObject();
        stats.put("lastRefreshed", Instant.now().toString());

        JSONObject movieStats = new JSONObject();
        for (Movie m : register.getAllMovies()) {
            JSONObject mStats = new JSONObject();
            mStats.put("avgRating", m.getAverageRating());
            mStats.put("ratingCount", m.getRatingCount());
            movieStats.put(String.valueOf(m.getId()), mStats);
        }
        stats.put("movieStats", movieStats);

        Files.writeString(Paths.get(META_DIR + "stats.json"), stats.toString(2));
    }

    // ── JSON CONVERSION ─────────────────────────────────────────────

    private JSONObject movieToJson(Movie m) {
        JSONObject obj = new JSONObject();
        obj.put("id", m.getId());
        obj.put("title", m.getTitle());
        obj.put("year", m.getYear());
        obj.put("director", m.getDirector());
        obj.put("runtime", m.getRuntime());
        obj.put("language", m.getLanguage());
        obj.put("description", m.getDescription());
        obj.put("posterUrl", m.getPosterUrl());
        obj.put("genres", new JSONArray(m.getGenres().stream().map(Enum::toString).toList()));
        obj.put("avgRating", m.getAverageRating());
        obj.put("ratingCount", m.getRatingCount());
        return obj;
    }

    private Movie jsonToMovie(JSONObject obj) {
        List<Genre> genres = new ArrayList<>();
        JSONArray genreArray = obj.getJSONArray("genres");
        for (int i = 0; i < genreArray.length(); i++) {
            String raw = genreArray.getString(i);
            String norm = raw.trim().toUpperCase().replace('-', '_').replace(' ', '_');
            try {
                genres.add(Genre.valueOf(norm));
            } catch (IllegalArgumentException e) {
                System.err.println("Unknown genre skipped: " + raw);
            }
        }

        return new Movie(
            obj.getLong("id"),
            obj.getString("title"),
            obj.getInt("year"),
            genres,
            obj.getString("director"),
            obj.getString("description"),
            obj.getString("language"),
            obj.getInt("runtime"),
            obj.getString("posterUrl")
        );
    }

    private JSONObject userToJson(User u) {
        JSONObject obj = new JSONObject();
        obj.put("id", u.getId());
        obj.put("name", u.getName());
        obj.put("preferredGenres", new JSONArray(u.getPreferredGenres().stream().map(Enum::toString).toList()));
        obj.put("watchHistory", new JSONArray(u.getWatchHistory()));
        return obj;
    }

    private User jsonToUser(JSONObject obj) {
        Set<Genre> genres = EnumSet.noneOf(Genre.class);
        JSONArray genreArray = obj.getJSONArray("preferredGenres");
        for (int i = 0; i < genreArray.length(); i++) {
            String raw = genreArray.getString(i);
            String norm = raw.trim().toUpperCase().replace('-', '_').replace(' ', '_');
            try {
                genres.add(Genre.valueOf(norm));
            } catch (IllegalArgumentException e) {
                System.err.println("Unknown preferred genre skipped for user: " + raw);
            }
        }

        return new User(
            obj.getLong("id"),
            obj.getString("name"),
            genres
        );
    }
}
