import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
    private apiUrl = 'http://localhost:8080/auth'
    private registerUrl = 'http://localhost:8080/auth/register'

    async login(email: string, password: string): Promise<{success: boolean; message?: string}>{
        try {
            const response = await fetch(this.apiUrl, {
                method: 'POST',
                headers: {'Content-Type' : 'application/json'},
                body: JSON.stringify({email, password}),
            });

            const data = await response.json();

            if(!response.ok){
              const errorMessage = data.message || this.getErrorMessage(response.status);
              throw new Error(errorMessage);
            }

            if (!data.token) {
              throw new Error('Token não recebido do servidor');
            }

            localStorage.setItem('token', data.token);
            return { success: true };
        } catch(e: any) {
            return { 
              success: false, 
              message: e.message || 'Erro ao fazer login' 
            };
        }
    }

    isAuthenticated(): boolean {
      return !!this.getToken();
    }

    logout(): void {
        localStorage.removeItem('token');
    }

    getToken(): string | null {
        return localStorage.getItem('token');
    }

    async register(userData: any): Promise<{success: boolean; message?: string}> {
        try {
            const response = await fetch(this.registerUrl, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(userData),
            });

            const data = await response.json().catch(() => ({}));
            
            if (!response.ok) {
              return { 
                success: false, 
                message: data.message || data.errors?.[0]?.defaultMessage || 'Erro no servidor' 
              };
            }

            return { success: true };
        } catch (e: any) {
            return { 
                success: false, 
                message: e.message || 'Erro ao realizar cadastro' 
            };
        }
    }

    private getErrorMessage(status: number): string {
      switch(status) {
        case 400: return 'Email ou senha inválidos';
        case 401: return 'Credenciais inválidas';
        case 404: return 'Endpoint de autenticação não encontrado';
        case 500: return 'Erro interno do servidor';
        default: return 'Erro ao conectar com o servidor';
      }
    }

}