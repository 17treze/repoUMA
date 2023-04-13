import { SharedModule } from '../../shared/shared.module';
import { DichiarazioniAssociativeListComponent } from './dichiarazioni-associative-list/dichiarazioni-associative-list.component';
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { DichiarazioniAssociativeRoutingModule } from './dichiarazioni-associative-routing.module';
import { CalendarModule } from 'primeng-lts/calendar';
import { DichiarazioniAssociativeDialogComponent } from './dichiarazioni-associative-dialog/dichiarazioni-associative-dialog.component';
import { DichiarazioniAssociativeContainerComponent } from './dichiarazioni-associative-container/dichiarazioni-associative-container.component';
import { ReactiveFormsModule } from '@angular/forms';


@NgModule({
  declarations: [
    DichiarazioniAssociativeListComponent,
    DichiarazioniAssociativeDialogComponent,
    DichiarazioniAssociativeContainerComponent
  ],
  imports: [
    SharedModule,
    CommonModule,
    DichiarazioniAssociativeRoutingModule,
    CalendarModule,
    ReactiveFormsModule
  ]
})
export class DichiarazioniAssociativeModule { }
