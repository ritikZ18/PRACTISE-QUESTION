import React, { useState, useEffect } from 'react';
import { Movie, TrendingResponse, User } from './types';
import { api } from './api';
import { MovieCard } from './components/MovieCard';
import { SearchPanel } from './components/SearchPanel';
import { FeaturedHero } from './components/FeaturedHero';
import { MovieRail } from './components/MovieRail';
import './App.css';

function App() {
  const [movies, setMovies] = useState<Movie[]>([]);
  const [users, setUsers] = useState<User[]>([]);
  const [currentUserId, setCurrentUserId] = useState<number>(1);
  const [recommendations, setRecommendations] = useState<Movie[]>([]);
  const [searchResults, setSearchResults] = useState<Movie[]>([]);
  const [trending, setTrending] = useState<TrendingResponse | null>(null);
  const [isLoading, setIsLoading] = useState(false);
  const [activeTab, setActiveTab] = useState<'home' | 'search' | 'trending'>('home');
  const [searchPageSize, setSearchPageSize] = useState(24);

  const tokenize = (value: string) =>
    value
      .toLowerCase()
      .split(/[^a-z0-9]+/)
      .filter((token) => token.length > 2);

  const normalizeLanguage = (value: string) => {
    const raw = (value || '').trim().toLowerCase();
    if (!raw) return '';
    if (raw === 'english') return 'en';
    if (raw.length === 2) return raw;
    return raw;
  };

  const parseInlineFilters = (input: string) => {
    const text = input || '';
    const ratingMatch = text.match(/rating\\s*[:=]\\s*(\\d+(?:\\.\\d+)?)/i);
    const languageMatch = text.match(/(?:lang|language)\\s*[:=]\\s*([a-z]{2,})/i);
    const countryMatch = text.match(/(?:country|region)\\s*[:=]\\s*([a-z]{2,})/i);
    return {
      rating: ratingMatch ? Number(ratingMatch[1]) : undefined,
      language: languageMatch ? languageMatch[1].toLowerCase() : undefined,
      country: countryMatch ? countryMatch[1].toLowerCase() : undefined,
    };
  };

  const searchLocally = (feeling: string, minRating: number, genres: string[], language?: string) => {
    const query = feeling.trim().toLowerCase();
    const tokens = tokenize(query);
    const selectedGenres = genres.map((genre) => genre.toUpperCase());
    const romanticQuery = /romantic|romance|love|dating|date|couple/.test(query);
    const inline = parseInlineFilters(query);
    const effectiveMinRating = inline.rating != null ? Math.max(minRating, inline.rating) : minRating;
    const effectiveLanguage = normalizeLanguage(language || inline.language || '');

    return movies
      .filter((movie) => movie.avgRating >= effectiveMinRating)
      .filter((movie) => selectedGenres.length === 0 || movie.genres.some((genre) => selectedGenres.includes(genre)))
      .filter((movie) => {
        if (!effectiveLanguage) return true;
        return normalizeLanguage(movie.language) === effectiveLanguage;
      })
      .map((movie) => {
        const haystack = [movie.title, movie.description, movie.director, movie.genres.join(' ')].join(' ').toLowerCase();
        let score = movie.avgRating * 3 + movie.ratingCount * 0.15;

        for (const token of tokens) {
          if (haystack.includes(token)) {
            score += 2;
          }
        }

        if (romanticQuery && movie.genres.includes('ROMANCE')) {
          score += 6;
        }

        if (query && haystack.includes(query)) {
          score += 4;
        }

        return { movie, score };
      })
      .filter(({ score }) => score > 0 || tokens.length === 0)
      .sort((left, right) => right.score - left.score || right.movie.avgRating - left.movie.avgRating || right.movie.ratingCount - left.movie.ratingCount)
      .map(({ movie }) => movie);
  };

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
    if (activeTab === 'home') {
      loadRecommendations();
    }
  }, [currentUserId, activeTab]);

  const loadRecommendations = async () => {
    setIsLoading(true);
    try {
      const recs = await api.getRecommendations(currentUserId, 20);
      setRecommendations(recs);
    } catch (error) {
      console.error('Failed to load recommendations:', error);
    } finally {
      setIsLoading(false);
    }
  };

  const loadTrending = async () => {
    setIsLoading(true);
    try {
      const charts = await api.getTrending(20);
      setTrending(charts);
      setActiveTab('trending');
    } catch (error) {
      console.error('Failed to load trending:', error);
    } finally {
      setIsLoading(false);
    }
  };

  const handleSearch = async (feeling: string, minRating: number, genres: string[], language?: string) => {
    setIsLoading(true);
    try {
      const results = searchLocally(feeling, minRating, genres, language);
      setSearchResults(results);
      setSearchPageSize(24);
      setActiveTab('search');
    } catch (error) {
      console.error('Failed to search:', error);
      setSearchResults(searchLocally(feeling, minRating, genres, language));
      setSearchPageSize(24);
      setActiveTab('search');
    } finally {
      setIsLoading(false);
    }
  };

  const handleRateMovie = (movieId: number, rating: number) => {
    const bump = (list: Movie[]) =>
      list.map((m) => (m.id === movieId ? { ...m, ratingCount: m.ratingCount + 1 } : m));
    setRecommendations((prev) => bump(prev));
    setSearchResults((prev) => bump(prev));
    setTrending((prev) =>
      prev
        ? {
            ...prev,
            tmdbTrending: bump(prev.tmdbTrending),
            tmdbTopRated: bump(prev.tmdbTopRated),
            communityTopRated: bump(prev.communityTopRated),
          }
        : prev
    );
  };

  const currentUser = users.find((u) => u.id === currentUserId);
  const preferred = currentUser?.preferredGenres ?? [];

  const pickByGenre = (genre: string, limit: number) =>
    movies
      .filter((m) => m.genres.includes(genre))
      .sort((a, b) => (b.year || 0) - (a.year || 0) || b.ratingCount - a.ratingCount)
      .slice(0, limit);

  const worldwidePicks = movies
    .filter((m) => {
      const lang = (m.language || '').toLowerCase();
      return lang !== 'en' && lang !== 'english';
    })
    .sort((a, b) => (b.year || 0) - (a.year || 0))
    .slice(0, 24);

  const topGenres = (list: Movie[], max: number) => {
    const counts = new Map<string, number>();
    for (const movie of list) {
      for (const genre of Array.from(new Set(movie.genres))) {
        counts.set(genre, (counts.get(genre) || 0) + 1);
      }
    }
    return Array.from(counts.entries())
      .sort((a, b) => b[1] - a[1])
      .slice(0, max)
      .map(([genre]) => genre);
  };

  return (
    <div className="app">
      <header className="header">
        <div className="container">
          <div className="header-content">
            <div className="brand">
              <span className="brand-mark">●</span>
              <span className="brand-name">CineStream</span>
            </div>

            <nav className="nav">
              <button
                className={`nav-link ${activeTab === 'home' ? 'active' : ''}`}
                type="button"
                onClick={() => setActiveTab('home')}
              >
                For You
              </button>
              <button
                className={`nav-link ${activeTab === 'trending' ? 'active' : ''}`}
                type="button"
                onClick={loadTrending}
              >
                Trending
              </button>
              <button
                className={`nav-link ${activeTab === 'search' ? 'active' : ''}`}
                type="button"
                onClick={() => {
                  setActiveTab('search');
                  document.getElementById('search')?.scrollIntoView({ behavior: 'smooth', block: 'start' });
                }}
              >
                Search
              </button>
            </nav>

            <div className="user-selector">
              <label htmlFor="user-select">Profile</label>
              <select id="user-select" value={currentUserId} onChange={(e) => setCurrentUserId(Number(e.target.value))}>
                {users.map((user) => (
                  <option key={user.id} value={user.id}>
                    {user.name}
                  </option>
                ))}
              </select>
            </div>
          </div>
        </div>
      </header>

      <main className="main">
        <div className="container">
          {activeTab === 'home' && (
            <>
              <FeaturedHero
                movie={recommendations[0] || movies[0]}
                user={currentUser}
                onShowTrending={loadTrending}
              />

              <MovieRail
                title={currentUser ? `Top picks for ${currentUser.name}` : 'Top picks'}
                subtitle={preferred.length ? `Based on your vibe: ${preferred.slice(0, 3).join(' • ')}` : undefined}
                movies={recommendations}
                currentUserId={currentUserId}
                onRate={handleRateMovie}
              />

              {preferred.slice(0, 3).map((genre) => (
                <MovieRail
                  key={genre}
                  title={`Because you like ${genre}`}
                  movies={pickByGenre(genre, 18)}
                  currentUserId={currentUserId}
                  onRate={handleRateMovie}
                />
              ))}

              <MovieRail
                title="From around the world"
                subtitle="International picks across languages & styles"
                movies={worldwidePicks}
                currentUserId={currentUserId}
                onRate={handleRateMovie}
              />
            </>
          )}

          {activeTab === 'trending' && (
            <>
              <FeaturedHero
                movie={trending?.tmdbTrending?.[0] || trending?.communityTopRated?.[0]}
                user={currentUser}
                onShowTrending={loadTrending}
              />

              {isLoading ? (
                <div className="loading">
                  <div className="spinner"></div>
                  <p>Loading charts…</p>
                </div>
              ) : (
                <>
                  <MovieRail
                    title="Trending this week on TMDB"
                    subtitle={trending?.tmdbTrending?.length ? 'Fresh releases and buzz' : 'Set TMDB_API_KEY to enable'}
                    movies={trending?.tmdbTrending || []}
                    currentUserId={currentUserId}
                    onRate={handleRateMovie}
                  />
                  <MovieRail
                    title="Top rated on TMDB"
                    movies={trending?.tmdbTopRated || []}
                    currentUserId={currentUserId}
                    onRate={handleRateMovie}
                  />
                  <MovieRail
                    title="Top rated by our users"
                    movies={trending?.communityTopRated || []}
                    currentUserId={currentUserId}
                    onRate={handleRateMovie}
                  />
                </>
              )}
            </>
          )}

          <section id="search" className="search-section">
            <div className="search-head">
              <h2>Search by mood</h2>
              <p>Describe the vibe. We’ll pull matching movies from the local catalog.</p>
            </div>
            <SearchPanel
              onSearch={handleSearch}
              isLoading={isLoading}
              availableLanguages={Array.from(
                new Set(
                  movies
                    .map((m) => normalizeLanguage(m.language))
                    .filter((v) => v && v !== 'en')
                )
              )
                .sort()
                .slice(0, 12)}
            />

            {activeTab === 'search' && (
              <>
                {isLoading ? (
                  <div className="loading">
                    <div className="spinner"></div>
                    <p>Searching…</p>
                  </div>
                ) : searchResults.length > 0 ? (
                  <>
                    <p className="results-info">Search Results ({searchResults.length})</p>

                    <MovieRail
                      title="Top matches"
                      subtitle="Hover a card to preview the synopsis"
                      movies={searchResults.slice(0, 24)}
                      currentUserId={currentUserId}
                      onRate={handleRateMovie}
                    />

                    {topGenres(searchResults, 3).map((genre) => (
                      <MovieRail
                        key={genre}
                        title={`More ${genre}`}
                        movies={searchResults.filter((m) => m.genres.includes(genre)).slice(0, 24)}
                        currentUserId={currentUserId}
                        onRate={handleRateMovie}
                      />
                    ))}

                    <div className="movies-grid">
                      {searchResults.slice(0, searchPageSize).map((movie) => (
                        <MovieCard key={`search-${movie.id}`} movie={movie} currentUserId={currentUserId} onRate={handleRateMovie} />
                      ))}
                    </div>
                    {searchResults.length > searchPageSize && (
                      <div className="load-more">
                        <button
                          className="ghost-button"
                          type="button"
                          onClick={() => setSearchPageSize((s) => Math.min(searchResults.length, s + 24))}
                        >
                          Load more
                        </button>
                      </div>
                    )}
                  </>
                ) : (
                  <div className="empty-state">
                    <p>No matches yet — try a different mood or loosen filters.</p>
                  </div>
                )}
              </>
            )}
          </section>
        </div>
      </main>

      <footer className="footer">
        <div className="container">
          <p>
            Movie Recommendation System • Powered by Hybrid Collaborative & Content-Based
            Filtering
          </p>
        </div>
      </footer>
    </div>
  );
}

export default App;
function setDisplayedMovies(arg0: Movie[]) {
    throw new Error('Function not implemented.');
}

