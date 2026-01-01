import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AnalyticsDashboardSuperAdmin } from './analytics-dashboard-super-admin';

describe('AnalyticsDashboardSuperAdmin', () => {
  let component: AnalyticsDashboardSuperAdmin;
  let fixture: ComponentFixture<AnalyticsDashboardSuperAdmin>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AnalyticsDashboardSuperAdmin]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AnalyticsDashboardSuperAdmin);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
