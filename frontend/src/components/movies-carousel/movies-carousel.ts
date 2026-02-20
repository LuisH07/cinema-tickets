import { Component, Input, ViewChild, ElementRef, AfterViewInit, OnChanges } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-movies-carousel',
  imports: [CommonModule, RouterModule],
  template: `
  <div class="carousel-container">
    <div class="carousel-header">
      <h2>Filmes em Cartaz</h2>
      <div class="carousel-controls" *ngIf="sortedMovies.length > 4">
        <button class="carousel-nav" 
                (click)="scrollLeft()" 
                [disabled]="!canScrollLeft">
          &lt;
        </button>
        <button class="carousel-nav" 
                (click)="scrollRight()" 
                [disabled]="!canScrollRight">
          &gt;
        </button>
      </div>
    </div>

    <div class="carousel-wrapper">
      <div class="cards-container" #carouselContainer (scroll)="updateScrollButtons()">
        <div *ngFor="let movie of sortedMovies" class="movie-card">
          
          <img [src]="getPoster(movie.id)" [alt]="movie.titulo" class="movie-poster">
          
          <div class="movie-info">
            <h3 class="movie-title">{{ movie.titulo }}</h3>
            
            <div class="movie-meta">
              <span class="rating" [style.backgroundColor]="getRatingColor(movie.classificacao)">
                {{ movie.classificacao }}
              </span>
              <span class="duration">{{ movie.duracao }}min</span>
            </div>

            <div class="sessions-container">
              <button *ngFor="let session of getSessionsForMovie(movie.id)" 
                      class="session-button" 
                      [routerLink]="['/movie', movie.id]"
                      [queryParams]="{ data: session.inicio.split('T')[0] }">
                <span class="session-time">{{ formatTime(session.inicio) }}</span>
                <span class="session-type">{{ session.tipo }}</span>
                <span class="session-room">{{ getRoom(session.salaId) }}</span>
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div *ngIf="!sessions.length" class="no-sessions">
      <p>Não há sessões para esta data</p>
    </div>
  </div>
  `,
  styleUrl: './movies-carousel.css',
})
export class MoviesCarousel implements AfterViewInit, OnChanges {
  @ViewChild('carouselContainer') carouselContainer!: ElementRef;
  
  @Input() sessions: any[] = [];
  @Input() movies: any[] = [];
  @Input() rooms: any[] = [];

  canScrollLeft = false;
  canScrollRight = false;

  private movieMap = new Map();
  private roomMap = new Map();

  ngAfterViewInit() {
    setTimeout(() => this.updateScrollButtons(), 100);
  }

  ngOnChanges() {
    setTimeout(() => {
      this.updateScrollButtons();
    }, 100);
  }

  get sortedMovies() {
    if (!this.sessions?.length || !this.movies?.length) return [];
    
    this.updateMaps();
    
    const movieIds = [...new Set(this.sessions.map(s => s.filmeId))];
    
    const moviesWithFirstSession = movieIds
      .map(id => {
        const movie = this.movieMap.get(id);
        if (!movie) return null;
        
        const movieSessions = this.sessions.filter(s => s.filmeId === id);
        
        const earliestSession = movieSessions.reduce((earliest, current) => {
          return current.inicio < earliest.inicio ? current : earliest;
        }, movieSessions[0]);
        
        return {
          ...movie,
          firstSessionTime: earliestSession ? this.extractTime(earliestSession.inicio) : '23:59'
        };
      })
      .filter(movie => movie);
    
    return moviesWithFirstSession.sort((a, b) => 
      a.firstSessionTime.localeCompare(b.firstSessionTime)
    );
  }

  private updateMaps() {
    this.movieMap.clear();
    this.movies?.forEach(m => this.movieMap.set(m.id, m));
    
    this.roomMap.clear();
    this.rooms?.forEach(r => this.roomMap.set(r.id, r));
  }

  getSessionsForMovie(movieId: string): any[] {
    return this.sessions?.filter(s => s.filmeId === movieId) || [];
  }

  getPoster(movieId: string): string {
    const movie = this.movieMap.get(movieId);
    return movie?.poster ? `${movie.poster}` : 'images/placeholder.jpg';
  }

  getRoom(roomId: string): string {
    return this.roomMap.get(roomId)?.nome || 'Sala';
  }

  getRatingColor(classificacao: string): string {
    const colors: {[key: string]: string} = {
      'L': '#2ecc71',
      '10': '#3498db',
      '12': '#f1c40f',
      '14': '#e67e22',
      '16': '#e74c3c',
      '18': '#000000'
    };
    return colors[classificacao] || '#95a5a6';
  }

  private extractTime(isoString: string): string {
    if (!isoString) return '23:59';
    const date = new Date(isoString);
    const hours = date.getHours().toString().padStart(2, '0');
    const minutes = date.getMinutes().toString().padStart(2, '0');
    return `${hours}:${minutes}`;
  }

  formatTime(isoString: string): string {
    if (!isoString) return '--:--';
    const date = new Date(isoString);
    return date.toLocaleTimeString('pt-BR', { 
      hour: '2-digit', 
      minute: '2-digit',
      hour12: false 
    });
  }

  updateScrollButtons() {
    const el = this.carouselContainer?.nativeElement;
    if (!el) return;
    
    this.canScrollLeft = el.scrollLeft > 0;
    this.canScrollRight = el.scrollLeft < el.scrollWidth - el.clientWidth - 1;
  }

  scrollLeft() {
    const el = this.carouselContainer?.nativeElement;
    if (el) {
      el.scrollBy({ left: -300, behavior: 'smooth' });
      setTimeout(() => this.updateScrollButtons(), 300);
    }
  }

  scrollRight() {
    const el = this.carouselContainer?.nativeElement;
    if (el) {
      el.scrollBy({ left: 300, behavior: 'smooth' });
      setTimeout(() => this.updateScrollButtons(), 300);
    }
  }
}