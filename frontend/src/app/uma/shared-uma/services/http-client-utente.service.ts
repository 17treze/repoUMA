import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { HttpClientUmaCoreService } from '../../core-uma/services/http-client-uma-core.service';
import { Observable } from 'rxjs';
import { UtenteDistributoreDto } from '../../core-uma/models/dto/DistributoreDto';
import { OAuthService } from 'angular-oauth2-oidc';

@Injectable({
  providedIn: 'root'
})
export class HttpClientUtenteService {

  readonly BASE_PATH = 'a4gutente';
  readonly CTX_PATH = '/utenti';

  constructor(
    private http: HttpClient,
    private oauthService: OAuthService,
    private httpClientCore: HttpClientUmaCoreService
  ) { }

  public getAccessToken() {
    return this.oauthService.getAccessToken();
  }

  public getDistributori(campagna: string): Observable<Array<UtenteDistributoreDto>> {
    let headers = new HttpHeaders().append('Authorization', this.getAccessToken());
    return this.http.get<Array<UtenteDistributoreDto>>(`${this.urlUtente()}/utente/distributori?campagna=${campagna}`, { headers: headers });
  }

  private urlUtente() {
    return 'http://localhost:8080/' + this.BASE_PATH + this.httpClientCore.API_V1 + this.CTX_PATH; // this.httpClientCore.HOST
  }
}
