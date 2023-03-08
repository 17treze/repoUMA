import { Location } from '@angular/common';
import { FabbisognoDichiarato } from '../../../../../core-uma/models/viewModels/FabbisognoDichiaratoViewModel';
import { Component, OnInit, OnDestroy, ViewChild, Input, EventEmitter, Output } from '@angular/core';
import { ActivatedRoute, Params } from '@angular/router';
import { ConfirmationService, MessageService } from 'primeng/api';
import { Accordion } from 'primeng/accordion';
import { Subscription } from 'rxjs/internal/Subscription';
import { A4gMultiTableColumn, AlignTypeEnum, ColumnTypeEnum, FontTypeEnum } from 'src/app/a4g-common/a4g-multi-table/a4g-multi-table-model';
import { FormGroup, FormControl } from '@angular/forms';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';
import { UMA_MESSAGES } from 'src/app/uma/uma.messages';
import { LavorazioneViewModel } from 'src/app/uma/core-uma/models/viewModels/LavorazioneVIewModel';
import { RaggruppamentoLavorazioneDto } from 'src/app/uma/core-uma/models/dto/RaggruppamentoDto';
import * as _ from 'lodash';
import { switchMap, catchError } from 'rxjs/operators';
import { AmbitoLavorazioneEnum } from 'src/app/uma/core-uma/models/dto/LavorazioneFilterDto';
import { TipoCarburante } from 'src/app/uma/core-uma/models/enums/TipoCarburante.enum';
import { TipologiaLavorazioneEnum } from 'src/app/uma/core-uma/models/enums/TipologiaLavorazione.enum';
import { HttpClientLavorazioniUmaService } from 'src/app/uma/core-uma/services/http-client-lavorazioni-uma.service';
import { RaggruppamentoLavorazioneViewModel } from 'src/app/uma/core-uma/models/viewModels/RaggruppamentoLavorazioneViewModel';
import { forkJoin } from 'rxjs';
import { LavorazioniBuiderService } from 'src/app/uma/core-uma/services/lavorazioni-builder.service';
import { DichiarazioneDto } from 'src/app/uma/core-uma/models/dto/DichiarazioneDto';
import { GestioneDomandaUmaService } from '../../../gestione-domanda-uma.service';
import { RichiestaCarburanteDto } from 'src/app/uma/core-uma/models/dto/RichiestaCarburanteDto';
import { ErrorService } from 'src/app/a4g-common/services/error.service';
@Component({
  selector: 'app-lavorazioni-superficie',
  templateUrl: './lavorazioni-superficie.component.html',
  styleUrls: ['./lavorazioni-superficie.component.scss']
})
export class LavorazioniSuperficieComponent implements OnInit, OnDestroy {
  @Input() inCompilazione: boolean;                         /** se lo stato è in compilazione, è possibile modificare la pagina, altrimenti può essere consultata */
  @Input() richiestaCarburante: RichiestaCarburanteDto;
  @Output() changeFabbisogno = new EventEmitter<boolean>(); /** aggiorna il padre se ci sono modifiche ai fabbisogni */
  @ViewChild('accordion') accordion: Accordion;

  openedAccordion: number;                                  /** indice dell'accordion correntemente aperto */
  quantitaLavorata: Array<number> = [];
  datasourceBase: Array<LavorazioneViewModel>;
  datasourceStraordinaria: Array<LavorazioneViewModel>;
  colsBase: Array<A4gMultiTableColumn>;
  colsStraordinaria: Array<A4gMultiTableColumn>;
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
  saveFabbisognoSubscription: Subscription;

  constructor(
    private activeRoute: ActivatedRoute,
    private messageService: MessageService,
    private confirmationService: ConfirmationService,
    private gestioneDomandaUmaService: GestioneDomandaUmaService,
    private httpClientLavorazioniUmaService: HttpClientLavorazioniUmaService,
    private location: Location,
    private lavorazioniBuiderService: LavorazioniBuiderService,
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
    if (this.saveFabbisognoSubscription) {
      this.saveFabbisognoSubscription.unsubscribe();
    }
  }

