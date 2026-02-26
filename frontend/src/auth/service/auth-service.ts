import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';
import { Router } from '@angular/router';
import { jwtDecode } from 'jwt-decode';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

    constructor(private router: Router) {}

    private apiUrl = 'http://localhost:8080/auth/login';
    private registerUrl = 'http://localhost:8080/auth/register';

    private authStatus = new BehaviorSubject<boolean>(this.isAuthenticated());
    authStatus$ = this.authStatus.asObservable();

    async login(email: string, password: string): Promise<{success: boolean; message?: string}>{
        try {
            const response = await fetch(this.apiUrl, {
                method: 'POST',
                headers: {'Content-Type' : 'application/json'},
                body: JSON.stringify({email: email, password: password}),
            });

            const data = await response.json();

            if(!response.ok){
              const errorMessage = data.message || this.getErrorMessage(response.status);
              throw new Error(errorMessage);
            }

            if (!data.accessToken) {
              throw new Error('Token não recebido do servidor');
            }

            localStorage.setItem('token', data.accessToken);
            this.authStatus.next(true);
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
        this.authStatus.next(false);
        localStorage.removeItem('token');
        this.router.navigate(['']);
    }

    getToken(): string | null {
        return localStorage.getItem('token');
    }

    getUserRole(): string {
      const token = this.getToken();
      if (!token) return '';
      
      try {
        const decoded: any = jwtDecode(token);
        return decoded.role || '';
      } catch (error) {
        console.error('Erro ao decodificar token:', error);
        return '';
      }
    }

    isAdmin(): boolean {
      return this.getUserRole() === 'ADMIN';
    }

    async register(userData: any): Promise<{success: boolean; message?: string}> {
        try {
            const response = await fetch(this.registerUrl, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(userData),
            });

            if (!response.ok) {
              const data = await response.json().catch(() => ({}));

              return { 
                success: false, 
                message: data.detail || 'Erro no servidor' 
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

    getRole(): string {
      const token = this.getToken();
      if (!token) return '';

      try {
        const payload = JSON.parse(atob(token.split('.')[1]));
        return payload.role || ''; 
      } catch (e) {
        console.error('Erro ao decodificar o token:', e);
        return '';
      }
    }

}