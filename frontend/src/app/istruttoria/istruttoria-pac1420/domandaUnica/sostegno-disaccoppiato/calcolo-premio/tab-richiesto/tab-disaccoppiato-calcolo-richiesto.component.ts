import { Component, OnDestroy, OnInit, ChangeDetectorRef } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { MessageService } from 'primeng/api';
import { TabDisaccoppiatoCommonComponent } from '../../TabDisaccoppiatoCommonComponent';
import { LoaderService } from '../../../../../../loader.service';
import { ProcessRepeatingServiceCall } from '../../../sostegno-shared/repeating-service-call';
import { ElencoDomandeService } from '../../../services/elenco-domande.service';
import { StatoIstruttoriaEnum } from '../../../cruscotto-sostegno/StatoIstruttoriaEnum';
import { IstruttoriaService } from '../../../istruttoria.service';
import { FlowIstruttoriaDUEnum } from '../../../classi/FlowIstruttoriaDUEnum';
 
@Component({
  selector: 'app-tab-disaccoppiato-calcolo-richiesto.component',
  templateUrl: '../../elenco-istruttorie-disaccoppiato-generic.component.html',
  styleUrls: ['../../elenco-istruttorie-disaccoppiato-generic.component.scss']
})
export class TabDisaccoppiatoCalcoloRichiestoComponent 
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
        StatoIstruttoriaEnum.RICHIESTO,
        changeDetectorRef,
        repeatingServiceCall,
        FlowIstruttoriaDUEnum.CALCOLO_PREMIO);
  }
  
  ngOnInit() {
    super.ngOnInit();
  }
  
  ngOnDestroy() {
    super.ngOnDestroy();
  }
}
