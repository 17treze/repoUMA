import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { TabViewModule } from 'primeng/tabview';
import { TabMenuModule } from 'primeng/tabmenu';
import { DialogModule } from 'primeng/dialog';
import { A4gCommonModule } from 'src/app/a4g-common/a4g-common.module';
import { AzLiquidazioneRoutingModule } from './az-liquidazione-routing.module';
import { AzLiquidazioneComponent } from './az-liquidazione.component';
import { TabAzLControlliIntersostegnoSuperatiComponent } from './tab-az-l-controlli-intersostegno-superati/tab-az-l-controlli-intersostegno-superati.component';
import { TabAzLLiquidabileComponent } from './tab-az-l-liquidabile/tab-az-l-liquidabile.component';
import { TabAzLNonLiquidabileComponent } from './tab-az-l-non-liquidabile/tab-az-l-non-liquidabile.component';
import { TabAzLPagamentoAutorizzatoComponent } from './tab-az-l-pagamento-autorizzato/tab-az-l-pagamento-autorizzato.component';
import { TabAzLPagamentoNonAutorizzatoComponent } from './tab-az-l-pagamento-non-autorizzato/tab-az-l-pagamento-non-autorizzato.component';
import { SostegnoSharedModule } from '../../sostegno-shared/sostegno-shared.module';
import { ZootecniaModule } from '../zootecnia.module';

@NgModule({
  imports: [
    CommonModule,
    AzLiquidazioneRoutingModule,
    TabMenuModule,
    TabViewModule,
    A4gCommonModule,
    DialogModule,
    SostegnoSharedModule,
    ZootecniaModule
  ],
  declarations: [
    AzLiquidazioneComponent,
    TabAzLControlliIntersostegnoSuperatiComponent,
    TabAzLLiquidabileComponent,
    TabAzLNonLiquidabileComponent,
    TabAzLPagamentoAutorizzatoComponent,
    TabAzLPagamentoNonAutorizzatoComponent
  ]
})
export class AzLiquidazioneModule { }
