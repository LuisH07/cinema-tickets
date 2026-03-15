import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { ValidationRequest, ValidationResponse } from '../../app/core/models/ticket-validation.model';

@Injectable({ providedIn: 'root' })
export class TicketValidationService {

  private apiUrl = `${environment.apiUrl}/ingressos/validacoes`; 

  constructor(private http: HttpClient) {}

  validarIngresso(codigo: string, token: string): Observable<ValidationResponse> {
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
    
    const body: ValidationRequest = { codigo_voucher: codigo };

    return this.http.post<ValidationResponse>(this.apiUrl, body, { headers });
  }
}