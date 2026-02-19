import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class MoviesService {
  private baseUrl = 'http://localhost:3000/filmes';

  async getMovies(): Promise<any[]> {
    const response = await fetch(this.baseUrl);
    return response.json();
  }

  async getMoviesById(id: number): Promise<any> {
    const response = await fetch(`${this.baseUrl}/${id}`);
    return response.json();
  }
}
