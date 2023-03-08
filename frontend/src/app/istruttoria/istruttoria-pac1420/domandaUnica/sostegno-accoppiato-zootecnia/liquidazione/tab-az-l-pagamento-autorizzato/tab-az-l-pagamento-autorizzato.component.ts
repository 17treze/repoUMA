import { Component, OnDestroy, OnInit, ChangeDetectorRef } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { MessageService } from 'primeng/api';
import { takeUntil } from 'rxjs/operators';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';
import { AzRisultatoRicerca } from 'src/app/istruttoria/istruttoria-pac1420/domandaUnica/classi/AzRisultatoRicerca';
import { StatoIstruttoriaEnum } from 'src/app/istruttoria/istruttoria-pac1420/domandaUnica/cruscotto-sostegno/StatoIstruttoriaEnum';
import { IstruttoriaService } from 'src/app/istruttoria/istruttoria-pac1420/domandaUnica/istruttoria.service';
import { ElencoDomandeService } from 'src/app/istruttoria/istruttoria-pac1420/domandaUnica/services/elenco-domande.service';
import { ProcessRepeatingServiceCall } from 'src/app/istruttoria/istruttoria-pac1420/domandaUnica/sostegno-shared/repeating-service-call';
import { LoaderService } from 'src/app/loader.service';
import { TabAzCommonComponent } from '../../TabAzCommonComponent';
import { FlowIstruttoriaDUEnum } from 'src/app/istruttoria/istruttoria-pac1420/domandaUnica/classi/FlowIstruttoriaDUEnum';
import * as FileSaver from 'file-saver';

@Component({
  selector: 'app-tab-az-l-pagamento-autorizzato',
  templateUrl: '../../elenco-istruttorie-zootecnia-generic.component.html',
  styleUrls: ['../../elenco-istruttorie-zootecnia-generic.component.scss']
})
export class TabAzLPagamentoAutorizzatoComponent extends TabAzCommonComponent implements OnInit, OnDestroy {

  constructor(
    activatedRoute: ActivatedRoute,
    loaderService: LoaderService,
    istruttoriaService: IstruttoriaService,
    repeatingServiceCall: ProcessRepeatingServiceCall ,
    elencoDomandeService: ElencoDomandeService,
    router: Router,
    messageService: MessageService,
    changeDetectorRef: ChangeDetectorRef) { 
      super(
        activatedRoute,
        istruttoriaService,
        elencoDomandeService,
        router,
        messageService,
        loaderService,
        StatoIstruttoriaEnum.PAGAMENTO_AUTORIZZATO,
        repeatingServiceCall,
        changeDetectorRef,
        FlowIstruttoriaDUEnum.LIQUIDAZIONE);

      this.cols.push({ field: 'elencoLiquidazione', header: 'Elenco Liquidazione' });
  }

  ngOnDestroy() {
    super.ngOnDestroy();
  }

  ngOnInit() {
    super.ngOnInit();
  }

  downloadVerbaleLiquidazione(domanda: AzRisultatoRicerca) {
    // TODO: chiamo il servizio usato anche dal disaccoppiato per adesso - cancella questo todo se ok
    // TODO: sostituisci a domanda.cuaaIntestatario codiceElenco anche nell'html
    /*const baseRequest = { idDomanda: domanda.idDomanda, codiceElenco: domanda.codiceElenco };
    const request = encodeURIComponent(JSON.stringify(baseRequest));
    this.istruttoriaService.getDocumentoElencoLiquidazione(request).subscribe(result => {
      FileSaver.saveAs(result, domanda.codiceElenco.toString().concat('.pdf'));
    }, err => {
      // TODO: chiedi conferma di questo messaggio al PO
      this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.erroreRecuperoDati));
    });*/

    this.istruttoriaService.getVerbaleLiquidazioneIstruttoria(domanda.idElencoLiquidazione).pipe(
      takeUntil(this.componentDestroyed$)
    ).subscribe(result => {
      FileSaver.saveAs(result, domanda.codiceElenco.toString().concat('.pdf'));
    }, err => {
      this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.erroreRecuperoDati));
    });
  }
}
