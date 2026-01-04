import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StaffListAdmin } from './staff-list-admin';

describe('StaffListAdmin', () => {
  let component: StaffListAdmin;
  let fixture: ComponentFixture<StaffListAdmin>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [StaffListAdmin]
    })
    .compileComponents();

    fixture = TestBed.createComponent(StaffListAdmin);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
