import { DichiarazioneConsumiPatchDto } from './../models/dto/DichiarazioneConsumiDto';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { DichiarazioneConsumiDto } from '../models/dto/DichiarazioneConsumiDto';
import { DtoBuilderService } from './dto-builder.service';
import { HttpClientUmaCoreService } from './http-client-uma-core.service';
import { ConsuntivoDto } from '../models/dto/ConsuntivoDto';
import { StatoDichiarazioneConsumiEnum } from 'src/app/a4g-common/classi/enums/uma/StatoDichiarazioneConsumi.enum';
import { PaginatorA4G } from 'src/app/a4g-common/interfaces/paginator.model';
import { DomandaUmaFilter } from '../models/dto/DomandaUmaFilter';
import { HttpHelperService } from 'src/app/a4g-common/services/http-helper.service';
import { DomandaUmaDto } from '../models/dto/DomandaUmaDto';
import { OAuthService } from 'angular-oauth2-oidc';

@Injectable({
  providedIn: 'root'
})
export class HttpClientDichiarazioneConsumiUmaService {

  readonly CTX_PATH = '/consumi';

  constructor(
    private http: HttpClient,
    private oauthService: OAuthService,
    private httpClientCore: HttpClientUmaCoreService,
    private dtoBuilderService: DtoBuilderService,
    private httpHelperService: HttpHelperService) { }

  public getAccessToken() {
    return this.oauthService.getAccessToken();
  }

  presentaDichiarazioneConsumi(cuaa: string, codiceFiscaleRichiedente: string): Observable<number> {
    let headers = new HttpHeaders().append('Authorization', this.getAccessToken());
    return this.http.post<number>(this.urlConsumi(), this.dtoBuilderService.buildPresentaDomandaDto(cuaa, codiceFiscaleRichiedente), { headers: headers });
  }

  getDichiarazioniConsumi(filter: DomandaUmaFilter): Observable<Array<DichiarazioneConsumiDto>> {
    const queryString = filter != null ? '?' + this.httpHelperService.buildQueryStringFromObject(filter) : '';
    let headers = new HttpHeaders().append('Authorization', this.getAccessToken());
    return this.http.get<Array<DichiarazioneConsumiDto>>(`${this.urlConsumi()}` + queryString, { headers: headers });
  }

  getDichiarazioniConsumiCaa(filter: DomandaUmaFilter): Observable<Array<DomandaUmaDto>> {
    const queryString = filter != null ? '?' + this.httpHelperService.buildQueryStringFromObject(filter) : '';
    let headers = new HttpHeaders().append('Authorization', this.getAccessToken());
    return this.http.get<Array<DomandaUmaDto>>(`${this.urlConsumi()}/caa` + queryString, { headers: headers });
  }

  getDichiarazioniConsumiPaged(filter: DomandaUmaFilter): Observable<PaginatorA4G<Array<DichiarazioneConsumiDto>>> {
    const queryString = filter != null ? '?' + this.httpHelperService.buildQueryStringFromObject(filter) : '';
    let headers = new HttpHeaders().append('Authorization', this.getAccessToken());
    return this.http.get<PaginatorA4G<Array<DichiarazioneConsumiDto>>>(`${this.urlConsumi()}/paged` + queryString, { headers: headers });
  }

  getDichiarazioneConsumiById(id: string): Observable<DichiarazioneConsumiDto> {
    let headers = new HttpHeaders().append('Authorization', this.getAccessToken());
    return this.http.get<DichiarazioneConsumiDto>(`${this.urlConsumi()}/${id}`, { headers: headers });
  }

  getResiduoAnnoPrecedente(cuaa: string, campagna: number, stati: Array<keyof typeof StatoDichiarazioneConsumiEnum>): Observable<Array<DichiarazioneConsumiDto>> {
    const queryString = this.dtoBuilderService.buildDichiarazioneConsumiFilterDto(cuaa, campagna, stati) != null ? '?' + this.httpHelperService.buildQueryStringFromObject(this.dtoBuilderService.buildDichiarazioneConsumiFilterDto(cuaa, campagna, stati)) : '';
    let headers = new HttpHeaders().append('Authorization', this.getAccessToken());
    return this.http.get<Array<DichiarazioneConsumiDto>>(`${this.urlConsumi()}` + queryString, { headers: headers });
  }

  getConsuntivi(id: string): Observable<Array<ConsuntivoDto>> {
    let headers = new HttpHeaders().append('Authorization', this.getAccessToken());
    return this.http.get<Array<ConsuntivoDto>>(`${this.urlConsumi()}/${id}/consuntivi`, { headers: headers });
  }

  getAmmissibile(id: string): Observable<Array<ConsuntivoDto>> {
    let headers = new HttpHeaders().append('Authorization', this.getAccessToken());
    return this.http.get<Array<ConsuntivoDto>>(`${this.urlConsumi()}/${id}/carburante`, { headers: headers });
  }

