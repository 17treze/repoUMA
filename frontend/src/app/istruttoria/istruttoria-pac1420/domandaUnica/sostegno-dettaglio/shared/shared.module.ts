import { NgModule } from "@angular/core";
import { DettaglioDomandaComponent } from "../dettaglio-domanda/dettaglio-domanda.component";
import { OverlayPanelModule } from "primeng/overlaypanel";
import { TabMenuModule } from "primeng/tabmenu";
import { TabViewModule } from "primeng/tabview";
import { A4gCommonModule } from "src/app/a4g-common/a4g-common.module";
import { DialogModule } from "primeng/dialog";
import { CtrlSostegnoAccComponent } from "../superficie/controlli-sostegno/ctrl-sostegno-acc.component";
import { DatiLiquidazioneComponent } from "./dati-liquidazione/dati-liquidazione.component";

@NgModule({
    imports: [
        TabMenuModule,
        TabViewModule,
        A4gCommonModule,
        DialogModule,
        OverlayPanelModule
    ],
    declarations: [
        DettaglioDomandaComponent,
        CtrlSostegnoAccComponent,
        DatiLiquidazioneComponent
    ],
    exports: [
        DettaglioDomandaComponent,
        TabMenuModule,
        TabViewModule,
        A4gCommonModule,
        DialogModule,
        OverlayPanelModule
    ],
    providers: [
    ]
  })
  export class DettaglioSharedModule {}