  private loadRaggruppamenti() {
    this.routerSubscription = this.activeRoute.params
      .pipe(
        catchError(this.errorService.handleError),
        switchMap((params: Params) => {
          this.idDomanda = params['idDomanda'];
          const getLavorazioneObs$ = this.httpClientLavorazioniUmaService.getLavorazioni(this.idDomanda, this.ambitoLavorazioneEnum.SUPERFICIE);
          const getDichiarazioniObs$ = this.httpClientLavorazioniUmaService.getDichiarazioni(this.idDomanda, this.ambitoLavorazioneEnum.SUPERFICIE);
          return forkJoin([getLavorazioneObs$, getDichiarazioniObs$]);
        }))
      .subscribe(([raggruppamenti, dichiarazioni]: [Array<RaggruppamentoLavorazioneDto>, Array<DichiarazioneDto>]) => {
        this.raggruppamenti = this.lavorazioniBuiderService.raggruppamentoLavorazioneDtoToRaggruppmanetoLavorazioneViewModel(raggruppamenti, dichiarazioni);
        // ordino per indice
        this.raggruppamenti = _.orderBy(this.raggruppamenti, ['indice'], ['asc']);
        // calcolo se visualizzare il check su accordion
        this.calcolaQuantita();
      }, error => this.errorService.showError(error, 'tst-lav-sup'));
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
    this.nomeRaggruppamento = null;
    this.isValidFormTable = true;
    this.datasourceBase = [];
    this.datasourceStraordinaria = [];
    // this.isChangedInputTable = false;
    // this.changeFabbisogno.emit(this.isChangedInputTable);
    this.colsBase = this.defineColumnsByTitle('Lavorazioni Base');
    this.colsStraordinaria = this.defineColumnsByTitle('Lavorazioni Straordinarie');
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
    // setto a valido quando il formato dei valori inseriti negli input della tabella è corretto
    this.isValidFormTable = $event.isValid;
  }

  saveLavorazioni(): boolean {
    // check select raggruppamento
    if (this.raggruppamentoForm.get('raggruppamento').value == null) {
      this.errorService.showErrorWithMessage(UMA_MESSAGES.raggruppamentiNotSelezionati, 'tst-lav-sup');
      return false;
    }
    // check formato input tabella
    if (!this.isValidFormTable) {
      this.errorService.showErrorWithMessage(UMA_MESSAGES.datiNonValidi, 'tst-lav-sup');
      return false;
    }
    // salvataggio
    const raggruppamentoSelezionato: RaggruppamentoLavorazioneViewModel = this.raggruppamentoForm.get('raggruppamento').value;
    const dichiarazioni: Array<DichiarazioneDto> = this.lavorazioniBuiderService.lavorazioniViewModelToDichiarazioniDtoBuilder(raggruppamentoSelezionato.lavorazioni);
    console.log('Salvo dichiarazioni: ', dichiarazioni);
    this.saveFabbisognoSubscription = this.httpClientLavorazioniUmaService.saveFabbisogni(this.idDomanda, dichiarazioni).subscribe(() => {
      this.isChangedInputTable = false;
      this.changeFabbisogno.emit(this.isChangedInputTable);
      this.messageService.add(A4gMessages.getToast('tst-lav-sup', A4gSeverityMessage.success, UMA_MESSAGES.salvataggioOKLavorazioniSuperficie));
      this.loadRaggruppamenti();
    }, error => this.errorService.showError(error, 'tst-lav-sup'));
    return true;
  }

