import { EMPTY, Subscription } from 'rxjs';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { Location } from '@angular/common';
import { IndiceUmaService } from 'src/app/uma/core-uma/services/indice-uma.service';
import { ActivatedRoute, Params } from '@angular/router';
import { A4gMultiTableColumn, FontTypeEnum, AlignTypeEnum, ColumnTypeEnum, A4GTitle } from 'src/app/a4g-common/a4g-multi-table/a4g-multi-table-model';
import { TipoCarburante } from 'src/app/uma/core-uma/models/enums/TipoCarburante.enum';
import { catchError, switchMap } from 'rxjs/operators';
import { HttpClientDichiarazioneConsumiUmaService } from 'src/app/uma/core-uma/services/http-client-dichiarazione-consumi-uma.service';
import { DichiarazioneConsumiDto } from 'src/app/uma/core-uma/models/dto/DichiarazioneConsumiDto';
import { HttpClientDomandaUmaService } from 'src/app/uma/core-uma/services/http-client-domanda-uma.service';
import { PrelievoDto } from 'src/app/uma/core-uma/models/dto/PrelievoDto';
import { PrelievoViewModel } from 'src/app/uma/core-uma/models/viewModels/PrelievoViewModel';
import { PrelieviBuilderService } from 'src/app/uma/core-uma/services/builders/prelievi-builder.service';
import { ErrorService } from 'src/app/a4g-common/services/error.service';
import { CarburanteTotale } from 'src/app/uma/core-uma/models/dto/CarburanteTotale';
import { ErrorDTO } from 'src/app/a4g-common/interfaces/error.model';
@Component({
  selector: 'app-prelievi',
  templateUrl: './prelievi.component.html',
  styleUrls: ['./prelievi.component.scss']
})
export class PrelieviComponent implements OnInit, OnDestroy {
  READONLY_MODE: boolean;
  idDichiarazione: string;
  titles: Array<A4GTitle>;
  cols: Array<A4gMultiTableColumn>;
  datasource: Array<PrelievoViewModel>;

  // Subscriptions
  routerSubscription: Subscription;

  constructor(
    private indiceUmaService: IndiceUmaService,
    private location: Location,
    private route: ActivatedRoute,
    private httpClientDichiarazioneConsumiUmaService: HttpClientDichiarazioneConsumiUmaService,
    private httpClientDomandaUmaService: HttpClientDomandaUmaService,
    private prelieviBuilderService: PrelieviBuilderService,
    private errorService: ErrorService
  ) { }

  ngOnInit() {
    this.initVariables();
    this.routerSubscription = this.route.params
      .pipe(switchMap((params: Params) => {
        this.idDichiarazione = params['id'];
        return this.httpClientDichiarazioneConsumiUmaService.getDichiarazioneConsumiById(this.idDichiarazione);
      }),
      catchError((err: ErrorDTO) => {
        this.errorService.showError(err);
        return EMPTY;
      }),
      switchMap((dichiarazione: DichiarazioneConsumiDto) => {
        return this.httpClientDomandaUmaService.getPrelieviByCuaaAndCampagna(dichiarazione.cuaa, dichiarazione.campagnaRichiesta.toString());
      }))
      .subscribe((prelievi: CarburanteTotale<PrelievoDto>) => {
        this.datasource = this.prelieviBuilderService.prelieviDtoToPrelieviViewModelBuilder(prelievi.dati);
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
    this.titles = [{ name: 'Distributore', width: '62%', align: AlignTypeEnum.CENTER }, { name: 'Prelievo', width: '38%', align: AlignTypeEnum.CENTER }];
    this.cols = this.defineColumns();
  }

  private defineColumns(): Array<A4gMultiTableColumn> {
    return [
      {
        field: 'distributoreNome',
        header: 'Distributore',
        sortable: true,
        font: FontTypeEnum.BOLD,
        width: '38%',
        alignCellHorizontal: AlignTypeEnum.LEFT,
        alignHeader: AlignTypeEnum.LEFT,
        type: ColumnTypeEnum.READONLY
      },
      {
        field: 'distributoreIndirizzo',
        header: 'Indirizzo',
        sortable: false,
        font: FontTypeEnum.BOLD,
        width: '24%',
        alignCellHorizontal: AlignTypeEnum.LEFT,
        alignHeader: AlignTypeEnum.LEFT,
        type: ColumnTypeEnum.READONLY
      },
      {
        field: 'data',
        header: 'Data prelievo',
        sortable: true,
        width: '12%',
        alignCellHorizontal: AlignTypeEnum.RIGHT,
        alignHeader: AlignTypeEnum.CENTER,
        type: ColumnTypeEnum.READONLY
      },
      {
        field: TipoCarburante.GASOLIO.toLocaleLowerCase(),
        header: 'Gasolio',
        sortable: false,
        width: '8%',
        alignCellHorizontal: AlignTypeEnum.RIGHT,
        alignHeader: AlignTypeEnum.CENTER,
        type: ColumnTypeEnum.READONLY
      },
      {
        field: TipoCarburante.BENZINA.toLocaleLowerCase(),
        header: 'Benzina',
        sortable: false,
        width: '8%',
        alignCellHorizontal: AlignTypeEnum.RIGHT,
        alignHeader: AlignTypeEnum.CENTER,
        type: ColumnTypeEnum.READONLY
      },
      {
        field: 'gasolioSerre',
        header: 'Gasolio serre',
        sortable: false,
        width: '10%',
        alignCellHorizontal: AlignTypeEnum.RIGHT,
        alignHeader: AlignTypeEnum.CENTER,
        type: ColumnTypeEnum.READONLY
      }
    ];
  }

}
