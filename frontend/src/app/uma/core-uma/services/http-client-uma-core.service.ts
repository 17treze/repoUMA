import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class HttpClientUmaCoreService {
  readonly HOST: string = environment.backendUrl;
  readonly BASE_PATH: string = 'uma';
  readonly API_V1: string = '/api/v1';
  constructor() { }



}
