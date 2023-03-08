import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { DisaccoppiatoCalcoloPremioComponent } from './disaccoppiato-calcolo-premio.component';
import { StatoIstruttoriaEnum } from '../../cruscotto-sostegno/StatoIstruttoriaEnum';
import { TabDisaccoppiatoCalcoloRichiestoComponent } from './tab-richiesto/tab-disaccoppiato-calcolo-richiesto.component';
import { TabDisaccoppiatoCalcoloControlliSuperatiComponent } from './tab-controlli-superati/tab-disaccoppiato-calcolo-controlli-superati.component';
import { TabDisaccoppiatoCalcoloControlliNonSuperatiComponent } from './tab-controlli-non-superati/tab-disaccoppiato-calcolo-controlli-non-superati.component';
import { TabDisaccoppiatoCalcoloNonAmmissibileComponent } from './tab-non-ammissibile/tab-disaccoppiato-calcolo-non-ammissibile.component';


const routes: Routes = [
  {
    path: '',
    component: DisaccoppiatoCalcoloPremioComponent,
    children: [
      {
        path: StatoIstruttoriaEnum.RICHIESTO,
        component: TabDisaccoppiatoCalcoloRichiestoComponent,
        data: {
          // mybreadcrumb: StatoIstruttoriaEnum.RICHIESTO
          mybreadcrumb: 'Richiesto'
        }
      }, {
        path: StatoIstruttoriaEnum.CONTROLLI_CALCOLO_OK,
        component: TabDisaccoppiatoCalcoloControlliSuperatiComponent,
        data: {
          // mybreadcrumb: StatoIstruttoriaEnum.CONTROLLI_CALCOLO_OK
          mybreadcrumb: 'Controlli superati'
        }
      }, {
        path: StatoIstruttoriaEnum.CONTROLLI_CALCOLO_KO,
        component: TabDisaccoppiatoCalcoloControlliNonSuperatiComponent,
        data: {
          // mybreadcrumb: StatoIstruttoriaEnum.CONTROLLI_CALCOLO_KO
          mybreadcrumb: 'Controlli non superati'
        }
      }, {
        path: StatoIstruttoriaEnum.NON_AMMISSIBILE,
        component: TabDisaccoppiatoCalcoloNonAmmissibileComponent,
        data: {
          // mybreadcrumb: StatoIstruttoriaEnum.NON_AMMISSIBILE
          mybreadcrumb: 'Non ammissibile'
        }
      }, { path: '',
        redirectTo: StatoIstruttoriaEnum.RICHIESTO,
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
export class DisaccoppiatoCalcoloPremioRoutingModule { }
