import { TestBed } from '@angular/core/testing';

import { TokenStorageServices } from './token-storage-services';

describe('TokenStorageServices', () => {
  let service: TokenStorageServices;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TokenStorageServices);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
