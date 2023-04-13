import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { DettaglioDomandaUmaComponent } from './dettaglio-domanda-uma/dettaglio-domanda-uma.component';
import { DomandeUmaComponent } from './domande-uma.component';

const routes: Routes = [
  {
    path: ':cuaa',
    component: DomandeUmaComponent,
    data: {
      breadcrumb: 'Cruscotto',
      hideHeaderIcons: true,
    },
  },
  {
    path: ':idDomanda/dettaglio',
    component: DettaglioDomandaUmaComponent,
    data: {
      breadcrumb: 'Richiesta carburante',
      hideHeaderIcons: true,
    },
  },
];
@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class DomandeUmaRoutingModule {}
