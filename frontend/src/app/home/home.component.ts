import { Component, OnInit, ViewEncapsulation, OnDestroy } from '@angular/core';
import { HomeService } from './home.service';
import { Fascicolo } from '../a4g-common/classi/Fascicolo';
import { AuthService } from '../auth/auth.service';
import { Utente } from '../auth/user';
import { MenuItem, Message } from 'primeng/api';
import { Router } from '@angular/router';
import { Configuration } from '../app.constants';
import { UtenteAgs } from '../a4g-common/classi/utenteAgs';
import { FascicoloService } from '../fascicolo/fascicolo.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
  styles: [`
  .ui-steps-item {
      width: 25%
  }
`],
  encapsulation: ViewEncapsulation.None
})
export class HomeComponent implements OnInit, OnDestroy {
  imagePath: '../../img/documentazione.png';
  fascicoli: Fascicolo[];
  roleCAA: boolean = false;
  roleADMIN: boolean = false;
  roleAzienda: boolean = false;
  roleIstruttoreUMA: boolean = false;
  roleDistributore: boolean = false;
  rolePrivate: boolean = false;
  utente: Utente;
  srTrentoRuoli: string[];
  agsUtenze: UtenteAgs[];
  srTrentoMenu: MenuItem[];
  agsItems: MenuItem[];
  a4GItems: MenuItem[];
  msgs: Message[] = [];

  // private _serviceSubscription;

  constructor(
    public router: Router,
    private homeService: HomeService,
    private authService: AuthService,
    private _configuration: Configuration,
    private fascicoloService: FascicoloService) {
    // this._serviceSubscription = this.authService.onUserChange.subscribe({
    //   next: (event: AuthService) => {
    //     this.caricaUtente();
    //   }
    // });
  }

  ngOnInit() {
    // this.clearStorage();
    this.caricaUtente();
  }

  ngOnDestroy() {
    this.cleanup();
  }

  public consume() {
    this.cleanup();
  }

  private cleanup() {
    // if (this._serviceSubscription) {
    //   this._serviceSubscription.unsubscribe();
    // }
  }

  caricaUtente() {
    this.utente = this.authService.getUser();
    console.log("CaricaUtente HomeComponent " + this.utente?.codiceFiscale);

    this.roleCAA = this.authService.isUserInRole(AuthService.roleCaa);
    this.rolePrivate = this.authService.isUserInRole(AuthService.rolePrivate);
    this.roleADMIN = this.authService.isUserInRole(AuthService.roleAdmin);
    this.roleAzienda = this.authService.isUserInRole(AuthService.rolePrivate);
    this.roleIstruttoreUMA = this.authService.isUserInRole(AuthService.roleIstruttoreUMA);
    this.roleDistributore = this.authService.isUserInRole(AuthService.roleDistributore);

    if (this.roleAzienda) {
      this.getFascicoliAziendaUtente().then(() => this.caricaCollegamentiUtente());
    }
    this.caricaCollegamentiUtente();
  }

  getFascicoliAziendaUtente() {
    console.log('home getFascicoliAziendaUtente');
    return new Promise((resolve) => {
      this.fascicoli = this.homeService.getFascicoliAziendaUtente();
      if (!this.fascicoli || this.fascicoli.length == 0) {
        this.fascicoloService.ricercaFascicoliAziendaUtente()
          .subscribe((next) => {
            this.fascicoli = next;
            this.homeService.salvaFascicoliAziendaUtente(next);
            resolve(true);
          });
      }
    });
  }

