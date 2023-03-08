import { Location } from '@angular/common';
import { Component, EventEmitter, Input, OnDestroy, OnInit, Output, ViewChild } from '@angular/core';
import { FormGroup, FormControl } from '@angular/forms';
import { ActivatedRoute, Params } from '@angular/router';
import { MessageService, ConfirmationService } from 'primeng/api';
import { forkJoin, Subscription } from 'rxjs';
import { catchError, switchMap } from 'rxjs/operators';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';
import { A4gMultiTableColumn, FontTypeEnum, AlignTypeEnum, ColumnTypeEnum, A4gMultiTable } from 'src/app/a4g-common/a4g-multi-table/a4g-multi-table-model';
import { AmbitoLavorazioneEnum } from 'src/app/uma/core-uma/models/dto/LavorazioneFilterDto';
import { RaggruppamentoLavorazioneDto } from 'src/app/uma/core-uma/models/dto/RaggruppamentoDto';
import { LavorazioneViewModel } from 'src/app/uma/core-uma/models/viewModels/LavorazioneVIewModel';
import { UMA_MESSAGES } from 'src/app/uma/uma.messages';
import * as _ from 'lodash';
import { TipoCarburante } from 'src/app/uma/core-uma/models/enums/TipoCarburante.enum';
import { TipologiaLavorazioneEnum } from 'src/app/uma/core-uma/models/enums/TipologiaLavorazione.enum';
import { HttpClientLavorazioniUmaService } from 'src/app/uma/core-uma/services/http-client-lavorazioni-uma.service';
import { FabbisognoDichiarato } from '../../../../../core-uma/models/viewModels/FabbisognoDichiaratoViewModel';
import { Accordion } from 'primeng/accordion';
import { LavorazioniBuiderService } from 'src/app/uma/core-uma/services/lavorazioni-builder.service';
import { RaggruppamentoLavorazioneViewModel } from 'src/app/uma/core-uma/models/viewModels/RaggruppamentoLavorazioneViewModel';
import { DichiarazioneDto } from 'src/app/uma/core-uma/models/dto/DichiarazioneDto';
import { GestioneDomandaUmaService } from '../../../gestione-domanda-uma.service';
import { RichiestaCarburanteDto } from 'src/app/uma/core-uma/models/dto/RichiestaCarburanteDto';
import { ErrorService } from 'src/app/a4g-common/services/error.service';
@Component({
  selector: 'app-lavorazioni-zootecnia',
  templateUrl: './lavorazioni-zootecnia.component.html',
  styleUrls: ['./lavorazioni-zootecnia.component.scss']
})
export class LavorazioniZootecniaComponent implements OnInit, OnDestroy {
  @Input() inCompilazione: boolean;                         /** se lo stato è in compilazione, è possibile modificare la pagina, altrimenti può essere consultata */
  @Input() richiestaCarburante: RichiestaCarburanteDto;
  @Output() changeFabbisogno = new EventEmitter<boolean>(); /** aggiorna il padre se ci sono modifiche ai fabbisogni */
  @ViewChild('accordion') accordion: Accordion;

  openedAccordion: number;                                  /** indice dell'accordion correntemente aperto */
  quantitaLavorata: Array<number>;
  tables: Array<A4gMultiTable>;
  datasources: Array<LavorazioneViewModel>;                 // ogni tabella ha un suo datasource: vettore dei datasource
  colsArray: Array<Array<A4gMultiTableColumn>>;             // ogni tabella ha le sue colonne: matrice di colonne
  fabbisognoDichiarato: FabbisognoDichiarato;
  raggruppamentoForm: FormGroup;
  nomeRaggruppamento: string;
  isChangedInputTable: boolean;
  isValidFormTable: boolean;
  resetChanges: boolean;
  raggruppamenti: Array<RaggruppamentoLavorazioneViewModel>;
  idDomanda: string;
  ambitoLavorazioneEnum = AmbitoLavorazioneEnum;

  // Subscriptions
  routerSubscription: Subscription;
  saveFabbisogniSubscription: Subscription;

  constructor(
    private activeRoute: ActivatedRoute,
    private messageService: MessageService,
    private confirmationService: ConfirmationService,
    private gestioneDomandaUmaService: GestioneDomandaUmaService,
    private httpClientLavorazioniUmaService: HttpClientLavorazioniUmaService,
    private lavorazioniBuiderService: LavorazioniBuiderService,
    private location: Location,
    private errorService: ErrorService) { }

  ngOnInit() {
    this.raggruppamentoForm = new FormGroup({
      raggruppamento: new FormControl()
    });
    this.initVariables();
    this.loadRaggruppamenti();
  }

  ngOnDestroy() {
    if (this.routerSubscription) {
      this.routerSubscription.unsubscribe();
    }
    if (this.saveFabbisogniSubscription) {
      this.saveFabbisogniSubscription.unsubscribe();
    }
  }

