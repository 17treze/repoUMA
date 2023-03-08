import { Component, OnInit, ViewEncapsulation, OnDestroy } from '@angular/core';
import { HomeService } from './home.service';
import { Fascicolo } from '../a4g-common/classi/Fascicolo';
import { AuthService } from '../auth/auth.service';
import { Utente } from '../auth/user';
import { MenuItem } from 'primeng/api';
import { Message } from 'primeng/api';
import { Router } from '@angular/router';
import { validaInput } from '../a4g-common/validazione/validaInput';
import { Configuration } from '../app.constants';
import { UtenteAgs } from '../a4g-common/classi/utenteAgs';
import { FascicoloService } from '../fascicolo/fascicolo.service';
import { environment } from 'src/environments/environment';

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
  srTrentoRuoli: string[];
  agsUtenze: UtenteAgs[];
  roleCAA: boolean = false;
  roleAPPAG: boolean = false;
  roleGestoreUtenti: boolean = false;
  roleADMIN: boolean = false;
  roleAzienda: boolean = false;
  roleIstruttoreAMF: boolean = false;
  roleIstruttoreDU: boolean = false;
  roleAltroEnte: boolean = false;
  roleViewerPAT: boolean = false;
  roleBackOffice: boolean = false;
  roleViticolo: boolean = false;
  roleIstruttoreUMA: boolean = false;
  roleDistributore: boolean = false;
  roleDogane: boolean = false;
  roleResponsabileFascicoloPat: boolean = false;
  utente: Utente;
  srTrentoMenu: MenuItem[];
  agsItems: MenuItem[];
  a4GItems: MenuItem[];
  msgs: Message[] = [];

  private _serviceSubscription;

  constructor(
    public router: Router,
    private homeService: HomeService,
    private authService: AuthService,
    private _configuration: Configuration,
    private fascicoloService: FascicoloService) {
    this._serviceSubscription = this.authService.onUserChange.subscribe({
      next: (event: AuthService) => {
        this.caricaUtente();
      }
    });
  }

  ngOnInit() {
    this.clearStorage();
    this.caricaUtente();
  }

  ngOnDestroy() {
    this.cleanup();
  }

  public consume() {
    this.cleanup();
  }

  private cleanup() {
    if (this._serviceSubscription) {
      this._serviceSubscription.unsubscribe();
    }
  }

  caricaUtente() {
    this.utente = this.authService.getUser();
    this.roleCAA = this.authService.isUserInRole(AuthService.roleCaa);
    this.roleAPPAG = this.authService.isUserInRole(AuthService.roleAppag);
    this.roleGestoreUtenti = this.authService.isUserInRole(AuthService.roleGestoreUtenti);
    this.roleADMIN = this.authService.isUserInRole(AuthService.roleAdmin);
    this.roleAzienda = this.authService.isUserInRole(AuthService.rolePrivate);
    this.roleIstruttoreAMF = this.authService.isUserInRole(AuthService.roleIstruttoreAMF);
    this.roleIstruttoreDU = this.authService.isUserInRole(AuthService.roleIstruttoreDomandaUnica);
    this.roleAltroEnte = this.authService.isUserInRole(AuthService.roleAltroEnte);
    this.roleViewerPAT = this.authService.isUserInRole(AuthService.roleViewerPAT);
    this.roleBackOffice = this.authService.isUserInRole(AuthService.roleBackOffice);
    this.roleViticolo = this.authService.isUserInRole(AuthService.roleViticolo);
    this.roleIstruttoreUMA = this.authService.isUserInRole(AuthService.roleIstruttoreUMA);
    this.roleDistributore = this.authService.isUserInRole(AuthService.roleDistributore);
    this.roleDogane = this.authService.isUserInRole(AuthService.roleDogane);
    this.roleResponsabileFascicoloPat = this.authService.isUserInRole(AuthService.roleResponsabileFascicoloPat);

    if (this.roleAzienda) {
      this.getFascicoliAziendaUtente().then(() => this.caricaCollegamentiUtente());
    }
    this.getRuoliSrTrento();
    this.getUtenzeAgs();
    this.caricaCollegamentiUtente();
  }

  getRuoliSrTrento() {
    if (this.utente && this.utente.codiceFiscale && validaInput.validaCf(this.utente.codiceFiscale, false)) {
      this.srTrentoRuoli = this.homeService.getSrtRuoliPerUtente();
      console.log('home getRuoliSrTrento');
      if (!this.srTrentoRuoli) {
        this.homeService.ricercaSrtRuoliPerUtente(this.utente.codiceFiscale)
          .subscribe((next) => {
            this.srTrentoRuoli = next,
              this.homeService.salvaSrtRuoliPerUtente(next);
            this.caricaRuoliSrTrento();
          });
      } else {
        this.caricaRuoliSrTrento();
      }
    }
  }

  caricaRuoliSrTrento() {
    const roles = new Array<MenuItem>();
    this.srTrentoMenu = [
      {
        label: 'Seleziona Ruoli',
        items: roles,
        expanded: true
      }
    ];
    if (this.srTrentoRuoli) {
      for (let index = 0; index < this.srTrentoRuoli.length; index++) {
        roles.push(
          {
            label: this.srTrentoRuoli[index],
            url: this._configuration.UrlRedirectSrTrento
          }
        );
      }
    } else {
      roles.push(
        {
          label: 'Nessun ruolo disponibile',
          url: this._configuration.UrlRedirectSrTrento
        }
      );
    }
  }

  getUtenzeAgs() {
    this.agsUtenze = this.homeService.getUtenzeAgsPerUtente();
    console.log('home getUtenzeAgs');
    if (!this.agsUtenze) {
      this.homeService.ricercaUtenzeAgsPerUtente()
        .subscribe((next) => {
          this.agsUtenze = next,
            this.homeService.salvaUtenzeAgsPerUtente(next);
          this.caricaUtenzeAgs();
        });
    } else {
      this.caricaUtenzeAgs();
    }
  }

  caricaUtenzeAgs() {
    const roles = new Array<MenuItem>();
    this.agsItems = [
      {
        label: 'Seleziona Utenza',
        items: roles,
        expanded: true
      }
    ];
    if (this.agsUtenze) {
      for (let index = 0; index < this.agsUtenze.length; index++) {
        roles.push(
          {
            label: this.agsUtenze[index].utenza + ' ' + this.agsUtenze[index].descrizione,
            url: this._configuration.UrlRedirectAgs
          }
        );
      }
    } else {
      roles.push(
        {
          label: 'Nessuna utenza disponibile',
        }
      );
    }
  }

  getFascicoliAziendaUtente() {
    console.log('home getFascicoliAziendaUtente');
    return new Promise((resolve) => {
      this.fascicoli = this.homeService.getFascicoliAziendaUtente();
      if (!this.fascicoli) {
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
      for (let index = 0; index < this.fascicoli.length; index++) {
        const element = this.fascicoli[index];
        roles.push({ label: element.denominazione, routerLink: '../fascicolo/' + element.idFascicolo + '/presentazioneIstanze' });
      }
      localStorage.setItem("selectedRole", 'azienda');
    }
    if (this.roleCAA) {
      roles.push({ label: 'Operatore CAA', routerLink: '../funzioniCaa', command: (event) => { localStorage.setItem("selectedRole", 'caa') } });
    }
    if (this.roleAPPAG) {
      roles.push({ label: 'Operatore PAT Appag - Admin', routerLink: '../funzioniPat', command: (event) => { localStorage.setItem("selectedRole", 'appag') } })
    }
    if (this.roleGestoreUtenti) {
      roles.push({ label: 'Operatore PAT Appag - Gestione Utenti', routerLink: '../funzioniPat', command: (event) => { localStorage.setItem("selectedRole", 'gestoreutenti') } })
    }
    if (this.roleViewerPAT) {
      roles.push({ label: 'Operatore PAT - Visualizzatore', routerLink: '../funzioniPatVisualizzatore', command: (event) => { localStorage.setItem("selectedRole", 'viewer_pat') } })
    }
    if (this.roleIstruttoreUMA) {
      roles.push({ label: 'Operatore PAT - Istruttore UMA', routerLink: '../funzioniPatIstruttoreUMA', command: (event) => { this.authService.userSelectedRole = AuthService.roleIstruttoreUMA } });
    }
    if (this.roleDistributore) {
      roles.push({ label: 'Distributore', routerLink: '../funzioniDistributore', command: (event) => { this.authService.userSelectedRole = AuthService.roleDistributore } });
    }
    if (this.roleADMIN) {
      roles.push({ label: 'Amministratore', routerLink: '../funzioniPat', command: (event) => { localStorage.setItem("selectedRole", 'amministratore') } });
    }
    if (this.roleIstruttoreAMF) {
      roles.push({ label: 'Operatore PAT Appag - Istruttore AMF', routerLink: '../funzioniPat', command: (event) => { localStorage.setItem("selectedRole", 'istruttoreamf') } });
    }
    if (this.roleIstruttoreDU) {
      roles.push({ label: 'Operatore PAT Appag - Istruttore DU', routerLink: '../funzioniPat/gestioneIstruttoria', command: (event) => { localStorage.setItem("selectedRole", 'istruttoredu') } });
    }
    if (this.roleAltroEnte) {
      roles.push({ label: 'Operatore Altri Enti - Visualizzatore', routerLink: '../funzioniOperatoreVisualizzatore', command: (event) => { localStorage.setItem("selectedRole", 'viewer_altro_ente') } })
    }
    if (this.roleBackOffice) {
      roles.push({ label: 'Operatore PAT - BackOffice', routerLink: '../gis', command: (event) => { localStorage.setItem("selectedRole", AuthService.roleBackOffice) } })
    }
    if (this.roleViticolo) {
      roles.push({ label: 'Operatore PAT - Viticolo', routerLink: '../gis', command: (event) => { localStorage.setItem("selectedRole", AuthService.roleViticolo) } })
    }
    if (this.roleDogane) {
      roles.push({ label: 'Dipendente Agenzia delle Dogane', routerLink: '../funzioniPatAgenziaDogane', command: (event) => { localStorage.setItem("selectedRole", AuthService.roleDogane) } })
    }
    if (this.roleResponsabileFascicoloPat) {
      roles.push({ label: 'Responsabile fascicolo PAT Appag', routerLink: '../funzioniPatResponsabileFascicolo', command: (event) => { localStorage.setItem("selectedRole", AuthService.roleResponsabileFascicoloPat) } })
    }
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
