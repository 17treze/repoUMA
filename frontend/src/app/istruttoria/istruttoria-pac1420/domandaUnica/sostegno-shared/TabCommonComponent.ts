import { switchMap, tap, takeUntil, catchError } from "rxjs/operators";
import { iif, defer, Subject } from "rxjs";

import { Input, SimpleChanges, ViewChild, OnDestroy, OnInit, ChangeDetectorRef, Directive } from "@angular/core";
import { ActivatedRoute, Router } from "@angular/router";
import { MessageService, MenuItem } from "primeng/api";
import { from, Observable, EMPTY, of } from "rxjs";
import { map, mergeMap } from "rxjs/operators";
import { Paginazione, SortDirection } from "src/app/a4g-common/utility/paginazione";
import { Labels } from "src/app/app.labels";
import { IstruttoriaDomandaUnicaFilter } from "src/app/istruttoria/istruttoria-pac1420/domandaUnica/classi/IstruttoriaDomandaUnicaFilter";
import { StatoIstruttoriaEnum } from "src/app/istruttoria/istruttoria-pac1420/domandaUnica/cruscotto-sostegno/StatoIstruttoriaEnum";
import { ElencoDomandeService } from 'src/app/istruttoria/istruttoria-pac1420/domandaUnica/services/elenco-domande.service';
import { A4gPagedTableComponent } from "src/app/a4g-common/a4g-paged-table/a4g-paged-table.component";
import { TipoProcesso } from "src/app/istruttoria/istruttoria-antimafia/dto/TipoProcesso";
import { AutocompleteElencoDomandeParams } from "../domain/autocomplete-elenco-domande";
import { IstruttoriaService } from "../istruttoria.service";
import { Costanti } from "../Costanti";
import { SostegnoDu } from "../classi/SostegnoDu";
import { TipoIstruttoriaEnum } from "../classi/TipoIstruttoriaEnum";
import { ProcessRepeatingServiceCall } from "./repeating-service-call";
import { LoaderService } from "src/app/loader.service";
import { IstruttoriaPagina } from "../classi/IstruttoriaPagina";
import { FlowIstruttoriaDUEnum } from "../classi/FlowIstruttoriaDUEnum";
import { CONF_PROCESSI } from "./conf-processi";
import { ActionMenuComponent } from "src/app/a4g-common/action-menu.component";
import { IstruttoriaSostegnoViewModel } from "../classi/IstruttoriaSostegnoViewModel";
import * as FileSaver from 'file-saver';
import { A4gMessages, A4gSeverityMessage } from "src/app/a4g-common/a4g-messages";
import { IstruttoriaFiltroRicercaDomande } from "../istruttoria-shared-filter/istruttoria-filtro-ricerca-domande";
import { ElencoLiquidazione } from "../classi/IstruttoriaDomandaUnica";

@Directive()
export abstract class TabCommonComponent implements OnInit, OnDestroy {
  public itemBadgesCount: { [k: string]: any } = {};
  public menu2: Array<MenuItem>;
  public annoCampagna: number = Number(this.activatedRoute.snapshot.paramMap.get('annoCampagna'));
  public istruttorieSelezionate: IstruttoriaSostegnoViewModel[] = [];  // domande attualmente selezionate
  public isSelectedAll: boolean = false;
  public cols: any[];
  public paginaIstruttorie: IstruttoriaPagina;
  protected componentDestroyed$: Subject<boolean> = new Subject();
  public processiInCorsoLabel: string = Labels.NESSUN_PROCESSO_DI_CONTROLLO;
  public labels = Labels;
  public autoCompleteParams: AutocompleteElencoDomandeParams;
  public processiDaControllare: TipoProcesso[];
  public popupVisibile: boolean;

  public elementiPerPagina = 10;
  public istruttorieTrovate: IstruttoriaSostegnoViewModel[] = [];     // contenuto tabella
  public elementiTotali = 0;
  sortBy: string;
  sortDirection: SortDirection;
  public currentMenuAction: ActionMenuComponent;
  public indexSelectedFromMenu: number;
  public istruttoriaSelezionata: IstruttoriaSostegnoViewModel;
  public items: MenuItem[];

  @ViewChild('table') tabella: A4gPagedTableComponent;
  public istruttoriaDomandaUnicaFilter: IstruttoriaDomandaUnicaFilter = new IstruttoriaDomandaUnicaFilter();

