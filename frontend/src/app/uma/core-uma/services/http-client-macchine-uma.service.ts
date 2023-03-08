import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { MacchinaDto } from '../../core-uma/models/dto/MacchinaDto';
import { HttpClientUmaCoreService } from './http-client-uma-core.service';

@Injectable({
  providedIn: 'root'
})
export class HttpClientMacchineUmaService {

  readonly CTX_PATH = '/richieste';

  constructor(
    private http: HttpClient,
    private httpClientCore: HttpClientUmaCoreService
  ) { }

  dichiaraMacchinariDomanda(idDomanda: string, macchine: Array<MacchinaDto>): Observable<Array<MacchinaDto>> {
    return this.http.post<Array<MacchinaDto>>(`${this.urlDomanda()}/${idDomanda}/macchine`, macchine);
  }

  validaRichiestaCarburante(idDomanda: string): Observable<any> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.post<any>(`${this.urlDomanda()}/${idDomanda}/valida`, { headers: headers });
  }

  getMacchineByIdDomanda(idDomanda: string): Observable<Array<MacchinaDto>> {
    return this.http.get<Array<MacchinaDto>>(`${this.urlDomanda()}/${idDomanda}/macchine`);
  }

  urlDomanda() {
    return this.httpClientCore.HOST + this.httpClientCore.BASE_PATH + this.httpClientCore.API_V1 + this.CTX_PATH;
  }

}
