import React, { useMemo, useRef } from 'react';
import { Movie } from '../types';
import { MovieCard } from './MovieCard';
import './MovieRail.css';

interface MovieRailProps {
  title: string;
  subtitle?: string;
  movies: Movie[];
  currentUserId: number;
  onRate?: (movieId: number, rating: number) => void;
}

const dedupeMovies = (movies: Movie[]) => {
  const seen = new Set<string>();
  const out: Movie[] = [];
  for (const movie of movies) {
    const key = `${movie.title?.toLowerCase() || ''}|${movie.year || 0}|${movie.runtime || 0}|${movie.language || ''}`;
    if (seen.has(key)) continue;
    seen.add(key);
    out.push(movie);
  }
  return out;
};

export const MovieRail: React.FC<MovieRailProps> = ({ title, subtitle, movies, currentUserId, onRate }) => {
  const scrollerRef = useRef<HTMLDivElement | null>(null);

  const items = useMemo(() => dedupeMovies(movies), [movies]);

  const scrollBy = (delta: number) => {
    scrollerRef.current?.scrollBy({ left: delta, behavior: 'smooth' });
  };

  if (!items.length) return null;

  return (
    <section className="movie-rail">
      <div className="rail-header">
        <div className="rail-titles">
          <h2 className="rail-title">{title}</h2>
          {subtitle && <p className="rail-subtitle">{subtitle}</p>}
        </div>
        <div className="rail-actions">
          <button className="rail-nav" type="button" onClick={() => scrollBy(-680)} aria-label="Scroll left">
            ‹
          </button>
          <button className="rail-nav" type="button" onClick={() => scrollBy(680)} aria-label="Scroll right">
            ›
          </button>
        </div>
      </div>

      <div className="rail-wrap">
        <div className="rail-fade left" />
        <div className="rail-fade right" />
        <div className="rail-scroller" ref={scrollerRef}>
          {items.map((movie) => (
            <MovieCard
              key={`${title}-${movie.id}-${movie.year}-${movie.language}`}
              movie={movie}
              currentUserId={currentUserId}
              onRate={onRate}
              compact
            />
          ))}
        </div>
      </div>
    </section>
  );
};
