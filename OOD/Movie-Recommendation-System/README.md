# 🎬 Movie Recommendation System

A full-stack movie recommendation application using **Hybrid Collaborative & Content-Based Filtering**, built with **Java backend** and **React + Vite + TypeScript frontend**, with **JSON-based persistence** (no database required).

---

## 🎯 Features

### Backend (Java)
- ✅ **Hybrid Recommendation Engine** - Combines collaborative filtering & content-based filtering
- ✅ **Mood-Based Search** - Parse natural language input to map to moods
- ✅ **Advanced Filtering** - Genre, year range, rating, runtime, language, director
- ✅ **Rating System** - 1-5 star ratings with averaging
- ✅ **JSON Persistence** - All data stored in organized JSON files (no database)
- ✅ **RESTful API** - HTTP server for frontend communication
- ✅ **50 Pre-seeded Movies** - Across 10 genres with realistic metadata

### Frontend (React + Vite)
- ✅ **Responsive UI** - Modern dark theme with smooth animations
- ✅ **User Selection** - Switch between multiple test users
- ✅ **Movie Cards** - Beautiful cards with ratings, genres, and star rating input
- ✅ **Smart Search** - Feel-based search with keyword parsing
- ✅ **Advanced Filters** - Genre, rating, and mood-based filtering
- ✅ **Trending Tab** - See most-rated movies
- ✅ **Real-time Updates** - Rate movies and see instant updates

---

## 📁 Project Structure

```
Movie-Recommendation-System/
├── src/main/java/com/example/movierecommender/
│   ├── Main.java                          # Demo entry point
│   │
│   ├── model/
│   │   ├── Genre.java                     # Enum for movie genres
│   │   ├── Movie.java                     # Movie entity
│   │   ├── User.java                      # User entity
│   │   ├── MovieRating.java               # Rating enum (1-5 stars)
│   │   ├── WatchEvent.java                # Watch history tracking
│   │   ├── Mood.java                      # Mood enum (HAPPY, SAD, THRILLED, etc)
│   │   └── Filter.java                    # Filter builder for search
│   │
│   ├── repository/
│   │   ├── RatingRegister.java            # In-memory data store
│   │   └── JsonPersistence.java           # JSON read/write layer
│   │
│   ├── strategy/
│   │   ├── RecommendationStrategy.java    # Interface for strategies
│   │   ├── CollaborativeFilteringStrategy.java  # User-based CF
│   │   ├── ContentBasedStrategy.java      # Genre & metadata-based
│   │   ├── HybridStrategy.java            # Combines both strategies
│   │   ├── FilterEngine.java              # Advanced filtering
│   │   └── MoodParser.java                # Natural language to mood mapping
│   │
│   ├── service/
│   │   ├── MovieRecommender.java          # Main recommendation service
│   │   └── SearchService.java             # Combined search & recommendation
│   │
│   └── api/
│       └── APIServer.java                 # REST API server (port 8080)
│
├── data/
│   ├── catalog/
│   │   ├── movies_*.json                  # Movies by genre (50 total)
│   │   └── movies_index.json              # Index mapping
│   ├── users/
│   │   └── users.json                     # User profiles
│   └── ratings/
│       └── ratings_2026.json              # User ratings
│
├── frontend/
│   ├── src/
│   │   ├── main.tsx                       # Vite entry point
│   │   ├── App.tsx                        # Main React component
│   │   ├── App.css                        # App styles
│   │   ├── index.css                      # Global styles
│   │   ├── api.ts                         # API client
│   │   ├── types.ts                       # TypeScript types
│   │   │
│   │   └── components/
│   │       ├── MovieCard.tsx              # Movie display card
│   │       ├── MovieCard.css
│   │       ├── SearchPanel.tsx            # Search & filters
│   │       └── SearchPanel.css
│   │
│   ├── index.html
│   ├── package.json
│   ├── vite.config.ts
│   ├── tsconfig.json
│   └── tsconfig.node.json
│
└── README.md
```

---

## 🚀 Quick Start

### Prerequisites
- Java 11+ (with org.json library)
- Node.js 16+ & npm
- No database required! (uses JSON files)

### Backend Setup

**1. Compile & Run the API Server**

```bash
cd src/main/java/com/example/movierecommender/api

# Compile with JSON library
javac -cp .:org.json.jar:.. APIServer.java

# Run
java -cp .:org.json.jar:.. APIServer

# Server starts on http://localhost:8080
```

### TMDB Integration

If you have a TMDB API key you can fetch movie metadata directly from The Movie Database.

1. Export your key:

```bash
export TMDB_API_KEY=your_tmdb_api_key_here
```

2. Compile project as usual (see above), then run the importer CLI to fetch a TMDB movie by its ID and print mapped JSON:

```bash
cd /path/to/project
find src/main/java -name "*.java" > sources.txt
javac -cp lib/json.jar -d out @sources.txt
java -cp out:lib/json.jar com.example.movierecommender.tmdb.TmdbImporterMain 550
```

