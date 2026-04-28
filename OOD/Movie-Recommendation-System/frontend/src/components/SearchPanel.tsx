import React, { useState } from 'react';
import { GENRES } from '../types';
import './SearchPanel.css';

interface SearchPanelProps {
  onSearch: (feeling: string, minRating: number, genres: string[], language?: string) => void;
  isLoading: boolean;
  availableLanguages?: string[];
}

export const SearchPanel: React.FC<SearchPanelProps> = ({ onSearch, isLoading, availableLanguages }) => {
  const [feeling, setFeeling] = useState('');
  const [minRating, setMinRating] = useState(0);
  const [selectedGenres, setSelectedGenres] = useState<string[]>([]);
  const [language, setLanguage] = useState<string>('ANY');

  const handleSearch = (event?: React.FormEvent) => {
    event?.preventDefault();
    onSearch(feeling, minRating, selectedGenres, language === 'ANY' ? undefined : language);
  };

  const handleFeelingKeyDown = (event: React.KeyboardEvent<HTMLTextAreaElement>) => {
    if (event.key === 'Enter' && !event.shiftKey) {
      event.preventDefault();
      handleSearch();
    }
  };

  const toggleGenre = (genre: string) => {
    setSelectedGenres((prev) =>
      prev.includes(genre) ? prev.filter((g) => g !== genre) : [...prev, genre]
    );
  };

  return (
    <form className="search-panel" onSubmit={handleSearch}>
      <div className="filters-section">
        <div className="filters-head">
          <h3>Search & Filters</h3>
          <p className="filters-subtitle">One update button. Mix text + genres + rating + language.</p>
        </div>

        <div className="filter-group">
          <label htmlFor="feeling">Mood / keywords</label>
          <textarea
            id="feeling"
            placeholder="Try: romance hindi drama rating:4 fun"
            value={feeling}
            onChange={(e) => setFeeling(e.target.value)}
            onKeyDown={handleFeelingKeyDown}
            rows={3}
          />
          <p className="hint">Examples: romantic, scary, mind-blowing, relaxing, inspiring…</p>
        </div>

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
          <label>Language</label>
          <select
            className="language-select"
            value={language}
            onChange={(e) => setLanguage(e.target.value)}
          >
            <option value="ANY">Any</option>
            {(availableLanguages?.length ? availableLanguages : ['en', 'hi', 'ja', 'ko', 'fr', 'es', 'ru', 'zh']).map((lang) => (
              <option key={lang} value={lang}>
                {lang.toUpperCase()}
              </option>
            ))}
          </select>
        </div>

        <div className="filter-group">
          <label>Genres</label>
          <div className="genres-grid">
            {GENRES.map((genre) => (
              <button
                key={genre}
                type="button"
                className={`genre-pill ${selectedGenres.includes(genre) ? 'active' : ''}`}
                onClick={() => toggleGenre(genre)}
              >
                {genre}
              </button>
            ))}
          </div>
        </div>

        <div className="filters-actions">
          <button className="search-button" type="submit" disabled={isLoading}>
            {isLoading ? 'Updating…' : 'Update Results'}
          </button>
          <button
            className="ghost-button"
            type="button"
            disabled={isLoading}
            onClick={() => {
              setFeeling('');
              setMinRating(0);
              setSelectedGenres([]);
              setLanguage('ANY');
            }}
          >
            Reset
          </button>
        </div>
      </div>
    </form>
  );
};
