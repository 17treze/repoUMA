import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { StatoIstruttoriaEnum } from '../../cruscotto-sostegno/StatoIstruttoriaEnum';
import { TabSuperficiLiquidazioneLiquidabileComponent } from './tab-liquidabile/tab-superfici-liquidazione-liquidabile.component';
import { TabSuperficiLiquidazioneNonLiquidabileComponent } from './tab-non-liquidabile/tab-superfici-liquidazione-non-liquidabile.component';
import { TabSuperficiLiquidazioneControlliSuperatiComponent } from './tab-controlli-superati/tab-superfici-liquidazione-controlli-superati.component';
import { TabSuperficiLiquidazioneNonAutorizzatoComponent } from './tab-non-autorizzato/tab-superfici-liquidazione-non-autorizzato.component';
import { TabSuperficiLiquidazioneControlliIntersostegnoSuperatiComponent } from './tab-controlli-intersostegno-superati/tab-superfici-liquidazione-controlli-intersostegno-superati.component';
import { TabSuperficiLiquidazioneAutorizzatoComponent } from './tab-autorizzato/tab-superfici-liquidazione-autorizzato.component';
import { SuperficiLiquidazioneComponent } from './superfici-liquidazione.component';


const routes: Routes = [
  {
    path: '',
    component: SuperficiLiquidazioneComponent,
    children: [
      {
        path: StatoIstruttoriaEnum.LIQUIDABILE,
        component: TabSuperficiLiquidazioneLiquidabileComponent,
        data: {
          mybreadcrumb: 'Liquidabile'
        }
      }, {
        path: StatoIstruttoriaEnum.NON_LIQUIDABILE,
        component: TabSuperficiLiquidazioneNonLiquidabileComponent,
        data: {
          mybreadcrumb: 'Non liquidabile'
        }
      }, {
        path: StatoIstruttoriaEnum.CONTROLLI_CALCOLO_OK,
        component: TabSuperficiLiquidazioneControlliSuperatiComponent,
        data: {
          // mybreadcrumb: StatoIstruttoriaEnum.CONTROLLI_CALCOLO_OK
          mybreadcrumb: 'Controlli superati'
        }
      }, {
        path: StatoIstruttoriaEnum.PAGAMENTO_NON_AUTORIZZATO,
        component: TabSuperficiLiquidazioneNonAutorizzatoComponent,
        data: { mybreadcrumb: 'Pagamento non autorizzato' }
      }, {
        path: StatoIstruttoriaEnum.CONTROLLI_INTERSOSTEGNO_OK,
        component: TabSuperficiLiquidazioneControlliIntersostegnoSuperatiComponent,
        data: { mybreadcrumb: 'Controlli intersostegno superati' }
      }, {
        path: StatoIstruttoriaEnum.PAGAMENTO_AUTORIZZATO,
        component: TabSuperficiLiquidazioneAutorizzatoComponent,
        data: { mybreadcrumb: 'Pagamento autorizzato' }
      }, {
        path: '',
        redirectTo: StatoIstruttoriaEnum.LIQUIDABILE
      }
    ]
  }
];

@NgModule({
  imports: [
    RouterModule.forChild(routes)
  ]
})
export class SuperficiLiquidazioneRoutingModule { }