The importer prints a small JSON object combining movie metadata and director info — you can pipe the output into your catalog JSON files if desired.


**2. Or run the demo**

```bash
cd src/main/java/com/example/movierecommender

javac -cp .:org.json.jar Main.java

java -cp .:org.json.jar Main
```

This will print recommendations, trending movies, and searches for all test users.

### Frontend Setup

**1. Install dependencies**

```bash
cd frontend
npm install
```

**2. Start dev server**

```bash
npm run dev
```

Frontend will be available at `http://localhost:3000`

**3. Build for production**

```bash
npm run build
npm run preview
```

---

## 🎮 How to Use

### Testing the Backend

The system comes with **pre-loaded users**:
- Alice (Sci-Fi, Thriller)
- Bob (Action, Crime)
- Carol (Drama)
- David (Horror, Thriller)
- Emma (Comedy, Romance)
- Fiona (Documentary, Drama)
- Hiro (Animation, Adventure, Fantasy)
- Aisha (Romance, Drama)
- Mateo (Crime, Thriller)
- Zara (Horror, Thriller)
- Priya (Sci-Fi, Action)
- Chen (Comedy, Animation)
- Luca (Adventure, Action)

Each user has rated 4-5 movies, giving the hybrid algorithm training data.

### Frontend Walkthrough

1. **Select a User** - Top right dropdown to switch between Alice, Bob, Carol, David, Emma
2. **"For You" Tab** - Personalized recommendations based on user history
3. **Search with Mood** - Type feelings like:
   - "I want something mind-blowing" → Sci-Fi/Thriller recommendations
   - "Make me laugh" → Comedy recommendations
   - "Scare me" → Horror/Thriller recommendations
4. **Advanced Filters** - Adjust minimum rating, select genres, etc.
5. **Rate Movies** - Click stars on any movie card to rate (1-5 stars)
6. **Trending Tab** - See most-rated movies across all users

---

## 🔧 API Endpoints

All endpoints are prefixed with `http://localhost:8080/api`

| Endpoint | Method | Description |
|---|---|---|
| `/movies` | GET | All 50 movies in catalog |
| `/users` | GET | All registered users |
| `/recommend?userId=1&limit=10` | GET | Personalized recommendations |
| `/search?userId=1&feeling=text` | GET | Mood-based search |
| `/trending?limit=10` | GET | Charts: TMDB Trending + TMDB Top Rated + community top-rated (TMDB requires `TMDB_API_KEY`) |
| `/mood?mood=HAPPY&limit=10` | GET | Movies for specific mood |
| `/rate` | POST | Rate a movie (JSON body) |

### Rate Movie Example

```bash
curl -X POST http://localhost:8080/api/rate \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "movieId": 5,
    "rating": 4.5
  }'
```

---

## 📊 How Recommendations Work

### 1. **Hybrid Strategy**
- **< 5 ratings**: Use Content-Based (new users)
- **5-20 ratings**: Mix 60% Content + 40% Collaborative Filtering
- **> 20 ratings**: Prioritize Collaborative Filtering (80%) + Content (20%)

### 2. **Collaborative Filtering**
- Finds similar users based on rating vectors
- Uses **cosine similarity** to measure user-user relationships
- Recommends movies rated highly by similar users

### 3. **Content-Based Filtering**
- Builds a genre preference profile from user's ratings
- Recommends movies with overlapping genres
- Factors in director and movie quality

### 4. **Mood Parsing**
Maps keywords to moods:
- `HAPPY` ← "happy", "fun", "laugh", "cheerful"
- `SAD` ← "cry", "emotional", "down", "depressed"
- `THRILLED` ← "exciting", "action", "intense", "adrenaline"
- `SCARED` ← "scary", "horror", "afraid", "terrified"
- `INSPIRED` ← "think", "philosophical", "motivate", "uplifting"
- `RELAXED` ← "chill", "calm", "peaceful", "soothing"
- `MIND_BLOWN` ← "mind", "puzzle", "complex", "philosophical"

---

## 📂 JSON Data Structure

### Movies (`data/catalog/movies_*.json`)
```json
{
  "genre": "SCI_FI",
  "movies": [
    {
      "id": 1,
      "title": "Inception",
      "year": 2010,
      "director": "Christopher Nolan",
      "genres": ["SCI_FI", "THRILLER"],
      "runtime": 148,
      "language": "English",
      "description": "...",
      "posterUrl": "...",
      "avgRating": 4.7,
      "ratingCount": 0
    }
  ]
}
```

### Users (`data/users/users.json`)
```json
{
  "version": "1.0",
  "users": {
    "1": {
      "id": 1,
      "name": "Alice",
      "preferredGenres": ["SCI_FI", "THRILLER"],
      "watchHistory": [1, 2, 3, 7]
    }
  },
  "nextId": 6
}
```

