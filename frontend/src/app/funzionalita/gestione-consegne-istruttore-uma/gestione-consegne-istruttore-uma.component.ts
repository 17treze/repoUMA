import { FormService } from '../../a4g-common/services/form.service';
import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { CalendarService } from 'src/app/a4g-common/services/calendar.service';
import { Subscription } from 'rxjs';
import { ErrorService } from 'src/app/a4g-common/services/error.service';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';
import { Calendar, MessageService, SelectItem } from 'primeng-lts';
import { HttpClientDistributoriService } from 'src/app/uma/core-uma/services/http-client-distributori.service';
import { DistributoreCarburanteDto } from 'src/app/uma/shared-uma/models/dto/DistributoreCarburanteDto';
import { DistributoriCarburanteBuilderService } from 'src/app/uma/core-uma/services/builders/distributori-carburante-builder.service copy';
import { PrelievoDto } from 'src/app/uma/core-uma/models/dto/PrelievoDto';
import { PrelieviFilter } from 'src/app/uma/core-uma/models/dto/PrelieviFilter';
import { A4gMultiTableColumn, AlignTypeEnum, ColumnTypeEnum } from 'src/app/a4g-common/a4g-multi-table/a4g-multi-table-model';
import { PrelievoViewModel } from 'src/app/uma/core-uma/models/viewModels/PrelievoViewModel';
import { UMA_MESSAGES } from 'src/app/uma/uma.messages';
import { PrelieviBuilderService } from 'src/app/uma/core-uma/services/builders/prelievi-builder.service';
import { ErrorDTO } from 'src/app/a4g-common/interfaces/error.model';
import { DateUtilService } from 'src/app/a4g-common/services/date-util.service';
import { environment } from 'src/environments/environment';
import * as _ from 'lodash';
@Component({
  selector: 'app-gestione-consegne-istruttore-uma',
  templateUrl: './gestione-consegne-istruttore-uma.component.html',
  styleUrls: ['./gestione-consegne-istruttore-uma.component.scss']
})
export class GestioneConsegneComponentIstruttoreUMA implements OnInit, OnDestroy {
  @ViewChild("dataPrelievoRef")
  dataPrelievoRef: Calendar;

  gestioneForm: FormGroup;
  listaDistributori: Array<SelectItem>;
  valoriCampagna: Array<SelectItem>;
  umaMessages = UMA_MESSAGES;

  // Tabella
  datasourcePrelievi: Array<PrelievoViewModel>;
  isChangedInputTable: boolean;
  isValidFormTable: boolean;
  cols: Array<A4gMultiTableColumn>;

  //Subscriptions
  aggiornaPrelieviByIdDistributoreSubscription: Subscription;
  getDistributoriSubscription: Subscription;

  constructor(
    private httpClientDistributoriService: HttpClientDistributoriService,
    private dateUtilService: DateUtilService,
    public calendarService: CalendarService,
    private errorService: ErrorService,
    private messageService: MessageService,
    private distributoriCarburanteBuilderService: DistributoriCarburanteBuilderService,
    private prelieviBuilderService: PrelieviBuilderService,
    private formService: FormService
  ) { }

  ngOnInit() {
    this.initForm();
    this.initData();
  }

  ngOnDestroy(): void {
    if (this.aggiornaPrelieviByIdDistributoreSubscription) {
      this.aggiornaPrelieviByIdDistributoreSubscription.unsubscribe();
    }
    if (this.getDistributoriSubscription) {
      this.getDistributoriSubscription.unsubscribe();
    }
  }

  onSubmit() {
    this.ricercaPrelievi();
  }