  getAllegatoConsuntivo(id: string, idConsuntivo: string, idAllegato: string): Observable<any> {
    let headers = new HttpHeaders().append('Authorization', this.getAccessToken());
    return this.http.get(`${this.urlConsumi()}/${id}/consuntivi/${idConsuntivo}/allegati/${idAllegato}/stampa`, { responseType: 'blob', headers: headers });
  }

  valida(id: string): Observable<any> {
    let headers = new HttpHeaders().append('Authorization', this.getAccessToken());
    return this.http.post(`${this.urlConsumi()}/${id}/valida`, null, { headers: headers });
  }

  /** @deprecated */
  saveConsuntivo(idDichiarazione: string, consuntivo: ConsuntivoDto | any, allegati: Array<any>): Observable<string> {
    const formData = new FormData();
    if (allegati && allegati.length) {
      allegati.forEach(allegato => {
        formData.append('allegati', allegato);
      });
    }
    formData.append('consuntivo', new Blob([JSON.stringify(consuntivo)], { type: "application/json" })); /** metadati consuntivo stringified*/
    let headers = new HttpHeaders().append('Authorization', this.getAccessToken());
    return this.http.post<string>(`${this.urlConsumi()}/${idDichiarazione}/consuntivo`, formData, { headers: headers });
  }

  /** 
   * @param idDichiarazione = id della dichiarazione a cui appartiene il consuntivo
   * @param idConsuntivo = id del consuntivo per cui devono essere aggiunti gli allegati
   * @param allegati = nuovi allegati da salvare per il consuntivo
   * @param idAllegati = allegati già esistenti per il consuntivo e che si vogliono mantenere
   * */
  saveAllegatiConsuntivo(idDichiarazione: string, consuntivo: ConsuntivoDto, allegati: Array<any>): Observable<string> {
    const formData = new FormData();
    if (allegati && allegati.length) {
      allegati.forEach(allegato => {
        formData.append('allegati', allegato);
      });
    }
    formData.append('consuntivo', new Blob([JSON.stringify(consuntivo)], { type: "application/json" })); /** metadati consuntivo stringified*/
    let headers = new HttpHeaders().append('Authorization', this.getAccessToken());
    return this.http.post<string>(`${this.urlConsumi()}/${idDichiarazione}/allegati`, formData, { headers: headers });
  }

  saveConsuntivi(idDichiarazione: string, consuntivoDtoList: Array<ConsuntivoDto>): Observable<Array<ConsuntivoDto>> {
    let headers = new HttpHeaders().append('Authorization', this.getAccessToken());
    return this.http.post<Array<ConsuntivoDto>>(`${this.urlConsumi()}/${idDichiarazione}/consuntivi`, consuntivoDtoList, { headers: headers });
  }

  updateDichiarazioneConsumi(idDichiarazione: string, dichiarazioneConsumiDto: DichiarazioneConsumiPatchDto): Observable<void> {
    let headers = new HttpHeaders().append('Authorization', this.getAccessToken());
    return this.http.put<void>(`${this.urlConsumi()}/${idDichiarazione}`, dichiarazioneConsumiDto, { headers: headers });
  }

  deleteConsuntivo(id: string, idConsuntivo: string): Observable<void> {
    let headers = new HttpHeaders().append('Authorization', this.getAccessToken());
    return this.http.delete<void>(`${this.urlConsumi()}/${id}/consuntivi/${idConsuntivo}`, { headers: headers });
  }

  deleteDichiarazioneConsumi(id: string): Observable<void> {
    let headers = new HttpHeaders().append('Authorization', this.getAccessToken());
    return this.http.delete<void>(`${this.urlConsumi()}/${id}`, { headers: headers });
  }

  getDichiarazioneConsumiFile(idDichiarazione: string): Observable<any> {
    let headers = new HttpHeaders().append('Authorization', this.getAccessToken());
    return this.http.get(`${this.urlConsumi()}/${idDichiarazione}/stampa`, { responseType: 'blob', headers: headers });
  }

  protocollaDichiarazioneConsumi(id: number, documento: File, haFirma: boolean): Observable<number> {
    const queryString = haFirma != null ? '?' + this.httpHelperService.buildQueryStringFromObject({ haFirma }) : '';
    const formInput: FormData = new FormData();
    formInput.append('documento', documento);
    let headers = new HttpHeaders().append('Authorization', this.getAccessToken());
    return this.http.post<number>(`${this.urlConsumi()}/${id}/protocolla` + queryString, formInput, { headers: headers });
  }

  urlConsumi() {
    return this.httpClientCore.HOST + this.httpClientCore.BASE_PATH + this.httpClientCore.API_V1 + this.CTX_PATH;
  }

}
