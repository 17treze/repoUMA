import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { MacchinaDto } from '../../core-uma/models/dto/MacchinaDto';
import { HttpClientUmaCoreService } from './http-client-uma-core.service';
import { OAuthService } from 'angular-oauth2-oidc';

@Injectable({
  providedIn: 'root'
})
export class HttpClientMacchineUmaService {

  readonly CTX_PATH = '/richieste';

  constructor(
    private http: HttpClient,
    private oauthService: OAuthService,
    private httpClientCore: HttpClientUmaCoreService
  ) { }

  public getAccessToken() {
    return this.oauthService.getAccessToken();
  }

  dichiaraMacchinariDomanda(idDomanda: string, macchine: Array<MacchinaDto>): Observable<Array<MacchinaDto>> {
    let headers = new HttpHeaders().append('Authorization', this.getAccessToken());
    return this.http.post<Array<MacchinaDto>>(`${this.urlDomanda()}/${idDomanda}/macchine`, macchine, { headers: headers });
  }

  validaRichiestaCarburante(idDomanda: string): Observable<any> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.post<any>(`${this.urlDomanda()}/${idDomanda}/valida`, { headers: headers });
  }

  getMacchineByIdDomanda(idDomanda: string): Observable<Array<MacchinaDto>> {
    let headers = new HttpHeaders().append('Authorization', this.getAccessToken());
    return this.http.get<Array<MacchinaDto>>(`${this.urlDomanda()}/${idDomanda}/macchine`, { headers: headers });
  }

  urlDomanda() {
    return this.httpClientCore.HOST + this.httpClientCore.BASE_PATH + this.httpClientCore.API_V1 + this.CTX_PATH;
  }

}
