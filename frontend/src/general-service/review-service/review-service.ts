import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { AuthService } from '../../auth/service/auth-service';

@Injectable({
  providedIn: 'root',
})
export class ReviewService {

  private readonly baseUrl = `${environment.apiUrl}`;

  constructor(private authService: AuthService) {}

  async avaliarTicket(ticketId: string, nota: number): Promise<any> {
    const token = this.authService.getToken();

    const response = await fetch(`${this.baseUrl}/avaliacoes`, {
      method: 'POST',
      headers: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        ticketId: ticketId,
        nota: nota
      })
    });

    if (!response.ok) {
      const errorData = await response.json().catch(() => null);
      throw new Error(errorData?.message || 'Erro ao enviar avaliação');
    }

    return await response.json();
  }
}