import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Configuration } from '../app.constants';
import { Fascicolo } from '../a4g-common/classi/Fascicolo';
import { FascicoloService } from '../fascicolo/fascicolo.service';
import { AuthService } from '../auth/auth.service';
import { Utente } from "../auth/user";

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

const sessioneFascicoloUtente = "fascicoloUtente";
// const sessionAgsUtente = "agsUtenze";
// const sessioneSrtUtente = "srTrentoRuoli";

@Injectable()
export class HomeService {
  constructor(
    private http: HttpClient,
    private _configuration: Configuration,
    private fascicoloService: FascicoloService,
    private authService: AuthService
  ) { }

  public verificaUtente(): Observable<boolean> {
    let headers = new HttpHeaders().append('Authorization', this.authService.getAccessToken());
    return this.http.get<boolean>(this._configuration.urlIsUtenteRegistrabile, { headers: headers });
  }

  // Fascicoli utente
  public salvaFascicoliAziendaUtente(salva: Array<Fascicolo>) {
    let parametroSessione = sessioneFascicoloUtente;
    console.log("carica " + parametroSessione + " in sessione");
    sessionStorage.setItem(parametroSessione, JSON.stringify(salva));
  }

  public isAziendaUtente(cuaa: string): boolean {
    let fascicoli: Array<Fascicolo> = this.getFascicoliAziendaUtente();
    if (fascicoli != null && fascicoli.filter(x => x.cuaa == cuaa).length > 0) 
      return true;
    else
      return false;
  }

  public getFascicoliAziendaUtente(): Array<Fascicolo> {
    let parametroSessione = sessioneFascicoloUtente;
    let fascicoli = new Array<Fascicolo>();
    let sessione = sessionStorage.getItem(parametroSessione);
    if (sessione != null) {
      console.log("carica " + parametroSessione + " da sessione");
      console.log(sessione);
      return JSON.parse(sessione);
    }
    // qui devo leggere le aziende delegate da altro oggetto in sessione e riempire questo
    const sessionUser: Utente = JSON.parse(sessionStorage.getItem("user"));
    if (sessionUser != null) {
      console.log("carica " + parametroSessione + " da sessione");
      console.log(sessionUser);
      if (sessionUser?.aziendeDelegate) {
        for (const azieApp of sessionUser.aziendeDelegate) {
          if (azieApp.applicazione == "UMA") {
            for (const azie of azieApp.aziendeDelegate) {
              let fascicolo = new Fascicolo();
              fascicolo.cuaa = azie.cuaa;
              fascicolo.denominazione = azie.denominazione;
              fascicoli.push(fascicolo);
            }
          }
        }
      }
    }
    return fascicoli;
  }
  /*
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
  */
}
