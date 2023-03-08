import { MandatoComponent } from './mandato.component';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { A4gCommonModule } from 'src/app/a4g-common/a4g-common.module';
import { MandatoRoutingModule } from './mandato-routing.module';
import { RevocaImmediataDialogComponent } from './revoca-immediata-dialog/revoca-immediata-dialog.component';
import { DialogModule } from 'primeng/dialog';

@NgModule({
  imports: [
    MandatoRoutingModule,
    A4gCommonModule,
    CommonModule,
    DialogModule
  ],
  declarations: [
    MandatoComponent,
    RevocaImmediataDialogComponent
  ]
})
export class MandatoModule { }
