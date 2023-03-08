import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClientCoreService } from './http-client-core.service';
import { HttpHelperService } from './http-helper.service';

@Injectable({
  providedIn: 'root'
})
export class HttpClientConfigUmaService {

  readonly CTX_PATH = '/configurazioni';

  constructor(
    private http: HttpClient,
    private httpClientCoreService: HttpClientCoreService,
    private httpHelperService: HttpHelperService
  ) { }

  public getConfigurazioneUma(annoCampagna: string): Observable<string> {
    const params = '?' + this.httpHelperService.buildQueryStringFromObject({ annoCampagna });
    return this.http.get<string>(`${this.urlConfigurazioni()}` + params);
  }

  public postConfigurazioneUma(data: Date): Observable<number> {
    return this.http.post<number>(`${this.urlConfigurazioni()}`, data);
  }

  private urlConfigurazioni() {
    return this.httpClientCoreService.HOST + this.httpClientCoreService.BASE_PATH_UMA + this.httpClientCoreService.API_V1 + this.CTX_PATH;
  }

}
