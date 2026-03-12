import { Component, OnInit, inject } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import Swal from 'sweetalert2';
import { CheckoutService } from '../../general-service/checkout-service/checkout-service';
import { CompraResumo } from '../../app/core/models/checkout.model';
import { IngressoService } from '../../general-service/ingresso-service/ingresso.service';

@Component({
  selector: 'app-checkout',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './checkout.html',
  styleUrl: './checkout.css'
})
export class Checkout implements OnInit {
  private readonly router = inject(Router);
  private readonly service = inject(CheckoutService);
  private readonly ingressoService = inject(IngressoService);
  
  informaçõesModal: any;
  compra?: CompraResumo;
  metodoPagamento: 'pix' | 'cartao_credito' | 'cartao_debito' | 'dinheiro' = 'pix';
  isProcessing = false;

  ngOnInit() {
    const data = localStorage.getItem('checkout_data');
    if (data) {
      this.compra = JSON.parse(data);
      console.log('Dados carregados no Checkout:', this.compra);
    } else {
      this.router.navigate(['/']);
    }
  }

  async processarPagamento() {
    if (this.isProcessing) return;
    this.isProcessing = true;

    const payload = {
      sessaoId: this.compra?.sessaoId,
      assentosIds: this.compra?.assentosIds,
      valorEsperado: this.compra?.valorTotal,
      metodo: this.metodoPagamento.toUpperCase(), 
      tokenPagamento: "TOKEN_PAGAMENTO_" + Math.random().toString(10).substring(7)
    };
    console.log("Payload enviado: ", payload);

    try {
      const resposta = await this.service.processarPagamento(payload);

      if(!resposta.ingressosIds){
        Swal.fire({
          icon: 'error',
          title: 'Voucher não encontrado',
        })

        return;
      }

      const dadosCompletosParaPDF = {
        filmeTitulo: this.compra?.filmeTitulo,
        salaNome: this.compra?.salaNome,
        data: this.compra?.data,
        horario: this.compra?.horario,
        assentosCodigos: this.compra?.assentosCodigos || [],
        vouchers: resposta.ingressosIds || []
      };

      console.log(resposta)

      Swal.fire({
        icon: 'success',
        title: 'Pagamento Confirmado!',
        text: 'Seu ingresso está pronto para download.',
        confirmButtonText: 'Baixar Ingresso',
        confirmButtonColor: '#c91432'
      }).then(() => {
        this.ingressoService.gerarPDF(dadosCompletosParaPDF);
        localStorage.removeItem('checkout_data'); 
        this.router.navigate(['/']); 
      });

    } catch (error: any) {
      console.error("Erro retornado: ", error)
      Swal.fire('Erro', error.message || 'Falha no processamento', 'error');
    } finally {
      this.isProcessing = false;
    }
  }
}