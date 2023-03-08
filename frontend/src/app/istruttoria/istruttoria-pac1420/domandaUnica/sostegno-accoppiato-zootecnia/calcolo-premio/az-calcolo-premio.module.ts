import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AzCalcoloPremioRoutingModule } from './az-calcolo-premio-routing.module';
import { TabViewModule } from 'primeng/tabview';
import { TabMenuModule } from 'primeng/tabmenu';
import { DialogModule } from 'primeng/dialog';
import { A4gCommonModule } from 'src/app/a4g-common/a4g-common.module';
import { TabAzPControlliNonSuperatiComponent } from './tab-az-p-controlli-non-superati/tab-az-p-controlli-non-superati.component';
import { TabAzPControlliSuperatiComponent } from './tab-az-p-controlli-superati/tab-az-p-controlli-superati.component';
import { TabAzPIntegrazioniAmmissibilitaComponent } from './tab-az-p-integrazioni-ammissibilita/tab-az-p-integrazioni-ammissibilita.component';
import { TabAzPNonAmmissibiliComponent } from './tab-az-p-non-ammissibili/tab-az-p-non-ammissibili.component';
import { AzCalcoloPremioComponent } from './az-calcolo-premio.component';
import { TabAzPRichiestoComponent } from './tab-az-p-richiesto/tab-az-p-richiesto.component';
import { SostegnoSharedModule } from '../../sostegno-shared/sostegno-shared.module';
import { ZootecniaModule } from '../zootecnia.module';

@NgModule({
  imports: [
    CommonModule,
    AzCalcoloPremioRoutingModule,
    TabMenuModule,
    TabViewModule,
    A4gCommonModule,
    DialogModule,
    SostegnoSharedModule,
    ZootecniaModule
  ],
  declarations: [
    AzCalcoloPremioComponent,
    TabAzPControlliNonSuperatiComponent,
    TabAzPControlliSuperatiComponent,
    TabAzPIntegrazioniAmmissibilitaComponent,
    TabAzPNonAmmissibiliComponent,
    TabAzPRichiestoComponent
  ]
})
export class AzCalcoloPremioModule { }
