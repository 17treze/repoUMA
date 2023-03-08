/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { PopoutServiceService } from './PopoutService.service';

describe('Service: PopoutService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [PopoutServiceService]
    });
  });

  it('should ...', inject([PopoutServiceService], (service: PopoutServiceService) => {
    expect(service).toBeTruthy();
  }));
});
