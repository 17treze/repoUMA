import { Component, OnInit } from '@angular/core';
import { MenuItem } from 'primeng/api';
import { ActivatedRoute } from '@angular/router';
import { A4gCostanti } from 'src/app/a4g-common/a4g-costanti';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-menu-pat',
  templateUrl: './menu-pat.component.html',
  styleUrls: ['./menu-pat.component.css']
})
export class MenuPatComponent implements OnInit {
  funzioni = Array<MenuItem>();
  title: String;

  constructor(private route: ActivatedRoute) {
  }

  ngOnInit() {
    this.title = this.route.snapshot.data[A4gCostanti.ROUTE_DATA_BREADCRUMB].toUpperCase();
    this.funzioni = new Array<MenuItem>(
      { routerLink: 'fascicoloAziendale', label: 'Fascicolo Aziendale', visible: this.visibleTabFascicoloAziendale() },
      { routerLink: 'gestioneAzienda', label: 'Domande', visible: this.visibleTabGestioneAziende() },
      { routerLink: 'gestioneIstruttoria', label: 'Gestione Istruttoria', visible: this.visibleTabGestioneIstruttoria() },
      { routerLink: 'amministrazione', label: 'Amministrazione', visible: this.visibleTabAmministrazione() },
      { routerLink: 'scadenzeAppag', label: 'Scadenze', visible: this.visibleTabScadenze() },
      { routerLink: 'ricercaAziende', label: 'Prelievi di carburante', visible: this.visibleTabRicercaAziende() },
      { routerLink: 'gestioneConsegne', label: 'Validare i prelievi', visible: this.visibleTabGestioneConsegne() },
    );
  }

  visibleTabRicercaAziende() {
    return localStorage.getItem('selectedRole') === 'operatore_distributore';
  }

  visibleTabGestioneConsegne() {
    return localStorage.getItem('selectedRole') === 'operatore_distributore';
  }

  visibleTabScadenze() {
    return localStorage.getItem('selectedRole') === 'appag'
      || localStorage.getItem('selectedRole') === 'amministratore'
      || localStorage.getItem('selectedRole') === 'istruttoredu';
  }

  visibleTabFascicoloAziendale() {
    return localStorage.getItem('selectedRole') === 'appag' ||
      localStorage.getItem('selectedRole') === 'amministratore' ||
      localStorage.getItem('selectedRole') === 'responsabile_fascicolo_pat';
  }

  visibleTabGestioneAziende() {
    return localStorage.getItem('selectedRole') === 'appag' ||
      localStorage.getItem('selectedRole') === 'amministratore' ||
      localStorage.getItem('selectedRole') === 'viewer_altro_ente' ||
      localStorage.getItem('selectedRole') === 'viewer_pat';
  }

  visibleTabGestioneIstruttoria() {
    return localStorage.getItem('selectedRole') === 'appag'
      || localStorage.getItem('selectedRole') === 'amministratore'
      || localStorage.getItem('selectedRole') === 'istruttoreamf'
      || localStorage.getItem('selectedRole') === 'istruttoredu'
      || localStorage.getItem('selectedRole') === 'viewer_altro_ente'
      || localStorage.getItem('selectedRole') === 'viewer_pat'
      || localStorage.getItem('selectedRole') === 'istruttoreuma';
  }

  visibleTabAmministrazione() {
    return localStorage.getItem('selectedRole') === 'gestoreutenti'
      || localStorage.getItem('selectedRole') === 'appag'
      || localStorage.getItem('selectedRole') === 'amministratore';
  }

}
