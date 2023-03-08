import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DisaccoppiatoRoutingModule } from './disaccoppiato-routing.module';
import { A4gCommonModule } from 'src/app/a4g-common/a4g-common.module';
import { ContainerDisaccoppiatoComponent } from './container/container-disaccoppiato.component';
import { LoaderModule } from 'src/app/loader/loader.component';
import { FiltriDisaccoppiatoComponent } from './filtri/filtri-disaccoppiato.component';

@NgModule({
  imports: [
    CommonModule,
    A4gCommonModule,
    DisaccoppiatoRoutingModule,
    LoaderModule
  ],
  declarations: [
    ContainerDisaccoppiatoComponent,
    FiltriDisaccoppiatoComponent
  ],
  exports: [
    ContainerDisaccoppiatoComponent,
    FiltriDisaccoppiatoComponent
  ]
})
export class DisaccoppiatoModule { }
