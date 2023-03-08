import { ChangeDetectorRef, OnDestroy, OnInit, Directive } from "@angular/core";
import { ActivatedRoute, Router } from "@angular/router";
import * as FileSaver from 'file-saver';
import { MessageService } from "primeng/api";
import { A4gMessages, A4gSeverityMessage } from "src/app/a4g-common/a4g-messages";
import { TipoProcesso } from "src/app/istruttoria/istruttoria-antimafia/dto/TipoProcesso";
import { StatoIstruttoriaEnum } from "src/app/istruttoria/istruttoria-pac1420/domandaUnica/cruscotto-sostegno/StatoIstruttoriaEnum";
import { ElencoDomandeService } from 'src/app/istruttoria/istruttoria-pac1420/domandaUnica/services/elenco-domande.service';
import { LoaderService } from "src/app/loader.service";
import { FlowIstruttoriaDUEnum } from "../classi/FlowIstruttoriaDUEnum";
import { SostegnoDu } from "../classi/SostegnoDu";
import { TipoIstruttoriaEnum } from "../classi/TipoIstruttoriaEnum";
import { IstruttoriaFiltroRicercaDomande } from "../istruttoria-shared-filter/istruttoria-filtro-ricerca-domande";
import { IstruttoriaService } from "../istruttoria.service";
import { ProcessRepeatingServiceCall } from "../sostegno-shared/repeating-service-call";
import { TabCommonComponent } from "../sostegno-shared/TabCommonComponent";

