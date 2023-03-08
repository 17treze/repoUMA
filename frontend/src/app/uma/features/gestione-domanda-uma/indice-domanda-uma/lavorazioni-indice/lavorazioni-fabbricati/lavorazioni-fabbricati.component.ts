import { FormatConverterService } from './../../../../../shared-uma/services/format-converter.service';
import { ActionByInput } from './../../../../../../a4g-common/a4g-multi-table/a4g-multi-table-model';
import { DichiarazioneFabbricatoDto } from '../../../../../core-uma/models/dto/DichiarazioneFabbricatoDto';
import { Location } from '@angular/common';
import { Component, EventEmitter, Input, OnInit, Output, ViewChild } from '@angular/core';
import { FormGroup, FormControl } from '@angular/forms';
import { ActivatedRoute, Params } from '@angular/router';
import { ConfirmationService, MessageService } from 'primeng/api';
import { forkJoin, Subscription } from 'rxjs';
import { switchMap } from 'rxjs/operators';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';
import { A4gMultiTableColumn, A4gMultiTable, FontTypeEnum, AlignTypeEnum, ColumnTypeEnum, ActionInputEnum } from 'src/app/a4g-common/a4g-multi-table/a4g-multi-table-model';
import { AmbitoLavorazioneEnum } from 'src/app/uma/core-uma/models/dto/LavorazioneFilterDto';
import { RaggruppamentoLavorazioneDto } from 'src/app/uma/core-uma/models/dto/RaggruppamentoDto';
import { FabbisognoDichiarato } from 'src/app/uma/core-uma/models/viewModels/FabbisognoDichiaratoViewModel';
import { LavorazioneViewModel } from 'src/app/uma/core-uma/models/viewModels/LavorazioneVIewModel';
import { HttpClientLavorazioniUmaService } from 'src/app/uma/core-uma/services/http-client-lavorazioni-uma.service';
import { UMA_MESSAGES } from 'src/app/uma/uma.messages';
import * as _ from 'lodash';
import { Accordion, AccordionTab } from 'primeng/accordion';
import { KeyValuePair } from 'src/app/a4g-common/classi/KeyValuePair';
import { LavorazioneFabbricatiViewModel } from 'src/app/uma/core-uma/models/viewModels/LavorazioneFabbricatiViewModel';
import { LavorazioniFabbricatiTipo } from 'src/app/uma/core-uma/models/enums/LavorazioniFabbricatiTipo.enum';
import { RaggruppamentoLavorazioneFabbricatoViewModel } from 'src/app/uma/core-uma/models/viewModels/RaggruppamentoLavorazioneFabbricatoViewModel';
import { TipoCarburante } from 'src/app/uma/core-uma/models/enums/TipoCarburante.enum';
import { LavorazioneFabbricatiBuilderService } from 'src/app/uma/core-uma/services/lavorazione-fabbricati-builder.service';
import { RichiestaCarburanteDto } from 'src/app/uma/core-uma/models/dto/RichiestaCarburanteDto';
import { GestioneDomandaUmaService } from '../../../gestione-domanda-uma.service';
import { ErrorService } from 'src/app/a4g-common/services/error.service';

@Component({
  selector: 'app-lavorazioni-fabbricati',
  templateUrl: './lavorazioni-fabbricati.component.html',
  styleUrls: ['./lavorazioni-fabbricati.component.scss']
})
export class LavorazioniFabbricatiComponent implements OnInit {

  @Input() inCompilazione: boolean;                         /** se lo stato è in compilazione, è possibile modificare la pagina, altrimenti può essere consultata */
  @Input() ctx: LavorazioniFabbricatiTipo;                  /** contesto = coltivazioni sotto serra o trasformazione prodotti */
  @Input() richiestaCarburante: RichiestaCarburanteDto;
  @Output() changeFabbisogno = new EventEmitter<boolean>(); /** aggiorna il padre se ci sono modifiche ai fabbisogni */
  @ViewChild('accordion') accordion: Accordion;

  openedAccordion: number;                                  /** indice dell'accordion correntemente aperto */
  quantitaLavorata: Array<number>;
  idDomanda: string;
  ambitoLavorazioneEnum = AmbitoLavorazioneEnum;
  lavorazioniFabbricatiTipoEnum = LavorazioniFabbricatiTipo;
  nomeRaggruppamento: string;
  isChangedInputTable: boolean;
  isValidFormTable: boolean;
  raggruppamentoForm: FormGroup;
  raggruppamenti: Array<RaggruppamentoLavorazioneFabbricatoViewModel>;
  fabbisognoDichiarato: FabbisognoDichiarato;
  datasources: Array<LavorazioneViewModel>;       /** ogni tabella ha un suo datasource: vettore dei datasource */
  colsArray: Array<Array<A4gMultiTableColumn>>;   /** ogni tabella ha le sue colonne: array di colonne */
  tables: Array<A4gMultiTable>;                   /** array di tabelle -> una per ciascuna lavorazione */
  headersTable: Array<Array<KeyValuePair>>;       /** intestazione di un sottogruppo di una tabella -> contiene i dati del fabbricato */

