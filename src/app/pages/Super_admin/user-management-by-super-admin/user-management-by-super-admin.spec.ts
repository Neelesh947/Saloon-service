import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserManagementBySuperAdmin } from './user-management-by-super-admin';

describe('UserManagementBySuperAdmin', () => {
  let component: UserManagementBySuperAdmin;
  let fixture: ComponentFixture<UserManagementBySuperAdmin>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UserManagementBySuperAdmin]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UserManagementBySuperAdmin);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
