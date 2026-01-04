import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EditSaloonByAdmin } from './edit-saloon-by-admin';

describe('EditSaloonByAdmin', () => {
  let component: EditSaloonByAdmin;
  let fixture: ComponentFixture<EditSaloonByAdmin>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EditSaloonByAdmin]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EditSaloonByAdmin);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
