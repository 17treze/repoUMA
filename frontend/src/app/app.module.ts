import { CreazioneFascicoloModule } from './fascicolo/creazione-fascicolo/creazione-fascicolo.module';
import { NgModule, ErrorHandler } from '@angular/core';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { FormsModule } from '@angular/forms';
import { HttpClientModule, HTTP_INTERCEPTORS, HttpClient } from '@angular/common/http';

import { AppComponent } from './app.component';
import { A4gRoutingModule } from './/a4g-routing.module';
import { Configuration } from './app.constants';
import { A4gCommonModule } from './a4g-common/a4g-common.module';
import { ContactComponent } from './contact/contact.component';
import { HomeComponent } from './home/home.component';
import { LoginComponent } from './login/login.component';
import { AuthService } from './auth/auth.service';
import { CrmGuard } from './auth/crm.guard';
import { HomeService } from './home/home.service';
import { RicercaFascicoliComponent } from './funzionalita/ricerca/ricerca-fascicoli/ricerca-fascicoli.component';
import { SintesiFascicoloListComponent } from './funzionalita/ricerca/sintesi-fascicolo-list/sintesi-fascicolo-list.component';
import { HomeCAAComponent } from './home-caa/home-caa.component';
import { HomePatComponent } from './home-pat/home-pat.component';
import { Labels } from './app.labels';
import { AppTopBarComponent } from './app.topbar.component';
import { AppFooterComponent } from './app.footer.component';
import { PanelMenuModule } from 'primeng/panelmenu';
import { ConfirmationService } from 'primeng/api';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { InputTextModule } from 'primeng/inputtext';
import { LoaderInterceptorService } from './loader-interceptors.service';
import { LoaderService } from './loader.service';
import { LoaderModule } from './loader/loader.component';
import { MenuCaaComponent } from './home-caa/menu-caa/menu-caa.component';
import { MenuPatComponent } from './home-pat/menu-pat/menu-pat.component';
import { PresentazioneIstanzeIstruttoriaComponent } from './funzionalita/presentazione-istanze-istruttoria/presentazione-istanze-istruttoria.component';
import { GlobalErrorHandler } from './a4g-common/handlers/GlobalErrorHandler';
import { AuthGuard } from './auth/auth.guard';
import { RoleGuard } from './auth/role.guard';
import { HomeGuard } from './auth/home.guard';
import { UserGuard } from './auth/user.guard';
import { ProtocollataGuard } from './auth/protocollata-guard';
import { DateFormatPipe } from './a4g-common/DateFormatPipe';
import { AmministrazioneComponent } from './funzionalita/amministrazione/amministrazione.component';
import { DettaglioDomandeScadenzaComponent } from './home-caa/scadenze/dettaglio-domande-scadenza/dettaglio-domande-scadenza/dettaglio-domande-scadenza.component';
import { ScadenzeComponent } from './home-caa/scadenze/scadenze.component';
import { ElencoDomandeService } from './istruttoria/istruttoria-pac1420/domandaUnica/services/elenco-domande.service';
import { IstruttoriaCorrente } from './istruttoria/istruttoria-pac1420/domandaUnica/istruttoriaCorrente';
import { IbanErratiDuComponent } from './funzionalita/iban-errati-du/iban-errati-du.component';
import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { LOCALE_ID } from '@angular/core';
import { registerLocaleData, DatePipe } from '@angular/common';
import localeIt from '@angular/common/locales/it';
import { GestioneAziendeComponent } from './fascicolo/gestione-aziende/gestione-aziende.component';
import { UtenteGuard } from './auth/utente.guard';
import { HttpErrorsInterceptor } from './a4g-common/handlers/http-errors-interceptor';
import { RicercaFascicoliNewComponent } from './fascicolo/ricerca-fascicoli-new/ricerca-fascicoli-new.component';
import { FiltroRicercaFascicoliNewComponent } from './fascicolo/ricerca-fascicoli-new/filtro-ricerca-fascicoli-new/filtro-ricerca-fascicoli-new.component';
import { FascicoloModule } from './fascicolo/fascicolo.module';
import { AppBreadcrumbService } from './a4g-common/app-breadcrumb/app.breadcrumb.service';
// import { GisModule } from './gis/gis.module';
// import { HeaderComponent } from './gis/components/header/header.component';
// import { FooterComponent } from './gis/components/footer/footer.component';
import { ConsultazioneUMAComponent } from './funzionalita/consultazione-UMA/consultazione-UMA.component';
import { ListaDomandeComponent } from './funzionalita/consultazione-UMA/app-lista-domande/app-lista-domande.component';
import { RicercaAziendeComponent } from './uma/features/gestione-distributore/ricerca-aziende/ricerca-aziende.component';
import { GestioneConsegneComponent } from './uma/features/gestione-distributore/gestione-consegne/gestione-consegne.component';
import { RisultatoRicercaDisponibilitaComponent } from './uma/features/gestione-distributore/risultato-ricerca-disponibilita/risultato-ricerca-disponibilita.component';
import { PopupNuovoPrelievoComponent } from './uma/features/gestione-distributore/ricerca-aziende/popup-nuovo-prelievo/popup-nuovo-prelievo.component';
import { ListaRettificheDomandeComponent } from './uma/features/gestione-rettifica-domanda-uma/app-lista-rettifiche-domande/app-lista-rettifiche-domande.component';
import { GestioneRettificaDomandaUMAComponent } from './uma/features/gestione-rettifica-domanda-uma/gestione-rettifica-domanda-uma.component';
import { RicercaDomandeComponent } from './funzionalita/ricerca/ricerca-domande/ricerca-domande.component';
import { RicercaAziendeComponentIstruttoreUMA } from './funzionalita/ricerca-aziende-istruttore-uma/ricerca-aziende-istruttore-uma.component';
import { GestioneConsegneComponentIstruttoreUMA } from './funzionalita/gestione-consegne-istruttore-uma/gestione-consegne-istruttore-uma.component';
import { PopupNuovoPrelievoComponentIstruttoreUMA } from './funzionalita/ricerca-aziende-istruttore-uma/popup-nuovo-prelievo-istruttore-uma/popup-nuovo-prelievo-istruttore-uma.component';
import { RisultatoRicercaDisponibilitaIstruttoreUMAComponent } from './funzionalita/ricerca-aziende-istruttore-uma/risultato-ricerca-disponibilita-istruttore-uma/risultato-ricerca-disponibilita-istruttore-uma.component';
import { PopupIstruttoreUmaConfigurazioneComponent } from './funzionalita/presentazione-istanze-istruttoria/popup-istruttore-uma-configurazione/popup-istruttore-uma-configurazione.component';
import { ListaDomandeCaaComponent } from './funzionalita/consultazione-UMA/app-lista-domande-caa/app-lista-domande-caa.component';
import { PopupGeneraFileXmlCup } from './funzionalita/presentazione-istanze-istruttoria/popup-genera-file-xml-cup/popup-genera-file-xml-cup.component';

