import { CommonModule } from '@angular/common';
import { Component, inject, OnInit, ChangeDetectorRef } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { NgSelectModule } from '@ng-select/ng-select';
import { SessionService } from '../../general-service/session-service/session-service';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-sessao',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule, NgSelectModule],
  template: `
  <div class="page-box">
    <div class="register-box"> 
      <div class="register-wrapper">
        <h1 class="auth-title">CADASTRO SESSÃO</h1>
        <form class="form-login" [formGroup]="sessaoForm" (ngSubmit)="onSubmit()">
          
          <div class="form-row">
            <div class="form-col">
              <label>Filme<span class="required">*</span></label>
              <ng-select 
                class="custom-select" 
                [items]="filmes" 
                bindLabel="titulo" 
                bindValue="id" 
                formControlName="filmeId"
                placeholder="Selecione um filme..."
                [clearable]="false">
              </ng-select>
            </div>
          </div>

          <div class="form-row">
            <div class="form-col">
              <label>Horário<span class="required">*</span></label>
              <input type="time" formControlName="horario">
            </div>
            
            <div class="form-col">
              <label>Sala<span class="required">*</span></label>
              <select formControlName="salaId">
                <option [value]="null" disabled>Selecione a sala</option>
                <option *ngFor="let s of salas" [value]="s.id">
                  Sala {{s.numero}} (Capacidade: {{s.capacidade}})
                </option>
              </select>
            </div>
          </div>

          <div class="form-row">

            <div class="form-col">
              <label>Tipo de Áudio<span class="required">*</span></label>
              <select formControlName="tipo">
                <option value="Dublado">Dublado</option>
                <option value="Legendado">Legendado</option>
              </select>
            </div>
          </div>

          <p class="section-title">PERÍODO DE EXIBIÇÃO</p>

          <div class="form-row">
            <div class="form-col">
              <label>Data Inicial<span class="required">*</span></label>
              <input type="date" formControlName="dataInicio">
            </div>
            <div class="form-col">
              <label>Data Final<span class="required">*</span></label>
              <input type="date" formControlName="dataFim">
            </div>
          </div>

          <button type="submit" [disabled]="sessaoForm.invalid || isLoading">
            <span *ngIf="!isLoading">Cadastrar sessão</span>
            <span *ngIf="isLoading">PROCESSANDO...</span>
          </button>
        </form>
      </div>
    </div>
    <div class="img-box"></div>
  </div>
  `,
  styleUrl: './sessao.css'
})

export class Sessao implements OnInit {
  private service = inject(SessionService);
  private fb = inject(FormBuilder);
  private cdr = inject(ChangeDetectorRef);

  sessaoForm: FormGroup;
  filmes: any[] = [];
  salas: any[] = [];
  isLoading = false;

  constructor() {
    this.sessaoForm = this.fb.group({
      filmeId: [null, Validators.required],
      salaId: [null, Validators.required],
      horario: ['', Validators.required],
      tipo: ['Dublado', Validators.required],
      classificacao: ['Livre', Validators.required],
      dataInicio: ['', Validators.required],
      dataFim: ['', Validators.required]
    });
  }

  // Getters para validação
  get filmeId() { return this.sessaoForm.get('filmeId'); }
  get salaId() { return this.sessaoForm.get('salaId'); }
  get horario() { return this.sessaoForm.get('horario'); }
  get dataInicio() { return this.sessaoForm.get('dataInicio'); }
  get dataFim() { return this.sessaoForm.get('dataFim'); }

  async ngOnInit() {
    try {
      const resFilmes = await this.service.listarFilmes();
      console.log('Filmes recebidos:', resFilmes); // DEBUG: Veja se aparece no console do F12
      this.filmes = resFilmes;

      const resSalas = await this.service.listarSalas();
      console.log('Salas recebidas:', resSalas);
      this.salas = resSalas;

      this.cdr.detectChanges(); // FORÇA A TELA A ATUALIZAR
    } catch (error) {
      console.error('Erro ao carregar dados:', error);
    }
  }

  async onSubmit() {
    if (this.sessaoForm.invalid) {
      this.sessaoForm.markAllAsTouched(); 
      return;
    }

    if (this.isLoading) return;

    const { dataInicio, dataFim, ...dadosForm } = this.sessaoForm.value;
    
    // Validação de data retroativa (conforme solicitado anteriormente)
    const hoje = new Date();
    hoje.setHours(0, 0, 0, 0);

    const dInicio = new Date(dataInicio + 'T00:00:00');
    const dFim = new Date(dataFim + 'T00:00:00');

    if (dInicio < hoje) {
      Swal.fire({ icon: 'warning', title: 'Data Inválida', text: 'Não é possível cadastrar sessões em datas passadas.', confirmButtonColor: '#c91432' });
      return;
    }

    if (dFim < dInicio) {
      Swal.fire({ icon: 'warning', title: 'Atenção', text: 'A data final deve ser maior ou igual à inicial.', confirmButtonColor: '#c91432' });
      return;
    }

    this.isLoading = true;
    try {
      let dataAtual = new Date(dInicio);
      
      while (dataAtual <= dFim) {
        
        const dataFormatada = dataAtual.toISOString().split('T')[0];
        
        const payload = {
          filmeId: Number(this.sessaoForm.value.filmeId), 
          salaId: Number(this.sessaoForm.value.salaId),
          tipo: this.sessaoForm.value.tipo,
          inicio: `${dataFormatada}T${this.sessaoForm.value.horario}:00` // <-- Junta data, o 'T', o horário e os segundos
        };

        console.log('Enviando para o Back-end:', payload);
        
        await this.service.salvarSessao(payload);
        dataAtual.setDate(dataAtual.getDate() + 1);
      }

      await Swal.fire({
        icon: 'success',
        title: 'Sucesso!',
        text: 'Todas as sessões do período foram criadas.',
        confirmButtonColor: '#c91432'
      });
      
      this.sessaoForm.reset({ tipo: 'Dublado', classificacao: 'Livre' });

    } catch (error: any) {
      // Captura o erro detalhado que o fetch lança no seu Service
      const msg = error.detail || error.message || 'Erro ao comunicar com o servidor.';
      Swal.fire({ icon: 'error', title: 'Erro', text: msg, confirmButtonColor: '#c91432' });
    } finally {
      this.isLoading = false;
      this.cdr.detectChanges();
    }
  }
}