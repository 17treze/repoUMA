import { Injectable, ComponentFactoryResolver } from '@angular/core';

import { Observable, of } from 'rxjs';

import { HttpClient, HttpHeaders, HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Configuration } from '../app.constants';
import { InputFascicolo } from '../a4g-common/classi/InputFascicolo';
import { Fascicolo } from '../a4g-common/classi/Fascicolo';
import { UtenteAgs } from '../a4g-common/classi/utenteAgs';
import { FascicoloService } from '../fascicolo/fascicolo.service';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

const sessioneFascicoloUtente = "fascicoloUtente";
const sessionAgsUtente = "agsUtenze";
const sessioneSrtUtente = "srTrentoRuoli";

@Injectable()
export class HomeService {
  constructor(
    private http: HttpClient,
    private _configuration: Configuration,
    private fascicoloService: FascicoloService
  ) { }

  public verificaUtente(): Observable<Boolean> {
    return this.http.get<Boolean>(this._configuration.urlIsUtenteRegistrabile);
  }

  // Fascicoli utente
  public salvaFascicoliAziendaUtente(salva: Array<Fascicolo>) {
    let parametroSessione = sessioneFascicoloUtente;
    console.log("carica " + parametroSessione + " in sessione");
    sessionStorage.setItem(parametroSessione, JSON.stringify(salva));
  }

  public isAziendaUtente(cuaa: String): Boolean {
    let fascicoli: Array<Fascicolo> = this.getFascicoliAziendaUtente();
    if (fascicoli != null && fascicoli.filter(x => x.cuaa == cuaa).length > 0) 
      return true;
    else
      return false;
  }

  public getFascicoliAziendaUtente(): Array<Fascicolo> {
    let parametroSessione = sessioneFascicoloUtente;
    let sessione = sessionStorage.getItem(parametroSessione);
    if (sessione != null) {
      console.log("carica " + parametroSessione + " da sessione");
      console.log(sessione);
      return JSON.parse(sessione);
    }
  }

  public salvaSrtRuoliPerUtente(salva: string[]) {
    let parametroSessione = sessioneSrtUtente;
    console.log("carica " + parametroSessione + " in sessione");
    sessionStorage.setItem(parametroSessione, JSON.stringify(salva));
  }

  public getSrtRuoliPerUtente(): string[] {
    let parametroSessione = sessioneSrtUtente;
    let sessione = sessionStorage.getItem(parametroSessione);
    if (sessione != null) {
      console.log("carica " + parametroSessione + " da sessione");
      console.log(sessione);
      return JSON.parse(sessione);
    }
  }

  public ricercaSrtRuoliPerUtente(codiceFiscale: string): Observable<string[]> {
    console.log("chiama ricercaSrtRuoliPerUtente");
    return this.http.get<string[]>(this._configuration.UrlSrtGetRuoliPerUtente.replace('${codiceFiscale}', codiceFiscale));
  }

  public salvaUtenzeAgsPerUtente(salva: Array<UtenteAgs>) {
    let parametroSessione = sessionAgsUtente;
    console.log("carica " + parametroSessione + " in sessione");
    sessionStorage.setItem(parametroSessione, JSON.stringify(salva));
  }

  public getUtenzeAgsPerUtente(): Array<UtenteAgs> {
    let parametroSessione = sessionAgsUtente;
    let sessione = sessionStorage.getItem(parametroSessione);
    if (sessione != null) {
      console.log("carica " + parametroSessione + " da sessione");
      console.log(sessione);
      return JSON.parse(sessione);
    }
  }

  public ricercaUtenzeAgsPerUtente(): Observable<Array<UtenteAgs>> {
    console.log("chiama ricercaUtenzeAgsPerUtente");
    return this.http.get<Array<UtenteAgs>>(this._configuration.UrlAgsGetUtenzePerUtente);
  }
}
