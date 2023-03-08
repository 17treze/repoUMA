import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { DichiarazioneAntimafiaComponent } from './dichiarazione-antimafia/dichiarazione-antimafia.component';
import { SintesiAntimafiaListComponent } from './sintesi-antimafia-list/sintesi-antimafia-list.component';
import { DichiarazioneAntimafiaService } from './dichiarazione-antimafia.service';
import { A4gCommonModule } from '../../a4g-common/a4g-common.module';
import { DichiarazioneResolve } from './dichiarazione-antimafia/dichiarazioneResolve';
import { AntimafiaComponent } from './antimafia/antimafia.component';
import { SelectRichiedenteComponent } from './select-richiedente/select-richiedente.component';


const routes: Routes = [
    { path: '', component: AntimafiaComponent, pathMatch: 'full'},
    { path: 'lista', component: SintesiAntimafiaListComponent, pathMatch: 'full'},
    { path: 'richiedente', component: SelectRichiedenteComponent, pathMatch: 'full'},
    { path: ':idDichiarazione', component: DichiarazioneAntimafiaComponent,
      resolve: {
        dichiarazione: DichiarazioneResolve
      },
      data: {
        mybreadcrumb: "Domanda Antimafia"
      }
    }
];


@NgModule({
  imports: [RouterModule.forChild(routes), A4gCommonModule],
  providers: [{ provide: DichiarazioneAntimafiaService, useClass: DichiarazioneAntimafiaService }]
})
export class AntimafiaRoutingModule { }

