import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { A4gCommonModule } from '../../../../../a4g-common/a4g-common.module';
import { TabViewModule } from 'primeng/tabview';
import { TabMenuModule } from 'primeng/tabmenu';
import { DialogModule } from 'primeng/dialog';
import { SostegnoSharedModule } from '../../sostegno-shared/sostegno-shared.module';
import { SuperficiLiquidazioneRoutingModule } from './superfici-liquidazione-routing.module';
import { SuperficiLiquidazioneComponent } from './superfici-liquidazione.component';
import { TabSuperficiLiquidazioneLiquidabileComponent } from './tab-liquidabile/tab-superfici-liquidazione-liquidabile.component';
import { TabSuperficiLiquidazioneNonLiquidabileComponent } from './tab-non-liquidabile/tab-superfici-liquidazione-non-liquidabile.component';
import { TabSuperficiLiquidazioneControlliSuperatiComponent } from './tab-controlli-superati/tab-superfici-liquidazione-controlli-superati.component';
import { TabSuperficiLiquidazioneNonAutorizzatoComponent } from './tab-non-autorizzato/tab-superfici-liquidazione-non-autorizzato.component';
import { TabSuperficiLiquidazioneControlliIntersostegnoSuperatiComponent } from './tab-controlli-intersostegno-superati/tab-superfici-liquidazione-controlli-intersostegno-superati.component';
import { TabSuperficiLiquidazioneAutorizzatoComponent } from './tab-autorizzato/tab-superfici-liquidazione-autorizzato.component';
import { SuperficiModule } from '../superfici.module';

@NgModule({
  imports: [
    CommonModule,
    SuperficiLiquidazioneRoutingModule,
    SuperficiModule,
    TabMenuModule,
    TabViewModule,
    A4gCommonModule,
    DialogModule,
    SostegnoSharedModule
  ],
  declarations: [
    SuperficiLiquidazioneComponent,
    TabSuperficiLiquidazioneLiquidabileComponent,
    TabSuperficiLiquidazioneNonLiquidabileComponent,
    TabSuperficiLiquidazioneControlliSuperatiComponent,
    TabSuperficiLiquidazioneNonAutorizzatoComponent,
    TabSuperficiLiquidazioneControlliIntersostegnoSuperatiComponent,
    TabSuperficiLiquidazioneAutorizzatoComponent
  ]
})
export class SuperficiLiquidazioneModule {}
