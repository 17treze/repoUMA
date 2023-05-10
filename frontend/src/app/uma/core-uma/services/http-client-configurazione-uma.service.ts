import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { GruppoColtureDto } from '../../core-uma/models/dto/GruppoColtureDto';
import { HttpClientUmaCoreService } from './http-client-uma-core.service';
import { PaginatorA4G } from 'src/app/a4g-common/interfaces/paginator.model';
import { Paginazione } from 'src/app/a4g-common/utility/paginazione';

@Injectable({
  providedIn: 'root'
})
export class HttpClientConfigurazioneUmaService {

  readonly CTX_PATH = '/configurazioni';

  constructor(
    private http: HttpClient,
    private httpClientCore: HttpClientUmaCoreService
  ) { }

  public getGruppiColture(paginazione: Paginazione): Observable<PaginatorA4G<Array<GruppoColtureDto>>> {
    const data: any = { ...paginazione };
    return this.http.get<PaginatorA4G<Array<GruppoColtureDto>>>(`${this.urlBase()}/gruppi-colture`, { params: data });
  }

  urlBase() {
    return this.httpClientCore.HOST + this.httpClientCore.BASE_PATH + this.httpClientCore.API_V1 + this.CTX_PATH;
  }

}