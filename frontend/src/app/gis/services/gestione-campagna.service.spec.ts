/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { GestioneCampagnaService } from './gestione-campagna.service';

describe('Service: GestioneCampagna', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [GestioneCampagnaService]
    });
  });

  it('should ...', inject([GestioneCampagnaService], (service: GestioneCampagnaService) => {
    expect(service).toBeTruthy();
  }));
});
