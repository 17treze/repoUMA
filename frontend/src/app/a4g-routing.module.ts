import { FascicoloAziendaleComponent } from './fascicolo/fascicolo-aziendale/fascicolo-aziendale.component';
import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { RouterModule, Routes, ExtraOptions } from "@angular/router";
import { ContactComponent } from "./contact/contact.component";
import { HomeComponent } from "./home/home.component";
import { LoginComponent } from "./login/login.component";
import { CrmGuard } from "./auth/crm.guard";
import { RicercaFascicoliComponent } from "./funzionalita/ricerca/ricerca-fascicoli/ricerca-fascicoli.component";
import { HomeCAAComponent } from "./home-caa/home-caa.component";
import { HomePatComponent } from "./home-pat/home-pat.component";
import { MenuPatComponent } from "./home-pat/menu-pat/menu-pat.component";
import { PresentazioneIstanzeIstruttoriaComponent } from "./funzionalita/presentazione-istanze-istruttoria/presentazione-istanze-istruttoria.component";
import { HomeGuard } from "./auth/home.guard";
import { Labels } from "./app.labels";
import { AmministrazioneComponent } from "./funzionalita/amministrazione/amministrazione.component";
import { MenuCaaComponent } from "./home-caa/menu-caa/menu-caa.component";
// import { DettaglioDomandeScadenzaComponent } from "./home-caa/scadenze/dettaglio-domande-scadenza/dettaglio-domande-scadenza/dettaglio-domande-scadenza.component";
// import { ScadenzeComponent } from "./home-caa/scadenze/scadenze.component";
// import { ScadenzeAppagComponent } from "./funzionalita/scadenze-appag/scadenze-appag.component";
import { ConfigurazioneUmaComponent } from "./uma/features/configurazione/configurazione.component";
import { GruppiColtureComponent } from "./uma/features/configurazione/gruppi-colture/gruppi-colture.component";
import { IbanErratiDuComponent } from "./funzionalita/iban-errati-du/iban-errati-du.component";
import { GestioneAziendeComponent } from './fascicolo/gestione-aziende/gestione-aziende.component';
import { ErrorPageComponent } from './a4g-common/errorPage/error-page.component';
import { FascicoloDettaglioContainerComponent } from "./fascicolo/fascicolo-dettaglio-container/fascicolo-dettaglio-container.component";
import { RicercaFascicoliNewComponent } from "./fascicolo/ricerca-fascicoli-new/ricerca-fascicoli-new.component";
import { ConsultazioneUMAComponent } from "./funzionalita/consultazione-UMA/consultazione-UMA.component";
import { RicercaAziendeComponent } from "./uma/features/gestione-distributore/ricerca-aziende/ricerca-aziende.component";
import { GestioneConsegneComponent } from "./uma/features/gestione-distributore/gestione-consegne/gestione-consegne.component";
import { RicercaDomandeComponent } from './funzionalita/ricerca/ricerca-domande/ricerca-domande.component';
import { RicercaAziendeComponentIstruttoreUMA } from "./funzionalita/ricerca-aziende-istruttore-uma/ricerca-aziende-istruttore-uma.component";
import { GestioneConsegneComponentIstruttoreUMA } from "./funzionalita/gestione-consegne-istruttore-uma/gestione-consegne-istruttore-uma.component";
// import { environment } from 'src/environments/environment';
import { CoefficientiComponent } from './uma/features/configurazione/coefficienti/coefficienti.component';
import { GruppiLavorazioneComponent } from './uma/features/configurazione/gruppi-lavorazione/gruppi-lavorazione.component';
import { LavorazioniComponent } from './uma/features/configurazione/lavorazioni/lavorazioni.component';
import { GruppiFabbricatoComponent } from './uma/features/configurazione/gruppi-fabbricato/gruppi-fabbricato.component';

