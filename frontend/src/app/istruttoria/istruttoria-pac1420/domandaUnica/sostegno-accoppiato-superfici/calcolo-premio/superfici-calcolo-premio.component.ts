import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { MenuItem } from 'primeng/api';
import { IstruttoriaService } from '../../istruttoria.service';
import { StatoIstruttoriaEnum } from '../../cruscotto-sostegno/StatoIstruttoriaEnum';
import { LoaderService } from '../../../../../loader.service';
import { GroupTabCommonComponent } from '../../sostegno-shared/GroupTabCommonComponent';
import { SostegnoDu } from '../../classi/SostegnoDu';
import { TipoIstruttoriaEnum } from '../../classi/TipoIstruttoriaEnum';

@Component({
  selector: 'app-superfici-calcolo-premio',
  templateUrl: '../../sostegno-shared/container-menu2-elenco-istruttorie.component.html',
  styleUrls: ['../../sostegno-shared/container-menu2-elenco-istruttorie.component.scss']
})
export class SuperficiCalcoloPremioComponent extends GroupTabCommonComponent implements OnInit {
  constructor(
    activatedRoute: ActivatedRoute,
    protected loaderService: LoaderService,
    istruttoriaService: IstruttoriaService,
    router: Router) {
      super(
        SostegnoDu.SUPERFICIE,
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
