/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { Popout.tokenService } from './popout.token.service';

describe('Service: Popout.token', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [Popout.tokenService]
    });
  });

  it('should ...', inject([Popout.tokenService], (service: Popout.tokenService) => {
    expect(service).toBeTruthy();
  }));
});
