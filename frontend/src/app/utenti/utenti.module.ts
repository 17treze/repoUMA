import { NgModule } from '@angular/core';
import { UtentiRoutingModule } from './utenti-routing.module';
import { UtenteComponent } from './utente/utente.component';
import { A4gCommonModule } from '../a4g-common/a4g-common.module';
import { TitolareComponent } from './responsabilita/titolare/titolare.component';
import { UtentiService } from './utenti.service';
import { DropdownModule } from 'primeng/dropdown';
import { AccordionModule } from 'primeng/accordion';
import { MessagesModule } from 'primeng/messages';
import { MessageModule } from 'primeng/message';
import { TreeModule } from 'primeng/tree';
import { CheckboxModule } from 'primeng/checkbox';
import { ConsulenteComponent } from './responsabilita/consulente/consulente.component';
import { PATComponent } from './responsabilita/pat/pat.component';
import { CAAComponent } from './responsabilita/caa/caa.component';
import { AltriEntiComponent } from './responsabilita/altri-enti/altri-enti.component';
import { LegaleComponent } from './responsabilita/legale/legale.component';
import { DistributoreComponent } from './responsabilita/distributore/distributore.component';

@NgModule({
  imports: [
    UtentiRoutingModule,
    A4gCommonModule,
    DropdownModule,
    AccordionModule,
    MessageModule,
    MessagesModule,
    TreeModule,
    CheckboxModule
  ],
  declarations: [
    UtenteComponent,
    TitolareComponent,
    ConsulenteComponent,
    PATComponent,
    CAAComponent,
    AltriEntiComponent,
    DistributoreComponent,
    LegaleComponent
  ],
  entryComponents: [
    TitolareComponent,
    ConsulenteComponent,
    PATComponent,
    CAAComponent,
    AltriEntiComponent,
    DistributoreComponent,
    LegaleComponent
  ],
  providers: [UtentiService]
})
export class UtentiModule { }