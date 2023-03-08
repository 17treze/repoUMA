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
export class TabSuperficiCommonComponent extends TabCommonComponent {

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
    flowIstruttoria: FlowIstruttoriaDUEnum) {

      super(
        activatedRoute,
        istruttoriaService,
        elencoDomandeService,
        router,
        messageService,
        loaderService,
        statoIstruttoria,
        repeatingServiceCall,
        SostegnoDu.SUPERFICIE,
        TipoIstruttoriaEnum.SALDO,
        changeDetectorRef,
        flowIstruttoria);
  }
  
  ngOnInit() {
    super.ngOnInit();
  }

  ngOnDestroy() {
    super.ngOnDestroy();
  }
  
  public onFiltersSubmit(filtroIstruttoria: IstruttoriaFiltroRicercaDomande) {
    this.initDefaultFilterValuesIstruttorie();
    this.setFilterValuesIstruttorie(filtroIstruttoria);
    this.getElencoIstruttoria(this.istruttoriaDomandaUnicaFilter);
    this.tabella.pTable.reset();
  }
  
  public getVerbale(idIstruttoria: number): void {
    this.overrideGetVerbale(idIstruttoria);
  }

  public isEnableControlliLiquidabilita() {
    return this.flowIstruttoria === FlowIstruttoriaDUEnum.CONTROLLI_LIQUIDABILITA
      && (this.statoIstruttoria === StatoIstruttoriaEnum.LIQUIDABILE
        || this.statoIstruttoria === StatoIstruttoriaEnum.CONTROLLI_LIQUIDABILE_KO
        || this.statoIstruttoria === StatoIstruttoriaEnum.CONTROLLI_CALCOLO_OK
        || this.statoIstruttoria === StatoIstruttoriaEnum.DEBITI);
  }

  public isEnableCalcoloPremio() {
    return this.flowIstruttoria === FlowIstruttoriaDUEnum.CALCOLO_PREMIO
      && (this.statoIstruttoria === StatoIstruttoriaEnum.RICHIESTO
        || this.statoIstruttoria === StatoIstruttoriaEnum.CONTROLLI_CALCOLO_KO
        || this.statoIstruttoria === StatoIstruttoriaEnum.CONTROLLI_CALCOLO_OK);
  }

  public isEnableVisualizzaDatiAggregati() {
    return this.statoIstruttoria === StatoIstruttoriaEnum.CONTROLLI_CALCOLO_OK;
  }

  public avviaProcessoCalcoloPremio() {
    this.avviaCalcolo(TipoProcesso.CALCOLO_ACCOPPIATO_SUPERFICIE_ISTRUTTORIA);
  }
  
  public isEnableProcessoAmmissibilita() {
    return this.flowIstruttoria === FlowIstruttoriaDUEnum.CALCOLO_PREMIO
      && this.statoIstruttoria === StatoIstruttoriaEnum.CONTROLLI_CALCOLO_KO;
  }

  public isShowDettagli() {
    return true;
    // return this.statoIstruttoria === StatoIstruttoriaEnum.RICHIESTO
    //   || this.statoIstruttoria === StatoIstruttoriaEnum.CONTROLLI_CALCOLO_OK
    //   || this.statoIstruttoria === StatoIstruttoriaEnum.CONTROLLI_CALCOLO_KO
    //   || this.statoIstruttoria === StatoIstruttoriaEnum.CONTROLLI_LIQUIDABILE_KO
    //   || this.statoIstruttoria === StatoIstruttoriaEnum.PAGAMENTO_AUTORIZZATO
    //   || this.statoIstruttoria === StatoIstruttoriaEnum.PAGAMENTO_NON_AUTORIZZATO
    //   || this.statoIstruttoria === StatoIstruttoriaEnum.CONTROLLI_INTERSOSTEGNO_OK
    //   || this.statoIstruttoria === StatoIstruttoriaEnum.NON_AMMISSIBILE
    //   || this.statoIstruttoria === StatoIstruttoriaEnum.NON_LIQUIDABILE
    //   || this.statoIstruttoria === StatoIstruttoriaEnum.LIQUIDABILE;
  }

  public isShowElencoLiquidazione() {
    return this.statoIstruttoria === StatoIstruttoriaEnum.PAGAMENTO_AUTORIZZATO;
  }

  public static isShowBloccatoByState(currentStatoIstruttoria: StatoIstruttoriaEnum) {
    return currentStatoIstruttoria === StatoIstruttoriaEnum.RICHIESTO
      || currentStatoIstruttoria === StatoIstruttoriaEnum.LIQUIDABILE
      || currentStatoIstruttoria === StatoIstruttoriaEnum.CONTROLLI_CALCOLO_OK
      || currentStatoIstruttoria === StatoIstruttoriaEnum.CONTROLLI_CALCOLO_KO
      || currentStatoIstruttoria === StatoIstruttoriaEnum.CONTROLLI_LIQUIDABILE_KO
      || currentStatoIstruttoria === StatoIstruttoriaEnum.PAGAMENTO_NON_AUTORIZZATO
      || currentStatoIstruttoria === StatoIstruttoriaEnum.CONTROLLI_INTERSOSTEGNO_OK
  }

  public static isShowErroriByState(currentStatoIstruttoria: StatoIstruttoriaEnum) {
    return TabSuperficiCommonComponent.isShowBloccatoByState(currentStatoIstruttoria);
  }

  public isShowBloccato() {
    return TabSuperficiCommonComponent.isShowBloccatoByState(this.statoIstruttoria);
  }
  
  public isShowErroriCalcolo() {
    return TabSuperficiCommonComponent.isShowErroriByState(this.statoIstruttoria);
  }

  public isNotIstruttorieSelezionate() {
    return this.istruttorieSelezionate === undefined 
    || this.istruttorieSelezionate.length === 0;
  }
  
  public isEnableBloccoIstruttoria() {
    return TabSuperficiCommonComponent.isShowBloccatoByState(this.statoIstruttoria);
   }

   public isEnableResetIstruttoria() {
    return this.statoIstruttoria === StatoIstruttoriaEnum.NON_AMMISSIBILE
      || this.statoIstruttoria === StatoIstruttoriaEnum.LIQUIDABILE
      || this.statoIstruttoria === StatoIstruttoriaEnum.CONTROLLI_LIQUIDABILE_KO
      || this.statoIstruttoria === StatoIstruttoriaEnum.NON_LIQUIDABILE
      || this.statoIstruttoria === StatoIstruttoriaEnum.PAGAMENTO_NON_AUTORIZZATO
      || this.statoIstruttoria === StatoIstruttoriaEnum.DEBITI;
  }

  public resetIstruttoria() {
    this.eseguiProcessoIstruttoria(
      TipoProcesso.RESET_ISTRUTTORIA,
      [TipoProcesso.NON_AMMISSIBILITA,
        TipoProcesso.LIQUIDAZIONE,
        TipoProcesso.CALCOLO_ACCOPPIATO_SUPERFICIE_ISTRUTTORIA,
        TipoProcesso.SBLOCCO_ISTRUTTORIE,
        TipoProcesso.BLOCCO_ISTRUTTORIE,
        TipoProcesso.CONTROLLO_LIQUIDABILITA_ISTRUTTORIA,
        TipoProcesso.RESET_ISTRUTTORIA]);
  }

}
