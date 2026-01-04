import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditServiceListAdmin } from './edit-service-list-admin';

describe('EditServiceListAdmin', () => {
  let component: EditServiceListAdmin;
  let fixture: ComponentFixture<EditServiceListAdmin>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EditServiceListAdmin]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EditServiceListAdmin);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