// AoT requires an exported function for factories
export function HttpLoaderFactory(
  http: HttpClient,
  configuration: Configuration) {
  return new TranslateHttpLoader(http, configuration.FrontendUrl + '/assets/i18n/');
}

@NgModule({
  imports: [
    A4gCommonModule,
    BrowserAnimationsModule,
    FormsModule,
    // GisModule,
    A4gRoutingModule,
    HttpClientModule,
    A4gCommonModule.forRoot(),
    PanelMenuModule,
    InputTextModule,
    LoaderModule,
    ConfirmDialogModule,
    CreazioneFascicoloModule,
    FascicoloModule,
    TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
        useFactory: HttpLoaderFactory,
        deps: [HttpClient, Configuration]
      }
    }),
  ],
  declarations: [
    AppComponent,
    AppTopBarComponent,
    AppFooterComponent,
    ContactComponent,
    HomeComponent,
    LoginComponent,
    RicercaFascicoliComponent,
    RicercaFascicoliNewComponent,
    FiltroRicercaFascicoliNewComponent,
    SintesiFascicoloListComponent,
    RisultatoRicercaDisponibilitaComponent,
    RisultatoRicercaDisponibilitaIstruttoreUMAComponent,
    HomeCAAComponent,
    HomePatComponent,
    PresentazioneIstanzeIstruttoriaComponent,
    AmministrazioneComponent,
    MenuCaaComponent,
    MenuPatComponent,
    DateFormatPipe,
    DettaglioDomandeScadenzaComponent,
    ScadenzeComponent,
    IbanErratiDuComponent,
    ConsultazioneUMAComponent,
    GestioneRettificaDomandaUMAComponent,
    GestioneAziendeComponent,
    // HeaderComponent,
    // FooterComponent,
    ListaDomandeComponent,
    ListaDomandeCaaComponent,
    ListaRettificheDomandeComponent,
    RicercaAziendeComponent,
    RicercaAziendeComponentIstruttoreUMA,
    GestioneConsegneComponent,
    GestioneConsegneComponentIstruttoreUMA,
    PopupNuovoPrelievoComponent,
    PopupNuovoPrelievoComponentIstruttoreUMA,
    RicercaDomandeComponent,
    PopupIstruttoreUmaConfigurazioneComponent,
    PopupGeneraFileXmlCup
  ],
  providers: [
    { provide: LOCALE_ID, useValue: 'it' },
    ElencoDomandeService,
    IstruttoriaCorrente,
    Configuration,
    CrmGuard,
    AuthService,
    HomeService,
    ConfirmationService,
    AuthGuard,
    RoleGuard,
    HomeGuard,
    UserGuard,
    ProtocollataGuard,
    UtenteGuard,
    Labels,
    LoaderService,
    DatePipe,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: HttpErrorsInterceptor,
      multi: true
    },
    {
      provide: ErrorHandler,
      useClass: GlobalErrorHandler
    },
    { // tenere LoaderInterceptor come ultimo degli HTTP_INTERCEPTORS in lista
      provide: HTTP_INTERCEPTORS,
      useClass: LoaderInterceptorService,
      multi: true
    },
    AppBreadcrumbService
  ],
  entryComponents: [
    PopupIstruttoreUmaConfigurazioneComponent,
    PopupGeneraFileXmlCup
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
  constructor() {
    registerLocaleData(localeIt, 'it');
  }
}
