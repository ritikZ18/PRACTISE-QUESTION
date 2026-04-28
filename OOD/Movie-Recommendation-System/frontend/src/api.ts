import axios, { AxiosInstance } from 'axios';
import { Movie, User, Rating, SearchFilters, TrendingResponse } from './types';

const API_BASE = (import.meta as any).env?.VITE_API_URL || 'http://localhost:8080/api';

class MovieAPI {
  private client: AxiosInstance;

  constructor() {
    this.client = axios.create({
      baseURL: API_BASE,
      headers: {
        'Content-Type': 'application/json',
      },
    });
  }

  async getMovies(): Promise<Movie[]> {
    const response = await this.client.get('/movies');
    return response.data;
  }

  async getUsers(): Promise<User[]> {
    const response = await this.client.get('/users');
    return response.data;
  }

  async getRecommendations(userId: number, limit: number = 10): Promise<Movie[]> {
    const response = await this.client.get('/recommend', {
      params: { userId, limit },
    });
    return response.data;
  }

  async search(
    userId: number,
    feeling: string,
    filters?: Partial<SearchFilters>
  ): Promise<Movie[]> {
    const params: any = { userId, feeling: feeling.trim() };
    if (filters?.minRating) params.minRating = filters.minRating;
    if (filters?.language) params.language = filters.language;
    if (filters?.genres?.length) params.genres = filters.genres.join(',');

    const response = await this.client.get('/search', { params });
    return response.data;
  }

  async getTrending(limit: number = 10): Promise<TrendingResponse> {
    const response = await this.client.get('/trending', {
      params: { limit },
    });
    return response.data;
  }

  async lookupPoster(title: string, year?: number, language: string = 'en-US'): Promise<string> {
    const response = await this.client.get('/poster', {
      params: { title, year, language },
    });
    return response.data?.posterUrl || '';
  }

  async getByMood(mood: string, limit: number = 10): Promise<Movie[]> {
    const response = await this.client.get('/mood', {
      params: { mood, limit },
    });
    return response.data;
  }

  async rateMovie(userId: number, movieId: number, rating: number): Promise<void> {
    await this.client.post('/rate', {
      userId,
      movieId,
      rating,
    });
  }
}

export const api = new MovieAPI();
