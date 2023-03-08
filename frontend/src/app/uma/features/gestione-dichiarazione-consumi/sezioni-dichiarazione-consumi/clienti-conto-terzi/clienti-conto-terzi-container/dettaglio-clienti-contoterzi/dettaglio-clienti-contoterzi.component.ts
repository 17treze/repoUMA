import { FormatConverterService } from './../../../../../../shared-uma/services/format-converter.service';
import { ClienteConsumiDto } from './../../../../../../core-uma/models/dto/ClienteConsumiDto';
import { ActivatedRoute, Params } from '@angular/router';
import { IndiceUmaService } from 'src/app/uma/core-uma/services/indice-uma.service';
import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { ConfirmationService, MessageService } from 'primeng/api';
import { UMA_MESSAGES } from 'src/app/uma/uma.messages';
import { Location } from '@angular/common';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';
import { Accordion } from 'primeng/accordion';
import { RaggruppamentoLavorazioneDto } from 'src/app/uma/core-uma/models/dto/RaggruppamentoDto';
import { LavorazioneViewModel } from 'src/app/uma/core-uma/models/viewModels/LavorazioneVIewModel';
import { A4gMultiTableColumn, AlignTypeEnum, ColumnTypeEnum, FontTypeEnum } from 'src/app/a4g-common/a4g-multi-table/a4g-multi-table-model';
import { TipoCarburante } from 'src/app/uma/core-uma/models/enums/TipoCarburante.enum';
import { TipologiaLavorazioneEnum } from 'src/app/uma/core-uma/models/enums/TipologiaLavorazione.enum';
import * as _ from 'lodash';
import { EMPTY, forkJoin, Subscription } from 'rxjs';
import { catchError, switchMap } from 'rxjs/operators';
import { HttpClientClienteUmaService } from 'src/app/uma/core-uma/services/http-client-cliente-uma.service';
import { RaggruppamentoLavorazioneViewModel } from 'src/app/uma/core-uma/models/viewModels/RaggruppamentoLavorazioneViewModel';
import { LavorazioniBuiderService } from 'src/app/uma/core-uma/services/lavorazioni-builder.service';
import { DichiarazioneDto } from 'src/app/uma/core-uma/models/dto/DichiarazioneDto';
import { AmbitoLavorazioneEnum } from 'src/app/uma/core-uma/models/dto/LavorazioneFilterDto';
import { ErrorService } from 'src/app/a4g-common/services/error.service';
import { ErrorDTO } from 'src/app/a4g-common/interfaces/error.model';
import { FabbisognoDto } from 'src/app/uma/core-uma/models/dto/FabbisognoDto';
import { HttpClientAnagraficaService } from 'src/app/uma/shared-uma/services/http-client-anagrafica.service';
import { DetenzioniAgsDto, FascicoloAgsDto, TipoDetenzioneAgs } from 'src/app/a4g-common/classi/FascicoloAgsDto';
@Component({
  selector: 'app-dettaglio-clienti-contoterzi',
  templateUrl: './dettaglio-clienti-contoterzi.component.html',
  styleUrls: ['./dettaglio-clienti-contoterzi.component.scss']
})
export class DettaglioClientiContoterziComponent implements OnInit, OnDestroy {
  @ViewChild('accordion') accordion: Accordion;
  openedAccordion: number;                  /** indice dell'accordion correntemente aperto */
  quantitaLavorata: Array<number>;

  READONLY_MODE: boolean;                   /** se lo stato è readonly, è possibile solo consultare la pagina */
  raggruppamentoForm: FormGroup;
  raggruppamenti: Array<RaggruppamentoLavorazioneViewModel>;
  nomeRaggruppamento: string;
  ambitoLavorazioneEnum = AmbitoLavorazioneEnum;

  datasourceBase: Array<LavorazioneViewModel>;
  datasourceStraordinaria: Array<LavorazioneViewModel>;
  colsBase: Array<A4gMultiTableColumn>;
  colsStraordinaria: Array<A4gMultiTableColumn>;

