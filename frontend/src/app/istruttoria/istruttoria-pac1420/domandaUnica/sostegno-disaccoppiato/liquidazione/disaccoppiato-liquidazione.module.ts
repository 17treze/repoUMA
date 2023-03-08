import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { A4gCommonModule } from '../../../../../a4g-common/a4g-common.module';
import { TabViewModule } from 'primeng/tabview';
import { TabMenuModule } from 'primeng/tabmenu';
import { DialogModule } from 'primeng/dialog';
import { SostegnoSharedModule } from '../../sostegno-shared/sostegno-shared.module';
import { DisaccoppiatoLiquidazioneRoutingModule } from './disaccoppiato-liquidazione-routing.module';
import { DisaccoppiatoLiquidazioneComponent } from './disaccoppiato-liquidazione.component';
import { TabDisaccoppiatoLiquidazioneLiquidabileComponent } from './tab-liquidabile/tab-disaccoppiato-liquidazione-liquidabile.component';
import { TabDisaccoppiatoLiquidazioneNonLiquidabileComponent } from './tab-non-liquidabile/tab-disaccoppiato-liquidazione-non-liquidabile.component';
import { TabDisaccoppiatoLiquidazioneControlliSuperatiComponent } from './tab-controlli-superati/tab-disaccoppiato-liquidazione-controlli-superati.component';
import { TabDisaccoppiatoLiquidazioneNonAutorizzatoComponent } from './tab-non-autorizzato/tab-disaccoppiato-liquidazione-non-autorizzato.component';
import { TabDisaccoppiatoLiquidazioneControlliIntersostegnoSuperatiComponent } from './tab-controlli-intersostegno-superati/tab-disaccoppiato-liquidazione-controlli-intersostegno-superati.component';
import { TabDisaccoppiatoLiquidazioneAutorizzatoComponent } from './tab-autorizzato/tab-disaccoppiato-liquidazione-autorizzato.component';
import { DisaccoppiatoModule } from '../disaccoppiato.module';

@NgModule({
  imports: [
    CommonModule,
    DisaccoppiatoLiquidazioneRoutingModule,
    DisaccoppiatoModule,
    TabMenuModule,
    TabViewModule,
    A4gCommonModule,
    DialogModule,
    SostegnoSharedModule
  ],
  declarations: [
    DisaccoppiatoLiquidazioneComponent,
    TabDisaccoppiatoLiquidazioneLiquidabileComponent,
    TabDisaccoppiatoLiquidazioneNonLiquidabileComponent,
    TabDisaccoppiatoLiquidazioneControlliSuperatiComponent,
    TabDisaccoppiatoLiquidazioneNonAutorizzatoComponent,
    TabDisaccoppiatoLiquidazioneControlliIntersostegnoSuperatiComponent,
    TabDisaccoppiatoLiquidazioneAutorizzatoComponent
  ]
})
export class DisaccoppiatoLiquidazioneModule {}
