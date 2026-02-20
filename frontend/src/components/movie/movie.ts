import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { MoviesService } from '../../general-service/movies-service/movies-service';
import { SessionService } from '../../general-service/session-service/session-service';
import { RoomService } from '../../general-service/room-service/room-service';
import { MovieModel } from '../../app/core/models/movie.model';
import { ActivatedRoute } from '@angular/router';
import { CommonModule } from '@angular/common';
import { SessionModel } from '../../app/core/models/session.model';

@Component({
  selector: 'app-movie',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './movie.html',
  styleUrl: './movie.css',
})
export class Movie implements OnInit {
  movie?: MovieModel;
  filteredSessions: SessionModel[] = [];
  
  currentDate: string = new Date(Date.now() - new Date().getTimezoneOffset() * 60000).toISOString().split('T')[0];
  movieId!: number;

  constructor(
    private moviesService: MoviesService,
    private sessionService: SessionService,
    private roomService: RoomService,
    private route: ActivatedRoute,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.route.queryParamMap.subscribe(queryParams => {
      const dataDaUrl = queryParams.get('data');
      if (dataDaUrl) {
        this.currentDate = dataDaUrl;
      }
    });

    this.route.paramMap.subscribe(async (params) => {
      this.movieId = Number(params.get('id'));

      if (this.movieId) {
        try {
          this.movie = await this.moviesService.getMoviesById(this.movieId);
        } catch (error) {
          console.error('Erro ao carregar o filme:', error);
        }

        await this.carregarSessoesNoBanco(this.currentDate);
      }
    });
  }

  async filterByDate(evento: any) {
    const selectedDate = typeof evento === 'string' ? evento : evento.target.value;
    this.currentDate = selectedDate;
    
    if (this.movieId) {
      await this.carregarSessoesNoBanco(this.currentDate);
    }
  }

  private async carregarSessoesNoBanco(dataBusca: string) {
    const retornoSessoes = await this.sessionService.getSessionsByDate(dataBusca);
    const retornoSalas = await this.roomService.getRooms();

    let todasAsSessoes = Array.isArray(retornoSessoes) ? retornoSessoes : ((retornoSessoes as any).content || []);
    if (!Array.isArray(todasAsSessoes)) todasAsSessoes = [];

    let todasAsSalas = Array.isArray(retornoSalas) ? retornoSalas : ((retornoSalas as any).content || []);

    this.filteredSessions = todasAsSessoes
      .filter((sessao: any) => Number(sessao.filmeId) === this.movieId)
      .map((sessao: any) => ({
        ...sessao,
        sala: todasAsSalas.find((sala: any) => Number(sala.id) === Number(sessao.salaId))
      }));

    this.cdr.detectChanges();
  }

  getPoster(): string {
    if (!this.movie?.poster) return '/images/placeholder.jpg';
    return this.movie.poster.startsWith('/') ? this.movie.poster : '/images/' + this.movie.poster;
  }

  getBackdropImage(): string {
    if (!this.movie?.backdrop) return '';
    return this.movie.backdrop.startsWith('/') ? this.movie.backdrop : '/images/' + this.movie.backdrop;
  }
}