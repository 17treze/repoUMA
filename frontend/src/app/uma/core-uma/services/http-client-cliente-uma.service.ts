import { DichiarazioneDto } from './../models/dto/DichiarazioneDto';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ClienteConsumiDto } from '../models/dto/ClienteConsumiDto';
import { ClienteDto } from '../models/dto/ClienteDto';
import { FatturaClienteDto } from '../models/dto/FatturaClienteDto';
import { RaggruppamentoLavorazioneDto } from '../models/dto/RaggruppamentoDto';
import { HttpClientUmaCoreService } from './http-client-uma-core.service';
import { OAuthService } from 'angular-oauth2-oidc';

@Injectable({
  providedIn: 'root'
})
export class HttpClientClienteUmaService {

  readonly CTX_BASE_PATH = '/consumi';
  readonly CTX_PATH = '/clienti';

  constructor(
    private http: HttpClient,
    private oauthService: OAuthService,
    private httpClientCore: HttpClientUmaCoreService
  ) { }

  public getAccessToken() {
    return this.oauthService.getAccessToken();
  }
  
  postAllegatiClientiContoterzi(idDichiarazione: number, idCliente: number, allegati: Array<File>): Observable<any> {
    const formInput: FormData = new FormData();
    allegati.forEach(allegato => {
      formInput.append('allegati', allegato);
    });
    return this.http.post<any>(`${this.urlConsumi()}/${idDichiarazione}/clienti/${idCliente}/allegati`, formInput);
  }

  getAllegatiClientiContoterzi(idDichiarazione: number, idCliente: number): Observable<Array<FatturaClienteDto>> {
    let headers = new HttpHeaders().append('Authorization', this.getAccessToken());
    return this.http.get<Array<FatturaClienteDto>>(`${this.urlConsumi()}/${idDichiarazione}/clienti/${idCliente}/allegati`, { headers: headers });
  }

  validaClientiContoterzi(idDichiarazione: number, idFascicolo: number): Observable<void> {
    return this.http.post<void>(`${this.urlConsumi()}/${idDichiarazione}/clienti/valida?idFascicolo=${idFascicolo}`, {});
  }

  getLavorazioniClientiContoTerzi(idDichiarazione: string, idCliente: number): Observable<Array<RaggruppamentoLavorazioneDto>> {
    let headers = new HttpHeaders().append('Authorization', this.getAccessToken());
    return this.http.get<Array<RaggruppamentoLavorazioneDto>>(`${this.urlConsumi()}/${idDichiarazione}${this.CTX_PATH}/${idCliente}/lavorazioni`, { headers: headers });
  }

  getFabbisogniRichiestaCliente(idDichiarazione: string, idCliente: number): Observable<Array<DichiarazioneDto>> {
    let headers = new HttpHeaders().append('Authorization', this.getAccessToken());
    return this.http.get<Array<DichiarazioneDto>>(`${this.urlConsumi()}/${idDichiarazione}${this.CTX_PATH}/${idCliente}/fabbisogni-richiesta`, { headers: headers });
  }

  getFabbisogniContoTerzista(idDichiarazione: string, idCliente: number): Observable<Array<DichiarazioneDto>> {
    let headers = new HttpHeaders().append('Authorization', this.getAccessToken());
    return this.http.get<Array<DichiarazioneDto>>(`${this.urlConsumi()}/${idDichiarazione}${this.CTX_PATH}/${idCliente}/fabbisogni`, { headers: headers });
  }

  saveLavorazioniClientiContoTerzi(idDichiarazione: string, idCliente: number, dichiarazioni: Array<DichiarazioneDto>): Observable<void> {
    let headers = new HttpHeaders().append('Authorization', this.getAccessToken());
    return this.http.post<void>(`${this.urlConsumi()}/${idDichiarazione}${this.CTX_PATH}/${idCliente}/fabbisogni`, dichiarazioni, { headers: headers });
  }

  getClientiContoterzi(idDomanda: number): Observable<ClienteDto[]> {
    let headers = new HttpHeaders().append('Authorization', this.getAccessToken());
    return this.http.get<ClienteDto[]>(`${this.urlConsumi()}/${idDomanda}/clienti`, { headers: headers });
  }

  postClientiContoterzi(idDichiarazione: number, idFascicolo: number, allegati: Array<File>): Observable<any> {
    const formInput: FormData = new FormData();
    allegati.forEach(allegato => {
      formInput.append('allegati', allegato);
    });
    return this.http.post<any>(`${this.urlConsumi()}/${idDichiarazione}/clienti?idFascicolo=${idFascicolo}`, formInput);
  }

  getCliente(idDichiarazione: string, idCliente: number): Observable<ClienteConsumiDto> {
    let headers = new HttpHeaders().append('Authorization', this.getAccessToken());
    return this.http.get<ClienteConsumiDto>(`${this.urlConsumi()}/${idDichiarazione}${this.CTX_PATH}/${idCliente}`, { headers: headers });
  }

  deleteCliente(idDichiarazione: number, idCliente: number): Observable<ClienteConsumiDto> {
    let headers = new HttpHeaders().append('Authorization', this.getAccessToken());
    return this.http.delete<ClienteConsumiDto>(`${this.urlConsumi()}/${idDichiarazione}${this.CTX_PATH}/${idCliente}`, { headers: headers });
  }

  urlConsumi() {
    return this.httpClientCore.HOST + this.httpClientCore.BASE_PATH + this.httpClientCore.API_V1 + this.CTX_BASE_PATH;
  }

}
