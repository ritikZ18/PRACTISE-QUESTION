#!/usr/bin/env bash
# start.sh - start app and optionally ingest TMDB data
# Usage:
#   ./start.sh           -> compile & ensure backend/frontend running
#   ./start.sh --ingest ids 550,278  -> run ingestion for given ids after ensuring services

set -euo pipefail
ROOT_DIR="$(cd "$(dirname "$0")" && pwd)"
LOG_DIR="$ROOT_DIR/logs"
mkdir -p "$LOG_DIR"

usage() {
  cat <<EOF
Usage: $0 [--ingest ids <id,id,...> | --ingest discover "query"]
  No args: ensure backend (8080) and frontend (3000) are running (compile if needed)
  --ingest ids <ids>      : ingest specific TMDB ids (comma separated)
  --ingest discover <q>   : run TMDB discover query string
EOF
  exit 1
}

is_port_open() {
  local port=$1
  if command -v ss >/dev/null 2>&1; then
    ss -ltn | awk '{print $4}' | grep -q ":${port}$" || ss -ltn | awk '{print $4}' | grep -q ":${port}\\b" && return 0 || return 1
  else
    netstat -ltn 2>/dev/null | awk '{print $4}' | grep -q ":${port}$" && return 0 || return 1
  fi
}

ensure_java_compile() {
  echo "[start] Ensuring Java compiled..."
  cd "$ROOT_DIR"
  find src/main/java -name "*.java" > sources.txt
  mkdir -p out lib
  if [ ! -f lib/json.jar ]; then
    echo "[start] Downloading org.json jar..."
    curl -sSL -o lib/json.jar https://repo1.maven.org/maven2/org/json/json/20230227/json-20230227.jar
  fi
  # compile only if classes are missing or sources changed
  javac -cp lib/json.jar -d out @sources.txt || { echo "[start] Java compile failed"; exit 1; }
}

start_backend() {
  echo "[start] Checking backend on port 8080..."
  if is_port_open 8080; then
    echo "[start] Backend already running"
  else
    echo "[start] Starting API server..."
    nohup java -cp out:lib/json.jar com.example.movierecommender.api.APIServer > "$LOG_DIR/backend.log" 2>&1 &
    sleep 1
    echo "[start] Backend started (logs: $LOG_DIR/backend.log)"
  fi
}

start_frontend() {
  echo "[start] Checking frontend on port 3000..."
  if is_port_open 3000; then
    echo "[start] Frontend already running"
  else
    echo "[start] Starting frontend dev server..."
    cd "$ROOT_DIR/frontend"
    if [ ! -d node_modules ]; then
      echo "[start] Installing npm dependencies..."
      npm install --no-audit --no-fund
    fi
    nohup npm run dev > "$LOG_DIR/frontend.log" 2>&1 &
    sleep 1
    echo "[start] Frontend started (logs: $LOG_DIR/frontend.log)"
  fi
}

stop_backend() {
  echo "[start] Stopping backend..."
  local pids
  pids=$(pgrep -f 'com.example.movierecommender.api.APIServer' || true)
  if [ -n "$pids" ]; then
    echo "[start] Killing backend PIDs: $pids"
    kill $pids || true
    sleep 1
  else
    local lpid
    lpid=$(lsof -t -i:8080 2>/dev/null || true)
    if [ -n "$lpid" ]; then
      echo "[start] Killing PID listening on 8080: $lpid"
      kill $lpid || true
      sleep 1
    else
      echo "[start] No backend process found"
    fi
  fi
}

stop_frontend() {
  echo "[start] Stopping frontend..."
  local pids
  pids=$(pgrep -f 'vite' || true)
  if [ -n "$pids" ]; then
    echo "[start] Killing frontend PIDs: $pids"
    kill $pids || true
    sleep 1
  else
    local lpid
    lpid=$(lsof -t -i:3000 2>/dev/null || true)
    if [ -n "$lpid" ]; then
      echo "[start] Killing PID listening on 3000: $lpid"
      kill $lpid || true
      sleep 1
    else
      echo "[start] No frontend process found"
    fi
  fi
}

stop_all() {
  stop_frontend
  stop_backend
  echo "[start] All services stopped"
}

restart_all() {
  echo "[start] Restarting services..."
  stop_all
  ensure_java_compile
  start_backend
  start_frontend
  echo "[start] Restart complete"
}

run_ingest() {
  local mode="$1"; shift
  case "$mode" in
    ids)
      local ids="$1"
      echo "[start] Ingesting IDs: $ids"
      cd "$ROOT_DIR"
      ./ingest.sh ids "$ids"
      ;;
    discover)
      local q="$1"
      echo "[start] Ingesting discover query: $q"
      cd "$ROOT_DIR"
      ./ingest.sh discover "$q"
      ;;
    *)
      echo "Unknown ingest mode: $mode"; usage
      ;;
  esac
}

# --- main
if [ "${1:-}" = "--help" ] || [ "${1:-}" = "-h" ]; then
  usage
fi
case "${1:-}" in
  --ingest)
    ensure_java_compile
    start_backend
    start_frontend
    if [ -z "${2:-}" ]; then usage; fi
    if [ "${2}" = "ids" ]; then
      run_ingest ids "${3:-}"
    elif [ "${2}" = "discover" ]; then
      # support optional start/end pages
      run_ingest discover "${3:-}"
    else
      echo "Invalid ingest option"; usage
    fi
    ;;
  --stop)
    stop_all
    ;;
  --restart)
    restart_all
    ;;
  --status)
    echo "Backend on 8080: $(is_port_open 8080 && echo up || echo down)"
    echo "Frontend on 3000: $(is_port_open 3000 && echo up || echo down)"
    ;;
  "" )
    ensure_java_compile
    start_backend
    start_frontend
    echo "[start] Services ensured. Open http://localhost:3000 and http://localhost:8080"
    ;;
  *)
    usage
    ;;
esac

