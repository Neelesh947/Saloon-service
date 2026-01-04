import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SaloonListAdmin } from './saloon-list-admin';

describe('SaloonListAdmin', () => {
  let component: SaloonListAdmin;
  let fixture: ComponentFixture<SaloonListAdmin>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SaloonListAdmin]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SaloonListAdmin);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
