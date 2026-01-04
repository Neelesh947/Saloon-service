import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditEmployeeByAdmin } from './edit-employee-by-admin';

describe('EditEmployeeByAdmin', () => {
  let component: EditEmployeeByAdmin;
  let fixture: ComponentFixture<EditEmployeeByAdmin>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EditEmployeeByAdmin]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EditEmployeeByAdmin);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
