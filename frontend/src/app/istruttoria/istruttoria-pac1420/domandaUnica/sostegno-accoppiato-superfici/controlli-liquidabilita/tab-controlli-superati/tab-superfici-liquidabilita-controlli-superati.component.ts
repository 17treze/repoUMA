import { Component, OnDestroy, OnInit, ChangeDetectorRef } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { MessageService } from 'primeng/api';
import { TabSuperficiCommonComponent } from '../../TabSuperficiCommonComponent';
import { LoaderService } from '../../../../../../loader.service';
import { ProcessRepeatingServiceCall } from '../../../sostegno-shared/repeating-service-call';
import { ElencoDomandeService } from '../../../services/elenco-domande.service';
import { StatoIstruttoriaEnum } from '../../../cruscotto-sostegno/StatoIstruttoriaEnum';
import { IstruttoriaService } from '../../../istruttoria.service';
import { FlowIstruttoriaDUEnum } from '../../../classi/FlowIstruttoriaDUEnum';
 
@Component({
  selector: 'app-tab-superfici-liquidabilita-controlli-superati',
  templateUrl: '../../elenco-istruttorie-superfici-generic.component.html',
  styleUrls: ['../../elenco-istruttorie-superfici-generic.component.scss']
})
export class TabSuperficiLiquidabilitaControlliSuperatiComponent extends TabSuperficiCommonComponent implements OnInit, OnDestroy {

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
        StatoIstruttoriaEnum.CONTROLLI_CALCOLO_OK,
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
