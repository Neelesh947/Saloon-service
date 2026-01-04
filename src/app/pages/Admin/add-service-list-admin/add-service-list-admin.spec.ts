import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddServiceListAdmin } from './add-service-list-admin';

describe('AddServiceListAdmin', () => {
  let component: AddServiceListAdmin;
  let fixture: ComponentFixture<AddServiceListAdmin>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AddServiceListAdmin]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AddServiceListAdmin);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
