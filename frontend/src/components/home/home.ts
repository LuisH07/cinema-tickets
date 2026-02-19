import { Component, ChangeDetectorRef, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { MoviesService } from '../../general-service/movies-service/movies-service';
import { RoomService } from '../../general-service/room-service/room-service';
import { SessionService } from '../../general-service/session-service/session-service';
import { MoviesCarousel } from '../films-carousel/movies-carousel';

@Component({
  selector: 'app-home',
  imports: [FormsModule, CommonModule, MoviesCarousel],
  template: `
    <div class="home-container">
      <div class="top-container">
        <h1>Próximas Sessões:</h1>
        <input type="date" 
               (change)="onDateChange()" 
               [(ngModel)]="selectedDate" 
               [min]="minDate" 
               [max]="maxDate" 
               class="date-picker" />
      </div>

      <div *ngIf="isLoading" class="loading-container">
        <p>Carregando filmes...</p>
      </div>

      <div *ngIf="!isLoading && movies.length > 0 && sessions.length === 0" class="no-movies-container">
        <i class="fa-solid fa-film"></i>
        <h2>Não há sessões disponíveis</h2>
        <p>Nenhum filme encontrado para a data {{ selectedDate | date:'dd/MM/yyyy' }}</p>
        <p>Tente selecionar outra data no calendário acima.</p>
      </div>

      <div *ngIf="!isLoading && movies.length > 0 && sessions.length > 0">
        <app-movies-carousel 
          [sessions]="sessions" 
          [movies]="movies" 
          [rooms]="rooms">
        </app-movies-carousel>
      </div>
      
      <div *ngIf="!isLoading && movies.length === 0" class="error-container">
        <p>Erro ao carregar filmes. Tente novamente mais tarde.</p>
      </div>
    </div>
  `,
  styleUrl: './home.css',
})
export class Home implements OnInit {
  selectedDate: string = '';
  minDate: string = '';
  maxDate: string = '';

  movies: any[] = [];
  sessions: any[] = [];
  rooms: any[] = [];
  isLoading = true;

  constructor(
    private filmsService: MoviesService,
    private sessionService: SessionService,
    private roomService: RoomService,
    private cdr: ChangeDetectorRef,
  ) {
    this.setDateRange();
  }

  async ngOnInit() {
    await this.loadData();
  }

  async onDateChange() {
    this.isLoading = true;
    await this.loadSessions();
    this.isLoading = false;
    this.cdr.detectChanges();
  }

  private async loadData() {
    this.isLoading = true;
    await Promise.all([
      this.loadMovies(),
      this.loadSessions(),
      this.loadRooms(),
    ]);
    this.isLoading = false;
    this.cdr.detectChanges();
  }

  private async loadMovies() {
    try {
      this.movies = await this.filmsService.getMovies();
    } catch (error) {
      console.error('Erro ao carregar filmes:', error);
    }
  }

  private async loadSessions() {
    try {
      const allSessions = await this.sessionService.getSessions();
      this.sessions = allSessions.filter(s => 
        s.inicio.startsWith(this.selectedDate)
      );
    } catch (error) {
      console.error('Erro ao carregar sessões:', error);
    }
  }

  private async loadRooms() {
    try {
      this.rooms = await this.roomService.getRooms();
    } catch (e) {
      console.error('Erro ao carregar salas:', e);
    }
  }

  private setDateRange() {
    const today = new Date();
    this.minDate = this.formatDate(today);
    
    const max = new Date(today);
    max.setDate(today.getDate() + 7);
    this.maxDate = this.formatDate(max);
    this.selectedDate = this.minDate;
  }

  private formatDate(date: Date): string {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
  }
}