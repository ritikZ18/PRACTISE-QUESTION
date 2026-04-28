#!/bin/bash
# Start backend and frontend quickly (background)
ROOT_DIR="$(cd "$(dirname "$0")" && pwd)"
echo "Starting backend and frontend from $ROOT_DIR"

echo "Compiling Java sources..."
cd "$ROOT_DIR" || exit 1
find src/main/java -name "*.java" > sources.txt
mkdir -p out lib
if [ ! -f lib/json.jar ]; then
  echo "Downloading org.json jar..."
  curl -sSL -o lib/json.jar https://repo1.maven.org/maven2/org/json/json/20230227/json-20230227.jar
fi
javac -cp lib/json.jar -d out @sources.txt || { echo "Java compile failed"; exit 1; }

echo "Starting API server (background)"
java -cp out:lib/json.jar com.example.movierecommender.api.APIServer > backend.log 2>&1 &
BACK_PID=$!
echo "Backend PID: $BACK_PID"

echo "Starting frontend (background)"
cd frontend || exit 1
npm install --no-audit --no-fund >/dev/null 2>&1 || true
npm run dev >/dev/null 2>&1 &
FRONT_PID=$!
echo "Frontend PID: $FRONT_PID"

echo "Open http://localhost:3000 and http://localhost:8080"
echo "To stop: kill $BACK_PID $FRONT_PID"