  // resetta pagina
  resetPage = () => {
    this.elementiTotali = 0;
    this.tabella.reset();
  }

  constructor(
    protected activatedRoute: ActivatedRoute,
    protected istruttoriaService: IstruttoriaService,
    protected elencoDomandeService: ElencoDomandeService,
    protected router: Router,
    protected messageService: MessageService,
    protected loaderService: LoaderService,
    public statoIstruttoria: StatoIstruttoriaEnum,
    protected repeatingServiceCall: ProcessRepeatingServiceCall,
    public sostegno: SostegnoDu,
    protected tipoIstruttoria: TipoIstruttoriaEnum,
    protected changeDetectorRef: ChangeDetectorRef,
    public flowIstruttoria: FlowIstruttoriaDUEnum) {

    if (!flowIstruttoria) {
      throw new Error("flowIstruttoria null");
    }
    this.loaderService.setTimeout(480000); //otto minuti
    this.initDefaultFilterValuesIstruttorie();
    this.cols = [
      { field: 'domandaUnicaModel.cuaaIntestatario', header: Labels.cuaaSigla },
      { field: 'domandaUnicaModel.numeroDomanda', header: Labels.NUMERO_DOMANDA },
      { field: 'domandaUnicaModel.ragioneSociale', header: Labels.descrizioneImpresa }
    ];
    this.paginaIstruttorie = new IstruttoriaPagina();
    this.processiDaControllare = this.getProcessiDaControllare();
  }

  ngOnInit(): void {
  }

  public openPopupDatiAggregati() {
    this.popupVisibile = true;
  }

  public closePopupDatiAggregati() {
    this.popupVisibile = false;
  }


  public loadData(event): void {
    if (event.sortField) {
      this.sortDirection = event.sortOrder === 1 ? SortDirection.ASC : SortDirection.DESC;
      this.sortBy = event.sortField;
    }
    this.getElencoIstruttoria(this.istruttoriaDomandaUnicaFilter, event.first);
  }

  abstract getVerbale(idIstruttoria: number);

