/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { CreazioneLavorazioneService } from './creazione-lavorazione.service';

describe('Service: CreazioneLavorazione', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [CreazioneLavorazioneService]
    });
  });

  it('should ...', inject([CreazioneLavorazioneService], (service: CreazioneLavorazioneService) => {
    expect(service).toBeTruthy();
  }));
});
