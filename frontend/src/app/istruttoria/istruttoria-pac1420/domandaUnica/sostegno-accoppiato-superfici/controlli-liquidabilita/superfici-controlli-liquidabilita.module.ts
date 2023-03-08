import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { TabViewModule } from 'primeng/tabview';
import { TabMenuModule } from 'primeng/tabmenu';
import { DialogModule } from 'primeng/dialog';
import { A4gCommonModule } from '../../../../../a4g-common/a4g-common.module';
import { SostegnoSharedModule } from '../../sostegno-shared/sostegno-shared.module';
import { SuperficiModule } from '../superfici.module';
import { SuperficiControlliLiquidiabilitaRoutingModule } from './superfici-controlli-liquidabilita-routing.module';
import { SuperficiControlliLiquidiabilitaComponent } from './superfici-controlli-liquidabilita.component';
import { TabAcsClDebitiComponent } from './tab-acs-debiti/tab-acs-cl-debiti.component';
import { TabSuperficiLiquidabilitaControlliNonSuperatiComponent } from './tab-controlli-non-superati/tab-superfici-liquidabilita-controlli-non-superati.component';
import { TabSuperficiLiquidabilitaControlliSuperatiComponent } from './tab-controlli-superati/tab-superfici-liquidabilita-controlli-superati.component';
import { TabSuperficiLiquidabilitaLiquidabileComponent } from './tab-liquidabile/tab-superfici-liquidabilita-liquidabile.component';


@NgModule({
  imports: [
    CommonModule,
    SuperficiControlliLiquidiabilitaRoutingModule,
    TabMenuModule,
    TabViewModule,
    A4gCommonModule,
    DialogModule,
    SostegnoSharedModule,
    SuperficiModule
  ],
  declarations: [
    SuperficiControlliLiquidiabilitaComponent,
    TabSuperficiLiquidabilitaControlliSuperatiComponent,
    TabSuperficiLiquidabilitaControlliNonSuperatiComponent,
    TabSuperficiLiquidabilitaLiquidabileComponent,
    TabAcsClDebitiComponent
  ]
})
export class SuperficiControlliLiquidabilitaModule {}
