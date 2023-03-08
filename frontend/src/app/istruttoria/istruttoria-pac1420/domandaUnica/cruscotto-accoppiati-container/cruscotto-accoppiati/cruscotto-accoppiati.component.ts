import { Component, OnInit } from '@angular/core';
import { MenuItem } from 'primeng/api';
import { Labels } from 'src/app/app.labels';
import { Costanti } from '../../Costanti';
import { SostegnoDu } from '../../classi/SostegnoDu';
import { ActivatedRoute, Router } from '@angular/router';
import { Istruttoria } from 'src/app/a4g-common/classi/Istruttoria';

@Component({
  selector: 'app-cruscotto-accoppiati',
  templateUrl: './cruscotto-accoppiati.component.html',
  styleUrls: ['./cruscotto-accoppiati.component.css']
})
export class CruscottoAccoppiatiComponent implements OnInit {

  menu1: Array<MenuItem>;
  title: String;
  selectedSostegno: string;
  sostegno: string;
  route: string;
  datiIstruttoriaCorrente: Istruttoria;

  constructor(private activatedroute: ActivatedRoute, private router: Router) { }

  ngOnInit() {
    this.datiIstruttoriaCorrente = this.activatedroute.snapshot.data['istruttoria'];
    this.setSostegno();
    this.setMenu();
  }

  setSostegno() {
    if (this.router.url.split('/').filter(url => url === Costanti.cruscottoAccoppiatoZootecnia).length > 0) {
      this.selectedSostegno = SostegnoDu.ZOOTECNIA;
    } else {
      this.selectedSostegno = SostegnoDu.SUPERFICIE;
    }
    if (this.selectedSostegno == SostegnoDu.ZOOTECNIA) {
      this.sostegno = 'zootecnia';
      this.title = Labels.ISTRUTTORIA_ACCOPPIATO_ZOOTECNIA.toUpperCase() + ' - ' + this.datiIstruttoriaCorrente.annoRiferimento + ' - ' + this.datiIstruttoriaCorrente.tipoIstruttoria;
      this.route = Costanti.calcoloAccoppiatoZootecnia;
    }
    else { // 'ACC_SUPERFICI'
      this.sostegno = 'superficie';
      this.title = Labels.ISTRUTTORIA_ACCOPPIATO_SUPERFICIE.toUpperCase() + ' - ' + this.datiIstruttoriaCorrente.annoRiferimento + ' - ' + this.datiIstruttoriaCorrente.tipoIstruttoria;
      this.route = Costanti.calcoloAccoppiatoSuperficie;
    }
    console.log('Sostegno: ' + this.selectedSostegno);
  }

  setMenu() {
    this.menu1 = new Array<MenuItem>(
      {
        routerLink: this.route,
        label: 'Calcolo premio accoppiato ' + this.sostegno
      },
      {
        routerLink: Costanti.controlliLiquidabilita,
        label: 'Controlli Liquidabilità'
      },
      {
        routerLink: Costanti.liquidazione,
        label: 'Liquidazione'
      }
    )
  }
}