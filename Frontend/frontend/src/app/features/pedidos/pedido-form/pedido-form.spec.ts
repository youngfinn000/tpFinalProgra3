import { ComponentFixture, TestBed } from '@angular/core/testing';
import { PedidoForm } from './pedido-form';

describe('PedidoForm', () => {
  let component: PedidoForm;
  let fixture: ComponentFixture<PedidoForm>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PedidoForm],
    }).compileComponents();

    fixture = TestBed.createComponent(PedidoForm);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
