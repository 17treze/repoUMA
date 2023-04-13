import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DichiarazioneAntimafiaRoutingModule } from './dichiarazione-antimafia-routing.module';
import { DichiarazioneAntimafiaComponent } from './dichiarazione-antimafia.component';
import { TranslateModule } from '@ngx-translate/core';
import { MessageModule } from 'primeng-lts/message';
import { SharedModule } from '../../../app/shared/shared.module';
import { StatoDichiarazioneComponent } from './stato-dichiarazione/stato-dichiarazione.component';
import { DomandeCollegateComponent } from './domande-collegate/domande-collegate.component';

@NgModule({
  declarations: [
    DichiarazioneAntimafiaComponent,
    StatoDichiarazioneComponent,
    DomandeCollegateComponent,
  ],
  imports: [
    SharedModule,
    CommonModule,
    DichiarazioneAntimafiaRoutingModule,
    TranslateModule,
    MessageModule,
  ],
})
export class DichiarazioneAntimafiaModule {}
