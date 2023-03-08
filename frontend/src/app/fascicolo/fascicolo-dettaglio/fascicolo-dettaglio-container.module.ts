import { MenubarModule } from 'primeng/menubar';
import { PopupControlloCompletezzaComponent } from '../popup-controllo-completezza/popup-controllo-completezza.component';
import { PopupCompletaTrasferimentoInAppagComponent } from './../popup-completa-trasferimento-in-appag/popup-completa-trasferimento-in-appag.component';
import { PopupModificaFirmatarioComponent } from '../popup-modifica-firmatario/popup-modifica-firmatario.component';
import { NgModule } from "@angular/core";
import { CardModule } from "primeng/card";
import { DialogModule } from "primeng/dialog";
import { CheckboxModule } from "primeng/checkbox";
import { ToolbarModule } from "primeng/toolbar";
import { A4gCommonModule } from "src/app/a4g-common/a4g-common.module";
import { FascicoloDettaglioContainerComponent } from "../fascicolo-dettaglio-container/fascicolo-dettaglio-container.component";
import { PopupModificaSedeComponent } from "../popup-modifica-sede/popup-modifica-sede.component";
import { PopupCompletaTrasferimentoDaAppagComponent } from '../popup-completa-trasferimento-da-appag/popup-completa-trasferimento-da-appag.component';
import { PopupSchedaValidazioneComponent } from './../popup-scheda-validazione/popup-scheda-validazione.component';
import { PopupPassaAStatofascicoloChoiceComponent } from '../popup-passa-a-statofascicolo-choice/popup-passa-a-statofascicolo-choice.component';
import { PopupConfermaInAggiornamentoComponent } from '../popup-conferma-in-aggiornamento/popup-conferma-in-aggiornamento.component';
import { PopupSospensioneFascicoloComponent } from '../popup-sospensione-fascicolo/popup-sospensione-fascicolo.component';


@NgModule({
  imports: [
    A4gCommonModule,
    DialogModule,
    CardModule,
    ToolbarModule,
    CheckboxModule,
    MenubarModule
  ], declarations: [
    FascicoloDettaglioContainerComponent,
    PopupModificaSedeComponent,
    PopupModificaFirmatarioComponent,
    PopupCompletaTrasferimentoDaAppagComponent,
    PopupCompletaTrasferimentoInAppagComponent,
    PopupSchedaValidazioneComponent,
    PopupPassaAStatofascicoloChoiceComponent,
    PopupControlloCompletezzaComponent,
    PopupConfermaInAggiornamentoComponent,
    PopupSospensioneFascicoloComponent
  ], providers: [
  ]
})
export class FascicoloDettaglioContainerModule { }
