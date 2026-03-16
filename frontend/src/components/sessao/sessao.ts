import { CommonModule } from '@angular/common';
import { Component, inject, OnInit, ChangeDetectorRef } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators, AbstractControl, ValidationErrors } from '@angular/forms';
import { Router } from '@angular/router'; 
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
              <span class="error-text" *ngIf="filmeId?.touched && filmeId?.errors?.['required']">
                O filme é obrigatório.
              </span>
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
              <input type="date" formControlName="dataInicio" 
                    [class.is-invalid]="dataInicio?.invalid && dataInicio?.touched">
              
              <span class="error-text" *ngIf="dataInicio?.errors?.['dataPassada'] && (dataInicio?.touched || dataInicio?.dirty)">
                Só é possível cadastrar sessões em datas futuras.
              </span>

              <span class="error-text" *ngIf="dataInicio?.errors?.['horarioPassado'] && (dataInicio?.touched || dataInicio?.dirty)">
                O horário selecionado para hoje já passou.
              </span>
              
              <span class="error-text" *ngIf="dataInicio?.errors?.['required'] && dataInicio?.touched">
                A data inicial é obrigatória.
              </span>
            </div>

            <div class="form-col">
              <label>Data Final<span class="required">*</span></label>
              <input type="date" formControlName="dataFim">
              <span class="error-text" *ngIf="dataFim?.touched && sessaoForm.errors?.['periodoInvalido']">
                A data final não pode ser menor que a inicial.
              </span>
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
  private readonly service = inject(SessionService);
  private readonly fb = inject(FormBuilder);
  private readonly cdr = inject(ChangeDetectorRef);
  private readonly router = inject(Router);

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
      dataInicio: ['', [Validators.required, this.dataFuturaValidator]],
      dataFim: ['', Validators.required]
    }, { validators: this.periodoValidator }); 
  }

  dataFuturaValidator(control: AbstractControl): ValidationErrors | null {
    if (!control.value) return null;

    const hoje = new Date();
    const [ano, mes, dia] = control.value.split('-').map(Number);
    const dataInput = new Date(ano, mes - 1, dia);
    
    const hojeApenasData = new Date();
    hojeApenasData.setHours(0, 0, 0, 0);

    if (dataInput < hojeApenasData) {
      return { dataPassada: true };
    }

    if (dataInput.getTime() === hojeApenasData.getTime()) {
      const horarioInput = control.parent?.get('horario')?.value;
      
      if (horarioInput) {
        const [horas, minutos] = horarioInput.split(':').map(Number);
        dataInput.setHours(horas, minutos);

        if (dataInput < hoje) {
          return { horarioPassado: true };
        }
      }
    }

    return null;
  }

  periodoValidator(group: AbstractControl): ValidationErrors | null {
    const inicio = group.get('dataInicio')?.value;
    const fim = group.get('dataFim')?.value;

    if (inicio && fim && new Date(fim) < new Date(inicio)) {
      return { periodoInvalido: true };
    }
    return null;
  }

  get filmeId() { return this.sessaoForm.get('filmeId'); }
  get salaId() { return this.sessaoForm.get('salaId'); }
  get horario() { return this.sessaoForm.get('horario'); }
  get dataInicio() { return this.sessaoForm.get('dataInicio'); }
  get dataFim() { return this.sessaoForm.get('dataFim'); }

  async ngOnInit() {
    try {
      this.filmes = await this.service.listarFilmes();
      this.salas = await this.service.listarSalas();
      this.cdr.detectChanges();
    } catch (error) {
      console.error('Erro ao carregar dados:', error);
    }
  }

  async onSubmit() {
    if (this.sessaoForm.invalid) {
      this.sessaoForm.markAllAsTouched();
      
      const erroData = this.dataInicio?.errors?.['dataPassada'];
      const erroHora = this.dataInicio?.errors?.['horarioPassado'];

      if (erroData || erroHora) {
        Swal.fire({
          icon: 'warning',
          title: 'Data ou Horário Inválido',
          text: erroData ? 'Só é possível cadastrar sessões em datas futuras.' : 'Para sessões de hoje, o horário deve ser posterior ao atual.',
          confirmButtonColor: '#c91432'
        });
      }
      return;
    }

    this.isLoading = true;
    try {
      const { dataInicio, dataFim, horario, filmeId, salaId, tipo } = this.sessaoForm.value;
      let dataAtual = new Date(dataInicio + 'T00:00:00');
      const dFim = new Date(dataFim + 'T00:00:00');
      
      while (dataAtual <= dFim) {
        const ano = dataAtual.getFullYear();
        const mes = String(dataAtual.getMonth() + 1).padStart(2, '0');
        const dia = String(dataAtual.getDate()).padStart(2, '0');
        
        const payload = {
          filmeId: Number(filmeId), 
          salaId: Number(salaId),
          tipo: tipo,
          inicio: `${ano}-${mes}-${dia}T${horario}:00`
        };
        
        await this.service.salvarSessao(payload);
        dataAtual.setDate(dataAtual.getDate() + 1);
      }

      Swal.fire({ 
        icon: 'success', 
        title: 'Sucesso!', 
        text: 'Sessões criadas com sucesso!', 
        confirmButtonColor: '#c91432' 
      }).then(() => {
        this.router.navigate(['/']); 
      });

    } catch (error: any) {
      const msg = error.detail || error.message || 'Erro ao comunicar com o servidor.';
      Swal.fire({ icon: 'error', title: 'Erro', text: msg, confirmButtonColor: '#c91432' });
    } finally {
      this.isLoading = false;
      this.cdr.detectChanges();
    }
  }
}