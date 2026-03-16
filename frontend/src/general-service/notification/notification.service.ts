import { Injectable, inject } from '@angular/core';
import { environment } from '../../environments/environment';
import { AuthService } from '../../auth/service/auth-service';

@Injectable({
  providedIn: 'root',
})
export class NotificationService {
  private readonly baseUrl = `${environment.apiUrl}/notificacoes`;
  
  private readonly authService = inject(AuthService); 

  private getDeviceToken(): string {
    let token = localStorage.getItem('deviceToken');
    if (!token) {
      token = 'local-token-' + Math.random().toString(36).substring(2, 11);
      localStorage.setItem('deviceToken', token);
    }
    return token;
  }

  async agendarLembrete(dados: {
    ingressosIds: number[];
    minutosAntecedencia: number;
    sessaoId: number;
  }): Promise<any> {
    try {
      const localToken = this.getDeviceToken();
      
      const authToken = this.authService.getToken(); 

      if (!authToken) {
        throw new Error('Usuário não autenticado. Token não encontrado.');
      }

      const payload = { ...dados, deviceToken: localToken };

      const response = await fetch(`${this.baseUrl}/agendar`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${authToken}`, 
        },
        body: JSON.stringify(payload),
      });

      if (!response.ok) {
        const errorData = await response.text();
        console.error('Erro 400 do backend. Detalhes:', errorData);
        throw new Error('Erro ao salvar agendamento no back');
      }

      return response.json();
    } catch (error) {
      console.error('Erro no NotificationService:', error);
      throw error;
    }
  }

  async listarNotificacoes(): Promise<any[]> {
    try {
      const authToken = this.authService.getToken(); 

      if (!authToken) return [];

      const response = await fetch(`${this.baseUrl}`, {
        method: 'GET',
        headers: {
          'Authorization': `Bearer ${authToken}`
        }
      });

      if (!response.ok) {
         if (response.status === 401 || response.status === 403) return [];
         throw new Error('Erro ao buscar notificações');
      }
      
      return response.json();
    } catch (error) {
      console.error('Erro ao listar notificações', error);
      return [];
    }
  }

  async marcarComoVista(id: number): Promise<void> {
    try {
      const authToken = this.authService.getToken(); 

      if (!authToken) return;

      await fetch(`${this.baseUrl}/${id}/visto`, {
        method: 'PATCH',
        headers: {
          'Authorization': `Bearer ${authToken}`
        }
      });
    } catch (error) {
      console.error('Erro ao marcar notificação como vista', error);
    }
  }
}