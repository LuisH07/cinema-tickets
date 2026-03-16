import { Component } from '@angular/core';
import { AuthService } from '../../../auth/service/auth-service';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { NavbarComponent } from '../../../app/notification/notification';

@Component({
  selector: 'app-header',
  standalone: true, // Adicionado caso ainda não estivesse
  imports: [ CommonModule, RouterModule, NavbarComponent ],
  template: `
    <header class="header">
      <div class="main-header">
          <a [routerLink]="['/']" class="logo-wrapper">
              <img src="ticket.png" class="logo-img">
              <h1 class="logo-title">Cinema-Tickets</h1>
          </a>

          <div class="nav-actions">
            @if (authService.authStatus$ | async) {
              @if (authService.isAdmin()) {
                <button 
                  type="button" 
                  class="session-button" 
                  [routerLink]="['/cadastro-sessao']">
                  <i class="fa-solid fa-plus"></i>
                  <span class="session-text"> Cadastrar Sessão </span>
                </button>
                <button 
                  type="button" 
                  class="validate-ticket-button" 
                  [routerLink]="['/validar-ingresso']">
                  <i class="fa-solid fa-ticket"></i>
                  <span class="session-text"> Validar Ingresso </span>
                </button>
                
                <app-notification></app-notification>tashed changes
              } @else if (router.url !== '/ingressos') {
                <button type="button" class="tickets-button" [routerLink]="['/ingressos']">
                  <span class="login-text">Ingressos</span>
                  <i class="fa-solid fa-ticket"></i>
                </button>

                
              }
              <button type="button" class="logout-button" (click)="onLogout()">
                <span class="logout-text"> Sair </span>
                <i class="fa-solid fa-arrow-right-from-bracket"></i>
              </button>
            } @else if (router.url !== '/login') {
              <button type ="button" class="login-button" [routerLink]="['/login']">
                <span class="login-text"> Entrar </span>
                <i class="fa-solid fa-arrow-right-to-bracket"></i>
              </button>
            }
          </div>
      </div>
    </header>
  `,
  styleUrl: './header.css',
})
export class Header {
  constructor(
    public authService: AuthService,
    public router: Router,
  ) {}

  onLogout() {
    this.authService.logout();
  }

}