import { NgModule } from '@angular/core';
import { A4gCommonModule } from '../../../../../a4g-common/a4g-common.module';
import { TabViewModule } from 'primeng/tabview';
import { TabMenuModule } from 'primeng/tabmenu';
import { DialogModule } from 'primeng/dialog';
import { SuperficiCalcoloPremioRoutingModule } from './superfici-calcolo-premio-routing.module';
import { SostegnoSharedModule } from '../../sostegno-shared/sostegno-shared.module';
import { SuperficiCalcoloPremioComponent } from './superfici-calcolo-premio.component';
import { TabSuperficiCalcoloRichiestoComponent } from './tab-richiesto/tab-superfici-calcolo-richiesto.component';
import { TabSuperficiCalcoloControlliSuperatiComponent } from './tab-controlli-superati/tab-superfici-calcolo-controlli-superati.component';
import { TabSuperficiCalcoloControlliNonSuperatiComponent } from './tab-controlli-non-superati/tab-superfici-calcolo-controlli-non-superati.component';
import { TabSuperficiCalcoloNonAmmissibileComponent } from './tab-non-ammissibile/tab-superfici-calcolo-non-ammissibile.component';
import { SuperficiModule } from '../superfici.module';


@NgModule({
  imports: [
    SuperficiCalcoloPremioRoutingModule,
    TabMenuModule,
    TabViewModule,
    A4gCommonModule,
    DialogModule,
    SostegnoSharedModule,
    SuperficiModule
  ],
  declarations: [
    SuperficiCalcoloPremioComponent,
    TabSuperficiCalcoloRichiestoComponent,
    TabSuperficiCalcoloControlliSuperatiComponent,
    TabSuperficiCalcoloControlliNonSuperatiComponent,
    TabSuperficiCalcoloNonAmmissibileComponent
  ]
})
export class SuperficiCalcoloPremioModule {}
