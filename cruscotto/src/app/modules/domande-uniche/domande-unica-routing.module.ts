import { ListaDomandeComponent } from './lista-domande/lista-domande.component';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ContainerDomandeUnicheComponent } from "./container-domande-uniche/container-domande-uniche.component";
import { ContainerDettaglioDomandaUnicaComponent } from './container-dettaglio-domanda-unica/container-dettaglio-domanda-unica.component';
import { DomandaAntimafiaComponent } from './domanda-antimafia/domanda-antimafia.component';

const routes: Routes = [
  {
    path: ':cuaa',
    component: ContainerDomandeUnicheComponent,
    children: [
      { path: '', pathMatch: 'full', redirectTo: 'lista' },
      {
        path: 'lista', 
        component: ListaDomandeComponent,
        data: {
          breadcrumb: 'Cruscotto'
        }
      },
      { path: 'antimafia', component: DomandaAntimafiaComponent }
    ]
  },
  {
    path: ':idDomanda/dettaglioDomandaUnica',
    component: ContainerDettaglioDomandaUnicaComponent,
    data: {
      breadcrumb: 'Domanda Unica'
    }
  },
  {
    path: ':idDomanda/dettaglioDomandaUnica/istruttoria',
    loadChildren: () => import('./dettaglio-istruttoria/dettaglio-istruttoria.module').then(m => m.DettaglioIstruttoriaModule),
    data: {
      breadcrumb: 'Dettaglio Istruttoria'
    }
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)]
})
export class DomandeUnicaRoutingModule {

}
