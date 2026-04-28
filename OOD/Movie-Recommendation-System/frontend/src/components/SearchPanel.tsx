import React, { useState } from 'react';
import { GENRES } from '../types';
import './SearchPanel.css';

interface SearchPanelProps {
  onSearch: (feeling: string, minRating: number, genres: string[]) => void;
  isLoading: boolean;
}

export const SearchPanel: React.FC<SearchPanelProps> = ({ onSearch, isLoading }) => {
  const [feeling, setFeeling] = useState('');
  const [minRating, setMinRating] = useState(3);
  const [selectedGenres, setSelectedGenres] = useState<string[]>([]);

  const handleSearch = () => {
    onSearch(feeling, minRating, selectedGenres);
  };

  const toggleGenre = (genre: string) => {
    setSelectedGenres((prev) =>
      prev.includes(genre) ? prev.filter((g) => g !== genre) : [...prev, genre]
    );
  };

  return (
    <div className="search-panel">
      <div className="feeling-box">
        <label htmlFor="feeling">🎯 How are you feeling?</label>
        <textarea
          id="feeling"
          placeholder="e.g., I want something mind-blowing and philosophical..."
          value={feeling}
          onChange={(e) => setFeeling(e.target.value)}
          rows={3}
        />
        <p className="hint">✨ Try keywords like: happy, scary, exciting, relaxing, inspiring, sad...</p>
      </div>

      <div className="filters-section">
        <h3>Filters</h3>

        <div className="filter-group">
          <label>Minimum Rating</label>
          <div className="rating-slider">
            <input
              type="range"
              min="0"
              max="5"
              step="0.5"
              value={minRating}
              onChange={(e) => setMinRating(parseFloat(e.target.value))}
            />
            <span className="rating-value">⭐ {minRating.toFixed(1)}+</span>
          </div>
        </div>

        <div className="filter-group">
          <label>Genres</label>
          <div className="genres-grid">
            {GENRES.map((genre) => (
              <label key={genre} className="genre-checkbox">
                <input
                  type="checkbox"
                  checked={selectedGenres.includes(genre)}
                  onChange={() => toggleGenre(genre)}
                />
                <span>{genre}</span>
              </label>
            ))}
          </div>
        </div>

        <button
          className="search-button"
          onClick={handleSearch}
          disabled={isLoading}
        >
          {isLoading ? '🔄 Searching...' : '🔍 Find Movies'}
        </button>
      </div>
    </div>
  );
};
