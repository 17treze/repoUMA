import { NgModule, Type } from '@angular/core';
import { CommonModule, registerLocaleData } from '@angular/common';
import { UmaRoutingModule } from './uma-routing.module';
import localeIt from '@angular/common/locales/it';
import { A4gCommonModule } from '../a4g-common/a4g-common.module';
import { RichiedenteUmaComponent } from './features/richiedente-uma/richiedente-uma.component';
import { CoreUmaModule } from './core-uma/core-uma.module';
import { GestioneDomandaUmaComponent } from './features/gestione-domanda-uma/gestione-domanda-uma.component';
import { HomeUmaComponent } from './features/home-uma/home-uma.component';
import { GestioneDichiarazioneConsumiComponent } from './features/gestione-dichiarazione-consumi/gestione-dichiarazione-consumi.component';
import { SezioniDichiarazioneConsumiComponent } from './features/gestione-dichiarazione-consumi/sezioni-dichiarazione-consumi/sezioni-dichiarazione-consumi.component';
import { ConsumiComponent } from './features/gestione-dichiarazione-consumi/sezioni-dichiarazione-consumi/consumi/consumi.component';
import { PrelieviComponent } from './features/gestione-dichiarazione-consumi/sezioni-dichiarazione-consumi/prelievi/prelievi.component';
import { RicevutiComponent } from './features/gestione-dichiarazione-consumi/sezioni-dichiarazione-consumi/ricevuti/ricevuti.component';
import { PopupRicercaClientiContoterziComponent } from './features/gestione-dichiarazione-consumi/sezioni-dichiarazione-consumi/clienti-conto-terzi/popup-ricerca-clienti-contoterzi/popup-ricerca-clienti-contoterzi.component';
import { DettaglioClientiContoterziComponent } from './features/gestione-dichiarazione-consumi/sezioni-dichiarazione-consumi/clienti-conto-terzi/clienti-conto-terzi-container/dettaglio-clienti-contoterzi/dettaglio-clienti-contoterzi.component';
import { ClientiContoTerziContainerComponent } from './features/gestione-dichiarazione-consumi/sezioni-dichiarazione-consumi/clienti-conto-terzi/clienti-conto-terzi-container/clienti-conto-terzi-container.component';
import { ClientiContoTerziComponent } from './features/gestione-dichiarazione-consumi/sezioni-dichiarazione-consumi/clienti-conto-terzi/clienti-conto-terzi-container/lista-clienti-conto-terzi/clienti-conto-terzi.component';
import { PopupCancellaClienteContoterziComponent } from './features/gestione-dichiarazione-consumi/sezioni-dichiarazione-consumi/clienti-conto-terzi/popup-cancella-cliente-contoterzi/popup-cancella-cliente-contoterzi.component';
import { PopupAllegatiClientiContoterziComponent } from './features/gestione-dichiarazione-consumi/sezioni-dichiarazione-consumi/clienti-conto-terzi/popup-allegati-clienti-contoterzi/popup-allegati-clienti-contoterzi.component';
import { LavorazioniFabbricatiComponent } from './features/gestione-domanda-uma/indice-domanda-uma/lavorazioni-indice/lavorazioni-fabbricati/lavorazioni-fabbricati.component';
import { PopupAllegatiAllegatiMotivoComponent } from './features/gestione-dichiarazione-consumi/sezioni-dichiarazione-consumi/consumi/popup-allegati-motivo-consumi/popup-allegati-motivo-consumi.component';
import { LavorazioniIndiceComponent } from './features/gestione-domanda-uma/indice-domanda-uma/lavorazioni-indice/lavorazioni-indice.component';
import { IndiceDomandaUmaComponent } from './features/gestione-domanda-uma/indice-domanda-uma/indice-domanda-uma.component';
import { ProtocollazioneComponent } from './features/gestione-domanda-uma/indice-domanda-uma/protocollazione/protocollazione.component';
import { FabbisognoComponent } from './features/gestione-domanda-uma/indice-domanda-uma/fabbisogno/fabbisogno.component';
import { LavorazioniAltreComponent } from './features/gestione-domanda-uma/indice-domanda-uma/lavorazioni-indice/lavorazioni-altre/lavorazioni-altre.component';
import { LavorazioniSuperficieComponent } from './features/gestione-domanda-uma/indice-domanda-uma/lavorazioni-indice/lavorazioni-superficie/lavorazioni-superficie.component';
import { LavorazioniZootecniaComponent } from './features/gestione-domanda-uma/indice-domanda-uma/lavorazioni-indice/lavorazioni-zootecnia/lavorazioni-zootecnia.component';
import { PopupMotivoRecuperoComponent } from './features/gestione-dichiarazione-consumi/sezioni-dichiarazione-consumi/consumi/popup-motivo-recupero/popup-motivo-recupero.component';
import { PopupProtocollazioneDichiarazioneComponent } from './features/gestione-dichiarazione-consumi/sezioni-dichiarazione-consumi/popup-protocollazione-dichiarazione/popup-protocollazione-dichiarazione.component';
import { GestioneEsuberoCarburanteComponent } from './features/gestione-esubero-carburante/gestione-esubero-carburante.component';
import { TrasferimentiComponent } from './features/gestione-dichiarazione-consumi/sezioni-dichiarazione-consumi/trasferimenti/trasferimenti.component';
import { GestioneTrasferimentiComponent } from './features/gestione-esubero-carburante/gestione-trasferimenti/gestione-trasferimenti.component';
import { PopupInserisciEditaTrasferimentoComponent } from './features/gestione-esubero-carburante/popup-inserisci-edita-trasferimento/popup-inserisci-edita-trasferimento.component';
import { PopupRicercaRichiestaCarburanteComponent } from './features/gestione-esubero-carburante/popup-ricerca-richiesta-carburante/popup-ricerca-richiesta-carburante.component';
import { MacchineComponent } from './features/gestione-domanda-uma/indice-domanda-uma/macchine/macchine.component';

