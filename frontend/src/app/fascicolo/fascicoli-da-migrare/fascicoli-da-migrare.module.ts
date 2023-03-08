import { FascicoloSharedModule } from './../shared/fascicolo-shared.module';
import { A4gCommonModule } from 'src/app/a4g-common/a4g-common.module';
import { FiltroRicercaFascicoliDaMigrareComponent } from './ricerca-fascicoli-da-migrare/filtro-ricerca-fascicoli-da-migrare/filtro-ricerca-fascicoli-da-migrare.component';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { FascicoliDaMigrareRoutingModule } from './fascicoli-da-migrare-routing.module';
import { RicercaFascicoliDaMigrareComponent } from './ricerca-fascicoli-da-migrare/ricerca-fascicoli-da-migrare.component';

@NgModule({
  imports: [
    A4gCommonModule,
    CommonModule,
    FascicoloSharedModule,
    FascicoliDaMigrareRoutingModule
  ],
  declarations: [
    RicercaFascicoliDaMigrareComponent,
    FiltroRicercaFascicoliDaMigrareComponent
  ]
})
export class FascicoliDaMigrareModule { }
