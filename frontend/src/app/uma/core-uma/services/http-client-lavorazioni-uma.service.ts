import { RichiestaCarburanteDto } from 'src/app/uma/core-uma/models/dto/RichiestaCarburanteDto';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AmbitoLavorazioneEnum } from '../../core-uma/models/dto/LavorazioneFilterDto';
import { RaggruppamentoLavorazioneDto } from '../../core-uma/models/dto/RaggruppamentoDto';
import { HttpClientUmaCoreService } from './http-client-uma-core.service';
import { HttpHelperService } from '../../../a4g-common/services/http-helper.service';
import { DichiarazioneDto } from '../models/dto/DichiarazioneDto';
import { DichiarazioneFabbricatoDto } from '../models/dto/DichiarazioneFabbricatoDto';
import { CarburanteRichiestoDto } from '../models/dto/CarburanteRichiestoDto';
import { TipoCarburante } from '../models/enums/TipoCarburante.enum';
import { OAuthService } from 'angular-oauth2-oidc';

@Injectable({
  providedIn: 'root'
})
export class HttpClientLavorazioniUmaService {

  readonly CTX_PATH = '/richieste';

  constructor(
    private http: HttpClient,
    private oauthService: OAuthService,
    private httpClientCore: HttpClientUmaCoreService,
    private httpHelperService: HttpHelperService) { }

  public getAccessToken() {
    return this.oauthService.getAccessToken();
  }
  
  getLavorazioni(idRichiesta: string, ambito: AmbitoLavorazioneEnum): Observable<Array<RaggruppamentoLavorazioneDto>> {
    const queryString = ambito != null ? '?' + this.httpHelperService.buildQueryStringFromObject({ambito}) : '';
    let headers = new HttpHeaders().append('Authorization', this.getAccessToken());
    return this.http.get<Array<RaggruppamentoLavorazioneDto>>(`${this.urlRichiesta()}/${idRichiesta}/lavorazioni` + queryString, { headers: headers });
  }

  getDichiarazioni(idRichiesta: string, ambito: AmbitoLavorazioneEnum): Observable<Array<DichiarazioneDto>> {
    const queryString = ambito != null ? '?' + this.httpHelperService.buildQueryStringFromObject({ambito}) : '';
    let headers = new HttpHeaders().append('Authorization', this.getAccessToken());
    return this.http.get<Array<DichiarazioneDto>>(`${this.urlRichiesta()}/${idRichiesta}/fabbisogni` + queryString, { headers: headers });
  }

  getFabbisogniFabbricati(idRichiesta: string, ambito: AmbitoLavorazioneEnum): Observable<Array<DichiarazioneFabbricatoDto>> {
    const queryString = ambito != null ? '?' + this.httpHelperService.buildQueryStringFromObject({ambito}) : '';
    let headers = new HttpHeaders().append('Authorization', this.getAccessToken());
    return this.http.get<Array<DichiarazioneFabbricatoDto>>(`${this.urlRichiesta()}/${idRichiesta}/fabbisogni-fabbricati` + queryString, { headers: headers });
  }

  saveDomandaUma(richiesta: RichiestaCarburanteDto): Observable<any> {
    return this.http.post<any>(`${this.urlRichiesta()}/${richiesta.id}`, richiesta);
  }

  updateDomandaUma(idRichiesta: string, carburanteRichiesto: CarburanteRichiestoDto): Observable<any> {
    let headers = new HttpHeaders().append('Authorization', this.getAccessToken());
    return this.http.put<any>(`${this.urlRichiesta()}/${idRichiesta}`, carburanteRichiesto, { headers: headers });
  }

  saveFabbisogni(idRichiesta: string, fabbisogni: Array<DichiarazioneDto>): Observable<any> {
    return this.http.post<any>(`${this.urlRichiesta()}/${idRichiesta}/fabbisogni`, fabbisogni);
  }

  saveFabbisogniFabbricati(idRichiesta: string, fabbisogni: Array<DichiarazioneFabbricatoDto>): Observable<any> {
    return this.http.post<any>(`${this.urlRichiesta()}/${idRichiesta}/fabbisogni-fabbricati`, fabbisogni);
  }

  deleteFabbisogniById(idRichiesta: string, tipiCarburante: Array<keyof typeof TipoCarburante>): Observable<void> {
    const queryString = tipiCarburante != null ? '?' + this.httpHelperService.buildQueryStringFromObject({tipiCarburante}) : '';
    return this.http.delete<void>(`${this.urlRichiesta()}/${idRichiesta}/fabbisogni` + queryString);
  }

  getDomandaById(idRichiesta: string): Observable<RichiestaCarburanteDto> {
    let headers = new HttpHeaders().append('Authorization', this.getAccessToken());
    return this.http.get<RichiestaCarburanteDto>(`${this.urlRichiesta()}/${idRichiesta}`, { headers: headers });
  }

  urlRichiesta() {
    return this.httpClientCore.HOST + this.httpClientCore.BASE_PATH + this.httpClientCore.API_V1 + this.CTX_PATH;
  }
}
