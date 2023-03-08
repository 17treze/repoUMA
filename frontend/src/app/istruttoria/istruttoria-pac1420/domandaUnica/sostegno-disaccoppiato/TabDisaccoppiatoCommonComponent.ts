import { ChangeDetectorRef, Directive } from "@angular/core";
import { ActivatedRoute, Router } from "@angular/router";
import { MessageService } from "primeng/api";
import { TipoProcesso } from "src/app/istruttoria/istruttoria-antimafia/dto/TipoProcesso";
import { LoaderService } from "src/app/loader.service";
import { FlowIstruttoriaDUEnum } from "../classi/FlowIstruttoriaDUEnum";
import { SostegnoDu } from "../classi/SostegnoDu";
import { TipoIstruttoriaEnum } from "../classi/TipoIstruttoriaEnum";
import { StatoIstruttoriaEnum } from "../cruscotto-sostegno/StatoIstruttoriaEnum";
import { IstruttoriaFiltroRicercaDomande } from "../istruttoria-shared-filter/istruttoria-filtro-ricerca-domande";
import { IstruttoriaService } from "../istruttoria.service";
import { ElencoDomandeService } from "../services/elenco-domande.service";
import { ProcessRepeatingServiceCall } from "../sostegno-shared/repeating-service-call";
import { TabCommonComponent } from "../sostegno-shared/TabCommonComponent";

@Directive()
export class TabDisaccoppiatoCommonComponent extends TabCommonComponent {

  constructor(
    activatedRoute: ActivatedRoute,
    istruttoriaService: IstruttoriaService,
    elencoDomandeService: ElencoDomandeService,
    router: Router,
    messageService: MessageService,
    loaderService: LoaderService,
    statoIstruttoria: StatoIstruttoriaEnum,
    changeDetectorRef: ChangeDetectorRef,
    repeatingServiceCall: ProcessRepeatingServiceCall,
    flowIstruttoria?: FlowIstruttoriaDUEnum) {

      super(
        activatedRoute,
        istruttoriaService,
        elencoDomandeService,
        router,
        messageService,
        loaderService,
        statoIstruttoria,
        repeatingServiceCall,
        SostegnoDu.DISACCOPPIATO,
        TabDisaccoppiatoCommonComponent.getTipoIstruttoriaFromString(activatedRoute.snapshot.paramMap.get('tipo')),
        changeDetectorRef,
        flowIstruttoria);

      this.items = this.menuActionPagamentoAutorizzato();
  }

  getVerbale(idIstruttoria: number): void {
    this.overrideGetVerbale(idIstruttoria);
  }
  
  ngOnInit() {
    super.ngOnInit();
  }

  ngOnDestroy() {
    super.ngOnDestroy();
  }
  
  public static getTipoIstruttoriaFromString(value: string) {
    if (value === 'saldo') {
      return TipoIstruttoriaEnum.SALDO;
    } else if (value === 'anticipo') {
      return TipoIstruttoriaEnum.ANTICIPO;
    } else if (value === 'integrazione') {
      return TipoIstruttoriaEnum.INTEGRAZIONE;
    }
  }
  
  public isEnableCalcoloDisaccoppiato() {
    return TabCommonComponent.anticipiAttivi(this.tipoIstruttoria, this.annoCampagna)
        && this.flowIstruttoria === FlowIstruttoriaDUEnum.CALCOLO_PREMIO
      && this.statoIstruttoria !== StatoIstruttoriaEnum.NON_AMMISSIBILE;
  }

  public isEnableControlliLiquidabilita() {
    return TabCommonComponent.anticipiAttivi(this.tipoIstruttoria, this.annoCampagna)
      && this.flowIstruttoria === FlowIstruttoriaDUEnum.CONTROLLI_LIQUIDABILITA;
  }

  public isEnableProcessoAmmissibilita() {
    return this.isEnableCalcoloDisaccoppiato()
      && this.statoIstruttoria === StatoIstruttoriaEnum.CONTROLLI_CALCOLO_KO;
  }

  public avviaProcessoCalcoloDisaccoppiato() {
    this.avviaCalcolo(TipoProcesso.CALCOLO_DISACCOPPIATO_ISTRUTTORIA);
  }

  public isShowDettagli() {
    return !this.isShowAzioni();
  }

