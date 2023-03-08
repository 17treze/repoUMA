import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { Labels } from 'src/app/app.labels';
import { MandatoComponent } from './mandato.component';

const routes: Routes = [
  {
    path: '', component: MandatoComponent, data: { mybreadcrumb: Labels.revocaMandato }
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
})
export class MandatoRoutingModule { }
