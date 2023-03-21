import { A4gPagedTableModule } from './../modules/common/a4g-paged-table/a4g-paged-table.component';
import { CheckboxModule } from 'primeng-lts/checkbox';
import { CalendarModule } from 'primeng-lts/calendar';
import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { ReactiveFormsModule,FormsModule } from '@angular/forms';
import { TranslateModule } from "@ngx-translate/core";
import { InlineSVGModule } from 'ng-inline-svg';
import { MessageService } from 'primeng-lts';
import { ButtonModule } from 'primeng-lts/button';
import { CardModule } from 'primeng-lts/card';
import { CarouselModule } from 'primeng-lts/carousel';
import { DropdownModule } from 'primeng-lts/dropdown';
import { TableModule } from 'primeng-lts/table';
import { TabMenuModule } from 'primeng-lts/tabmenu';
import { ToastModule } from 'primeng-lts/toast';
import { DateService } from "./utilities/date.service";
import { ValidaCfDirective } from './validazione/valida-cf.directive';
import { ValidaCognomeDirective } from './validazione/valida-cognome.directive';
import { ValidaCuaaCompletoDirective } from './validazione/valida-cuaa-completo.directive';
import { ValidaCuaaDirective } from './validazione/valida-cuaa.directive';
import { ValidaDenominazioneDirective } from './validazione/valida-denominazione.directive';
import { ValidaEmailDirective } from "./validazione/valida-email.directive";
import { ValidaNomeDirective } from './validazione/valida-nome.directive';
import { ValidaTelefonoDirective } from './validazione/valida-telefono.directive';
import { InfoAziendaComponent } from '../modules/common/info-azienda/info-azienda.component';
import { ScrollToTopDirective } from './directives/scroll-to-top.directive';
import { ScrollToElementDirective } from './directives/scroll-to-element.directive';
import { DialogModule } from 'primeng-lts/dialog';
import { DateUtilService } from './services/date-util.service';

@NgModule({
  declarations: [
    ValidaEmailDirective,
    ValidaCfDirective,
    ValidaCognomeDirective,
    ValidaCuaaCompletoDirective,
    ValidaCuaaDirective,
    ValidaDenominazioneDirective,
    ValidaNomeDirective,
    ValidaTelefonoDirective,
    InfoAziendaComponent,
    ScrollToTopDirective,
    ScrollToElementDirective
  ],
  imports: [
    CommonModule,
    CardModule,
    ButtonModule,
    DropdownModule,
    FormsModule,
    ReactiveFormsModule,
    TabMenuModule,
    CarouselModule,
    TranslateModule,
    ToastModule,
    TableModule,
    InlineSVGModule,
    DialogModule,
    CalendarModule,
    CheckboxModule,
    A4gPagedTableModule
  ],
  providers: [
    DateService, MessageService, DateUtilService
  ],
  exports: [
    CommonModule,
    CardModule,
    ButtonModule,
    DropdownModule,
    FormsModule,
    ReactiveFormsModule,
    TabMenuModule,
    CarouselModule,
    TranslateModule,
    ToastModule,
    TableModule,
    InlineSVGModule,
    ValidaEmailDirective,
    ValidaCfDirective,
    ValidaCognomeDirective,
    ValidaCuaaCompletoDirective,
    ValidaCuaaDirective,
    ValidaDenominazioneDirective,
    ValidaNomeDirective,
    ValidaTelefonoDirective,
    InfoAziendaComponent,
    ScrollToTopDirective,
    ScrollToElementDirective,
    DialogModule,
    CalendarModule,
    CheckboxModule,
    A4gPagedTableModule
  ]
})
export class SharedModule { }
