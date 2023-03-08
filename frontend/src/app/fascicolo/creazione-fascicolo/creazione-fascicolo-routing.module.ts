import { CreaFascicoloComponent } from './crea-fascicolo/crea-fascicolo.component';
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { Labels } from 'src/app/app.labels';
import { ImportaFascicoloV2Component } from './crea-fascicolo/importa-fascicolo-v2/importa-fascicolo-v2.component';
import { FascicoloDettaglioContainerComponent } from '../fascicolo-dettaglio-container/fascicolo-dettaglio-container.component';
import { FascicoloDettaglioContainerModule } from '../fascicolo-dettaglio/fascicolo-dettaglio-container.module';

const routes: Routes = [
  {
    path: '',
    component: CreaFascicoloComponent,
    data: { mybreadcrumb: Labels.creaFascicolo }
  },
  {
    path: 'crea',
    data: { mybreadcrumb: Labels.selezioneCreazioneNuovoFascicolo },
    children: [
      {
        path: '',
        component: CreaFascicoloComponent
      },
      {
        path: ':cuaa',
        component: ImportaFascicoloV2Component,
        data: { mybreadcrumb: Labels.creazioneNuovoFascicolo }
      }
    ]
  },
  {
    path: 'costituisci-trasferisci',
    data: { mybreadcrumb: Labels.selezioneCostituisciTrasferisciFascicolo },
    children: [
      {
        path: '',
        component: CreaFascicoloComponent
      },
      {
        path: ':cuaa',
        component: ImportaFascicoloV2Component,
        data: { mybreadcrumb: Labels.costituisciTrasferisciFascicolo }
      }
    ]
  },
  {
    path: 'trasferisci',
    data: { mybreadcrumb: Labels.selezioneTrasferisciFascicolo },
    children: [
      {
        path: '',
        component: CreaFascicoloComponent
      },
      {
        path: ':cuaa',
        component: ImportaFascicoloV2Component,
        data: { mybreadcrumb: Labels.trasferisciFascicolo }
      }
    ]
  },
  {
    path: 'cuaa/:cuaa/dettaglio',
    component: FascicoloDettaglioContainerComponent,
    data: {
      breadcrumbTranslateKey: "FAS_ANA.FASCICOLO_AZIENDALE",
      mybreadcrumb: Labels.FASCICOLO_AZIENDALE
    },
    loadChildren: () => import('../fascicolo-dettaglio/fascicolo-dettaglio.module').then(m => m.FascicoloDettaglioModule)
  }
];

@NgModule({
  imports: [
    FascicoloDettaglioContainerModule,
    RouterModule.forChild(routes)
  ],
  exports: [
    RouterModule
  ]
})
export class CreazioneFascicoloRoutingModule { }