  private splitDatasources(lavorazioneViewArray: Array<LavorazioneViewModel>) {
    const dsStarordinariaTemp: Array<LavorazioneViewModel> = [];
    const dsBaseTemp: Array<LavorazioneViewModel> = [];
    lavorazioneViewArray.forEach((lavorazione: LavorazioneViewModel) => {
      if (lavorazione.tipo === TipologiaLavorazioneEnum.BASE) { // Datasource Base
        dsBaseTemp.push(lavorazione);
      } else {                                                  // Datasource Straordinaria
        dsStarordinariaTemp.push(lavorazione);
      }
    });
    // Ordinamento per indice 1.1, 1.2, ecc.
    this.datasourceBase = _.sortBy(dsBaseTemp, ['indice']);
    this.datasourceStraordinaria = _.sortBy(dsStarordinariaTemp, ['indice']);
  }

  onClickIcon($event: { element: any, col: A4gMultiTableColumn }) {
    if ($event.col.field === TipoCarburante.GASOLIO || $event.col.field === TipoCarburante.BENZINA) {
      const lavorazione = $event.element as LavorazioneViewModel;
      this.updateTable(lavorazione, $event.col);
    }
  }

  private updateTable(lavorazione: LavorazioneViewModel, col: A4gMultiTableColumn) {
    let lavorazioneTrovata = this.datasourceBase.filter((element: LavorazioneViewModel) => element.id === lavorazione.id)[0];
    if (lavorazioneTrovata == null) {
      lavorazioneTrovata = this.datasourceStraordinaria.filter((element: LavorazioneViewModel) => element.id === lavorazione.id)[0];
    }
    // calcolo altro campo superficie diverso da quello selezionato
    const fieldToCheck: keyof typeof TipoCarburante = col.field === TipoCarburante.GASOLIO ? TipoCarburante.BENZINA : TipoCarburante.GASOLIO;

    // Primo click    -> set superficieMassima nel campo selezionato e resetta (con '' per consentire il salvataggio con fabbisogno vuoto) l'altro campo; 
    // Secondo click  -> reset il campo selezionato e l'altro resta invariato
    lavorazioneTrovata[col.field] = lavorazioneTrovata[col.field] != lavorazioneTrovata.superficieMassima ? lavorazione.superficieMassima : '';
    lavorazioneTrovata[fieldToCheck] = '';
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
    return [{
      field: "descrizioneCompleta",
      header: title,
      sortable: false,
      font: FontTypeEnum.BOLD,
      width: '46%',
      alignCellHorizontal: AlignTypeEnum.LEFT,
      alignHeader: AlignTypeEnum.LEFT,
      type: ColumnTypeEnum.READONLY
    }, {
      field: "superficieMassima",
      suffix: 'unitaDiMisura',
      header: "Superficie massima",
      sortable: false,
      width: '16%',
      alignCellHorizontal: AlignTypeEnum.RIGHT,
      alignHeader: AlignTypeEnum.CENTER,
      type: ColumnTypeEnum.READONLY
    }, {
      field: TipoCarburante.GASOLIO,
      suffix: 'unitaDiMisura',
      header: "Sup. gasolio",
      sortable: false,
      disabled: this.fabbisognoDichiarato != null && !this.fabbisognoDichiarato.gasolio,
      inputOpts: { pattern: '^([0-9])+$', maxLength: '7', upperLimitField: 'superficieMassima' },
      icon: 'pi pi-chevron-circle-right',
      width: '19%',
      alignCellHorizontal: AlignTypeEnum.CENTER,
      alignHeader: AlignTypeEnum.CENTER,
      type: ColumnTypeEnum.INPUT_NUMBER
    },
    {
      field: TipoCarburante.BENZINA,
      suffix: 'unitaDiMisura',
      header: "Sup. benzina",
      sortable: false,
      disabled: this.fabbisognoDichiarato != null && !this.fabbisognoDichiarato.benzina,
      inputOpts: { pattern: '^([0-9])+$', maxLength: '7', upperLimitField: 'superficieMassima' },
      icon: 'pi pi-chevron-circle-right',
      width: '19%',
      alignCellHorizontal: AlignTypeEnum.CENTER,
      alignHeader: AlignTypeEnum.CENTER,
      type: ColumnTypeEnum.INPUT_NUMBER
    }
    ];
  }

}
