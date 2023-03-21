import { RegisterCreaFascicoloComponent } from './register-crea-fascicolo.component';
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';


const routes: Routes = [
  {
    path: '',
    component: RegisterCreaFascicoloComponent
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class RegisterCreaFascicoloRoutingModule { }
