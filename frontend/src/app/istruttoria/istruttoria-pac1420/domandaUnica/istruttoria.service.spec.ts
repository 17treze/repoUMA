import { TestBed, inject } from '@angular/core/testing';

import { IstruttoriaService } from './istruttoria.service';

describe('IstruttoriaService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [IstruttoriaService]
    });
  });

  it('should be created', inject([IstruttoriaService], (service: IstruttoriaService) => {
    expect(service).toBeTruthy();
  }));
});
