import { Component, OnInit } from '@angular/core';
import { MenuItem } from 'primeng/api';
import { Costanti } from '../../Costanti';

@Component({
  selector: 'app-disaccoppiato-controlli-superati',
  templateUrl: './disaccoppiato-controlli-superati.component.html',
  styleUrls: ['./disaccoppiato-controlli-superati.component.css']
})
export class DisaccoppiatoControlliSuperatiComponent implements OnInit {
  menu2: Array<MenuItem>;

  constructor() { }

  ngOnInit() {
    this.menu2 = new Array<MenuItem>(
      { routerLink: Costanti.superficiImpegnate, label: 'Superfici Impegnate' },
      { routerLink: Costanti.dichiarazioni, label: 'Dichiarazioni' },
      { routerLink: Costanti.informazioniDomanda, label: 'Informazioni di domanda' },
      { routerLink: Costanti.controlliSostegno, label: 'Controlli di sostegno' },
      { routerLink: Costanti.datiDomanda, label: 'Esiti di domanda' },
      { routerLink: Costanti.datiPerPascolo, label: 'Esiti per pascolo' },
      { routerLink: Costanti.datiParticella, label: 'Dati per particella' },
      { routerLink: Costanti.datiIstruttoria, label: 'Inserimento dati Istruttoria' }
    );
  }
}
