import { LimitSumArray } from './directives/limit-sum-array.validator';
import { FascicoloApertoComponent } from './../fascicolo/fascicolo-aperto/fascicolo-aperto.component';
import { NgModule, ModuleWithProviders } from "@angular/core";
import { CommonModule } from "@angular/common";
import { UploadComponent } from "./uploadFile/upload/upload.component";
import { ValidaCuaaDirective } from "./validazione/valida-cuaa.directive";
import { ValidaDenominazioneDirective } from "./validazione/valida-denominazione.directive";
import { FormsModule, ReactiveFormsModule } from "@angular/forms";
import { CanDeactivateGuardService } from "./can-deactivate-guard.service";
import { TabMenuModule } from "primeng/tabmenu";
import { PanelModule } from "primeng/panel";
import { BreadcrumbModule } from "primeng/breadcrumb";
import { ButtonModule } from "primeng/button";
import { CheckboxModule } from "primeng/checkbox";
import { RadioButtonModule } from "primeng/radiobutton";
import { ConfirmDialogModule } from "primeng/confirmdialog";
import { MessageService } from "primeng/api";
import { FileUploadModule } from "primeng/fileupload";
import { ListboxModule } from "primeng/listbox";
import { MultiSelectModule } from "primeng/multiselect";
import { InputSwitchModule } from "primeng/inputswitch";
import { TooltipModule } from "primeng/tooltip";
import { FieldsetModule } from "primeng/fieldset";
import { AutoCompleteModule } from "primeng/autocomplete";
import { AccordionModule } from "primeng/accordion";
import { DropdownModule } from "primeng/dropdown";
import { TreeModule } from "primeng/tree";
import { InputTextModule } from "primeng/inputtext";
import { OverlayPanelModule } from "primeng/overlaypanel";
import { ScrollPanelModule } from "primeng/scrollpanel";
import { DialogModule } from "primeng/dialog";
import { StepsModule } from "primeng/steps";
import { TabViewModule } from "primeng/tabview";
import { ProgressSpinnerModule } from "primeng/progressspinner";
import { AppBreadcrumbComponent } from "./app-breadcrumb/app.breadcrumb.component";
import { ValidaCuaaCompletoDirective } from "./validazione/valida-cuaa-completo.directive";
import { TableModule } from "primeng/table";
import { ValidaTelefonoDirective } from "./validazione/valida-telefono.directive";
import { ValidaEmailDirective } from "./validazione/valida-email.directive";
import { ToastModule } from "primeng/toast";
import { CardModule } from 'primeng/card';
import { ValidaCfDirective } from "./validazione/valida-cf.directive";
import {
  ActionMenuComponent,
  ActionSubMenuComponent
} from "./action-menu.component";
import { DebounceClickDirective } from "../directive/debounceClickDirective";
import { HackPtabMenuComponent } from "../istruttoria/istruttoria-pac1420/domandaUnica/hack-ptab-menu/hack-ptab-menu.component";
import { TranslateModule } from "@ngx-translate/core";
import { ScadenzeAppagComponent } from "../funzionalita/scadenze-appag/scadenze-appag.component";
import { A4gPagedTableModule } from "./a4g-paged-table/a4g-paged-table.component";
import { ValidaNomeDirective } from "./validazione/valida-nome.directive";
import { ValidaCognomeDirective } from "./validazione/valida-cognome.directive";
import { HasPermissionDirective } from './directives/has-permission.directive';
import { TipoFilter, FixDichiarazioni } from "../istruttoria/istruttoria-pac1420/domandaUnica/domain/pipes";
import { ErrorPageComponent } from './errorPage/error-page.component';
import { MessagesModule } from 'primeng/messages';
import { MessageModule } from 'primeng/message';
import { CalendarModule } from 'primeng/calendar';
import { PaginatorService } from './services/paginator.service';
import { A4gAccordionTabComponent } from './a4g-accordion-tab/a4g-accordion-tab.component';
import { A4gShowMoreShowLessComponent } from './a4g-show-more-show-less/a4g-show-more-show-less.component';
import { SoggettiConCaricheService } from './services/soggetti-con-cariche.service';
import { DateUtilService } from './services/date-util.service';
import { A4gMultiTableComponent } from './a4g-multi-table/a4g-multi-table.component';
import { MaxInputSize } from './directives/max-input-size.validator';
import { LimitSup } from './directives/limit-sup.validator';
import { LimitSum } from './directives/limit-sum.validator';
import { NotEmptyList } from './directives/not-empty-list.validator';
import { NotZero } from './directives/not-zero.validator';
import { ButtonSpinnerTemplateComponent } from './directives/button-spinner-template/button-spinner-template.component';
import { ButtonLoadingDirective } from './directives/button-loading.directive';
import { TipologicheService } from './services/tipologiche.service';
import { ClearStringPipe } from './pipes/clear-string.pipe';
import { ReplaceWithSpaceByPipe } from './pipes/replace-with-space-by.pipe';
import { CalendarService } from './services/calendar.service';
import { ErrorService } from './services/error.service';
import { PickListModule } from 'primeng-lts/picklist';
import { SupportoDescriptionPipe } from './pipes/supporto-description.pipe';
import { CapitalizeFirstLetterOnlyPipe } from './pipes/capitalize-first-letter-only.pipe';
import { A4gMultiTableService } from './a4g-multi-table/a4g-multi-table.service';
import { NavigatorService } from './services/navigator.service';
import { HttpClientConfigUmaService } from './services/http-client-config-uma.service';
import { HttpClientCoreService } from './services/http-client-core.service';
import { HttpHelperService } from './services/http-helper.service';
import { FormService } from './services/form.service';
import { TieredMenuModule } from 'primeng/tieredmenu';
import { MaxValue } from './directives/max-val.validator';
import {SpinnerModule} from 'primeng/spinner';
import { DotazioneTecnicaBuilderService } from './services/builders/dotazione-tecnica-builder.service';
import { HttpClientTipologieService } from './services/http-client-tipologie.service';
import { HttpClientMacchineService } from './services/http-client-macchine.service';
import { BoxAllegatiComponent } from './box-allegati/box-allegati.component';
import { BoxComponent } from './box/box.component';
import { HttpClientElenchiService } from './services/http-client-elenchi.service';
import { PopupScaricaElenchiUmaComponent } from './popup-scarica-elenchi-uma/popup-scarica-elenchi-uma.component';
import { LimitSumNoInput } from './directives/limit-sum-no-input.directive';
import { SelectButtonModule } from 'primeng-lts';
import { Iban } from './directives/iban.validator';
import { ExitConfirmDirective } from './exit-confirm/exit-confirm.directive';
import { ExitConfirmDialogComponent } from './exit-confirm/exit-confirm-dialog/exit-confirm-dialog.component';