  isChangedInputTable: boolean;
  isValidFormTable: boolean;

  idDichiarazione: string;
  cliente: ClienteConsumiDto;
  fabbisogniRichiestaCliente: Array<DichiarazioneDto>;
  sportelloCliente: DetenzioniAgsDto;

  // Subscriptions
  routerSubscription: Subscription;
  saveLavorazioniSubscription: Subscription;

  constructor(
    private confirmationService: ConfirmationService,
    private location: Location,
    private messageService: MessageService,
    private indiceUmaService: IndiceUmaService,
    private httpClientClienteUmaService: HttpClientClienteUmaService,
    private activeRoute: ActivatedRoute,
    private lavorazioniBuiderService: LavorazioniBuiderService,
    private errorService: ErrorService,
    private formatConverterService: FormatConverterService,
    private httpClientAnagraficaService: HttpClientAnagraficaService
  ) { }

  ngOnInit() {
    this.raggruppamentoForm = new FormGroup({
      raggruppamento: new FormControl()
    });

    this.routerSubscription = this.activeRoute.params
      .pipe(
        switchMap((params: Params) => {
          this.idDichiarazione = params['id'];
          const getClienteObs$ = this.httpClientClienteUmaService.getCliente(params['id'], params['idCliente']);
          return getClienteObs$;
        }),
        catchError((err: ErrorDTO) => {
          this.errorService.showError(err);
          return EMPTY;
        }),
        switchMap((cliente: ClienteConsumiDto) => {
          this.cliente = cliente;
          const getFascicoloAgs$ = this.httpClientAnagraficaService.getFascicoloAgs(this.cliente.cuaa);
          const getLavorazioneObs$ = this.httpClientClienteUmaService.getLavorazioniClientiContoTerzi(this.idDichiarazione, this.cliente.id);
          const getFabbisogniRichiestaClienteObs$ = this.httpClientClienteUmaService.getFabbisogniRichiestaCliente(this.idDichiarazione, this.cliente.id);
          const getFabbisogniContoTerzistaObs$ = this.httpClientClienteUmaService.getFabbisogniContoTerzista(this.idDichiarazione, this.cliente.id);
          return forkJoin([getFascicoloAgs$, getLavorazioneObs$, getFabbisogniRichiestaClienteObs$, getFabbisogniContoTerzistaObs$]);
        }))
      .subscribe(([fascicoloAgs, raggruppamenti, fabbisogniRichiestaCliente, fabbisogniDichiaratiContoTerzista]: [FascicoloAgsDto, Array<RaggruppamentoLavorazioneDto>, Array<DichiarazioneDto>, Array<DichiarazioneDto>]) => {
        this.initVariables();
        this.sportelloCliente = this.getDetenzioneFromFascicoloAgs(fascicoloAgs);

        this.fabbisogniRichiestaCliente = fabbisogniRichiestaCliente;
        this.raggruppamenti = this.lavorazioniBuiderService.raggruppamentoLavorazioneDtoToRaggruppmanetoLavorazioneViewModel(raggruppamenti, fabbisogniDichiaratiContoTerzista);
        // ordino per indice
        this.raggruppamenti = _.orderBy(this.raggruppamenti, ['indice'], ['asc']);
        // calcolo se visualizzare il check su accordion
        this.calcolaQuantita();
      }, error => this.errorService.showError(error));
  }

