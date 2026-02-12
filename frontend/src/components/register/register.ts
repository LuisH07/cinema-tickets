import { CommonModule } from '@angular/common';
import { Component, inject, ChangeDetectorRef } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../auth/service/auth-service';
import Swal from 'sweetalert2';
import { NgxMaskDirective } from 'ngx-mask';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [ReactiveFormsModule, CommonModule, NgxMaskDirective],
  template: `
  <div class="page-box">
    <div class="register-box"> 
      <div class="register-wrapper">
        <h1 class="auth-title">CADASTRO</h1> <div>
          <form class="form-login" [formGroup]="registerForm" (ngSubmit)="onSubmit()">
            
          <label>Nome<span class="required">*</span>:</label>
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
                  <input 
                    type="text" 
                    placeholder="(81) 99999-9999"
                    mask="(00) 00000-0000" 
                    formControlName="celular" 
                    [class.loading]="isLoading"
                  >
                </div>
              </div>

              <div class="form-col">
                <label>CPF<span class="required">*</span>:</label>
                <div class="input-group">
                  <input 
                    type="text" 
                    placeholder="000.000.000-00" 
                    mask="000.000.000-00"
                    formControlName="cpf" 
                    [class.loading]="isLoading"
                  >
                  <div *ngIf="cpf?.invalid && cpf?.touched" class="error-messages">
                    <small *ngIf="cpf?.errors?.['required']">CPF é obrigatório!</small>
                    <small *ngIf="cpf?.errors?.['pattern']">CPF deve ter 11 números</small>
                  </div>
                </div>
              </div>
            </div>

            <div class="form-row">
              <div class="form-col">
                <label>E-mail<span class="required">*</span>:</label>
                <div class="input-group">
                  <input type="email" placeholder="tickets@email.com" formControlName="email" [class.loading]="isLoading">
                  <div *ngIf="email?.invalid && email?.touched" class="error-messages">
                    <small *ngIf="email?.errors?.['required']">O e-mail é obrigatório!</small>
                    <small *ngIf="email?.errors?.['email']">Email inválido.</small>
                  </div>
                </div>
              </div>

              <div class="form-col">
                <label>Senha<span class="required">*</span>:</label>
                <div class="input-group">
                  <input type="password" placeholder="••••••••••" formControlName="senha" [class.loading]="isLoading">
                  <div *ngIf="senha?.invalid && senha?.touched" class="error-messages">
                    <small *ngIf="senha?.errors?.['required']">A senha é obrigatória!</small>
                    <small *ngIf="senha?.errors?.['minlength']">Mínimo de 6 caracteres</small>
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
  private authService = inject(AuthService);
  private router = inject(Router);
  registerForm: FormGroup;
  isLoading = false;

  private cdr = inject(ChangeDetectorRef);

  constructor(private fb: FormBuilder) {
    this.registerForm = this.fb.group({
      nome: ['', [Validators.required, Validators.minLength(3)]],
      email: ['', [Validators.required, Validators.email]],
      senha: ['', [Validators.required, Validators.minLength(6)]],
      celular: [''],
      cpf: ['', [Validators.required, Validators.pattern(/^[0-9]{11}$/)]],
      role: ['USER']
    });
  }

  get nome() { return this.registerForm.get('nome'); }
  get email() { return this.registerForm.get('email'); }
  get senha() { return this.registerForm.get('senha'); }
  get celular() { return this.registerForm.get('celular'); }
  get cpf() { return this.registerForm.get('cpf'); }

  async onSubmit() {
    if (this.isLoading || this.registerForm.invalid) {
      this.registerForm.markAllAsTouched();
      return;
    }

    this.isLoading = true;

    try {
      const result = await this.authService.register(this.registerForm.value);

      if (result.success) {
        await Swal.fire({
          icon: 'success',
          title: 'Sucesso!',
          text: 'Sua conta no Cinema Tickets foi criada!',
          confirmButtonColor: '#c91432',
        });
        this.router.navigate(['/']); // Redireciona para o login
      } else {
        this.isLoading = false;

        Swal.fire({
          icon: 'error',
          title: 'Ops...',
          text: "Não foi possível realizar o cadastro",
          confirmButtonColor: '#c91432',
        });
        
      }
    } catch (e) {
      this.isLoading = false;

      Swal.fire({
        icon: 'error',
        title: 'Erro',
        text: 'Não foi possível conectar ao servidor.',
        confirmButtonColor: '#c91432',
      });
    } finally {
      this.isLoading = false;
      this.cdr.detectChanges();
    }
  }
}