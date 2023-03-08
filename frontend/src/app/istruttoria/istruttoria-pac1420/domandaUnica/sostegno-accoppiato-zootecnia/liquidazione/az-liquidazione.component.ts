import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { Configuration } from 'src/app/app.constants';
import { Labels } from 'src/app/app.labels';
import { StatoProcesso } from 'src/app/istruttoria/istruttoria-antimafia/dto/StatoProcesso';
import { TipoProcesso } from 'src/app/istruttoria/istruttoria-antimafia/dto/TipoProcesso';
import { LoaderService } from 'src/app/loader.service';
import { AzCountResult, AzCountFilter } from '../../classi/AzCount';
import { IstruttoriaService } from '../../istruttoria.service';
import { StatoIstruttoriaEnum } from '../../cruscotto-sostegno/StatoIstruttoriaEnum';
import { MenuItem } from 'primeng/api';
import { GroupTabCommonComponent } from '../../sostegno-shared/GroupTabCommonComponent';
import { SostegnoDu } from '../../classi/SostegnoDu';
import { TipoIstruttoriaEnum } from '../../classi/TipoIstruttoriaEnum';

@Component({
  selector: 'app-az-liquidazione',
  templateUrl: '../../sostegno-shared/container-menu2-elenco-istruttorie.component.html',
  styleUrls: ['../../sostegno-shared/container-menu2-elenco-istruttorie.component.scss']
})
export class AzLiquidazioneComponent extends GroupTabCommonComponent implements OnInit {
  labels = Labels;
  itemBadges: AzCountResult = new AzCountResult();
  tabNumber: number = 0;

  constructor(
    private loaderService: LoaderService,
    protected istruttoriaService: IstruttoriaService,
    private http: HttpClient,
    protected activatedRoute: ActivatedRoute,
    private _configuration: Configuration,
    protected router: Router) {
      super(
        SostegnoDu.ZOOTECNIA,
        TipoIstruttoriaEnum.SALDO,
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
  
  checkProcessoZootecniaRun(tipoProcesso: TipoProcesso): Observable<any> {
    let params = encodeURIComponent(JSON.stringify({ tipoProcesso: tipoProcesso, statoProcesso: StatoProcesso.RUN }));
    return this.http.get(this._configuration.urlControlloAntimafiaStatoAvanzamento + params);
  }
}
