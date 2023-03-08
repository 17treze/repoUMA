import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { TipoElencoEnum } from 'src/app/a4g-common/classi/enums/uma/TipoElenco.enum';
import { HttpHelperService } from 'src/app/a4g-common/services/http-helper.service';
import { HttpClientUmaCoreService } from 'src/app/uma/core-uma/services/http-client-uma-core.service';

@Injectable({
  providedIn: 'root'
})
export class HttpClientElenchiService {

  readonly CTX_PATH = '/elenchi';

  constructor(
    private http: HttpClient,
    private httpHelperService: HttpHelperService,
    private httpClientCore: HttpClientUmaCoreService
  ) { }

  getElenco(campagna: string, tipo: keyof typeof TipoElencoEnum): Observable<any> {
    const headers = new HttpHeaders({ "Content-Type": "application/json" });
    const queryString = '?' + this.httpHelperService.buildQueryStringFromObject({ tipo });
    return this.http.get(`${this.urlElenchi()}/${campagna}/stampa` + queryString, { headers, responseType: 'blob', observe: 'response' });
  }

  urlElenchi() {
    return this.httpClientCore.HOST + this.httpClientCore.BASE_PATH + this.httpClientCore.API_V1 + this.CTX_PATH;
  }

}
