import { NgModule } from '@angular/core';
import { A4gCommonModule } from 'src/app/a4g-common/a4g-common.module';
import { RichiesteAccessoSistemaComponent } from './richieste-accesso-sistema/richieste-accesso-sistema.component';
import { GestioneUtenzeRoutingModule } from './gestione-utenze-routing.module';
import { TabViewModule } from 'primeng/tabview';
import { ScrollPanelModule } from 'primeng/scrollpanel';
import { DialogModule } from 'primeng/dialog';
import { RichiesteProtocollateComponent } from './richieste-accesso-sistema/tabs/richieste-protocollate/richieste-protocollate.component';
import { RichiesteInLavorazioneComponent } from './richieste-accesso-sistema/tabs/richieste-in-lavorazione/richieste-in-lavorazione.component';
import { RichiesteApprovateComponent } from './richieste-accesso-sistema/tabs/richieste-approvate/richieste-approvate.component';
import { RichiesteRifiutateComponent } from './richieste-accesso-sistema/tabs/richieste-rifiutate/richieste-rifiutate.component';
import { DettaglioUtenzaComponent } from './dettaglio-utenza/dettaglio-utenza.component';
import { GestioneUtenzeService } from './gestione-utenze.service';
import { AssegnazioneConfigurazioniDialogComponent } from './assegnazione-configurazioni-dialog/assegnazione-configurazioni-dialog.component';
import { ApprovazioneDialogComponent } from './approvazione-dialog/approvazione-dialog.component';
import { RifiutoDialogComponent } from './rifiuto-dialog/rifiuto-dialog.component';
import { MotivazioneApprovazioneDialogComponent } from './richieste-accesso-sistema/motivazione-approvazione-dialog/motivazione-approvazione-dialog.component';
import { MotivazioneRifiutoDialogComponent } from './motivazione-rifiuto-dialog/motivazione-rifiuto-dialog.component';
import {CalendarModule} from 'primeng/calendar';
import {RadioButtonModule} from 'primeng/radiobutton';

@NgModule({
  imports: [
    A4gCommonModule,
    GestioneUtenzeRoutingModule,
    TabViewModule,
    DialogModule,
    ScrollPanelModule,
    CalendarModule,
    RadioButtonModule
  ],
  declarations: [
    RichiesteAccessoSistemaComponent,
    RichiesteProtocollateComponent,
    RichiesteInLavorazioneComponent,
    RichiesteApprovateComponent,
    RichiesteRifiutateComponent,
    DettaglioUtenzaComponent,
    AssegnazioneConfigurazioniDialogComponent,
    ApprovazioneDialogComponent,
    MotivazioneApprovazioneDialogComponent,
    RifiutoDialogComponent,
    MotivazioneRifiutoDialogComponent
  ],
  providers: [
    GestioneUtenzeService
  ]

})
export class GestioneUtenzeModule {
}
