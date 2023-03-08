import { NgModule } from "@angular/core";
import { CommonModule, registerLocaleData } from "@angular/common";

import { IstruttoriaAntimafiaRoutingModule } from "./istruttoria-antimafia-routing.module";
import { CertificazioniAntimafiaComponent } from "./certificazioni-antimafia/certificazioni-antimafia.component";
import { CertAmfProtocollateComponent } from "./certificazioni-antimafia/tabs/cert-amf-protocollate/cert-amf-protocollate.component";
import { TabMenuModule } from "primeng/tabmenu";
import { ButtonModule } from "primeng/button";
import { TabViewModule } from "primeng/tabview";
import { CalendarModule } from "primeng/calendar";
import { ToastModule } from "primeng/toast";
import { A4gCommonModule } from "src/app/a4g-common/a4g-common.module";
import { CertAmfControllateComponent } from './certificazioni-antimafia/tabs/cert-amf-controllate/cert-amf-controllate.component';
import { DettaglioDomandeCollegateComponent } from "./certificazioni-antimafia/tabs/cert-amf-controllate/dettaglio-domande-collegate/dettaglio-domande-collegate.component";
import localeIt from '@angular/common/locales/it';
import { PopupRifiutaDomandaComponent } from './popup/popup-rifiuta-domanda/popup-rifiuta-domanda.component';
import { DialogModule } from 'primeng/dialog';
import { CertAmfControlloManualeComponent } from './certificazioni-antimafia/tabs/cert-amf-controllo-manuale/cert-amf-controllo-manuale.component';
import { CertAmfRifiutataComponent } from "./certificazioni-antimafia/tabs/cert-amf-rifiutate/cert-amf-rifiutate.component";
import { CertAmfEsitoPositivoComponent } from "./certificazioni-antimafia/tabs/cert-amf-esito-positivo/cert-amf-esito-positivo.component";
import { CertAmfVerificaPeriodicaComponent } from './certificazioni-antimafia/tabs/cert-amf-verifica-periodica/cert-amf-verifica-periodica.component';
import { CertAmfBozzaComponent } from './certificazioni-antimafia/tabs/cert-amf-bozza/cert-amf-bozza.component';

registerLocaleData(localeIt, 'it');
@NgModule({
  imports: [
    CommonModule,
    IstruttoriaAntimafiaRoutingModule,
    TabMenuModule,
    
    ButtonModule,
    TabViewModule,
    TabMenuModule,
    ToastModule,
    A4gCommonModule,
    CalendarModule,
    DialogModule
  ],
  declarations: [CertificazioniAntimafiaComponent, CertAmfProtocollateComponent, CertAmfControllateComponent, DettaglioDomandeCollegateComponent, PopupRifiutaDomandaComponent, CertAmfControlloManualeComponent, CertAmfRifiutataComponent, CertAmfEsitoPositivoComponent, CertAmfVerificaPeriodicaComponent, CertAmfBozzaComponent]
})
export class IstruttoriaAntimafiaModule { }
