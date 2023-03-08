import { ImportaFascicoloV2Component } from './../creazione-fascicolo/crea-fascicolo/importa-fascicolo-v2/importa-fascicolo-v2.component';
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { Labels } from 'src/app/app.labels';
import { RicercaFascicoloDaRiaprireComponent } from './ricerca-fascicolo-da-riaprire/ricerca-fascicolo-da-riaprire.component';

const routes: Routes = [
  {
    path: '', component: RicercaFascicoloDaRiaprireComponent,
    data: { mybreadcrumb: Labels.fascicoloDaRiaprire }
  },
  {
    path: 'cuaa/:cuaa/riapri', component: ImportaFascicoloV2Component,
    data: { mybreadcrumb: Labels.riaperturaFascicolo }
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class RiaperturaFascicoloRoutingModule { }
