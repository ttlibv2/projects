import { TestBed } from '@angular/core/testing';

import { TslibService } from './tslib.service';

describe('TslibService', () => {
  let service: TslibService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TslibService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
