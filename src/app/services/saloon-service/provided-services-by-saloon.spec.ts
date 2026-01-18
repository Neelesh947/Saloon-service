import { TestBed } from '@angular/core/testing';

import { ProvidedServicesBySaloon } from './provided-services-by-saloon';

describe('ProvidedServicesBySaloon', () => {
  let service: ProvidedServicesBySaloon;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ProvidedServicesBySaloon);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
