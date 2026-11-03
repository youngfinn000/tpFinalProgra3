import { ComponentFixture, TestBed } from '@angular/core/testing';
import { SaborList } from './sabor-list';

describe('SaborList', () => {
  let component: SaborList;
  let fixture: ComponentFixture<SaborList>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SaborList],
    }).compileComponents();

    fixture = TestBed.createComponent(SaborList);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
