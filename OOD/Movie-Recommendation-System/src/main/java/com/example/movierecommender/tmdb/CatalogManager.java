package com.example.movierecommender.tmdb;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.*;

/**
 * Simple catalog manager that writes movies into data/catalog and maintains movies_index.json
 * - Places movies into a file based on their first genre: movies_<genre>.json
 * - If a file exceeds MAX_PER_FILE movies, creates a new part file: movies_<genre>_part2.json
 * - Maintains movies_index.json mapping id -> filename and totalMovies
 */
public class CatalogManager {
    private final Path catalogDir = Path.of("data","catalog");
    private final Path indexPath = catalogDir.resolve("movies_index.json");
    private final int MAX_PER_FILE = 1000;

    private JSONObject indexJson;
    private Map<Integer, String> indexMap = new HashMap<>();
    private int totalMovies = 0;
    private Map<String, JSONObject> openFiles = new HashMap<>();

    public CatalogManager() throws Exception {
        if (!Files.exists(catalogDir)) Files.createDirectories(catalogDir);
        loadIndex();
    }

    private void loadIndex() throws Exception {
        if (!Files.exists(indexPath)) {
            indexJson = new JSONObject();
            indexJson.put("version","1.0");
            indexJson.put("lastUpdated", Instant.now().toString());
            indexJson.put("index", new JSONObject());
            indexJson.put("totalMovies", 0);
            saveIndex();
            return;
        }
        String s = Files.readString(indexPath);
        indexJson = new JSONObject(s);
        JSONObject idx = indexJson.optJSONObject("index");
        if (idx != null) {
            for (String k : idx.keySet()) indexMap.put(Integer.parseInt(k), idx.getString(k));
        }
        totalMovies = indexJson.optInt("totalMovies", indexMap.size());
    }

    private void saveIndex() throws Exception {
        indexJson.put("lastUpdated", Instant.now().toString());
        JSONObject idx = new JSONObject();
        for (var e : indexMap.entrySet()) idx.put(String.valueOf(e.getKey()), e.getValue());
        indexJson.put("index", idx);
        indexJson.put("totalMovies", totalMovies);
        try (FileWriter w = new FileWriter(indexPath.toFile())) {
            w.write(indexJson.toString(2));
        }
    }

    public synchronized void addMovie(JSONObject movie) throws Exception {
        // assign an internal id
        totalMovies++;
        int id = totalMovies;
        movie.put("id", id);

        JSONArray genres = movie.optJSONArray("genres");
        String genre = (genres != null && genres.length() > 0) ? genres.getString(0) : "DRAMA";
        String baseName = "movies_" + genre.toLowerCase();
        String filename = baseName + ".json";

        // load or create file
        JSONObject fileJson = loadOrCreateFile(filename, genre);

        // append movie
        fileJson.getJSONArray("movies").put(movie);

        // if exceeds max, split
        if (fileJson.getJSONArray("movies").length() > MAX_PER_FILE) {
            // create a new part file name
            int part = 2;
            String newName;
            do {
                newName = baseName + "_part" + part + ".json";
                part++;
            } while (Files.exists(catalogDir.resolve(newName)));
            // move overflow movies to new file
            JSONArray movies = fileJson.getJSONArray("movies");
            JSONArray keep = new JSONArray();
            JSONArray move = new JSONArray();
            for (int i = 0; i < movies.length(); i++) {
                if (i < MAX_PER_FILE) keep.put(movies.get(i)); else move.put(movies.get(i));
            }
            fileJson.put("movies", keep);
            writeFile(catalogDir.resolve(filename), fileJson);

            JSONObject newFile = new JSONObject();
            newFile.put("genre", genre);
            newFile.put("movies", move);
            writeFile(catalogDir.resolve(newName), newFile);

            // update indexMap for moved movies
            for (int i = 0; i < move.length(); i++) {
                JSONObject mv = move.getJSONObject(i);
                int mid = mv.getInt("id");
                indexMap.put(mid, newName);
            }
        } else {
            writeFile(catalogDir.resolve(filename), fileJson);
        }

        indexMap.put(id, filename);
    }

    private JSONObject loadOrCreateFile(String filename, String genre) throws Exception {
        Path p = catalogDir.resolve(filename);
        if (openFiles.containsKey(filename)) return openFiles.get(filename);
        if (Files.exists(p)) {
            String s = Files.readString(p);
            JSONObject j = new JSONObject(s);
            openFiles.put(filename, j);
            return j;
        }
        JSONObject j = new JSONObject();
        j.put("genre", genre);
        j.put("movies", new JSONArray());
        openFiles.put(filename, j);
        return j;
    }

    private void writeFile(Path p, JSONObject j) throws Exception {
        try (FileWriter w = new FileWriter(p.toFile())) { w.write(j.toString(2)); }
    }

    public void saveIndexAndVectors() throws Exception {
        saveIndex();
        // build simple vectors file
        JSONObject vectors = new JSONObject();
        for (var e : indexMap.entrySet()) {
            int id = e.getKey();
            String file = e.getValue();
            Path p = catalogDir.resolve(file);
            if (!Files.exists(p)) continue;
            String s = Files.readString(p);
            JSONObject jf = new JSONObject(s);
            JSONArray movies = jf.getJSONArray("movies");
            for (int i = 0; i < movies.length(); i++) {
                JSONObject mv = movies.getJSONObject(i);
                if (mv.getInt("id") != id) continue;
                double[] vec = computeVector(mv);
                JSONArray a = new JSONArray();
                for (double v : vec) a.put(v);
                vectors.put(String.valueOf(id), a);
            }
        }
        writeFile(catalogDir.resolve("movies_vectors.json"), vectors);
    }

    private double[] computeVector(JSONObject mv) {
        // Simple vector: genres(11 one-hot), avgRating/5, runtime/300, yearNorm(1900-2100), sentiment(-1..1 mapped 0..1)
        List<String> genres = new ArrayList<>();
        JSONArray garr = mv.optJSONArray("genres");
        if (garr != null) for (int i=0;i<garr.length();i++) genres.add(garr.getString(i));

        String[] all = Arrays.stream(com.example.movierecommender.model.Genre.values()).map(Enum::name).toArray(String[]::new);
        double[] vec = new double[all.length + 4];
        for (int i = 0; i < all.length; i++) vec[i] = genres.contains(all[i]) ? 1.0 : 0.0;
        double avg = mv.optDouble("avgRating", 0.0);
        vec[all.length] = avg / 5.0;
        double runtime = mv.optDouble("runtime", 0.0);
        vec[all.length+1] = Math.min(runtime/300.0, 1.0);
        int year = mv.optInt("year", 2000);
        vec[all.length+2] = (year - 1900) / 200.0;
        double sent = simpleSentimentScore(mv.optString("description",""));
        vec[all.length+3] = (sent + 1.0)/2.0;
        return vec;
    }

    private double simpleSentimentScore(String text) {
        if (text == null || text.isBlank()) return 0.0;
        String t = text.toLowerCase();
        String[] pos = new String[]{"good","great","amazing","wonderful","uplift","love","hero"};
        String[] neg = new String[]{"bad","boring","awful","terrible","hate","sad","angry"};
        int p=0,n=0;
        for (String s: pos) if (t.contains(s)) p++;
        for (String s: neg) if (t.contains(s)) n++;
        if (p==0 && n==0) return 0.0;
        return (p - n) / (double)(p + n);
    }
}
