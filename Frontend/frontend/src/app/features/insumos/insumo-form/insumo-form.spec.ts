import { ComponentFixture, TestBed } from '@angular/core/testing';
import { InsumoForm } from './insumo-form';

describe('InsumoForm', () => {
  let component: InsumoForm;
  let fixture: ComponentFixture<InsumoForm>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [InsumoForm],
    }).compileComponents();

    fixture = TestBed.createComponent(InsumoForm);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
