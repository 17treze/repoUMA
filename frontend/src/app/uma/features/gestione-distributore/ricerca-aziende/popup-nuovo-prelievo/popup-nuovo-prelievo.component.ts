import { PresentaPrelievoDto } from './../../../../core-uma/models/dto/PresentaPrelievoDto';
import { HttpClientDistributoriService } from 'src/app/uma/core-uma/services/http-client-distributori.service';
import { PrelievoViewModel } from 'src/app/uma/core-uma/models/viewModels/PrelievoViewModel';
import { PrelievoDto } from 'src/app/uma/core-uma/models/dto/PrelievoDto';
import { SelectItem } from 'primeng/api';
import { FormatConverterService } from 'src/app/uma/shared-uma/services/format-converter.service';
import { FormService } from '../../../../../a4g-common/services/form.service';
import { DateUtilService } from 'src/app/a4g-common/services/date-util.service';
import { DistributoriBuilderService } from './../../../../core-uma/services/builders/distributori-builder.service';
import { ErrorService } from 'src/app/a4g-common/services/error.service';
import { CalendarService } from 'src/app/a4g-common/services/calendar.service';
import { TipologicheService } from 'src/app/a4g-common/services/tipologiche.service';
import { Component, OnInit, ViewChild, OnDestroy, Output, EventEmitter, Input } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Dialog } from 'primeng-lts/dialog';
import { A4gMultiTableColumn, AlignTypeEnum, ColumnTypeEnum } from 'src/app/a4g-common/a4g-multi-table/a4g-multi-table-model';
import { HttpClientUtenteService } from 'src/app/uma/shared-uma/services/http-client-utente.service';
import { Subscription } from 'rxjs';
import { UMA_MESSAGES } from 'src/app/uma/uma.messages';
import { CarburantePrelevabileVM } from 'src/app/uma/core-uma/models/viewModels/CarburantePrelevabileViewModel';
import { PrelievoCarburanteVM } from 'src/app/uma/core-uma/models/viewModels/PrelievoCarburanteViewModel';
import { PrelieviBuilderService } from 'src/app/uma/core-uma/services/builders/prelievi-builder.service';
import { CarburanteDto } from 'src/app/uma/core-uma/models/dto/CarburanteDto';
import * as moment from 'moment';
@Component({
  selector: 'app-popup-nuovo-prelievo',
  templateUrl: './popup-nuovo-prelievo.component.html',
  styleUrls: ['./popup-nuovo-prelievo.component.scss']
})
export class PopupNuovoPrelievoComponent implements OnInit, OnDestroy {
  @ViewChild('dialogNuovoPrelievo') dialog: Dialog;
  @Input() distributoriVM: Array<SelectItem>;
  @Output() onHide = new EventEmitter<any>();

  READONLY_MODE: boolean;
  prelievoForm: FormGroup;
  display: boolean = false;
  eventClosing: { type: 'SAVE' | 'UPDATE' | 'KO', model: PrelievoDto };
  eventOpening: { type: 'SAVE' | 'UPDATE' };
  cols: Array<A4gMultiTableColumn>;
  isChangedInputTable: boolean;
  isValidFormTable: boolean;
  datasource: Array<CarburantePrelevabileVM>;
  umaMessages = UMA_MESSAGES;
  prelievoDto: PrelievoDto;
  idRichiesta: string;
  idPrelievo: number;
  distributoreId: number;
  minDate: Date;
  maxDate: Date

  // Subscriptions
  getDistributoriSubscription: Subscription;
  salvaPrelieviSubscription: Subscription;

  constructor(
    public tipologicheService: TipologicheService,
    public calendarService: CalendarService,
    private httpClientUtenteService: HttpClientUtenteService,
    private errorService: ErrorService,
    private distributoriBuilderService: DistributoriBuilderService,
    private dateUtilService: DateUtilService,
    private formService: FormService,
    private formatConverterService: FormatConverterService,
    private prelieviBuilderService: PrelieviBuilderService,
    private httpClientDistributoriService: HttpClientDistributoriService
  ) { }

  ngOnInit() {
    this.initForm();
    this.initData();
  }

  ngOnDestroy(): void {
    if (this.getDistributoriSubscription) {
      this.getDistributoriSubscription.unsubscribe();
    }
    if (this.salvaPrelieviSubscription) {
      this.salvaPrelieviSubscription.unsubscribe();
    }
  }

