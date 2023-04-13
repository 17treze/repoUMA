import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { ContainerDomandePsrStrutturaliComponent } from './container-domande-psr-strutturali/container-domande-psr-strutturali.component';
import { ContainerDettaglioDomandaPsrStruttComponent } from './container-dettaglio-psr-strutturale/container-dettaglio-psr-strutturale.component';
import { ListaDomandePsrStrutturaliComponent } from './lista-domande-psr-strutturali/lista-domande-psr-strutturali.component';
import { DettaglioInvestimentiComponent } from './dettaglio-istruttoria-psr-strutturale/dettaglio-finanziabilita/dettaglio-investimenti.component';
import { DettagliDomandaPrsStrutturaleBackButtonResolver } from './domanda-prs-strutturale-back-button-resolver.service';
import { DettaglioInterventoPsrStrutturaleComponent } from './dettaglio-istruttoria-psr-strutturale/dettaglio-intervento/dettaglio-intervento-psr-strutturale.component';


const routes: Routes = [
  {
    path: ':cuaa',
    component: ContainerDomandePsrStrutturaliComponent,
    children: [
      { path: '', pathMatch: 'full', redirectTo: 'lista' },
      {
        path: 'lista',
        component: ListaDomandePsrStrutturaliComponent,
        data: {
          breadcrumb: 'Cruscotto'
        }
      }
    ]
  },
  {
    path: ':idDomanda/dettaglioPsrStrutturali',
    component: ContainerDettaglioDomandaPsrStruttComponent,
    data: {
      breadcrumb: 'Domanda PSR Strutturale'
    }
  },
  {
    path: ':idDomanda/dettaglioPsrStrutturali/dettaglio/:tipologia',
    component: DettaglioInvestimentiComponent,
    data: {
      breadcrumb: 'Domande PSR Strutturali',
      hideHeaderIcons: true
    },
    resolve: {
      backButtonDetail: DettagliDomandaPrsStrutturaleBackButtonResolver
    }
  },
  {
    path: ':idDomanda/dettaglioPsrStrutturali/dettaglio/:tipologia/:idDomandaPagamento',
    component: DettaglioInvestimentiComponent,
    data: {
      breadcrumb: 'Domande PSR Strutturali',
      hideHeaderIcons: true
    },
    resolve: {
      backButtonDetail: DettagliDomandaPrsStrutturaleBackButtonResolver
    }
  },
  {
    path: ':idDomanda/dettaglioPsrStrutturali/dettaglio/:tipologia/intervento/:idIntervento',
    component: DettaglioInterventoPsrStrutturaleComponent,
    data: {
      breadcrumb: 'Domande PSR Strutturali',
      hideHeaderIcons: true
    },
    resolve: {
      backButtonDetail: DettagliDomandaPrsStrutturaleBackButtonResolver
    }
  },
  {
    path: ':idDomanda/dettaglioPsrStrutturali/dettaglio/:tipologia/:idDomandaPagamento/intervento/:idIntervento',
    component: DettaglioInterventoPsrStrutturaleComponent,
    data: {
      breadcrumb: 'Domande PSR Strutturali',
      hideHeaderIcons: true
    },
    resolve: {
      backButtonDetail: DettagliDomandaPrsStrutturaleBackButtonResolver
    }
  },
  {
    path: ':idDomanda/DettaglioDomandaPsrStrutturale',
    component: ContainerDettaglioDomandaPsrStruttComponent,
    data: {
      breadcrumb: 'Domande PSR Strutturali'
    }
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)]
})
export class DomandePsrStrutturaliRoutingModule { }
