import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SuperAdminRouterOutlet } from './super-admin-router-outlet';

describe('SuperAdminRouterOutlet', () => {
  let component: SuperAdminRouterOutlet;
  let fixture: ComponentFixture<SuperAdminRouterOutlet>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SuperAdminRouterOutlet]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SuperAdminRouterOutlet);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
