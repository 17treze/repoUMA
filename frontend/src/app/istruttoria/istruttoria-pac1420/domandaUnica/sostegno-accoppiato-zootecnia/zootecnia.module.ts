import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { A4gCommonModule } from 'src/app/a4g-common/a4g-common.module';
import { ContainerZootecniaComponent } from './container/container-zootecnia.component';
import { ZootecniaRoutingModule } from './zootecnia-routing.module';
import { PopupDatiAggregatiZootecniaComponent } from './calcolo-premio/popup-dati-aggregati-zootecnia/popup-dati-aggregati-zootecnia.component';
import { FiltriZootecniaComponent } from './filtri/filtri-zootecnia.component';
import { DialogModule } from 'primeng/dialog';

@NgModule({
  imports: [
    CommonModule,
    A4gCommonModule,
    ZootecniaRoutingModule,
    DialogModule
  ],
  declarations: [
    ContainerZootecniaComponent,
    PopupDatiAggregatiZootecniaComponent,
    FiltriZootecniaComponent
  ],
  exports: [
    PopupDatiAggregatiZootecniaComponent,
    FiltriZootecniaComponent,
    DialogModule
  ]
})
export class ZootecniaModule { }
