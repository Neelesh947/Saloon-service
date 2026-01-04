import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddStaffByAdmin } from './add-staff-by-admin';

describe('AddStaffByAdmin', () => {
  let component: AddStaffByAdmin;
  let fixture: ComponentFixture<AddStaffByAdmin>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AddStaffByAdmin]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AddStaffByAdmin);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
