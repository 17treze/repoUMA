import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { CardModule } from 'primeng/card';
import { DialogModule } from 'primeng/dialog';
import { InplaceModule } from 'primeng/inplace';
import { A4gCommonModule } from 'src/app/a4g-common/a4g-common.module';
import { AnagraficaFascicoloService } from './anagrafica-fascicolo.service';
import { CreaFascicoloComponent } from './crea-fascicolo/crea-fascicolo.component';
import { ImportaFascicoloComponent } from './crea-fascicolo/importa-fascicolo/importa-fascicolo.component';
import { TabFasAttivitaIvaComponent } from './crea-fascicolo/importa-fascicolo/tabs/tab-fas-attivita-iva/tab-fas-attivita-iva.component';
import { TabFasAziendeCollegateComponent } from './crea-fascicolo/importa-fascicolo/tabs/tab-fas-aziende-collegate/tab-fas-aziende-collegate.component';
import { TabFasDatiIdentificativiComponent } from './crea-fascicolo/importa-fascicolo/tabs/tab-fas-dati-identificativi/tab-fas-dati-identificativi.component';
import { TabFasIscrizioneCciaaComponent } from './crea-fascicolo/importa-fascicolo/tabs/tab-fas-iscrizione-cciaa/tab-fas-iscrizione-cciaa.component';
import { TabFasResidenzaSedeComponent } from './crea-fascicolo/importa-fascicolo/tabs/tab-fas-residenza-sede/tab-fas-residenza-sede.component';
import { TabFasSoggettiCaricaComponent } from './crea-fascicolo/importa-fascicolo/tabs/tab-fas-soggetti-carica/tab-fas-soggetti-carica.component';
import { CreazioneFascicoloRoutingModule } from './creazione-fascicolo-routing.module';
import { CreazioneFascicoloResolver } from './CreazioneFascicoloResolver';
import { FascicoloSharedModule } from '../shared/fascicolo-shared.module';


@NgModule({
  imports: [
    CommonModule,
    CreazioneFascicoloRoutingModule,
    A4gCommonModule,
    InplaceModule,
    DialogModule,
    CardModule,
    FascicoloSharedModule
  ],
  declarations: [
    CreaFascicoloComponent,
    ImportaFascicoloComponent,
    TabFasDatiIdentificativiComponent,
    TabFasResidenzaSedeComponent,
    TabFasAttivitaIvaComponent,
    TabFasIscrizioneCciaaComponent,
    TabFasSoggettiCaricaComponent,
    TabFasAziendeCollegateComponent
  ],
  providers: [
    AnagraficaFascicoloService,
    CreazioneFascicoloResolver
  ]
})
export class CreazioneFascicoloModule { }
