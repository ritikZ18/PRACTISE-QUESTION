package com.example.movierecommender.api;

import com.example.movierecommender.model.*;
import com.example.movierecommender.repository.JsonPersistence;
import com.example.movierecommender.repository.RatingRegister;
import com.example.movierecommender.service.MovieRecommender;
import com.example.movierecommender.service.SearchService;
import com.example.movierecommender.tmdb.TmdbChartsService;
import com.example.movierecommender.tmdb.TmdbPosterLookup;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class APIServer {
    private final RatingRegister register;
    private final SearchService searchService;
    private final MovieRecommender recommender;
    private final JsonPersistence persistence;
    private final Map<String, String> posterCache = new LinkedHashMap<>() {
        @Override
        protected boolean removeEldestEntry(Map.Entry<String, String> eldest) {
            return size() > 500;
        }
    };

    public APIServer() {
        this.register = new RatingRegister();
        this.persistence = new JsonPersistence();
        this.persistence.loadAll(register);
        this.searchService = new SearchService(register);
        this.recommender = new MovieRecommender(register);
    }

    public void start(int port) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        // CORS setup
        server.createContext("/", new CorsHandler());
        server.createContext("/api/movies", new MoviesHandler());
        server.createContext("/api/users", new UsersHandler());
        server.createContext("/api/recommend", new RecommendHandler());
        server.createContext("/api/search", new SearchHandler());
        server.createContext("/api/rate", new RateHandler());
        server.createContext("/api/trending", new TrendingHandler());
        server.createContext("/api/mood", new MoodHandler());
        server.createContext("/api/poster", new PosterHandler());

        server.setExecutor(null);
        server.start();
        System.out.println("🚀 API Server running on http://localhost:" + port);
    }

    private boolean handleOptions(HttpExchange exchange) throws IOException {
        if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
            exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
            exchange.sendResponseHeaders(204, -1);
            exchange.close();
            return true;
        }
        return false;
    }

    private class CorsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
            exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");
            exchange.sendResponseHeaders(204, -1);
        }
    }

    private class MoviesHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (handleOptions(exchange)) return;
            if ("GET".equals(exchange.getRequestMethod())) {
                JSONArray movies = new JSONArray();
                for (Movie m : register.getAllMovies()) {
                    movies.put(movieToJson(m));
                }
                sendJson(exchange, movies.toString());
            }
        }
    }

    private class UsersHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (handleOptions(exchange)) return;
            if ("GET".equals(exchange.getRequestMethod())) {
                JSONArray users = new JSONArray();
                for (User u : register.getAllUsers()) {
                    users.put(userToJson(u));
                }
                sendJson(exchange, users.toString());
            }
        }
    }

    private class RecommendHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (handleOptions(exchange)) return;
            if ("GET".equals(exchange.getRequestMethod())) {
                String query = exchange.getRequestURI().getQuery();
                long userId = Long.parseLong(getParam(query, "userId", "1"));
                int limit = Integer.parseInt(getParam(query, "limit", "10"));

                List<Movie> recs = recommender.recommend(userId, limit);
                JSONArray result = new JSONArray();
                for (Movie m : recs) {
                    result.put(movieToJson(m));
                }
                sendJson(exchange, result.toString());
            }
        }
    }

    private class SearchHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (handleOptions(exchange)) return;
            if ("GET".equals(exchange.getRequestMethod())) {
                try {
                    String query = exchange.getRequestURI().getQuery();
                    long userId = Long.parseLong(getParam(query, "userId", "1"));
                    String feeling = getParam(query, "feeling", "");

                    Filter baseFilter = Filter.builder()
                        .minRating(Double.parseDouble(getParam(query, "minRating", "0")))
                        .build();

                    String genresParam = getParam(query, "genres", "");
                    if (!genresParam.isBlank()) {
                        for (String rawGenre : genresParam.split(",")) {
                            String normalized = rawGenre.trim();
                            if (normalized.isEmpty()) {
                                continue;
                            }
                            try {
                                baseFilter = Filter.builder()
                                    .genres(baseFilter.getGenres())
                                    .genre(Genre.valueOf(normalized.toUpperCase()))
                                    .yearFrom(baseFilter.getYearFrom())
                                    .yearTo(baseFilter.getYearTo())
                                    .minRating(baseFilter.getMinRating())
                                    .language(baseFilter.getLanguage())
                                    .maxRuntime(baseFilter.getMaxRuntime())
                                    .director(baseFilter.getDirector())
                                    .sortBy(baseFilter.getSortBy())
                                    .build();
                            } catch (IllegalArgumentException ignored) {
                                // Skip unknown genre names from the client.
                            }
                        }
                    }

                    List<Movie> results = searchService.search(userId, feeling, baseFilter);
                    JSONArray result = new JSONArray();
                    for (Movie m : results) {
                        result.put(movieToJson(m));
                    }
                    sendJson(exchange, result.toString());
                } catch (Exception e) {
                    sendError(exchange, e.getMessage());
                }
            }
        }
    }

    private class RateHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (handleOptions(exchange)) return;
            if ("POST".equals(exchange.getRequestMethod())) {
                String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                JSONObject json = new JSONObject(body);

                long userId = json.getLong("userId");
                long movieId = json.getLong("movieId");
                double rating = json.getDouble("rating");

                try {
                    register.addRating(userId, movieId, MovieRating.fromDouble(rating));
                    persistence.appendRating(userId, movieId, rating);

                    JSONObject response = new JSONObject();
                    response.put("success", true);
                    response.put("message", "Rating recorded");
                    sendJson(exchange, response.toString());
                } catch (Exception e) {
                    sendError(exchange, e.getMessage());
                }
            }
        }
    }

    private class TrendingHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (handleOptions(exchange)) return;
            if ("GET".equals(exchange.getRequestMethod())) {
                int limit = Integer.parseInt(getParam(exchange.getRequestURI().getQuery(), "limit", "10"));
                JSONObject payload = new JSONObject();

                // Community trending (from local ratings)
                List<Movie> community = searchService.getTrending(limit);
                JSONArray communityJson = new JSONArray();
                for (Movie m : community) {
                    communityJson.put(movieToJson(m));
                }
                payload.put("communityTopRated", communityJson);

                // TMDB charts are optional (requires TMDB_API_KEY)
                JSONArray tmdbTrending = new JSONArray();
                JSONArray tmdbTopRated = new JSONArray();
                try {
                    TmdbChartsService charts = new TmdbChartsService();
                    for (Movie m : charts.trendingMovies("week", limit)) {
                        tmdbTrending.put(movieToJson(m));
                    }
                    for (Movie m : charts.topRatedMovies(limit)) {
                        tmdbTopRated.put(movieToJson(m));
                    }
                } catch (Exception ignored) {
                    // If TMDB isn't configured or is unavailable, just omit chart content.
                }

                payload.put("tmdbTrending", tmdbTrending);
                payload.put("tmdbTopRated", tmdbTopRated);

                sendJson(exchange, payload.toString());
            }
        }
    }

    private class PosterHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (handleOptions(exchange)) return;
            if (!"GET".equals(exchange.getRequestMethod())) {
                sendError(exchange, "Method not allowed");
                return;
            }

            try {
                String query = exchange.getRequestURI().getQuery();
                String title = decodeParam(getParam(query, "title", ""));
                String yearRaw = getParam(query, "year", "");
                String language = decodeParam(getParam(query, "language", "en-US"));

                Integer year = null;
                if (yearRaw != null && !yearRaw.isBlank()) {
                    try {
                        year = Integer.parseInt(yearRaw.trim());
                    } catch (NumberFormatException ignored) {
                        year = null;
                    }
                }

                String cacheKey = (title == null ? "" : title.trim().toLowerCase()) + "|" + (year == null ? "" : year);
                synchronized (posterCache) {
                    if (posterCache.containsKey(cacheKey)) {
                        JSONObject out = new JSONObject();
                        out.put("posterUrl", posterCache.get(cacheKey));
                        sendJson(exchange, out.toString());
                        return;
                    }
                }

                String posterUrl = "";
                try {
                    TmdbPosterLookup lookup = new TmdbPosterLookup();
                    Optional<String> found = lookup.findPosterUrl(title, year, language);
                    posterUrl = found.orElse("");
                } catch (Exception ignored) {
                    posterUrl = "";
                }

                synchronized (posterCache) {
                    if (posterUrl != null && !posterUrl.isBlank()) {
                        posterCache.put(cacheKey, posterUrl);
                    }
                }

                JSONObject out = new JSONObject();
                out.put("posterUrl", posterUrl);
                sendJson(exchange, out.toString());
            } catch (Exception e) {
                sendError(exchange, e.getMessage());
            }
        }
    }

    private class MoodHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (handleOptions(exchange)) return;
            if ("GET".equals(exchange.getRequestMethod())) {
                String moodStr = getParam(exchange.getRequestURI().getQuery(), "mood", "HAPPY");
                int limit = Integer.parseInt(getParam(exchange.getRequestURI().getQuery(), "limit", "10"));

                try {
                    Mood mood = Mood.valueOf(moodStr.toUpperCase());
                    List<Movie> results = searchService.getByMood(mood, limit);
                    JSONArray result = new JSONArray();
                    for (Movie m : results) {
                        result.put(movieToJson(m));
                    }
                    sendJson(exchange, result.toString());
                } catch (Exception e) {
                    sendError(exchange, "Invalid mood: " + moodStr);
                }
            }
        }
    }

    // ──── UTILITIES ────

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

    private JSONObject userToJson(User u) {
        JSONObject obj = new JSONObject();
        obj.put("id", u.getId());
        obj.put("name", u.getName());
        obj.put("preferredGenres", new JSONArray(u.getPreferredGenres().stream().map(Enum::toString).toList()));
        obj.put("watchHistory", new JSONArray(u.getWatchHistory()));
        return obj;
    }

    private void sendJson(HttpExchange exchange, String json) throws IOException {
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        byte[] response = json.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(200, response.length);
        exchange.getResponseBody().write(response);
        exchange.close();
    }

    private void sendError(HttpExchange exchange, String error) throws IOException {
        JSONObject obj = new JSONObject();
        obj.put("error", error);
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        byte[] response = obj.toString().getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(400, response.length);
        exchange.getResponseBody().write(response);
        exchange.close();
    }

    private String getParam(String query, String name, String def) {
        if (query == null) return def;
        for (String param : query.split("&")) {
            String[] pair = param.split("=");
            if (pair[0].equals(name)) {
                return pair.length > 1 ? pair[1] : def;
            }
        }
        return def;
    }

    private String decodeParam(String value) {
        if (value == null) return null;
        try {
            return URLDecoder.decode(value, StandardCharsets.UTF_8);
        } catch (Exception ignored) {
            return value;
        }
    }

    public static void main(String[] args) throws IOException {
        APIServer server = new APIServer();
        server.start(8080);
    }
}
