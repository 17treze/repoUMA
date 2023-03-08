import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";
import { DettaglioDomandaComponent } from "./dettaglio-domanda/dettaglio-domanda.component";
import { DisaccoppiatoComponent } from "./disaccoppiato/disaccoppiato.component";
import { IstruttoriaDuDettaglioResolve } from "./shared/IstruttoriaDuDettaglioResolve";
import { ControlliSostegnoComponent } from "./disaccoppiato/controlli-sostegno/controlli-sostegno.component";
import { SuperficiImpegnateComponent } from "./disaccoppiato/superfici-impegnate/superfici-impegnate.component";
import { DichiarazioniComponent } from "./disaccoppiato/dichiarazioni/dichiarazioni.component";
import { InformazioniDomandaComponent } from "./disaccoppiato/informazioni-domanda/informazioni-domanda.component";
import { Costanti } from "../Costanti";
import { DatiDomandaComponent } from "./disaccoppiato/dati-domanda/dati-domanda.component";
import { EsitiParticelleComponent } from "./disaccoppiato/esiti-particelle/esiti-particelle.component";
import { InserimentoDatiIstruttoriaComponent } from "./disaccoppiato/inserimento-dati-istruttoria/inserimento-dati-istruttoria.component";
import { DatiPascoloComponent } from "./disaccoppiato/dati-pascolo/dati-pascolo.component";
import { DatiLiquidazioneComponent } from "./shared/dati-liquidazione/dati-liquidazione.component";

const routes: Routes = [
  {
    path: '',
    component: DettaglioDomandaComponent,
    resolve: {
      domandaIstruttoria: IstruttoriaDuDettaglioResolve
    },
    children: [
      {
        path: '',
        component: DisaccoppiatoComponent,
        children: [
          {
            path: '',
            redirectTo: Costanti.controlliSostegno,
            pathMatch: 'full'
          }, {
            path: Costanti.superficiImpegnate,
            component: SuperficiImpegnateComponent,
          }, {
            path: Costanti.dichiarazioni,
            component: DichiarazioniComponent,
          }, {
            path: Costanti.informazioniDomanda,
            component: InformazioniDomandaComponent,
          }, {
            path: Costanti.controlliSostegno,
            component: ControlliSostegnoComponent,
          }, {
            path: Costanti.datiDomanda,
            component: DatiDomandaComponent,
          }, {
            path: Costanti.esitiParticelle,
            component: EsitiParticelleComponent,
          }, {
            path: Costanti.datiIstruttoria,
            component: InserimentoDatiIstruttoriaComponent
          }, {
            path: Costanti.datiPerPascolo,
            component: DatiPascoloComponent
          },
          {
            path: Costanti.datiLiquidazione,
            component: DatiLiquidazioneComponent
          }
        ],
        data: {
          mybreadcrumb: ''
        }
      }
    ]
  }
];

@NgModule({
  imports: [
    RouterModule.forChild(routes)
  ]
})
export class SostegnoDettaglioDisaccoppiatoRoutingModule { }
