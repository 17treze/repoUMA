import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ErrorPageComponent } from './modules/common/errorPage/error-page.component';
import { LoggedInGuard } from './shared/security/logged-in.guard';
import { ContainerHomeComponent } from './modules/home/container-home/container-home.component';

const routes: Routes = [
  {
    path: '',
    canActivate: [LoggedInGuard],
    children: [
      {
        path: '',
        component: ContainerHomeComponent,
      },
      {
        path: 'domande-uniche',
        loadChildren: () => import('./modules/domande-uniche/domande-uniche.module').then(m => m.DomandeUnicheModule)
      },
      {
        path: 'psr',
        loadChildren: () => import('./modules/domande-psr/domande-psr.module').then(m => m.DomandePsrModule)
      },
      {
        path: 'psr-strutturali',
        loadChildren: () => import('./modules/domande-psr-strutturali/domande-psr-strutturali.module').then(m => m.DomandePsrStrutturaliModule)
      },
      {
        path: 'fascicolo-dettaglio',
        loadChildren: () => import('./modules/fascicolo-dettaglio/fascicolo-dettaglio.module').then(m => m.FascicoloDettaglioModule)
      },
      {
        path: 'antimafia',
        loadChildren: () => import('./modules/dichiarazione-antimafia/dichiarazione-antimafia.module').then(m => m.DichiarazioneAntimafiaModule)
      },
      {
        path: 'domande-uma',
        loadChildren: () => import('./modules/domande-uma/domande-uma.module').then(m => m.DomandeUmaModule)
      },
      {
        path: 'dichiarazioni-associative',
        loadChildren: () => import('./modules/dichiarazioni-associative/dichiarazioni-associative.module').then(m => m.DichiarazioniAssociativeModule)
      },
      {
        path: 'crea-fascicolo',
        loadChildren: () => import('./modules/crea-fascicolo/crea-fascicolo.module').then(m => m.CreaFascicoloModule)
      }
    ]
  },
  {
    path: 'register',
    loadChildren: () => import('./modules/register/register.module').then(m => m.RegisterModule)
  },
  {
    path: 'register-crea-fascicolo',
    loadChildren: () => import('./modules/register-crea-fascicolo/register-crea-fascicolo.module').then(m => m.RegisterCreaFascicoloModule)
  },
  {
    path: 'errorPage/:error',
    component: ErrorPageComponent
  },
  {
    path: '**',
    component: ErrorPageComponent
  }
];
@NgModule({
  imports: [RouterModule.forRoot(routes)]
})
export class AppRoutingModule { }
