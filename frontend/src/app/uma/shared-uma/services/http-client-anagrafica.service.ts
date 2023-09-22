import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { FascicoloLazio } from 'src/app/a4g-common/classi/FascicoloLazio';
import { Configuration } from 'src/app/app.constants';
import { OAuthService } from 'angular-oauth2-oidc';

@Injectable({
  providedIn: 'root'
})
export class HttpClientAnagraficaService {

constructor(
  private http: HttpClient,
  private oauthService: OAuthService,
  private configuration: Configuration
) { } 

anagrafe_unica_lazio = `${this.configuration.anagrafe_unica_lazio}`;

public getAccessToken() {
  return this.oauthService.getAccessToken();
}

public getFascicoloLazio(cuaa: string): Observable<FascicoloLazio> {
  console.log('ricercaFascicoloLazio ' + cuaa);
  // let headers = new HttpHeaders().append('Authorization', this.getAccessToken());
  let headers = new HttpHeaders().append('Content-Type', 'application/json');
  return this.http.get<FascicoloLazio>(this.getUrlGetFascicoloLazio(cuaa), { headers: headers });
}

public getUrlGetFascicoloLazio(cuaa: string): string {
  return `${this.anagrafe_unica_lazio}/fascicoloFS6/trovaFascicoloFS6?cuaa=${cuaa}`;
}

}
