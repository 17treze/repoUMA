import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { DomandeUmaRoutingModule } from './domande-uma-routing.module';
import { DomandeUmaComponent } from './domande-uma.component';
import { SharedModule } from 'src/app/shared/shared.module';
import { TranslateModule } from '@ngx-translate/core';
import { MessageModule } from 'primeng-lts/message';
import { DettaglioDomandaUmaComponent } from './dettaglio-domanda-uma/dettaglio-domanda-uma.component';
import { DettaglioCarburanteComponent } from './dettaglio-domanda-uma/dettaglio-carburante/dettaglio-carburante.component';
import { SwipeModule } from 'ng-swipe';

@NgModule({
  declarations: [
    DomandeUmaComponent,
    DettaglioDomandaUmaComponent,
    DettaglioCarburanteComponent,
  ],
  imports: [
    SharedModule,
    CommonModule,
    TranslateModule,
    DomandeUmaRoutingModule,
    MessageModule,
    SwipeModule
  ],
})
export class DomandeUmaModule {}
