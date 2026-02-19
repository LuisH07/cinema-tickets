import { CommonModule } from '@angular/common';
import { Component, inject } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../../auth/service/auth-service';
import { Router } from '@angular/router';
import { AuthMock } from '../../auth/mock/auth-mock';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-login',
  imports: [
    ReactiveFormsModule,
    CommonModule,
  ],
  template: `
  <div class="page-box">
    <div class="login-box">
      <div class="login-wrapper">
        <h1>LOGIN</h1>
        <div>
          <form class="form-login" [formGroup]="loginForm" (ngSubmit)="onSubmit()">
            <label>E-mail:</label>
            <div class="input-group">
              <input type="email" placeholder="tickets@email.com" formControlName="email" [class.loading]="isLoading">
              <div *ngIf="email?.invalid && email?.touched" class="error-messages">
                <small *ngIf="email?.errors?.['required']">Email é obrigatório!</small>
                <small *ngIf="email?.errors?.['email']">Email inválido!</small>
              </div>
            </div>
            <label>Senha:</label>
            <div class="input-group">
              <input type="password" placeholder="••••••••••" formControlName="password" [class.loading]="isLoading">
              <div *ngIf="password?.invalid && password?.touched" class="error-messages">
                <small *ngIf="password?.errors?.['required']">Senha é obrigatória!</small>
                <small *ngIf="password?.errors?.['minlength']">
                  Mínimo 6 caracteres! (atual: {{ password?.errors?.['minlength']?.actualLength }})
                </small>
              </div>
            </div>
            <button type="submit" [disabled]="loginForm.invalid || isLoading" [class.loading]="isLoading">
              <span *ngIf="!isLoading">Entrar</span>
              <span *ngIf="isLoading">Entrando...</span>
            </button>
          </form>
          <p class="register">Não tem uma conta?<a href="/cadastro" class="register-link">Cadastre-se aqui!</a></p>
        </div>
      </div>
    </div>
    <div class="img-box"></div>
  </div>
  `,
  styleUrl: './login.css',
})
export class Login {
  private authService = inject(AuthService);
  private router = inject(Router);
  loginForm: FormGroup;

  isLoading = false;

  constructor(private fb: FormBuilder) {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
    });
  }

  get email() {
    return this.loginForm.get('email');
  }

  get password() {
    return this.loginForm.get('password');
  }

  async onSubmit() {
    if(this.isLoading || this.loginForm.invalid){
      if (this.loginForm.invalid) {
        this.loginForm.markAllAsTouched();
      }
      return;
    }

    this.isLoading = true;

    const { email, password } = this.loginForm.value;

    try {
      const result = await this.authService.login(email, password);

      if(result.success){
        this.router.navigate(['']);
      } else {
        Swal.fire({
          icon: 'error',
          title: 'Ops...',
          text: 'Falha no login, essa conta não existe!',
          confirmButtonColor: getComputedStyle(document.documentElement).getPropertyValue('--danger').trim(),
        });
      }
    } catch(e: any) {
      Swal.fire({
        icon: 'error',
        title: 'Erro',
        text: 'Erro ao conectar com o servidor, por favor tente novamente mais tarde!',
        confirmButtonColor: getComputedStyle(document.documentElement).getPropertyValue('--danger').trim(),
      });
    } finally {
      this.isLoading = false;
    }
  }

}