@Directive()
export class TabAzCommonComponent extends TabCommonComponent
  implements OnInit, OnDestroy {

  constructor(
    activatedRoute: ActivatedRoute,
    istruttoriaService: IstruttoriaService,
    elencoDomandeService: ElencoDomandeService,
    router: Router,
    messageService: MessageService,
    loaderService: LoaderService,
    statoIstruttoria: StatoIstruttoriaEnum,
    protected repeatingServiceCall: ProcessRepeatingServiceCall,
    changeDetectorRef: ChangeDetectorRef,
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
        SostegnoDu.ZOOTECNIA,
        TipoIstruttoriaEnum.SALDO,
        changeDetectorRef,
        flowIstruttoria);

      this.items = this.menuActionPagamentoAutorizzato();
  }
  
  getVerbale(idIstruttoria: number) {
    this.istruttoriaService.getVerbaleIstruttoriaAcz(idIstruttoria).subscribe(x => {
      FileSaver.saveAs(x, 'VerbaleIstruttoria_'.concat(this.istruttoriaSelezionata.idDomanda.toString()).concat('.pdf'));
    }, error => {
      this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.IMPOSSILE_GENERARE_FILE));
    });
  }
  
  public onFiltersSubmit(filtroIstruttoria: IstruttoriaFiltroRicercaDomande) {
    this.initDefaultFilterValuesIstruttorie();
    this.setFilterValuesIstruttorie(filtroIstruttoria);
    this.istruttoriaDomandaUnicaFilter.clean();
    this.getElencoIstruttoria(this.istruttoriaDomandaUnicaFilter);
    this.tabella.pTable.reset();
  }

  public avviaProcessoCalcoloAccoppiatoZootecnia() {
    this.avviaCalcolo(TipoProcesso.CALCOLO_ACCOPPIATO_ZOOTECNIA_ISTRUTTORIA);
  }
  
  public isShowUltimoCalcolo() {
    return this.statoIstruttoria === StatoIstruttoriaEnum.INTEGRATO;
  }

  public isEnableCalcoloCapi() {
    return this.statoIstruttoria === StatoIstruttoriaEnum.RICHIESTO
        || this.statoIstruttoria === StatoIstruttoriaEnum.INTEGRATO;
  }

  public isEnableDatiCapiAgea() {
    return this.statoIstruttoria === StatoIstruttoriaEnum.INTEGRATO
  }

  public isEnableCapiPerIntervento() {
    return this.statoIstruttoria === StatoIstruttoriaEnum.INTEGRATO
  }

  public isEnableCalcoloAccoppiatoZootecnia() {
    return this.statoIstruttoria === StatoIstruttoriaEnum.INTEGRATO
      || (this.flowIstruttoria === FlowIstruttoriaDUEnum.CALCOLO_PREMIO
        && (this.statoIstruttoria === StatoIstruttoriaEnum.CONTROLLI_CALCOLO_OK
        || this.statoIstruttoria === StatoIstruttoriaEnum.CONTROLLI_CALCOLO_KO));
  }

  public isEnableProcessoAmmissibilita() {
    return this.statoIstruttoria === StatoIstruttoriaEnum.CONTROLLI_CALCOLO_KO;
  }

  public isEnableControlloLiquidabilita() {
    return this.flowIstruttoria === FlowIstruttoriaDUEnum.CONTROLLI_LIQUIDABILITA
      && (this.statoIstruttoria === StatoIstruttoriaEnum.CONTROLLI_CALCOLO_OK
        || this.statoIstruttoria === StatoIstruttoriaEnum.LIQUIDABILE
        || this.statoIstruttoria === StatoIstruttoriaEnum.CONTROLLI_LIQUIDABILE_KO
        || this.statoIstruttoria === StatoIstruttoriaEnum.DEBITI);
  }

  public isShowAzioni() {
    return this.statoIstruttoria === StatoIstruttoriaEnum.NON_AMMISSIBILE
      || this.statoIstruttoria === StatoIstruttoriaEnum.NON_LIQUIDABILE
      || this.statoIstruttoria === StatoIstruttoriaEnum.PAGAMENTO_AUTORIZZATO
      || this.statoIstruttoria === StatoIstruttoriaEnum.DEBITI;
  }

  public isShowDettagli() {
    return !this.isShowAzioni();
  }

  public static isShowBloccatoByState(currentStatoIstruttoria: StatoIstruttoriaEnum) {
    return currentStatoIstruttoria === StatoIstruttoriaEnum.RICHIESTO
      || currentStatoIstruttoria === StatoIstruttoriaEnum.INTEGRATO
      || currentStatoIstruttoria === StatoIstruttoriaEnum.CONTROLLI_CALCOLO_OK
      || currentStatoIstruttoria === StatoIstruttoriaEnum.LIQUIDABILE
      || currentStatoIstruttoria === StatoIstruttoriaEnum.CONTROLLI_LIQUIDABILE_KO
      || currentStatoIstruttoria === StatoIstruttoriaEnum.CONTROLLI_CALCOLO_KO
      || currentStatoIstruttoria === StatoIstruttoriaEnum.PAGAMENTO_NON_AUTORIZZATO
      || currentStatoIstruttoria === StatoIstruttoriaEnum.CONTROLLI_INTERSOSTEGNO_OK;
  }

  public static isShowErroriByState(currentStatoIstruttoria: StatoIstruttoriaEnum) {
    return TabAzCommonComponent.isShowBloccatoByState(currentStatoIstruttoria);
  }

  public isShowBloccato() {
    return TabAzCommonComponent.isShowBloccatoByState(this.statoIstruttoria);
  }
  
  public isShowErroriCalcolo() {
    return TabAzCommonComponent.isShowErroriByState(this.statoIstruttoria);
  }

  public isEnableBloccoIstruttoria() {
    return this.statoIstruttoria === StatoIstruttoriaEnum.RICHIESTO
      || this.statoIstruttoria === StatoIstruttoriaEnum.INTEGRATO
      || this.statoIstruttoria === StatoIstruttoriaEnum.CONTROLLI_CALCOLO_OK
      || this.statoIstruttoria === StatoIstruttoriaEnum.LIQUIDABILE
      || this.statoIstruttoria === StatoIstruttoriaEnum.CONTROLLI_CALCOLO_KO
      || this.statoIstruttoria === StatoIstruttoriaEnum.CONTROLLI_LIQUIDABILE_KO
      || this.statoIstruttoria === StatoIstruttoriaEnum.PAGAMENTO_NON_AUTORIZZATO
      || this.statoIstruttoria === StatoIstruttoriaEnum.CONTROLLI_INTERSOSTEGNO_OK;
   }

  public isNotIstruttorieSelezionate() {
    return this.istruttorieSelezionate === undefined 
    || this.istruttorieSelezionate.length === 0;
  }
  
  public isEnableResetIstruttoria() {
    return this.statoIstruttoria === StatoIstruttoriaEnum.NON_AMMISSIBILE || this.statoIstruttoria === StatoIstruttoriaEnum.DEBITI;
  }

  public resetIstruttoria() {
    this.eseguiProcessoIstruttoria(
      TipoProcesso.RESET_ISTRUTTORIA,
      [TipoProcesso.CALCOLO_CAPI_ISTRUTTORIE,
        TipoProcesso.CALCOLO_ACCOPPIATO_ZOOTECNIA_ISTRUTTORIA,
        TipoProcesso.RESET_ISTRUTTORIA]);
  }
}
