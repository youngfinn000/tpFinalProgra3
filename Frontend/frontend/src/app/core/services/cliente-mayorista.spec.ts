import { TestBed } from '@angular/core/testing';
import { ClienteMayorista } from './cliente-mayorista';

describe('ClienteMayorista', () => {
  let service: ClienteMayorista;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ClienteMayorista);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
