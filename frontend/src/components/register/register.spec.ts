import { TestBed } from '@angular/core/testing';
import { Register } from './register';
import { NgxMaskDirective, NgxMaskPipe, provideNgxMask } from 'ngx-mask';
import { ReactiveFormsModule } from '@angular/forms';

describe('Register', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        Register,
        ReactiveFormsModule,
        NgxMaskDirective,
        NgxMaskPipe
      ],
      providers: [
        provideNgxMask()
      ]
    }).compileComponents();
  });

  it('should create', () => {
    const fixture = TestBed.createComponent(Register);
    const component = fixture.componentInstance;
    expect(component).toBeTruthy();
  });
});