  open(prelievoLoaded: { idRichiesta: string, prelievo: PrelievoViewModel, prelevabile: CarburanteDto , isDichiarazioneConsumiAutorizzata: boolean}) {
    this.display = true;
    this.isValidFormTable = true;
    this.idRichiesta = prelievoLoaded.idRichiesta;

    this.READONLY_MODE = prelievoLoaded && prelievoLoaded.prelievo && (prelievoLoaded.prelievo.isConsegnato || prelievoLoaded.prelievo.readonly || prelievoLoaded.isDichiarazioneConsumiAutorizzata) ? true : false;
    if (this.READONLY_MODE) {
      this.prelievoForm.get('estremiDocumentoFiscale').disable(); /** bug su disable dell'input -> forzato a mano */
    }
    if (prelievoLoaded.prelievo != null) {
      this.eventOpening.type = 'UPDATE';
      this.idPrelievo = prelievoLoaded.prelievo.id;
      this.distributoreId = prelievoLoaded.prelievo.distributoreId;
      this.initForm(prelievoLoaded.prelievo);
      this.buildDataSource(prelievoLoaded.prelevabile, prelievoLoaded.prelievo);
    } else {
      this.eventOpening.type = 'SAVE';
      this.initForm();
      this.buildDataSource(prelievoLoaded.prelevabile);
    }
  }

  buildDataSource(prelevabile: CarburanteDto, prelievo?: PrelievoViewModel) {
    this.datasource = [
      {
        carburante: 'Gasolio',
        prelevabile: (prelevabile || {} as CarburanteDto).gasolio || Number('0'),
        prelevato: prelievo ? prelievo.gasolio : null
      }, {
        carburante: 'Benzina',
        prelevabile: (prelevabile || {} as CarburanteDto).benzina || Number('0'),
        prelevato: prelievo ? prelievo.benzina : null
      }, {
        carburante: 'Gasolio Serre',
        prelevabile: (prelevabile || {} as CarburanteDto).gasolioSerre || Number('0'),
        prelevato: prelievo ? prelievo.gasolioSerre : null
      }
    ]
  }

  onSubmit() {

  }

  onChangeDistributore() {

  }

  annulla() {
    this.eventClosing.type = 'KO';
    this.display = false;
  }

  salvaPrelievo() {
    this.formService.validateForm(this.prelievoForm);
    if (!this.prelievoForm.valid || !this.isValidFormTable) {
      this.errorService.showErrorWithMessage(UMA_MESSAGES.datiNonValidi, 'tst-prelievo');
      return;
    }
    if (!this.datasource.filter((carburante: CarburantePrelevabileVM) => this.formatConverterService.toNumber(carburante.prelevato) > 0).length) {
      this.errorService.showErrorWithMessage(UMA_MESSAGES.prelevabileObbligatorio, 'tst-prelievo');
      return;
    }
    const benzina = this.datasource.filter((el: CarburantePrelevabileVM) => el.carburante == "Benzina")[0];
    const gasolio = this.datasource.filter((el: CarburantePrelevabileVM) => el.carburante == "Gasolio")[0];
    const gasolioSerre = this.datasource.filter((el: CarburantePrelevabileVM) => el.carburante == "Gasolio Serre")[0];
    if (this.formatConverterService.toNumber(benzina.prelevabile) < this.formatConverterService.toNumber(benzina.prelevato)) {
      this.errorService.showErrorWithMessage(UMA_MESSAGES.prelevabileBenzinaLtDisponibile, 'tst-prelievo');
      return;
    }
    if (this.formatConverterService.toNumber(gasolio.prelevabile) < this.formatConverterService.toNumber(gasolio.prelevato)) {
      this.errorService.showErrorWithMessage(UMA_MESSAGES.prelevabileGasolioLtDisponibile, 'tst-prelievo');
      return;
    }
    if (this.formatConverterService.toNumber(gasolioSerre.prelevabile) < this.formatConverterService.toNumber(gasolioSerre.prelevato)) {
      this.errorService.showErrorWithMessage(UMA_MESSAGES.prelevabileGasolioSerreLtDisponibile, 'tst-prelievo');
      return;
    }
    console.log("FORM: ", this.prelievoForm.value);
    const prelievoCarburanteVm: PrelievoCarburanteVM = this.prelievoForm.value;
    prelievoCarburanteVm.carburante = this.datasource.slice();
    prelievoCarburanteVm.data = this.dateUtilService.toDateWithNoOffset(prelievoCarburanteVm.data);
    prelievoCarburanteVm.id = this.idPrelievo;
    const prelievoDto = this.prelieviBuilderService.prelievoCarburanteVmToPrelievoDtoBuilder(prelievoCarburanteVm);
    if (this.eventOpening.type === 'SAVE') {
      this.invokeSalvaPrelievo(prelievoDto);
    } else if (this.eventOpening.type === 'UPDATE') {
      this.invokeAggiornaPrelievo(prelievoDto);
    }
  }

