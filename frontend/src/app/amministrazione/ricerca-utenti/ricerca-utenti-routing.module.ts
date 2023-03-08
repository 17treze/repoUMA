import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { RicercaUtentiComponent } from './ricerca-utenti.component';
import { Labels } from 'src/app/app.labels';
import { StoricoUtenzaComponent } from './storico-utenza/storico-utenza.component';

const routes: Routes = [
    {
        path: '',
        component: RicercaUtentiComponent,
        pathMatch: 'full'
    },
    {
        path: ':idUtenza/storicoUtenza',
        component: StoricoUtenzaComponent,
        data: {
            mybreadcrumb: Labels.STORICO_UTENZE
        }
    }
];

@NgModule({
    imports: [CommonModule, RouterModule.forChild(routes)]
})
export class RicercaUtentiRoutingModule {}
