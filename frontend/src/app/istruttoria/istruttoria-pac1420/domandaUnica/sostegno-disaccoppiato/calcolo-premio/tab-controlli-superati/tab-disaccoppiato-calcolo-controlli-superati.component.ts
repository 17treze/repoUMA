import { Component, OnDestroy, OnInit, ChangeDetectorRef } from '@angular/core';
import { TabDisaccoppiatoCommonComponent } from '../../TabDisaccoppiatoCommonComponent';
import { ActivatedRoute, Router } from '@angular/router';
import { IstruttoriaService } from '../../../istruttoria.service';
import { MessageService } from 'primeng/api';
import { StatoIstruttoriaEnum } from '../../../cruscotto-sostegno/StatoIstruttoriaEnum';
import { ElencoDomandeService } from '../../../services/elenco-domande.service';
import { ProcessRepeatingServiceCall } from '../../../sostegno-shared/repeating-service-call';
import { LoaderService } from 'src/app/loader.service';
import { FlowIstruttoriaDUEnum } from '../../../classi/FlowIstruttoriaDUEnum';

@Component({
  selector: 'app-tab-disaccoppiato-calcolo-controlli-superati',
  templateUrl: '../../elenco-istruttorie-disaccoppiato-generic.component.html',
  styleUrls: ['../../elenco-istruttorie-disaccoppiato-generic.component.scss']
})
export class TabDisaccoppiatoCalcoloControlliSuperatiComponent extends TabDisaccoppiatoCommonComponent {

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
      FlowIstruttoriaDUEnum.CALCOLO_PREMIO);
  }

  ngOnInit() {
    super.ngOnInit();
  }

  ngOnDestroy() {
    super.ngOnDestroy();
  }
}
