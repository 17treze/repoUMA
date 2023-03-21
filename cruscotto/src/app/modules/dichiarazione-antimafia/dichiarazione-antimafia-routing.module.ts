import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { DichiarazioneAntimafiaComponent } from './dichiarazione-antimafia.component';

const routes: Routes = [
  {
    path: ':cuaa',
    component: DichiarazioneAntimafiaComponent,
    data: {
      breadcrumb: 'Cruscotto',
      hideHeaderIcons: true,
    },
  },
];
@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class DichiarazioneAntimafiaRoutingModule {}
