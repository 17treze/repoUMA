/* tslint:disable:no-unused-variable */

import { TestBed, inject } from '@angular/core/testing';
import { AllegatiGisService } from './allegati-gis.service';

describe('Service: AllegatiGis', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [AllegatiGisService]
    });
  });

  it('should ...', inject([AllegatiGisService], (service: AllegatiGisService) => {
    expect(service).toBeTruthy();
  }));
});
