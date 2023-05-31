import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class HttpClientUmaCoreService {
  readonly HOST: string = 'http://localhost:9012/'; // environment.backendUrl;
  readonly BASE_PATH: string = 'uma';
  readonly API_V1: string = '/api/v1';
  constructor() { }



}
