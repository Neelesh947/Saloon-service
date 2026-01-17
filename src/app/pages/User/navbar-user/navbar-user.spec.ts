import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NavbarUser } from './navbar-user';

describe('NavbarUser', () => {
  let component: NavbarUser;
  let fixture: ComponentFixture<NavbarUser>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NavbarUser]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NavbarUser);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
