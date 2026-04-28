#!/bin/bash
# Movie Recommendation System - Quick Start Script

echo "🎬 Movie Recommendation System - Quick Start"
echo "============================================="
echo ""

# Check if we have Java
if ! command -v java &> /dev/null; then
    echo "❌ Java is not installed. Please install Java 11+"
    exit 1
fi

# Check if we have Node
if ! command -v node &> /dev/null; then
    echo "❌ Node.js is not installed. Please install Node 16+"
    exit 1
fi

echo "✅ Java and Node detected"
echo ""

# Option 1: Run Backend
echo "Option 1: Run Backend API Server"
echo "Run this to start the Java API server on port 8080:"
echo ""
echo "  cd src/main/java/com/example/movierecommender/api"
echo "  javac -cp .:org.json.jar APIServer.java"
echo "  java -cp .:org.json.jar APIServer"
echo ""
echo "Or run the demo:"
echo "  cd src/main/java/com/example/movierecommender"
echo "  javac -cp .:org.json.jar Main.java"
echo "  java -cp .:org.json.jar Main"
echo ""
echo "---"
echo ""

# Option 2: Run Frontend
echo "Option 2: Run Frontend (Vite + React)"
echo "Run this to start the dev frontend on port 3000:"
echo ""
echo "  cd frontend"
echo "  npm install"
echo "  npm run dev"
echo ""
echo "TMDB Integration:"
echo "Set your TMDB API key in the environment before running the importer or starting the server if you want TMDB features:" 
echo "  export TMDB_API_KEY=your_tmdb_api_key_here"
echo "Example: fetch a movie and print mapped JSON:" 
echo "  cd src/main/java && java -cp ../../../out:../../../lib/json.jar com.example.movierecommender.tmdb.TmdbImporterMain 550"
echo "---"
echo ""

echo "🎯 Full Setup:"
echo "1. Terminal 1: Run the backend API server ↑"
echo "2. Terminal 2: Run the frontend dev server ↑"
echo "3. Open http://localhost:3000 in your browser"
echo ""
echo "✨ You're ready to go!"
echo ""
echo "📚 For detailed documentation, see README.md"
