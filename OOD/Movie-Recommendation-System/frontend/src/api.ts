import axios, { AxiosInstance } from 'axios';
import { Movie, User, Rating, SearchFilters } from './types';

const API_BASE = process.env.REACT_APP_API_URL || 'http://localhost:8080/api';

class MovieAPI {
  private client: AxiosInstance;

  constructor() {
    this.client = axios.create({
      baseURL: API_BASE,
      headers: {
        'Content-Type': 'application/json',
        'Access-Control-Allow-Origin': '*',
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
    const params: any = { userId, feeling };
    if (filters?.minRating) params.minRating = filters.minRating;
    if (filters?.language) params.language = filters.language;

    const response = await this.client.get('/search', { params });
    return response.data;
  }

  async getTrending(limit: number = 10): Promise<Movie[]> {
    const response = await this.client.get('/trending', {
      params: { limit },
    });
    return response.data;
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
