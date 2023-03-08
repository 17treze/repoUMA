import { Component, OnInit, OnDestroy } from '@angular/core';
import { MenuItem } from 'primeng/api';
import { Costanti } from '../../../../Costanti';
import { ActivatedRoute } from '@angular/router';
import { LoaderService } from 'src/app/loader.service';

@Component({
  selector: 'app-dettaglio-accoppiati',
  templateUrl: './dettaglio-accoppiati.component.html',
  styleUrls: ['./dettaglio-accoppiati.component.css']
})
export class DettaglioAccoppiatiComponent implements OnInit, OnDestroy {

  menu2: Array<MenuItem>;

  constructor(private route: ActivatedRoute, private loader: LoaderService) { }

  ngOnInit() {
    this.setMenu();
    this.loader.setTimeout(480000); //otto minuti
  }

  ngOnDestroy() {
    this.loader.resetTimeout();
  }

  private setMenu() {
    if (this.route.snapshot.routeConfig.path === Costanti.accoppiatoZootecniaRichiesto) {
      this.menu2 = new Array<MenuItem>(
        {
          routerLink: Costanti.dichiarazioni,
          label: 'Dichiarazioni'
        },
        {
          routerLink: Costanti.capiAmmessi,
          label: 'Capi Ammessi'
        },
        {
          routerLink: Costanti.capiRichiesti,
          label: 'Capi Richiesti'
        },
        {
          routerLink: Costanti.controlliSostegno,
          label: 'Controlli di sostegno'
        },
        {
          routerLink: Costanti.datiDomanda,
          label: 'Dati di domanda'
        },
        {
          routerLink: Costanti.datiIstruttoria,
          label: 'Inserimento dati Istruttoria'
        }
      )
    }
    else if (this.route.snapshot.routeConfig.path === Costanti.accoppiatoSuperficiRichiesto) {
      this.menu2 = new Array<MenuItem>(
        {
          routerLink: Costanti.superficiImpegnate,
          label: 'Superfici Impegnate'
        },
        {
          routerLink: Costanti.dichiarazioni,
          label: 'Dichiarazioni'
        },
        {
          routerLink: Costanti.controlliSostegno,
          label: 'Controlli di sostegno'
        },
        {
          routerLink: Costanti.datiDomanda,
          label: 'Dati di domanda'
        },
        {
          routerLink: Costanti.datiParticella,
          label: 'Dati per Particella'
        },
        {
          routerLink: Costanti.datiIstruttoria,
          label: 'Inserimento dati Istruttoria'
        }
      )
    }
  }
}
