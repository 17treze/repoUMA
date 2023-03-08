import { ChangeDetectorRef, Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { MessageService } from 'primeng/api';
import { LoaderService } from '../../../../../../loader.service';
import { FlowIstruttoriaDUEnum } from '../../../classi/FlowIstruttoriaDUEnum';
import { StatoIstruttoriaEnum } from '../../../cruscotto-sostegno/StatoIstruttoriaEnum';
import { IstruttoriaService } from '../../../istruttoria.service';
import { ElencoDomandeService } from '../../../services/elenco-domande.service';
import { ProcessRepeatingServiceCall } from '../../../sostegno-shared/repeating-service-call';
import { TabSuperficiCommonComponent } from '../../TabSuperficiCommonComponent';
 
@Component({
  selector: 'app-tab-superfici-liquidabilita-debiti',
  templateUrl: '../../elenco-istruttorie-superfici-generic.component.html',
  styleUrls: ['../../elenco-istruttorie-superfici-generic.component.scss']
})
export class TabAcsClDebitiComponent extends TabSuperficiCommonComponent implements OnInit, OnDestroy {
  
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
        changeDetectorRef,
        repeatingServiceCall,
        FlowIstruttoriaDUEnum.CONTROLLI_LIQUIDABILITA);
  }
  
  ngOnInit() {
    super.ngOnInit();
  }

  ngOnDestroy() {
    super.ngOnDestroy();
  }
}
