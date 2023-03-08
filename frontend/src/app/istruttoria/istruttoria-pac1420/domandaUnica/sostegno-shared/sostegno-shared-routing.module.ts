import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ConfigurazioneIstruttoriaComponent } from './configurazione-istruttoria/configurazione-istruttoria.component';
import { IstruttoriaDisaccoppiatoComponent } from './configurazione-istruttoria/tabs/istruttoria-disaccoppiato/istruttoria-disaccoppiato.component';
import { IstruttoriaAczComponent } from './configurazione-istruttoria/tabs/istruttoria-acz/istruttoria-acz.component';
import { IstruttoriaAcsComponent } from './configurazione-istruttoria/tabs/istruttoria-acs/istruttoria-acs.component';
import { RicevibilitaComponent } from './configurazione-istruttoria/tabs/ricevibilita/ricevibilita.component';
import { ElencoDomandeComponent } from './elenco-domande/elenco-domande.component';

const routes: Routes = [
    {
        path: '',
        data: { mybreadcrumb: '' },
        component: ConfigurazioneIstruttoriaComponent,
        children: [
            { path: '', redirectTo: 'ricevibilita', pathMatch: 'full' },
            { path: 'ricevibilita', component: RicevibilitaComponent },
            { path: 'istruttoriaDisaccoppiato', component: IstruttoriaDisaccoppiatoComponent },
            { path: 'istruttoriaAcz', component: IstruttoriaAczComponent },
            { path: 'istruttoriaAcs', component: IstruttoriaAcsComponent }
        ]
    },
    {
        path: 'ricevibilita/domande/:statoDomanda',
        component: ElencoDomandeComponent,
        data: { mybreadcrumb: 'Domande' },
    }
];

@NgModule({
    imports: [CommonModule, RouterModule.forChild(routes)]
})
export class SostegnoSharedRoutingModule { }
