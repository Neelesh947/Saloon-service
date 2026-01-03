import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SaloonManagements } from './saloon-managements';

describe('SaloonManagements', () => {
  let component: SaloonManagements;
  let fixture: ComponentFixture<SaloonManagements>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SaloonManagements]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SaloonManagements);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
