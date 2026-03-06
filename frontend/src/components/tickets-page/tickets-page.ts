import { Component, OnInit, ChangeDetectorRef, ElementRef, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TicketsService } from '../../general-service/tickets-service/tickets-service';
import { Ticket } from '../../app/core/models/ticket.model';
import jsPDF from 'jspdf';
import html2canvas from 'html2canvas';

@Component({
  selector: 'app-tickets-page',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="page-container">
      <div class="top-container">
        <h1>Meus Ingressos:</h1>
      </div>

      <!-- Loading State -->
      <div class="loading-container" *ngIf="isLoading">
        <div class="spinner"></div>
        <p>Carregando seus ingressos...</p>
      </div>

      <!-- Error State -->
      <div class="error-container" *ngIf="hasError && !isLoading">
        <div class="error-icon">!</div>
        <h3>Erro ao carregar ingressos</h3>
        <p>{{ errorMessage }}</p>
        <button class="retry-btn" (click)="retryLoad()">Tentar novamente</button>
      </div>

      <!-- Tickets Content -->
      <div class="content-container" *ngIf="!isLoading && !hasError">
        <div class="tickets-horizontal-list" *ngIf="sortedTickets.length > 0">
          <div
            class="ticket-card"
            *ngFor="let ticket of sortedTickets; trackBy: trackByTicket"
            [class.ticket-card--past]="isDatePassed(ticket.data)"
          >
            <div class="ticket-header" [class.ticket-header--past]="isDatePassed(ticket.data)">
              <span class="ticket-id">{{ ticket.id }}</span>
              <span
                class="ticket-status"
                [class.ticket-status--confirmed]="ticket.status === 'confirmado'"
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
                  <span class="detail-label">Assento:</span>
                  <span>{{ ticket.assentos }}</span>
                </div>
              </div>
            </div>

            <button
              class="ticket-action-btn"
              [class.ticket-action-btn--past]="isDatePassed(ticket.data)"
              [disabled]="isDatePassed(ticket.data)"
              (click)="generateTicket(ticket)"
            >
              {{ isDatePassed(ticket.data) ? 'Sessão Encerrada' : 'Baixar Ingresso' }}
            </button>
          </div>
        </div>

        <div class="empty-state" *ngIf="sortedTickets.length === 0">
          <h3>Nenhum ingresso encontrado</h3>
          <p>Você ainda não possui ingressos comprados.</p>
        </div>
      </div>
    </div>

    <div class="pdf-render-container">
      <div #ticketTemplate class="ticket-pdf-container" *ngIf="selectedTicket">
        <div class="ticket-pdf">
            <div class="ticket-left">
            <div class="cinema-brand">
              <img src="ticket.png" class="cinema-logo" alt="CINE">
              <span class="cinema-name">CINE</span>
            </div>
            <div class="film-section">
              <div class="film-label">FILME</div>
              <div class="film-title">{{ selectedTicket.filme }}</div>
            </div>
            <div class="date-box">
              {{ selectedTicket.data | date:'dd/MM/yyyy' }}
            </div>
            <div class="session-details">
              <div class="detail">
                <span class="detail-label">HORÁRIO</span>
                <span class="detail-value highlight-red">{{ selectedTicket.horario }}</span>
              </div>
              <div class="detail">
                <span class="detail-label">SALA</span>
                <span class="detail-value highlight-orange">{{ selectedTicket.sala }}</span>
              </div>
              <div class="detail">
                <span class="detail-label">ASSENTO</span>
                <span class="detail-value highlight-green">{{ selectedTicket.assentos }}</span>
              </div>
            </div>
            <div class="status-section">
              <span class="status-badge" [class.status-confirmed]="selectedTicket.status === 'confirmado'">
                {{ selectedTicket.status }}
              </span>
            </div>
          </div>
          <div class="ticket-right">
            <div class="ticket-id-section">
              <div class="ticket-id-label">INGRESSO #</div>
              <div class="ticket-id-value">{{ selectedTicket.id }}</div>
            </div>
            <div class="barcode-section">
              <div class="barcode-bars"></div>
              <div class="barcode-number">{{ selectedTicket.id }}</div>
            </div>
            <div class="legal-info">
              <div class="legal-line">Apresente na entrada da sala</div>
              <div class="legal-line small">Válido apenas para esta sessão</div>
            </div>
          </div>
          <div class="popcorn-overlay"></div>
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

  constructor(
    private readonly ticketsService: TicketsService,
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
      const response = await this.ticketsService.testGetTickets();
      
      if (response && Array.isArray(response)) {
        this.tickets = response;
        this.sortTickets();
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

  isDatePassed(date: string): boolean {
    const ticketDate = new Date(date);
    const today = new Date();
    today.setHours(0, 0, 0, 0);
    return ticketDate < today;
  }

  trackByTicket(index: number, ticket: Ticket): string {
    return ticket.id;
  }

  async generateTicket(ticket: Ticket): Promise<void> {
    this.selectedTicket = ticket;
    this.cdr.detectChanges();
    
    setTimeout(async () => {
      try {
        const element = document.querySelector('.pdf-render-container .ticket-pdf-container');
        
        if (!element) {
          throw new Error('Elemento não encontrado');
        }

        const canvas = await html2canvas(element as HTMLElement, {
          scale: 3,
          backgroundColor: '#ffffff',
          logging: false,
          allowTaint: false,
          useCORS: true,
          windowWidth: 1480,
          windowHeight: 630,
          onclone: (clonedDoc) => {
            const clonedElement = clonedDoc.querySelector('.ticket-pdf-container') as HTMLElement;
            if (clonedElement) {
              clonedElement.style.width = '148mm';
              clonedElement.style.height = '63mm';
              clonedElement.style.position = 'relative';
              clonedElement.style.visibility = 'visible';
            }
          }
        });

        const imgData = canvas.toDataURL('image/jpeg', 0.95);
        
        const pdf = new jsPDF({
          orientation: 'landscape',
          unit: 'mm',
          format: [148, 63]
        });

        const pdfWidth = pdf.internal.pageSize.getWidth();
        const pdfHeight = pdf.internal.pageSize.getHeight();
        
        pdf.addImage(imgData, 'JPEG', 0, 0, pdfWidth, pdfHeight);
        pdf.save(`ingresso-${ticket.id}.pdf`);
        
      } catch (error) {
        console.error('Erro ao gerar PDF:', error);
        alert('Erro ao gerar o PDF. Tente novamente.');
      } finally {
        this.selectedTicket = null;
        this.cdr.detectChanges();
      }
    }, 300);
  }
}