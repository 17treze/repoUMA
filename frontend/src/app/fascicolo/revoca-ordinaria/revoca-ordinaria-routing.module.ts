import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { RevocaOrdinariaComponent } from './revoca-ordinaria.component';
import { Labels } from 'src/app/app.labels';
import { ImportaFascicoloV2Component } from '../creazione-fascicolo/crea-fascicolo/importa-fascicolo-v2/importa-fascicolo-v2.component';

const routes: Routes = [
  {
    path: '', component: RevocaOrdinariaComponent, data: { mybreadcrumb: Labels.revocaOrdinaria }
  },
  {
    path: ':cuaa', component: ImportaFascicoloV2Component,
    data: { mybreadcrumb: Labels.revocaMandato }
  }


];

@NgModule({
  imports: [RouterModule.forChild(routes)]
})
export class RevocaOrdinariaRoutingModule { }


