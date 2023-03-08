import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { A4gCommonModule } from '../../../../../a4g-common/a4g-common.module';
import { TabViewModule } from 'primeng/tabview';
import { TabMenuModule } from 'primeng/tabmenu';
import { DialogModule } from 'primeng/dialog';
import { DisaccoppiatoCalcoloPremioRoutingModule } from './disaccoppiato-calcolo-premio-routing.module';
import { SostegnoSharedModule } from '../../sostegno-shared/sostegno-shared.module';
import { DisaccoppiatoCalcoloPremioComponent } from './disaccoppiato-calcolo-premio.component';
import { TabDisaccoppiatoCalcoloRichiestoComponent } from './tab-richiesto/tab-disaccoppiato-calcolo-richiesto.component';
import { TabDisaccoppiatoCalcoloControlliSuperatiComponent } from './tab-controlli-superati/tab-disaccoppiato-calcolo-controlli-superati.component';
import { TabDisaccoppiatoCalcoloControlliNonSuperatiComponent } from './tab-controlli-non-superati/tab-disaccoppiato-calcolo-controlli-non-superati.component';
import { TabDisaccoppiatoCalcoloNonAmmissibileComponent } from './tab-non-ammissibile/tab-disaccoppiato-calcolo-non-ammissibile.component';
import { DisaccoppiatoModule } from '../disaccoppiato.module';


@NgModule({
  imports: [
    DisaccoppiatoCalcoloPremioRoutingModule,
    TabMenuModule,
    TabViewModule,
    A4gCommonModule,
    DialogModule,
    SostegnoSharedModule,
    DisaccoppiatoModule
  ],
  declarations: [
    DisaccoppiatoCalcoloPremioComponent,
    TabDisaccoppiatoCalcoloRichiestoComponent,
    TabDisaccoppiatoCalcoloControlliSuperatiComponent,
    TabDisaccoppiatoCalcoloControlliNonSuperatiComponent,
    TabDisaccoppiatoCalcoloNonAmmissibileComponent
  ]
})
export class DisaccoppiatoCalcoloPremioModule {}
