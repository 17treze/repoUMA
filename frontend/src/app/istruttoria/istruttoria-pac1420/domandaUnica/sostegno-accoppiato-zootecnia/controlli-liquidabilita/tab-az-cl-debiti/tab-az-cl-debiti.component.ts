import { ChangeDetectorRef, Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { MessageService } from 'primeng/api';
import { FlowIstruttoriaDUEnum } from 'src/app/istruttoria/istruttoria-pac1420/domandaUnica/classi/FlowIstruttoriaDUEnum';
import { StatoIstruttoriaEnum } from 'src/app/istruttoria/istruttoria-pac1420/domandaUnica/cruscotto-sostegno/StatoIstruttoriaEnum';
import { IstruttoriaService } from 'src/app/istruttoria/istruttoria-pac1420/domandaUnica/istruttoria.service';
import { ElencoDomandeService } from 'src/app/istruttoria/istruttoria-pac1420/domandaUnica/services/elenco-domande.service';
import { ProcessRepeatingServiceCall } from 'src/app/istruttoria/istruttoria-pac1420/domandaUnica/sostegno-shared/repeating-service-call';
import { LoaderService } from 'src/app/loader.service';
import { TabAzCommonComponent } from '../../TabAzCommonComponent';

@Component({
  selector: 'app-tab-az-cl-debiti',
  templateUrl: '../../elenco-istruttorie-zootecnia-generic.component.html',
  styleUrls: ['../../elenco-istruttorie-zootecnia-generic.component.scss']
})
export class TabAzClDebitiComponent extends TabAzCommonComponent implements OnInit, OnDestroy {

  constructor(
    activatedRoute: ActivatedRoute,
    loaderService: LoaderService,
    istruttoriaService: IstruttoriaService,
    repeatingServiceCall: ProcessRepeatingServiceCall,
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
        StatoIstruttoriaEnum.DEBITI,
        repeatingServiceCall,
        changeDetectorRef,
        FlowIstruttoriaDUEnum.CONTROLLI_LIQUIDABILITA);
  }
  
  ngOnInit() {
    super.ngOnInit();
  }

  ngOnDestroy() {
    super.ngOnDestroy();
  }
}
