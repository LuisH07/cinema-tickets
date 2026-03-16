import { ComponentFixture, TestBed } from '@angular/core/testing';
import { TicketsPage } from './tickets-page';
import { TicketsService } from '../../general-service/tickets-service/tickets-service';
import { IngressoService } from '../../general-service/ingresso-service/ingresso.service';
import { ReviewService } from '../../general-service/review-service/review-service';

class MockTicketsService {
  getTickets() {
    return Promise.resolve([]);
  }
}

class MockReviewService {
  avaliarTicket() {
    return Promise.resolve({});
  }
}

class MockIngressoService {
  gerarPDF() {
    return Promise.resolve();
  }
}

describe('TicketsPage', () => {
  let component: TicketsPage;
  let fixture: ComponentFixture<TicketsPage>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TicketsPage],
      providers: [
        { provide: TicketsService, useClass: MockTicketsService },
        { provide: ReviewService, useClass: MockReviewService },
        { provide: IngressoService, useClass: MockIngressoService }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(TicketsPage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});