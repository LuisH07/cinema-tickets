import { Component, ElementRef, ViewChild, AfterViewInit } from '@angular/core';
import { TicketValidationService } from '../../general-service/ticket-validation-service/ticket-validation-service';
import { ValidationResponse } from '../../app/core/models/ticket-validation.model';

@Component({
  selector: 'app-ticket-validation',
  templateUrl: './ticket-validation.component.html',
  styleUrls: ['./ticket-validation.component.css']
})
export class TicketValidation implements AfterViewInit {
  @ViewChild('voucherInput') voucherInput!: ElementRef;
  
  codigoVoucher: string = '';
  resultado: ValidationResponse | null = null;
  erroNaoEncontrado: boolean = false;
  carregando: boolean = false;

  constructor(private ticketService: TicketValidationService) {}

  ngAfterViewInit(): void {
    setTimeout(() => this.voucherInput.nativeElement.focus(), 0);
  }

  aoDigitar(): void {
    this.codigoVoucher = this.codigoVoucher.toUpperCase();
  }

  validarIngresso(): void {
    if (!this.codigoVoucher.trim()) return;

    this.carregando = true;
    this.resultado = null;
    this.erroNaoEncontrado = false;

    // TODO: Obter o token real do serviço de autenticação
    const token = 'token-temporario'; 

    this.ticketService.validarIngresso(this.codigoVoucher, token).subscribe({
      next: (response) => {
        this.resultado = response;
        this.carregando = false;
        this.codigoVoucher = '';
      },
      error: (err) => {
        this.erroNaoEncontrado = true;
        this.carregando = false;
      }
    });
  }
}