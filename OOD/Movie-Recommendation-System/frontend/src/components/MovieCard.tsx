import React, { useState } from 'react';
import { Movie } from '../types';
import { api } from '../api';
import './MovieCard.css';

interface MovieCardProps {
  movie: Movie;
  currentUserId: number;
  onRate?: (movieId: number, rating: number) => void;
}

export const MovieCard: React.FC<MovieCardProps> = ({ movie, currentUserId, onRate }) => {
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
    <div className="movie-card">
      <div className="movie-poster">
        <img
          src={movie.posterUrl}
          alt={movie.title}
          onError={(e) => {
            (e.target as HTMLImageElement).src = 'https://via.placeholder.com/300x450?text=' + encodeURIComponent(movie.title);
          }}
        />
        <div className="movie-rating-badge">
          <span className="rating-value">⭐ {movie.avgRating.toFixed(1)}</span>
          <span className="rating-count">({movie.ratingCount})</span>
        </div>
      </div>

      <div className="movie-info">
        <h3 className="movie-title">{movie.title}</h3>
        <p className="movie-meta">
          <span>{movie.year}</span>
          <span>•</span>
          <span>{movie.runtime} min</span>
        </p>

        <p className="movie-director">{movie.director}</p>

        <div className="genres">
          {movie.genres.slice(0, 2).map((genre) => (
            <span key={genre} className="genre-tag">
              {genre}
            </span>
          ))}
          {movie.genres.length > 2 && (
            <span className="genre-tag more">+{movie.genres.length - 2}</span>
          )}
        </div>

        <p className="movie-description">{movie.description.substring(0, 100)}...</p>

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
