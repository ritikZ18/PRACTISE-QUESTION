import React, { useState } from 'react';
import { Movie } from '../types';
import { api } from '../api';
import { LazyPoster } from './LazyPoster';
import './MovieCard.css';

interface MovieCardProps {
  movie: Movie;
  currentUserId: number;
  onRate?: (movieId: number, rating: number) => void;
  compact?: boolean;
}

export const MovieCard: React.FC<MovieCardProps> = ({ movie, currentUserId, onRate, compact }) => {
  const [rating, setRating] = useState<number | null>(null);
  const [isRating, setIsRating] = useState(false);

  const handleRate = async (stars: number) => {
    setIsRating(true);
    try {
      await api.rateMovie(currentUserId, movie.id, stars);
      setRating(stars);
      onRate?.(movie.id, stars);
    } catch (error) {
      console.error('Failed to rate movie:', error);
    } finally {
      setIsRating(false);
    }
  };

  return (
    <div className={`movie-card ${compact ? 'compact' : ''}`}>
      <div className="movie-poster">
        <LazyPoster posterUrl={movie.posterUrl} title={movie.title} year={movie.year} language={movie.language} />
        <div className="movie-rating-badge">
          <span className="rating-value">
            ⭐ {movie.ratingCount > 0 ? movie.avgRating.toFixed(1) : '—'}
          </span>
          {movie.ratingCount > 0 && <span className="rating-count">({movie.ratingCount})</span>}
        </div>
      </div>

      <div className="movie-info">
        <h3 className="movie-title">{movie.title}</h3>
        {compact && (
          <p className="movie-compact-meta">
            <span>{movie.year || '—'}</span>
            <span className="dot">•</span>
            <span>{movie.ratingCount > 0 ? `⭐ ${movie.avgRating.toFixed(1)}` : '⭐ —'}</span>
          </p>
        )}
        {!compact && (
          <>
            <p className="movie-meta">
              <span>{movie.year || '—'}</span>
              <span>•</span>
              <span>{movie.runtime ? `${movie.runtime} min` : '—'}</span>
            </p>

            {movie.director && <p className="movie-director">{movie.director}</p>}
          </>
        )}

        <div className="genres">
          {Array.from(new Set(movie.genres)).slice(0, compact ? 1 : 2).map((genre) => (
            <span key={genre} className="genre-tag">
              {genre}
            </span>
          ))}
          {!compact && movie.genres.length > 2 && (
            <span className="genre-tag more">+{Array.from(new Set(movie.genres)).length - 2}</span>
          )}
        </div>

        {!compact && (
          <p className="movie-description">
            {(movie.description || '').substring(0, 120)}
            {(movie.description || '').length > 120 ? '…' : ''}
          </p>
        )}

        {compact && (
          <p className="movie-compact-description">
            {(movie.description || '').substring(0, 90)}
            {(movie.description || '').length > 90 ? '…' : ''}
          </p>
        )}

        <div className="rating-stars">
          {[1, 2, 3, 4, 5].map((stars) => (
            <button
              key={stars}
              className={`star ${rating && rating >= stars ? 'active' : ''} ${isRating ? 'disabled' : ''}`}
              onClick={() => handleRate(stars)}
              disabled={isRating}
              title={`Rate ${stars} stars`}
            >
              ★
            </button>
          ))}
        </div>

        {rating && <p className="rating-saved">✓ Rated {rating} stars</p>}
      </div>
    </div>
  );
};
