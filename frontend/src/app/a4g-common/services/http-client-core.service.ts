import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class HttpClientCoreService {

  readonly HOST: string = environment.backendUrl;
  readonly BASE_PATH_UMA: string = 'uma';
  readonly BASE_PATH_FASCICOLO: string = '/fascicolo';
  readonly BASE_PATH_DOTAZIONE_TECNICA: string = 'fascicolo/dotazione-tecnica';
  readonly API_V1: string = '/api/v1';

  constructor() { }

}
