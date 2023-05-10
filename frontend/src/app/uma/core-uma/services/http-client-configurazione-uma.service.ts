import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClientUmaCoreService } from './http-client-uma-core.service';
import { GruppoColtureDto } from '../models/dto/ConfigurazioneDto';

@Injectable({
  providedIn: 'root'
})
export class HttpClientConfigurazioneUmaService {

  readonly CTX_PATH = '/configurazioni';

  constructor(
    private http: HttpClient,
    private httpClientCore: HttpClientUmaCoreService
  ) { }

  getGruppiColture(): Observable<Array<GruppoColtureDto>> {
    return this.http.get<Array<GruppoColtureDto>>(`${this.urlBase()}/gruppi-colture`);
  }

  urlBase() {
    return this.httpClientCore.HOST + this.httpClientCore.BASE_PATH + this.httpClientCore.API_V1 + this.CTX_PATH;
  }

}