import { NgModule } from '@angular/core';
import { A4gCommonModule } from 'src/app/a4g-common/a4g-common.module';
import { ModificaConfigurazioneUtenteDialogComponent } from './modifica-configurazione-utente-dialog/modifica-configurazione-utente-dialog.component';
import { TabViewModule } from 'primeng/tabview';
import { DialogModule } from 'primeng/dialog';
import { ScrollPanelModule } from 'primeng/scrollpanel';
import { InputTextModule } from 'primeng/inputtext';
import { RadioButtonModule} from 'primeng/radiobutton';
import { RicercaUtentiRoutingModule } from './ricerca-utenti-routing.module';
import { RicercaUtentiComponent } from './ricerca-utenti.component';
import { GestioneUtenzeModule } from '../gestione-utenze/gestione-utenze.module';
import { StoricoUtenzaComponent } from './storico-utenza/storico-utenza.component';

@NgModule({
    imports: [
        A4gCommonModule,
        TabViewModule,
        DialogModule,
        ScrollPanelModule,
        InputTextModule,
        RicercaUtentiRoutingModule,
        GestioneUtenzeModule,
        RadioButtonModule
    ], declarations: [
        ModificaConfigurazioneUtenteDialogComponent,
        RicercaUtentiComponent,
        StoricoUtenzaComponent
    ]
})
export class RicercaUtentiModule { }
