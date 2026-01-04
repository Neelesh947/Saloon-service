import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditSaloonSuperAdmin } from './edit-saloon-super-admin';

describe('EditSaloonSuperAdmin', () => {
  let component: EditSaloonSuperAdmin;
  let fixture: ComponentFixture<EditSaloonSuperAdmin>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EditSaloonSuperAdmin]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EditSaloonSuperAdmin);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
