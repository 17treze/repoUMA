import { TestBed, inject } from '@angular/core/testing';

import { SearchGisService } from './search-gis.service';

describe('SearchGisService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [SearchGisService]
    });
  });

  it('should be created', inject([SearchGisService], (service: SearchGisService) => {
    expect(service).toBeTruthy();
  }));
});
