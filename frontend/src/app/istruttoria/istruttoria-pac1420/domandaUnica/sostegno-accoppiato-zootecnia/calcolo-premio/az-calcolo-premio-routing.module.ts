import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { AzCalcoloPremioComponent } from './az-calcolo-premio.component';
import { StatoIstruttoriaEnum } from '../../cruscotto-sostegno/StatoIstruttoriaEnum';
import { TabAzPRichiestoComponent } from './tab-az-p-richiesto/tab-az-p-richiesto.component';
import { TabAzPIntegrazioniAmmissibilitaComponent } from './tab-az-p-integrazioni-ammissibilita/tab-az-p-integrazioni-ammissibilita.component';
import { TabAzPNonAmmissibiliComponent } from './tab-az-p-non-ammissibili/tab-az-p-non-ammissibili.component';
import { TabAzPControlliSuperatiComponent } from './tab-az-p-controlli-superati/tab-az-p-controlli-superati.component';
import { TabAzPControlliNonSuperatiComponent } from './tab-az-p-controlli-non-superati/tab-az-p-controlli-non-superati.component';

const routes: Routes = [
  {
    path: '',
    component: AzCalcoloPremioComponent,
    children: [
      {
        path: StatoIstruttoriaEnum.RICHIESTO,
        component: TabAzPRichiestoComponent,
        data: {
          // mybreadcrumb: StatoIstruttoriaEnum.RICHIESTO
          mybreadcrumb: 'Richiesto'
        }
      }, {
        path: StatoIstruttoriaEnum.INTEGRATO,
        component: TabAzPIntegrazioniAmmissibilitaComponent,
        data: {
          // mybreadcrumb: StatoIstruttoriaEnum.INTEGRATO
          mybreadcrumb: 'Integrato'
        }
      }, {
        path: StatoIstruttoriaEnum.CONTROLLI_CALCOLO_OK,
        component: TabAzPControlliSuperatiComponent,
        data: {
          // mybreadcrumb: StatoIstruttoriaEnum.CONTROLLI_CALCOLO_OK
          mybreadcrumb: 'Controlli superati'
        }
      }, {
        path: StatoIstruttoriaEnum.CONTROLLI_CALCOLO_KO,
        component: TabAzPControlliNonSuperatiComponent,
        data: {
          // mybreadcrumb: StatoIstruttoriaEnum.CONTROLLI_CALCOLO_KO
          mybreadcrumb: 'Controlli non superati'
        }
      }, {
        path: StatoIstruttoriaEnum.NON_AMMISSIBILE,
        component: TabAzPNonAmmissibiliComponent,
        data: {
          // mybreadcrumb: StatoIstruttoriaEnum.NON_AMMISSIBILE
          mybreadcrumb: 'Non ammissibile'
        }
      }, {
        path: '',
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
export class AzCalcoloPremioRoutingModule { }
