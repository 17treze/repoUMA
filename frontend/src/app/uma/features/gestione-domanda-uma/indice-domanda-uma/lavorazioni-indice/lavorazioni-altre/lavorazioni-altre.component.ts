import { GestioneDomandaUmaService } from '../../../gestione-domanda-uma.service';
import { Location } from '@angular/common';
import { Component, EventEmitter, Input, OnInit, Output, ViewChild } from '@angular/core';
import { FormGroup, FormControl } from '@angular/forms';
import { ActivatedRoute, Params } from '@angular/router';
import { ConfirmationService, MessageService } from 'primeng/api';
import { EMPTY, forkJoin, Subscription } from 'rxjs';
import { switchMap, catchError } from 'rxjs/operators';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';
import { A4gMultiTable, A4gMultiTableColumn, AlignTypeEnum, ColumnTypeEnum, FontTypeEnum } from 'src/app/a4g-common/a4g-multi-table/a4g-multi-table-model';
import { AmbitoLavorazioneEnum } from 'src/app/uma/core-uma/models/dto/LavorazioneFilterDto';
import { RaggruppamentoLavorazioneDto } from 'src/app/uma/core-uma/models/dto/RaggruppamentoDto';
import { TipoCarburante } from 'src/app/uma/core-uma/models/enums/TipoCarburante.enum';
import { TipologiaLavorazioneEnum } from 'src/app/uma/core-uma/models/enums/TipologiaLavorazione.enum';
import { LavorazioneViewModel } from 'src/app/uma/core-uma/models/viewModels/LavorazioneVIewModel';
import { UMA_MESSAGES } from 'src/app/uma/uma.messages';
import { FabbisognoDichiarato } from '../../../../../core-uma/models/viewModels/FabbisognoDichiaratoViewModel';
import * as _ from 'lodash';
import { HttpClientLavorazioniUmaService } from 'src/app/uma/core-uma/services/http-client-lavorazioni-uma.service';
import { Accordion } from 'primeng/accordion';
import { LavorazioniBuiderService } from 'src/app/uma/core-uma/services/lavorazioni-builder.service';
import { RaggruppamentoLavorazioneViewModel } from 'src/app/uma/core-uma/models/viewModels/RaggruppamentoLavorazioneViewModel';
import { DichiarazioneDto } from 'src/app/uma/core-uma/models/dto/DichiarazioneDto';
import { RichiestaCarburanteDto } from 'src/app/uma/core-uma/models/dto/RichiestaCarburanteDto';
import { ErrorService } from 'src/app/a4g-common/services/error.service';
import { ErrorDTO } from 'src/app/a4g-common/interfaces/error.model';

@Component({
  selector: 'app-lavorazioni-altre',
  templateUrl: './lavorazioni-altre.component.html',
  styleUrls: ['./lavorazioni-altre.component.scss']
})
export class LavorazioniAltreComponent implements OnInit {
  @Input() inCompilazione: boolean;                         /** se lo stato è in compilazione, è possibile modificare la pagina, altrimenti può essere consultata */
  @Input() richiestaCarburante: RichiestaCarburanteDto;
  @Output() changeFabbisogno = new EventEmitter<boolean>(); /** aggiorna il padre se ci sono modifiche ai fabbisogni */
  @ViewChild('accordion') accordion: Accordion;

  openedAccordion: number;                                  /** indice dell'accordion correntemente aperto */

  quantitaLavorata: Array<number>;
  idDomanda: string;
  ambitoLavorazioneEnum = AmbitoLavorazioneEnum;
  nomeRaggruppamento: string;
  isChangedInputTable: boolean;
  isValidFormTable: boolean;
  raggruppamentoForm: FormGroup;
  raggruppamenti: Array<RaggruppamentoLavorazioneViewModel>;
  fabbisognoDichiarato: FabbisognoDichiarato;
  datasources: Array<LavorazioneViewModel>;       /** ogni tabella ha un suo datasource: vettore dei datasource */
  colsArray: Array<Array<A4gMultiTableColumn>>;   /** ogni tabella ha le sue colonne: array di colonne */
  tables: Array<A4gMultiTable>;                   /** array di tabelle -> una per ciascuna lavorazione */