  ngOnDestroy() {
    if (this.routerSubscription) {
      this.routerSubscription.unsubscribe();
    }
    if (this.saveLavorazioniSubscription) {
      this.saveLavorazioniSubscription.unsubscribe();
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
      this.caricaDatiAccordionAperta($event);
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

  onChangeForm($event: { isChanged: boolean, isValid: boolean }) {
    // setto a changed, quando scatta l'onChange su un qualsiasi input della tabella
    this.isChangedInputTable = $event.isChanged;
    // setto a valida quando il formato dei valori inseriti negli input della tabella è corretto
    this.isValidFormTable = $event.isValid;
  }

  saveLavorazioni(): boolean {
    // check select raggruppamento
    if (this.raggruppamentoForm.get('raggruppamento').value == null) {
      this.errorService.showErrorWithMessage(UMA_MESSAGES.raggruppamentiNotSelezionati)
      return false;
    }
    // check formato input tabella
    if (!this.isValidFormTable) {
      this.errorService.showErrorWithMessage(UMA_MESSAGES.datiNonValidi)
      return false;
    }

    // check fabbisogni contoterzista + fabbisogni cliente <= superficie massima contoterzista   
    const raggruppamentoSelezionato: RaggruppamentoLavorazioneViewModel = this.raggruppamentoForm.get('raggruppamento').value;
    const dichiarazioniConteTerzista: Array<DichiarazioneDto> = this.lavorazioniBuiderService.lavorazioniViewModelToDichiarazioniDtoBuilder(raggruppamentoSelezionato.lavorazioni);
    let limiteSuperato = false;
    let lavorazioniIrregolari: Array<LavorazioneViewModel> = [];
    dichiarazioniConteTerzista.forEach((dichiarazioneContoTerzista: DichiarazioneDto) => {
      this.fabbisogniRichiestaCliente.forEach((dichiarazioneCliente: DichiarazioneDto) => {
        let totaleDichiaratoDalContoTerzista: number = 0;
        let totaleDichiaratoDalCliente: number = 0;
        if (dichiarazioneContoTerzista.lavorazioneId === dichiarazioneCliente.lavorazioneId) {
          totaleDichiaratoDalContoTerzista = dichiarazioneContoTerzista.fabbisogni.reduce((totale: number, currentValue: FabbisognoDto) => totale + this.formatConverterService.toNumber(currentValue.quantita), 0);
          totaleDichiaratoDalCliente = dichiarazioneCliente.fabbisogni.reduce((totale: number, currentValue: FabbisognoDto) => totale + this.formatConverterService.toNumber(currentValue.quantita), 0);
          console.log('totaleDichiaratoDalContoTerzista', totaleDichiaratoDalContoTerzista);
          console.log('totaleDichiaratoDalCliente', totaleDichiaratoDalCliente);
          console.log('superficieMassima', raggruppamentoSelezionato.superficieMassima);
          // Il controllo vale solo per le lavorazioni in cui il contoterzista ha inserito un fabbisogno (totaleDichiaratoDalContoTerzista > 0)
          // e tale fabbisogno, sommato a quello inserito dal cliente nella sua richiesta di carburante per la stessa lavorazione non deve superare la superficie massima
          if (totaleDichiaratoDalContoTerzista > 0 && totaleDichiaratoDalContoTerzista + totaleDichiaratoDalCliente > raggruppamentoSelezionato.superficieMassima) {
            limiteSuperato = true;
            lavorazioniIrregolari.push(...raggruppamentoSelezionato.lavorazioni.filter((lav: LavorazioneViewModel) => lav.id === dichiarazioneContoTerzista.lavorazioneId));
          }
        }
      });
    });
    if (limiteSuperato) {
      this.showDialogSuperficieMax(dichiarazioniConteTerzista, lavorazioniIrregolari);
      return false;
    }
    this.persistLavorazioni(dichiarazioniConteTerzista);
    return true;
  }

  goBack() {
    if (this.isChangedInputTable) {
      this.confirmationService.confirm({
        message: UMA_MESSAGES.datiNonSalvati,
        accept: () => {
          this.location.back();
        },
        reject: () => { },
        key: "conto-terzi-dettaglio-dialog"
      });
    } else {
      this.location.back();
    }
  }

  private persistLavorazioni(dichiarazioniConteTerzista: Array<DichiarazioneDto>) {
    console.log('Salvo dichiarazioni contoterzista: ', dichiarazioniConteTerzista);
    this.saveLavorazioniSubscription = this.httpClientClienteUmaService.saveLavorazioniClientiContoTerzi(this.idDichiarazione, this.cliente.id, dichiarazioniConteTerzista).subscribe(() => {
      this.isChangedInputTable = false;
      this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.success, UMA_MESSAGES.salvataggioOKConsumiLavorazioni));
      this.calcolaQuantita();
    }, error => this.errorService.showError(error));
  }

