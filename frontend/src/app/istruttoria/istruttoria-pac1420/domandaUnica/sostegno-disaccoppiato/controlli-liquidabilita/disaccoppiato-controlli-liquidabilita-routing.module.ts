import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { StatoIstruttoriaEnum } from '../../cruscotto-sostegno/StatoIstruttoriaEnum';
import { DisaccoppiatoControlliLiquidiabilitaComponent } from './disaccoppiato-controlli-liquidabilita.component';
import { TabDisaccoppiatoLiquidabilitaControlliNonSuperatiComponent } from './tab-controlli-non-superati/tab-disaccoppiato-liquidabilita-controlli-non-superati.component';
import { TabDisaccoppiatoLiquidabilitaControlliSuperatiComponent } from './tab-controlli-superati/tab-disaccoppiato-liquidabilita-controlli-superati.component';
import { TabDisaccoppiatoLiquidabilitaDebitiComponent } from './tab-debiti/tab-disaccoppiato-liquidabilita-debiti.component';
import { TabDisaccoppiatoLiquidabilitaLiquidabileComponent } from './tab-liquidabile/tab-disaccoppiato-liquidabilita-liquidabile.component';


const routes: Routes = [
  {
    path: '',
    component: DisaccoppiatoControlliLiquidiabilitaComponent,
    children: [
      {
        path: StatoIstruttoriaEnum.CONTROLLI_CALCOLO_OK,
        component: TabDisaccoppiatoLiquidabilitaControlliSuperatiComponent,
        data: {
          // mybreadcrumb: StatoIstruttoriaEnum.CONTROLLI_CALCOLO_OK
          mybreadcrumb: 'Controlli superati'
        }
      }, {
        path: StatoIstruttoriaEnum.LIQUIDABILE,
        component: TabDisaccoppiatoLiquidabilitaLiquidabileComponent,
        data: {
          // mybreadcrumb: StatoIstruttoriaEnum.LIQUIDABILE
          mybreadcrumb: 'Non ammissibile'
        }
      }, {
        path: StatoIstruttoriaEnum.CONTROLLI_LIQUIDABILE_KO,
        component: TabDisaccoppiatoLiquidabilitaControlliNonSuperatiComponent,
        data: {
          // mybreadcrumb: StatoIstruttoriaEnum.CONTROLLI_LIQUIDABILE_KO
          mybreadcrumb: 'Controlli non superati'
        }
      },
      {
        path: StatoIstruttoriaEnum.DEBITI,
        component: TabDisaccoppiatoLiquidabilitaDebitiComponent,
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
export class DisaccoppiatoControlliLiquidiabilitaRoutingModule { }