  public isShowAzioni() {
    return this.statoIstruttoria === StatoIstruttoriaEnum.NON_AMMISSIBILE
      || this.statoIstruttoria === StatoIstruttoriaEnum.NON_LIQUIDABILE
      || this.statoIstruttoria === StatoIstruttoriaEnum.PAGAMENTO_AUTORIZZATO
      || this.statoIstruttoria === StatoIstruttoriaEnum.DEBITI;
  }
  
  public isEnableBloccoIstruttoria() {
    return TabCommonComponent.anticipiAttivi(this.tipoIstruttoria, this.annoCampagna)
      && this.sostegno === SostegnoDu.DISACCOPPIATO
      && this.statoIstruttoria !== StatoIstruttoriaEnum.NON_LIQUIDABILE
      && this.statoIstruttoria !== StatoIstruttoriaEnum.NON_AMMISSIBILE
      && this.statoIstruttoria !== StatoIstruttoriaEnum.PAGAMENTO_AUTORIZZATO
      && this.statoIstruttoria !== StatoIstruttoriaEnum.DEBITI;
  }

  public static isShowBloccatoByState(currentStatoIstruttoria: StatoIstruttoriaEnum) {
    return currentStatoIstruttoria === StatoIstruttoriaEnum.RICHIESTO
      || currentStatoIstruttoria === StatoIstruttoriaEnum.CONTROLLI_CALCOLO_OK
      || currentStatoIstruttoria === StatoIstruttoriaEnum.CONTROLLI_CALCOLO_KO
      || currentStatoIstruttoria === StatoIstruttoriaEnum.LIQUIDABILE
      || currentStatoIstruttoria === StatoIstruttoriaEnum.PAGAMENTO_NON_AUTORIZZATO
      || currentStatoIstruttoria === StatoIstruttoriaEnum.CONTROLLI_LIQUIDABILE_KO
      || currentStatoIstruttoria === StatoIstruttoriaEnum.CONTROLLI_INTERSOSTEGNO_OK;
  }

  public static isShowErroriByState(currentStatoIstruttoria: StatoIstruttoriaEnum) {
    return TabDisaccoppiatoCommonComponent.isShowBloccatoByState(currentStatoIstruttoria);
  }
  
  public isShowBloccato() {
    return TabDisaccoppiatoCommonComponent.isShowBloccatoByState(this.statoIstruttoria);
  }
  
  public isShowErroriCalcolo() {
    return TabDisaccoppiatoCommonComponent.isShowErroriByState(this.statoIstruttoria);
  }

  public onFiltersSubmit(filtroIstruttoria: IstruttoriaFiltroRicercaDomande) {
    this.initDefaultFilterValuesIstruttorie();
    this.setFilterValuesIstruttorie(filtroIstruttoria);
    this.getElencoIstruttoria(this.istruttoriaDomandaUnicaFilter);
    this.tabella.pTable.reset();
  }

  public isEnableResetIstruttoria() {
    return TabCommonComponent.anticipiAttivi(this.tipoIstruttoria, this.annoCampagna)
        && (this.statoIstruttoria === StatoIstruttoriaEnum.NON_AMMISSIBILE
      || this.statoIstruttoria === StatoIstruttoriaEnum.LIQUIDABILE
      || this.statoIstruttoria === StatoIstruttoriaEnum.CONTROLLI_LIQUIDABILE_KO
      || this.statoIstruttoria === StatoIstruttoriaEnum.NON_LIQUIDABILE
      || this.statoIstruttoria === StatoIstruttoriaEnum.PAGAMENTO_NON_AUTORIZZATO
      || this.statoIstruttoria === StatoIstruttoriaEnum.DEBITI);
  }

  public resetIstruttoria() {
    this.eseguiProcessoIstruttoria(
      TipoProcesso.RESET_ISTRUTTORIA,
      [TipoProcesso.NON_AMMISSIBILITA,
        TipoProcesso.LIQUIDAZIONE,
        TipoProcesso.CALCOLO_DISACCOPPIATO_ISTRUTTORIA,
        TipoProcesso.SBLOCCO_ISTRUTTORIE,
        TipoProcesso.BLOCCO_ISTRUTTORIE,
        TipoProcesso.CONTROLLO_LIQUIDABILITA_ISTRUTTORIA,
        TipoProcesso.INTEGRAZIONE_ISTRUTTORIE,
        TipoProcesso.RESET_ISTRUTTORIA]);
  }
}