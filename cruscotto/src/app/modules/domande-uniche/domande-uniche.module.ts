import { NgModule } from '@angular/core';
import { ListaDomandeComponent } from './lista-domande/lista-domande.component';
import { ContainerDomandeUnicheComponent } from './container-domande-uniche/container-domande-uniche.component';
import { DomandeUnicaRoutingModule } from "./domande-unica-routing.module";
import { DomandeUnicheService } from "./domande-uniche.service";
import { SharedModule } from 'src/app/shared/shared.module';
import { ContainerDettaglioDomandaUnicaComponent } from './container-dettaglio-domanda-unica/container-dettaglio-domanda-unica.component';
import { DettaglioDomandaComponent } from './dettaglio-domanda/dettaglio-domanda.component';
import { DettaglioIstruttoriaComponent } from './dettaglio-istruttoria/dettaglio-istruttoria.component';
import { CreaRegole } from './dettaglio-istruttoria/crea-regole';
import { DomandaAntimafiaComponent } from './domanda-antimafia/domanda-antimafia.component';
import { SwipeModule } from 'ng-swipe';

@NgModule({
  declarations: [
    ListaDomandeComponent,
    ContainerDomandeUnicheComponent,
    ContainerDettaglioDomandaUnicaComponent,
    DettaglioDomandaComponent,
    DettaglioIstruttoriaComponent,
    DomandaAntimafiaComponent
  ],
  imports: [
    SharedModule,
    DomandeUnicaRoutingModule,
    SwipeModule
  ],
  providers: [
    DomandeUnicheService,
    CreaRegole
  ]
})
export class DomandeUnicheModule { }
