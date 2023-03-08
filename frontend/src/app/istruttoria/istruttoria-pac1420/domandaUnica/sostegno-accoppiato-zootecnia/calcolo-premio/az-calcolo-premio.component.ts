import { Component, OnInit } from '@angular/core';
import { LoaderService } from 'src/app/loader.service';
import { ActivatedRoute, Router } from '@angular/router';
import { IstruttoriaService } from '../../istruttoria.service';
import { StatoIstruttoriaEnum } from '../../cruscotto-sostegno/StatoIstruttoriaEnum';
import { MenuItem } from 'primeng/api';
import { GroupTabCommonComponent } from '../../sostegno-shared/GroupTabCommonComponent';
import { Configuration } from 'src/app/app.constants';
import { SostegnoDu } from '../../classi/SostegnoDu';
import { TipoIstruttoriaEnum } from '../../classi/TipoIstruttoriaEnum';

@Component({
  selector: 'app-az-calcolo-premio',
  templateUrl: '../../sostegno-shared/container-menu2-elenco-istruttorie.component.html',
  styleUrls: ['../../sostegno-shared/container-menu2-elenco-istruttorie.component.scss']
})
export class AzCalcoloPremioComponent extends GroupTabCommonComponent implements OnInit {
  
  constructor(
    activatedRoute: ActivatedRoute,
    istruttoriaService: IstruttoriaService,
    private loaderService: LoaderService,
    private _configuration: Configuration,
    router: Router) {
      super(
        SostegnoDu.ZOOTECNIA,
        TipoIstruttoriaEnum.SALDO,
        activatedRoute,
        istruttoriaService,
        router);
      this.menu2 = new Array<MenuItem>(
        {
          id: StatoIstruttoriaEnum.RICHIESTO,
          command: (event: any) => {
            this.router.navigate([StatoIstruttoriaEnum.RICHIESTO], { relativeTo: this.activatedRoute });
            this.aggiornaContatori();
          }
        }, {
          id: StatoIstruttoriaEnum.INTEGRATO,
          command: (event: any) => {
            this.router.navigate([StatoIstruttoriaEnum.INTEGRATO], { relativeTo: this.activatedRoute });
            this.aggiornaContatori();
          },
        }, {
          id: StatoIstruttoriaEnum.CONTROLLI_CALCOLO_OK,
          badge: 'true',
          command: (event: any) => {
            this.router.navigate([StatoIstruttoriaEnum.CONTROLLI_CALCOLO_OK], { relativeTo: this.activatedRoute });
            this.aggiornaContatori();
          }
        }, {
          id: StatoIstruttoriaEnum.CONTROLLI_CALCOLO_KO,
          badge: 'true',
          command: (event: any) => {
            this.router.navigate([StatoIstruttoriaEnum.CONTROLLI_CALCOLO_KO], { relativeTo: this.activatedRoute });
            this.aggiornaContatori();
          }
        }, {
          id: StatoIstruttoriaEnum.NON_AMMISSIBILE,
          badge: 'true',
          command: (event: any) => {
            this.router.navigate([StatoIstruttoriaEnum.NON_AMMISSIBILE], { relativeTo: this.activatedRoute });
            this.aggiornaContatori();
          }
        },
      );
  }

  ngOnDestroy(): void {
    super.ngOnDestroy();
    this.loaderService.resetTimeout();
  }

  ngOnInit() {
    super.ngOnInit();
    this.loaderService.setTimeout(480000); //otto minuti
  }
}
