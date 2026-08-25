import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Familie } from './familie';

describe('Familie', () => {
  let component: Familie;
  let fixture: ComponentFixture<Familie>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Familie]
    })
    .compileComponents();

    fixture = TestBed.createComponent(Familie);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
