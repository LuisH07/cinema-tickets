import { Component, Input, OnInit, Output, EventEmitter, inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SessionService } from '../../general-service/session-service/session-service';
import { Router } from '@angular/router';
import Swal from 'sweetalert2';
import { MovieModel } from '../../app/core/models/movie.model';
import { CompraResumo } from '../../app/core/models/checkout.model';

interface Seats {
  id: string;
  codigo: string;
  status: 'disponivel' | 'vendido' | 'selecionado';
  tipo: string;
  valor: number;
}

@Component({
  selector: 'app-seats-modal',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './seats-modal.html',
  styleUrl: './seats-modal.css'
})
export class SeatsModal implements OnInit {
  private readonly service = inject(SessionService);
  private readonly cdr = inject(ChangeDetectorRef);
  private readonly router = inject(Router);

  @Input() sessaoId!: number;
  @Input() filme?: MovieModel;
  @Input() sessaoDados?: any;
  @Output() fechar = new EventEmitter<void>();

  assentos: Seats[] = [];
  selecionados: string[] = [];
  isLoading = true;

  ngOnInit() {
    this.carregarAssentos();
  }

  async carregarAssentos() {
    this.isLoading = true;
    try {
        const res = await this.service.getAssentos(this.sessaoId);
        console.log('Resposta bruta da API:', res);

        if (res && res.assentos) {
        this.assentos = res.assentos;
        } else if (Array.isArray(res)) {
        this.assentos = res;
        }

        console.log('Assentos mapeados para a variável:', this.assentos);

        if (this.assentos.length === 0) {
            console.warn('A lista de assentos retornou vazia do servidor.');
        }

    } catch (error) {
        console.error('Erro ao carregar assentos:', error);
        Swal.fire('Erro', 'Não foi possível carregar os assentos.', 'error');
    } finally {
        this.isLoading = false;
        this.cdr.detectChanges(); 
    }
    }

  toggleAssento(assento: Seats) {
    if (assento.status === 'vendido') return;

    const index = this.selecionados.indexOf(assento.codigo);

    if (index !== -1) {
        this.selecionados.splice(index, 1);
        assento.status = 'disponivel';
    } else {
        this.selecionados.push(assento.codigo);
        assento.status = 'selecionado';
    }
  }

  confirmarSelecao() {
    const dataHora = this.sessaoDados?.inicio ? this.sessaoDados.inicio.split('T') : [];
    
    const dataFormatada = dataHora[0] ? dataHora[0].split('-').reverse().join('/') : 'Data não informada';
    const horarioFormatado = dataHora[1] ? dataHora[1].substring(0, 5) : '--:--'; 

    const dadosParaCheckout: CompraResumo = {
      sessaoId: this.sessaoId,
      filmeTitulo: this.filme?.titulo || '',
      filmePoster: this.filme?.poster || '', 
      data: dataFormatada,
      salaNome: this.sessaoDados?.sala?.nome || 'Sala Padrão',
      horario: horarioFormatado,
      assentosCodigos: this.selecionados,
      assentosIds: this.assentos
        .filter(a => this.selecionados.includes(a.codigo))
        .map(a => Number(a.id)),
      valorTotal: this.assentos
        .filter(a => this.selecionados.includes(a.codigo))
        .reduce((acc, a) => acc + a.valor, 0)
    };

    localStorage.setItem('checkout_data', JSON.stringify(dadosParaCheckout));
    this.router.navigate(['/checkout']);
    this.fechar.emit();
  }

  private getPosterUrl(poster?: string): string {
    if (!poster) return '/images/placeholder.jpg';
    return poster.startsWith('/') ? poster : '/images/' + poster;
  }
}