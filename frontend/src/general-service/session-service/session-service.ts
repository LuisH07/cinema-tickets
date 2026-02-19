import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class SessionService {
  private baseUrl = 'http://localhost:3000/sessoes';

  async getSessions(): Promise<any[]> {
    const response = await fetch(this.baseUrl);
    return response.json();
  }

  async getSessionById(id: number): Promise<any> {
    const response = await fetch(`${this.baseUrl}/${id}`);
    return response.json();
  }

}
