import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddSaloonSuperAdmin } from './add-saloon-super-admin';

describe('AddSaloonSuperAdmin', () => {
  let component: AddSaloonSuperAdmin;
  let fixture: ComponentFixture<AddSaloonSuperAdmin>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AddSaloonSuperAdmin]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AddSaloonSuperAdmin);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
