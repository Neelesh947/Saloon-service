import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SaloonListComponent } from './saloon-list-component';

describe('SaloonListComponent', () => {
  let component: SaloonListComponent;
  let fixture: ComponentFixture<SaloonListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SaloonListComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SaloonListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