  // Subscriptions
  routerSubscription: Subscription;
  saveFabbisognoSubscription: Subscription;

  constructor(
    private activeRoute: ActivatedRoute,
    private gestioneDomandaUmaService: GestioneDomandaUmaService,
    private httpClientLavorazioniUmaService: HttpClientLavorazioniUmaService,
    private confirmationService: ConfirmationService,
    private messageService: MessageService,
    private location: Location,
    private lavorazioneFabbricatiBuiderService: LavorazioneFabbricatiBuilderService,
    private errorService: ErrorService,
    private formatConverterService: FormatConverterService
  ) { }

  ngOnInit() {
    this.raggruppamentoForm = new FormGroup({
      raggruppamento: new FormControl()
    });
    this.initVariables();
    this.loadRaggruppamenti();
  }

  initVariables() {
    this.fabbisognoDichiarato = this.gestioneDomandaUmaService.getTipoCarburanteDichiarato(this.richiestaCarburante);
    this.nomeRaggruppamento = null;
    this.isValidFormTable = true;
    this.colsArray = [];
    this.tables = [];
    this.datasources = [];
    this.quantitaLavorata = [];
    this.headersTable = [];
    this.raggruppamentoForm.controls['raggruppamento'].setValue(null);
  }

  onSelectAccordion($event: any) {
    if (this.openedAccordion != null && this.openedAccordion == $event.index) {
      return;
    }
    if (this.openedAccordion == null) {
      this.openedAccordion = $event.index; // setto la prima volta openedAccordion
    }
    // se già ne è uno aperto -> fai il check con la dialog
    if (this.isChangedInputTable) {
      this.showDialog($event);
    } else {
      this.openedAccordion = $event.index;
      this.onChangeRaggruppamento($event);
    }
  }

  openAccordionByIndex(indexToOpen: number) {
    this.accordion.tabs.forEach((tab: AccordionTab, index: number) => {
      if (index == indexToOpen) {
        tab.selected = true;
      } else {
        tab.selected = false;
      }
    });
  }

  onChangeForm($event: { isChanged: boolean, isValid: boolean, action?: ActionByInput }) {
    // setto a changed, quando scatta l'onChange su un qualsiasi input della tabella
    this.isChangedInputTable = $event.isChanged;
    this.changeFabbisogno.emit(this.isChangedInputTable);
    // setto a valida quando il formato dei valori inseriti negli input della tabella è corretto
    this.isValidFormTable = $event.isValid;
    // in base alla action eseguo l'azione da fare - [Solo nel caso di Lavorazioni sotto serra]
    if ($event && $event.action && $event.action.type && $event.action.type === 'MULTIPLY_BY') {
      this.tables.forEach((table: A4gMultiTable, index: number) => {
        if (index === $event.action.index) {
          table.datasource.forEach((element: LavorazioneFabbricatiViewModel) => {
            element.GASOLIO = (this.formatConverterService.toNumber(element.volume) * this.formatConverterService.toNumber(element.mesi)).toString();
            element.BENZINA = (this.formatConverterService.toNumber(element.volume) * this.formatConverterService.toNumber(element.mesi)).toString();
          });
        }
      });
    }
  }

  goBack() {
    if (this.isChangedInputTable) {
      this.confirmationService.confirm({
        message: UMA_MESSAGES.datiNonSalvati,
        accept: () => {
          this.location.back();
        },
        reject: () => { },
        key: "lavorazioni-dialog"
      });
    } else {
      this.location.back();
    }
  }

