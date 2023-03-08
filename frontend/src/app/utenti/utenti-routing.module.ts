import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { UtenteComponent } from './utente/utente.component';
import { UserGuard } from '../auth/user.guard';
import { UtenteGuard } from '../auth/utente.guard';

const routes: Routes = [
  {
    path: '', component: UtenteComponent, canActivate: [UserGuard]
  },
  {
    path: "modificaUtente", component: UtenteComponent , canActivate: [UtenteGuard]
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)]
})
export class UtentiRoutingModule { }
