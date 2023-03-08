import { Injectable } from '@angular/core';
import { Observable } from "rxjs";
import { DatiDomanda } from '../../utenti/classi/DatiDomanda';
import { HttpClient, HttpParams, HttpHeaders } from "@angular/common/http";
import { Configuration } from "../../app.constants";
import { RichiesteAccessoSistemaElenco } from './richieste-accesso-sistema/dto/RichiesteAccessoSistemaElenco';
import { RichiestaAccessoSistemaApprovazione } from './richieste-accesso-sistema/dto/RichiestaAccessoSistemaApprovazione';
import { Cached } from 'src/assets/decorators/cached';
import { RichiestaAccessoSistemaRifiuto } from './richieste-accesso-sistema/dto/richiesta-accesso-sistema-rifiuto';
import { FiltroRicercaRichiesteAccessoSistema } from "./richieste-accesso-sistema/dto/filtro-ricerca-richieste-accesso-sistema";
import { Paginazione } from "../../a4g-common/utility/paginazione";
import { DatiUtente } from '../ricerca-utenti/dto/Profili';
import {Params} from "@angular/router";
import {StoricoIstruttoria} from './model/storico-istruttoria';

@Injectable({
  providedIn: 'root'
})
export class GestioneUtenzeService {

  constructor(private http: HttpClient,
    private configuration: Configuration) { }

  @Cached()
  public getDatiAnagraficiUtente(id: Number): Observable<DatiDomanda> {
    return this.http.get<DatiDomanda>(
      this.configuration
        .UrlGetDomandaRegistrazioneUtente
        .replace("{id}", id.toString()));
  }

  @Cached()
  public getDatiUtenteByCodiceFiscale(codiceFiscale: string): Observable<DatiUtente> {
    return this.http.get<DatiUtente>(
      this.configuration.UrlGetUtentiCodiceFiscale
        .replace("{codiceFiscale}", codiceFiscale));
  }

  public getDatiUtenteById(idUtente: string): Observable<DatiUtente> {
    return this.http.get<DatiUtente>(
      this.configuration.UrlGetUtentiById
        .replace('{id}', idUtente));
  }

  public getRichiesteAccessoSistemaPerStato(
    filtro: FiltroRicercaRichiesteAccessoSistema,
    paginazione: Paginazione): Observable<RichiesteAccessoSistemaElenco> {
    let paramshttp: Params = filtro.getHttpParams(paginazione);
    return this.http.get<RichiesteAccessoSistemaElenco>(
      this.configuration.UrlRicercaDomanda, { params: paramshttp }
    );
  }

  public getOrdine(n: number) {
    if (n === 1) {
      return 'ASC';
    } else {
      return 'DESC';
    }
  }

  public putPresaInCarico(idDomanda: number) {
    const headers = new HttpHeaders({ "Content-Type": "application/json" });
    return this.http.put<any>(this.configuration.UrlPresaInCarico
      .replace("${id}", idDomanda.toString()),
      { headers: headers }
    )
  }

  public approvaRichiestaAccessoSistema(idDomanda: number, richiesta: RichiestaAccessoSistemaApprovazione) {
    const headers = new HttpHeaders({ "Content-Type": "application/json" });
    return this.http.put<any>(this.configuration.UrlApprovaDomanda
      .replace("${id}", idDomanda.toString()),
      JSON.stringify(richiesta),
      { headers: headers }
    )
  }

  public rifiutaRichiestaAccessoSistema(idDomanda: number, richiesta: RichiestaAccessoSistemaRifiuto) {
    const headers = new HttpHeaders({ "Content-Type": "application/json" });
    return this.http.put<any>(this.configuration.UrlRifiutaDomanda
      .replace("${id}", idDomanda.toString()),
      JSON.stringify(richiesta),
      { headers: headers }
    )
  }

  public putDatiAnagraficiUtente(id: Number): Observable<DatiDomanda> {
    return this.http.get<DatiDomanda>(
      this.configuration.UrlGetDomandaRegistrazioneUtente.replace(
        "{id}", id.toString()));
  }

  public getIstruttoriaProfiloByDomanda(idDomanda: number): Observable<RichiestaAccessoSistemaRifiuto> {
    return this.http.get<RichiestaAccessoSistemaRifiuto>(
      this.configuration.UrlGetIstruttoriaByDomanda.replace("${id}", `${idDomanda}`));
  }

  public getStorico(idUtente: string): Observable<StoricoIstruttoria> {
    return this.http.get<StoricoIstruttoria>(
      this.configuration.UrlGetStoricoIstruttoria.replace('${idUtente}', `${idUtente}`));
  }

}
