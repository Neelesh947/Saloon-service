import { TestBed } from '@angular/core/testing';

import { AnalyticsServices } from './analytics-services';

describe('AnalyticsServices', () => {
  let service: AnalyticsServices;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AnalyticsServices);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
