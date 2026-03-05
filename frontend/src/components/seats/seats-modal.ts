import { Component, Input, OnInit, Output, EventEmitter, inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SessionService } from '../../general-service/session-service/session-service';
import Swal from 'sweetalert2';

interface Seats {
  id: string;
  codigo: string;
  status: 'disponivel' | 'ocupado' | 'selecionado';
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

  @Input() sessaoId!: number;
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
    if (assento.status === 'ocupado') return;

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
    if (this.selecionados.length === 0) return;
    console.log('Assentos selecionados:', this.selecionados);
  }
}