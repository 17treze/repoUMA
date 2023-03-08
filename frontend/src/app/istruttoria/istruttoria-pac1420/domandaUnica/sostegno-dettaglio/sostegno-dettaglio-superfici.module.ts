import { CommonModule } from "@angular/common";
import { NgModule } from "@angular/core";
import { A4gCommonModule } from "src/app/a4g-common/a4g-common.module";
import { SostegnoSharedModule } from "../sostegno-shared/sostegno-shared.module";
import { IstruttoriaDuDettaglioResolve } from "./shared/IstruttoriaDuDettaglioResolve";
import { DettaglioSharedModule } from "./shared/shared.module";
import { SostegnoDettaglioSuperficieRoutingModule } from "./sostegno-dettaglio-superfici-routing.module";
import { DatiDomandaAccComponent } from "./superficie/dati-domanda-acc/dati-domanda-acc.component";
import { DatiIstruttoriaComponent } from "./superficie/dati-istruttoria/dati-istruttoria.component";
import { DatiParticellaAccComponent } from "./superficie/dati-particella-acc/dati-particella-acc.component";
import { DichiarazioniAccComponent } from "./superficie/dichiarazioni-acc/dichiarazioni-acc.component";
import { SuperficiImpegnateAccComponent } from "./superficie/superfici-impegnate-acc/superfici-impegnate-acc.component";
import { SuperficieComponent } from './superficie/superficie.component';
@NgModule({
    imports: [
      CommonModule,
      SostegnoDettaglioSuperficieRoutingModule,
      DettaglioSharedModule,
      A4gCommonModule,
      SostegnoSharedModule
    ],
    declarations: [
      SuperficieComponent,
      DatiIstruttoriaComponent,
      SuperficiImpegnateAccComponent,
      DichiarazioniAccComponent,
      DatiDomandaAccComponent,
      DatiParticellaAccComponent
    ],
    providers: [
      IstruttoriaDuDettaglioResolve
    ]
  })
  export class SostegnoDettaglioSuperficiModule { }