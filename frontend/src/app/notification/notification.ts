import { Component, OnInit, inject, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NotificationService } from '../../general-service/notification/notification.service';

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
    this.carregarHistorico();

    // Puxa as notificações do backend a cada 30 segundos silenciosamente
    setInterval(() => {
      this.carregarHistorico();
    }, 30000);
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

  limparTudo() {
    this.notificacoes = [];
    this.notificacoesPendentes = 0;
    this.cdr.detectChanges();
  }
}
