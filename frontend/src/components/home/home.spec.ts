import { TestBed } from '@angular/core/testing';
import { Home } from './home';
import { MoviesService } from '../../general-service/movies-service/movies-service';
import { SessionService } from '../../general-service/session-service/session-service';
import { RoomService } from '../../general-service/room-service/room-service';
import { HttpClientTestingModule } from '@angular/common/http/testing';

describe('Home', () => {
  let mockMoviesService: Partial<MoviesService>;
  let mockSessionService: Partial<SessionService>;
  let mockRoomService: Partial<RoomService>;

  beforeEach(async () => {
    mockMoviesService = {
      getMovies: () => Promise.resolve([])
    };

    mockSessionService = {
      getSessionsByDate: () => Promise.resolve([])
    };

    mockRoomService = {
      getRooms: () => Promise.resolve([])
    };

    await TestBed.configureTestingModule({
      imports: [Home, HttpClientTestingModule],
      providers: [
        { provide: MoviesService, useValue: mockMoviesService },
        { provide: SessionService, useValue: mockSessionService },
        { provide: RoomService, useValue: mockRoomService }
      ]
    }).compileComponents();
  });

  it('should create', () => {
    const fixture = TestBed.createComponent(Home);
    const component = fixture.componentInstance;
    expect(component).toBeTruthy();
  });
});