import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { RichiesteAccessoSistemaComponent } from './richieste-accesso-sistema/richieste-accesso-sistema.component';
import { DettaglioUtenzaComponent } from './dettaglio-utenza/dettaglio-utenza.component';
import { Labels } from '../../app.labels';

const routes: Routes = [
    {
        path: '',
        component: RichiesteAccessoSistemaComponent,
        pathMatch: 'full',
    },
    {
        path: ':idUtenza/dettaglioUtenza',
        component: DettaglioUtenzaComponent,
        data: {
            mybreadcrumb: Labels.DETTAGLIO_UTENZE
        }
    }
];

@NgModule({
    imports: [CommonModule, RouterModule.forChild(routes)]
})
export class GestioneUtenzeRoutingModule { }
