import { NgModule, Component } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { Labels } from '../app.labels';
import { ExternalWindowComponent } from './components/gis/external-window/external-window.component';
import { GisComponent } from './components/gis/gis.component';
import { CrmGuard } from '../auth/crm.guard';

const routes: Routes = [
  {
    path: 'gis', component: GisComponent,
    data: { mybreadcrumb: Labels.breadcrumbModificaSuolo },
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)]
})
export class GisRoutingModule { }
