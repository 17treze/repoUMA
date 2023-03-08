import { TestBed, inject } from '@angular/core/testing';

import { DomandaIntegrativaService } from './domanda-integrativa.service';

describe('DomandaIntegrativaService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [DomandaIntegrativaService]
    });
  });

  it('should be created', inject([DomandaIntegrativaService], (service: DomandaIntegrativaService) => {
    expect(service).toBeTruthy();
  }));
});
