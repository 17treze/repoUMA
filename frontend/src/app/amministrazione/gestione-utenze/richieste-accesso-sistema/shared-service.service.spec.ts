import { TestBed, inject } from '@angular/core/testing';

import { SharedService } from './shared-service.service';

describe('UpdateCountersService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [SharedService]
    });
  });

  it('should be created', inject([SharedService], (service: SharedService) => {
    expect(service).toBeTruthy();
  }));
});
