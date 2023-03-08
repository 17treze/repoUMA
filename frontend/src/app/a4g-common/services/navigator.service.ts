import { Injectable } from '@angular/core';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class NavigatorService {

  constructor(
    private router: Router
  ) { }

  goToPresentazioneIstanze(idFascicolo: string) {
    if (idFascicolo == null || !localStorage.getItem("selectedRole")) {
      console.log('Non è stato possibile leggere idFascicolo o selectedRole!');
      this.router.navigate(['/']);
    }
    let root: string;
    if (localStorage.getItem("selectedRole") == 'appag') { root = "funzioniPat/"; }
    if (localStorage.getItem("selectedRole") == 'amministratore') { root = "funzioniPat/"; }
    if (localStorage.getItem("selectedRole") == 'caa') { root = "funzioniCaa/"; }
    if (localStorage.getItem("selectedRole") == 'azienda') { root = "/"; }
    if (localStorage.getItem("selectedRole") == 'gestoreutenti') { root = "funzioniPat/"; }
    if (localStorage.getItem("selectedRole") == 'viewer_pat') { root = "funzioniPatVisualizzatore/"; }
    if (localStorage.getItem("selectedRole") == 'istruttoreuma') { root = "funzioniPatIstruttoreUMA/"; }
    if (localStorage.getItem("selectedRole") == 'operatore_distributore') { root = "funzioniDistributore/"; }
    if (localStorage.getItem("selectedRole") == 'amministratore') { root = "funzioniPat/"; }
    if (localStorage.getItem("selectedRole") == 'istruttoreamf') { root = "funzioniPat/"; }
    if (localStorage.getItem("selectedRole") == 'istruttoredu') { root = "funzioniPat/gestioneIstruttoria/"; }
    if (localStorage.getItem("selectedRole") == 'viewer_altro_ente') { root = "funzioniOperatoreVisualizzatore/"; }
    this.router.navigate([`${root}fascicolo/${idFascicolo.toString()}/presentazioneIstanze`]);
  }
}
