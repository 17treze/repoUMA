import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { AzLiquidazioneComponent } from './az-liquidazione.component';
import { TabAzLLiquidabileComponent } from './tab-az-l-liquidabile/tab-az-l-liquidabile.component';
import { TabAzLNonLiquidabileComponent } from './tab-az-l-non-liquidabile/tab-az-l-non-liquidabile.component';
import { StatoIstruttoriaEnum } from '../../cruscotto-sostegno/StatoIstruttoriaEnum';
import { TabAzLPagamentoNonAutorizzatoComponent } from './tab-az-l-pagamento-non-autorizzato/tab-az-l-pagamento-non-autorizzato.component';
import { TabAzLControlliIntersostegnoSuperatiComponent } from './tab-az-l-controlli-intersostegno-superati/tab-az-l-controlli-intersostegno-superati.component';
import { TabAzLPagamentoAutorizzatoComponent } from './tab-az-l-pagamento-autorizzato/tab-az-l-pagamento-autorizzato.component';

const routes: Routes = [
  {
    path: '',
    component: AzLiquidazioneComponent,
    children: [
      {
        path: '',
        redirectTo: StatoIstruttoriaEnum.LIQUIDABILE,
        pathMatch: 'full'
      }, {
        path: StatoIstruttoriaEnum.LIQUIDABILE,
        component: TabAzLLiquidabileComponent,
        data: {
          mybreadcrumb: 'Liquidabile'
          // mybreadcrumb: StatoIstruttoriaEnum.LIQUIDABILE
        }
      }, {
        path: StatoIstruttoriaEnum.NON_LIQUIDABILE,
        component: TabAzLNonLiquidabileComponent,
        data: {
          mybreadcrumb: 'Non liquidabile'
          // mybreadcrumb: StatoIstruttoriaEnum.NON_LIQUIDABILE
        }
      }, {
        path: StatoIstruttoriaEnum.PAGAMENTO_NON_AUTORIZZATO,
        component: TabAzLPagamentoNonAutorizzatoComponent,
        data: {
          mybreadcrumb: 'Pagamento non autorizzato'
          // mybreadcrumb: StatoIstruttoriaEnum.PAGAMENTO_NON_AUTORIZZATO
        }
      }, {
        path: StatoIstruttoriaEnum.CONTROLLI_INTERSOSTEGNO_OK,
        component: TabAzLControlliIntersostegnoSuperatiComponent,
        data: {
          mybreadcrumb: 'Controlli intersostegno superati'
          // mybreadcrumb: StatoIstruttoriaEnum.CONTROLLI_INTERSOSTEGNO_OK
        }
      }, {
        path: StatoIstruttoriaEnum.PAGAMENTO_AUTORIZZATO,
        component: TabAzLPagamentoAutorizzatoComponent,
        data: {
          mybreadcrumb: 'Pagamento autorizzato'
          // mybreadcrumb: StatoIstruttoriaEnum.PAGAMENTO_AUTORIZZATO
        }
      }
    ]
  }
];


@NgModule({
  imports: [
    RouterModule.forChild(routes)
  ]
})
export class AzLiquidazioneRoutingModule { }
