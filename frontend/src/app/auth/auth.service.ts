import { Injectable, Output, EventEmitter, Directive } from "@angular/core";
import { Utente } from "./user";
import { Configuration } from "../app.constants";
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Subject, EMPTY, Observable, of } from "rxjs";
import { catchError, tap } from "rxjs/operators";

import { MessageService } from 'primeng/api';
import { Router } from '@angular/router';
import { AuthConfig, OAuthService } from 'angular-oauth2-oidc';
import { environment } from 'src/environments/environment';

@Directive()
@Injectable({
  providedIn: 'root'
})

export class AuthService {

  public authenticationEventObservable: Subject<boolean> =
    new Subject<boolean>();

  authConfig: AuthConfig = {
    issuer: environment.discoveryDocumentEndpoint,
    redirectUri: environment.redirectUri,
    logoutUrl: environment.logoutUrl,
    responseType: environment.responseType,
    scope: environment.scope,
    clientId: environment.clientId,
    dummyClientSecret: environment.dummyClientSecret,
    showDebugInformation: environment.showDebugInformation,
    requireHttps: environment.requireHttps,
    strictDiscoveryDocumentValidation:
    environment.strictDiscoveryDocumentValidation,
    skipIssuerCheck: environment.skipIssuerCheck,
    // gestione delle sessioni
    sessionChecksEnabled: environment.sessionChecksEnabled,
    useHttpBasicAuth: environment.useHttpBasicAuth
  };

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

  /**
   *
   * @param router
   * @param oauthService
   */
  constructor(
    // to be removed
    private http: HttpClient,
    private _configuration: Configuration,

    private router: Router,
    private oauthService: OAuthService,
    private messageService: MessageService
  ) {
    this.oauthService.configure(this.authConfig);
  }

  // WSO2
  public logout() {
    this.oauthService.logOut();
  }

  public isAuthenticated(): boolean {
    if (
      this.oauthService.hasValidAccessToken() &&
      this.oauthService.hasValidIdToken()
    ) {
      return true;
    } else {
      return false;
    }
  }

  public login() {
    console.log("login");
  //  this.oauthService.silentRefresh();
    this.oauthService.useSilentRefresh = true;
    this.oauthService
      .loadDiscoveryDocumentAndLogin({
        disableOAuth2StateCheck: true
      })
      .then((result: boolean) => {
        if (!result) {
          // this.toastr.showErrorMessage('Utente non autorizzato');
          console.log('Utente non autorizzato');
        }
        console.log('Result: ' + result);
        this.authenticationEventObservable.next(result);
      })
      .catch((error) => {
        // this.toastr.showErrorMessage('Utente non autorizzato');
        // this.logout();
        console.log(error);
        console.log('Utente non autorizzato');
      });
  }

  public revokeTokenAndLogout() {
    this.oauthService.revokeTokenAndLogout();
  }

  public refreshToken() {
    this.oauthService.refreshToken();
  }

  public getAccessToken() {
       return this.oauthService.getAccessToken();
  }

  public getIdToken(): string {
    return this.oauthService.getIdToken();
  }

  public getIdentityClaims(): any {
    return this.oauthService.getIdentityClaims();
  }

  // legacy
  isLoggedIn(): boolean {
    return this.isAuthenticated();
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

  public getUserFromSession(): Observable<Utente> {
    if (!sessionStorage.getItem('user')) {
      console.log('Autenticato: ' + this.isAuthenticated());
      if (this.isAuthenticated()) {
        return this.getUserService();
      }
      else {
        this.login();
        return EMPTY;
      }
    }
    else {
      return of(JSON.parse(sessionStorage.getItem("user")));
    }  
  }

  getUserService(): Observable<Utente> {
    let headers = new HttpHeaders().append('Authorization', this.getAccessToken());
    console.log('Access token: ' + this.getAccessToken());
    return this.http.get<Utente>(this._configuration.urlGetSSO, { headers: headers });  
  }

  isUserPrivate() {
    return this.isUserInRole(AuthService.rolePrivate);
  }

  /**
    * @deprecated uso improprio dell'asincronia
    */
  isUserInRole(requiredRole: string, user: Utente = null): boolean {
    if (user) {
      if (!user.profili) {
        return false;
      }
      for (const profilo of user.profili) {
        if (profilo && profilo.identificativo == requiredRole) {
          return true;
        }
      }
    }
    else {
      this.getUserFromSession().subscribe((u) => {
        user = u;
        if (!user.profili) {
          return false;
        }
        for (const profilo of user.profili) {
          if (profilo && profilo.identificativo == requiredRole) {
            return true;
          }
        }
      });
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