  overrideGetVerbale(idIstruttoria: number): void {
    this.istruttoriaService.getDocumentoDomanda(idIstruttoria).subscribe(x => {
      FileSaver.saveAs(x, 'VerbaleIstruttoria_'.concat(this.istruttoriaSelezionata.numeroDomanda.toString()).concat('.pdf'));
    }, error => {
      this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.IMPOSSILE_GENERARE_FILE));
    });
  }

  protected menuActionPagamentoAutorizzato(): Array<any> {
    return [{
      label: Labels.azioni.toUpperCase(),
      items: [{
        label: Labels.DETTAGLIO_DOMANDA,
        command: () => {
          this.apriDettaglio(this.istruttoriaSelezionata);
          this.currentMenuAction.reset = true;
        }
      }, {
        label: Labels.STAMPA_VERBALE_ISTRUTTORIA,
        command: () => {
          this.getVerbale(this.istruttoriaSelezionata.idDomanda);
          this.currentMenuAction.reset = true;
        }
      }]
    }];
  }

  public onDropdownMenuOpen(
    actionMenu: ActionMenuComponent,
    domanda: IstruttoriaSostegnoViewModel,
    indexSelectedFromMenu: number) {
    this.indexSelectedFromMenu = indexSelectedFromMenu;
    this.istruttoriaSelezionata = domanda;
    this.currentMenuAction = actionMenu;
  }

  protected getElencoIstruttoria(istruttoriaDomandaUnicaFilter: IstruttoriaDomandaUnicaFilter, start: number = 0): void {
    istruttoriaDomandaUnicaFilter.campagna = Number(this.activatedRoute.snapshot.paramMap.get('annoCampagna'));
    istruttoriaDomandaUnicaFilter.sostegno = this.sostegno;
    istruttoriaDomandaUnicaFilter.tipo = this.tipoIstruttoria;
    istruttoriaDomandaUnicaFilter.stato = this.statoIstruttoria.toString();

    let sortField: string = this.sortBy || 'id';
    let paginazione: Paginazione = Paginazione.of(
      Math.round(start / this.elementiPerPagina), this.elementiPerPagina, sortField, this.sortDirection || SortDirection.ASC);
    this.istruttoriaService.ricercaIstruttorieDU(istruttoriaDomandaUnicaFilter, paginazione)
      .pipe(takeUntil(this.componentDestroyed$))
      .subscribe(istruttoriaPagina => {
        this.istruttorieTrovate = [];
        this.paginaIstruttorie = istruttoriaPagina;
        istruttoriaPagina.risultati.forEach(istruttoria => {
          let newIstruttoriaResult: IstruttoriaSostegnoViewModel = new IstruttoriaSostegnoViewModel();
          newIstruttoriaResult.idDomanda = istruttoria.id;
          newIstruttoriaResult.codiceElenco = istruttoria.elencoLiquidazione ? istruttoria.elencoLiquidazione.codElenco : null;
          newIstruttoriaResult.elencoLiquidazione = istruttoria.elencoLiquidazione;
          newIstruttoriaResult.cuaaIntestatario = istruttoria.domanda.cuaaIntestatario;
          newIstruttoriaResult.idDomandaIntegrativa = null;
          newIstruttoriaResult.numeroDomanda = istruttoria.domanda.numeroDomanda;
          newIstruttoriaResult.ragioneSociale = istruttoria.domanda.ragioneSociale;
          newIstruttoriaResult.isBloccata = istruttoria.isBloccata;
          newIstruttoriaResult.dataUltimoCalcolo = istruttoria.dataUltimoCalcolo;
          newIstruttoriaResult.isErroreCalcolo = istruttoria.isErroreCalcolo;
          this.istruttorieTrovate.push(newIstruttoriaResult);
        });
        this.elementiTotali = istruttoriaPagina.count;
        this.changeDetectorRef.markForCheck();
        this.changeDetectorRef.detectChanges();
      });
  }

  public downloadCsv() {
    this.getIstruttorieSelezionate().pipe(
      switchMap(elencoIdRes => {
        return this.istruttoriaService.downloadCsvIstruttorie(elencoIdRes);
      })
    ).subscribe(csv => {
      let data = new Date();
      let month = (data.getMonth() < 10) ? ("0" + (data.getMonth() + 1).toString()) : (data.getMonth()).toString();
      let day = (data.getDate() < 10) ? ("0" + data.getDate().toString()) : data.getDate().toString();
      let hours = (data.getHours() < 10) ? ("0" + data.getHours().toString()) : data.getHours().toString();
      let mins = (data.getMinutes() < 10) ? ("0" + data.getMinutes().toString()) : data.getMinutes().toString();
      let sec = (data.getSeconds() < 10) ? ("0" + data.getSeconds().toString()) : data.getSeconds().toString();
      // IDU-ANT-12 - Il file avrà il seguente nome SOSTEGNO_$SOSTEGNO_$CAMPAGNA_$TIPO_PAGAMENTO_$SOTTOSTATO_YYYYMMDD_HH24miss.csv mi = minuti ss = secondi
      const fileName = "SOSTEGNO_" 
        + this.sostegno + "_" 
        + this.annoCampagna + "_"
        + this.tipoIstruttoria + "_"
        + this.statoIstruttoria + "_" 
        + data.getFullYear().toString() 
        + month
        + day + "_" 
        + hours
        + mins
        + sec
        + ".csv";
      FileSaver.saveAs(csv, fileName);
    }, err => {
      this.messageService.add(A4gMessages.getToast("generic", A4gSeverityMessage.error, err.message));
    });
  }

  public apriDettaglio(domanda: IstruttoriaSostegnoViewModel) {
    this.router.navigate([Costanti.dettaglioIstruttoria.replace(':idIstruttoria', String(domanda.idDomanda))],
      { relativeTo: this.activatedRoute.parent.parent.parent });
  }

  protected initDefaultFilterValuesIstruttorie() {
    this.istruttoriaDomandaUnicaFilter = new IstruttoriaDomandaUnicaFilter();
    this.istruttoriaDomandaUnicaFilter.sostegno = this.sostegno;
    this.istruttoriaDomandaUnicaFilter.campagna = Number(this.activatedRoute.snapshot.paramMap.get('annoCampagna'));
    this.istruttoriaDomandaUnicaFilter.tipo = this.tipoIstruttoria;
    this.istruttoriaDomandaUnicaFilter.stato = this.statoIstruttoria;

    this.autoCompleteParams = new AutocompleteElencoDomandeParams();
    this.autoCompleteParams.statoSostegno = this.statoIstruttoria;
    this.autoCompleteParams.sostegno = this.sostegno;
    this.autoCompleteParams.annoCampagna = this.activatedRoute.snapshot.paramMap.get('annoCampagna');
    this.autoCompleteParams.tipo = this.tipoIstruttoria;
  }

  protected setFilterValuesIstruttorie(filtroIstruttoria: IstruttoriaFiltroRicercaDomande) {
    this.istruttoriaDomandaUnicaFilter.cuaa = filtroIstruttoria.cuaa;
    this.istruttoriaDomandaUnicaFilter.numeroDomanda = filtroIstruttoria.numero_domanda;
    this.istruttoriaDomandaUnicaFilter.ragioneSociale = filtroIstruttoria.denominazione;
    this.istruttoriaDomandaUnicaFilter.interventi = filtroIstruttoria.interventi;
    this.istruttoriaDomandaUnicaFilter.istruttoriaBloccata = filtroIstruttoria.bloccataBool;
    this.istruttoriaDomandaUnicaFilter.erroreCalcolo = filtroIstruttoria.erroreCalcolo;
    this.istruttoriaDomandaUnicaFilter.campione = filtroIstruttoria.campione;
    this.istruttoriaDomandaUnicaFilter.giovane = filtroIstruttoria.giovane;
    this.istruttoriaDomandaUnicaFilter.pascoli = filtroIstruttoria.pascoli;
    this.istruttoriaDomandaUnicaFilter.riservaNazionale = filtroIstruttoria.riservaNazionale;
    this.istruttoriaDomandaUnicaFilter.anomalie = filtroIstruttoria.anomalie;
    this.istruttoriaDomandaUnicaFilter.codiciAnomalieError = filtroIstruttoria.anomalieERROR;
    this.istruttoriaDomandaUnicaFilter.codiciAnomalieInfo = filtroIstruttoria.anomalieINFO;
    this.istruttoriaDomandaUnicaFilter.codiciAnomalieWarning = filtroIstruttoria.anomalieWARNING;
    this.istruttoriaDomandaUnicaFilter.integrazione = filtroIstruttoria.integrazione;
    this.istruttoriaDomandaUnicaFilter.clean();
  }

  protected getIstruttorieSelezionate(): Observable<number[]> {
    if (this.isSelectedAll) {
      let paginazione: Paginazione = Paginazione.of(0, this.elementiTotali, 'id', 'ASC');
      return this.istruttoriaService
        .ricercaIstruttorieDU(this.istruttoriaDomandaUnicaFilter, paginazione)
        .pipe(map(
          page => page.risultati.map(x => x.id)));
    } else {
      let copyIstruttorie: Array<number> = this.istruttorieSelezionate.map(x => x.idDomanda);
      if (copyIstruttorie.length > 0) {
        return of(copyIstruttorie);
      } else {
        this.messageService.add(A4gMessages.getToast(
          'tst', A4gSeverityMessage.warn, A4gMessages.calcoloAccoppiatiWarningNumeroDomande));
        return EMPTY;
      }
    }
  }

  protected eseguiProcessoIstruttoria(
    tipoProcesso: TipoProcesso,
    checkTipiProcessoInEsecuzione: TipoProcesso[],
    annoCampagna?: number, sostegno?: SostegnoDu) {
    const avvia$ = this.getIstruttorieSelezionate().pipe(
      switchMap(elencoId => {
        console.log(elencoId);
        return this.istruttoriaService.avviaProcessoIstruttoriaDUByID(elencoId, tipoProcesso, annoCampagna, sostegno);
      }),
      tap(val => {
        this.repeatingServiceCall.start();
        this.messageService.add(A4gMessages.getToast(
          'tst', A4gSeverityMessage.info, A4gMessages.processoAvviatoOK));
      })
    );
    const showMessage$ = defer(() => this.messageService.add(A4gMessages.getToast(
      'tst', A4gSeverityMessage.warn, A4gMessages.processoInCorso)));
    this.istruttoriaService.checkProcessiInEsecuzione(checkTipiProcessoInEsecuzione).pipe(
      takeUntil(this.componentDestroyed$),
      switchMap(inEsecuzione => iif(
        () => inEsecuzione, showMessage$, avvia$))).subscribe();
  }

  protected avviaCalcolo(tipoProcesso: TipoProcesso) {
    this.eseguiProcessoIstruttoria(
      tipoProcesso, this.processiDaControllare, this.annoCampagna, this.sostegno);
  }

  public avviaProcessoControlloLiquidabilita() {
    this.avviaCalcolo(TipoProcesso.CONTROLLO_LIQUIDABILITA_ISTRUTTORIA);
  }

  public avviaProcessoIntegrazione() {
    this.avviaCalcolo(TipoProcesso.INTEGRAZIONE_ISTRUTTORIE);
  }

  public avviaProcessoCalcoloCapi() {
    this.avviaCalcolo(TipoProcesso.CALCOLO_CAPI_ISTRUTTORIE);
  }

  public avviaProcessoNonAmmissibilita() {
    this.avviaCalcolo(TipoProcesso.NON_AMMISSIBILITA);
  }

  public avviaProcessoLiquidazione() {
    this.avviaCalcolo(TipoProcesso.LIQUIDAZIONE);
  }

  private aggiornaBloccoIstruttorie(isBlocco: boolean): void {
    let idIstruttorie: number[] = [];
    this.getIstruttorieSelezionate().pipe(
      switchMap(elencoId => {
        idIstruttorie = elencoId;
        let tipoProcesso: TipoProcesso;
        if (isBlocco) {
          tipoProcesso = TipoProcesso.BLOCCO_ISTRUTTORIE;
        } else {
          tipoProcesso = TipoProcesso.SBLOCCO_ISTRUTTORIE;
        }
        return this.istruttoriaService.avviaProcessoIstruttoriaDUByID(
          idIstruttorie, tipoProcesso, this.annoCampagna, this.sostegno);
      })
    ).subscribe();
  }

  public bloccoIstruttorie() {
    this.aggiornaBloccoIstruttorie(true);
  }

  public sbloccoIstruttorie() {
    this.aggiornaBloccoIstruttorie(false);
  }

  ngOnChanges(simpleChanges: SimpleChanges) {
    if (simpleChanges.tabNumber && simpleChanges.tabNumber.currentValue === 0) {
      this.resetPage();
    }
  }

  ngOnDestroy() {
    this.loaderService.resetTimeout();
    this.componentDestroyed$.next(true);
    this.componentDestroyed$.complete();
  }

  public isEnableProcessoLiquidazione() {
    return TabCommonComponent.anticipiAttivi(this.tipoIstruttoria, this.annoCampagna)
      && this.flowIstruttoria === FlowIstruttoriaDUEnum.LIQUIDAZIONE
      && (this.statoIstruttoria === StatoIstruttoriaEnum.LIQUIDABILE
        || this.statoIstruttoria === StatoIstruttoriaEnum.PAGAMENTO_NON_AUTORIZZATO
        || this.statoIstruttoria === StatoIstruttoriaEnum.CONTROLLI_INTERSOSTEGNO_OK);
  }

  public isPagamentoAutorizzato() {
    return this.statoIstruttoria === StatoIstruttoriaEnum.PAGAMENTO_AUTORIZZATO;
  }

  public isEnableProcessoIntegrazione() {
    return this.tipoIstruttoria !== TipoIstruttoriaEnum.ANTICIPO
      && this.isPagamentoAutorizzato();
  }

  public isNotIstruttorieSelezionate() {
    return this.istruttorieSelezionate === undefined
      || this.istruttorieSelezionate.length === 0;
  }

  private getProcessiDaControllare(): TipoProcesso[] {
    try {
      let retVal = CONF_PROCESSI
      [this.sostegno]
      [this.flowIstruttoria]
      [this.statoIstruttoria];
      if (retVal.length === 0) {
        return null;
      }
      return retVal;
    } catch (error) {
      return null;
    }
  }

  public stampaElencoLiquidazione(elenco: ElencoLiquidazione) {
    this.istruttoriaService.getVerbaleLiquidazioneIstruttoria(elenco.id).subscribe(
      x => {
        FileSaver.saveAs(x, elenco.codElenco.toString().concat('.pdf'));
      }, error => {
        this.messageService.add(A4gMessages.getToast(
          'tst', A4gSeverityMessage.error, A4gMessages.IMPOSSILE_GENERARE_FILE));
      });
  }

  public static anticipiAttivi(tipoIstr: TipoIstruttoriaEnum, annoCampagna: number): boolean {
    if (TipoIstruttoriaEnum.ANTICIPO === tipoIstr) {
      const dataFineModifica: Date = new Date(annoCampagna, 10, 30);
      const dataAttuale = new Date();
      if (dataAttuale > dataFineModifica) {
        return false;
      }
    }
    return true;
  }
}
