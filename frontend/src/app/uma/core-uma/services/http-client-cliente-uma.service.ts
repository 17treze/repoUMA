import { DichiarazioneDto } from './../models/dto/DichiarazioneDto';
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ClienteConsumiDto } from '../models/dto/ClienteConsumiDto';
import { ClienteDto } from '../models/dto/ClienteDto';
import { FatturaClienteDto } from '../models/dto/FatturaClienteDto';
import { RaggruppamentoLavorazioneDto } from '../models/dto/RaggruppamentoDto';
import { HttpClientUmaCoreService } from './http-client-uma-core.service';

@Injectable({
  providedIn: 'root'
})
export class HttpClientClienteUmaService {

  readonly CTX_BASE_PATH = '/consumi';
  readonly CTX_PATH = '/clienti';

  constructor(
    private http: HttpClient,
    private httpClientCore: HttpClientUmaCoreService
  ) { }

  postAllegatiClientiContoterzi(idDichiarazione: number, idCliente: number, allegati: Array<File>): Observable<any> {
    const formInput: FormData = new FormData();
    allegati.forEach(allegato => {
      formInput.append('allegati', allegato);
    });
    return this.http.post<any>(`${this.urlConsumi()}/${idDichiarazione}/clienti/${idCliente}/allegati`, formInput);
  }

  getAllegatiClientiContoterzi(idDichiarazione: number, idCliente: number): Observable<Array<FatturaClienteDto>> {
    return this.http.get<Array<FatturaClienteDto>>(`${this.urlConsumi()}/${idDichiarazione}/clienti/${idCliente}/allegati`);
  }

  validaClientiContoterzi(idDichiarazione: number, idFascicolo: number): Observable<void> {
    return this.http.post<void>(`${this.urlConsumi()}/${idDichiarazione}/clienti/valida?idFascicolo=${idFascicolo}`, {});
  }

  getLavorazioniClientiContoTerzi(idDichiarazione: string, idCliente: number): Observable<Array<RaggruppamentoLavorazioneDto>> {
    return this.http.get<Array<RaggruppamentoLavorazioneDto>>(`${this.urlConsumi()}/${idDichiarazione}${this.CTX_PATH}/${idCliente}/lavorazioni`);
  }

  getFabbisogniRichiestaCliente(idDichiarazione: string, idCliente: number): Observable<Array<DichiarazioneDto>> {
    return this.http.get<Array<DichiarazioneDto>>(`${this.urlConsumi()}/${idDichiarazione}${this.CTX_PATH}/${idCliente}/fabbisogni-richiesta`);
  }

  getFabbisogniContoTerzista(idDichiarazione: string, idCliente: number): Observable<Array<DichiarazioneDto>> {
    return this.http.get<Array<DichiarazioneDto>>(`${this.urlConsumi()}/${idDichiarazione}${this.CTX_PATH}/${idCliente}/fabbisogni`);
  }

  saveLavorazioniClientiContoTerzi(idDichiarazione: string, idCliente: number, dichiarazioni: Array<DichiarazioneDto>): Observable<void> {
    return this.http.post<void>(`${this.urlConsumi()}/${idDichiarazione}${this.CTX_PATH}/${idCliente}/fabbisogni`, dichiarazioni);
  }

  getClientiContoterzi(idDomanda: number): Observable<ClienteDto[]> {
    return this.http.get<ClienteDto[]>(`${this.urlConsumi()}/${idDomanda}/clienti`);
  }

  postClientiContoterzi(idDichiarazione: number, idFascicolo: number, allegati: Array<File>): Observable<any> {
    const formInput: FormData = new FormData();
    allegati.forEach(allegato => {
      formInput.append('allegati', allegato);
    });
    return this.http.post<any>(`${this.urlConsumi()}/${idDichiarazione}/clienti?idFascicolo=${idFascicolo}`, formInput);
  }

  getCliente(idDichiarazione: string, idCliente: number): Observable<ClienteConsumiDto> {
    return this.http.get<ClienteConsumiDto>(`${this.urlConsumi()}/${idDichiarazione}${this.CTX_PATH}/${idCliente}`);
  }

  deleteCliente(idDichiarazione: number, idCliente: number): Observable<ClienteConsumiDto> {
    return this.http.delete<ClienteConsumiDto>(`${this.urlConsumi()}/${idDichiarazione}${this.CTX_PATH}/${idCliente}`);
  }

  urlConsumi() {
    return this.httpClientCore.HOST + this.httpClientCore.BASE_PATH + this.httpClientCore.API_V1 + this.CTX_BASE_PATH;
  }

}
