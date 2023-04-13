import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FascicoloDettaglioRoutingModule } from './fascicolo-dettaglio-routing.module';
import { ContainerFascicoloDettaglioComponent } from './container-fascicolo-dettaglio/container-fascicolo-dettaglio.component';
import { DatiAziendaComponent } from './dati-azienda/dati-azienda.component';
import { SharedModule } from 'src/app/shared/shared.module';
import { AccordionModule } from 'primeng-lts/accordion';
import { A4gAccordionTabComponent } from './a4g-accordion-tab/a4g-accordion-tab.component';
import { A4gShowMoreShowLessComponent } from './a4g-show-more-show-less/a4g-show-more-show-less.component';
import { PopupSchedaValidazioneComponent } from './popup-scheda-validazione/popup-scheda-validazione.component';
import { PopupSchedaValidazioneInProprioComponent } from './popup-scheda-validazione-in-proprio/popup-scheda-validazione-in-proprio.component';
import { DatiAnagrafeTributariaComponent } from './dati-azienda/dati-anagrafe-tributaria/dati-anagrafe-tributaria.component';
import { DatiCameraCommercioComponent } from '../fascicolo-dettaglio/dati-azienda/dati-camera-commercio/dati-camera-commercio.component';
import { RiepilogoFascicoloDettaglioComponent } from './riepilogo-fascicolo-dettaglio/riepilogo-fascicolo-dettaglio.component';
import { PersoneCaricaComponent } from './dati-azienda/persone-carica/persone-carica.component';
import { UnitaTecnicoEconomicheComponent } from './dati-azienda/unita-tecnico-economiche/unita-tecnico-economiche.component';
import { A4gAccordionTabExtComponent } from './a4g-accordion-tab-ext/a4g-accordion-tab-ext.component';
import { ModalitaPagamentoComponent } from './dati-azienda/modalita-pagamento/modalita-pagamento.component';
import { PopupSelezioneFirmatarioComponent } from './popup-selezione-firmatario/popup-selezione-firmatario.component';
import {ListboxModule} from 'primeng-lts/listbox';
import { PopupControlloCompletezzaComponent } from './popup-controllo-completezza/popup-controllo-completezza.component';
import { FascicoliValidatiComponent } from './fascicoli-validati/fascicoli-validati.component';
import { FiltroFascicoliValidatiComponent } from './fascicoli-validati/fascicoli-validati-filter/fascicoli-validati-filter.component';

@NgModule({
  declarations: [
    ContainerFascicoloDettaglioComponent,
    RiepilogoFascicoloDettaglioComponent,
    DatiAziendaComponent,
    A4gAccordionTabComponent,
    A4gShowMoreShowLessComponent,
    PopupSchedaValidazioneComponent,
    PopupSchedaValidazioneInProprioComponent,
    PopupSelezioneFirmatarioComponent,
    DatiAnagrafeTributariaComponent,
    DatiCameraCommercioComponent,
    PersoneCaricaComponent,
    UnitaTecnicoEconomicheComponent,
    A4gAccordionTabExtComponent,
    ModalitaPagamentoComponent,
    PopupControlloCompletezzaComponent,
    FascicoliValidatiComponent,
    FiltroFascicoliValidatiComponent
  ], imports: [
    SharedModule,
    CommonModule,
    FascicoloDettaglioRoutingModule,
    AccordionModule,
    ListboxModule
  ]
})
export class FascicoloDettaglioModule { }
