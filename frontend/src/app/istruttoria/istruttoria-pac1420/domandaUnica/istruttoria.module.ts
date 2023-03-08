import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { DettaglioIstruttoriaComponent } from './dettaglio-istruttoria/dettaglio-istruttoria.component';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { ProgressBarModule } from 'primeng/progressbar';
import { ElencoDomandeComponent } from './elenco-domande/elenco-domande.component';
import { ElencoDomandeService } from './services/elenco-domande.service';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { registerLocaleData } from '@angular/common';
import localeIt from '@angular/common/locales/it';
import localeItExtra from '@angular/common/locales/extra/it';
import { CruscottoSostegnoComponent } from './cruscotto-sostegno/cruscotto-sostegno.component';
import { TreeTableModule } from 'primeng/treetable';
import { TabMenuModule } from 'primeng/tabmenu';
import { CheckboxModule } from 'primeng/checkbox';
import { AccordionModule } from 'primeng/accordion';
import { CardModule } from 'primeng/card';
import { MultiSelectModule } from 'primeng/multiselect';
import { DropdownModule } from 'primeng/dropdown';
import { TabViewModule } from 'primeng/tabview';
import { CruscottoDisaccoppiatoComponent } from './cruscotto-disaccoppiato/cruscotto-disaccoppiato.component';
import { DettaglioDomandaComponent } from './dettaglioDomanda/dettaglio-domanda/dettaglio-domanda.component';
import { DatiDomandaComponent } from './dettaglioDomanda/dati-domanda/dati-domanda.component';
import { InformazioniDomandaComponent } from './dettaglioDomanda/informazioni-domanda/informazioni-domanda.component';
import { SuperficiImpegnateComponent } from './dettaglioDomanda/superfici-impegnate/superfici-impegnate.component';
import { DichiarazioniComponent } from './dettaglioDomanda/dichiarazioni/dichiarazioni.component';
import { ControlliSostegnoComponent } from './dettaglioDomanda/controlli-sostegno/controlli-sostegno.component';
import { DatiParticellaComponent } from './dettaglioDomanda/dati-particella/dati-particella.component';
import { CruscottoDisaccoppiatoContainerComponent } from './cruscotto-disaccoppiato-container/cruscotto-disaccoppiato-container.component';
import { ValidaNumero4decDirective } from './valida-numero4dec.directive';
import { ValidaNumero2decDirective } from './valida-numero2dec.directive';
// tslint:disable-next-line:max-line-length
import { InserimentoDatiIstruttoriaComponent } from './dettaglioDomanda/inserimento-dati-istruttoria/inserimento-dati-istruttoria.component';
// tslint:disable-next-line:max-line-length
import { DisaccoppiatoControlliSuperatiComponent } from './dettaglioDomanda/disaccoppiato-controlli-superati/disaccoppiato-controlli-superati.component';
import { StatoDomandeCardComponent } from './stato-domande-card/stato-domande-card.component';
import { SharedService } from './shared.service';
import { InIstruttoriaDialogComponent } from './in-istruttoria-dialog/in-istruttoria-dialog.component';
import { DialogModule } from 'primeng/dialog';
import { InputTextModule } from 'primeng/inputtext';
import { DomandaIstruttoriaDettaglioResolve } from './dettaglioDomanda/DomandaIstruttoriaDettaglioResolve';
import { DomandaIstruttoriacorrente } from './dettaglioDomanda/domandaIstruttoriacorrente';
import { CalendarModule } from 'primeng/calendar';
import { AutoCompleteModule } from 'primeng/autocomplete';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ValidateInputNumberDirective } from './direttive/validate-input-number.directive';
import { DatiPerPascoloComponent } from './dettaglioDomanda/dati-per-pascolo/dati-per-pascolo.component';

