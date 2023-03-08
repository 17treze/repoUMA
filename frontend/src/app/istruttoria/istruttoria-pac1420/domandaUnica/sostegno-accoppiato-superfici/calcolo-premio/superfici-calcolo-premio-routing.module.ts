import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { SuperficiCalcoloPremioComponent } from './superfici-calcolo-premio.component';
import { StatoIstruttoriaEnum } from '../../cruscotto-sostegno/StatoIstruttoriaEnum';
import { TabSuperficiCalcoloRichiestoComponent } from './tab-richiesto/tab-superfici-calcolo-richiesto.component';
import { TabSuperficiCalcoloControlliSuperatiComponent } from './tab-controlli-superati/tab-superfici-calcolo-controlli-superati.component';
import { TabSuperficiCalcoloControlliNonSuperatiComponent } from './tab-controlli-non-superati/tab-superfici-calcolo-controlli-non-superati.component';
import { TabSuperficiCalcoloNonAmmissibileComponent } from './tab-non-ammissibile/tab-superfici-calcolo-non-ammissibile.component';


const routes: Routes = [
  {
    path: '',
    component: SuperficiCalcoloPremioComponent,
    children: [
      {
        path: StatoIstruttoriaEnum.RICHIESTO,
        component: TabSuperficiCalcoloRichiestoComponent,
        data: {
          // mybreadcrumb: StatoIstruttoriaEnum.RICHIESTO
          mybreadcrumb: 'Richiesto'
        }
      }, {
        path: StatoIstruttoriaEnum.CONTROLLI_CALCOLO_OK,
        component: TabSuperficiCalcoloControlliSuperatiComponent,
        data: {
          // mybreadcrumb: StatoIstruttoriaEnum.CONTROLLI_CALCOLO_OK
          mybreadcrumb: 'Controlli superati'
        }
      }, {
        path: StatoIstruttoriaEnum.CONTROLLI_CALCOLO_KO,
        component: TabSuperficiCalcoloControlliNonSuperatiComponent,
        data: {
          // mybreadcrumb: StatoIstruttoriaEnum.CONTROLLI_CALCOLO_KO
          mybreadcrumb: 'Controlli non superati'
        }
      }, {
        path: StatoIstruttoriaEnum.NON_AMMISSIBILE,
        component: TabSuperficiCalcoloNonAmmissibileComponent,
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
export class SuperficiCalcoloPremioRoutingModule { }
