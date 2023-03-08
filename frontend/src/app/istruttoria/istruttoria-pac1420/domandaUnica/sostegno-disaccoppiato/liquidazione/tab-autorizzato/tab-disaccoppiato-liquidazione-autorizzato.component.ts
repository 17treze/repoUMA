import { Component, OnDestroy, OnInit, ChangeDetectorRef } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { MessageService } from 'primeng/api';
import { TabDisaccoppiatoCommonComponent } from '../../TabDisaccoppiatoCommonComponent';
import { LoaderService } from '../../../../../../loader.service';
import { ProcessRepeatingServiceCall } from '../../../sostegno-shared/repeating-service-call';
import { ElencoDomandeService } from '../../../services/elenco-domande.service';
import { StatoIstruttoriaEnum } from '../../../cruscotto-sostegno/StatoIstruttoriaEnum';
import { IstruttoriaService } from '../../../istruttoria.service';
import * as FileSaver from 'file-saver';
import { ElencoLiquidazione } from '../../../classi/IstruttoriaDomandaUnica';
import { A4gSeverityMessage, A4gMessages } from 'src/app/a4g-common/a4g-messages';
import { FlowIstruttoriaDUEnum } from '../../../classi/FlowIstruttoriaDUEnum';
 
@Component({
  selector: 'app-tab-disaccoppiato-liquidazione-autorizzato',
  templateUrl: '../../elenco-istruttorie-disaccoppiato-generic.component.html',
  styleUrls: ['../../elenco-istruttorie-disaccoppiato-generic.component.scss']
})
export class TabDisaccoppiatoLiquidazioneAutorizzatoComponent extends TabDisaccoppiatoCommonComponent {

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
        StatoIstruttoriaEnum.PAGAMENTO_AUTORIZZATO,
        changeDetectorRef,
        repeatingServiceCall,
        FlowIstruttoriaDUEnum.LIQUIDAZIONE);

      this.items = this.menuActionPagamentoAutorizzato();
      this.cols.push({ field: 'elencoLiquidazione', header: 'Elenco Liquidazione' });
  }
  
  ngOnInit() {
    super.ngOnInit();
  }

  ngOnDestroy() {
    super.ngOnDestroy();
  }
}
