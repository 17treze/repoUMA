import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Fascicolo } from '../a4g-common/classi/Fascicolo';
import { InputFascicolo } from '../a4g-common/classi/InputFascicolo';
import { FascicoloAgsDto } from '../a4g-common/classi/FascicoloAgsDto';
import { PaginatorA4G } from '../a4g-common/interfaces/paginator.model';
import { Paginazione } from '../a4g-common/utility/paginazione';
import { Configuration } from '../app.constants';
import { RisultatiRicercaClientiDto } from '../uma/core-uma/models/dto/RisultatiRicercaClientiDto';
import { FascicoliValidatiFilterDto } from './fascicoli-validati/filtro-fascicoli-validati/filtro-fascicoli-validati.component';
import { CambioSportelloPatch, DatiSospensioneFascicolo, ValidazioneFascicoloDto } from './shared/fascicolo.model';
import { PersonaAgsDto } from '../uma/core-uma/models/dto/PersonaAgsDto';
import { OAuthService } from 'angular-oauth2-oidc';

@Injectable()
export class FascicoloService {

  constructor(
    private http: HttpClient,
    private oauthService: OAuthService,
    private _configuration: Configuration,
  ) { }

  anagrafica_server_fascicolo = `${this._configuration.anagrafica_server}`;
  anagrafica_server = `${this._configuration.anagrafica_server}`;
  urlGetFascicoli = `${this.anagrafica_server_fascicolo}/consultazione/fascicoli/?params=`;
  urlGetFascicoliMiei = `${this.anagrafica_server_fascicolo}/consultazione/mieifascicoli`;

  public getAccessToken() {
    return this.oauthService.getAccessToken();
  }

  public ricercaFascicoliAziendaUtente(): Observable<Array<Fascicolo>> {
    let headers = new HttpHeaders().append('Authorization', this.getAccessToken());
    return this.http.get<Array<Fascicolo>>(this.urlGetFascicoliMiei, { headers: headers });
  }

  public getUrlCambioSportello(cuaa: string, idSportello: number) {
    return `${this.anagrafica_server}/mandato/${cuaa}/sportello/${idSportello}`;
  }

  public getUrlGetFascicolo(id: number): string {
    return `${this.anagrafica_server_fascicolo}/consultazione/fascicoli/${id}`;
  }

  public getUrlGetLegacy(id: number): string {
    return `${this.anagrafica_server}/fascicolo/legacy?id=${id}`;
  }

  public getUrlGetLegacyByCuaa(cuaa: string): Observable<FascicoloAgsDto> {
    return this.http.get<FascicoloAgsDto>(`${this.anagrafica_server}/fascicolo/legacy/${cuaa}`);
  }

  public getUrlGetListaPaged(testoDaCercare: string): string {
    return `${this.anagrafica_server}/fascicolo/legacy/paged?cuaa=${testoDaCercare}&denominazione=${testoDaCercare}`;
  }

  public ricercaFascicoli(parametriRicerca: InputFascicolo): Observable<Array<Fascicolo>> {
    let json = JSON.stringify(parametriRicerca);
    return this.http.get<Array<Fascicolo>>(this.urlGetFascicoli + encodeURIComponent(json));
  }

  // chiamata più aggiornata per reperire il fascicolo legacy
  public getLegacy(idFascicolo: number): Observable<FascicoloAgsDto> {
    console.log('ricercaLegacy ' + idFascicolo);
    return this.http.get<FascicoloAgsDto>(this.getUrlGetLegacy(idFascicolo));
  }

  public getListaPaged(testoDaCercare: string, paginazione: Paginazione): Observable<PaginatorA4G<Array<RisultatiRicercaClientiDto>>> {
    console.log('ricercaListaPaged ' + testoDaCercare);
    const data: any = { ...paginazione };
    return this.http.get<PaginatorA4G<Array<RisultatiRicercaClientiDto>>>(this.getUrlGetListaPaged(testoDaCercare), { params: data });
  }

  public getFascicolo(idFascicolo: number): Observable<Fascicolo> {
    console.log('ricercaFascicolo ' + idFascicolo);
    return this.http.get<Fascicolo>(this.getUrlGetFascicolo(idFascicolo));
  }

  public putCambioSportello(cuaa, idSportello, cambioSportelloPatch: CambioSportelloPatch): Observable<any> {
    const headers = new HttpHeaders({ "Content-Type": "application/json" });
    return this.http.put<any>(
      this.getUrlCambioSportello(cuaa, idSportello),
      JSON.stringify(cambioSportelloPatch),
      { headers: headers }
    );
  }

  private getUrlFascicoliValidati(cuaa: string) {
    return `${this.anagrafica_server}/fascicolo/${cuaa}/validati`;
  }

  public getFascicoliValidati(cuaa, filtro: FascicoliValidatiFilterDto, paginazione: Paginazione): Observable<PaginatorA4G<ValidazioneFascicoloDto[]>> {
    const data: any = { ...filtro, ...paginazione };
    return this.http.get<PaginatorA4G<ValidazioneFascicoloDto[]>>(this.getUrlFascicoliValidati(cuaa), { params: data });
  }

  private getUrlDatiSospensioneFascicolo(cuaa: string) {
    return `${this.anagrafica_server}/fascicolo/${cuaa}/dati-sospensione`;
  }

  public getDatiSospensioneFascicolo(cuaa): Observable<DatiSospensioneFascicolo[]> {
    return this.http.get<DatiSospensioneFascicolo[]>(this.getUrlDatiSospensioneFascicolo(cuaa));
  }

  public getEredi(cuaa: string): Observable<Array<PersonaAgsDto>> {
    return this.http.get<Array<PersonaAgsDto>>(this.getUrlFascioloLegacy() + `/${cuaa}/eredi`);
  }

  private getUrlFascioloLegacy() {
    return this.anagrafica_server + '/fascicolo' + '/legacy';
  }

}
