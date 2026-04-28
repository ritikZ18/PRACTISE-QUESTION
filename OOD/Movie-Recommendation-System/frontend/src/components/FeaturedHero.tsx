import React, { useMemo } from 'react';
import { Movie, User } from '../types';
import './FeaturedHero.css';

interface FeaturedHeroProps {
  movie?: Movie;
  user?: User;
  onShowTrending?: () => void;
}

const resolvePosterUrl = (posterUrl: string, title: string) => {
  const raw = (posterUrl || '').trim();
  if (!raw) {
    return `https://via.placeholder.com/1280x720?text=${encodeURIComponent(title || 'Movie')}`;
  }
  if (raw.startsWith('http://') || raw.startsWith('https://')) {
    return raw;
  }
  if (raw.startsWith('/')) {
    return `https://image.tmdb.org/t/p/w780${raw}`;
  }
  return raw;
};

export const FeaturedHero: React.FC<FeaturedHeroProps> = ({ movie, user, onShowTrending }) => {
  const backgroundUrl = useMemo(() => {
    if (!movie) return '';
    return resolvePosterUrl(movie.posterUrl, movie.title);
  }, [movie]);

  if (!movie) return null;

  const metaParts = [
    movie.year ? String(movie.year) : null,
    movie.runtime ? `${movie.runtime} min` : null,
    movie.language ? movie.language.toUpperCase() : null,
  ].filter(Boolean);

  return (
    <section className="featured-hero">
      <div className="hero-bg" style={{ backgroundImage: `url(${backgroundUrl})` }} />
      <div className="hero-overlay" />
      <div className="container hero-inner">
        <div className="hero-content">
          <div className="hero-badges">
            <span className="hero-pill">For You</span>
            {user?.preferredGenres?.slice(0, 3).map((g) => (
              <span key={g} className="hero-pill soft">
                {g}
              </span>
            ))}
          </div>

          <h1 className="hero-title">{movie.title}</h1>

          <div className="hero-meta">
            <span className="hero-rating">
              ⭐ {movie.ratingCount > 0 ? movie.avgRating.toFixed(1) : '—'}
              {movie.ratingCount > 0 ? ` (${movie.ratingCount})` : ''}
            </span>
            {metaParts.length > 0 && <span className="hero-dot">•</span>}
            {metaParts.map((part, idx) => (
              <React.Fragment key={part}>
                {idx > 0 && <span className="hero-dot">•</span>}
                <span className="hero-meta-item">{part}</span>
              </React.Fragment>
            ))}
          </div>

          <p className="hero-description">
            {(movie.description || '').slice(0, 220)}
            {(movie.description || '').length > 220 ? '…' : ''}
          </p>

          <div className="hero-actions">
            <button className="hero-btn primary" type="button" onClick={onShowTrending}>
              Explore Trending
            </button>
            <a className="hero-btn ghost" href="#search">
              Search by Mood
            </a>
          </div>
        </div>
      </div>
    </section>
  );
};

