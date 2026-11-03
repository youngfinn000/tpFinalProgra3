import { ComponentFixture, TestBed } from '@angular/core/testing';
import { SaborForm } from './sabor-form';

describe('SaborForm', () => {
  let component: SaborForm;
  let fixture: ComponentFixture<SaborForm>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SaborForm],
    }).compileComponents();

    fixture = TestBed.createComponent(SaborForm);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
