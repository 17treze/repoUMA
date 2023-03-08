import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { StatoIstruttoriaEnum } from '../../cruscotto-sostegno/StatoIstruttoriaEnum';
import { TabDisaccoppiatoLiquidazioneLiquidabileComponent } from './tab-liquidabile/tab-disaccoppiato-liquidazione-liquidabile.component';
import { TabDisaccoppiatoLiquidazioneNonLiquidabileComponent } from './tab-non-liquidabile/tab-disaccoppiato-liquidazione-non-liquidabile.component';
import { TabDisaccoppiatoLiquidazioneControlliSuperatiComponent } from './tab-controlli-superati/tab-disaccoppiato-liquidazione-controlli-superati.component';
import { TabDisaccoppiatoLiquidazioneNonAutorizzatoComponent } from './tab-non-autorizzato/tab-disaccoppiato-liquidazione-non-autorizzato.component';
import { TabDisaccoppiatoLiquidazioneControlliIntersostegnoSuperatiComponent } from './tab-controlli-intersostegno-superati/tab-disaccoppiato-liquidazione-controlli-intersostegno-superati.component';
import { TabDisaccoppiatoLiquidazioneAutorizzatoComponent } from './tab-autorizzato/tab-disaccoppiato-liquidazione-autorizzato.component';
import { DisaccoppiatoLiquidazioneComponent } from './disaccoppiato-liquidazione.component';


const routes: Routes = [
  {
    path: '',
    component: DisaccoppiatoLiquidazioneComponent,
    children: [
      {
        path: StatoIstruttoriaEnum.LIQUIDABILE,
        component: TabDisaccoppiatoLiquidazioneLiquidabileComponent,
        data: {
          mybreadcrumb: 'Liquidabile'
        }
      }, {
        path: StatoIstruttoriaEnum.NON_LIQUIDABILE,
        component: TabDisaccoppiatoLiquidazioneNonLiquidabileComponent,
        data: {
          mybreadcrumb: 'Non liquidabile'
        }
      }, {
        path: StatoIstruttoriaEnum.CONTROLLI_CALCOLO_OK,
        component: TabDisaccoppiatoLiquidazioneControlliSuperatiComponent,
        data: {
          // mybreadcrumb: StatoIstruttoriaEnum.CONTROLLI_CALCOLO_OK
          mybreadcrumb: 'Controlli superati'
        }
      }, {
        path: StatoIstruttoriaEnum.PAGAMENTO_NON_AUTORIZZATO,
        component: TabDisaccoppiatoLiquidazioneNonAutorizzatoComponent,
        data: { mybreadcrumb: 'Pagamento non autorizzato' }
      }, {
        path: StatoIstruttoriaEnum.CONTROLLI_INTERSOSTEGNO_OK,
        component: TabDisaccoppiatoLiquidazioneControlliIntersostegnoSuperatiComponent,
        data: { mybreadcrumb: 'Controlli intersostegno superati' }
      }, {
        path: StatoIstruttoriaEnum.PAGAMENTO_AUTORIZZATO,
        component: TabDisaccoppiatoLiquidazioneAutorizzatoComponent,
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
export class DisaccoppiatoLiquidazioneRoutingModule { }
