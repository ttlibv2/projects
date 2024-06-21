import { TestBed } from '@angular/core/testing';

import { TsUiService } from './ts-ui.service';

describe('TsUiService', () => {
  let service: TsUiService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TsUiService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
