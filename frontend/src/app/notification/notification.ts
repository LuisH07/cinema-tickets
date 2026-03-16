import { Component, OnInit, inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NotificationService } from '../../general-service/notification-service/notification.service';

@Component({
  selector: 'app-notification',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './notification.html',
  styleUrl: './notification.css',
})
export class NavbarComponent implements OnInit {
  private readonly notificationService = inject(NotificationService);
  private readonly cdr = inject(ChangeDetectorRef);

  dropdownAberto = false;
  notificacoes: any[] = []; 
  notificacoesPendentes = 0;

  ngOnInit() {
    this.notificationService.currentMessage.subscribe((payload) => {
      const titulo = payload.notification?.title || 'Lembrete de Sessão';
      const corpo = payload.notification?.body || 'Sua sessão está prestes a começar!';

      this.adicionarNotificacao(`${titulo} - ${corpo}`);
    });

    this.carregarHistorico();
  }

  toggleDropdown() {
    this.dropdownAberto = !this.dropdownAberto;
  }

  async carregarHistorico() {
    const historico = await this.notificationService.listarNotificacoes();
    this.notificacoes = historico.map((n) => ({
      id: n.id,
      mensagem: n.mensagem,
      horario: n.dataEnvioAgendada,
      lida: n.visto,
    }));
    this.notificacoesPendentes = this.notificacoes.filter((n) => !n.lida).length;
    this.cdr.detectChanges();
  }

  async marcarComoLida(notificacao: any) {
    if (!notificacao.lida && notificacao.id) {
      await this.notificationService.marcarComoVista(notificacao.id);
      notificacao.lida = true;
      if (this.notificacoesPendentes > 0) {
        this.notificacoesPendentes--;
      }
      this.cdr.detectChanges();
    }
  }

  adicionarNotificacao(msg: string) {
    this.notificacoes.unshift({
      mensagem: msg,
      horario: new Date(),
      lida: false,
    });
    this.notificacoesPendentes++;
    this.cdr.detectChanges();
  }

  limparTudo() {
    this.notificacoes = [];
    this.notificacoesPendentes = 0;
    this.cdr.detectChanges();
  }
}