  private initVariables() {
    this.READONLY_MODE = this.indiceUmaService.READONLY_MODE_DICHIARAZIONE || (localStorage.getItem('UMA_RO_DICH') == 'true' ? true : false);
    this.isValidFormTable = true;
    this.isChangedInputTable = false;
    this.nomeRaggruppamento = null;
    this.quantitaLavorata = [];
    this.datasourceBase = [];
    this.datasourceStraordinaria = [];
    this.colsBase = this.defineColumnsByTitle('Lavorazioni Base');
    this.colsStraordinaria = this.defineColumnsByTitle('Lavorazioni Straordinarie');
    this.raggruppamentoForm.controls['raggruppamento'].setValue(null);
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

  private caricaDatiAccordionAperta($event: any) {
    const raggruppamentoSelezionato: RaggruppamentoLavorazioneViewModel = this.raggruppamenti[$event.index];
    this.nomeRaggruppamento = raggruppamentoSelezionato.indice + ' ' + raggruppamentoSelezionato.nome;
    this.splitDatasources(raggruppamentoSelezionato.lavorazioni);
    this.raggruppamentoForm.controls['raggruppamento'].setValue(raggruppamentoSelezionato);
    // resetto i controlli sulla tabella
    this.isChangedInputTable = false;
  }

  private splitDatasources(lavorazioneViewArray: Array<LavorazioneViewModel>) {
    const dsStarordinariaTemp: Array<LavorazioneViewModel> = [];
    const dsBaseTemp: Array<LavorazioneViewModel> = [];
    lavorazioneViewArray.forEach((lavorazione: LavorazioneViewModel) => {
      if (lavorazione.tipo === TipologiaLavorazioneEnum.BASE) {
        dsBaseTemp.push(lavorazione);
      } else { // Straordinaria
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

    // Primo click -> set superficieMassima nel campo selezionato e resetta l'altro campo (con '' per consentire il salvataggio con fabbisogno vuoto); 
    // Secondo click -> reset il campo selezionato e l'altro?
    lavorazioneTrovata[col.field] = lavorazioneTrovata[col.field] != lavorazioneTrovata.superficieMassima ? lavorazione.superficieMassima : '';
    lavorazioneTrovata[fieldToCheck] = '';
  }

  private getDetenzioneFromFascicoloAgs(fascicoloAgs: FascicoloAgsDto): DetenzioniAgsDto {
    if (!fascicoloAgs || !fascicoloAgs.detenzioni || !fascicoloAgs.detenzioni.length) {
      return null;
    }
    if (fascicoloAgs && fascicoloAgs.detenzioni && fascicoloAgs.detenzioni.length && fascicoloAgs.detenzioni.length === 1) {
      return fascicoloAgs.detenzioni[0];
    }
    // se non ha il mandato e ha 0 deleghe, eccezione.
    // se dovesse per qualunque motivo avere 2 o più mandati, eccezione.
    // se non ha il mandato e ha 2 o più deleghe, eccezione.  
    // se non ha il mandato e ha 1 delega prende l'unica delega.
    // se ha il mandato e ha n deleghe oppure non ne ha, prende l'unico mandato mandato
    const deleghe = fascicoloAgs.detenzioni.filter((detenzione: DetenzioniAgsDto) => detenzione.tipoDetenzione === TipoDetenzioneAgs.DELEGA);
    const mandati = fascicoloAgs.detenzioni.filter((detenzione: DetenzioniAgsDto) => detenzione.tipoDetenzione === TipoDetenzioneAgs.MANDATO);
    if (!mandati && !mandati.length && !deleghe && !deleghe.length) {
      this.errorService.showErrorWithMessage('Il cliente non dispone di un mandato nè di una delega - Non è possibile reperire lo sportello!');
      return null;
    }
    if (!mandati && !mandati.length && deleghe && deleghe.length > 1) {
      this.errorService.showErrorWithMessage('Il cliente dispone di più deleghe - Non è possibile reperire lo sportello!');
      return null;
    }
    if (!mandati && !mandati.length && deleghe && deleghe.length === 1) {
      return deleghe[0];
    }
    if (mandati && mandati.length > 1) {
      this.errorService.showErrorWithMessage('Il cliente dispone di piu mandati - Non è possibile reperire lo sportello!');
      return null;
    }
    if (mandati && mandati.length === 1) {
      return mandati[0];
    }
  }

  private showDialog($event: any) {
    this.confirmationService.confirm({
      message: UMA_MESSAGES.datiNonSalvati,
      accept: () => {
        this.openedAccordion = $event.index;
        this.caricaDatiAccordionAperta($event);
        this.openAccordionByIndex(this.openedAccordion);
      },
      reject: () => {
        this.openAccordionByIndex(this.openedAccordion);
      },
      key: "conto-terzi-dettaglio-dialog"
    });
  }

  private showDialogSuperficieMax(dichiarazioniConteTerzista: Array<DichiarazioneDto>, lav: Array<LavorazioneViewModel>) {
    const codiciLavorazioniStringified: string = lav.map(l=> l.descrizioneCompleta.split(' ')[0]).join(', '); // estraggo solo il codice - es. 10.1
    const singolareOPlurale = lav.length > 1 ? 'i' : 'e';
    this.confirmationService.confirm({
      header: `Lavorazion${singolareOPlurale}: ` + codiciLavorazioniStringified,
      message: UMA_MESSAGES.superficieDichiarataGtSupMax + this.sportelloCliente.sportello + ' ' + this.sportelloCliente.identificativoSportello + UMA_MESSAGES.superficieDichiarataGtSupMax2,
      accept: () => {
        this.persistLavorazioni(dichiarazioniConteTerzista);
      },
      reject: () => { },
      key: "conto-terzi-superficie-max-alert"
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
      width: '18%',
      alignCellHorizontal: AlignTypeEnum.RIGHT,
      alignHeader: AlignTypeEnum.CENTER,
      type: ColumnTypeEnum.READONLY
    }, {
      field: TipoCarburante.GASOLIO,
      suffix: 'unitaDiMisura',
      header: "Sup. gasolio",
      sortable: false,
      disabled: !this.cliente.gasolio,
      inputOpts: { pattern: '^([0-9])+$', maxLength: '7', upperLimitField: 'superficieMassima' },
      icon: 'pi pi-chevron-circle-right',
      width: '18%',
      alignCellHorizontal: AlignTypeEnum.CENTER,
      alignHeader: AlignTypeEnum.CENTER,
      type: ColumnTypeEnum.INPUT_NUMBER
    },
    {
      field: TipoCarburante.BENZINA,
      suffix: 'unitaDiMisura',
      header: "Sup. benzina",
      sortable: false,
      disabled: !this.cliente.benzina,
      inputOpts: { pattern: '^([0-9])+$', maxLength: '7', upperLimitField: 'superficieMassima' },
      icon: 'pi pi-chevron-circle-right',
      width: '18%',
      alignCellHorizontal: AlignTypeEnum.CENTER,
      alignHeader: AlignTypeEnum.CENTER,
      type: ColumnTypeEnum.INPUT_NUMBER
    }
    ];
  }

}
