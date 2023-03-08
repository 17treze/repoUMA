import { registerLocaleData } from '@angular/common';
import localeIt from '@angular/common/locales/it';
import { NgModule } from '@angular/core';
import { CardModule } from 'primeng/card';
import { ToolbarModule } from 'primeng/toolbar';
import { CheckboxModule } from 'primeng/checkbox';
import { DialogModule } from 'primeng/dialog';
import { A4gCommonModule } from '../a4g-common/a4g-common.module';
import { DomandaIntegrativaComponent } from '../istruttoria/istruttoria-pac1420/domanda-integrativa/domanda-integrativa.component';
import { FascicoloRoutingModule } from './fascicolo-routing.module';
import { FascicoloService } from './fascicolo.service';
import { PresentazioneIstanzeComponent } from './presentazione-istanze/presentazione-istanze.component';
import { HeaderFascicoloComponent } from './header-fascicolo/header-fascicolo.component';
import { DettaglioDomandeCollegateComponent } from './sintesi-domande/dettaglio-domande-collegate/dettaglio-domande-collegate/dettaglio-domande-collegate.component';
import { SintesiDomandeContainerComponent } from './sintesi-domande/dettaglio-domande-collegate/sintesi-domande-container/sintesi-domande-container.component';
import { SintesiDomandeComponent } from './sintesi-domande/sintesi-domande.component';
import { FascicoloDettaglioService } from './fascicolo-dettaglio-container/fascicolo-dettaglio.service';
import { MandatoService } from './mandato.service';
import { FascicoloDettaglioContainerModule } from './fascicolo-dettaglio/fascicolo-dettaglio-container.module';
import { MediatorService } from './mediator.service';
import { FascicoloAziendaleComponent } from './fascicolo-aziendale/fascicolo-aziendale.component';

registerLocaleData(localeIt, 'it');
@NgModule({
  imports: [
    FascicoloRoutingModule,
    A4gCommonModule,
    CardModule,
    ToolbarModule,
    CheckboxModule,
    DialogModule,
    FascicoloDettaglioContainerModule
  ], declarations: [
    PresentazioneIstanzeComponent,
    HeaderFascicoloComponent,
    SintesiDomandeComponent,
    DettaglioDomandeCollegateComponent,
    SintesiDomandeContainerComponent,
    DomandaIntegrativaComponent,
    FascicoloAziendaleComponent
  ], providers: [
    FascicoloService,
    MandatoService,
    FascicoloDettaglioService,
    MediatorService
  ]
})
export class FascicoloModule {
}
