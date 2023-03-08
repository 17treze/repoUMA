import { PopupRunMigrazioneComponent } from './../fascicoli-da-migrare/popup-run-migrazione/popup-run-migrazione.component';
import { PopupRichiestaTrasferimentoComponent } from './../creazione-fascicolo/crea-fascicolo/importa-fascicolo-v2/popup-richiesta-trasferimento/popup-richiesta-trasferimento.component';
import { ImportaFascicoloV2Component } from './../creazione-fascicolo/crea-fascicolo/importa-fascicolo-v2/importa-fascicolo-v2.component';
import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { CardModule } from "primeng/card";
import { DialogModule } from "primeng/dialog";
import { InplaceModule } from "primeng/inplace";
import { A4gCommonModule } from "src/app/a4g-common/a4g-common.module";
import { PopupCartaServiziComponent } from "../creazione-fascicolo/crea-fascicolo/importa-fascicolo-v2/popup-carta-servizi/popup-carta-servizi.component";
import { MandatoService } from '../mandato.service';

@NgModule({
    imports: [
        CommonModule,
        A4gCommonModule,
        InplaceModule,
        DialogModule,
        CardModule
    ],
    declarations: [PopupCartaServiziComponent, ImportaFascicoloV2Component, PopupRichiestaTrasferimentoComponent, PopupRunMigrazioneComponent],
    providers: [MandatoService],
    exports: [PopupCartaServiziComponent, PopupRichiestaTrasferimentoComponent]
})
export class FascicoloSharedModule { }