  // Subscriptions
  routerSubscription: Subscription;
  saveFabbisogniSubscription: Subscription;

  constructor(
    private activeRoute: ActivatedRoute,
    private gestioneDomandaUmaService: GestioneDomandaUmaService,
    private httpClientLavorazioniUmaService: HttpClientLavorazioniUmaService,
    private confirmationService: ConfirmationService,
    private messageService: MessageService,
    private location: Location,
    private lavorazioniBuiderService: LavorazioniBuiderService,
    private errorService: ErrorService
  ) { }

  ngOnInit() {
    this.raggruppamentoForm = new FormGroup({
      raggruppamento: new FormControl()
    });
    this.initVariables();
    this.loadRaggruppamenti();
  }

  private calcolaQuantita() {
    if (this.raggruppamenti && this.raggruppamenti.length) {
      this.raggruppamenti.forEach((raggruppamento: RaggruppamentoLavorazioneViewModel, index) => {
        this.quantitaLavorata[index] = 0;
        raggruppamento.lavorazioni.forEach((lavorazione: LavorazioneViewModel) => {
          const qtabenzina: number = !isNaN(Number(lavorazione.BENZINA)) ? Number(lavorazione.BENZINA) : 0;
          const qtaGasolio: number = !isNaN(Number(lavorazione.GASOLIO)) ? Number(lavorazione.GASOLIO) : 0;
          this.quantitaLavorata[index] = this.quantitaLavorata[index] + qtabenzina + qtaGasolio;
        });
      });
    }
  }

  private loadRaggruppamenti() {
    this.routerSubscription = this.activeRoute.params
      .pipe(
        switchMap((params: Params) => {
          this.idDomanda = params['idDomanda'];
          const getLavorazioneObs$ = this.httpClientLavorazioniUmaService.getLavorazioni(this.idDomanda, this.ambitoLavorazioneEnum.ALTRO);
          const getDichiarazioniObs$ = this.httpClientLavorazioniUmaService.getDichiarazioni(this.idDomanda, this.ambitoLavorazioneEnum.ALTRO);
          return forkJoin([getLavorazioneObs$, getDichiarazioniObs$]);
        }),
        catchError((err: ErrorDTO) => {
          this.errorService.showError(err, 'tst-lav-altre');
          return EMPTY;
        }),
      )
      .subscribe(([raggruppamenti, dichiarazioni]: [Array<RaggruppamentoLavorazioneDto>, Array<DichiarazioneDto>]) => {
        this.raggruppamenti = this.lavorazioniBuiderService.raggruppamentoLavorazioneDtoToRaggruppmanetoLavorazioneViewModel(raggruppamenti, dichiarazioni);
        // ordino per indice
        this.raggruppamenti = _.orderBy(this.raggruppamenti, ['indice'], ['asc']);
        // calcolo se visualizzare il check su accordion
        this.calcolaQuantita();
      }, error => this.errorService.showError(error, 'tst-lav-altre'));
  }

  initVariables() {
    this.fabbisognoDichiarato = this.gestioneDomandaUmaService.getTipoCarburanteDichiarato(this.richiestaCarburante);
    this.nomeRaggruppamento = null;
    this.isValidFormTable = true;
    // this.isChangedInputTable = false;
    // this.changeFabbisogno.emit(this.isChangedInputTable);
    this.colsArray = [];
    this.tables = [];
    this.datasources = [];
    this.quantitaLavorata = [];
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
    this.accordion.tabs.forEach((tab, index) => {
      if (index == indexToOpen) {
        tab.selected = true;
      } else {
        tab.selected = false;
      }
    });
  }

  onChangeRaggruppamento($event: any) {
    const raggruppamentoSelezionato: RaggruppamentoLavorazioneViewModel = this.raggruppamenti[$event.index];
    this.nomeRaggruppamento = raggruppamentoSelezionato.indice + ' ' + raggruppamentoSelezionato.nome;
    this.splitDatasources(raggruppamentoSelezionato.lavorazioni);
    this.raggruppamentoForm.controls['raggruppamento'].setValue(raggruppamentoSelezionato);
    // resetto i controlli sulla tabella
    this.isChangedInputTable = false;
    this.changeFabbisogno.emit(this.isChangedInputTable);
  }

