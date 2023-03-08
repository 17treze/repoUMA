import { Routes, RouterModule } from "@angular/router";
import { NgModule } from "@angular/core";
import { Costanti } from "../Costanti";
import { ContainerDisaccoppiatoComponent } from "./container/container-disaccoppiato.component";

const routes: Routes = [
  { 
    path: '', 
    redirectTo: Costanti.PREMIO,
    pathMatch: 'full'
  },
  {
    path: '',
    component: ContainerDisaccoppiatoComponent,
    children: [
      {
        path: Costanti.PREMIO,
        loadChildren: () => import('src/app/istruttoria/istruttoria-pac1420/domandaUnica/sostegno-disaccoppiato/calcolo-premio/disaccoppiato-calcolo-premio.module').then(m => m.DisaccoppiatoCalcoloPremioModule),
        data: {
          mybreadcrumb: 'Calcolo Premio'
        }
      },
      {
        path: Costanti.LIQUIDABILITA,
        loadChildren: () => import('src/app/istruttoria/istruttoria-pac1420/domandaUnica/sostegno-disaccoppiato/controlli-liquidabilita/disaccoppiato-controlli-liquidabilita.module').then(m => m.DisaccoppiatoControlliLiquidabilitaModule),
        data: {
          mybreadcrumb: 'Controlli Liquidabilità'
        }
      },
      {
        path: Costanti.LIQUIDAZIONE,
        loadChildren: () => import('src/app/istruttoria/istruttoria-pac1420/domandaUnica/sostegno-disaccoppiato/liquidazione/disaccoppiato-liquidazione.module').then(m => m.DisaccoppiatoLiquidazioneModule),
        data: {
          mybreadcrumb: 'Liquidazione'
        }
      }
    ]
  },
  {
    path: 'istruttoria/:idIstruttoria',
    loadChildren: () => import('src/app/istruttoria/istruttoria-pac1420/domandaUnica/sostegno-dettaglio/sostegno-dettaglio-disaccoppiato.module').then(m => m.SostegnoDettaglioDisaccoppiatoModule),
    data: {
      mybreadcrumb: 'Dettaglio'
    }
  }
];


@NgModule({
  imports: [RouterModule.forChild(routes)]
})
export class DisaccoppiatoRoutingModule { }
