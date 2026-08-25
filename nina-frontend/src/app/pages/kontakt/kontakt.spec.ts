import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Kontakt } from './kontakt';

describe('Kontakt', () => {
  let component: Kontakt;
  let fixture: ComponentFixture<Kontakt>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Kontakt]
    })
    .compileComponents();

    fixture = TestBed.createComponent(Kontakt);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
