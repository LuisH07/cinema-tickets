import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { AuthService } from '../../auth/service/auth-service';

@Injectable({
  providedIn: 'root',
})
export class TicketsService {

  private readonly baseUrl = `${environment.apiUrl}`;

  constructor(private authService: AuthService) {}

  async testGetTickets(): Promise<any[]> {
    const token = this.authService.getToken();

    const response = await fetch(`${this.baseUrl}/ingressos`, {
      method: 'GET',
      headers: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json'
      }
    });
    return response.json();
  }

}
