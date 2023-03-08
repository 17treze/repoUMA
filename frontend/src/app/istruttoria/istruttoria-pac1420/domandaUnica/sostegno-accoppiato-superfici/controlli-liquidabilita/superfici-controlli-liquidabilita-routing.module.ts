import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { StatoIstruttoriaEnum } from '../../cruscotto-sostegno/StatoIstruttoriaEnum';
import { SuperficiControlliLiquidiabilitaComponent } from './superfici-controlli-liquidabilita.component';
import { TabAcsClDebitiComponent } from './tab-acs-debiti/tab-acs-cl-debiti.component';
import { TabSuperficiLiquidabilitaControlliNonSuperatiComponent } from './tab-controlli-non-superati/tab-superfici-liquidabilita-controlli-non-superati.component';
import { TabSuperficiLiquidabilitaControlliSuperatiComponent } from './tab-controlli-superati/tab-superfici-liquidabilita-controlli-superati.component';
import { TabSuperficiLiquidabilitaLiquidabileComponent } from './tab-liquidabile/tab-superfici-liquidabilita-liquidabile.component';


const routes: Routes = [
  {
    path: '',
    component: SuperficiControlliLiquidiabilitaComponent,
    children: [
      {
        path: StatoIstruttoriaEnum.CONTROLLI_CALCOLO_OK,
        component: TabSuperficiLiquidabilitaControlliSuperatiComponent,
        data: {
          // mybreadcrumb: StatoIstruttoriaEnum.CONTROLLI_CALCOLO_OK
          mybreadcrumb: 'Controlli superati'
        }
      }, {
        path: StatoIstruttoriaEnum.LIQUIDABILE,
        component: TabSuperficiLiquidabilitaLiquidabileComponent,
        data: {
          // mybreadcrumb: StatoIstruttoriaEnum.LIQUIDABILE
          mybreadcrumb: 'Non ammissibile'
        }
      }, {
        path: StatoIstruttoriaEnum.CONTROLLI_LIQUIDABILE_KO,
        component: TabSuperficiLiquidabilitaControlliNonSuperatiComponent,
        data: {
          // mybreadcrumb: StatoIstruttoriaEnum.CONTROLLI_LIQUIDABILE_KO
          mybreadcrumb: 'Controlli non superati'
        }
      } , {
        path: StatoIstruttoriaEnum.DEBITI,
        component: TabAcsClDebitiComponent,
        data: {
          // mybreadcrumb: StatoIstruttoriaEnum.CONTROLLI_LIQUIDABILE_KO
          mybreadcrumb: 'Recupero Debiti'
        }
      }, { path: '',
        redirectTo: StatoIstruttoriaEnum.CONTROLLI_CALCOLO_OK,
        pathMatch: 'full'
      }
    ]
  }
];

@NgModule({
  imports: [
    RouterModule.forChild(routes)
  ]
})
export class SuperficiControlliLiquidiabilitaRoutingModule { }
