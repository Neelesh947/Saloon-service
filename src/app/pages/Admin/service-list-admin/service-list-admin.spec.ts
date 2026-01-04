import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ServiceListAdmin } from './service-list-admin';

describe('ServiceListAdmin', () => {
  let component: ServiceListAdmin;
  let fixture: ComponentFixture<ServiceListAdmin>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ServiceListAdmin]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ServiceListAdmin);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
