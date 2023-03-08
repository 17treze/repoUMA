import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {Labels} from "../../app.labels";
import {
  RicercaFascicoloPerAcquisizioneMandatoComponent
} from "./ricerca-fascicolo-per-acquisizione-mandato/ricerca-fascicolo-per-acquisizione-mandato.component";
import {
  ImportaFascicoloV2Component
} from "../creazione-fascicolo/crea-fascicolo/importa-fascicolo-v2/importa-fascicolo-v2.component";


const routes: Routes = [
  {
    path: '', component: RicercaFascicoloPerAcquisizioneMandatoComponent,
    data: { mybreadcrumb: Labels.fascicoloDaRiaprire }
  },
  {
    path: 'cuaa/:cuaa/acquisizione-mandato', component: ImportaFascicoloV2Component,
    data: { mybreadcrumb: Labels.acquisizioneMandato }
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class RevocaImmediataAcquisizioneMandatoRoutingModule { }
