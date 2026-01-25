import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SalonManagement } from './salon-management';

describe('SalonManagement', () => {
  let component: SalonManagement;
  let fixture: ComponentFixture<SalonManagement>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SalonManagement]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SalonManagement);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
