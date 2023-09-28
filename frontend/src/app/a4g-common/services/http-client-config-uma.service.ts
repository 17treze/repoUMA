import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpHelperService } from './http-helper.service';
import { Configuration } from 'src/app/app.constants';
import { OAuthService } from 'angular-oauth2-oidc';

@Injectable({
  providedIn: 'root'
})
export class HttpClientConfigUmaService {

  readonly CTX_PATH = '/configurazioni';

  constructor(
    private http: HttpClient,
    private httpHelperService: HttpHelperService,
    private oauthService: OAuthService,
    private _configuration: Configuration,
  ) { }

  uma_server = `${this._configuration.uma_server}`;

  public getAccessToken() : string {
    if (this.oauthService.hasValidAccessToken() && this.oauthService.hasValidIdToken()) {
      return this.oauthService.getAccessToken();
    }
    else {
      // rimandare al login
      return '';
    }
  }

  public getConfigurazioneUma(annoCampagna: string): Observable<string> {
    let headers = new HttpHeaders().append('Authorization', this.getAccessToken());
    const params = '?' + this.httpHelperService.buildQueryStringFromObject({ annoCampagna });
    return this.http.get<string>(`${this.urlConfigurazioni()}` + params, { headers: headers });
  }

  public postConfigurazioneUma(data: Date): Observable<number> {
    let headers = new HttpHeaders().append('Authorization', this.getAccessToken());
    return this.http.post<number>(`${this.urlConfigurazioni()}`, data, { headers: headers });
  }

  private urlConfigurazioni() {
    return this.uma_server + this.CTX_PATH;
  }

}
