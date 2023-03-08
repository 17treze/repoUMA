import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, NavigationEnd, Router } from '@angular/router';
import { MenuItem } from 'primeng/api';
import { Observable } from 'rxjs';
import { filter, takeUntil } from 'rxjs/operators';
import { Configuration } from 'src/app/app.constants';
import { Labels } from 'src/app/app.labels';
import { StatoProcesso } from 'src/app/istruttoria/istruttoria-antimafia/dto/StatoProcesso';
import { TipoProcesso } from 'src/app/istruttoria/istruttoria-antimafia/dto/TipoProcesso';
import { LoaderService } from 'src/app/loader.service';
import { AzCountResult } from '../../classi/AzCount';
import { SostegnoDu } from '../../classi/SostegnoDu';
import { TipoIstruttoriaEnum } from '../../classi/TipoIstruttoriaEnum';
import { StatoIstruttoriaEnum } from '../../cruscotto-sostegno/StatoIstruttoriaEnum';
import { IstruttoriaService } from '../../istruttoria.service';
import { GroupTabCommonComponent } from '../../sostegno-shared/GroupTabCommonComponent';


@Component({
  selector: 'app-az-controlli-liquidabilita',
  templateUrl: '../../sostegno-shared/container-menu2-elenco-istruttorie.component.html',
  styleUrls: ['../../sostegno-shared/container-menu2-elenco-istruttorie.component.scss']
})
export class AzControlliLiquidabilitaComponent extends GroupTabCommonComponent implements OnInit {
  labels = Labels;
  itemBadges: AzCountResult = new AzCountResult();

  constructor(
    activatedRoute: ActivatedRoute,
    istruttoriaService: IstruttoriaService,
    private http: HttpClient,
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
          command: (event: any) => {
            this.router.navigate([StatoIstruttoriaEnum.DEBITI], { relativeTo: this.activatedRoute });
            this.aggiornaContatori();
          }
        }
      );
      
      this.router.events.pipe(
        takeUntil(this.componentDestroyed$),
        filter(event => event instanceof NavigationEnd)
      ).subscribe(event => {
        // let root: ActivatedRoute = this.activatedRoute.root;
        let ne: NavigationEnd = <NavigationEnd>event;
        this.activeTab = this.getActiveTabMenuItem(ne.url);
      });
  }

  ngOnDestroy(): void {
    super.ngOnDestroy();
    this.loaderService.resetTimeout();
  }

  public checkProcessoZootecniaRun(tipoProcesso: TipoProcesso): Observable<any> {
    let params = encodeURIComponent(JSON.stringify({ tipoProcesso: tipoProcesso, statoProcesso: StatoProcesso.RUN }));
    return this.http.get(this._configuration.urlControlloAntimafiaStatoAvanzamento + params);
  }

  ngOnInit() {
    super.ngOnInit();
    this.loaderService.setTimeout(480000); //otto minuti
  }
}