  saveLavorazioni(): boolean {
    // check select raggruppamento
    if (this.raggruppamentoForm.get('raggruppamento').value == null) {
      this.errorService.showErrorWithMessage(UMA_MESSAGES.raggruppamentiNotSelezionati, 'tst-lav-fab');
      return false;
    }
    // check formato input tabella
    if (!this.isValidFormTable) {
      this.errorService.showErrorWithMessage(UMA_MESSAGES.datiNonValidi, 'tst-lav-fab');
      return false;
    }
    // salvataggio
    const raggruppamentoSelezionato: RaggruppamentoLavorazioneFabbricatoViewModel = this.raggruppamentoForm.get('raggruppamento').value;
    const dichiarazioniFabbricati: Array<DichiarazioneFabbricatoDto> = this.lavorazioneFabbricatiBuiderService.lavorazioniViewModelToDichiarazioniFabbricatoDtoBuilder(raggruppamentoSelezionato.lavorazioni);
    console.log('Salvo dichiarazioni: ', dichiarazioniFabbricati);
    this.saveFabbisognoSubscription = this.httpClientLavorazioniUmaService.saveFabbisogniFabbricati(this.idDomanda, dichiarazioniFabbricati).subscribe(() => {
      this.isChangedInputTable = false;
      this.changeFabbisogno.emit(this.isChangedInputTable);
      this.messageService.add(A4gMessages.getToast('tst-lav-fab', A4gSeverityMessage.success, UMA_MESSAGES.salvataggioOKLavorazioniSuperficie));
      this.loadRaggruppamenti();
    }, error => this.errorService.showError(error, 'tst-lav-fab'));
    return true;
  }

  private loadRaggruppamenti() {
    this.routerSubscription = this.activeRoute.params
      .pipe(
        switchMap((params: Params) => {
          this.idDomanda = params['idDomanda'];
          const ambito = this.ctx === LavorazioniFabbricatiTipo.COLTIVAZIONI_SOTTO_SERRA ? this.ambitoLavorazioneEnum.SERRE : this.ambitoLavorazioneEnum.FABBRICATI;
          const getLavorazioneObs$ = this.httpClientLavorazioniUmaService.getLavorazioni(this.idDomanda, ambito);
          const getDichiarazioniObs$ = this.httpClientLavorazioniUmaService.getFabbisogniFabbricati(this.idDomanda, ambito);
          return forkJoin([getLavorazioneObs$, getDichiarazioniObs$]);
        }))
      .subscribe(([raggruppamenti, fabbisogniFabbricati]: [Array<RaggruppamentoLavorazioneDto>, Array<DichiarazioneFabbricatoDto>]) => {
        this.raggruppamenti = this.lavorazioneFabbricatiBuiderService.fabbricatiDtoToRaggruppamentiLavorazioneViewModel(raggruppamenti, fabbisogniFabbricati);
        // ordino per indice
        this.raggruppamenti = _.orderBy(this.raggruppamenti, ['indice'], ['asc']);
        // calcolo se visualizzare il check su accordion
        this.calcolaQuantita();
      }, error => this.errorService.showError(error, 'tst-lav-fab'));
  }

  private showDialog($event: any) {
    this.confirmationService.confirm({
      message: UMA_MESSAGES.datiNonSalvati,
      accept: () => {
        this.openedAccordion = $event.index;
        this.onChangeRaggruppamento($event);
        this.openAccordionByIndex(this.openedAccordion);
      },
      reject: () => {
        this.openAccordionByIndex(this.openedAccordion);
      },
      key: "lavorazioni-dialog"
    });
  }

  onChangeRaggruppamento($event: any) {
    const raggruppamentoSelezionato: RaggruppamentoLavorazioneFabbricatoViewModel = this.raggruppamenti[$event.index];
    this.nomeRaggruppamento = raggruppamentoSelezionato.indice + ' ' + raggruppamentoSelezionato.nome;
    this.tables = [];
    this.colsArray = [];
    this.datasources = [];
    this.headersTable = [];
    this.splitDatasources(raggruppamentoSelezionato.lavorazioni);
    this.raggruppamentoForm.controls['raggruppamento'].setValue(raggruppamentoSelezionato);
    // resetto i controlli sulla tabella
    this.isChangedInputTable = false;
    this.changeFabbisogno.emit(this.isChangedInputTable);
  }

  private calcolaQuantita() {
    if (this.raggruppamenti && this.raggruppamenti.length) {
      this.raggruppamenti.forEach((raggruppamento: RaggruppamentoLavorazioneFabbricatoViewModel, index) => {
        this.quantitaLavorata[index] = 0;
        raggruppamento.lavorazioni.forEach((lavorazione: LavorazioneFabbricatiViewModel) => {
          const qtabenzina: number = this.formatConverterService.toNumber(lavorazione.BENZINA);
          const qtaGasolio: number = this.formatConverterService.toNumber(lavorazione.GASOLIO);
          this.quantitaLavorata[index] = this.quantitaLavorata[index] + qtabenzina + qtaGasolio;
        });
      });
    }
  }

