import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { TabViewModule } from 'primeng/tabview';
import { TabMenuModule } from 'primeng/tabmenu';
import { A4gCommonModule } from 'src/app/a4g-common/a4g-common.module';
import { SostegnoSharedModule } from '../../sostegno-shared/sostegno-shared.module';
import { ZootecniaModule } from '../zootecnia.module';
import { AzControlliLiquidabilitaRoutingModule } from './az-controlli-liquidabilita-routing.module';
import { AzControlliLiquidabilitaComponent } from './az-controlli-liquidabilita.component';
import { TabAzClControlliNonSuperatiComponent } from './tab-az-cl-controlli-non-superati/tab-az-cl-controlli-non-superati.component';
import { TabAzClControlliSuperatiComponent } from './tab-az-cl-controlli-superati/tab-az-cl-controlli-superati.component';
import { TabAzClDebitiComponent } from './tab-az-cl-debiti/tab-az-cl-debiti.component';
import { TabAzClLiquidabileComponent } from './tab-az-cl-liquidabile/tab-az-cl-liquidabile.component';

@NgModule({
  imports: [
    CommonModule,
    AzControlliLiquidabilitaRoutingModule,
    TabMenuModule,
    TabViewModule,
    A4gCommonModule,
    SostegnoSharedModule,
    ZootecniaModule
  ],
  declarations: [
    AzControlliLiquidabilitaComponent,
    TabAzClControlliNonSuperatiComponent,
    TabAzClControlliSuperatiComponent,
    TabAzClLiquidabileComponent,
    TabAzClDebitiComponent

  ]
})
export class AzControlliLiquidabilitaModule { }
