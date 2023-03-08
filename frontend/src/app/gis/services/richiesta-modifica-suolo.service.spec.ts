/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { RichiestaModificaSuoloService } from './richiesta-modifica-suolo.service';

describe('Service: RichiestaModificaSuolo', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [RichiestaModificaSuoloService]
    });
  });

  it('should ...', inject([RichiestaModificaSuoloService], (service: RichiestaModificaSuoloService) => {
    expect(service).toBeTruthy();
  }));
});
