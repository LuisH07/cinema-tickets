import { TestBed } from '@angular/core/testing';
import { Sessao } from './sessao';
import { SessionService } from '../../general-service/session-service/session-service';
import { HttpClientTestingModule } from '@angular/common/http/testing';

describe('Sessao', () => {
  let mockSessionService: Partial<SessionService>;

  beforeEach(async () => {
    mockSessionService = {
      listarFilmes: () => Promise.resolve([])
    };

    await TestBed.configureTestingModule({
      imports: [Sessao, HttpClientTestingModule],
      providers: [
        { provide: SessionService, useValue: mockSessionService }
      ]
    }).compileComponents();
  });

  it('should create', () => {
    const fixture = TestBed.createComponent(Sessao);
    const component = fixture.componentInstance;
    expect(component).toBeTruthy();
  });
});