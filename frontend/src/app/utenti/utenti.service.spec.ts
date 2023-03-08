import { TestBed, inject } from '@angular/core/testing';

import { UtentiService } from './utenti.service';

describe('UtentiService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [UtentiService]
    });
  });

  it('should be created', inject([UtentiService], (service: UtentiService) => {
    expect(service).toBeTruthy();
  }));
});
