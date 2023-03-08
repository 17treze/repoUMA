import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SuperficiRoutingModule } from './superfici-routing.module';
import { A4gCommonModule } from 'src/app/a4g-common/a4g-common.module';
import { ContainerSuperficiComponent } from './container/container-superfici.component';
import { LoaderModule } from 'src/app/loader/loader.component';
import { FiltriSuperficiComponent } from './filtri/filtri-superfici.component';
import { PopupDatiAggregatiSuperficieInterventoComponent } from './popup-dati-aggregati-superficie-intervento/popup-dati-aggregati-superficie-intervento.component';
import { DialogModule } from 'primeng/dialog';

@NgModule({
  imports: [
    CommonModule,
    A4gCommonModule,
    SuperficiRoutingModule,
    LoaderModule,
    DialogModule
  ],
  declarations: [
    ContainerSuperficiComponent,
    PopupDatiAggregatiSuperficieInterventoComponent,
    FiltriSuperficiComponent
  ],
  exports: [
    ContainerSuperficiComponent,
    PopupDatiAggregatiSuperficieInterventoComponent,
    FiltriSuperficiComponent,
    DialogModule
  ]
})
export class SuperficiModule { }
