import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class NotificationService {
  private readonly baseUrl = `${environment.apiUrl}/notificacoes`;

  // Gera um token local apenas para associar a notificação ao navegador atual
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

      // Busca o token tanto no localStorage quanto no sessionStorage
      let authToken = localStorage.getItem('token') || sessionStorage.getItem('token');

      // Remove possíveis aspas extras caso o token tenha sido salvo com JSON.stringify
      if (authToken && authToken.startsWith('"')) {
        authToken = authToken.replace(/^"|"$/g, '');
      }
      
      if (!authToken) {
        console.error('🔍 Chaves no Local Storage:', Object.keys(localStorage));
        console.error('🔍 Chaves no Session Storage:', Object.keys(sessionStorage));
        throw new Error('Usuário não autenticado. Token não encontrado.');
      }

      const payload = { ...dados, deviceToken: localToken };

      const response = await fetch(`${this.baseUrl}/agendar`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${authToken}`, // Adiciona o token JWT no cabeçalho
        },
        body: JSON.stringify(payload),
      });

      if (!response.ok) {
        const errorData = await response.text();
        console.error('❌ Erro 400 do backend. Detalhes:', errorData);
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
      const localToken = this.getDeviceToken();

      let authToken = localStorage.getItem('token') || sessionStorage.getItem('token');
      if (authToken && authToken.startsWith('"')) authToken = authToken.replace(/^"|"$/g, '');

      const response = await fetch(`${this.baseUrl}/${localToken}`, {
        method: 'GET',
        headers: {
          'Authorization': `Bearer ${authToken}`
        }
      });

      if (!response.ok) return [];
      return response.json();
    } catch (error) {
      console.error('Erro ao listar notificações', error);
      return [];
    }
  }

  async marcarComoVista(id: number): Promise<void> {
    try {
      let authToken = localStorage.getItem('token') || sessionStorage.getItem('token');
      if (authToken && authToken.startsWith('"')) authToken = authToken.replace(/^"|"$/g, '');

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
