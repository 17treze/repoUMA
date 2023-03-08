import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { MenuItem } from 'primeng/api';
import { IstruttoriaService } from '../../istruttoria.service';
import { StatoIstruttoriaEnum } from '../../cruscotto-sostegno/StatoIstruttoriaEnum';
import { LoaderService } from '../../../../../loader.service';
import { GroupTabCommonComponent } from '../../sostegno-shared/GroupTabCommonComponent';
import { SostegnoDu } from '../../classi/SostegnoDu';
import { TabDisaccoppiatoCommonComponent } from '../TabDisaccoppiatoCommonComponent';

@Component({
  selector: 'app-disaccoppiato-liquidazione',
  templateUrl: '../../sostegno-shared/container-menu2-elenco-istruttorie.component.html',
  styleUrls: ['../../sostegno-shared/container-menu2-elenco-istruttorie.component.scss']
})
export class DisaccoppiatoLiquidazioneComponent extends GroupTabCommonComponent implements OnInit {
  constructor(
    private loaderService: LoaderService,
    protected router: Router,
    protected istruttoriaService: IstruttoriaService,
    protected activatedRoute: ActivatedRoute) {
      super(
        SostegnoDu.DISACCOPPIATO,
        TabDisaccoppiatoCommonComponent.getTipoIstruttoriaFromString(activatedRoute.snapshot.paramMap.get('tipo')),
        activatedRoute,
        istruttoriaService,
        router);
      this.menu2 = new Array<MenuItem>(
        {
          id: StatoIstruttoriaEnum.LIQUIDABILE,
          command: (event: any) => {
            this.router.navigate([StatoIstruttoriaEnum.LIQUIDABILE], { relativeTo: this.activatedRoute });
            this.aggiornaContatori();
          }
        }, {
          id: StatoIstruttoriaEnum.NON_LIQUIDABILE,
          command: (event: any) => {
            this.router.navigate([StatoIstruttoriaEnum.NON_LIQUIDABILE], { relativeTo: this.activatedRoute });
            this.aggiornaContatori();
          },
        }, {
          id: StatoIstruttoriaEnum.PAGAMENTO_NON_AUTORIZZATO,
          badge: 'true',
          command: (event: any) => {
            this.router.navigate([StatoIstruttoriaEnum.PAGAMENTO_NON_AUTORIZZATO], { relativeTo: this.activatedRoute });
            this.aggiornaContatori();
          }
        }, {
          id: StatoIstruttoriaEnum.CONTROLLI_INTERSOSTEGNO_OK,
          badge: 'true',
          command: (event: any) => {
            this.router.navigate([StatoIstruttoriaEnum.CONTROLLI_INTERSOSTEGNO_OK], { relativeTo: this.activatedRoute });
            this.aggiornaContatori();
          }
        }, {
          id: StatoIstruttoriaEnum.PAGAMENTO_AUTORIZZATO,
          badge: 'true',
          command: (event: any) => {
            this.router.navigate([StatoIstruttoriaEnum.PAGAMENTO_AUTORIZZATO], { relativeTo: this.activatedRoute });
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
