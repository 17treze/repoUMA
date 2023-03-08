import { PopupNuovoTitoloConduzioneComponent } from './territorio/titoli-conduzione/popup-nuovo-titolo-conduzione/popup-nuovo-titolo-conduzione.component';
import { TitoliConduzioneComponent } from './territorio/titoli-conduzione/titoli-conduzione.component';
import { PopupEredeComponent } from './../popup-erede/popup-erede.component';
import { DatiSospensioneComponent } from './../dati-sospensione/dati-sospensione.component';
import { PopupNuovoFabbricatoComponent } from './dotazione-tecnica/fabbricati/popup-nuovo-fabbricato/popup-nuovo-fabbricato.component';
import { NgModule } from "@angular/core";
import { RouterModule, Routes } from "@angular/router";
import { DialogModule } from "primeng/dialog";
import { A4gCommonModule } from "src/app/a4g-common/a4g-common.module";
import { Labels } from "src/app/app.labels";
import { FascicoliValidatiComponent } from "../fascicoli-validati/fascicoli-validati.component";
import { FiltroFascicoliValidatiComponent } from "../fascicoli-validati/filtro-fascicoli-validati/filtro-fascicoli-validati.component";
import { DatiAziendaComponent } from "./dati-anagrafici/dati-azienda/dati-azienda.component";
import { PersoneConCaricaComponent } from "./dati-anagrafici/dati-azienda/persone-con-carica/persone-con-carica.component";
import { UnitaTecnicoEconomicheComponent } from "./dati-anagrafici/dati-azienda/unita-tecnico-economiche/unita-tecnico-economiche.component";
import { ModalitaPagamentoComponent } from "./dati-anagrafici/modalita-pagamento/modalita-pagamento.component";
import { ErediComponent } from "./dati-anagrafici/eredi/eredi.component";
import { FascicoloDettaglioComponent } from "./fascicolo-dettaglio.component";
import { AllevamentiComponent } from "./zootecnia/allevamenti/allevamenti.component";
import { MacchineComponent } from './dotazione-tecnica/macchine/macchine.component';
import { DettaglioMacchinaComponent } from './dotazione-tecnica/macchine/dettaglio-macchina/dettaglio-macchina.component';
import { PopupNuovaMacchinaComponent } from './dotazione-tecnica/macchine/popup-nuova-macchina/popup-nuova-macchina.component';
import { FabbricatiComponent } from "./dotazione-tecnica/fabbricati/fabbricati.component";
import { DettaglioFabbricatoComponent } from "./dotazione-tecnica/fabbricati/dettaglio-fabbricato/dettaglio-fabbricato.component";

const routes: Routes = [
    {
        path: '',
        component: FascicoloDettaglioComponent,
        data: { mybreadcrumb: Labels.FASCICOLO_DETTAGLIO },
      },
      {
        path: 'dati-azienda',
        component: DatiAziendaComponent,
        data: { mybreadcrumb: Labels.FASCICOLO_DETTAGLIO_DATI_AZIENDA }
      },
      {
        path: 'modalita-pagamento',
        component: ModalitaPagamentoComponent,
        data: { mybreadcrumb: Labels.FASCICOLO_DETTAGLIO_MODALITA_PAGAMENTO }
      },
      {
        path: 'eredi',
        component: ErediComponent,
        data: { mybreadcrumb: Labels.FASCICOLO_DETTAGLIO_EREDE }
      },
      {
        path: 'allevamenti',
        component: AllevamentiComponent,
        data: { mybreadcrumb: Labels.FASCICOLO_DETTAGLIO_ALLEVAMENTI }
      },
      {
        path: 'macchine',
        component: MacchineComponent,
        data: { mybreadcrumb: Labels.FASCICOLO_MACCHINARI },
      },
      {
        path: 'macchine/:idMacchina/dettaglio-macchina',
        component: DettaglioMacchinaComponent,
        data: { mybreadcrumb: Labels.FASCICOLO_DETTAGLIO_MACCHINARI }
      },
      {
        path: 'fascicoli-validati',
        component: FascicoliValidatiComponent,
        data: {
          mybreadcrumb: Labels.FASCICOLI_VALIDATI
        }
      },
      {
        path: 'fabbricati',
        component: FabbricatiComponent,
        data: { mybreadcrumb: Labels.FASCICOLO_FABBRICATI },
      },
      {
        path: 'fabbricati/:idFabbricato/dettaglio-fabbricato',
        component: DettaglioFabbricatoComponent,
        data: { mybreadcrumb: Labels.FASCICOLO_DETTAGLIO_FABBRICATI }
      },
      {
        path: 'dati-sospensione',
        component: DatiSospensioneComponent,
        data: {
          mybreadcrumb: Labels.DATI_SOSPENSIONE
        }
      },
      {
        path: 'titoli-conduzione',
        component: TitoliConduzioneComponent,
        data: { mybreadcrumb: Labels.TITOLI_CONDUZIONE },
      },
  ];
  
  @NgModule({
    imports: [
      A4gCommonModule,
      DialogModule,
      RouterModule.forChild(routes),
    ], declarations: [
        FascicoloDettaglioComponent,
        DatiAziendaComponent,
        ModalitaPagamentoComponent,
        ErediComponent,
        AllevamentiComponent,
        MacchineComponent,
        FabbricatiComponent,
        DettaglioMacchinaComponent,
        DettaglioFabbricatoComponent,
        PersoneConCaricaComponent,
        UnitaTecnicoEconomicheComponent,
        FascicoliValidatiComponent,
        FiltroFascicoliValidatiComponent,
        PopupNuovaMacchinaComponent,
        PopupNuovoFabbricatoComponent,
        DatiSospensioneComponent,
        PopupEredeComponent,
        TitoliConduzioneComponent,
        PopupNuovoTitoloConduzioneComponent
    ], providers: [
    ]
  })
  export class FascicoloDettaglioModule { }
  