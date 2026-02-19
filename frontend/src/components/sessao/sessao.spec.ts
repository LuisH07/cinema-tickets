import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';
import { Sessao } from './sessao';

describe('Sessao', () => {
  let component: Sessao;
  let fixture: ComponentFixture<Sessao>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      // Como é Standalone, o componente entra em imports, não em declarations
      imports: [Sessao, ReactiveFormsModule],
      providers: [
        provideHttpClient(),
        provideHttpClientTesting()
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(Sessao);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('deve validar formulário como inválido quando vazio', () => {
    expect(component.sessaoForm.valid).toBeFalsy();
  });
});