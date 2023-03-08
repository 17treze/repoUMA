import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";
import { DettaglioDomandaComponent } from "./dettaglio-domanda/dettaglio-domanda.component";
import { IstruttoriaDuDettaglioResolve } from "./shared/IstruttoriaDuDettaglioResolve";
import { ZootecniaComponent } from "./zootecnia/zootecnia.component";
import { Costanti } from "../Costanti";
import { DatiIstruttoriaComponent } from "./zootecnia/dati-istruttoria/dati-istruttoria.component";
import { DichiarazioniAccComponent } from "./zootecnia/dichiarazioni-acc/dichiarazioni-acc.component";
import { EsitiCalcoliComponent } from "./zootecnia/esiti-calcoli/esiti-calcoli.component";
import { ControlliSostegnoComponent } from "./zootecnia/controlli-sostegno/controlli-sostegno.component";
import { CapiAmmessiComponent } from "./zootecnia/capi-ammessi/capi-ammessi.component";
import { CapiRichiestiComponent } from "./zootecnia/capi-richiesti/capi-richiesti.component";
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
        component: ZootecniaComponent,
        children: [
          {
            path: '',
            redirectTo: Costanti.controlliSostegno,
            pathMatch: 'full'
          }, {
            path: Costanti.dichiarazioni,
            component: DichiarazioniAccComponent
          }, {
            path: Costanti.datiIstruttoria,
            component: DatiIstruttoriaComponent
          }, {
            path: Costanti.esiticalcoli,
            component: EsitiCalcoliComponent
          }, {
            path: Costanti.controlliSostegno,
            component: ControlliSostegnoComponent
          }, {
            path: Costanti.capiAmmessi,
            component: CapiAmmessiComponent
          }, {
            path: Costanti.capiRichiesti,
            component: CapiRichiestiComponent
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
export class SostegnoDettaglioZootecniaRoutingModule { }