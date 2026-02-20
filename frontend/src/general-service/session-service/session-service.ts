import { Injectable } from '@angular/core';
import { SessionModel } from '../../app/core/models/session.model';

@Injectable({
  providedIn: 'root',
})
export class SessionService {
  private baseUrl = 'http://localhost:3000';

  private getHeaders() {
    const token = localStorage.getItem('token');
    return {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    };
  }

  async getSessions(): Promise<any[]> {
    const response = await fetch(`${this.baseUrl}/sessoes`);
    return response.json();
  }

  async getSessionById(id: number): Promise<any> {
    const response = await fetch(`${this.baseUrl}/sessoes/${id}`);
    return response.json();
  }

  async salvarSessao(dados: any): Promise<any> {
    const response = await fetch(
      'http://localhost:8080/sessoes', {
      method: 'POST',
      headers: this.getHeaders(),
      body: JSON.stringify(dados)
    });
    
    if (!response.ok) {
      const errorData = await response.json();
      throw errorData;
    }
    
    return response.json();
  }

  async listarFilmes(): Promise<any[]> {
    const response = await fetch(`${this.baseUrl}/filmes`, { headers: this.getHeaders() });
    return response.json();
  }

  async listarSalas(): Promise<any[]> {
    const response = await fetch(`${this.baseUrl}/salas`, { headers: this.getHeaders() });
    return response.json();
  }

}
