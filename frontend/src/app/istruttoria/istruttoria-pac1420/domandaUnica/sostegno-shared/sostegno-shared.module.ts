import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { A4gCommonModule } from 'src/app/a4g-common/a4g-common.module';
import { ProcessiDiControlloComponent } from './processi-di-controllo/processi-di-controllo.component';
import { ConfigurazioneIstruttoriaComponent } from './configurazione-istruttoria/configurazione-istruttoria.component';
import { IstruttoriaDisaccoppiatoComponent } from './configurazione-istruttoria/tabs/istruttoria-disaccoppiato/istruttoria-disaccoppiato.component';
import { IstruttoriaAczComponent } from './configurazione-istruttoria/tabs/istruttoria-acz/istruttoria-acz.component';
import { IstruttoriaAcsComponent } from './configurazione-istruttoria/tabs/istruttoria-acs/istruttoria-acs.component';
import { TabellaInterventiAczComponent } from './configurazione-istruttoria/tabs/istruttoria-acz/tabella/tabella-interventi-acz.compontent';
import { RicevibilitaComponent } from './configurazione-istruttoria/tabs/ricevibilita/ricevibilita.component';
import { SostegnoSharedRoutingModule } from './sostegno-shared-routing.module';
import { StatoDomandeRicevibilitaComponent } from './configurazione-istruttoria/tabs/ricevibilita/stato-domande-ricevibilita/stato-domande-ricevibilita.component';
import { ElencoDomandeComponent } from './elenco-domande/elenco-domande.component';
import { DialogModule } from 'primeng/dialog';

@NgModule({
  imports: [
    CommonModule,
    A4gCommonModule,
    SostegnoSharedRoutingModule,
    DialogModule
  ],
  declarations: [
    ProcessiDiControlloComponent,
    ConfigurazioneIstruttoriaComponent,
    RicevibilitaComponent,
    StatoDomandeRicevibilitaComponent,
    IstruttoriaDisaccoppiatoComponent,
    IstruttoriaAczComponent,
    IstruttoriaAcsComponent,
    ElencoDomandeComponent,
    TabellaInterventiAczComponent
  ],
  exports: [
    ProcessiDiControlloComponent,
    DialogModule
  ]
})
export class SostegnoSharedModule { }
