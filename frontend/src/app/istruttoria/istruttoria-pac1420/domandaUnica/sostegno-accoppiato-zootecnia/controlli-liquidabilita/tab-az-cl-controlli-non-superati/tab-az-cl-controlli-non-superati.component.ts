import { Component, OnInit, OnDestroy, ChangeDetectorRef } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { MessageService } from 'primeng/api';
import { LoaderService } from 'src/app/loader.service';
import { StatoIstruttoriaEnum } from 'src/app/istruttoria/istruttoria-pac1420/domandaUnica/cruscotto-sostegno/StatoIstruttoriaEnum';
import { TabAzCommonComponent } from '../../TabAzCommonComponent';
import { ProcessRepeatingServiceCall } from 'src/app/istruttoria/istruttoria-pac1420/domandaUnica/sostegno-shared/repeating-service-call';
import { ElencoDomandeService } from 'src/app/istruttoria/istruttoria-pac1420/domandaUnica/services/elenco-domande.service';
import { TipoProcesso } from 'src/app/istruttoria/istruttoria-antimafia/dto/TipoProcesso';
import { IstruttoriaService } from 'src/app/istruttoria/istruttoria-pac1420/domandaUnica/istruttoria.service';
import { FlowIstruttoriaDUEnum } from 'src/app/istruttoria/istruttoria-pac1420/domandaUnica/classi/FlowIstruttoriaDUEnum';

@Component({
  selector: 'app-tab-az-cl-controlli-non-superati',
  templateUrl: '../../elenco-istruttorie-zootecnia-generic.component.html',
  styleUrls: ['../../elenco-istruttorie-zootecnia-generic.component.scss']
})
export class TabAzClControlliNonSuperatiComponent extends TabAzCommonComponent implements OnInit, OnDestroy {

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
        StatoIstruttoriaEnum.CONTROLLI_LIQUIDABILE_KO,
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