  ricercaPrelievi() {
    this.formService.validateForm(this.gestioneForm);
    if (this.gestioneForm.get('distributore').value == null) {
      this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, UMA_MESSAGES.noDistributoreUmaSelezionato));
      this.datasourcePrelievi = [];
      return;
    }
    let filter: PrelieviFilter = null;
    if (this.gestioneForm.get('dataPrelievo').value) {
      filter = { dataPrelievo: this.dateUtilService.toLocalDate(this.gestioneForm.get('dataPrelievo').value), campagna: this.gestioneForm.get('campagna').value };
    } else {
      filter = { dataPrelievo: null, campagna: this.gestioneForm.get('campagna').value };
    }
    console.log('ricerca prelievo per: ', filter);
    this.getDistributoriSubscription = this.httpClientDistributoriService.getPrelieviByDistributore(this.gestioneForm.get('distributore').value, filter).subscribe((listaPrelievi: Array<PrelievoDto>) => {
      if (listaPrelievi && listaPrelievi.length) {
        this.datasourcePrelievi = this.prelieviBuilderService.prelieviDtoToPrelieviViewModelWithDenominazioneBuilder(listaPrelievi);
        this.gestioneForm.get('checkbox').setValue(false);
      } else {
        this.datasourcePrelievi = [];
      }
    }, (error: ErrorDTO) => {
      this.errorService.showError(error, 'tst-consegne');
      this.datasourcePrelievi = [];
    });
  }

  salva() {
    if (!this.isValidFormTable) {
      this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, UMA_MESSAGES.datiNonValidi));
      return;
    }

    const prelieviDto: Array<PrelievoDto> = this.prelieviBuilderService.prelievoViewModelPrelievoDtoNoDistributoriBuilder(this.datasourcePrelievi);
    this.aggiornaPrelieviByIdDistributoreSubscription = this.httpClientDistributoriService.aggiornaPrelieviByIdDistributore(this.gestioneForm.get('distributore').value, prelieviDto).subscribe(response => {
      this.ricercaPrelievi();
      this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.success, UMA_MESSAGES.aggiornamentoOK));
    }, error => this.errorService.showError(error, 'tst-consegne'));
  }

  onChangeForm($event: { isChanged: boolean, isValid: boolean }) {
    // setto a changed, quando scatta l'onChange su un qualsiasi input della tabella
    this.isChangedInputTable = $event.isChanged;
    // setto a valida quando il formato dei valori inseriti negli input della tabella è corretto
    this.isValidFormTable = $event.isValid;
  }

  onClickCheckbox($event: any) {
    if (this.datasourcePrelievi.every(prelievo => prelievo.isConsegnato == true)) {
      this.gestioneForm.get('checkbox').setValue(true);
    } else {
      this.gestioneForm.get('checkbox').setValue(false);
    }
  }

  private initForm() {
    this.gestioneForm = new FormGroup({
      distributore: new FormControl(null, [Validators.required]),
      dataPrelievo: new FormControl(null),
      campagna: new FormControl(null, [Validators.required]),
      checkbox: new FormControl()
    });
  }

  private initData() {
    this.datasourcePrelievi = null;
    this.isValidFormTable = true;
    this.calendarService.configITA();
    this.cols = this.defineColumns();
    this.valoriCampagna = [];
    // Sono visualizzabili tutte le domande a partire dall'anno di campagna uma configurato
    let fromYear = environment.annoInizioCampagnaUma;
    let untilYear = this.dateUtilService.getCurrentYear() + 1;
    let range: Array<string> = _.range(fromYear, untilYear);
    range.forEach(year =>  this.valoriCampagna.push({ label: year.toString(), value: year }));

    // Di default setto l'anno corrente
    this.gestioneForm.get('campagna').setValue(this.dateUtilService.getCurrentYear());

    this.getDistributoriList();
  }

  refreshDistributori() {
    this.datasourcePrelievi = null;
    this.getDistributoriList();
  }

  private getDistributoriList() {
    this.getDistributoriSubscription = this.httpClientDistributoriService.getDistributori(this.gestioneForm.get('campagna').value).subscribe((distributori: Array<DistributoreCarburanteDto>) => {
      const unorderedList: Array<SelectItem> =  this.distributoriCarburanteBuilderService.distributoreDtoToDistributoreVmBuilder(distributori);
      this.listaDistributori = _.orderBy(unorderedList, ['label'], ['asc']);
      this.listaDistributori.unshift({ label: 'Seleziona un distributore', value: null });
      this.gestioneForm.get('distributore').setValue(null);
    }, error => this.errorService.showError(error, 'tst-consegne'));
  }

  private defineColumns(): Array<A4gMultiTableColumn> {
    return [
      {
        field: 'data',
        header: 'Data prelievo',
        sortable: true,
        disabled: false,
        width: '15%',
        alignCellHorizontal: AlignTypeEnum.LEFT,
        alignHeader: AlignTypeEnum.LEFT,
        type: ColumnTypeEnum.READONLY
      },
      {
        field: 'cuaa',
        header: 'Cuaa',
        sortable: true,
        width: '20%',
        alignCellHorizontal: AlignTypeEnum.LEFT,
        alignHeader: AlignTypeEnum.LEFT,
        type: ColumnTypeEnum.READONLY
      },
      {
        field: 'denominazione',
        header: 'Denominazione Azienda',
        sortable: true,
        width: '35%',
        alignCellHorizontal: AlignTypeEnum.LEFT,
        alignHeader: AlignTypeEnum.LEFT,
        type: ColumnTypeEnum.READONLY
      },
      {
        field: 'gasolio',
        header: 'Gasolio',
        sortable: false,
        width: '10%',
        inputOpts: { pattern: '^([0-9])+$', maxLength: '7' },
        alignCellHorizontal: AlignTypeEnum.RIGHT,
        alignHeader: AlignTypeEnum.CENTER,
        type: ColumnTypeEnum.INPUT_NUMBER
      },
      {
        field: 'benzina',
        header: 'Benzina',
        sortable: false,
        width: '10%',
        inputOpts: { pattern: '^([0-9])+$', maxLength: '7' },
        alignCellHorizontal: AlignTypeEnum.RIGHT,
        alignHeader: AlignTypeEnum.CENTER,
        type: ColumnTypeEnum.INPUT_NUMBER
      },
      {
        field: 'gasolioSerre',
        header: 'Gasolio serre',
        sortable: false,
        width: '10%',
        inputOpts: { pattern: '^([0-9])+$', maxLength: '7' },
        alignCellHorizontal: AlignTypeEnum.RIGHT,
        alignHeader: AlignTypeEnum.CENTER,
        type: ColumnTypeEnum.INPUT_NUMBER
      },
      {
        field: 'isConsegnato',
        header: 'Consegnato',
        sortable: false,
        width: '10%',
        alignCellHorizontal: AlignTypeEnum.CENTER,
        alignHeader: AlignTypeEnum.CENTER,
        type: ColumnTypeEnum.CHECKBOX
      }
    ];
  }
}
