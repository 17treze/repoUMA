import { ChangeDetectorRef, Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { MessageService } from 'primeng/api';
import { LoaderService } from '../../../../../../loader.service';
import { FlowIstruttoriaDUEnum } from '../../../classi/FlowIstruttoriaDUEnum';
import { StatoIstruttoriaEnum } from '../../../cruscotto-sostegno/StatoIstruttoriaEnum';
import { IstruttoriaService } from '../../../istruttoria.service';
import { ElencoDomandeService } from '../../../services/elenco-domande.service';
import { ProcessRepeatingServiceCall } from '../../../sostegno-shared/repeating-service-call';
import { TabDisaccoppiatoCommonComponent } from '../../TabDisaccoppiatoCommonComponent';
 
@Component({
  selector: 'app-tab-disaccoppiato-liquidabilita-debiti',
  templateUrl: '../../elenco-istruttorie-disaccoppiato-generic.component.html',
  styleUrls: ['../../elenco-istruttorie-disaccoppiato-generic.component.scss']
})
export class TabDisaccoppiatoLiquidabilitaDebitiComponent
    extends TabDisaccoppiatoCommonComponent implements OnInit, OnDestroy {

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