import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class CheckoutService {
  private readonly baseUrl = `${environment.apiUrl}`;

  private getHeaders() {
    const token = localStorage.getItem('token');
    return {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    };
  }

  async processarPagamento(dados: any): Promise<any> {
    const response = await fetch(`${this.baseUrl}/pagamentos/processar`, {
      method: 'POST',
      headers: this.getHeaders(),
      body: JSON.stringify(dados)
    });

    if (!response.ok) {
      const errorData = await response.json();
      throw new Error(errorData.mensagem || 'Falha ao processar pagamento');
    }

    return response.json();
  }
}