const MODULES: Array<Type<any>>  = [
  CommonModule,
  A4gCommonModule,
  UmaRoutingModule,
  CoreUmaModule];

const COMPONENTS: Array<Type<any>> = [
  LavorazioniSuperficieComponent,
  LavorazioniZootecniaComponent,
  RichiedenteUmaComponent,
  GestioneDomandaUmaComponent,
  HomeUmaComponent,
  FabbisognoComponent,
  LavorazioniAltreComponent,
  GestioneDichiarazioneConsumiComponent,
  SezioniDichiarazioneConsumiComponent,
  PrelieviComponent,
  ClientiContoTerziComponent,
  RicevutiComponent,
  TrasferimentiComponent,
  GestioneTrasferimentiComponent,
  ConsumiComponent,
  PopupRicercaClientiContoterziComponent,
  PopupCancellaClienteContoterziComponent,
  PopupAllegatiClientiContoterziComponent,
  PopupAllegatiAllegatiMotivoComponent,
  PopupMotivoRecuperoComponent,
  PopupInserisciEditaTrasferimentoComponent,
  PopupRicercaRichiestaCarburanteComponent,
  DettaglioClientiContoterziComponent,
  ClientiContoTerziContainerComponent,
  LavorazioniFabbricatiComponent,
  IndiceDomandaUmaComponent,
  LavorazioniIndiceComponent,
  ProtocollazioneComponent,
  PopupProtocollazioneDichiarazioneComponent,
  GestioneEsuberoCarburanteComponent,
  MacchineComponent
];

const ENTRY_COMPONENTS: Array<Type<any>> = [];

const PIPES: Array<Type<any>> = [];

const DIRECTIVES: Array<Type<any>> = [];

const PROVIDERS: Array<Type<any>> = [];

const SERVICES: Array<Type<any>> = [];

@NgModule({
  imports: [
    ...MODULES
  ],
  declarations: [
    ...ENTRY_COMPONENTS,
    ...COMPONENTS,
    ...PIPES,
    ...DIRECTIVES
  ],
  entryComponents: [
    ...ENTRY_COMPONENTS
  ],
  providers: [
    ...SERVICES,
    ...PROVIDERS
  ],
  exports: [
    ...MODULES,
    ...COMPONENTS,
    ...ENTRY_COMPONENTS,
    ...PIPES,
    ...DIRECTIVES
  ],
})
export class UmaModule {
  constructor() {
    registerLocaleData(localeIt, 'it');
  }
}
