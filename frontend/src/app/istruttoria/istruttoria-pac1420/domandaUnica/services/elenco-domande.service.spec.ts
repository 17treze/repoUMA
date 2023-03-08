import { TestBed, inject } from '@angular/core/testing';

import { ElencoDomandeService } from './elenco-domande.service';

describe('ElencoDomandeService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ElencoDomandeService]
    });
  });

  it('should be created', inject([ElencoDomandeService], (service: ElencoDomandeService) => {
    expect(service).toBeTruthy();
  }));
});
