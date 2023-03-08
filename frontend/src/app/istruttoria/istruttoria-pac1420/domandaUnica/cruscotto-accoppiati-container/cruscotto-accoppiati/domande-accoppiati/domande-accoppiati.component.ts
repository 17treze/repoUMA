import { Component, OnInit, OnDestroy } from '@angular/core';
import { MenuItem } from 'primeng/api';
import { Router, ActivatedRoute } from '@angular/router';
import { HackMenuItem } from '../../../hack-ptab-menu/hack-menu-item';
import { IstruttoriaService } from '../../../istruttoria.service';
import { Costanti } from '../../../Costanti';
import { StatoDomandaEnum } from '../../../dettaglio-istruttoria/statoDomanda';
import { LoaderService } from 'src/app/loader.service';
import { SostegnoDu } from '../../../classi/SostegnoDu';
import { StatoIstruttoriaEnum } from '../../../cruscotto-sostegno/StatoIstruttoriaEnum';

@Component({
  selector: 'app-domande-accoppiati',
  templateUrl: './domande-accoppiati.component.html',
  styleUrls: ['./domande-accoppiati.component.css']
})
export class DomandeAccoppiatiComponent implements OnInit, OnDestroy {

  menu2: Array<HackMenuItem>;
  activeTab: MenuItem;
  idIstruttoria: number;
  selectedSostegno: string;

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private istruttoriaService: IstruttoriaService,
    private loader: LoaderService) {
  }

  ngOnDestroy(): void {
    this.loader.resetTimeout();
  }
  ngOnInit() {
    this.loader.setTimeout(480000); //otto minuti

    if (this.router.url.split('/').filter(url => url === Costanti.cruscottoAccoppiatoZootecnia).length > 0) {
      this.selectedSostegno = SostegnoDu.ZOOTECNIA;
    } else {
      this.selectedSostegno = SostegnoDu.SUPERFICIE;
    }

    this.buildMenu();
    this.filterMenu();
    this.activateMenu();
  }

  private buildMenu() {
    if (this.route.snapshot.routeConfig.path === Costanti.calcoloAccoppiatoSuperficie || this.route.snapshot.routeConfig.path === Costanti.calcoloAccoppiatoZootecnia) {
      this.menu2 = new Array<HackMenuItem>(
        {
          id: StatoIstruttoriaEnum.RICHIESTO,
          command: (event: any) => {
            this.router.navigate([StatoIstruttoriaEnum.RICHIESTO], { relativeTo: this.route });
          },
          label: "Richiesto"
        }, {
          id: StatoIstruttoriaEnum.INTEGRATO,
          command: (event: any) => {
            this.router.navigate([StatoIstruttoriaEnum.INTEGRATO], { relativeTo: this.route });
          },
          label: "Integrazioni Ammissibilità"
        }, {
          id: StatoIstruttoriaEnum.CONTROLLI_CALCOLO_OK,
          command: (event: any) => {
            this.router.navigate([StatoIstruttoriaEnum.CONTROLLI_CALCOLO_OK], { relativeTo: this.route });
          },
          label: "Controlli Superati"
        }, {
          id: StatoIstruttoriaEnum.CONTROLLI_CALCOLO_KO,
          badge: 'true',
          command: (event: any) => {
            this.router.navigate([StatoIstruttoriaEnum.CONTROLLI_CALCOLO_KO], { relativeTo: this.route });
          },
          label: "Controlli Non Superati"
        }, {
          id: StatoIstruttoriaEnum.NON_AMMISSIBILE,
          badge: 'true',
          command: (event: any) => {
            this.router.navigate([StatoIstruttoriaEnum.NON_AMMISSIBILE], { relativeTo: this.route });
          },
          label: "Non Ammissibili"
        },
      );

    }
    if (this.route.snapshot.routeConfig.path === Costanti.controlliLiquidabilita) {
      this.menu2 = new Array<HackMenuItem>(
        {
          id: StatoIstruttoriaEnum.CONTROLLI_CALCOLO_OK,
          command: (event: any) => {
            this.router.navigate([StatoIstruttoriaEnum.CONTROLLI_CALCOLO_OK], { relativeTo: this.route });
          },
          label: "Controlli Superati"
        }, {
          id: StatoIstruttoriaEnum.LIQUIDABILE,
          command: (event: any) => {
            this.router.navigate([StatoIstruttoriaEnum.LIQUIDABILE],
              { relativeTo: this.route });
          },
          label: "Liquidabile"
        }, {
          id: StatoIstruttoriaEnum.CONTROLLI_LIQUIDABILE_KO,
          command: (event: any) => {
            this.router.navigate([StatoIstruttoriaEnum.CONTROLLI_LIQUIDABILE_KO],
              { relativeTo: this.route });
          },
          label: "Controlli liquidabilita' non superati"
        }
      );
    }

    if (this.route.snapshot.routeConfig.path === Costanti.liquidazione) {
      this.menu2 = new Array<HackMenuItem>(
        {
          id: StatoIstruttoriaEnum.LIQUIDABILE,
          command: (event: any) => {
            this.router.navigate([StatoIstruttoriaEnum.LIQUIDABILE], { relativeTo: this.route });
          },
          label: 'Liquidabile'
        }, {
          id: StatoIstruttoriaEnum.NON_LIQUIDABILE,
          command: (event: any) => {
            this.router.navigate([StatoIstruttoriaEnum.NON_LIQUIDABILE], { relativeTo: this.route });
          },
          label: 'Non liquidabile'
        }, {
          id: StatoIstruttoriaEnum.PAGAMENTO_NON_AUTORIZZATO,
          command: (event: any) => {
            this.router.navigate([StatoIstruttoriaEnum.PAGAMENTO_NON_AUTORIZZATO], { relativeTo: this.route });
          },
          label: 'Pagamento non autorizzato'
        }, {
          id: StatoIstruttoriaEnum.CONTROLLI_INTERSOSTEGNO_OK,
          command: (event: any) => {
            this.router.navigate([StatoIstruttoriaEnum.CONTROLLI_INTERSOSTEGNO_OK], { relativeTo: this.route });
          },
          label: 'Controlli intersostegno superati'
        }, {
          id: StatoIstruttoriaEnum.PAGAMENTO_AUTORIZZATO,
          command: (event: any) => {
            this.router.navigate([StatoIstruttoriaEnum.PAGAMENTO_AUTORIZZATO], { relativeTo: this.route });
          },
          label: 'Pagamento autorizzato'
        },
      );
    }

    if (this.route.snapshot.routeConfig.path === Costanti.calcoloAccoppiatoSuperficie)
      this.menu2.splice(1, 1);
  }

  private filterMenu() {
    this.route
      .paramMap
      .subscribe(params => {
        this.idIstruttoria = Number(params.get('idIstruttoria'));
        console.log("IdIstruttoria: " + this.idIstruttoria);
        if (this.route.snapshot.routeConfig.path === Costanti.calcoloAccoppiatoSuperficie || this.route.snapshot.routeConfig.path === Costanti.calcoloAccoppiatoZootecnia) {
          const baseRequest = '{"idDatiSettore": ' + this.idIstruttoria + ', "statoDomanda": "' + StatoDomandaEnum.IN_ISTRUTTORIA +
            '", "sostegno": "' + this.selectedSostegno + '", "statoSostegno" : "RICHIESTO" }';
          const requestJSON = encodeURIComponent(baseRequest);
          this.istruttoriaService.countDomandeFiltered(requestJSON).subscribe(x => {
            this.menu2.filter(y => y.id === 'RICHIESTO')[0].label = 'Richiesto ';
            this.menu2.filter(y => y.id === 'RICHIESTO')[0].value = x;
          });
          const baseRequest1 = '{"idDatiSettore": ' + this.idIstruttoria + ', "statoDomanda": "' + StatoDomandaEnum.IN_ISTRUTTORIA +
            '", "sostegno": "' + this.selectedSostegno + '", "statoSostegno" : "CONTROLLI_CALCOLO_OK" }';
          const requestJSON1 = encodeURIComponent(baseRequest1);
          this.istruttoriaService.countDomandeFiltered(requestJSON1).subscribe(x => {
            this.menu2.filter(y => y.id === 'CONTROLLI_CALCOLO_OK')[0].label = 'Controlli Superati ';
            this.menu2.filter(y => y.id === 'CONTROLLI_CALCOLO_OK')[0].value = x;
          });
          const baseRequest2 = '{"idDatiSettore": ' + this.idIstruttoria + ', "statoDomanda": "' + StatoDomandaEnum.IN_ISTRUTTORIA +
            '", "sostegno": "' + this.selectedSostegno + '", "statoSostegno" : "CONTROLLI_CALCOLO_KO" }';
          const requestJSON2 = encodeURIComponent(baseRequest2);
          this.istruttoriaService.countDomandeFiltered(requestJSON2).subscribe(x => {
            this.menu2.filter(y => y.id === 'CONTROLLI_CALCOLO_KO')[0].label = 'Controlli Non Superati ';
            this.menu2.filter(y => y.id === 'CONTROLLI_CALCOLO_KO')[0].value = x;
          });
          const baseRequest3 = '{"idDatiSettore": ' + this.idIstruttoria + ', "statoDomanda": "' + StatoDomandaEnum.IN_ISTRUTTORIA +
            '", "sostegno": "' + this.selectedSostegno + '", "statoSostegno" : "NON_AMMISSIBILE" }';
          const requestJSON3 = encodeURIComponent(baseRequest3);
          this.istruttoriaService.countDomandeFiltered(requestJSON3).subscribe(x => {
            this.menu2.filter(y => y.id === 'NON_AMMISSIBILE')[0].label = 'Non Ammissibili ';
            this.menu2.filter(y => y.id === 'NON_AMMISSIBILE')[0].value = x;
          })
        }
        if (this.route.snapshot.routeConfig.path === Costanti.calcoloAccoppiatoZootecnia) {
          const baseRequest4 = '{"idDatiSettore": ' + this.idIstruttoria + ', "statoDomanda": "' + StatoDomandaEnum.IN_ISTRUTTORIA +
            '", "sostegno": "' + this.selectedSostegno + '", "statoSostegno" : "INTEGRATO" }';
          const requestJSON4 = encodeURIComponent(baseRequest4);
          this.istruttoriaService.countDomandeFiltered(requestJSON4).subscribe(x => {
            this.menu2.filter(y => y.id === 'INTEGRATO')[0].label = 'Integrazioni Ammissibilità ';
            this.menu2.filter(y => y.id === 'INTEGRATO')[0].value = x;
          });
        }
        if (this.route.snapshot.routeConfig.path === Costanti.controlliLiquidabilita) {
          const baseRequest1 = '{"idDatiSettore": ' + this.idIstruttoria + ', "statoDomanda": "' + StatoDomandaEnum.IN_ISTRUTTORIA +
            '", "sostegno": "' + this.selectedSostegno + '", "statoSostegno" : "CONTROLLI_CALCOLO_OK" }';
          const requestJSON1 = encodeURIComponent(baseRequest1);
          this.istruttoriaService.countDomandeFiltered(requestJSON1).subscribe(x => {

            this.menu2.filter(y => y.id === 'CONTROLLI_CALCOLO_OK')[0].value = x;
          });
          const baseRequest4 = '{"idDatiSettore": ' + this.idIstruttoria + ', "statoDomanda": "' + StatoDomandaEnum.IN_ISTRUTTORIA +
            '", "sostegno": "' + this.selectedSostegno + '", "statoSostegno" : "LIQUIDABILE" }';
          const requestJSON4 = encodeURIComponent(baseRequest4);
          this.istruttoriaService.countDomandeFiltered(requestJSON4).subscribe(x => {

            this.menu2.filter(y => y.id === 'LIQUIDABILE')[0].value = x;
          });
          const baseRequest5 = '{"idDatiSettore": ' + this.idIstruttoria + ', "statoDomanda": "' + StatoDomandaEnum.IN_ISTRUTTORIA +
            '", "sostegno": "' + this.selectedSostegno + '", "statoSostegno" : "CONTROLLI_LIQUIDABILE_KO" }';
          const requestJSON5 = encodeURIComponent(baseRequest5);
          this.istruttoriaService.countDomandeFiltered(requestJSON5).subscribe(x => {

            this.menu2.filter(y => y.id === 'CONTROLLI_LIQUIDABILE_KO')[0].value = x;
          });
        }
        if (this.route.snapshot.routeConfig.path === Costanti.liquidazione) {
          const baseRequest1 = '{"idDatiSettore": ' + this.idIstruttoria + ', "statoDomanda": "' + StatoDomandaEnum.IN_ISTRUTTORIA +
            '", "sostegno": "' + this.selectedSostegno + '", "statoSostegno" : "LIQUIDABILE" }';
          const requestJSON1 = encodeURIComponent(baseRequest1);
          this.istruttoriaService.countDomandeFiltered(requestJSON1).subscribe(x => {
            this.menu2.filter(y => y.id === 'LIQUIDABILE')[0].label = 'Liquidabile ';
            this.menu2.filter(y => y.id === 'LIQUIDABILE')[0].value = x;
          });
          const baseRequest4 = '{"idDatiSettore": ' + this.idIstruttoria + ', "statoDomanda": "' + StatoDomandaEnum.IN_ISTRUTTORIA +
            '", "sostegno":"' + this.selectedSostegno + '", "statoSostegno" : "PAGAMENTO_NON_AUTORIZZATO" }';
          const requestJSON4 = encodeURIComponent(baseRequest4);
          this.istruttoriaService.countDomandeFiltered(requestJSON4).subscribe(x => {
            this.menu2.filter(y => y.id === 'PAGAMENTO_NON_AUTORIZZATO')[0].label = 'Pagamento non autorizzato ';
            this.menu2.filter(y => y.id === 'PAGAMENTO_NON_AUTORIZZATO')[0].value = x;
          });
          const baseRequest3 = '{"idDatiSettore": ' + this.idIstruttoria + ', "statoDomanda": "' + StatoDomandaEnum.IN_ISTRUTTORIA +
            '", "sostegno":"' + this.selectedSostegno + '", "statoSostegno" : "PAGAMENTO_AUTORIZZATO" }';
          const requestJSON3 = encodeURIComponent(baseRequest3);
          this.istruttoriaService.countDomandeFiltered(requestJSON3).subscribe(x => {
            this.menu2.filter(y => y.id === 'PAGAMENTO_AUTORIZZATO')[0].label = 'Pagamento autorizzato ';
            this.menu2.filter(y => y.id === 'PAGAMENTO_AUTORIZZATO')[0].value = x;
          });
          const baseRequest6 = '{"idDatiSettore": ' + this.idIstruttoria + ', "statoDomanda": "' + StatoDomandaEnum.IN_ISTRUTTORIA +
            '", "sostegno": "' + this.selectedSostegno + '", "statoSostegno" : "CONTROLLI_INTERSOSTEGNO_OK" }';
          const requestJSON6 = encodeURIComponent(baseRequest6);
          this.istruttoriaService.countDomandeFiltered(requestJSON6).subscribe(x => {
            this.menu2.filter(y => y.id === 'CONTROLLI_INTERSOSTEGNO_OK')[0].label = 'Controlli intersostegno superati';
            this.menu2.filter(y => y.id === 'CONTROLLI_INTERSOSTEGNO_OK')[0].value = x;
          });
          const baseRequest5 = '{"idDatiSettore": ' + this.idIstruttoria + ', "statoDomanda": "' + StatoDomandaEnum.IN_ISTRUTTORIA +
            '", "sostegno": "' + this.selectedSostegno + '", "statoSostegno" : "NON_LIQUIDABILE" }';
          const requestJSON5 = encodeURIComponent(baseRequest5);
          this.istruttoriaService.countDomandeFiltered(requestJSON5).subscribe(x => {
            this.menu2.filter(y => y.id === 'NON_LIQUIDABILE')[0].label = 'Non liquidabile ';
            this.menu2.filter(y => y.id === 'NON_LIQUIDABILE')[0].value = x;

          });
        }
      });
  }

  private activateMenu() {
    this.activeTab = this.menu2[0];
    this.router.navigate([StatoIstruttoriaEnum.RICHIESTO], { relativeTo: this.route });
    if (this.route.snapshot.routeConfig.path === Costanti.controlliLiquidabilita) {
      this.router.navigate([StatoIstruttoriaEnum.CONTROLLI_CALCOLO_OK], { relativeTo: this.route });
    }
    if (this.route.snapshot.routeConfig.path === Costanti.liquidazione) {
      this.router.navigate([StatoIstruttoriaEnum.LIQUIDABILE], { relativeTo: this.route });
    }
  }
}