  private splitDatasources(lavorazioneViewArray: Array<LavorazioneFabbricatiViewModel>) {
    if (!lavorazioneViewArray || !lavorazioneViewArray.length) {
      return;
    }
    // raggruppo per fabbricati
    const fabbricatoWithLavorazioni: { [key: string]: Array<LavorazioneFabbricatiViewModel> } = _.groupBy(lavorazioneViewArray, 'idFabbricato');
    // Per ciascun fabbricato, le lavorazioni relative ad esso sono il datasource di una tabella -> ad ogni fabbricato corrisponde una tabella
    Object.keys(fabbricatoWithLavorazioni).forEach((fabbricato: string, index: number) => {
      const lavorazioni: Array<LavorazioneFabbricatiViewModel> = fabbricatoWithLavorazioni[fabbricato];
      const table: A4gMultiTable = new A4gMultiTable();
      this.headersTable.push(this.defineHeader(lavorazioni));
      table.title = !this.tables.length ? this.nomeRaggruppamento : '';   // solo la prima ha l'intestazione verde 
      const cols: Array<A4gMultiTableColumn> = this.ctx === LavorazioniFabbricatiTipo.COLTIVAZIONI_SOTTO_SERRA ? this.defineColumnsSottoSerraByTitle(lavorazioni[0].tipo, index, 'Quantità') : this.defineColumnsFabbricatiByTitle(lavorazioni[0].tipo);
      this.colsArray.push(cols);
      table.datasource = lavorazioni;
      this.datasources.push(...table.datasource);
      this.tables.push(table);
    });
  }

  ngOnDestroy() {
    if (this.routerSubscription) {
      this.routerSubscription.unsubscribe();
    }
    if (this.saveFabbisognoSubscription) {
      this.saveFabbisognoSubscription.unsubscribe();
    }
  }

  private defineHeader(lavorazioni: Array<LavorazioneFabbricatiViewModel>) {
    if (!lavorazioni || !lavorazioni.length) {
      return;
    }
    const header: Array<KeyValuePair> =
      [
        { key: 'Comune', value: lavorazioni[0].comuneProvincia },
        { key: 'Particella', value: this.buildParticella(lavorazioni[0]) },
        { key: 'Volume mc', value: lavorazioni[0].volume || '' }
      ];
    return header;
  }

  private buildParticella(lavorazione: LavorazioneFabbricatiViewModel) {
    let subalterno: string;
    if (!this.formatConverterService.isEmptyString(lavorazione.subalterno)) {
      subalterno = '(' + lavorazione.subalterno + ')';
    }
    return subalterno ? lavorazione.particella + subalterno : lavorazione.particella;
  }

  private defineColumnsFabbricatiByTitle(title: string, headerDimensioneColumn: string = 'Quantità'): Array<A4gMultiTableColumn> {
    return [
      {
        field: "descrizioneCompleta",
        header: title,
        sortable: false,
        font: FontTypeEnum.BOLD,
        width: '80%',
        alignCellHorizontal: AlignTypeEnum.LEFT,
        alignHeader: AlignTypeEnum.LEFT,
        type: ColumnTypeEnum.READONLY
      },
      {
        field: TipoCarburante.GASOLIO,
        suffix: 'unitaDiMisura',
        header: headerDimensioneColumn,
        sortable: false,
        disabled: false,
        inputOpts: { pattern: '^([0-9])+$', maxLength: '7' },
        width: '20%',
        alignCellHorizontal: AlignTypeEnum.CENTER,
        alignHeader: AlignTypeEnum.CENTER,
        type: ColumnTypeEnum.INPUT_NUMBER
      }
    ];
  }

  private defineColumnsSottoSerraByTitle(title: string, tableIndex: number, headerDimensioneColumn: string = 'Quantità'): Array<A4gMultiTableColumn> {
    return [
      {
        field: "descrizioneCompleta",
        header: title,
        sortable: false,
        font: FontTypeEnum.BOLD,
        width: '60%',
        alignCellHorizontal: AlignTypeEnum.LEFT,
        alignHeader: AlignTypeEnum.LEFT,
        type: ColumnTypeEnum.READONLY
      },
      {
        field: 'mesi',
        header: 'Mesi',
        sortable: false,
        disabled: false,
        inputOpts: { pattern: '^([0-9])+$', maxLength: '1', max: 6, action: { type: ActionInputEnum.MULTIPLY_BY, byField: this.quantitaLavorata, index: tableIndex } },
        width: '20%',
        alignCellHorizontal: AlignTypeEnum.CENTER,
        alignHeader: AlignTypeEnum.CENTER,
        type: ColumnTypeEnum.INPUT_NUMBER_ACTION
      },
      {
        field: TipoCarburante.GASOLIO,
        suffix: 'unitaDiMisura',
        header: headerDimensioneColumn,
        sortable: false,
        disabled: true,
        inputOpts: { pattern: '^([0-9])+$', maxLength: '7' },
        width: '20%',
        alignCellHorizontal: AlignTypeEnum.CENTER,
        alignHeader: AlignTypeEnum.CENTER,
        type: ColumnTypeEnum.INPUT_NUMBER
      }
    ];
  }

}

