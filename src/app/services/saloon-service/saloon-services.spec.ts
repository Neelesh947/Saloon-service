import { TestBed } from '@angular/core/testing';

import { SaloonServices } from './saloon-services';

describe('SaloonServices', () => {
  let service: SaloonServices;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SaloonServices);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
