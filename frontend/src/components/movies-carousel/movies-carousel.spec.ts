import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MoviesCarousel } from './movies-carousel';

describe('MoviesCarousel', () => {
  let component: MoviesCarousel;
  let fixture: ComponentFixture<MoviesCarousel>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MoviesCarousel]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MoviesCarousel);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
