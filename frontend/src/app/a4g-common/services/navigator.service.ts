import { Injectable } from '@angular/core';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class NavigatorService {

  constructor(
    private router: Router
  ) { }

  goToPresentazioneIstanze(cuaa: string) {
    if (cuaa == null || !localStorage.getItem("selectedRole")) {
      console.log('Non è stato possibile leggere cuaa o selectedRole!');
      this.router.navigate(['/']);
    }
    let root: string;
    if (localStorage.getItem("selectedRole") == 'uma_funzionario_comunale') { root = "funzioniPatIstruttoreUMA/"; }
    if (localStorage.getItem("selectedRole") == 'uma_funzionario_regionale') { root = "funzioniPat/"; }
    if (localStorage.getItem("selectedRole") == 'uma_caa') { root = "funzioniCaa/"; }
    if (localStorage.getItem("selectedRole") == 'uma_azienda') { root = "/"; }
    if (localStorage.getItem("selectedRole") == 'uma_distributore') { root = "funzioniCaa/"; }
    /*
    if (localStorage.getItem("selectedRole") == 'viewer_pat') { root = "funzioniPatVisualizzatore/"; }
    if (localStorage.getItem("selectedRole") == 'istruttoreuma') { root = "funzioniPatIstruttoreUMA/"; }
    if (localStorage.getItem("selectedRole") == 'operatore_distributore') { root = "funzioniDistributore/"; }
    if (localStorage.getItem("selectedRole") == 'amministratore') { root = "funzioniPat/"; }
    if (localStorage.getItem("selectedRole") == 'istruttoreamf') { root = "funzioniPat/"; }
    if (localStorage.getItem("selectedRole") == 'istruttoredu') { root = "funzioniPat/gestioneIstruttoria/"; }
    if (localStorage.getItem("selectedRole") == 'viewer_altro_ente') { root = "funzioniOperatoreVisualizzatore/"; }
    */
    this.router.navigate([`${root}fascicolo/${cuaa}/rettifiche/${cuaa}`]);
  }
}
