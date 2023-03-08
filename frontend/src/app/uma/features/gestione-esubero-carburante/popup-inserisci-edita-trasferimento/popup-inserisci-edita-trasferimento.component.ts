import { DichiarazioneConsumiDto } from './../../../core-uma/models/dto/DichiarazioneConsumiDto';
import { Component, ElementRef, EventEmitter, Input, OnDestroy, OnInit, Output, ViewChild } from '@angular/core';
import { MessageService } from 'primeng-lts';
import { Subscription } from 'rxjs';
import { A4gMultiTableColumn, AlignTypeEnum, ColumnTypeEnum } from 'src/app/a4g-common/a4g-multi-table/a4g-multi-table-model';
import { ErrorService } from 'src/app/a4g-common/services/error.service';
import { CarburanteDto } from 'src/app/uma/core-uma/models/dto/CarburanteDto';
import { CarburanteTrasferibileVM } from 'src/app/uma/core-uma/models/viewModels/CarburanteTrasferibileVM';
import { HttpClientTrasferimentiCarburanteService } from 'src/app/uma/core-uma/services/http-client-trasferimenti-carburante.service';
import { FormatConverterService } from 'src/app/uma/shared-uma/services/format-converter.service';
import { UMA_MESSAGES } from 'src/app/uma/uma.messages';
import { StatoDichiarazioneConsumiEnum } from 'src/app/a4g-common/classi/enums/uma/StatoDichiarazioneConsumi.enum';

@Component({
  selector: 'app-popup-inserisci-edita-trasferimento',
  templateUrl: './popup-inserisci-edita-trasferimento.component.html',
  styleUrls: ['./popup-inserisci-edita-trasferimento.component.scss']
})
export class PopupInserisciEditaTrasferimentoComponent implements OnInit, OnDestroy {

  @ViewChild('dialog', { read: ElementRef, static: true }) dialogElement: ElementRef;

  @Input() READONLY_MODE: boolean
  @Output() chiudiPopup = new EventEmitter();

  modalitaCrea: boolean;
  display: boolean;
  idRichiestaMittente: number;
  idRichiestaDestinatario: number;
  dichiarazioneConsumi: DichiarazioneConsumiDto;
  isChangedInputTable: boolean;
  isValidFormTable: boolean;

  cols: Array<A4gMultiTableColumn>;
  datasource: Array<CarburanteTrasferibileVM>;

  idTrasferimento: String;
  idMittente: String;
  idDestinatario: String;
  cuaa: String;
  denominazione: String;

  // Subscriptions
  postTrasferimentoSub: Subscription;
  putTrasferimento: Subscription;

  constructor(
    private formatConverterService: FormatConverterService,
    private errorService: ErrorService,
    private trasferimentiService: HttpClientTrasferimentiCarburanteService,
    private messageService: MessageService
  ) { }

  ngOnInit() {
    this.cols = this.defineColumns();
  }

  ngOnDestroy() {
    if (this.postTrasferimentoSub) {
      this.postTrasferimentoSub.unsubscribe();
    }
    if (this.putTrasferimento) {
      this.putTrasferimento.unsubscribe();
    }
  }

  open(data: {
    trasferibile: CarburanteDto, cuaa: String, denominazione: String, idTrasferimento?: String,
    trasferimento?: CarburanteDto, idMittente?: String, idDestinatario?: String, modalitaCrea?: boolean, dichiarazioneConsumi?: DichiarazioneConsumiDto
  }) {
    this.isValidFormTable = true;
    this.display = true;
    this.modalitaCrea = data.modalitaCrea;
    this.dichiarazioneConsumi = data.dichiarazioneConsumi;
    this.cuaa = data.cuaa;
    this.denominazione = data.denominazione;

    // Dati inserimento
    this.idMittente = data.idMittente;
    this.idDestinatario = data.idDestinatario;

    // Nel caso della modifica viene caricato il carburante trasferito
    if (this.modalitaCrea) {
      this.buildDataSource(data.trasferibile);
    } else {
      this.idTrasferimento = data.idTrasferimento;
      this.buildDataSource(data.trasferibile, data.trasferimento);
    }

  }

