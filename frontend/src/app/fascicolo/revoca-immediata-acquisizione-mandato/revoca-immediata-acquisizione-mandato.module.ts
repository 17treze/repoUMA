import {NgModule} from '@angular/core';
import {RevocaImmediataAcquisizioneMandatoRoutingModule} from './revoca-immediata-acquisizione-mandato-routing.module';
import {
  RicercaFascicoloPerAcquisizioneMandatoComponent
} from './ricerca-fascicolo-per-acquisizione-mandato/ricerca-fascicolo-per-acquisizione-mandato.component';
import {A4gCommonModule} from "../../a4g-common/a4g-common.module";

@NgModule({
  imports: [
    A4gCommonModule,
    RevocaImmediataAcquisizioneMandatoRoutingModule
  ],
  declarations: [RicercaFascicoloPerAcquisizioneMandatoComponent]
})
export class RevocaImmediataAcquisizioneMandatoModule { }
