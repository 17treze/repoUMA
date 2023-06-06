import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { FascicoloAgsDto } from 'src/app/a4g-common/classi/FascicoloAgsDto';
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

public getAccessToken() {
  return this.oauthService.getAccessToken();
}

public getFascicoloAgs(cuaa: string): Observable<FascicoloAgsDto> {
  let headers = new HttpHeaders().append('Authorization', this.getAccessToken());
  return this.http.get<FascicoloAgsDto>(`${this.urlGetFascicoloAgsFromAnagrafica()}/${cuaa}`, { headers: headers });
}

private urlGetFascicoloAgsFromAnagrafica() {
  return this.configuration.anagrafica_server +"/fascicolo/legacy";
}

}
