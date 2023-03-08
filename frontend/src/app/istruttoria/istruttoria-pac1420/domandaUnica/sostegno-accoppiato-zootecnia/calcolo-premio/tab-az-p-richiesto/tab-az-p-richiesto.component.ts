import { Component, OnDestroy, OnInit, ChangeDetectorRef } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { MessageService } from 'primeng/api';
import { StatoIstruttoriaEnum } from 'src/app/istruttoria/istruttoria-pac1420/domandaUnica/cruscotto-sostegno/StatoIstruttoriaEnum';
import { ElencoDomandeService } from 'src/app/istruttoria/istruttoria-pac1420/domandaUnica/services/elenco-domande.service';
import { ProcessRepeatingServiceCall } from 'src/app/istruttoria/istruttoria-pac1420/domandaUnica/sostegno-shared/repeating-service-call';
import { LoaderService } from 'src/app/loader.service';
import { TabAzCommonComponent } from '../../TabAzCommonComponent';
import { IstruttoriaService } from 'src/app/istruttoria/istruttoria-pac1420/domandaUnica/istruttoria.service';
import { FlowIstruttoriaDUEnum } from 'src/app/istruttoria/istruttoria-pac1420/domandaUnica/classi/FlowIstruttoriaDUEnum';

@Component({
  selector: 'app-tab-az-p-richiesto',
  templateUrl: '../../elenco-istruttorie-zootecnia-generic.component.html',
  styleUrls: ['../../elenco-istruttorie-zootecnia-generic.component.scss']
})
export class TabAzPRichiestoComponent extends TabAzCommonComponent
  implements OnInit, OnDestroy {

  constructor(
    activatedRoute: ActivatedRoute,
    loaderService: LoaderService,
    istruttoriaService: IstruttoriaService,
    protected repeatingServiceCall: ProcessRepeatingServiceCall ,
    protected elencoDomandeService: ElencoDomandeService,
    protected router: Router,
    protected messageService: MessageService,
    protected changeDetectorRef: ChangeDetectorRef) { 
      super(
        activatedRoute,
        istruttoriaService,
        elencoDomandeService,
        router,
        messageService,
        loaderService,
        StatoIstruttoriaEnum.RICHIESTO,
        repeatingServiceCall,
        changeDetectorRef,
        FlowIstruttoriaDUEnum.CALCOLO_PREMIO);
  }
  
  ngOnInit() {
    super.ngOnInit();
  }

  ngOnDestroy() {
    super.ngOnDestroy();
  }
}
