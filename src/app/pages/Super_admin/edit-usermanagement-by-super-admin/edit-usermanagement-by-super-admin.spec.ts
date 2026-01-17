import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditUsermanagementBySuperAdmin } from './edit-usermanagement-by-super-admin';

describe('EditUsermanagementBySuperAdmin', () => {
  let component: EditUsermanagementBySuperAdmin;
  let fixture: ComponentFixture<EditUsermanagementBySuperAdmin>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EditUsermanagementBySuperAdmin]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EditUsermanagementBySuperAdmin);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