  salva($event: Event) {
    const benzina = this.datasource.filter((el: CarburanteTrasferibileVM) => el.carburante == "Benzina")[0];
    const gasolio = this.datasource.filter((el: CarburanteTrasferibileVM) => el.carburante == "Gasolio")[0];
    const gasolioSerre = this.datasource.filter((el: CarburanteTrasferibileVM) => el.carburante == "Gasolio Serre")[0];

    // Controllo se è stata inserita almeno una quantità
    if (this.formatConverterService.toNumber(benzina.trasferito) == 0
      && this.formatConverterService.toNumber(gasolio.trasferito) == 0
      && this.formatConverterService.toNumber(gasolioSerre.trasferito) == 0) {
      this.errorService.showErrorWithMessage(UMA_MESSAGES.trasferimentoObbligatorio, 'tst-dialog');
      return;
    }
    // Controlli se il trasferito è maggiore del trasferibile
    if (this.formatConverterService.toNumber(gasolio.trasferibile) < this.formatConverterService.toNumber(gasolio.trasferito)) {
      this.errorService.showErrorWithMessage(UMA_MESSAGES.trasferimentoGasolioLtError, 'tst-dialog');
      return;
    }
    if (this.formatConverterService.toNumber(benzina.trasferibile) < this.formatConverterService.toNumber(benzina.trasferito)) {
      this.errorService.showErrorWithMessage(UMA_MESSAGES.trasferimentoBenzinaLtError, 'tst-dialog');
      return;
    }
    if (this.formatConverterService.toNumber(gasolioSerre.trasferibile) < this.formatConverterService.toNumber(gasolioSerre.trasferito)) {
      this.errorService.showErrorWithMessage(UMA_MESSAGES.trasferimentoGasolioSerreLtError, 'tst-dialog');
      return;
    }

    if (this.modalitaCrea) {
      this.postTrasferimentoSub = this.trasferimentiService.postTrasferimento(this.idMittente, this.idDestinatario, {
        gasolio: gasolio.trasferito ? gasolio.trasferito : 0,
        benzina: benzina.trasferito ? benzina.trasferito : 0,
        gasolioSerre: gasolioSerre.trasferito ? gasolioSerre.trasferito : 0
      } as CarburanteDto).subscribe(res => {
        console.log("Salvataggio effettuato correttamente!");
        this.chiudiPopup.emit(true);
        this.display = false;
      }, error => this.errorService.showError(error, 'tst-dialog'));
    } else { // modalita modifica
      if (this.dichiarazioneConsumi && this.dichiarazioneConsumi.stato === StatoDichiarazioneConsumiEnum.PROTOCOLLATA) {
        this.errorService.showErrorWithMessage(UMA_MESSAGES.modificaTrasferimentoKO, 'tst-dialog');
        return;
      }
      this.putTrasferimento = this.trasferimentiService.putTrasferimento(this.idTrasferimento, {
        gasolio: this.formatConverterService.toNumber(gasolio.trasferito ? gasolio.trasferito : 0),
        benzina: this.formatConverterService.toNumber(benzina.trasferito ? benzina.trasferito : 0),
        gasolioSerre: this.formatConverterService.toNumber(gasolioSerre.trasferito ? gasolioSerre.trasferito : 0)
      }).subscribe(res => {
        console.log("Aggiornamento effettuato correttamente!");
        this.chiudiPopup.emit(true);
        this.display = false;
      }, error => this.errorService.showError(error, 'tst-dialog'))
    }

  }

  onChangeForm($event: { isChanged: boolean, isValid: boolean }) {
    // setto a changed, quando scatta l'onChange su un qualsiasi input della tabella
    this.isChangedInputTable = $event.isChanged;
    // setto a valida quando il formato dei valori inseriti negli input della tabella è corretto
    this.isValidFormTable = $event.isValid;
  }

  buildDataSource(trasferibile: CarburanteDto, trasferito?: CarburanteDto) {
    this.datasource = [
      {
        carburante: 'Gasolio',
        trasferibile: (trasferibile || {} as CarburanteDto).gasolio || Number('0'),
        trasferito: trasferito ? trasferito.gasolio : null
      }, {
        carburante: 'Benzina',
        trasferibile: (trasferibile || {} as CarburanteDto).benzina || Number('0'),
        trasferito: trasferito ? trasferito.benzina : null
      }, {
        carburante: 'Gasolio Serre',
        trasferibile: (trasferibile || {} as CarburanteDto).gasolioSerre || Number('0'),
        trasferito: trasferito ? trasferito.gasolioSerre : null
      }
    ]
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
        field: 'trasferibile',
        header: 'Quantità Trasferibile',
        sortable: false,
        width: '18%',
        alignCellHorizontal: AlignTypeEnum.CENTER,
        alignHeader: AlignTypeEnum.CENTER,
        type: ColumnTypeEnum.READONLY
      },
      {
        field: 'trasferito',
        header: 'Quantità Trasferita',
        sortable: false,
        width: '18%',
        inputOpts: { pattern: '^([0-9])+$', maxLength: '7' },
        alignCellHorizontal: AlignTypeEnum.CENTER,
        alignHeader: AlignTypeEnum.CENTER,
        type: ColumnTypeEnum.INPUT_NUMBER
      }
    ];
  }

  annulla() {
    this.display = false;
  }
}
