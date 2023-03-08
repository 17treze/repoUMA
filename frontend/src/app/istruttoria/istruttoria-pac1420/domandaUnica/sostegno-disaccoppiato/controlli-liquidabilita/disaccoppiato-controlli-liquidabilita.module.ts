import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { TabViewModule } from 'primeng/tabview';
import { TabMenuModule } from 'primeng/tabmenu';
import { DialogModule } from 'primeng/dialog';
import { A4gCommonModule } from '../../../../../a4g-common/a4g-common.module';
import { SostegnoSharedModule } from '../../sostegno-shared/sostegno-shared.module';
import { DisaccoppiatoModule } from '../disaccoppiato.module';
import { DisaccoppiatoControlliLiquidiabilitaRoutingModule } from './disaccoppiato-controlli-liquidabilita-routing.module';
import { DisaccoppiatoControlliLiquidiabilitaComponent } from './disaccoppiato-controlli-liquidabilita.component';
import { TabDisaccoppiatoLiquidabilitaControlliNonSuperatiComponent } from './tab-controlli-non-superati/tab-disaccoppiato-liquidabilita-controlli-non-superati.component';
import { TabDisaccoppiatoLiquidabilitaControlliSuperatiComponent } from './tab-controlli-superati/tab-disaccoppiato-liquidabilita-controlli-superati.component';
import { TabDisaccoppiatoLiquidabilitaDebitiComponent } from './tab-debiti/tab-disaccoppiato-liquidabilita-debiti.component';
import { TabDisaccoppiatoLiquidabilitaLiquidabileComponent } from './tab-liquidabile/tab-disaccoppiato-liquidabilita-liquidabile.component';


@NgModule({
  imports: [
    CommonModule,
    DisaccoppiatoControlliLiquidiabilitaRoutingModule,
    DisaccoppiatoModule,
    TabMenuModule,
    TabViewModule,
    A4gCommonModule,
    DialogModule,
    SostegnoSharedModule
  ],
  declarations: [
    DisaccoppiatoControlliLiquidiabilitaComponent,
    TabDisaccoppiatoLiquidabilitaControlliSuperatiComponent,
    TabDisaccoppiatoLiquidabilitaControlliNonSuperatiComponent,
    TabDisaccoppiatoLiquidabilitaLiquidabileComponent,
    TabDisaccoppiatoLiquidabilitaDebitiComponent
  ]
})
export class DisaccoppiatoControlliLiquidabilitaModule {}
