import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StaffmanagementBySuperAdmin } from './staffmanagement-by-super-admin';

describe('StaffmanagementBySuperAdmin', () => {
  let component: StaffmanagementBySuperAdmin;
  let fixture: ComponentFixture<StaffmanagementBySuperAdmin>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [StaffmanagementBySuperAdmin]
    })
    .compileComponents();

    fixture = TestBed.createComponent(StaffmanagementBySuperAdmin);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
