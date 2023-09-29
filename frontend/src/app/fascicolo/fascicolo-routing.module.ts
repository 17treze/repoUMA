import { NgModule } from "@angular/core";
import { Routes, RouterModule } from "@angular/router";
import { PresentazioneIstanzeComponent } from "./presentazione-istanze/presentazione-istanze.component";
import { FascicoloCorrente } from "./fascicoloCorrente";
import { HeaderFascicoloComponent } from "./header-fascicolo/header-fascicolo.component";
import { FascicoloDettaglioContainerComponent } from "./fascicolo-dettaglio-container/fascicolo-dettaglio-container.component"
import { SintesiDomandeComponent } from "./sintesi-domande/sintesi-domande.component";
import { Labels } from "../app.labels";
import { DettaglioDomandeCollegateComponent } from "./sintesi-domande/dettaglio-domande-collegate/dettaglio-domande-collegate/dettaglio-domande-collegate.component";
import { SintesiDomandeContainerComponent } from './sintesi-domande/dettaglio-domande-collegate/sintesi-domande-container/sintesi-domande-container.component';
import { GestioneRettificaDomandaUMAComponent } from "../uma/features/gestione-rettifica-domanda-uma/gestione-rettifica-domanda-uma.component";
import { UmaLabels } from "../uma/uma.labels";

export const routes: Routes = [
  {
    path: 'cuaa/:cuaa/dettaglio',
    component: FascicoloDettaglioContainerComponent,
    data: {
      breadcrumbTranslateKey: "FAS_ANA.FASCICOLO_AZIENDALE",
      mybreadcrumb: Labels.FASCICOLO_AZIENDALE
    },
    loadChildren: () => import('./fascicolo-dettaglio/fascicolo-dettaglio.module').then(m => m.FascicoloDettaglioModule)
  },
  {
    path: ':idFascicolo',
    component: HeaderFascicoloComponent,
    data: {
      mybreadcrumb: Labels.GESTIONE_AZIENDA
    },
    children: [
      // {
      //   path: 'presentazioneIstanze',
      //   component: PresentazioneIstanzeComponent,
      //   data: { mybreadcrumb: '' }
      // },
      {
        path: 'rettifiche/:cuaa',
        component: GestioneRettificaDomandaUMAComponent,
        data: { mybreadcrumb: UmaLabels.BREADCRUMB.gestione_rettifica }
      },
      // {
      //   path: "antimafia",
      //   loadChildren: () => import('./antimafia/antimafia.module').then(m => m.AntimafiaModule),
      //   data: { mybreadcrumb: '' }
      // },
      // {
      //   path: 'mandato',
      //   loadChildren: () => import('./mandato/mandato.module').then(m => m.MandatoModule),
      //   data: { mybreadcrumb: Labels.mandato }
      // },
      // {
      //   path: 'sintesiDomande',
      //   component: SintesiDomandeContainerComponent,
      //   data: { mybreadcrumb: Labels.CONSULTAZIONE_ANTIMAFIA },
      //   children: [
      //     {
      //       path: '',
      //       component: SintesiDomandeComponent
      //     },
      //     {
      //       path: 'dettaglioDomandeCollegate',
      //       component: DettaglioDomandeCollegateComponent,
      //       pathMatch: 'full',
      //       data: { mybreadcrumb: Labels.DOMANDE_COLLEGATE }
      //     }
      //   ]
      // }
      // {
      //   path: 'domandaunica/domandaintegrativa',
      //   component: DomandaIntegrativaComponent,
      //   pathMatch: 'full',
      //   data: { mybreadcrumb: Labels.DOMANDA_INTEGRATIVA }
      // },
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  providers: [FascicoloCorrente]
})
export class FascicoloRoutingModule { }
