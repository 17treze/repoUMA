import { IndiceDomandaUmaComponent } from './features/gestione-domanda-uma/indice-domanda-uma/indice-domanda-uma.component';
import { ClientiContoTerziContainerComponent } from './features/gestione-dichiarazione-consumi/sezioni-dichiarazione-consumi/clienti-conto-terzi/clienti-conto-terzi-container/clienti-conto-terzi-container.component';
import { HomeUmaComponent } from './features/home-uma/home-uma.component';
import { Routes, RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';
import { RichiedenteUmaComponent } from './features/richiedente-uma/richiedente-uma.component';
import { GestioneDomandaUmaComponent } from './features/gestione-domanda-uma/gestione-domanda-uma.component';
import { UmaLabels } from './uma.labels';
import { GestioneDichiarazioneConsumiComponent } from './features/gestione-dichiarazione-consumi/gestione-dichiarazione-consumi.component';
import { SezioniDichiarazioneConsumiComponent } from './features/gestione-dichiarazione-consumi/sezioni-dichiarazione-consumi/sezioni-dichiarazione-consumi.component';
import { PrelieviComponent } from './features/gestione-dichiarazione-consumi/sezioni-dichiarazione-consumi/prelievi/prelievi.component';
import { ConsumiComponent } from './features/gestione-dichiarazione-consumi/sezioni-dichiarazione-consumi/consumi/consumi.component';
import { RicevutiComponent } from './features/gestione-dichiarazione-consumi/sezioni-dichiarazione-consumi/ricevuti/ricevuti.component';
import { DettaglioClientiContoterziComponent } from './features/gestione-dichiarazione-consumi/sezioni-dichiarazione-consumi/clienti-conto-terzi/clienti-conto-terzi-container/dettaglio-clienti-contoterzi/dettaglio-clienti-contoterzi.component';
import { ClientiContoTerziComponent } from './features/gestione-dichiarazione-consumi/sezioni-dichiarazione-consumi/clienti-conto-terzi/clienti-conto-terzi-container/lista-clienti-conto-terzi/clienti-conto-terzi.component';
import { GestioneEsuberoCarburanteComponent } from './features/gestione-esubero-carburante/gestione-esubero-carburante.component';
import { TrasferimentiComponent } from './features/gestione-dichiarazione-consumi/sezioni-dichiarazione-consumi/trasferimenti/trasferimenti.component';
import { GestioneTrasferimentiComponent } from './features/gestione-esubero-carburante/gestione-trasferimenti/gestione-trasferimenti.component';

const routes: Routes = [
  {
    path: '',
    component: HomeUmaComponent,
    data: { mybreadcrumb: '' },
    children: [
      {
        path: 'richiedente/:tipoRichiesta',
        component: RichiedenteUmaComponent,
        data: { mybreadcrumb: UmaLabels.BREADCRUMB.richiedente }
      },
      {
        path: 'richiesta/:idDomanda',
        component: GestioneDomandaUmaComponent,
        children: [
          {
            path: '',
            redirectTo: 'indice'
          },
          {
            path: 'indice',
            component: IndiceDomandaUmaComponent,
            data: { mybreadcrumb: UmaLabels.BREADCRUMB.gestione_domanda }
          }
        ]
      },
      {
        path: 'dichiarazione-consumi/:id',
        component: GestioneDichiarazioneConsumiComponent,
        children: [
          {
            path: '',
            redirectTo: 'sezioni'
          },
          {
            path: 'sezioni',
            component: SezioniDichiarazioneConsumiComponent,
            data: { mybreadcrumb: UmaLabels.BREADCRUMB.gestione_dichiarazione_consumi }
          },
          {
            path: 'prelievi',
            component: PrelieviComponent,
            data: { mybreadcrumb: UmaLabels.BREADCRUMB.prelievi }
          },
          {
            path: 'clienti-contoterzi',
            component: ClientiContoTerziContainerComponent,
            data: { mybreadcrumb: '' },
            children: [
              {
                path: '',
                redirectTo: 'lista'
              },
              {
                path: 'lista',
                component: ClientiContoTerziComponent,
                data: { mybreadcrumb: UmaLabels.BREADCRUMB.clientiContoTerzi },
              },
              {
                path: ':idCliente',
                component: DettaglioClientiContoterziComponent,
                data: { mybreadcrumb: UmaLabels.BREADCRUMB.clientiContoTerziDettaglio }
              }]
          },
          {
            path: 'ricevuti',
            component: RicevutiComponent,
            data: { mybreadcrumb: UmaLabels.BREADCRUMB.ricevuti }
          },
          {
            path: 'consumi',
            component: ConsumiComponent,
            data: { mybreadcrumb: UmaLabels.BREADCRUMB.consumi }
          },
          {
            path: 'trasferimenti',
            component: TrasferimentiComponent,
            data: { mybreadcrumb: UmaLabels.BREADCRUMB.trasferimenti }
          }
        ]
      },
      {
        path: 'esubero-carburante/:idDomanda',
        children: [
          {
            path: '',
            redirectTo: 'sezioni'
          },
          {
            path: 'sezioni',
            component: GestioneEsuberoCarburanteComponent,
            data: { mybreadcrumb: UmaLabels.BREADCRUMB.esubero_carburante }
          },
          {
            path: 'trasferimenti',
            component: GestioneTrasferimentiComponent,
            data: { mybreadcrumb: UmaLabels.BREADCRUMB.trasferimenti }
          }
        ]
      }
    ]
  },
];

@NgModule({
  imports: [RouterModule.forChild(routes)]
})
export class UmaRoutingModule { }

