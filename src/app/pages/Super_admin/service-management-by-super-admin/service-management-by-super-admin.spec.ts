import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ServiceManagementBySuperAdmin } from './service-management-by-super-admin';

describe('ServiceManagementBySuperAdmin', () => {
  let component: ServiceManagementBySuperAdmin;
  let fixture: ComponentFixture<ServiceManagementBySuperAdmin>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ServiceManagementBySuperAdmin]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ServiceManagementBySuperAdmin);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
