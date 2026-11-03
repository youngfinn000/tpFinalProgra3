import { ComponentFixture, TestBed } from '@angular/core/testing';
import { InsumoLista } from './insumo-list';

describe('InsumoList', () => {
  let component: InsumoLista;
  let fixture: ComponentFixture<InsumoLista>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [InsumoLista],
    }).compileComponents();

    fixture = TestBed.createComponent(InsumoLista);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});