  private loadRaggruppamenti() {
    this.routerSubscription = this.activeRoute.params
      .pipe(
        switchMap((params: Params) => {
          this.idDomanda = params['idDomanda'];
          const getLavorazioneObs$ = this.httpClientLavorazioniUmaService.getLavorazioni(this.idDomanda, this.ambitoLavorazioneEnum.ZOOTECNIA);
          const getDichiarazioniObs$ = this.httpClientLavorazioniUmaService.getDichiarazioni(this.idDomanda, this.ambitoLavorazioneEnum.ZOOTECNIA);
          return forkJoin([getLavorazioneObs$, getDichiarazioniObs$]);
        }))
      .subscribe(([raggruppamenti, dichiarazioni]: [Array<RaggruppamentoLavorazioneDto>, Array<DichiarazioneDto>]) => {
        this.raggruppamenti = this.lavorazioniBuiderService.raggruppamentoLavorazioneDtoToRaggruppmanetoLavorazioneViewModel(raggruppamenti, dichiarazioni);
        // ordino per indice
        this.raggruppamenti = _.orderBy(this.raggruppamenti, ['indice'], ['asc']);
        // calcolo se visualizzare il check su accordion
        this.calcolaQuantita();
      }, error => this.errorService.showError(error, 'tst-lav-zoot'));
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

  initVariables() {
    this.fabbisognoDichiarato = this.gestioneDomandaUmaService.getTipoCarburanteDichiarato(this.richiestaCarburante);
    this.nomeRaggruppamento = null; // Stringa header verde
    this.isValidFormTable = true;
    // this.isChangedInputTable = false;
    // this.changeFabbisogno.emit(this.isChangedInputTable);
    this.tables = [];
    this.colsArray = [];
    this.datasources = [];
    this.quantitaLavorata = [];
    this.raggruppamentoForm.controls['raggruppamento'].setValue(null);
  }

  goBack() {
    if (this.isChangedInputTable) {
      this.confirmationService.confirm({
        message: UMA_MESSAGES.datiNonSalvati,
        accept: () => {
          this.location.back();
        },
        reject: () => {
        },
        key: "lavorazioni-dialog"
      });
    } else {
      this.location.back();
    }
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

  saveLavorazioni(): boolean {
    // check select raggruppamento
    if (this.raggruppamentoForm.get('raggruppamento').value == null) {
      this.errorService.showErrorWithMessage(UMA_MESSAGES.raggruppamentiNotSelezionati, 'tst-lav-zoot');
      return false;
    }
    // check formato input tabella
    if (!this.isValidFormTable) {
      this.errorService.showErrorWithMessage(UMA_MESSAGES.datiNonValidi, 'tst-lav-zoot');
      return false;
    }
    // salvataggio
    const raggruppamentoSelezionato: RaggruppamentoLavorazioneViewModel = this.raggruppamentoForm.get('raggruppamento').value;
    const dichiarazioni: Array<DichiarazioneDto> = this.lavorazioniBuiderService.lavorazioniViewModelToDichiarazioniDtoBuilder(raggruppamentoSelezionato.lavorazioni);
    console.log('Salvo dichiarazioni: ', dichiarazioni);
    this.saveFabbisogniSubscription = this.httpClientLavorazioniUmaService.saveFabbisogni(this.idDomanda, dichiarazioni).subscribe(() => {
      this.isChangedInputTable = false;
      this.changeFabbisogno.emit(this.isChangedInputTable);
      this.messageService.add(A4gMessages.getToast('tst-lav-zoot', A4gSeverityMessage.success, UMA_MESSAGES.salvataggioOKLavorazioniSuperficie));
      this.loadRaggruppamenti();
    }, error => this.errorService.showError(error, 'tst-lav-zoot'));
    return true;
  }

  private splitDatasources(lavorazioneViewArray: Array<LavorazioneViewModel>) {
    const lavorazionibyTipo: { [key in TipologiaLavorazioneEnum]: Array<LavorazioneViewModel> } = _.groupBy(lavorazioneViewArray, 'tipo');
    this.tables = [];
    this.colsArray = [];
    this.datasources = [];

    Object.keys(lavorazionibyTipo).forEach((tipologia: TipologiaLavorazioneEnum) => {
      let table: A4gMultiTable = new A4gMultiTable();
      let cols: Array<A4gMultiTableColumn> = this.defineColumnsByTitle(TipologiaLavorazioneEnum[tipologia]);
      table.title = !this.tables.length ? this.nomeRaggruppamento : '';   // solo la prima ha l'intestazione verde con indicato il raggruppamento 
      this.colsArray.push(cols);
      table.datasource = lavorazionibyTipo[tipologia];
      this.datasources.push(...table.datasource);
      this.tables.push(table);
    })
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
        header: title,
        sortable: false,
        font: FontTypeEnum.BOLD,
        width: '78%',
        alignCellHorizontal: AlignTypeEnum.LEFT,
        alignHeader: AlignTypeEnum.LEFT,
        type: ColumnTypeEnum.READONLY
      },
      {
        field: TipoCarburante.GASOLIO,
        header: "Quantità",
        sortable: false,
        disabled: false,
        width: '22%',
        inputOpts: { pattern: '^(([0-9])([0-9]+)?([\,][0-9]{1})?)?$', maxLength: '7' },
        suffix: 'unitaDiMisura',
        alignCellHorizontal: AlignTypeEnum.CENTER,
        alignHeader: AlignTypeEnum.CENTER,
        type: ColumnTypeEnum.INPUT_NUMBER
      }
    ];
  }
}