  private invokeSalvaPrelievo(prelievoDto: PrelievoDto) {
    const presentaPrelievoDto: PresentaPrelievoDto = { idRichiesta: this.idRichiesta, prelievo: prelievoDto };
    this.salvaPrelieviSubscription = this.httpClientDistributoriService.salvaPrelievoByIdentificativoDistributore(prelievoDto.distributore.identificativo.toString(), presentaPrelievoDto).subscribe((res: number) => {
      this.prelievoForm.reset();
      this.eventClosing.type = 'SAVE';
      this.eventClosing.model = prelievoDto;
      this.display = false;
    }, error => this.errorService.showError(error));
  }

  private invokeAggiornaPrelievo(prelievoDto: PrelievoDto) {
    this.salvaPrelieviSubscription = this.httpClientDistributoriService.aggiornaPrelievoByIdentificativoDistributore(this.distributoreId.toString(), prelievoDto.id.toString(), prelievoDto).subscribe((res: number) => {
      this.prelievoForm.reset();
      this.eventClosing.type = 'UPDATE';
      this.eventClosing.model = prelievoDto;
      this.display = false;
    }, error => this.errorService.showError(error));
  }

  private initForm(prelievo?: PrelievoViewModel) {
    this.prelievoForm = new FormGroup({
      identificativoDistributore: new FormControl(prelievo ? prelievo.distributoreIdentificativo : null, [Validators.required]),
      estremiDocumentoFiscale: new FormControl(prelievo ? prelievo.estremiDocumentoFiscale : null),
      data: new FormControl(prelievo ? moment(prelievo.data, 'DD/MM/YYYY').toDate() : null, [Validators.required]),
      isConsegnato: new FormControl(prelievo ? prelievo.isConsegnato : null)
    })
  }

  onChangeForm($event: { isChanged: boolean, isValid: boolean }) {
    // setto a changed, quando scatta l'onChange su un qualsiasi input della tabella
    this.isChangedInputTable = $event.isChanged;
    // setto a valida quando il formato dei valori inseriti negli input della tabella è corretto
    this.isValidFormTable = $event.isValid;
  }

  onHideDialog($event: any) {
    this.onHide.emit(this.eventClosing);
  }

  private initData() {
    this.cols = this.defineColumns();
    this.minDate = this.setMinDate();
    this.maxDate = this.setMaxDate();
    this.eventClosing = {} as { type: 'SAVE' | 'UPDATE' | 'KO', model: PrelievoDto };
    this.eventOpening = {} as { type: 'SAVE' | 'UPDATE' };
  }

  private setMinDate(): Date {
    return this.dateUtilService.toDateFrom(1, 1, this.dateUtilService.getCurrentYear());
  }

  private setMaxDate(): Date {
    return this.dateUtilService.toDateFrom(31, 12, this.dateUtilService.getCurrentYear());
  }

  private defineColumns(): Array<A4gMultiTableColumn> {
    return [
      {
        field: 'carburante',
        header: 'Carburante',
        sortable: false,
        disabled: false,
        width: '44%',
        alignCellHorizontal: AlignTypeEnum.LEFT,
        alignHeader: AlignTypeEnum.LEFT,
        type: ColumnTypeEnum.READONLY
      },
      {
        field: 'prelevabile',
        header: 'Prelevabile',
        sortable: false,
        width: '18%',
        alignCellHorizontal: AlignTypeEnum.CENTER,
        alignHeader: AlignTypeEnum.CENTER,
        type: ColumnTypeEnum.READONLY
      },
      {
        field: 'prelevato',
        header: 'Prelevato',
        sortable: false,
        width: '18%',
        inputOpts: { pattern: '^([0-9])+$', maxLength: '7' },
        alignCellHorizontal: AlignTypeEnum.CENTER,
        alignHeader: AlignTypeEnum.CENTER,
        type: ColumnTypeEnum.INPUT_NUMBER
      }
    ];
  }

}
