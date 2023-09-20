import { Injectable, Output, EventEmitter, Directive } from "@angular/core";
import { Utente } from "./user";
import { Configuration } from "../app.constants";
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Subject, Observable, of } from "rxjs";

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

  public static roleCaa = "uma_caa";
  public static rolePrivate = "uma_azienda";
  public static roleAdmin = "uma_funzionario_regionale";
  public static roleIstruttoreUMA = "uma_funzionario_comunale";
  public static roleDistributore = "uma_distributore";
  // public static roleAppag = "appag";
  // public static roleGestoreUtenti = "gestoreutenti";
  // public static roleIstruttoreAMF = "istruttoreamf";
  // public static roleIstruttoreDomandaUnica = "istruttoredu";
  // public static roleAltroEnte = "viewer_altro_ente";
  // public static roleViewerPAT = "viewer_pat";
  // public static roleBackOffice = "backoffice";
  // public static roleViticolo = "viticolo";
  // public static roleDogane = "operatore_dogane";
  // public static roleResponsabileFascicoloPat = "responsabile_fascicolo_pat";
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
    sessionStorage.clear();
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
        this.getUserFromSession().subscribe(
          x => {
            this.setUser(x);
            this.authenticationEventObservable.next(result);
          },
          err => { 
            console.error('Observer error: ' + err);
          }
        );
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
    sessionStorage.setItem("user", JSON.stringify(user));
    this.onUserChange.emit(user);
  }

  public getUserFromSession(): Observable<Utente> {
    if (sessionStorage.getItem('user') !== null && sessionStorage.getItem('user') !== 'undefined') {
      console.log('User in session: ' + sessionStorage.getItem('user'));
      return of(JSON.parse(sessionStorage.getItem("user")));
    }
    else {
      return this.getUserService();
    }
  }

  getUserService(): Observable<Utente> {
    let headers = new HttpHeaders().append('Authorization', this.getAccessToken());
    console.log('Authorization: ' + this.getAccessToken());
    console.log('URL: ' + this._configuration.urlGetSSO);
    return this.http.get<Utente>(this._configuration.urlGetSSO, { headers: headers });  
  }

  isUserPrivate() {
    return this.isUserInRole(AuthService.rolePrivate);
  }

  /**
    * nessuna asincronia
    */
  isUserInRole(requiredRole: string, user: Utente = null): boolean {
    if (!user) {
      user = JSON.parse(sessionStorage.getItem("user"));
    }
    if (requiredRole && user?.ruoli) {
      // console.log("Ruolo cercato: " + requiredRole.toLowerCase());
      for (const ruoloApp of user.ruoli) {
        if (ruoloApp.applicazione == "UMA") {
          for (const ruolo of ruoloApp.ruoli) {
            // console.log("Ruolo trovato: " + JSON.stringify(ruolo));
            if (ruolo.id && ruolo.id.toLowerCase() == requiredRole.toLowerCase()) {
              console.log("Ruolo trovato: " + JSON.stringify(ruolo));
              return true;
            }
          }
        }
      }
    }
    console.log("Ruolo non trovato :-(");
    return false;
  }

  public getUser(): Utente {    
    //   return this.http.get<Utente>(this._configuration.urlGetSSO);
    const user: Utente = JSON.parse(sessionStorage.getItem("user"));
    return user;
  }
  
  public get userSelectedRole() {
    const ruoloSelezionato = this._userSelectedRole || localStorage.getItem("selectedRole"); // || this.getRoleFromSessionStorage();
    // console.log("Ruolo selezionato: " + ruoloSelezionato);
    return ruoloSelezionato;
  }

  public set userSelectedRole(role: string) {
    this._userSelectedRole = role;
    localStorage.setItem("selectedRole", this.userSelectedRole);
  }
}
