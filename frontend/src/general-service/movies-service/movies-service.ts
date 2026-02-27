import { Injectable } from '@angular/core';
import { MovieModel } from '../../app/core/models/movie.model';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class MoviesService {
  private readonly baseUrl = `${environment.apiUrl}/filmes`;

  async getMovies(): Promise<MovieModel[]> {
    const response = await fetch(this.baseUrl);
    return response.json();
  }

  async getMoviesById(id: number): Promise<MovieModel> {
    const response = await fetch(`${this.baseUrl}/${id}`);
    return response.json();
  }
}
