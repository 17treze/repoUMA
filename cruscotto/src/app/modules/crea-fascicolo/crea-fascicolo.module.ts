import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CreaFascicoloComponent } from './crea-fascicolo.component';
import { RouterModule, Routes } from '@angular/router';
import { SharedModule } from '../../../app/shared/shared.module';

const routes: Routes = [
  {
    path: '',
    component: CreaFascicoloComponent
  }, {
    path: ':cuaa',
    component: CreaFascicoloComponent
  }
];
export const CreaFascicoloRoutes = RouterModule.forChild(routes);


@NgModule({
  imports: [
    RouterModule.forChild(routes),
    CommonModule,
    SharedModule
  ],
  declarations: [CreaFascicoloComponent],
  exports: [
    CreaFascicoloComponent
  ]
})
export class CreaFascicoloModule { }
