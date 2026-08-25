import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Babybauch } from './babybauch';

describe('Babybauch', () => {
  let component: Babybauch;
  let fixture: ComponentFixture<Babybauch>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Babybauch]
    })
    .compileComponents();

    fixture = TestBed.createComponent(Babybauch);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
