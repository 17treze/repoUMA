import { TestBed, inject } from '@angular/core/testing';

import { GestioneUtenzeService } from './gestione-utenze.service';

describe('GestioneUtenzeService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [GestioneUtenzeService]
    });
  });

  it('should be created', inject([GestioneUtenzeService], (service: GestioneUtenzeService) => {
    expect(service).toBeTruthy();
  }));
});
