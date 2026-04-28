export interface Genre {
  id?: string;
  name: string;
}

export interface Movie {
  id: number;
  title: string;
  year: number;
  director: string;
  runtime: number;
  language: string;
  description: string;
  posterUrl: string;
  genres: string[];
  avgRating: number;
  ratingCount: number;
}

export interface TrendingResponse {
  tmdbTrending: Movie[];
  tmdbTopRated: Movie[];
  communityTopRated: Movie[];
}

export interface User {
  id: number;
  name: string;
  preferredGenres: string[];
  watchHistory: number[];
}

export interface Rating {
  userId: number;
  movieId: number;
  rating: number;
}

export interface SearchFilters {
  minRating: number;
  genres: string[];
  yearFrom: number;
  yearTo: number;
  language?: string;
  director?: string;
  maxRuntime?: number;
}

export interface Mood {
  type: 'HAPPY' | 'SAD' | 'THRILLED' | 'RELAXED' | 'INSPIRED' | 'SCARED' | 'MIND_BLOWN';
  genres: string[];
}

export const MOODS = {
  HAPPY: { type: 'HAPPY', genres: ['COMEDY', 'ANIMATION', 'ROMANCE'] },
  SAD: { type: 'SAD', genres: ['DRAMA', 'ROMANCE'] },
  THRILLED: { type: 'THRILLED', genres: ['THRILLER', 'ACTION', 'HORROR'] },
  RELAXED: { type: 'RELAXED', genres: ['DOCUMENTARY', 'COMEDY'] },
  INSPIRED: { type: 'INSPIRED', genres: ['DRAMA', 'DOCUMENTARY', 'SCI_FI'] },
  SCARED: { type: 'SCARED', genres: ['HORROR', 'THRILLER'] },
  MIND_BLOWN: { type: 'MIND_BLOWN', genres: ['SCI_FI', 'THRILLER', 'CRIME'] },
};

export const GENRES = [
  'ACTION',
  'ADVENTURE',
  'COMEDY',
  'DRAMA',
  'THRILLER',
  'HORROR',
  'ROMANCE',
  'SCI_FI',
  'DOCUMENTARY',
  'ANIMATION',
  'CRIME',
  'FANTASY',
];
