import { NgModule } from '@angular/core';
import { RichiesteRevocaMandatoComponent } from './richieste-revoca-mandato.component';
import { Routes, RouterModule } from '@angular/router';
import { A4gCommonModule } from 'src/app/a4g-common/a4g-common.module';
import { DialogModule } from 'primeng/dialog';
import { TabViewModule } from 'primeng/tabview';
import { RichiestaRevocaMandatoDaValutareComponent } from './richiesta-revoca-mandato-da-valutare/richiesta-revoca-mandato-da-valutare.component';
import { RichiestaRevocaMandatoValutateComponent } from './richiesta-revoca-mandato-valutate/richiesta-revoca-mandato-valutate.component';
import { RichiestaRevocaMandatoRifiutoDialogComponent } from './richiesta-revoca-mandato-da-valutare/richiesta-revoca-mandato-rifiuto-dialog/richiesta-revoca-mandato-rifiuto-dialog.component';
import { RichiestaValutataEventService } from './richiesta-valutata-event.service';

const routes: Routes = [
  {
    path: '',
    component: RichiesteRevocaMandatoComponent,
    pathMatch: 'full'
  }
];

@NgModule({
  imports: [
    A4gCommonModule,
    TabViewModule,
    DialogModule,
    RouterModule.forChild(routes)
  ], declarations: [
    RichiesteRevocaMandatoComponent,
    RichiestaRevocaMandatoDaValutareComponent,
    RichiestaRevocaMandatoValutateComponent,
    RichiestaRevocaMandatoRifiutoDialogComponent
  ], providers: [
    RichiestaValutataEventService
  ]
})

export class RichiesteRevocaMandatoModule { }
