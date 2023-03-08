import { Component, OnInit, OnDestroy, Input, ChangeDetectorRef } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Labels } from 'src/app/app.labels';
import { LoaderService } from 'src/app/loader.service';
import { StatoIstruttoriaEnum } from 'src/app/istruttoria/istruttoria-pac1420/domandaUnica/cruscotto-sostegno/StatoIstruttoriaEnum';
import { TabAzCommonComponent } from '../../TabAzCommonComponent';
import { ElencoDomandeService } from 'src/app/istruttoria/istruttoria-pac1420/domandaUnica/services/elenco-domande.service';
import { MessageService } from 'primeng/api';
import { ProcessRepeatingServiceCall } from 'src/app/istruttoria/istruttoria-pac1420/domandaUnica/sostegno-shared/repeating-service-call';
import { IstruttoriaService } from 'src/app/istruttoria/istruttoria-pac1420/domandaUnica/istruttoria.service';
import { FlowIstruttoriaDUEnum } from 'src/app/istruttoria/istruttoria-pac1420/domandaUnica/classi/FlowIstruttoriaDUEnum';
import * as FileSaver from "file-saver";


@Component({
  selector: 'app-tab-az-p-integrazioni-ammissibilita',
  templateUrl: '../../elenco-istruttorie-zootecnia-generic.component.html',
  styleUrls: ['../../elenco-istruttorie-zootecnia-generic.component.scss']
})
export class TabAzPIntegrazioniAmmissibilitaComponent
  extends TabAzCommonComponent implements OnInit, OnDestroy {
  @Input() popupVisibile: boolean;

  constructor(
    activatedRoute: ActivatedRoute,
    loaderService: LoaderService,
    istruttoriaService: IstruttoriaService,
    elencoDomandeService: ElencoDomandeService,
    repeatingServiceCall: ProcessRepeatingServiceCall,
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
        StatoIstruttoriaEnum.INTEGRATO,
        repeatingServiceCall,
        changeDetectorRef,
        FlowIstruttoriaDUEnum.CALCOLO_PREMIO);
  }
  
  
  ngOnInit() {
    super.ngOnInit();
    this.cols.push({ field: 'dataUltimoCalcolo', header: Labels.dtUltimoCalcolo });
  }

  ngOnDestroy() {
    super.ngOnDestroy();
  }

  downloadDatiCapiAgea() {
    this.istruttoriaService.getCapiImpegnatiPerAGEA(this.annoCampagna).toPromise().then(result => {
      FileSaver.saveAs(result, "CAPI_ZOOTECNIA_APPAG.csv");
    });
  }
}
