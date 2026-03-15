import { Component, ElementRef, ViewChild, AfterViewInit, ChangeDetectorRef } from '@angular/core';
import { TicketValidationService } from '../../general-service/ticket-validation-service/ticket-validation-service';
import { HistoricoItem, ValidationResponse } from '../../app/core/models/ticket-validation.model';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../auth/service/auth-service';

@Component({
  selector: 'app-ticket-validation',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './ticket-validation.html',
  styleUrls: ['./ticket-validation.css']
})
export class TicketValidation implements AfterViewInit {
  @ViewChild('voucherInput') voucherInput!: ElementRef;
  
  codigoVoucher: string = '';
  resultado: ValidationResponse | null = null;
  erroNaoEncontrado: boolean = false;
  carregando: boolean = false;
  historico: HistoricoItem[] = [];

  constructor(
    private ticketService: TicketValidationService, 
    private authService: AuthService, 
    private cdr: ChangeDetectorRef
  ) {}

  ngAfterViewInit(): void {
    setTimeout(() => this.voucherInput.nativeElement.focus(), 0);
  }

  aoDigitar(): void {
    this.codigoVoucher = this.codigoVoucher.toUpperCase();
  }

  validarIngresso(): void {
    console.log('Passo 1: Iniciando validação para o código:', this.codigoVoucher);
    if (!this.codigoVoucher.trim()) return;

    this.carregando = true;
    this.resultado = null;
    this.erroNaoEncontrado = false;

    const token = this.authService.getToken() || '';
    console.log('Passo 2: Chamando o serviço com o token');

    this.ticketService.validarIngresso(this.codigoVoucher, token).subscribe({
     next: (response) => {
        console.log('Passo 3 [SUCESSO]:', response);
        this.carregando = false;

        try {
          if (response.valido) {
            this.resultado = response;
            
            if (response.dados_ingresso) {
              const assentosFormatados = Array.isArray(response.dados_ingresso.assentos)
                ? response.dados_ingresso.assentos.join(', ')
                : response.dados_ingresso.assentos || 'Nenhum';

              this.historico.unshift({
                codigo: this.codigoVoucher,
                filme: response.dados_ingresso.filme,
                assentos: assentosFormatados
              });
            }
            this.codigoVoucher = ''; 
          } else {
            const msg = response.mensagem?.toLowerCase() || '';
            if (msg.includes('não encontrado') || msg.includes('inválido')) {
              this.erroNaoEncontrado = true;
            } else {
              this.resultado = response; 
            }
          }
        } catch (e) {
          console.error('Erro interno ao ler a resposta:', e);
        }

        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Passo 3 [ERRO]: Falha capturada pelo Angular', err);
        this.carregando = false;
      }
    });
  }
}