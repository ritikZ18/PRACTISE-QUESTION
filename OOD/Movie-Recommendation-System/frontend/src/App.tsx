import React, { useState, useEffect } from 'react';
import { Movie, User } from './types';
import { api } from './api';
import { MovieCard } from './components/MovieCard';
import { SearchPanel } from './components/SearchPanel';
import './App.css';

function App() {
  const [movies, setMovies] = useState<Movie[]>([]);
  const [users, setUsers] = useState<User[]>([]);
  const [currentUserId, setCurrentUserId] = useState<number>(1);
  const [displayedMovies, setDisplayedMovies] = useState<Movie[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const [activeTab, setActiveTab] = useState<'recommendations' | 'search' | 'trending'>('recommendations');

  // Load initial data
  useEffect(() => {
    const loadData = async () => {
      try {
        const [moviesData, usersData] = await Promise.all([
          api.getMovies(),
          api.getUsers(),
        ]);
        setMovies(moviesData);
        setUsers(usersData);
        setDisplayedMovies(moviesData.slice(0, 12));
      } catch (error) {
        console.error('Failed to load data:', error);
      }
    };

    loadData();
  }, []);

  // Load recommendations for current user
  useEffect(() => {
    if (activeTab === 'recommendations') {
      loadRecommendations();
    }
  }, [currentUserId, activeTab]);

  const loadRecommendations = async () => {
    setIsLoading(true);
    try {
      const recs = await api.getRecommendations(currentUserId, 20);
      setDisplayedMovies(recs);
    } catch (error) {
      console.error('Failed to load recommendations:', error);
    } finally {
      setIsLoading(false);
    }
  };

  const loadTrending = async () => {
    setIsLoading(true);
    try {
      const trending = await api.getTrending(20);
      setDisplayedMovies(trending);
      setActiveTab('trending');
    } catch (error) {
      console.error('Failed to load trending:', error);
    } finally {
      setIsLoading(false);
    }
  };

  const handleSearch = async (feeling: string, minRating: number, genres: string[]) => {
    setIsLoading(true);
    try {
      const results = await api.search(currentUserId, feeling, { minRating });
      setDisplayedMovies(results);
      setActiveTab('search');
    } catch (error) {
      console.error('Failed to search:', error);
    } finally {
      setIsLoading(false);
    }
  };

  const handleRateMovie = (movieId: number, rating: number) => {
    // Update the displayed movies with new rating info
    setDisplayedMovies((prev) =>
      prev.map((m) =>
        m.id === movieId
          ? { ...m, ratingCount: m.ratingCount + 1 }
          : m
      )
    );
  };

  const currentUser = users.find((u) => u.id === currentUserId);

  return (
    <div className="app">
      <header className="header">
        <div className="container">
          <div className="header-content">
            <h1>🎬 Movie Recommender</h1>
            <div className="user-selector">
              <label htmlFor="user-select">👤 Select User:</label>
              <select
                id="user-select"
                value={currentUserId}
                onChange={(e) => setCurrentUserId(Number(e.target.value))}
              >
                {users.map((user) => (
                  <option key={user.id} value={user.id}>
                    {user.name}
                  </option>
                ))}
              </select>
            </div>
          </div>
          {currentUser && (
            <div className="user-info">
              <p>
                Watching: <strong>{currentUser.preferredGenres.join(', ')}</strong>
              </p>
            </div>
          )}
        </div>
      </header>

      <main className="main">
        <div className="container">
          <SearchPanel onSearch={handleSearch} isLoading={isLoading} />

          <div className="tabs">
            <button
              className={`tab ${activeTab === 'recommendations' ? 'active' : ''}`}
              onClick={() => {
                setActiveTab('recommendations');
                loadRecommendations();
              }}
            >
              ⭐ For You
            </button>
            <button
              className={`tab ${activeTab === 'trending' ? 'active' : ''}`}
              onClick={loadTrending}
            >
              🔥 Trending
            </button>
          </div>

          {isLoading ? (
            <div className="loading">
              <div className="spinner"></div>
              <p>Loading movies...</p>
            </div>
          ) : displayedMovies.length > 0 ? (
            <>
              <p className="results-info">
                {activeTab === 'recommendations' && `✨ Personalized for ${currentUser?.name}`}
                {activeTab === 'search' && '🔍 Search Results'}
                {activeTab === 'trending' && '🔥 Trending Now'}
                {' '}({displayedMovies.length} results)
              </p>
              <div className="movies-grid">
                {displayedMovies.map((movie) => (
                  <MovieCard
                    key={movie.id}
                    movie={movie}
                    currentUserId={currentUserId}
                    onRate={handleRateMovie}
                  />
                ))}
              </div>
            </>
          ) : (
            <div className="empty-state">
              <p>😅 No movies found. Try adjusting your search!</p>
            </div>
          )}
        </div>
      </main>

      <footer className="footer">
        <div className="container">
          <p>
            🎥 Movie Recommendation System • Powered by Hybrid Collaborative & Content-Based
            Filtering
          </p>
          <p style={{ fontSize: '0.875rem', marginTop: '0.5rem' }}>
            💾 Using JSON for persistence • No database required
          </p>
        </div>
      </footer>
    </div>
  );
}

export default App;
