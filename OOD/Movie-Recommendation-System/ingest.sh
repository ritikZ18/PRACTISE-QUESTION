#!/bin/bash
# Ingest TMDB movies into data/catalog
ROOT_DIR="$(cd "$(dirname "$0")" && pwd)"
cd "$ROOT_DIR" || exit 1
if [ -z "$TMDB_API_KEY" ]; then
  echo "Please set TMDB_API_KEY environment variable"
  exit 2
fi

if [ "$1" == "ids" ]; then
  shift
  IDS="$1"
  java -cp out:lib/json.jar com.example.movierecommender.tmdb.TmdbBulkImporter "--ids $IDS"
elif [ "$1" == "discover" ]; then
  shift
  QUERY="$1"
  START_PAGE=${2:-1}
  END_PAGE=${3:-$START_PAGE}
  for ((p=START_PAGE; p<=END_PAGE; p++)); do
    echo "Ingesting discover page $p"
    java -cp out:lib/json.jar com.example.movierecommender.tmdb.TmdbBulkImporter "--discover ${QUERY}&page=${p}"
  done
else
  echo "Usage: ingest.sh ids 550,551  OR ingest.sh discover 'with_genres=28&language=en-US' [startPage endPage]"
  exit 2
fi