### Ratings (`data/ratings/ratings_2026.json`)
```json
{
  "year": 2026,
  "ratings": {
    "1": {
      "1": { "score": 5.0, "ratedAt": "2026-01-15T10:30:00Z" },
      "2": { "score": 4.0, "ratedAt": "2026-02-20T14:45:00Z" }
    }
  }
}
```

---

## 🎓 Design Patterns Used

| Pattern | Location | Purpose |
|---|---|---|
| **Strategy Pattern** | `RecommendationStrategy` | Swappable recommendation algorithms |
| **Builder Pattern** | `Filter.java` | Fluent API for filter construction |
| **Singleton** | `api.ts` | Single API client instance |
| **Factory Pattern** | `MoodParser` | Convert string → Mood enum |
| **Repository Pattern** | `RatingRegister` + `JsonPersistence` | Data abstraction layer |
| **Facade Pattern** | `SearchService` | Unified search + recommendation interface |
| **Layered Architecture** | Package structure | Model → Repository → Strategy → Service |

---

## 🔐 SOLID Principles

- **Single Responsibility**: Each class has one job (Movie model ≠ Rating logic ≠ Storage)
- **Open/Closed**: Add new strategies without modifying existing ones
- **Liskov Substitution**: All strategies implement the same interface
- **Interface Segregation**: Strategies don't depend on unnecessary methods
- **Dependency Inversion**: Services depend on abstractions (interfaces), not concrete classes

---

## 📈 Scalability Notes

**Current Limits:**
- 50 movies (easily add more by creating new JSON files)
- 5 test users (add to `users.json`)
- In-memory storage (all data loaded at startup)

**Future Enhancements:**
- Switch from JSON to SQLite/PostgreSQL (only change `JsonPersistence.java`)
- Add web authentication with JWT tokens
- Implement caching layer (Redis)
- Add image hosting (currently using placeholder images)
- Add user-generated reviews/comments
- Implement movie watchlist feature
- Add social features (friend recommendations)

---

## 🧪 Testing the System

### Test the Hybrid Algorithm
```java
// See Main.java for examples of:
// 1. Getting recommendations for each user
// 2. Searching by mood ("mind blown")
// 3. Finding trending movies
// 4. Filtering by genre
// 5. Switching recommendation strategies
```

### Test Mood Parsing
```java
String[] testInputs = {
  "I'm feeling happy",
  "Scare me please",
  "Something philosophical",
  "I need to chill"
};

for (String input : testInputs) {
  System.out.println(MoodParser.parse(input));
}
```

### Test Collaborative Filtering
1. Alice rates Inception ⭐⭐⭐⭐⭐
2. David also rates Inception ⭐⭐⭐⭐⭐
3. Alice rates Blade Runner 2049 ⭐⭐⭐⭐
4. Get recommendations for David → Blade Runner 2049 will be recommended

---

## 🐛 Troubleshooting

**Problem**: API returns 404
- **Solution**: Make sure `APIServer` is running on port 8080

**Problem**: Frontend can't connect to backend
- **Solution**: Check CORS headers in `APIServer.java`. Should allow `*`

**Problem**: org.json not found
- **Solution**: Download org.json JAR and add to classpath

**Problem**: No recommendations appearing
- **Solution**: Check that user has rated at least 1 movie. New users get content-based results.

---

## 📚 Learning Resources

### Key Concepts Implemented
- **Cosine Similarity**: Mathematical formula for comparing user preferences
- **Collaborative Filtering**: ML technique for user-based recommendations
- **Content-Based Filtering**: Feature matching for recommendations
- **Natural Language Processing**: Simple keyword matching for mood detection
- **REST APIs**: HTTP server for client-server communication
- **JSON Serialization**: Data persistence without databases

### References
- Collaborative Filtering: https://en.wikipedia.org/wiki/Collaborative_filtering
- Cosine Similarity: https://en.wikipedia.org/wiki/Cosine_similarity
- SOLID Principles: https://en.wikipedia.org/wiki/SOLID
- Design Patterns: https://www.refactoring.guru/design-patterns

---

## 📝 License

This project is created for educational purposes as part of interview preparation.

---

## 🎯 What This Demonstrates

- ✅ **Software Architecture** - Clean layered design with separation of concerns
- ✅ **Data Structures** - Maps, sets, streams for efficient data handling
- ✅ **Algorithms** - Cosine similarity, filtering, sorting, ranking
- ✅ **Design Patterns** - Strategy, Builder, Repository, Facade patterns
- ✅ **SOLID Principles** - Applied throughout the codebase
- ✅ **Java Expertise** - Generics, lambdas, streams, exception handling
- ✅ **Frontend Development** - React, TypeScript, CSS Grid, responsive design
- ✅ **API Design** - RESTful endpoints, JSON payloads, HTTP methods
- ✅ **Testing** - Multiple demo users, various filtering scenarios
- ✅ **Documentation** - Clear code, comprehensive README

---

**Built with ❤️ for movie lovers everywhere! 🍿**
