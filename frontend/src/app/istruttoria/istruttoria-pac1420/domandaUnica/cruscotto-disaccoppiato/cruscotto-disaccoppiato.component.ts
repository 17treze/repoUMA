import { IstruttoriaService } from 'src/app/istruttoria/istruttoria-pac1420/domandaUnica/istruttoria.service';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { A4gCostanti } from 'src/app/a4g-common/a4g-costanti';
import { MenuItem } from 'primeng/api';
import { Costanti } from '../Costanti';
import { Labels } from 'src/app/app.labels';
import { Istruttoria } from 'src/app/a4g-common/classi/Istruttoria';
import { of } from 'rxjs';
import { mapTo, delay, tap } from 'rxjs/operators';

@Component({
  selector: 'app-cruscotto-disaccoppiato',
  templateUrl: './cruscotto-disaccoppiato.component.html',
  styleUrls: ['./cruscotto-disaccoppiato.component.css']
})
export class CruscottoDisaccoppiatoComponent implements OnInit {
  menu1:  Array<MenuItem>;
  title: String;
  datiIstruttoriaCorrente: Istruttoria;
  constructor(private route: ActivatedRoute, private router: Router, private istruttoriaService: IstruttoriaService) {
  }

  ngOnInit() {
    this.menu1 = new Array<MenuItem>(
      {
        label: 'Calcolo premio disaccoppiato',
        routerLink: Costanti.CALCOLO_DISACCOPPIATO
        /*command: (event: any) => {
          this.router.navigate([Costanti.calcoloDisaccoppiato], { relativeTo: this.route });
        }*/
      },
      {  routerLink: Costanti.controlliLiquidabilita, label: 'Controlli Liquidabilita'},
      {  routerLink: Costanti.liquidazione, label: 'Liquidazione'}
      );
  }
}