@NgModule({
  imports: [
    DialogModule,
    CommonModule,
    FormsModule,
    TabMenuModule,
    PanelModule,
    ButtonModule,
    BreadcrumbModule,
    TieredMenuModule,
    TableModule,
    InputTextModule,
    CheckboxModule,
    RadioButtonModule,
    ConfirmDialogModule,
    ToastModule,
    CardModule,
    FileUploadModule,
    ListboxModule,
    MultiSelectModule,
    InputSwitchModule,
    TooltipModule,
    FieldsetModule,
    AutoCompleteModule,
    AccordionModule,
    DropdownModule,
    TreeModule,
    PickListModule,
    A4gPagedTableModule,
    ReactiveFormsModule,
    MessagesModule,
    MessageModule,
    CalendarModule,
    TranslateModule,
    OverlayPanelModule,
    ScrollPanelModule,
    DialogModule,
    StepsModule,
    TabViewModule,
    ProgressSpinnerModule,
    SpinnerModule,
    SelectButtonModule
  ],
  declarations: [
    ExitConfirmDialogComponent,
    ExitConfirmDirective,
    UploadComponent,
    ValidaCuaaDirective,
    ValidaCuaaCompletoDirective,
    ValidaCfDirective,
    ValidaDenominazioneDirective,
    AppBreadcrumbComponent,
    ValidaTelefonoDirective,
    ValidaEmailDirective,
    ErrorPageComponent,
    ActionMenuComponent,
    ActionSubMenuComponent,
    DebounceClickDirective,
    ScadenzeAppagComponent,
    HackPtabMenuComponent,
    ValidaNomeDirective,
    ValidaCognomeDirective,
    HasPermissionDirective,
    TipoFilter,
    FixDichiarazioni,
    A4gAccordionTabComponent,
    A4gShowMoreShowLessComponent,
    FascicoloApertoComponent,
    A4gMultiTableComponent,
    MaxInputSize,
    LimitSup,
    LimitSum,
    MaxValue,
    LimitSumArray,
    LimitSumNoInput,
    NotEmptyList,
    NotZero,
    ButtonSpinnerTemplateComponent,
    ButtonLoadingDirective,
    ClearStringPipe,
    ReplaceWithSpaceByPipe,
    SupportoDescriptionPipe,
    CapitalizeFirstLetterOnlyPipe,
    BoxComponent,
    BoxAllegatiComponent,
    PopupScaricaElenchiUmaComponent,
    Iban
  ],
  exports: [
    ExitConfirmDialogComponent,
    ExitConfirmDirective,
    CommonModule,
    FormsModule,
    PanelModule,
    ButtonModule,
    BreadcrumbModule,
    TieredMenuModule,
    TabMenuModule,
    TableModule,
    InputTextModule,
    CheckboxModule,
    RadioButtonModule,
    ConfirmDialogModule,
    ToastModule,
    CardModule,
    FileUploadModule,
    UploadComponent,
    ValidaCuaaDirective,
    ValidaCuaaCompletoDirective,
    ValidaCfDirective,
    ValidaDenominazioneDirective,
    ValidaTelefonoDirective,
    ValidaEmailDirective,
    AppBreadcrumbComponent,
    ListboxModule,
    MultiSelectModule,
    InputSwitchModule,
    TooltipModule,
    FieldsetModule,
    ActionMenuComponent,
    ActionSubMenuComponent,
    AutoCompleteModule,
    AccordionModule,
    DropdownModule,
    TreeModule,
    PickListModule,
    DebounceClickDirective,
    HackPtabMenuComponent,
    TranslateModule,
    A4gPagedTableModule,
    ReactiveFormsModule,
    ValidaNomeDirective,
    ValidaCognomeDirective,
    HasPermissionDirective,
    TipoFilter,
    FixDichiarazioni,
    MessagesModule,
    MessageModule,
    CalendarModule,
    A4gAccordionTabComponent,
    A4gShowMoreShowLessComponent,
    FascicoloApertoComponent,
    A4gMultiTableComponent,
    OverlayPanelModule,
    ScrollPanelModule,
    DialogModule,
    MaxInputSize,
    StepsModule,
    TabViewModule,
    LimitSup,
    LimitSum,
    MaxValue,
    LimitSumArray,
    LimitSumNoInput,
    NotEmptyList,
    NotZero,
    ProgressSpinnerModule,
    ButtonSpinnerTemplateComponent,
    ButtonLoadingDirective,
    ClearStringPipe,
    ReplaceWithSpaceByPipe,
    SupportoDescriptionPipe,
    CapitalizeFirstLetterOnlyPipe,
    SpinnerModule,
    BoxComponent,
    BoxAllegatiComponent,
    PopupScaricaElenchiUmaComponent,
    SelectButtonModule,
    Iban
  ],
  entryComponents: [ButtonSpinnerTemplateComponent, PopupScaricaElenchiUmaComponent],
  providers: [
    CanDeactivateGuardService,
    MessageService,
    FixDichiarazioni,
    PaginatorService,
    SoggettiConCaricheService,
    DateUtilService,
    TipologicheService,
    CalendarService,
    ErrorService,
    A4gMultiTableService,
    NavigatorService,
    HttpClientCoreService,
    HttpClientConfigUmaService,
    HttpHelperService,
    FormService,
    DotazioneTecnicaBuilderService,
    HttpClientTipologieService,
    HttpClientMacchineService,
    HttpClientElenchiService
  ]
})
export class A4gCommonModule {
  static forRoot(): ModuleWithProviders<A4gCommonModule> {
    return {
      ngModule: A4gCommonModule
    };
  }
}
