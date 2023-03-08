import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { A4gCommonModule } from "src/app/a4g-common/a4g-common.module";
import { ControlliSostegnoComponent } from "./disaccoppiato/controlli-sostegno/controlli-sostegno.component";
import { DisaccoppiatoComponent } from "./disaccoppiato/disaccoppiato.component";
import { IstruttoriaDuDettaglioResolve } from "./shared/IstruttoriaDuDettaglioResolve";
import { DettaglioSharedModule } from "./shared/shared.module";
import { SostegnoDettaglioDisaccoppiatoRoutingModule } from "./sostegno-dettaglio-disaccoppiato-routing.module";
import { SuperficiImpegnateComponent } from "./disaccoppiato/superfici-impegnate/superfici-impegnate.component";
import { DichiarazioniComponent } from "./disaccoppiato/dichiarazioni/dichiarazioni.component";
import { InformazioniDomandaComponent } from "./disaccoppiato/informazioni-domanda/informazioni-domanda.component";
import { DatiDomandaComponent } from "./disaccoppiato/dati-domanda/dati-domanda.component";
import { EsitiParticelleComponent } from "./disaccoppiato/esiti-particelle/esiti-particelle.component";
import { TreeTableModule } from "primeng/treetable";
import { InserimentoDatiIstruttoriaComponent } from "./disaccoppiato/inserimento-dati-istruttoria/inserimento-dati-istruttoria.component";
import {DatiPascoloComponent} from "./disaccoppiato/dati-pascolo/dati-pascolo.component";
import { DatiLiquidazioneComponent } from "./shared/dati-liquidazione/dati-liquidazione.component";
import { SostegnoSharedModule } from "../sostegno-shared/sostegno-shared.module";


@NgModule({
    imports: [
      CommonModule,
      SostegnoDettaglioDisaccoppiatoRoutingModule,
      DettaglioSharedModule,
      SostegnoSharedModule,
      A4gCommonModule,
      TreeTableModule
    ],
    declarations: [
      DisaccoppiatoComponent,
      SuperficiImpegnateComponent,
      DichiarazioniComponent,
      InformazioniDomandaComponent,
      ControlliSostegnoComponent,
      DatiDomandaComponent,
      EsitiParticelleComponent,
      InserimentoDatiIstruttoriaComponent,
      DatiPascoloComponent
    ],
    providers: [
      IstruttoriaDuDettaglioResolve
    ]
  })
  export class SostegnoDettaglioDisaccoppiatoModule { }
