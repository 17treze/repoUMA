import { registerLocaleData } from '@angular/common';
import { HttpClient, HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import localeIt from '@angular/common/locales/it';
import { LOCALE_ID, ErrorHandler, NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ServiceWorkerModule } from '@angular/service-worker';
import { TranslateLoader, TranslateModule } from "@ngx-translate/core";
import { TranslateHttpLoader } from "@ngx-translate/http-loader";
import { environment } from '../environments/environment';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BackComponent } from './layout/back/back.component';
import { FooterComponent } from './layout/footer/footer.component';
import { HeaderComponent } from './layout/header/header.component';
import { ErrorPageComponent } from './modules/common/errorPage/error-page.component';
import { GlobalErrorHandler } from './modules/common/errorPage/GlobalErrorHandler';
import { ContainerHomeComponent } from './modules/home/container-home/container-home.component';
import { ListaCuaComponent } from './modules/home/lista-cuaa/lista-cuaa.component';
import { LoaderInterceptorService } from './shared/loader/loader-interceptors.service';
import { LoaderModule } from './shared/loader/loader.component';
import { LoaderService } from './shared/loader/loader.service';
import { AziendaAgricolaService } from './shared/services/azienda-agricola.service';
import { MandatoService } from './shared/services/mandato.service';
import { SharedModule } from './shared/shared.module';
import { AccordionModule } from 'primeng-lts/accordion'
import { AuthService } from './shared/services/auth.service';

export function createTranslateLoader(http: HttpClient) {
  return new TranslateHttpLoader(http, './assets/i18n/', '.json');
}

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    FooterComponent,
    ErrorPageComponent,
    BackComponent,
    ContainerHomeComponent,
    ListaCuaComponent],
  imports: [
    BrowserModule,
    LoaderModule,
    AppRoutingModule,
    SharedModule,
    HttpClientModule,
    AccordionModule,
    BrowserAnimationsModule,
    TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
        useFactory: (createTranslateLoader),
        deps: [HttpClient]
      }
    }),
    ServiceWorkerModule.register('ngsw-worker.js', { enabled: environment.production })
  ],
  providers: [
    {
      provide: ErrorHandler,
      useClass: GlobalErrorHandler
    },
    { provide: LOCALE_ID, useValue: 'it' },
    {
      provide: HTTP_INTERCEPTORS,
      useClass: LoaderInterceptorService,
      multi: true
    },
    LoaderService,
    AziendaAgricolaService,
    MandatoService,
    AuthService
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
  constructor() {
    registerLocaleData(localeIt, 'it');
 }
}
