import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Newborn } from './newborn';

describe('Newborn', () => {
  let component: Newborn;
  let fixture: ComponentFixture<Newborn>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Newborn]
    })
    .compileComponents();

    fixture = TestBed.createComponent(Newborn);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
