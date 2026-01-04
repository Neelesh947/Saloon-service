import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddSaloonByAdmin } from './add-saloon-by-admin';

describe('AddSaloonByAdmin', () => {
  let component: AddSaloonByAdmin;
  let fixture: ComponentFixture<AddSaloonByAdmin>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AddSaloonByAdmin]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AddSaloonByAdmin);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
