import { Component, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { MessageService } from 'primeng/api';
import { AuthService } from 'src/app/auth/auth.service';
import { PopupScaricaElenchiUmaComponent } from '../../a4g-common/popup-scarica-elenchi-uma/popup-scarica-elenchi-uma.component';

@Component({
  selector: 'app-gestione-aziende',
  templateUrl: './gestione-aziende.component.html',
  styleUrls: ['./gestione-aziende.component.scss']
})
export class GestioneAziendeComponent implements OnInit {
  @ViewChild('popupScaricaElenchiUma')
  popupScaricaElenchiUma: PopupScaricaElenchiUmaComponent;

  public displayCardSuolo = false;
  public displayCardConsultazioneUma: boolean;
  public displayCardCercaFascicolo: boolean;

  constructor(private router: Router, private messageService: MessageService, private route: ActivatedRoute) { }

  ngOnInit() {
    this.messageService.clear();
    if (localStorage.getItem('selectedRole') === 'gestoreutenti') {
      this.router.navigate(['/funzioniPat/amministrazione'], { replaceUrl: true });
      return;
    }
    if (localStorage.getItem('selectedRole') === 'istruttoredu' || localStorage.getItem('selectedRole') === 'istruttoreamf') {
      this.router.navigate(['/funzioniPat/gestioneIstruttoria'], { replaceUrl: true });
      return;
    }
    this.displayCardConsultazioneUma = localStorage.getItem('selectedRole') === AuthService.roleCaa;
    this.displayCardSuolo = localStorage.getItem('selectedRole') === AuthService.roleCaa;
      // || localStorage.getItem('selectedRole') === AuthService.roleAltroEnte
      // || localStorage.getItem('selectedRole') === AuthService.roleAppag
      // || localStorage.getItem('selectedRole') === AuthService.roleViewerPAT;
    // this.displayCardCercaFascicolo = !(localStorage.getItem('selectedRole') === AuthService.roleAltroEnte || localStorage.getItem('selectedRole') === AuthService.roleViewerPAT);
  }

  openDialogScarica() {
    this.popupScaricaElenchiUma.open();
  }

  onCloseDialog($event: any) {

  }

}
