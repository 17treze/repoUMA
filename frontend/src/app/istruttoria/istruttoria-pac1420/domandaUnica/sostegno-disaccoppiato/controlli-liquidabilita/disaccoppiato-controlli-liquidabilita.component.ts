import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { MenuItem } from 'primeng/api';
import { Configuration } from 'src/app/app.constants';
import { LoaderService } from '../../../../../loader.service';
import { SostegnoDu } from '../../classi/SostegnoDu';
import { StatoIstruttoriaEnum } from '../../cruscotto-sostegno/StatoIstruttoriaEnum';
import { IstruttoriaService } from '../../istruttoria.service';
import { GroupTabCommonComponent } from '../../sostegno-shared/GroupTabCommonComponent';
import { TabDisaccoppiatoCommonComponent } from '../TabDisaccoppiatoCommonComponent';

@Component({
  selector: 'app-disaccoppiato-controlli-liquidabilita',
  templateUrl: '../../sostegno-shared/container-menu2-elenco-istruttorie.component.html',
  styleUrls: ['../../sostegno-shared/container-menu2-elenco-istruttorie.component.scss']
})
export class DisaccoppiatoControlliLiquidiabilitaComponent extends GroupTabCommonComponent implements OnInit {

  constructor(
    activatedRoute: ActivatedRoute,
    istruttoriaService: IstruttoriaService,
    private loaderService: LoaderService,
    private _configuration: Configuration,
    router: Router) {
      super(
        SostegnoDu.DISACCOPPIATO,
        TabDisaccoppiatoCommonComponent.getTipoIstruttoriaFromString(activatedRoute.snapshot.paramMap.get('tipo')),
        activatedRoute,
        istruttoriaService,
        router);
      this.menu2 = new Array<MenuItem>(
        {
          id: StatoIstruttoriaEnum.CONTROLLI_CALCOLO_OK,
          command: (event: any) => {
            this.router.navigate([StatoIstruttoriaEnum.CONTROLLI_CALCOLO_OK], { relativeTo: this.activatedRoute });
            this.aggiornaContatori();
          }
        }, {
          id: StatoIstruttoriaEnum.LIQUIDABILE,
          command: (event: any) => {
            this.router.navigate([StatoIstruttoriaEnum.LIQUIDABILE], { relativeTo: this.activatedRoute });
            this.aggiornaContatori();
          },
        }, {
          id: StatoIstruttoriaEnum.CONTROLLI_LIQUIDABILE_KO,
          badge: 'true',
          command: (event: any) => {
            this.router.navigate([StatoIstruttoriaEnum.CONTROLLI_LIQUIDABILE_KO], { relativeTo: this.activatedRoute });
            this.aggiornaContatori();
          }
        }, {
          id: StatoIstruttoriaEnum.DEBITI,
          //badge: 'true',
          command: (event: any) => {
            this.router.navigate([StatoIstruttoriaEnum.DEBITI], { relativeTo: this.activatedRoute });
            this.aggiornaContatori();
          }
        }
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
