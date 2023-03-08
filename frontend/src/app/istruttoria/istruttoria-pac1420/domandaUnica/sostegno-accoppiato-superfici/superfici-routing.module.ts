import { Routes, RouterModule } from "@angular/router";
import { NgModule } from "@angular/core";
import { Costanti } from "../Costanti";
import { ContainerSuperficiComponent } from "./container/container-superfici.component";

const routes: Routes = [
  { 
    path: '', 
    redirectTo: Costanti.PREMIO,
    pathMatch: 'full'
  },
  {
    path: '',
    component: ContainerSuperficiComponent,
    children: [
      {
        path: Costanti.PREMIO,
        loadChildren: () => import('src/app/istruttoria/istruttoria-pac1420/domandaUnica/sostegno-accoppiato-superfici/calcolo-premio/superfici-calcolo-premio.module').then(m => m.SuperficiCalcoloPremioModule),
        data: {
          mybreadcrumb: 'Calcolo Premio'
        }
      },
      {
        path: Costanti.LIQUIDABILITA,
        //src\app\istruttoria\istruttoria-pac1420\domandaUnica\sostegno-accoppiato-superfici\controlli-liquidabilita\superfici-controlli-liquidabilita.module.ts
        loadChildren: () => import('src/app/istruttoria/istruttoria-pac1420/domandaUnica/sostegno-accoppiato-superfici/controlli-liquidabilita/superfici-controlli-liquidabilita.module').then(m => m.SuperficiControlliLiquidabilitaModule),
        data: {
          mybreadcrumb: 'Controlli Liquidabilità'
        }
      },
      {
        path: Costanti.LIQUIDAZIONE,
        loadChildren: () => import('src/app/istruttoria/istruttoria-pac1420/domandaUnica/sostegno-accoppiato-superfici/liquidazione/superfici-liquidazione.module').then(m => m.SuperficiLiquidazioneModule),
        data: {
          mybreadcrumb: 'Liquidazione'
        }
      }
    ]
  },
  {
    path: 'istruttoria/:idIstruttoria',
    loadChildren: () => import('src/app/istruttoria/istruttoria-pac1420/domandaUnica/sostegno-dettaglio/sostegno-dettaglio-superfici.module').then(m => m.SostegnoDettaglioSuperficiModule),
    data: {
      mybreadcrumb: 'Dettaglio'
    }
  }
];
@NgModule({
  imports: [RouterModule.forChild(routes)]
})
export class SuperficiRoutingModule { }
