import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";
import { Costanti } from '../Costanti';
import { ContainerZootecniaComponent } from "./container/container-zootecnia.component";

const routes: Routes = [
  { 
    path: '', 
    redirectTo: Costanti.PREMIO, 
    pathMatch: 'full'
  },
  {
    path: '',
    component: ContainerZootecniaComponent,
    children: [
      {
        path: Costanti.PREMIO,
        loadChildren: () => import('src/app/istruttoria/istruttoria-pac1420/domandaUnica/sostegno-accoppiato-zootecnia/calcolo-premio/az-calcolo-premio.module').then(m => m.AzCalcoloPremioModule),
        data: {
          mybreadcrumb: 'Calcolo Premio'
        }
      },
      {
        path: Costanti.LIQUIDABILITA,
        loadChildren: () => import('src/app/istruttoria/istruttoria-pac1420/domandaUnica/sostegno-accoppiato-zootecnia/controlli-liquidabilita/az-controlli-liquidabilita.module').then(m => m.AzControlliLiquidabilitaModule),
        data: {
          mybreadcrumb: 'Controlli Liquidabilità'
        }
      },
      {
        path: Costanti.LIQUIDAZIONE,
        loadChildren: () => import('src/app/istruttoria/istruttoria-pac1420/domandaUnica/sostegno-accoppiato-zootecnia/liquidazione/az-liquidazione.module').then(m => m.AzLiquidazioneModule),
        data: {
          mybreadcrumb: 'Liquidazione'
        }
      }
    ]
  },
  {
    path: 'istruttoria/:idIstruttoria',
    loadChildren: () => import('src/app/istruttoria/istruttoria-pac1420/domandaUnica/sostegno-dettaglio/sostegno-dettaglio-zootecnia.module').then(m => m.SostegnoDettaglioZootecniaModule),
    data: {
      mybreadcrumb: 'Dettaglio'
    }
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
})
export class ZootecniaRoutingModule { }
