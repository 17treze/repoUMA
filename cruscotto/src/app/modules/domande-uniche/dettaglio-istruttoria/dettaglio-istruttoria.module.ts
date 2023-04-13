import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SharedModule } from 'src/app/shared/shared.module';
import { DettaglioIstruttoriaRoutingModule } from './dettaglio-istruttoria-routing.module';
import { EsitiIstruttoriaComponent } from './esiti-istruttoria/esiti-istruttoria.component';
import { EsitiIstruttoriaZootecniaComponent } from './esiti-istruttoria-zootecnia/esiti-istruttoria-zootecnia.component';
import { EsitiIstruttoriaSuperficieComponent } from './esiti-istruttoria-superficie/esiti-istruttoria-superficie.component';



@NgModule({
  declarations: [
    EsitiIstruttoriaSuperficieComponent,
    EsitiIstruttoriaComponent,
    EsitiIstruttoriaZootecniaComponent],
  imports: [
    SharedModule,
    CommonModule,
    DettaglioIstruttoriaRoutingModule
  ]
})
export class DettaglioIstruttoriaModule { }
