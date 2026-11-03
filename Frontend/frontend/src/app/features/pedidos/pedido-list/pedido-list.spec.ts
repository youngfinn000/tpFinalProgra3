import { ComponentFixture, TestBed } from '@angular/core/testing';
import { PedidoList } from './pedido-list';

describe('PedidoList', () => {
  let component: PedidoList;
  let fixture: ComponentFixture<PedidoList>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PedidoList],
    }).compileComponents();

    fixture = TestBed.createComponent(PedidoList);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
