import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { CrmGuard } from 'src/app/auth/crm.guard';
import { SommarioIstruttoria1420Component } from './sommario-istruttoria1420/sommario-istruttoria1420.component';

const routes: Routes = [
    {
        path: '',
        component: SommarioIstruttoria1420Component,
        pathMatch: 'full',
    }, {
        path: ':annoCampagna',
        component: SommarioIstruttoria1420Component,
        pathMatch: 'full',
        data: {
            mybreadcrumb: ''
        }
    }, {
        path: ':annoCampagna/configurazione',
        loadChildren: () => import('./domandaUnica/sostegno-shared/sostegno-shared.module').then(m => m.SostegnoSharedModule),
        canLoad: [CrmGuard],
        data: {
            mybreadcrumb: 'Configurazione Istruttoria'
        }
    },
    {
        path: ':annoCampagna/disaccoppiato/:tipo',
        loadChildren: () => import('./domandaUnica/sostegno-disaccoppiato/disaccoppiato.module').then(m => m.DisaccoppiatoModule),
        canLoad: [CrmGuard],
        data: {
            mybreadcrumb: 'Disaccoppiato'
        }
    },
    {
        path: ':annoCampagna/superficie/saldo', //:tipo
        loadChildren: () => import('./domandaUnica/sostegno-accoppiato-superfici/superfici.module').then(m => m.SuperficiModule),
        canLoad: [CrmGuard],
        data: {
            mybreadcrumb: 'Superfici'
        }
    },
    {
        path: ':annoCampagna/zootecnia/saldo',
        loadChildren: () => import('./domandaUnica/sostegno-accoppiato-zootecnia/zootecnia.module').then(m => m.ZootecniaModule),
        canLoad: [CrmGuard],
        data: {
            mybreadcrumb: 'Zootecnia Saldo'
        }
    }

    /*
    DA IMPLEMENTARE
    {
        path: ':annoCampagna/zootecnia/integrazione',
        loadChildren: './domandaUnica/sostegno-accoppiato-zootecnia/zootecnia.module#ZootecniaModule',
        canLoad: [CrmGuard],
        data: {
            mybreadcrumb: 'Zootecnia Saldo'
        }
    }
    */
];

@NgModule({
    imports: [CommonModule, RouterModule.forChild(routes)]
})
export class IstruttoriaPac1420RoutingModule { }
