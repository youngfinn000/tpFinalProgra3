import { ComponentFixture, TestBed } from '@angular/core/testing';
import { PedidosList } from './pedidos-list';

describe('PedidosList', () => {
  let component: PedidosList;
  let fixture: ComponentFixture<PedidosList>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PedidosList],
    }).compileComponents();

    fixture = TestBed.createComponent(PedidosList);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
