import { ComponentFixture, TestBed } from '@angular/core/testing';
import { PedidosDetail } from './pedidos-detail';

describe('PedidosDetail', () => {
  let component: PedidosDetail;
  let fixture: ComponentFixture<PedidosDetail>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PedidosDetail],
    }).compileComponents();

    fixture = TestBed.createComponent(PedidosDetail);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
