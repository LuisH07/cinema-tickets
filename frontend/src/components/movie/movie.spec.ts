import { TestBed } from '@angular/core/testing';
import { Movie } from './movie';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

describe('Movie', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Movie],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            params: of({ id: '1' }),
            snapshot: { params: { id: '1' } }
          }
        }
      ]
    }).compileComponents();
  });

  it('should create', () => {
    const fixture = TestBed.createComponent(Movie);
    const component = fixture.componentInstance;
    expect(component).toBeTruthy();
  });
});