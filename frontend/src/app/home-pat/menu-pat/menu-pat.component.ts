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
      { routerLink: 'gestioneIstruttoria', label: 'Gestione Istruttoria', visible: this.visibleTabGestioneIstruttoria() },
      { routerLink: 'amministrazione', label: 'Amministrazione', visible: this.visibleTabAmministrazione() },
      { routerLink: 'ricercaAziende', label: 'Prelievi di carburante', visible: this.visibleTabRicercaAziende() },
      { routerLink: 'gestioneConsegne', label: 'Validare i prelievi', visible: this.visibleTabGestioneConsegne() },
      // { routerLink: 'gestioneAzienda', label: 'Domande', visible: this.visibleTabGestioneAziende() },
      // { routerLink: 'scadenzeAppag', label: 'Scadenze', visible: this.visibleTabScadenze() },
      );
  }

  visibleTabRicercaAziende() {
    return localStorage.getItem('selectedRole') === 'uma_distributore';
  }

  visibleTabGestioneConsegne() {
    return localStorage.getItem('selectedRole') === 'uma_distributore';
  }

  /*
  visibleTabScadenze() {
    return localStorage.getItem('selectedRole') === 'uma_funzionario_regionale'
      || localStorage.getItem('selectedRole') === 'uma_funzionario_comunale';
  }

  visibleTabGestioneAziende() {
    return localStorage.getItem('selectedRole') === 'uma_funzionario_regionale';
  }
  */

  visibleTabFascicoloAziendale() {
    return localStorage.getItem('selectedRole') === 'uma_funzionario_regionale' ||
      localStorage.getItem('selectedRole') === 'uma_funzionario_comunale' ||
      localStorage.getItem('selectedRole') === 'uma_azienda' ||
      localStorage.getItem('selectedRole') === 'uma_caa';
  }

  visibleTabGestioneIstruttoria() {
    return localStorage.getItem('selectedRole') === 'uma_funzionario_regionale'
      || localStorage.getItem('selectedRole') === 'uma_funzionario_comunale';
  }

  visibleTabAmministrazione() {
    return localStorage.getItem('selectedRole') === 'uma_funzionario_regionale'
      || localStorage.getItem('selectedRole') === 'uma_amministratore';
  }

}
