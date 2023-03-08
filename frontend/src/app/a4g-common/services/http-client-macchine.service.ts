import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { DettaglioMacchinaDto } from '../classi/dto/dotazione-tecnica/DettaglioMacchinaDto';
import { MacchinaDto } from '../classi/dto/dotazione-tecnica/MacchinaDto';
import { HttpClientCoreService } from './http-client-core.service';
import { HttpHelperService } from './http-helper.service';

@Injectable({
  providedIn: 'root'
})
export class HttpClientMacchineService {

  readonly CTX_PATH = '/macchine';

  constructor(
    private http: HttpClient,
    private httpClientCore: HttpClientCoreService,
    private httpHelperService: HttpHelperService
  ) { }

  // Reperimento dati generici inerenti alle macchine del fascicolo
  getMacchine(cuaa: string, idValidazione?: string): Observable<Array<MacchinaDto>> {
    const queryString = idValidazione ? '?' + this.httpHelperService.buildQueryStringFromObject({idValidazione}) : '';
    return this.http.get<Array<MacchinaDto>>(`${this.urlMacchine()}/${cuaa}${this.CTX_PATH}` + queryString);
  }

  // Reperimento dati di dettaglio di una macchina
  getMacchinaById(cuaa: string, idMacchina: string, idValidazione?: string): Observable<DettaglioMacchinaDto> {
    const queryString = idValidazione ? '?' + this.httpHelperService.buildQueryStringFromObject({idValidazione}) : '';
    return this.http.get<DettaglioMacchinaDto>(`${this.urlMacchine()}/${cuaa}${this.CTX_PATH}/${idMacchina}` + queryString);
  }

  // Recupera il documento di possesso associato ad una macchina
  getAllegato(cuaa: string, idMacchina: string, idValidazione?: string): Observable<any> {
    const queryString = idValidazione ? '?' + this.httpHelperService.buildQueryStringFromObject({idValidazione}) : '';
    return this.http.get(`${this.urlMacchine()}/${cuaa}${this.CTX_PATH}/${idMacchina}/allegati` + queryString, { responseType: 'blob' });
  }

  private urlMacchine() {
    return this.httpClientCore.HOST + this.httpClientCore.BASE_PATH_DOTAZIONE_TECNICA + this.httpClientCore.API_V1 + this.httpClientCore.BASE_PATH_FASCICOLO;
  }

}
