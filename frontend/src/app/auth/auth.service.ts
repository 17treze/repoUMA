import { Injectable, Output, EventEmitter, Directive } from "@angular/core";
import { Utente } from "./user";
import { Configuration } from "../app.constants";
import { HttpClient } from "@angular/common/http";
import { EMPTY, Observable, of } from "rxjs";
import { catchError, tap } from "rxjs/operators";

@Directive()
@Injectable()
export class AuthService {
  public static roleCaa = "caa";
  public static rolePrivate = "azienda";
  public static roleAppag = "appag";
  public static roleGestoreUtenti = "gestoreutenti";
  public static roleAdmin = "amministratore";
  public static roleIstruttoreAMF = "istruttoreamf";
  public static roleIstruttoreDomandaUnica = "istruttoredu";
  public static roleAltroEnte = "viewer_altro_ente";
  public static roleViewerPAT = "viewer_pat";
  public static roleBackOffice = "backoffice";
  public static roleViticolo = "viticolo";
  public static roleIstruttoreUMA = "istruttoreuma";
  public static roleDistributore = "operatore_distributore";
  public static roleDogane = "operatore_dogane";
  public static roleResponsabileFascicoloPat = "responsabile_fascicolo_pat";
  private _userSelectedRole: string;

  @Output() onUserChange = new EventEmitter<Utente>();

  constructor(
    private http: HttpClient,
    private _configuration: Configuration
  ) { }

  isLoggedIn(): boolean {
    if (this.getUser()) {
      return true;
    }
    return false;
  }

  setUser(user: Utente) {
    if (sessionStorage.getItem("user")) {
      let utenteLoggato: Utente = JSON.parse(sessionStorage.getItem("user"));
      if (utenteLoggato.identificativo != user.identificativo)
        console.log("Ricarico profili");
      sessionStorage.clear();
    } else {
      sessionStorage.clear();
    }

    sessionStorage.setItem("user", JSON.stringify(user));
    this.onUserChange.emit(user);
  }

  public getUserNew(force: boolean = false): Observable<Utente> {
    if (force || !sessionStorage.getItem("user")) {
      return this.http.get<Utente>(this._configuration.urlGetSSO).pipe(
        catchError(err => {
          console.error("Errore in recupero utente: " + err);
          return EMPTY;
        }),
        tap(res => this.setUser(res))
      );
    } else {
      return of(JSON.parse(sessionStorage.getItem("user")));
    }
  }

  /**
    * @deprecated uso improprio dell'asincronia
    */
  getUser(force: boolean = false): Utente {
    if (!sessionStorage.getItem("user")) {
      if (force) {
        this.callAsyncUser();
        return JSON.parse(sessionStorage.getItem("user"));
      }
      return null;
    } else {
      return JSON.parse(sessionStorage.getItem("user"));
    }
  }

  /**
    * @deprecated uso improprio dell'asincronia
    */
  async callAsyncUser() {
    let time = Date.now().toString();
    let user = null;
    try {
      user = await this.getUserFromSSO().toPromise();
    } catch {
      console.error("Errore in recupero utente " + time);
    }
    this.setUser(user);
  }

  /**
    * @deprecated usare getUserNew()
    */
  getUserFromSSO(): Observable<Utente> {
    return this.http.get<Utente>(this._configuration.urlGetSSO);
  }

  isUserPrivate() {
    return this.isUserInRole(AuthService.rolePrivate);
  }

  /**
    * @deprecated uso improprio dell'asincronia
    */
  isUserInRole(requiredRole: string, user: Utente = null): boolean {
    // return true;
    if (!user) user = this.getUser();

    if (!user || !user.profili) {
      return false;
    }

    for (const profilo of user.profili) {
      if (profilo && profilo.identificativo == requiredRole) {
        return true;
      }
    }
    return false;
  }

  public caricaUtente(): Observable<Utente> {
    return this.http.get<Utente>(this._configuration.urlGetSSO);
  }

  public get userSelectedRole() {
    const ruoloSelezionato = this._userSelectedRole || localStorage.getItem("selectedRole"); // || this.getRoleFromSessionStorage();
    return ruoloSelezionato;
  }

  public set userSelectedRole(role: string) {
    this._userSelectedRole = role;
    localStorage.setItem("selectedRole", this.userSelectedRole);
  }

  private getRoleFromSessionStorage() {
    const user: Utente = JSON.parse(sessionStorage.getItem("user"));
    if (user && user.profili && user.profili.length) {
      return user.profili[0].identificativo;
    }
    return null;
  }
}
