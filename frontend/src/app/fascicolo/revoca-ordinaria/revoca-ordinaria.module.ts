import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RevocaOrdinariaRoutingModule } from './revoca-ordinaria-routing.module';
import { A4gCommonModule } from 'src/app/a4g-common/a4g-common.module';
import { DialogModule } from 'primeng/dialog';
import { RevocaOrdinariaComponent } from './revoca-ordinaria.component';
import { FascicoloSharedModule } from '../shared/fascicolo-shared.module';

@NgModule({
  imports: [
    RevocaOrdinariaRoutingModule,
    A4gCommonModule,
    CommonModule,
    DialogModule,
    FascicoloSharedModule
  ],
  declarations: [
    RevocaOrdinariaComponent
  ]
})
export class RevocaOrdinariaModule { }
