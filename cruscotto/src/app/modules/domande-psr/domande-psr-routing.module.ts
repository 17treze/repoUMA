import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ContainerDomandePsrComponent } from './container-domande-psr/container-domande-psr.component';
import { ListaDomandePsrComponent } from './lista-domande-psr/lista-domande-psr.component';
import { DomandaPsrSuperficieComponent } from './domanda-psr-superficie/domanda-psr-superficie.component';
import { DettaglioPagamentoPsrSuperficieComponent } from './domanda-psr-superficie/istruttoria-domanda-psr-superficie/dettaglio-pagamento-psr-superficie/dettaglio-pagamento-psr-superficie.component';
import { DomandaPrsSuperficieBackButtonResolverService } from './domanda-prs-superficie-back-button-resolver.service';


const routes: Routes = [
  {
    path: ':cuaa',
    component: ContainerDomandePsrComponent,
    children: [
      { path: '', pathMatch: 'full', redirectTo: 'lista' },
      {
        path: 'lista',
        component: ListaDomandePsrComponent,
        data: {
          breadcrumb: 'Cruscotto'
        }
      }
    ]
  },
  {
    path: ':idDomanda/dettaglio-psr-superficie',
    component: DomandaPsrSuperficieComponent,
    data: {
      breadcrumb: 'Domanda PSR Superficie'
    }
  },
  {
    path: ':idDomanda/dettaglio-psr-superficie/dettaglio-pagamento/:tipologia/:codiceOperazione',
    component: DettaglioPagamentoPsrSuperficieComponent,
    data: {
      breadcrumb: 'Dettaglio pagamento PSR Superficie'
    },
    resolve: {
      backButtonDetail: DomandaPrsSuperficieBackButtonResolverService
    }
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)]
})
export class DomandePsrRoutingModule { }
