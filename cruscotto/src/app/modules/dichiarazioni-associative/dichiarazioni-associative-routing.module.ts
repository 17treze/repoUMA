import { DichiarazioniAssociativeContainerComponent } from './dichiarazioni-associative-container/dichiarazioni-associative-container.component';
import { DichiarazioniAssociativeListComponent } from './dichiarazioni-associative-list/dichiarazioni-associative-list.component';
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';


const routes: Routes = [
  {
    path: ':cuaa',
    component: DichiarazioniAssociativeContainerComponent,
    children: [
      { path: '', pathMatch: 'full', redirectTo: 'lista' },
      {
        path: 'lista',
        component: DichiarazioniAssociativeListComponent,
        data: {
          breadcrumb: 'Cruscotto'
        }
      }
    ]
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class DichiarazioniAssociativeRoutingModule { }
