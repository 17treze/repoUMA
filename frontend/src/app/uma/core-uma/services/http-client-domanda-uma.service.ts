import { PaginatorA4G } from './../../../a4g-common/interfaces/paginator.model';
import { CarburanteDto } from 'src/app/uma/core-uma/models/dto/CarburanteDto';
import { DtoBuilderService } from './dto-builder.service';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { HttpClientUmaCoreService } from './http-client-uma-core.service';
import { Observable } from 'rxjs/internal/Observable';
import { RichiestaCarburanteDto } from '../models/dto/RichiestaCarburanteDto';
import { DomandaUmaFilter } from '../models/dto/DomandaUmaFilter';
import { HttpHelperService } from '../../../a4g-common/services/http-helper.service';
import { PrelievoDto } from '../models/dto/PrelievoDto';
import { CarburanteTotale } from '../models/dto/CarburanteTotale';
import { DomandaUmaDto } from '../models/dto/DomandaUmaDto';
import { OAuthService } from 'angular-oauth2-oidc';

@Injectable({
  providedIn: 'root'
})
export class HttpClientDomandaUmaService {

  readonly CTX_PATH = '/richieste';

  constructor(
    private http: HttpClient,
    private oauthService: OAuthService,
    private httpClientCore: HttpClientUmaCoreService,
    private dtoBuilderService: DtoBuilderService,
    private httpHelperService: HttpHelperService
  ) {
  }

  public getAccessToken() {
    return this.oauthService.getAccessToken();
  }

  presentaDomanda(cuaa: string, codiceFiscaleRichiedente: string): Observable<number> {
    let headers = new HttpHeaders().append('Authorization', this.getAccessToken());
    return this.http.post<number>(this.urlDomanda(), this.dtoBuilderService.buildPresentaDomandaDto(cuaa, codiceFiscaleRichiedente), { headers: headers });
  }

  getDomande(filter: DomandaUmaFilter): Observable<Array<RichiestaCarburanteDto>> {
    const queryString = filter != null ? '?' + this.httpHelperService.buildQueryStringFromObject(filter) : '';
    let headers = new HttpHeaders().append('Authorization', this.getAccessToken());
    return this.http.get<Array<RichiestaCarburanteDto>>(this.urlDomanda() + queryString, { headers: headers });
  }

  getDomandeCaa(filter: DomandaUmaFilter): Observable<Array<DomandaUmaDto>> {
    const queryString = filter != null ? '?' + this.httpHelperService.buildQueryStringFromObject(filter) : '';
    let headers = new HttpHeaders().append('Authorization', this.getAccessToken());
    return this.http.get<Array<DomandaUmaDto>>(`${this.urlDomanda()}/caa` + queryString, { headers: headers });
  }

  getDomandePaged(filter: DomandaUmaFilter): Observable<PaginatorA4G<Array<RichiestaCarburanteDto>>> {
    const queryString = filter != null ? '?' + this.httpHelperService.buildQueryStringFromObject(filter) : '';
    let headers = new HttpHeaders().append('Authorization', this.getAccessToken());
    return this.http.get<PaginatorA4G<Array<RichiestaCarburanteDto>>>(`${this.urlDomanda()}/paged` + queryString, { headers: headers });
  }

  getDomandaById(idDomanda: string): Observable<RichiestaCarburanteDto> {
    let headers = new HttpHeaders().append('Authorization', this.getAccessToken());
    return this.http.get<RichiestaCarburanteDto>(`${this.urlDomanda()}/${idDomanda}`, { headers: headers });
  }

  deleteDomandaById(idDomanda: string): Observable<void> {
    let headers = new HttpHeaders().append('Authorization', this.getAccessToken());
    return this.http.delete<void>(`${this.urlDomanda()}/${idDomanda}`, { headers: headers });
  }

  protocollaDomanda(id: number, documento: File, haFirma: boolean): Observable<number> {
    const queryString = haFirma != null ? '?' + this.httpHelperService.buildQueryStringFromObject({ haFirma }) : '';
    const formInput: FormData = new FormData();
    formInput.append('documento', documento);
    let headers = new HttpHeaders().append('Authorization', this.getAccessToken());
    return this.http.post<number>(`${this.urlDomanda()}/${id}/protocolla` + queryString, formInput, { headers: headers });
  }

  getPrelieviByCuaaAndCampagna(cuaa: string, campagna: string, dataPresentazione?: string): Observable<CarburanteTotale<PrelievoDto>> {
    const queryString = '?' + this.httpHelperService.buildQueryStringFromObject({ cuaa, dataPresentazione });
    let headers = new HttpHeaders().append('Authorization', this.getAccessToken());
    return this.http.get<CarburanteTotale<PrelievoDto>>(`${this.urlDomanda()}/${campagna}/prelievi`+ queryString, { headers: headers });
  }

  getCarburanteAmmissibile(idDomanda: string): Observable<CarburanteDto> {
    let headers = new HttpHeaders().append('Authorization', this.getAccessToken());
    return this.http.get<CarburanteDto>(`${this.urlDomanda()}/${idDomanda}/carburante`, { headers: headers });
  }

  getRichiestaCarburanteFile(idDomanda: string): Observable<any> {
    return this.http.get(`${this.urlDomanda()}/${idDomanda}/stampa`, { responseType: 'blob' });
  }

  urlDomanda() {
    return this.httpClientCore.HOST + this.httpClientCore.BASE_PATH + this.httpClientCore.API_V1 + this.CTX_PATH;
  }
}
