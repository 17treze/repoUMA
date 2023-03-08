import { ImportaFascicoloV2Component } from './../creazione-fascicolo/crea-fascicolo/importa-fascicolo-v2/importa-fascicolo-v2.component';
import { RicercaFascicoliDaMigrareComponent } from './ricerca-fascicoli-da-migrare/ricerca-fascicoli-da-migrare.component';
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { Labels } from 'src/app/app.labels';

const routes: Routes = [
  {
    path: '', component: RicercaFascicoliDaMigrareComponent,
    data: { mybreadcrumb: Labels.fascicoliDaMigrare }
  },
  {
    path: 'cuaa/:cuaa/migra', component: ImportaFascicoloV2Component,
    data: { mybreadcrumb: Labels.migrazioneFascicolo }
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class FascicoliDaMigrareRoutingModule { }
