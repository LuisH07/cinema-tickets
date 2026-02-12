import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthMock } from '../../auth/mock/auth-mock';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule],
  template: `
  <div class="page-box">
    <div class="register-box"> 
      <div class="register-wrapper">
        <h1 class="auth-title">CADASTRO</h1> <div>
          <form class="form-login" [formGroup]="registerForm" (ngSubmit)="onSubmit()">
            
            <label>Nome:</label>
            <div class="input-group">
              <input type="text" placeholder="Digite seu nome completo" formControlName="nome" [class.loading]="isLoading">
              <div *ngIf="nome?.invalid && nome?.touched" class="error-messages">
                <small *ngIf="nome?.errors?.['required']">Nome é obrigatório!</small>
                <small *ngIf="nome?.errors?.['minlength']">Mínimo 3 caracteres!</small>
              </div>
            </div>

            <div class="form-row">
              <div class="form-col">
                <label>Celular:</label>
                <div class="input-group">
                  <input type="text" placeholder="(81) 99999-9999" formControlName="celular" [class.loading]="isLoading">
                  <div *ngIf="celular?.invalid && celular?.touched" class="error-messages">
                    <small *ngIf="celular?.errors?.['required']">Obrigatório!</small>
                  </div>
                </div>
              </div>
              <div class="form-col">
                <label>CPF:</label>
                <div class="input-group">
                  <input type="text" placeholder="000.000.000-00" formControlName="cpf" [class.loading]="isLoading">
                  <div *ngIf="cpf?.invalid && cpf?.touched" class="error-messages">
                    <small *ngIf="cpf?.errors?.['required']">Obrigatório!</small>
                    <small *ngIf="cpf?.errors?.['pattern']">11 números!</small>
                  </div>
                </div>
              </div>
            </div>

            <div class="form-row">
              <div class="form-col">
                <label>E-mail:</label>
                <div class="input-group">
                  <input type="email" placeholder="tickets@email.com" formControlName="email" [class.loading]="isLoading">
                  <div *ngIf="email?.invalid && email?.touched" class="error-messages">
                    <small *ngIf="email?.errors?.['required']">Obrigatório!</small>
                    <small *ngIf="email?.errors?.['email']">Inválido!</small>
                  </div>
                </div>
              </div>
              <div class="form-col">
                <label>Senha:</label>
                <div class="input-group">
                  <input type="password" placeholder="••••••••••" formControlName="password" [class.loading]="isLoading">
                  <div *ngIf="password?.invalid && password?.touched" class="error-messages">
                    <small *ngIf="password?.errors?.['required']">Obrigatória!</small>
                    <small *ngIf="password?.errors?.['minlength']">Mín. 6 carac.!</small>
                  </div>
                </div>
              </div>
            </div>

            <button type="submit" [disabled]="registerForm.invalid || isLoading" [class.loading]="isLoading">
              <span *ngIf="!isLoading">Cadastrar</span>
              <span *ngIf="isLoading">Processando...</span>
            </button>
          </form>
          <p class="register">Já tem uma conta? <a href="/" class="register-link">Entre aqui!</a></p>
        </div>
      </div>
    </div>
    <div class="img-box"></div>
  </div>
  `,
  styleUrl: './register.css',
})
export class Register {
  private authService = inject(AuthMock);
  private router = inject(Router);
  registerForm: FormGroup;
  isLoading = false;

  constructor(private fb: FormBuilder) {
    this.registerForm = this.fb.group({
      nome: ['', [Validators.required, Validators.minLength(3)]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      celular: ['', [Validators.required]],
      cpf: ['', [Validators.required, Validators.pattern(/^[0-9]{11}$/)]],
      role: ['CLIENT']
    });
  }

  get nome() { return this.registerForm.get('nome'); }
  get email() { return this.registerForm.get('email'); }
  get password() { return this.registerForm.get('password'); }
  get celular() { return this.registerForm.get('celular'); }
  get cpf() { return this.registerForm.get('cpf'); }

  async onSubmit() {
    if (this.isLoading || this.registerForm.invalid) {
      this.registerForm.markAllAsTouched();
      return;
    }
    this.isLoading = true;
    try {
      const userData = this.registerForm.value;
      await new Promise(resolve => setTimeout(resolve, 1500));
      Swal.fire({
        icon: 'success',
        title: 'Sucesso!',
        text: 'Conta criada com sucesso!',
        confirmButtonColor: '#c91432',
      });
      this.router.navigate(['/']);
    } catch (e) {
      Swal.fire({
        icon: 'error',
        title: 'Erro',
        text: 'Erro ao realizar cadastro!',
        confirmButtonColor: '#c91432',
      });
    } finally {
      this.isLoading = false;
    }
  }
}