import { ComponentFixture, TestBed } from '@angular/core/testing';
import { PedidosForm } from './pedidos-form';

describe('PedidosForm', () => {
  let component: PedidosForm;
  let fixture: ComponentFixture<PedidosForm>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PedidosForm],
    }).compileComponents();

    fixture = TestBed.createComponent(PedidosForm);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