  caricaCollegamentiUtente() {
    const roles = new Array<MenuItem>();
    this.a4GItems = [
      {
        label: 'Seleziona Ruoli',
        items: roles,
        expanded: true
      }
    ];
    if (this.roleAzienda && this.fascicoli) {
      for (let element of this.fascicoli) {
        roles.push({ label: element.denominazione, routerLink: '../fascicolo/' + element.idFascicolo + '/presentazioneIstanze' });
      }
      localStorage.setItem("selectedRole", 'azienda');
    }
    if (this.roleCAA) {
      roles.push({ label: 'Operatore CAA', routerLink: '../funzioniCaa', command: (event) => { localStorage.setItem("selectedRole", AuthService.roleCaa) } });
    }
    if (this.roleIstruttoreUMA) {
      roles.push({ label: 'Funzionario comunale - Istruttore UMA', routerLink: '../funzioniPatIstruttoreUMA', command: (event) => { this.authService.userSelectedRole = AuthService.roleIstruttoreUMA } });
    }
    if (this.roleDistributore) {
      roles.push({ label: 'Distributore', routerLink: '../funzioniDistributore', command: (event) => { this.authService.userSelectedRole = AuthService.roleDistributore } });
    }
    if (this.roleADMIN) {
      roles.push({ label: 'Amministratore', routerLink: '../funzioniPat', command: (event) => { localStorage.setItem("selectedRole", AuthService.roleAdmin) } });
    }
    /*
    if (this.roleAPPAG) {
      roles.push({ label: 'Funzionario regionale - Admin', routerLink: '../funzioniPat', command: (event) => { localStorage.setItem("selectedRole", 'appag') } })
    }
    if (this.roleGestoreUtenti) {
      roles.push({ label: 'Funzionario regionale - Gestione Utenti', routerLink: '../funzioniPat', command: (event) => { localStorage.setItem("selectedRole", 'gestoreutenti') } })
    }
    if (this.roleViewerPAT) {
      roles.push({ label: 'Funzionario regionale - Visualizzatore', routerLink: '../funzioniPatVisualizzatore', command: (event) => { localStorage.setItem("selectedRole", 'viewer_pat') } })
    }
    if (this.roleIstruttoreAMF) {
      roles.push({ label: 'Funzionario comunale - Istruttore AMF', routerLink: '../funzioniPat', command: (event) => { localStorage.setItem("selectedRole", 'istruttoreamf') } });
    }
    if (this.roleIstruttoreDU) {
      roles.push({ label: 'Funzionario comunale - Istruttore DU', routerLink: '../funzioniPat/gestioneIstruttoria', command: (event) => { localStorage.setItem("selectedRole", 'istruttoredu') } });
    }
    if (this.roleAltroEnte) {
      roles.push({ label: 'Operatore Altri Enti - Visualizzatore', routerLink: '../funzioniOperatoreVisualizzatore', command: (event) => { localStorage.setItem("selectedRole", 'viewer_altro_ente') } })
    }
    if (this.roleBackOffice) {
      roles.push({ label: 'Funzionario comunale - BackOffice', routerLink: '../gis', command: (event) => { localStorage.setItem("selectedRole", AuthService.roleBackOffice) } })
    }
    if (this.roleViticolo) {
      roles.push({ label: 'Funzionario comunale - Viticolo', routerLink: '../gis', command: (event) => { localStorage.setItem("selectedRole", AuthService.roleViticolo) } })
    }
    if (this.roleDogane) {
      roles.push({ label: 'Dipendente Agenzia delle Dogane', routerLink: '../funzioniPatAgenziaDogane', command: (event) => { localStorage.setItem("selectedRole", AuthService.roleDogane) } })
    }
    if (this.roleResponsabileFascicoloPat) {
      roles.push({ label: 'Responsabile fascicolo Reg. Lazio', routerLink: '../funzioniPatResponsabileFascicolo', command: (event) => { localStorage.setItem("selectedRole", AuthService.roleResponsabileFascicoloPat) } })
    }
    */
    if (roles.length === 0) {
      roles.push(
        {
          label: 'Nessun ruolo disponibile',
        }
      );
    }
  }

  private clearStorage() {
    // sessionStorage.clear();
    localStorage.removeItem('UMA_RO_DICH');
    localStorage.removeItem('UMA_RO_DICH');
    localStorage.removeItem('UMA_RO');
    localStorage.removeItem('selectedRole');
    localStorage.removeItem('A4G_BREAD_CRUMB_STEP');
  }
}
