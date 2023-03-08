import { TrasferimentoDto } from 'src/app/uma/core-uma/models/dto/CarburanteRicevutoDto';
import { HttpClientTrasferimentiCarburanteService } from '../../../../core-uma/services/http-client-trasferimenti-carburante.service';
import { Component, OnInit } from '@angular/core';
import { Location } from '@angular/common';
import { EMPTY, Subscription } from 'rxjs';
import { IndiceUmaService } from 'src/app/uma/core-uma/services/indice-uma.service';
import { ActivatedRoute, Params } from '@angular/router';
import { A4gMultiTableColumn, FontTypeEnum, AlignTypeEnum, ColumnTypeEnum } from 'src/app/a4g-common/a4g-multi-table/a4g-multi-table-model';
import { catchError, switchMap } from 'rxjs/operators';
import { HttpClientDichiarazioneConsumiUmaService } from 'src/app/uma/core-uma/services/http-client-dichiarazione-consumi-uma.service';
import { DichiarazioneConsumiDto } from 'src/app/uma/core-uma/models/dto/DichiarazioneConsumiDto';
import { RicevutiBuilderService } from 'src/app/uma/core-uma/services/builders/ricevuti-builder.service';
import { ErrorService } from 'src/app/a4g-common/services/error.service';
import { CarburanteTotale } from 'src/app/uma/core-uma/models/dto/CarburanteTotale';
import { ErrorDTO } from 'src/app/a4g-common/interfaces/error.model';
@Component({
  selector: 'app-ricevuti',
  templateUrl: './ricevuti.component.html',
  styleUrls: ['./ricevuti.component.scss']
})
export class RicevutiComponent implements OnInit {
  READONLY_MODE: boolean;
  cols: Array<A4gMultiTableColumn>;
  datasource: Array<any>;

  // Subscriptions
  routerSubscription: Subscription;

  constructor(
    private indiceUmaService: IndiceUmaService,
    private location: Location,
    private route: ActivatedRoute,
    private httpClientDichiarazioneConsumiUmaService: HttpClientDichiarazioneConsumiUmaService,
    private httpCLientCarburanteRicevutoService: HttpClientTrasferimentiCarburanteService,
    private ricevutiBuilderService: RicevutiBuilderService,
    private errorService: ErrorService
  ) { }

  ngOnInit() {
    this.initVariables();
    this.routerSubscription = this.route.params
      .pipe(
        switchMap((params: Params) => {
          return this.httpClientDichiarazioneConsumiUmaService.getDichiarazioneConsumiById(params['id']);
        }),
        catchError((err: ErrorDTO) => {
          this.errorService.showError(err);
          return EMPTY;
        }),
        switchMap((dichiarazione: DichiarazioneConsumiDto) => {
          return this.httpCLientCarburanteRicevutoService.getCarburanteRicevuto(dichiarazione.cuaa, dichiarazione.campagnaRichiesta.toString());
        }))
      .subscribe((ricevuti: CarburanteTotale<TrasferimentoDto>) => {
        this.datasource = this.ricevutiBuilderService.ricevutiDtoToRicevutiViewModelBuilder(ricevuti.dati);
      }, error => this.errorService.showError(error));
  }

  ngOnDestroy(): void {
    if (this.routerSubscription) {
      this.routerSubscription.unsubscribe();
    }
  }

  goBack() {
    this.location.back();
  }

  private initVariables() {
    this.READONLY_MODE = this.indiceUmaService.READONLY_MODE_DICHIARAZIONE || (localStorage.getItem('UMA_RO_DICH') == 'true' ? true : false);
    this.cols = this.defineColumns();
  }

  private defineColumns(): Array<A4gMultiTableColumn> {
    return [
      {
        field: "cuaa",
        header: 'CUAA',
        sortable: true,
        font: FontTypeEnum.BOLD,
        width: '25%',
        alignCellHorizontal: AlignTypeEnum.CENTER,
        alignHeader: AlignTypeEnum.LEFT,
        type: ColumnTypeEnum.READONLY
      },
      {
        field: "denominazione",
        header: 'Denominazione',
        sortable: true,
        font: FontTypeEnum.BOLD,
        width: '45%',
        alignCellHorizontal: AlignTypeEnum.LEFT,
        alignHeader: AlignTypeEnum.LEFT,
        type: ColumnTypeEnum.READONLY
      },
      {
        field: 'gasolio',
        header: 'Gasolio ricevuto (lt.)',
        sortable: false,
        width: '10%',
        alignCellHorizontal: AlignTypeEnum.RIGHT,
        alignHeader: AlignTypeEnum.CENTER,
        type: ColumnTypeEnum.READONLY
      },
      {
        field: 'benzina',
        header: 'Benzina ricevuta (lt.)',
        sortable: false,
        width: '10%',
        alignCellHorizontal: AlignTypeEnum.RIGHT,
        alignHeader: AlignTypeEnum.CENTER,
        type: ColumnTypeEnum.READONLY
      },
      {
        field: 'gasolioSerre',
        header: 'Gasolio serre ricevuto (lt.)',
        sortable: false,
        width: '10%',
        alignCellHorizontal: AlignTypeEnum.RIGHT,
        alignHeader: AlignTypeEnum.CENTER,
        type: ColumnTypeEnum.READONLY
      }
    ];
  }

}
