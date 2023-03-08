import { A4gCommonModule } from 'src/app/a4g-common/a4g-common.module';
import { NgModule } from '@angular/core';
import { RiaperturaFascicoloRoutingModule } from './riapertura-fascicolo-routing.module';
import { RicercaFascicoloDaRiaprireComponent } from './ricerca-fascicolo-da-riaprire/ricerca-fascicolo-da-riaprire.component';

@NgModule({
  imports: [
    A4gCommonModule,
    RiaperturaFascicoloRoutingModule
  ],
  declarations: [RicercaFascicoloDaRiaprireComponent]
})
export class RiaperturaFascicoloModule { }
