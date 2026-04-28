#!/bin/bash
# Ingest a diverse, international set of movies from TMDB into data/catalog.
# Requires: export TMDB_API_KEY=...
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "$0")" && pwd)"
cd "$ROOT_DIR" || exit 1

if [ -z "${TMDB_API_KEY:-}" ]; then
  echo "Please set TMDB_API_KEY environment variable"
  exit 2
fi

PAGES="${1:-1}"

run_query() {
  local label="$1"
  local query="$2"
  echo ""
  echo "==> $label"
  ./ingest.sh discover "$query" 1 "$PAGES"
}

# Action + crowd pleasers across regions
run_query "Korea (KR) — thrillers" "with_genres=53&with_original_language=ko&sort_by=popularity.desc"
run_query "Japan (JP) — animation/adventure" "with_genres=16,12&with_original_language=ja&sort_by=popularity.desc"
run_query "India (IN) — drama/romance" "with_genres=18,10749&with_original_language=hi&sort_by=popularity.desc"
run_query "France (FR) — drama" "with_genres=18&with_original_language=fr&sort_by=popularity.desc"
run_query "Spain (ES) — crime/thriller" "with_genres=80,53&with_original_language=es&sort_by=popularity.desc"

# Genre staples (language-agnostic)
run_query "Sci-Fi — popular" "with_genres=878&sort_by=popularity.desc&language=en-US"
run_query "Horror — popular" "with_genres=27&sort_by=popularity.desc&language=en-US"
run_query "Documentaries — top rated" "with_genres=99&sort_by=vote_average.desc&vote_count.gte=500"

echo ""
echo "Done. Catalog updated in data/catalog/"

