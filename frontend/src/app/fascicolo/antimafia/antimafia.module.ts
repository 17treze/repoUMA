import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AntimafiaRoutingModule } from './antimafia-routing.module';
import { SintesiAntimafiaListComponent } from './sintesi-antimafia-list/sintesi-antimafia-list.component';
import { DichiarazioneAntimafiaComponent } from './dichiarazione-antimafia/dichiarazione-antimafia.component';
import { FormsModule } from '@angular/forms';
import { A4gCommonModule } from '../../a4g-common/a4g-common.module';
import { DichiarazioneAntimafiaService } from './dichiarazione-antimafia.service';
import { DichiarazioneResolve } from './dichiarazione-antimafia/dichiarazioneResolve';
import { RichiedenteComponent } from './modulo/richiedente/richiedente.component';
import { ImpresaComponent } from './modulo/impresa/impresa.component';
import { SoggettiComponent } from './modulo/soggetti/soggetti.component';
import { FamiliariComponent } from './modulo/familiari/familiari.component';
import { VerificaComponent } from './modulo/verifica/verifica.component';
import { AziendeComponent } from './modulo/aziende/aziende.component';
import { AntimafiaComponent } from './antimafia/antimafia.component';
import { SelectRichiedenteComponent } from './select-richiedente/select-richiedente.component';
import { DateFormatPipe } from '../../a4g-common/DateFormatPipe';
import { TableModule } from 'primeng/table';
import { CheckboxModule } from 'primeng/checkbox';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import { StepsModule } from 'primeng/steps';
import { MessageService } from 'primeng/api';
import { DataViewModule } from 'primeng/dataview';
import { DropdownModule } from 'primeng/dropdown';
import { AccordionModule } from 'primeng/accordion';
import { PanelModule } from 'primeng/panel';
import { StepActionComponent } from './modulo/step-action/step-action.component';
import { ToastModule } from 'primeng/toast';
import { InplaceModule } from 'primeng/inplace';
import { FieldsetModule } from 'primeng/fieldset';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { BlockUIModule } from 'primeng/blockui';
import { ProcedimentiComponent } from './modulo/procedimenti/procedimenti.component';

@NgModule({
  imports: [
    CommonModule,
    AntimafiaRoutingModule,
    FormsModule,
    A4gCommonModule,
    TableModule,
    CheckboxModule,
    ButtonModule,
    DialogModule,
    StepsModule,
    DataViewModule,
    DropdownModule,
    AccordionModule,
    PanelModule,
    ToastModule,
    InplaceModule,
    FieldsetModule,
    ConfirmDialogModule,
    ProgressSpinnerModule,
    BlockUIModule,
  ],
  declarations: [
    AntimafiaComponent,
    SintesiAntimafiaListComponent,
    DichiarazioneAntimafiaComponent,
    RichiedenteComponent,
    ImpresaComponent,
    SoggettiComponent,
    FamiliariComponent,
    VerificaComponent,
    AziendeComponent,
    SelectRichiedenteComponent,
    StepActionComponent,
    ProcedimentiComponent
  ],
  providers: [
    DichiarazioneAntimafiaService,
    DichiarazioneResolve,
    DateFormatPipe,
    MessageService,
  ]
})
export class AntimafiaModule { }
