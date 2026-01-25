import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RegisterSaloon } from './register-saloon';

describe('RegisterSaloon', () => {
  let component: RegisterSaloon;
  let fixture: ComponentFixture<RegisterSaloon>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RegisterSaloon]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RegisterSaloon);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
