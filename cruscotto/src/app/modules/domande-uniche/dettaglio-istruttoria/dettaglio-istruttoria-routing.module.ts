import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { EsitiIstruttoriaComponent } from './esiti-istruttoria/esiti-istruttoria.component';
import { EsitiIstruttoriaSuperficieComponent } from './esiti-istruttoria-superficie/esiti-istruttoria-superficie.component';
import { EsitiIstruttoriaZootecniaComponent } from './esiti-istruttoria-zootecnia/esiti-istruttoria-zootecnia.component';

const routes: Routes = [
    {
        path: ':idIstruttoria/esitiIstruttoria',
        component: EsitiIstruttoriaComponent,
      },
      {
        path: ':idIstruttoria/esitiIstruttoriaSuperficie',
        component: EsitiIstruttoriaSuperficieComponent,
      },
      {
        path: ':idIstruttoria/esitiIstruttoriaZootecnia',
        component: EsitiIstruttoriaZootecniaComponent,
      }
];

@NgModule({
  imports: [RouterModule.forChild(routes)]
})
export class DettaglioIstruttoriaRoutingModule {

}
