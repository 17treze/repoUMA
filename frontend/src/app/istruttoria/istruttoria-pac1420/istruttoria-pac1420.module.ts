import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TableModule } from 'primeng/table';
import { CardModule } from 'primeng/card';
import { MessageModule } from 'primeng/message'
import { SommarioIstruttoria1420Component } from './sommario-istruttoria1420/sommario-istruttoria1420.component';
import { IstruttoriaPac1420RoutingModule } from './istruttoria-pac1420-routing.module';
import { TabMenuModule } from 'primeng/tabmenu';
import { SplitButtonModule } from 'primeng/splitbutton';
import { A4gCommonModule } from 'src/app/a4g-common/a4g-common.module';
import { SostegnoSharedModule } from './domandaUnica/sostegno-shared/sostegno-shared.module';
import { MenubarModule } from 'primeng/menubar';
import { SuperficiModule } from './domandaUnica/sostegno-accoppiato-superfici/superfici.module';
import { PopupDatiAggregatiIstruttoriaComponent } from './domandaUnica/sostegno-shared/popup-dati-aggregati-istruttoria/popup-dati-aggregati-istruttoria.component';
import { TabellaDatiAggregatiIstruttoriaComponent } from './domandaUnica/sostegno-shared/popup-dati-aggregati-istruttoria/tabella-dati-aggregati-istruttoria.component';
import { PopupDatiAggregatiDisaccoppiatoIstruttoriaComponent } from './domandaUnica/sostegno-shared/popup-dati-aggregati-istruttoria/popup-dati-aggregati-disaccoppiato-istruttoria.component';
import { TabellaTotaliDatiAggregatiIstruttoriaComponent } from './domandaUnica/sostegno-shared/popup-dati-aggregati-istruttoria/tabella-totali-dati-aggregati-istruttoria.component';
import { PopupDatiAggregatiZootecniaComponent } from './domandaUnica/sostegno-accoppiato-zootecnia/calcolo-premio/popup-dati-aggregati-zootecnia/popup-dati-aggregati-zootecnia.component';
import { ZootecniaModule } from './domandaUnica/sostegno-accoppiato-zootecnia/zootecnia.module';
import { IstruttoriaModule } from './domandaUnica/istruttoria.module';

@NgModule({
  imports: [
    CommonModule,
    TableModule,
    TabMenuModule,
    IstruttoriaPac1420RoutingModule,
    CardModule,
    MessageModule, 
    MenubarModule, 
    SplitButtonModule,
    A4gCommonModule,
    SostegnoSharedModule,
    SuperficiModule,
    ZootecniaModule,
    IstruttoriaModule
  ],
  declarations: [
    SommarioIstruttoria1420Component,
    PopupDatiAggregatiIstruttoriaComponent,
    PopupDatiAggregatiDisaccoppiatoIstruttoriaComponent,
    TabellaDatiAggregatiIstruttoriaComponent,
    TabellaTotaliDatiAggregatiIstruttoriaComponent
  ]
})
export class IstruttoriaPac1420Module { }
