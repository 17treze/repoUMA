import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { IstruttoriaDuDettaglioResolve } from "./shared/IstruttoriaDuDettaglioResolve";
import { SostegnoDettaglioZootecniaRoutingModule } from "./sostegno-dettaglio-zootecnia-routing.module";
import { ZootecniaComponent } from './zootecnia/zootecnia.component';
import { DettaglioSharedModule } from "./shared/shared.module";
import { DatiIstruttoriaComponent } from "./zootecnia/dati-istruttoria/dati-istruttoria.component";
import { DichiarazioniAccComponent } from "./zootecnia/dichiarazioni-acc/dichiarazioni-acc.component";
import { EsitiCalcoliComponent } from "./zootecnia/esiti-calcoli/esiti-calcoli.component";
import { ControlliSostegnoComponent } from "./zootecnia/controlli-sostegno/controlli-sostegno.component";
import { CapiAmmessiComponent } from "./zootecnia/capi-ammessi/capi-ammessi.component";
import { CapiRichiestiComponent } from "./zootecnia/capi-richiesti/capi-richiesti.component";
import { SostegnoSharedModule } from "../sostegno-shared/sostegno-shared.module";

@NgModule({
    imports: [
      CommonModule,
      SostegnoDettaglioZootecniaRoutingModule,
      DettaglioSharedModule,
      SostegnoSharedModule
    ],
    declarations: [
      ZootecniaComponent,
      DatiIstruttoriaComponent,
      DichiarazioniAccComponent,
      EsitiCalcoliComponent,
      ControlliSostegnoComponent,
      CapiAmmessiComponent,
      CapiRichiestiComponent
    ],
    providers: [
      IstruttoriaDuDettaglioResolve
    ]
  })
  export class SostegnoDettaglioZootecniaModule { }