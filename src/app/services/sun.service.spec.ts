import { TestBed } from '@angular/core/testing';

import { SunService } from './sun.service';

describe('SunService', () => {
  let service: SunService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SunService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