const routes: Routes = [
  {
    path: '',
    redirectTo: 'home',
    pathMatch: 'full'
  },
  {
    path: 'informazioni',
    component: ContactComponent
  },
  {
    path: 'profilo',
    component: ContactComponent
  },
  {
    path: 'logout',
    component: ContactComponent
  },
  {
    path: 'login',
    component: LoginComponent,
    canDeactivate: [CrmGuard]
  },
  {
    path: 'funzioniCaa',
    component: HomeCAAComponent,
    canActivate: [CrmGuard],
    data: {
      mybreadcrumb: Labels.DIPENDENTE_CAA
    },
    children: [
      {
        path: '',
        data: { mybreadcrumb: '' },
        component: MenuCaaComponent,
        children: [
          { path: '', redirectTo: 'fascicoloAziendale', pathMatch: 'full' },
          { path: 'fascicoloAziendale', component: FascicoloAziendaleComponent },
          { path: 'gestioneAzienda', component: GestioneAziendeComponent },
          // { path: 'scadenze', component: ScadenzeComponent },
          { path: 'configurazione', component: ConfigurazioneUmaComponent },
        ]
      },
      // {
      //   path: '',
      //   loadChildren: () => import('./gis/gis.module').then(m => m.GisModule)
      // },
      {
        path: "consultazioneUMA",
        component: ConsultazioneUMAComponent,
        data: { mybreadcrumb: Labels.CONSULTAZIONE_UMA }
      },
      { path: 'ricercaFascicolo', component: RicercaFascicoliComponent, data: { mybreadcrumb: Labels.ricercaFascicolo } },
      {
        path: 'ricerca-fascicolo-new',
        data: {
          mybreadcrumb: Labels.ricercaFascicolo
        },
        children: [
          {
            path: '',
            component: RicercaFascicoliNewComponent,
            data: { mybreadcrumb: Labels.FASCICOLO_DETTAGLIO },
          },
          {
            path: 'cuaa/:cuaa/dettaglio',
            component: FascicoloDettaglioContainerComponent,
            data: {
              breadcrumbTranslateKey: "FAS_ANA.FASCICOLO_AZIENDALE",
              mybreadcrumb: Labels.FASCICOLO_AZIENDALE
            },
            loadChildren: () => import('./fascicolo/fascicolo-dettaglio/fascicolo-dettaglio.module').then(m => m.FascicoloDettaglioModule)
          }
        ]
      },
      {
        path: 'fascicolo',
        loadChildren: () => import('./fascicolo/creazione-fascicolo/creazione-fascicolo.module').then(m => m.CreazioneFascicoloModule),
        data: { mybreadcrumb: Labels.fascicolo }
      },
      {
        path: 'revocaOrdinaria',
        loadChildren: () => import('./fascicolo/revoca-ordinaria/revoca-ordinaria.module').then(m => m.RevocaOrdinariaModule), // percorso del revoca-ordinaria.module
        canLoad: [CrmGuard],
        data: { mybreadcrumb: Labels.revocaOrdinaria }
      },
      {
        path: 'fascicoliDaMigrare',
        loadChildren: () => import('./fascicolo/fascicoli-da-migrare/fascicoli-da-migrare.module').then(m => m.FascicoliDaMigrareModule),
        canLoad: [CrmGuard],
        data: { mybreadcrumb: Labels.fascicoliDaMigrare }
      },
      /*
      {
        path: 'domandeScadenze',
        component: DettaglioDomandeScadenzaComponent,
        data: { mybreadcrumb: 'Scadenze Antimafia' },
        children: [
          {
            path: '',
            component: DettaglioDomandeScadenzaComponent
          },
        ]
      },
      {
        path: "ibanErratiDu",
        component: IbanErratiDuComponent,
        data: { mybreadcrumb: 'IBAN Errati DU' },
        children: [
          {
            path: "",
            component: IbanErratiDuComponent
          },
        ]
      },
      */
      {
        path: "fascicolo",
        loadChildren: () => import('./fascicolo/fascicolo.module').then(m => m.FascicoloModule), // percorso del  fascicolo.module
        canLoad: [CrmGuard],
        data: { mybreadcrumb: '' }
      },
      {
        path: 'richiesteRevocaMandato',
        loadChildren: () => import('./fascicolo/richieste-revoca-mandato/richieste-revoca-mandato.module').then(m => m.RichiesteRevocaMandatoModule),
        canLoad: [CrmGuard],
        data: {
          mybreadcrumb: Labels.RICHIESTE_REVOCA_MANDATO
        },
      },
      {
        path: 'ricercaFascicoliDaRiaprire',
        loadChildren: () => import('./fascicolo/riapertura-fascicolo/riapertura-fascicolo.module').then(m => m.RiaperturaFascicoloModule),
        canLoad: [CrmGuard],
        data: { mybreadcrumb: Labels.riaperturaFascicolo }
      },
      {
        path: 'fascicoloPerAcquisizioneMandato',
        loadChildren: () => import('./fascicolo/revoca-immediata-acquisizione-mandato/revoca-immediata-acquisizione-mandato.module').then(m => m.RevocaImmediataAcquisizioneMandatoModule),
        canLoad: [CrmGuard],
        data: { mybreadcrumb: Labels.ricercaFascicoloPerAcquisizioneMandato }
      },
      {
        path: "coefficienti",
        component: CoefficientiComponent,
        data: { mybreadcrumb: Labels.CONFIG_COEFFICIENTI }
      },
      {
        path: "lavorazioni",
        component: LavorazioniComponent,
        data: { mybreadcrumb: Labels.CONFIG_LAVORAZIONI }
      },
      {
        path: "gruppiLavorazione",
        component: GruppiLavorazioneComponent,
        data: { mybreadcrumb: Labels.CONFIG_GRUPPI_LAVORAZIONE }
      },
      {
        path: "gruppiColture",
        component: GruppiColtureComponent,
        data: { mybreadcrumb: Labels.GRUPPI_COLTURE }
      },
      {
        path: "gruppiFabbricato",
        component: GruppiFabbricatoComponent,
        data: { mybreadcrumb: Labels.GRUPPI_FABBRICATO }
      }
    ]
  },
  {
    path: 'funzioniPatVisualizzatore',
    component: HomePatComponent,
    canActivate: [CrmGuard],
    data: {
      mybreadcrumb: Labels.DIPENDENTE_PAT_VISUALIZZATORE
    },
    children: [
      {
        path: '',
        component: MenuPatComponent,
        children: [
          { path: '', redirectTo: 'gestioneAzienda', pathMatch: 'full' },
          { path: 'gestioneAzienda', component: GestioneAziendeComponent },
          { path: 'gestioneIstruttoria', component: PresentazioneIstanzeIstruttoriaComponent },
        ],
        data: { mybreadcrumb: '' }
      },
      {
        path: 'istruttoriaAntimafia',
        loadChildren:
          () => import('./istruttoria/istruttoria-antimafia/istruttoria-antimafia.module').then(m => m.IstruttoriaAntimafiaModule),
        canLoad: [CrmGuard],
        data: {
          mybreadcrumb: Labels.ISTRUTTORIA_ANTIMAFIA
        }
      },
      {
        path: 'istruttoriaPac1420',
        loadChildren: () => import('./istruttoria/istruttoria-pac1420/istruttoria-pac1420.module').then(m => m.IstruttoriaPac1420Module),
        canLoad: [CrmGuard],
        data: {
          mybreadcrumb: Labels.PAC_14_20
        },
      },
      // {
      // path: '',
      // loadChildren: () => import('./gis/gis.module').then(m => m.GisModule)
      // },
      {
        path: 'consultazioneUMA',
        component: ConsultazioneUMAComponent,
        data: { mybreadcrumb: Labels.CONSULTAZIONE_UMA }
      },
      {
        path: 'ricercaAziende',
        component: RicercaAziendeComponentIstruttoreUMA,
        data: { mybreadcrumb: Labels.PRELIEVI_CARBURANTE }
      },
      {
        path: 'gestioneConsegne',
        component: GestioneConsegneComponentIstruttoreUMA,
        data: { mybreadcrumb: Labels.VALIDARE_PRELIEVI }
      }
    ]
  },
  {
    path: 'funzioniPatResponsabileFascicolo',
    component: HomePatComponent,
    canActivate: [CrmGuard],
    data: {
      mybreadcrumb: Labels.DIPENDENTE_PAT_RESPONSABILE_FASCICOLO
    },
    children: [
      {
        path: '',
        component: MenuPatComponent,
        children: [
          { path: '', redirectTo: 'fascicoloAziendale', pathMatch: 'full' },
          { path: 'fascicoloAziendale', component: FascicoloAziendaleComponent }
        ],
        data: { mybreadcrumb: '' }
      },
      {
        path: 'ricerca-fascicolo-new',
        data: {
          mybreadcrumb: Labels.ricercaFascicolo
        },
        children: [
          {
            path: '',
            component: RicercaFascicoliNewComponent,
            data: { mybreadcrumb: Labels.FASCICOLO_DETTAGLIO },
          },
          {
            path: 'cuaa/:cuaa/dettaglio',
            component: FascicoloDettaglioContainerComponent,
            data: {
              breadcrumbTranslateKey: "FAS_ANA.FASCICOLO_AZIENDALE",
              mybreadcrumb: Labels.FASCICOLO_AZIENDALE
            },
            loadChildren: () => import('./fascicolo/fascicolo-dettaglio/fascicolo-dettaglio.module').then(m => m.FascicoloDettaglioModule)
          }
        ]
      },
      {
        path: 'richiesteRevocaMandato',
        loadChildren: () => import('./fascicolo/richieste-revoca-mandato/richieste-revoca-mandato.module').then(m => m.RichiesteRevocaMandatoModule),
        canLoad: [CrmGuard],
        data: {
          mybreadcrumb: Labels.RICHIESTE_REVOCA_MANDATO
        },
      }
    ]
  },
  {
    path: 'funzioniPatAgenziaDogane',
    component: ConsultazioneUMAComponent,
    data: {
      mybreadcrumb: Labels.DIPENDENTE_DOGANE
    },
  },
  {
    path: 'funzioniPatIstruttoreUMA',
    component: HomePatComponent,
    canActivate: [CrmGuard],
    data: {
      mybreadcrumb: Labels.DIPENDENTE_PAT_ISTRUTTORE_UMA
    },
    children: [
      {
        path: '',
        component: MenuPatComponent,
        children: [
          { path: '', redirectTo: 'gestioneIstruttoria', pathMatch: 'full' },
          {
            path: 'gestioneIstruttoria',
            component: PresentazioneIstanzeIstruttoriaComponent
          },
          { 
            path: 'fascicoloAziendale', 
            component: FascicoloAziendaleComponent,
            data: { mybreadcrumb: Labels.ricercaFascicoloNew }
          },
        ],
        data: { mybreadcrumb: '' }
      },
      {
        path: 'ricerca-fascicolo-new',
        data: {
          mybreadcrumb: Labels.ricercaFascicolo
        },
        children: [
          {
            path: '',
            component: RicercaFascicoliNewComponent,
            data: { mybreadcrumb: Labels.FASCICOLO_DETTAGLIO },
          },
          {
            path: 'cuaa/:cuaa/dettaglio',
            component: FascicoloDettaglioContainerComponent,
            data: {
              breadcrumbTranslateKey: "FAS_ANA.FASCICOLO_AZIENDALE",
              mybreadcrumb: Labels.FASCICOLO_AZIENDALE
            },
            loadChildren: () => import('./fascicolo/fascicolo-dettaglio/fascicolo-dettaglio.module').then(m => m.FascicoloDettaglioModule)
          }
        ]
      },
      {
        path: "consultazioneUMA",
        component: ConsultazioneUMAComponent,
        data: { mybreadcrumb: Labels.CONSULTAZIONE_UMA }
      },
      {
        path: 'ricercaAziende',
        component: RicercaAziendeComponentIstruttoreUMA,
        data: { mybreadcrumb: Labels.PRELIEVI_CARBURANTE }
      },
      {
        path: 'gestioneConsegne',
        component: GestioneConsegneComponentIstruttoreUMA,
        data: { mybreadcrumb: Labels.VALIDARE_PRELIEVI }
      }
    ]
  },
  {
    path: 'funzioniDistributore',
    component: HomePatComponent,
    canActivate: [CrmGuard],
    data: {
      mybreadcrumb: Labels.DISTRIBUTORE
    },
    children: [
      {
        path: '',
        component: MenuPatComponent,
        children: [
          { path: '', redirectTo: 'ricercaAziende', pathMatch: 'full' },
          {
            path: 'ricercaAziende',
            data: { mybreadcrumb: '' },
            component: RicercaAziendeComponent
          },
          {
            path: 'gestioneConsegne',
            data: { mybreadcrumb: '' },
            component: GestioneConsegneComponent
          },
        ],
        data: { mybreadcrumb: '' }
      }
    ]
  },
  {
    path: 'funzioniOperatoreVisualizzatore',
    component: HomePatComponent,
    canActivate: [CrmGuard],
    data: {
      mybreadcrumb: Labels.DIPENDENTE_VISUALIZZATORE
    },
    children: [
      {
        path: '',
        component: MenuPatComponent,
        children: [
          { path: '', redirectTo: 'gestioneAzienda', pathMatch: 'full' },
          { path: 'gestioneIstruttoria', component: PresentazioneIstanzeIstruttoriaComponent },
          { path: 'gestioneAzienda', component: GestioneAziendeComponent }
        ],
        data: { mybreadcrumb: '' }
      },
      /*
      {
        path: 'istruttoriaAntimafia',
        loadChildren:
          () => import('./istruttoria/istruttoria-antimafia/istruttoria-antimafia.module').then(m => m.IstruttoriaAntimafiaModule),
        canLoad: [CrmGuard],
        data: {
          mybreadcrumb: Labels.ISTRUTTORIA_ANTIMAFIA
        }
      },
      {
        path: 'istruttoriaPac1420',
        loadChildren: () => import('./istruttoria/istruttoria-pac1420/istruttoria-pac1420.module').then(m => m.IstruttoriaPac1420Module),
        canLoad: [CrmGuard],
        data: {
          mybreadcrumb: Labels.PAC_14_20
        },
      },
      */
      // {
      // path: '',
      // loadChildren: () => import('./gis/gis.module').then(m => m.GisModule)
      // },
      {
        path: 'consultazioneUMA',
        component: ConsultazioneUMAComponent,
        data: { mybreadcrumb: Labels.CONSULTAZIONE_UMA }
      },
      {
        path: 'ricercaAziende',
        component: RicercaAziendeComponentIstruttoreUMA,
        data: { mybreadcrumb: Labels.PRELIEVI_CARBURANTE }
      },
      {
        path: 'gestioneConsegne',
        component: GestioneConsegneComponentIstruttoreUMA,
        data: { mybreadcrumb: Labels.VALIDARE_PRELIEVI }
      }
    ]
  },
  {
    path: 'funzioniPat',
    component: HomePatComponent,
    canActivate: [CrmGuard],
    data: {
      mybreadcrumb: Labels.DIPENDENTE_PAT
    },
    children: [
      {
        path: '',
        component: MenuPatComponent,
        children: [
          { path: '', redirectTo: 'fascicoloAziendale', pathMatch: 'full' },
          { path: 'fascicoloAziendale', component: FascicoloAziendaleComponent },
          { path: 'gestioneAzienda', component: GestioneAziendeComponent },
          { path: 'gestioneIstruttoria', component: PresentazioneIstanzeIstruttoriaComponent },
          { path: 'amministrazione', component: AmministrazioneComponent },
          // { path: 'scadenzeAppag', component: ScadenzeAppagComponent }
        ],
        data: { mybreadcrumb: '' }
      },
      {
        path: 'ricercaAziende',
        component: RicercaAziendeComponentIstruttoreUMA,
        data: { mybreadcrumb: Labels.PRELIEVI_CARBURANTE }
      },
      {
        path: 'gestioneConsegne',
        component: GestioneConsegneComponentIstruttoreUMA,
        data: { mybreadcrumb: Labels.VALIDARE_PRELIEVI }
      },
      {
        path: 'ricerca-domande', component: RicercaDomandeComponent, data: {
          mybreadcrumb: Labels.RICERCA_DU
        }
      },
      // {
      // path: '',
      // loadChildren: () => import('./gis/gis.module').then(m => m.GisModule)
      // },
      { path: 'ricercaFascicolo', component: RicercaFascicoliComponent, data: { mybreadcrumb: Labels.ricercaFascicolo } },
      {
        path: 'ricerca-fascicolo-new',
        data: {
          mybreadcrumb: Labels.ricercaFascicolo
        },
        children: [
          {
            path: '',
            component: RicercaFascicoliNewComponent,
            data: { mybreadcrumb: Labels.FASCICOLO_DETTAGLIO },
          },
          {
            path: 'cuaa/:cuaa/dettaglio',
            component: FascicoloDettaglioContainerComponent,
            data: {
              breadcrumbTranslateKey: "FAS_ANA.FASCICOLO_AZIENDALE",
              mybreadcrumb: Labels.FASCICOLO_AZIENDALE
            },
            loadChildren: () => import('./fascicolo/fascicolo-dettaglio/fascicolo-dettaglio.module').then(m => m.FascicoloDettaglioModule)
          }
        ]
      },
      {
        path: 'fascicolo/crea',
        loadChildren: () => import('./fascicolo/creazione-fascicolo/creazione-fascicolo.module').then(m => m.CreazioneFascicoloModule),
        data: { mybreadcrumb: Labels.creaFascicolo }
      },
      {
        path: 'revocaOrdinaria',
        loadChildren: () => import('./fascicolo/revoca-ordinaria/revoca-ordinaria.module').then(m => m.RevocaOrdinariaModule), // percorso del revoca-ordinaria.module
        canLoad: [CrmGuard],
        data: { mybreadcrumb: Labels.revocaOrdinaria }
      },
      {
        path: "ibanErratiDu",
        component: IbanErratiDuComponent,
        data: { mybreadcrumb: 'IBAN Errati DU' }
      },
      {
        path: 'istruttoriaAntimafia',
        loadChildren:
          () => import('./istruttoria/istruttoria-antimafia/istruttoria-antimafia.module').then(m => m.IstruttoriaAntimafiaModule),
        canLoad: [CrmGuard],
        data: {
          mybreadcrumb: Labels.ISTRUTTORIA_ANTIMAFIA
        }
      },
      // {
      //   path: 'istruttoriaPac',
      //   loadChildren: './istruttoria/istruttoria-pac1420/istruttoria-pac.module#IstruttoriaPacModule',
      //   canLoad: [CrmGuard],
      //   data: {
      //     mybreadcrumb: Labels.PAC_14_20
      //   },
      // },
      {
        path: 'istruttoriaPac1420',
        loadChildren: () => import('./istruttoria/istruttoria-pac1420/istruttoria-pac1420.module').then(m => m.IstruttoriaPac1420Module),
        canLoad: [CrmGuard],
        data: {
          mybreadcrumb: Labels.PAC_14_20
        },
      },
      {
        path: 'fascicolo',
        loadChildren: () => import('./fascicolo/fascicolo.module').then(m => m.FascicoloModule), // percorso del  fascicolo.module
        canLoad: [CrmGuard],
        data: { mybreadcrumb: '' }
      },
      {
        path: 'gestioneUtenze',
        loadChildren: () => import('./amministrazione/gestione-utenze/gestione-utenze.module').then(m => m.GestioneUtenzeModule),
        canLoad: [CrmGuard],
        data: {
          mybreadcrumb: Labels.GESTIONE_UTENZE
        },
      },
      {
        path: 'ricercaUtenti',
        loadChildren: () => import('./amministrazione/ricerca-utenti/ricerca-utenti.module').then(m => m.RicercaUtentiModule),
        canLoad: [CrmGuard],
        data: {
          mybreadcrumb: Labels.RICERCA_UTENTI
        },
      }, {
        path: 'richiesteRevocaMandato',
        loadChildren: () => import('./fascicolo/richieste-revoca-mandato/richieste-revoca-mandato.module').then(m => m.RichiesteRevocaMandatoModule),
        canLoad: [CrmGuard],
        data: {
          mybreadcrumb: Labels.RICHIESTE_REVOCA_MANDATO
        },
      },
      {
        path: "consultazioneUMA",
        component: ConsultazioneUMAComponent,
        data: { mybreadcrumb: Labels.CONSULTAZIONE_UMA }
      }
    ]
  },
  {
    path: 'ricercaFascicolo',
    component: RicercaFascicoliComponent,
    pathMatch: 'full',
    canActivate: [CrmGuard]
  },
  // {
  // path: '',
  // loadChildren: () => import('./gis/gis.module').then(m => m.GisModule),
  // canLoad: [CrmGuard]
  // },
  {
    path: 'fascicolo',
    loadChildren: () => import('./fascicolo/fascicolo.module').then(m => m.FascicoloModule), // percorso del  fascicolo.module
    canLoad: [CrmGuard]
  },
  {
    path: 'uma/:idFascicolo',
    loadChildren: () => import('./uma/uma.module').then(m => m.UmaModule),
    data: { mybreadcrumb: '' },
    canLoad: [CrmGuard]
  },
  {
    path: 'funzioniCaa/consultazioneUMA',
    loadChildren: () => import('./uma/uma.module').then(m => m.UmaModule)
  },
  {
    path: 'funzioniPatIstruttoreUMA/consultazioneUMA',
    loadChildren: () => import('./uma/uma.module').then(m => m.UmaModule),
    data: { mybreadcrumb: '' },
    canLoad: [CrmGuard]
  },
  {
    path: 'funzioniPatAgenziaDogane',
    loadChildren: () => import('./uma/uma.module').then(m => m.UmaModule),
    data: { mybreadcrumb: '' },
    canLoad: [CrmGuard]
  },
  {
    path: 'funzioniPat/ricercaAziende',
    loadChildren: () => import('./uma/uma.module').then(m => m.UmaModule),
    data: { mybreadcrumb: '' },
    canLoad: [CrmGuard]
  },
  {
    path: 'funzioniPat/gestioneConsegne',
    loadChildren: () => import('./uma/uma.module').then(m => m.UmaModule),
    data: { mybreadcrumb: '' },
    canLoad: [CrmGuard]
  },
  {
    path: 'funzioniPat/consultazioneUMA',
    loadChildren: () => import('./uma/uma.module').then(m => m.UmaModule),
    data: { mybreadcrumb: '' },
    canLoad: [CrmGuard]
  },
  {
    path: 'funzioniOperatoreVisualizzatore/consultazioneUMA',
    loadChildren: () => import('./uma/uma.module').then(m => m.UmaModule),
    data: { mybreadcrumb: '' },
    canLoad: [CrmGuard]
  },
  {
    path: 'funzioniPatVisualizzatore/consultazioneUMA',
    loadChildren: () => import('./uma/uma.module').then(m => m.UmaModule),
    data: { mybreadcrumb: '' },
    canLoad: [CrmGuard]
  },
  {
    path: 'utenti',
    loadChildren: () => import('./utenti/utenti.module').then(m => m.UtentiModule), // percorso dell' utenti.module
    canLoad: [CrmGuard],
  },
  {
    path: 'home',
    component: HomeComponent,
    canActivate: [HomeGuard],
    canLoad: [CrmGuard],
    pathMatch: 'full',
  },
  {
    path: 'errorPage/:error',
    component: ErrorPageComponent
  },
  {
    path: '**',
    component: ErrorPageComponent
  }
];

export const routingConfiguration: ExtraOptions = {
  paramsInheritanceStrategy: 'always',
  onSameUrlNavigation: 'reload'
};

@NgModule({
  imports: [CommonModule, RouterModule.forRoot(routes, routingConfiguration)],
  declarations: []
})
export class A4gRoutingModule { }