  onChangeForm($event: { isChanged: boolean, isValid: boolean }) {
    // setto a changed, quando scatta l'onChange su un qualsiasi input della tabella
    this.isChangedInputTable = $event.isChanged;
    this.changeFabbisogno.emit(this.isChangedInputTable);
    // setto a valida quando il formato dei valori inseriti negli input della tabella è corretto
    this.isValidFormTable = $event.isValid;
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
      this.errorService.showErrorWithMessage(UMA_MESSAGES.raggruppamentiNotSelezionati, 'tst-lav-altre');

      return false;
    }
    // check formato input tabella
    if (!this.isValidFormTable) {
      this.errorService.showErrorWithMessage(UMA_MESSAGES.datiNonValidi, 'tst-lav-altre');
      return false;
    }
    // salvataggio
    const raggruppamentoSelezionato: RaggruppamentoLavorazioneViewModel = this.raggruppamentoForm.get('raggruppamento').value;
    const dichiarazioni: Array<DichiarazioneDto> = this.lavorazioniBuiderService.lavorazioniViewModelToDichiarazioniDtoBuilder(raggruppamentoSelezionato.lavorazioni);
    this.saveFabbisogniSubscription = this.httpClientLavorazioniUmaService.saveFabbisogni(this.idDomanda, dichiarazioni).subscribe(() => {
      this.isChangedInputTable = false;
      this.changeFabbisogno.emit(this.isChangedInputTable);
      this.messageService.add(A4gMessages.getToast('tst-lav-altre', A4gSeverityMessage.success, UMA_MESSAGES.salvataggioOKLavorazioniSuperficie));
      this.loadRaggruppamenti();
    }, error => this.errorService.showError(error, 'tst-lav-altre'));
    return true;
  }

  private splitDatasources(lavorazioneViewArray: Array<LavorazioneViewModel>) {

    const lavorazionibyTipo: { [key in TipologiaLavorazioneEnum]: Array<LavorazioneViewModel> } = _.groupBy(lavorazioneViewArray, 'tipo');
    this.tables = [];
    this.colsArray = [];
    this.datasources = [];

    Object.keys(lavorazionibyTipo).forEach((tipologia: TipologiaLavorazioneEnum) => {
      let table: A4gMultiTable = new A4gMultiTable();
      this.tables.length == 0 ? table.title = this.nomeRaggruppamento : "";   // solo la prima ha l'intestazione verde 
      let cols: Array<A4gMultiTableColumn> = this.defineColumnsByTitle(TipologiaLavorazioneEnum[tipologia]);
      this.colsArray.push(cols);
      table.datasource = lavorazionibyTipo[tipologia];
      this.datasources.push(table.datasource);
      this.tables.push(table);
    });
  }

  ngOnDestroy() {
    if (this.routerSubscription) {
      this.routerSubscription.unsubscribe();
    }
    if (this.saveFabbisogniSubscription) {
      this.saveFabbisogniSubscription.unsubscribe();
    }
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

  private defineColumnsByTitle(title: string): Array<A4gMultiTableColumn> {
    return [
      {
        field: "descrizioneCompleta",
        header: title === 'STRAORDINARIA' ? 'Lavorazioni Straordinarie' : 'Lavorazioni Base',
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
        header: 'Gasolio',
        sortable: false,
        disabled: this.fabbisognoDichiarato != null && !this.fabbisognoDichiarato.gasolio,
        inputOpts: { pattern: '^([0-9])+$', maxLength: '7' },
        width: '20%',
        alignCellHorizontal: AlignTypeEnum.CENTER,
        alignHeader: AlignTypeEnum.CENTER,
        type: ColumnTypeEnum.INPUT_NUMBER
      },
      {
        field: TipoCarburante.BENZINA,
        suffix: 'unitaDiMisura',
        header: 'Benzina',
        sortable: false,
        disabled: this.fabbisognoDichiarato != null && !this.fabbisognoDichiarato.benzina,
        inputOpts: { pattern: '^([0-9])+$', maxLength: '7' },
        width: '20%',
        alignCellHorizontal: AlignTypeEnum.CENTER,
        alignHeader: AlignTypeEnum.CENTER,
        type: ColumnTypeEnum.INPUT_NUMBER
      }
    ];
  }

}