import { A4gCommonModule } from 'src/app/a4g-common/a4g-common.module';
import { ValidateInputOtherNumberDirective } from './direttive/validate-input-other-number.directive';
import { SplitButtonModule } from 'primeng/splitbutton';
import { CruscottoAccoppiatiContainerComponent } from './cruscotto-accoppiati-container/cruscotto-accoppiati-container.component';
import { CruscottoAccoppiatiComponent } from './cruscotto-accoppiati-container/cruscotto-accoppiati/cruscotto-accoppiati.component';
import { DomandeAccoppiatiComponent } from './cruscotto-accoppiati-container/cruscotto-accoppiati/domande-accoppiati/domande-accoppiati.component';
import { ElencoAccoppiatiComponent } from './cruscotto-accoppiati-container/cruscotto-accoppiati/domande-accoppiati/elenco-accoppiati/elenco-accoppiati.component';
import { FiltroAccoppiatiComponent } from './cruscotto-accoppiati-container/cruscotto-accoppiati/domande-accoppiati/filtro-accoppiati/filtro-accoppiati.component';
import { DettaglioAccoppiatiComponent } from './cruscotto-accoppiati-container/cruscotto-accoppiati/domande-accoppiati/dettaglio-accoppiati/dettaglio-accoppiati.component';
import { CapiAmmessiComponent } from './cruscotto-accoppiati-container/cruscotto-accoppiati/domande-accoppiati/dettaglio-accoppiati/capi-ammessi/capi-ammessi.component';
import { DettaglioIstruttoriaDatiComponent } from './dettaglio-istruttoria/dettaglio-istruttoria-dati/dettaglio-istruttoria-dati.component';
import { DettaglioIstruttoriaDatiAczComponent } from './dettaglio-istruttoria/dettaglio-istruttoria-dati-acz/dettaglio-istruttoria-dati-acz.component';
import { DatiIstruttoriaComponent } from './cruscotto-accoppiati-container/cruscotto-accoppiati/domande-accoppiati/dettaglio-accoppiati/dati-istruttoria/dati-istruttoria.component';
import { DatiParticellaAccComponent } from './cruscotto-accoppiati-container/cruscotto-accoppiati/domande-accoppiati/dettaglio-accoppiati/dati-particella-acc/dati-particella-acc.component';
import { DatiDomandaAccComponent } from './cruscotto-accoppiati-container/cruscotto-accoppiati/domande-accoppiati/dettaglio-accoppiati/dati-domanda-acc/dati-domanda-acc.component';
import { SuperficiImpegnateAccComponent } from './cruscotto-accoppiati-container/cruscotto-accoppiati/domande-accoppiati/dettaglio-accoppiati/superfici-impegnate-acc/superfici-impegnate-acc.component';
import { CapiRichiestiComponent } from './cruscotto-accoppiati-container/cruscotto-accoppiati/domande-accoppiati/dettaglio-accoppiati/capi-richiesti/capi-richiesti.component';
import { DichiarazioniAccComponent } from './cruscotto-accoppiati-container/cruscotto-accoppiati/domande-accoppiati/dettaglio-accoppiati/dichiarazioni-acc/dichiarazioni-acc.component';
import { DettaglioIstruttoriaDatiAcsComponent } from './dettaglio-istruttoria/dettaglio-istruttoria-dati-acs/dettaglio-istruttoria-dati-acs.component';
import { LoaderService } from '../../../loader.service';
import { LoaderModule } from '../../../loader/loader.component';
import { OverlayPanelModule } from 'primeng/overlaypanel';
import { TipoFilter } from './domain/pipes';

registerLocaleData(localeIt, 'it-IT', localeItExtra);

@NgModule({
  imports: [
    A4gCommonModule,
    ConfirmDialogModule,
    AutoCompleteModule,
    CalendarModule,
    CommonModule,
    TableModule,
    ButtonModule,
    ProgressBarModule,
    FormsModule,
    TreeTableModule,
    TabMenuModule,
    DropdownModule,
    CheckboxModule,
    AccordionModule,
    CardModule,
    DialogModule,
    ReactiveFormsModule,
    InputTextModule,
    MultiSelectModule,
    SplitButtonModule,
    TabViewModule,
    TabMenuModule,
    LoaderModule,
    OverlayPanelModule
  ],
  declarations: [
    DettaglioIstruttoriaComponent,
    ElencoDomandeComponent,
    CruscottoSostegnoComponent,
    CruscottoDisaccoppiatoComponent,
    DettaglioDomandaComponent,
    DatiDomandaComponent,
    InformazioniDomandaComponent,
    SuperficiImpegnateComponent,
    InserimentoDatiIstruttoriaComponent,
    DichiarazioniComponent,
    ControlliSostegnoComponent,
    DatiParticellaComponent,
    DisaccoppiatoControlliSuperatiComponent,
    CruscottoDisaccoppiatoContainerComponent,
    ValidaNumero4decDirective,
    ValidaNumero2decDirective,
    StatoDomandeCardComponent,
    InIstruttoriaDialogComponent,
    
    ValidateInputNumberDirective,
    DatiPerPascoloComponent,

    // Accoppiato Zootecnia e Superficie
    CruscottoAccoppiatiContainerComponent,
    CruscottoAccoppiatiComponent,
    DomandeAccoppiatiComponent,
    ElencoAccoppiatiComponent,
    FiltroAccoppiatiComponent,
    DettaglioAccoppiatiComponent,
    CapiAmmessiComponent,
    DatiIstruttoriaComponent,
    DatiParticellaAccComponent,
    DatiDomandaAccComponent,
    SuperficiImpegnateAccComponent,
    DettaglioIstruttoriaDatiComponent,
    ValidateInputOtherNumberDirective,
    DettaglioIstruttoriaDatiAczComponent,
    DettaglioIstruttoriaDatiAcsComponent,
    CapiRichiestiComponent,
    DichiarazioniAccComponent
  ],
  providers: [
    ElencoDomandeService,
    SharedService,
    DomandaIstruttoriaDettaglioResolve,
    DomandaIstruttoriacorrente,
    LoaderService,
    TipoFilter
  ]
})
export class IstruttoriaModule {}
