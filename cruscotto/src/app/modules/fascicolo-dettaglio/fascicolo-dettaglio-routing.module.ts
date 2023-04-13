import { FascicoliValidatiComponent } from './fascicoli-validati/fascicoli-validati.component';
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { DatiAnagrafeTributariaComponent } from './dati-azienda/dati-anagrafe-tributaria/dati-anagrafe-tributaria.component';
import { DatiCameraCommercioComponent } from '../fascicolo-dettaglio/dati-azienda/dati-camera-commercio/dati-camera-commercio.component';
import { ContainerFascicoloDettaglioComponent } from './container-fascicolo-dettaglio/container-fascicolo-dettaglio.component';
import { PersoneCaricaComponent } from './dati-azienda/persone-carica/persone-carica.component';
import { UnitaTecnicoEconomicheComponent } from './dati-azienda/unita-tecnico-economiche/unita-tecnico-economiche.component';
import { ModalitaPagamentoComponent } from './dati-azienda/modalita-pagamento/modalita-pagamento.component';

const routes: Routes = [
  {
    path: ':cuaa',
    component: ContainerFascicoloDettaglioComponent,
    children: [
      {
        path: '',
        redirectTo: 'anagrafe-tributaria',
      },
      {
        path: 'anagrafe-tributaria',
        component: DatiAnagrafeTributariaComponent,
        data: {
          breadcrumb: 'common.indietro'
        }
      }, {
        path: 'anagrafe-camera-commercio',
        component: DatiCameraCommercioComponent,
        data: {
          breadcrumb: 'common.indietro'
        }
      }, {
        path: 'persone-carica',
        component: PersoneCaricaComponent,
        data: {
          breadcrumb: 'common.indietro'
        }
      }, {
        path: 'unita-tecnico-economiche',
        component: UnitaTecnicoEconomicheComponent,
        data: {
          breadcrumb: 'common.indietro'
        }
      }, {
        path: 'modalita-pagamento',
        component: ModalitaPagamentoComponent,
        data: {
          breadcrumb: 'common.indietro'
        }
      }, {
        path: 'fascicoli-validati',
        component: FascicoliValidatiComponent,
        data: {
          mybreadcrumb: 'common.FASCICOLI_VALIDATI'
        }
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)]
})
export class FascicoloDettaglioRoutingModule { }

