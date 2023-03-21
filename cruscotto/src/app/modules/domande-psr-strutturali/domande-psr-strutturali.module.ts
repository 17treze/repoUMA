import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { DomandePsrStrutturaliRoutingModule } from './domande-psr-strutturali-routing.module';
import { ContainerDomandePsrStrutturaliComponent } from './container-domande-psr-strutturali/container-domande-psr-strutturali.component';
import { ContainerDettaglioDomandaPsrStruttComponent } from './container-dettaglio-psr-strutturale/container-dettaglio-psr-strutturale.component'
import { DettaglioDomandaPsrStruttComponent } from './dettaglio-domande-psr-strutturali/dettaglio-domande-psr-strutturali.component';
import { DettaglioDomandaPsrStrutturaleComponent } from './dettaglio-domanda-psr-strutturale/dettaglio-domanda-psr-strutturale.component';
import { DettaglioIstruttoriaPsrStrutturaleComponent } from './dettaglio-istruttoria-psr-strutturale/dettaglio-istruttoria-psr-strutturale.component';
import { ListaDomandePsrStrutturaliComponent } from './lista-domande-psr-strutturali/lista-domande-psr-strutturali.component';
import { SharedModule } from 'src/app/shared/shared.module';
import { MessageModule } from 'primeng-lts/message';
import { AccordionModule } from 'primeng-lts/accordion';
import { DettaglioInvestimentiComponent } from './dettaglio-istruttoria-psr-strutturale/dettaglio-finanziabilita/dettaglio-investimenti.component';
import { StoricoInterventoComponent } from './dettaglio-istruttoria-psr-strutturale/dettaglio-finanziabilita/storico-intervento/storico-intervento.component';
import { HeaderDomandaPsrStrutturaleComponent } from './header-domanda-psr-strutturale/header-domanda-psr-strutturale.component';
import { HeaderDomandaPsrStrutturaleComponentAlt } from './header-domanda-psr-strutturale-alt/header-domanda-psr-strutturale-alt.component';
import { SanzioneDomandaComponent } from './dettaglio-istruttoria-psr-strutturale/sanzione-domanda/sanzione-domanda.component';
import { PanelModule } from "primeng-lts/panel";
import { DettaglioInterventoPsrStrutturaleComponent } from './dettaglio-istruttoria-psr-strutturale/dettaglio-intervento/dettaglio-intervento-psr-strutturale.component';
import { StoricoFatturaComponent } from './dettaglio-istruttoria-psr-strutturale/dettaglio-intervento/storico-fattura/storico-fattura.component';
import { VarianteAlertInlineComponent } from './dettaglio-domanda-psr-strutturale/variante-alert-inline/variante-alert-inline.component';
import { CardDettaglioInvestimentoComponent } from './dettaglio-domanda-psr-strutturale/card-dettaglio-investimento/card-dettaglio-investimento.component';
import { SwipeModule } from 'ng-swipe';

@NgModule({
  declarations: [
    ContainerDomandePsrStrutturaliComponent,
    ContainerDettaglioDomandaPsrStruttComponent,
    DettaglioDomandaPsrStruttComponent,
    DettaglioDomandaPsrStrutturaleComponent,
    DettaglioIstruttoriaPsrStrutturaleComponent,
    ListaDomandePsrStrutturaliComponent,
    DettaglioInvestimentiComponent,
    StoricoInterventoComponent,
    HeaderDomandaPsrStrutturaleComponent,
    HeaderDomandaPsrStrutturaleComponentAlt,
    SanzioneDomandaComponent,
    DettaglioInterventoPsrStrutturaleComponent,
    StoricoFatturaComponent,
    VarianteAlertInlineComponent,
    CardDettaglioInvestimentoComponent],
    imports: [
        SharedModule,
        CommonModule,
        MessageModule,
        AccordionModule,
        DomandePsrStrutturaliRoutingModule,
        PanelModule,
        SwipeModule
    ]
})
export class DomandePsrStrutturaliModule { }
