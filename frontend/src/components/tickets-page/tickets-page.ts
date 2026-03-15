import { Component, OnInit, ChangeDetectorRef, ElementRef, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TicketsService } from '../../general-service/tickets-service/tickets-service';
import { IngressoService } from '../../general-service/ingresso-service/ingresso.service';
import { ReviewService } from '../../general-service/review-service/review-service';
import { Ticket } from '../../app/core/models/ticket.model';

@Component({
  selector: 'app-tickets-page',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="page-container">
      <div class="top-container">
        <h1>Meus Ingressos:</h1>
      </div>

      <div class="loading-container" *ngIf="isLoading">
        <div class="spinner"></div>
        <p>Carregando seus ingressos...</p>
      </div>

      <div class="error-container" *ngIf="hasError && !isLoading">
        <div class="error-icon">!</div>
        <h3>Erro ao carregar ingressos</h3>
        <p>{{ errorMessage }}</p>
        <button class="retry-btn" (click)="retryLoad()">Tentar novamente</button>
      </div>

      <div class="content-container" *ngIf="!isLoading && !hasError">
        <div class="tickets-horizontal-list" *ngIf="sortedTickets.length > 0">
          <div
            class="ticket-card"
            *ngFor="let ticket of sortedTickets; trackBy: trackByTicket"
            [class.ticket-card--past]="isDatePassed(ticket.data, ticket.horario)"
            [class.ticket-card--with-rating]="ticket.status === 'UTILIZADO' || ticket.status === 'AVALIADO'"
          >
            <div class="ticket-header" [class.ticket-header--past]="isDatePassed(ticket.data, ticket.horario)">
              <span class="ticket-id">{{ ticket.id }}</span>
              <span
                class="ticket-status"
                [class.ticket-status--confirmed]="ticket.status === 'confirmado'"
                [class.ticket-status--used]="ticket.status === 'UTILIZADO'"
                [class.ticket-status--rated]="ticket.status === 'AVALIADO'"
              >
                {{ ticket.status }}
              </span>
            </div>

            <div class="movie-info-ticket">
              <h3 class="movie-title-ticket">{{ ticket.filme }}</h3>

              <div class="ticket-details">
                <div class="detail-item">
                  <span class="detail-label">Data:</span>
                  <span>{{ ticket.data | date:'dd/MM/yyyy' }}</span>
                </div>

                <div class="detail-item">
                  <span class="detail-label">Horário:</span>
                  <span>{{ ticket.horario }}</span>
                </div>

                <div class="detail-item">
                  <span class="detail-label">Sala:</span>
                  <span>{{ ticket.sala }}</span>
                </div>

                <div class="detail-item">
                  <span class="detail-label">Assentos:</span>
                  <span>{{ ticket.assentos.join(', ') }}</span>
                </div>
              </div>
            </div>
            <button
              *ngIf="ticket.status === 'confirmado'"
              class="ticket-action-btn"
              [class.ticket-action-btn--past]="isDatePassed(ticket.data, ticket.horario)"
              [disabled]="isDatePassed(ticket.data, ticket.horario)"
              (click)="generateTicket(ticket)"
            >
              {{ isDatePassed(ticket.data, ticket.horario) ? 'Sessão Encerrada' : 'Baixar Ingresso' }}
            </button>
            <div class="ticket-footer" *ngIf="ticket.status === 'UTILIZADO' || ticket.status === 'AVALIADO'">
              <div class="rating-section" *ngIf="ticket.status === 'UTILIZADO'">
                <div class="rating-container">
                  <span class="rating-label">Avalie o filme:</span>
                  <div class="stars-container">
                    <span
                      *ngFor="let star of [1,2,3,4,5]; let i = index"
                      class="star star-clickable"
                      [class.star-filled]="i < (tempRatings[ticket.id] || 0)"
                      [class.star-loading]="isRatingLoading[ticket.id]"
                      (click)="!isRatingLoading[ticket.id] && setTempRating(ticket.id, i + 1)"
                    >
                      ★
                    </span>
                  </div>
                  
                  <button 
                    class="confirm-rating-btn"
                    *ngIf="tempRatings[ticket.id] > 0"
                    (click)="submitRating(ticket)"
                    [disabled]="isRatingLoading[ticket.id]"
                  >
                    <span *ngIf="!isRatingLoading[ticket.id]">Confirmar avaliação</span>
                    <span *ngIf="isRatingLoading[ticket.id]">Enviando...</span>
                  </button>
                </div>
              </div>

              <div class="rating-section rating-section--rated" *ngIf="ticket.status === 'AVALIADO'">
                <div class="rating-display">
                  <span class="rating-label">Filme avaliado:</span>
                  <span class="rated-message">✓ Obrigado pela sua avaliação!</span>
                </div>
              </div>
            </div>
          </div>
        </div>

        <div class="empty-state" *ngIf="sortedTickets.length === 0">
          <h3>Nenhum ingresso encontrado</h3>
          <p>Você ainda não possui ingressos comprados.</p>
        </div>
      </div>
    </div>
  `,
  styleUrls: ['./tickets-page.css']
})
export class TicketsPage implements OnInit {
  @ViewChild('ticketTemplate') ticketTemplate!: ElementRef;
  
  tickets: Ticket[] = [];
  sortedTickets: Ticket[] = [];
  isLoading = true;
  hasError = false;
  errorMessage = '';
  selectedTicket: Ticket | null = null;

  tempRatings: { [ticketId: string]: number } = {};
  isRatingLoading: { [ticketId: string]: boolean } = {};

  constructor(
    private readonly ticketsService: TicketsService,
    private readonly ingressoService: IngressoService,
    private readonly reviewService: ReviewService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadData();
  }

  private async loadData(): Promise<void> {
    this.isLoading = true;
    this.hasError = false;
    this.cdr.detectChanges();

    try {
      const response = await this.ticketsService.getTickets();
      
      if (response && Array.isArray(response)) {
        this.tickets = response;
        this.sortTickets();
        this.tickets[0].status = "UTILIZADO";
        console.log('Tickets carregados:', this.tickets.length);
      } else {
        throw new Error('Resposta inválida do servidor');
      }
    } catch (error) {
      this.hasError = true;
      this.errorMessage = error instanceof Error 
        ? error.message 
        : 'Não foi possível carregar seus ingressos.';
      this.tickets = [];
      this.sortedTickets = [];
      console.error('Erro:', error);
    } finally {
      this.isLoading = false;
      this.cdr.detectChanges();
    }
  }

  retryLoad(): void {
    this.loadData();
  }

  private sortTickets(): void {
    if (!this.tickets || this.tickets.length === 0) {
      this.sortedTickets = [];
      return;
    }

    const today = new Date();
    today.setHours(0, 0, 0, 0);

    this.sortedTickets = [...this.tickets].sort((a, b) => {
      const dateA = new Date(a.data);
      const dateB = new Date(b.data);

      const aIsPast = dateA < today;
      const bIsPast = dateB < today;

      if (!aIsPast && bIsPast) return -1;
      if (aIsPast && !bIsPast) return 1;

      return dateA.getTime() - dateB.getTime();
    });
  }

  isDatePassed(date: string, horario: string): boolean {
    console.log('=== DEBUG isDatePassed ===');
    console.log('Data recebida (string):', date);
    console.log('Horário recebido:', horario);
    
    // Cria data do ticket (assumindo que a data vem no formato ISO ou yyyy-mm-dd)
    const ticketDateTime = new Date(`${date}T${horario}:00`);
    
    console.log('TicketDateTime (objeto Date):', ticketDateTime);
    console.log('TicketDateTime ISO:', ticketDateTime.toISOString());
    console.log('TicketDateTime local:', ticketDateTime.toString());
    
    const now = new Date();
    console.log('Agora (objeto Date):', now);
    console.log('Agora ISO:', now.toISOString());
    console.log('Agora local:', now.toString());
    
    console.log('Ticket < agora?', ticketDateTime < now);
    console.log('==========================');
    
    return ticketDateTime < now;
  }

  trackByTicket(index: number, ticket: Ticket): string {
    return ticket.id;
  }

  async generateTicket(ticket: Ticket): Promise<void> {
    try {
      const dados = {
        filmeTitulo: ticket.filme,
        salaNome: ticket.sala,
        data: ticket.data,
        horario: ticket.horario,
        ingressosIds: [ticket.id],
        assentosCodigos: ticket.assentos
      };

      await this.ingressoService.gerarPDF(dados);

    } catch (error) {
      console.error('Erro ao gerar PDF:', error);
      alert('Erro ao gerar o PDF. Tente novamente.');
    }
  }

  setTempRating(ticketId: string, rating: number): void {
    this.tempRatings[ticketId] = rating;
  }

  async submitRating(ticket: Ticket): Promise<void> {
    const rating = this.tempRatings[ticket.id];
    if (!rating || rating < 1 || rating > 5) {
      alert('Por favor, selecione uma nota entre 1 e 5 estrelas');
      return;
    }

    this.isRatingLoading[ticket.id] = true;

    try {
      await this.reviewService.avaliarTicket(ticket.id, rating);
      await this.loadData();
    } catch (error) {
      console.error('Erro ao enviar avaliação:', error);
      alert('Erro ao enviar avaliação. Tente novamente.');
    } finally {
      this.isRatingLoading[ticket.id] = false;
      this.cdr.detectChanges();
    }
  }
}