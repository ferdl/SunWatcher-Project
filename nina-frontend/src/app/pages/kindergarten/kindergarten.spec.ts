import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Kindergarten } from './kindergarten';

describe('Kindergarten', () => {
  let component: Kindergarten;
  let fixture: ComponentFixture<Kindergarten>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Kindergarten]
    })
    .compileComponents();

    fixture = TestBed.createComponent(Kindergarten);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
