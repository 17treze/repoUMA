import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { StatoIstruttoriaEnum } from '../../cruscotto-sostegno/StatoIstruttoriaEnum';
import { AzControlliLiquidabilitaComponent } from './az-controlli-liquidabilita.component';
import { TabAzClControlliNonSuperatiComponent } from './tab-az-cl-controlli-non-superati/tab-az-cl-controlli-non-superati.component';
import { TabAzClControlliSuperatiComponent } from './tab-az-cl-controlli-superati/tab-az-cl-controlli-superati.component';
import { TabAzClDebitiComponent } from './tab-az-cl-debiti/tab-az-cl-debiti.component';
import { TabAzClLiquidabileComponent } from './tab-az-cl-liquidabile/tab-az-cl-liquidabile.component';

const routes: Routes = [
  {
    path: '',
    component: AzControlliLiquidabilitaComponent,
    children: [
      {
        path: '',
        redirectTo: StatoIstruttoriaEnum.CONTROLLI_CALCOLO_OK,
        pathMatch: 'full'
      }, {
        path: StatoIstruttoriaEnum.CONTROLLI_CALCOLO_OK,
        component: TabAzClControlliSuperatiComponent,
        data: {
          mybreadcrumb: 'Controlli superati'
        }
      }, {
        path: StatoIstruttoriaEnum.LIQUIDABILE,
        component: TabAzClLiquidabileComponent,
        data: {
          mybreadcrumb: 'Liquidabile'
        }
      }, {
        path: StatoIstruttoriaEnum.CONTROLLI_LIQUIDABILE_KO,
        component: TabAzClControlliNonSuperatiComponent,
        data: {
          mybreadcrumb: 'Controlli non superati'
        }
      },  {
        path: StatoIstruttoriaEnum.DEBITI,
        component: TabAzClDebitiComponent,
        data: {
          // mybreadcrumb: StatoIstruttoriaEnum.CONTROLLI_LIQUIDABILE_KO
          mybreadcrumb: 'Recupero Debiti'
        }
      },
    ]
  }
];

@NgModule({
  imports: [
    RouterModule.forChild(routes)
  ]
})
export class AzControlliLiquidabilitaRoutingModule { }
