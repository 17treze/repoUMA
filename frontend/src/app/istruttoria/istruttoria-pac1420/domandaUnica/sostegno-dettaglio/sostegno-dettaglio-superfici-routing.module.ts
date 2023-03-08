import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";
import { DettaglioDomandaComponent } from "./dettaglio-domanda/dettaglio-domanda.component";
import { IstruttoriaDuDettaglioResolve } from "./shared/IstruttoriaDuDettaglioResolve";
import { SuperficieComponent } from "./superficie/superficie.component";
import { Costanti } from "../Costanti";
import { DatiIstruttoriaComponent } from "./superficie/dati-istruttoria/dati-istruttoria.component";
import { SuperficiImpegnateAccComponent } from "./superficie/superfici-impegnate-acc/superfici-impegnate-acc.component";
import { DichiarazioniAccComponent } from "./superficie/dichiarazioni-acc/dichiarazioni-acc.component";
import { CtrlSostegnoAccComponent } from "./superficie/controlli-sostegno/ctrl-sostegno-acc.component";
import { DatiDomandaAccComponent } from "./superficie/dati-domanda-acc/dati-domanda-acc.component";
import { DatiParticellaAccComponent } from "./superficie/dati-particella-acc/dati-particella-acc.component";
import { DatiLiquidazioneComponent } from "./shared/dati-liquidazione/dati-liquidazione.component";

const routes: Routes = [
    {
      path: '',
      component: DettaglioDomandaComponent,
      data: {
        mybreadcrumb: ''
      },
      resolve: {
        domandaIstruttoria: IstruttoriaDuDettaglioResolve
      },
      children: [
        {
          path: '',
          component: SuperficieComponent,
          children: [
            {
              path: '',
              redirectTo: Costanti.controlliSostegno,
              pathMatch: 'full'
            },
            {
              path: Costanti.superficiImpegnate,
              component: SuperficiImpegnateAccComponent
            },
            {
              path: Costanti.dichiarazioni,
              component: DichiarazioniAccComponent
            },            
            {
              path: Costanti.datiIstruttoria,
              component: DatiIstruttoriaComponent
            },
            {
              path: Costanti.controlliSostegno,
              component: CtrlSostegnoAccComponent
            },
            {
              path: Costanti.esiticalcoli,
              component: DatiDomandaAccComponent
            },
            {
              path: Costanti.esitiParticelle,
              component: DatiParticellaAccComponent
            },
            {
              path: Costanti.datiLiquidazione,
              component: DatiLiquidazioneComponent
            }
          ]
        }
      ]
    }
  ];
  
  
  @NgModule({
    imports: [
      RouterModule.forChild(routes)
    ]
  })
  export class SostegnoDettaglioSuperficieRoutingModule { }