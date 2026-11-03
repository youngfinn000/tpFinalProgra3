import { ComponentFixture, TestBed } from '@angular/core/testing';
import { SaborDetalle } from './sabor-detalle';

describe('SaborDetalle', () => {
  let component: SaborDetalle;
  let fixture: ComponentFixture<SaborDetalle>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SaborDetalle],
    }).compileComponents();

    fixture = TestBed.createComponent(SaborDetalle);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
