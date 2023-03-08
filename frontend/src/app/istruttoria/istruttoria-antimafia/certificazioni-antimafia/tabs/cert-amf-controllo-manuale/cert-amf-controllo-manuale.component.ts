import { AntimafiaService } from './../../../../../fascicolo/antimafia/antimafia.service';
import { ActivatedRoute } from '@angular/router';
import { DichiarazioneAntimafiaConEsiti } from './../../../dto/DichiarazioneAntimafiaConEsiti';
import { IstruttoriaAntimafiaService } from 'src/app/istruttoria/istruttoria-antimafia/istruttoria-antimafia.service';
import { Component, OnInit, EventEmitter, Output, ViewChild, OnDestroy } from '@angular/core';
import { Labels } from 'src/app/app.labels';
import { DichiarazioneAntimafia } from 'src/app/fascicolo/antimafia/classi/dichiarazioneAntimafia';
import { ActionMenuComponent } from 'src/app/a4g-common/action-menu.component';
import { StatoDichiarazione } from 'src/app/fascicolo/antimafia/classi/statoDichiarazioneEnum';
import { MessageService } from 'primeng/api';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';
import { PopupRifiutaDomandaComponent } from '../../../popup/popup-rifiuta-domanda/popup-rifiuta-domanda.component';
import { LoaderService } from 'src/app/loader.service';
import { NavigationExtras, Router } from '@angular/router';
import { SincronizzazioneDateBdnaDto } from 'src/app/a4g-common/classi/SincronizzazioneDateBdnaDto';
import { RicercaAntimafiaFilter } from '../../../dto/RicercaAntimafiaFilter';
import { CaricaCodiceFiscale } from 'src/app/fascicolo/antimafia/classi/datiDichiarazione';
import { switchMap } from 'rxjs/operators';
import { of, Subscription } from 'rxjs';

@Component({
  selector: 'app-cert-amf-controllo-manuale',
  templateUrl: './cert-amf-controllo-manuale.component.html',
  styleUrls: ['./cert-amf-controllo-manuale.component.css', '../shared/esiti.css']
})
export class CertAmfControlloManualeComponent implements OnInit, OnDestroy {
  cols: any[];
  labels = Labels;
  textTitle;
  certificazioneAntimafia: Array<DichiarazioneAntimafiaConEsiti>;
  menuAction: any[] = new Array();
  indexSelectedFromMenu: number;
  domandaAntimafiaSelezionataFromMenuAction: DichiarazioneAntimafiaConEsiti;
  currentMenuAction: ActionMenuComponent;
  @Output() aggiornaCertificazioniCounters = new EventEmitter();
  @ViewChild(PopupRifiutaDomandaComponent, { static: true })
  private popupComponent: PopupRifiutaDomandaComponent;
  statoDaAggiornare: StatoDichiarazione;
  elementiPerPagina: number = 5;
  ricercaAntimafiaFilter: RicercaAntimafiaFilter = new RicercaAntimafiaFilter();
  elementiTotali = 0;
  dichiarazioneAntimafiaSubscpription: Subscription;

  constructor(private istruttoriaAntimafiaService: IstruttoriaAntimafiaService, private antimafiaService: AntimafiaService, private messages: MessageService, private loader: LoaderService,
    private router: Router, private route: ActivatedRoute) {
    // ordinamento sulle colonne
    this.cols = [
      {
        field: "datiDichiarazione.dettaglioImpresa.codiceFiscale",
        header: this.labels.cuaaSigla
      },
      {
        field: "datiDichiarazione.dettaglioImpresa.denominazione",
        header: this.labels.descrizioneImpresa
      },
      {
        field: "dtProtocollazione",
        header: this.labels.data
      },
      {
        field: "idProtocollo",
        header: this.labels.nrProtocollo
      },
      {
        field: "esito",
        header: this.labels.esitoTrasmissione
      }
    ];
  }

  ngOnInit() {
    this.loader.setTimeout(480000); //otto minuti
    this.menuAction = this.menuActionProtocollata();
    this.textTitle = Labels.CONTROLLO_CERTIFICAZIONE_ANTIMAFIA_CONTROLLO_MANUALE;
  }

  ngOnDestroy(): void {
    this.loader.resetTimeout();
    if (this.dichiarazioneAntimafiaSubscpription) {
      this.dichiarazioneAntimafiaSubscpription.unsubscribe();
    }
  }

  onDropdownMenuOpen(
    actionMenu: ActionMenuComponent,
    certificazioneProtocollata: DichiarazioneAntimafiaConEsiti,
    indexSelectedFromMenu: number
  ) {
    this.indexSelectedFromMenu = indexSelectedFromMenu;
    this.domandaAntimafiaSelezionataFromMenuAction = certificazioneProtocollata;
    this.currentMenuAction = actionMenu;
    this.currentMenuAction.reset = false;
  }

  menuActionProtocollata(): Array<any> {
    return [
      {
        label: Labels.azioni.toUpperCase(),
        items: [
          {
            label: this.labels.ESITO_CONTROLLO,
            command: () => {
              this.getEsitoControlloAntimafia();
              this.currentMenuAction.reset = true;
            }
          },
          { separator: true },
          {
            label: this.labels.SCARICA,
            command: () => {
              this.downloadDomandaCertificazioneAntimafia();
              this.currentMenuAction.reset = true;
            }
          },
          {
            label: this.labels.RIFIUTA,
            command: () => {
              this.openPopup(StatoDichiarazione.RIFIUTATA);
              this.currentMenuAction.reset = true;
            }
          },
          {
            label: this.labels.APPROVA,
            command: () => {
              this.approvaDomandaControllataManualmente();
              this.currentMenuAction.reset = true;
            }
          },
          {
            label: Labels.ESITO_POSITIVO,
            command: () => {
              this.openPopup(StatoDichiarazione.POSITIVO);
              this.currentMenuAction.reset = true;
            }
          }
        ]
      }
    ];
  }


  // GET ESITO CONTROLLO 
  getEsitoControlloAntimafia() {
    let id = this.domandaAntimafiaSelezionataFromMenuAction.id;
    let cuua = this.domandaAntimafiaSelezionataFromMenuAction.datiDichiarazione.dettaglioImpresa.codiceFiscale;
    this.antimafiaService.getEsitoControlloAntimafia(id).subscribe(esito => {
      let domandeGestite: Array<any> = esito.domandeGestite;
      domandeGestite.forEach(esitoDomanda => {
        let esitoParsed = JSON.parse(esitoDomanda);
        //Elaborazione messaggi per PARIX
        if (esitoParsed.parixMessage) {
          this.messages.add(
            A4gMessages.getToast(
              "generic",
              A4gSeverityMessage.info,
              esitoParsed.parixMessage
            )
          );
        } else {
          this.messages.add(
            A4gMessages.getToast(
              "generic",
              A4gSeverityMessage.info,
              cuua + " valido in Parix"
            )
          );
        }
        //Elaborazione messaggi per SIAP
        if (esitoParsed.siapMessage) {
          this.messages.add(
            A4gMessages.getToast(
              "generic",
              A4gSeverityMessage.info,
              esitoParsed.siapMessage
            )
          );
        } else {
          this.messages.add(
            A4gMessages.getToast(
              "generic",
              A4gSeverityMessage.info,
              cuua + " valido in Siap"
            )
          );
        }
        //Elaborazione messaggi per SOGGETTI
        if (esitoParsed.soggettiMessages) {
          let soggErr = "";
          esitoParsed.soggettiMessages.forEach(soggettoMessage => {
            soggErr += soggettoMessage;
          });
          this.messages.add(
            A4gMessages.getToast("generic", A4gSeverityMessage.info, soggErr)
          );
        } else {
          this.messages.add(
            A4gMessages.getToast(
              "generic",
              A4gSeverityMessage.info,
              "Tutti i soggetti con carica validi in Parix"
            )
          );
        }
      });
    },
      error => {
        this.messages.add(A4gMessages.getToast("generic", A4gSeverityMessage.error, error.error.message));
      });
  }

  // download dichiarazione antimafia
  downloadDomandaCertificazioneAntimafia() {
    const certificazioneProtocollata: DichiarazioneAntimafia = this.domandaAntimafiaSelezionataFromMenuAction;
    const getDichiarazioneAntimafia$ = this.antimafiaService.getDichiarazioneAntimafiaWithPdfFirmato(certificazioneProtocollata.id.toString());

    this.dichiarazioneAntimafiaSubscpription = getDichiarazioneAntimafia$.pipe(
      switchMap((dichiarazione: DichiarazioneAntimafia) => {
        certificazioneProtocollata.pdfFirmato = dichiarazione.pdfFirmato;
        this.antimafiaService.downloadDichiarazioneAntimafia(dichiarazione.pdfFirmato, dichiarazione.tipoPdfFirmato)
        return of(dichiarazione);
      })
    ).subscribe((dichiarazione: DichiarazioneAntimafia) => {
      const cariche: Array<CaricaCodiceFiscale> = this.antimafiaService.getAllegati(dichiarazione);
      this.antimafiaService.downloadAllegati(cariche);
    });
  }

  approvaDomandaControllataManualmente() {
    let dichiarazione: DichiarazioneAntimafia = this.domandaAntimafiaSelezionataFromMenuAction;
    dichiarazione.stato.identificativo = StatoDichiarazione.CONTROLLATA;
    this.antimafiaService
      .aggiornaDichiarazioneAntimafia(dichiarazione)
      .subscribe(
        dichiarazioneAggiornata => {
          if (
            dichiarazioneAggiornata.dichiarazione.stato.identificativo.toString() ==
            StatoDichiarazione.CONTROLLATA
          ) {
            this.messages.add(
              A4gMessages.getToast(
                "generic",
                A4gSeverityMessage.success,
                A4gMessages.OPERAZIONE_OK
              )
            );
            this.certificazioneAntimafia.splice(
              this.indexSelectedFromMenu,
              1
            );
            this.aggiornaCertificazioniCounters.emit();
          } else {
            this.messages.add(
              A4gMessages.getToast(
                "generic",
                A4gSeverityMessage.error,
                "Passaggio di stato della domanda non riuscito"
              )
            );
          }
        },
        error => {
          this.messages.add(
            A4gMessages.getToast(
              "generic",
              A4gSeverityMessage.error,
              "Passaggio di stato della domanda non riuscito"
            )
          );
        }
      );
  }

  // POPOUP E CAMBIAMENTO DI STATO DOMANDA 
  openPopup(statoDichiarazione: StatoDichiarazione) {
    this.statoDaAggiornare = statoDichiarazione;
    this.messages.add(A4gMessages.getToast("aggiorna-stato", A4gSeverityMessage.warn, A4gMessages.WARNING_AGGIORNA_DOMANDA(statoDichiarazione)));
  }
  onConfirmAggiornaStato() {
    this.messages.clear();
    switch (this.statoDaAggiornare) {
      case StatoDichiarazione.RIFIUTATA:
        this.popupComponent.open();
        break;
      case StatoDichiarazione.POSITIVO:
        this.esitoPositivo();
        break;
    }
  }
  public onRejectAggiornaStato() {
    this.messages.clear();
  }


  aggiornaTabella() {
    this.aggiornaCertificazioniCounters.emit();
    this.certificazioneAntimafia.splice(this.indexSelectedFromMenu, 1);
  }

  visualizzaDettaglioDomanda(dichiarazione: DichiarazioneAntimafiaConEsiti, activeIndex: number) {

    let navigationExtras: NavigationExtras = {
      queryParams: {
        "activeIndex": activeIndex,
        "descAzienda": JSON.stringify(dichiarazione.datiDichiarazione.dettaglioImpresa.denominazione),
        "idDichiarazioneAntimafia": dichiarazione.id,
        "cuaa": JSON.stringify(dichiarazione.datiDichiarazione.dettaglioImpresa.codiceFiscale),
        "parentTabIndex": 3
      },
      relativeTo: this.route.parent
    };
    this.router.navigate(["./dettaglioDomandeCollegate"], navigationExtras);
  }


  esitoPositivo() {
    // cambia stato dichiarazione antimafia A4G
    let dichiarazione = this.domandaAntimafiaSelezionataFromMenuAction;
    dichiarazione.stato.descrizione = null;
    dichiarazione.stato.id = null;
    dichiarazione.stato.identificativo = StatoDichiarazione.POSITIVO;
    this.antimafiaService.aggiornaDichiarazioneAntimafia(dichiarazione).subscribe(dichiarazioneUpdate => {
      // recupera esiti antimafia ags
      let esitoAntimafiaExample: SincronizzazioneDateBdnaDto = new SincronizzazioneDateBdnaDto(this.domandaAntimafiaSelezionataFromMenuAction.datiDichiarazione.dettaglioImpresa.codiceFiscale);
      this.istruttoriaAntimafiaService.getEsitiAntimafiaAGS(esitoAntimafiaExample).subscribe(esiti => {
        // AM-04-03-02 PASSAGGIO DA TUTTI GLI STATI A ESITO POSITIVO - setta a null tutte le date
        if (esiti) {
          esiti.forEach(esito => {
            if (esito.cuaa === esitoAntimafiaExample.cuaa) {
              esito.dtFineEsitoNegativo = null;
              esito.dtFineSilenzioAssenso = null;
              esito.dtInizioEsitoNegativo = null;
              esito.dtInizioSilenzioAssenso = null;
            }
            // aggiorna i record in ags
            this.istruttoriaAntimafiaService.sincronizzaDateBDNAAntimafia(esiti);
          });
        }
        // aggiorna tabella e counter
        this.aggiornaTabella();
      }, err => console.log(err));
    }, err => {
      // err put dichiarazione
      this.messages.add(A4gMessages.getToast("generic", A4gSeverityMessage.error, A4gMessages.CTRLIAMCNT007_KO));
    });
  }
  // lazy loading
  loadData(event) {
    if (event.rows) {
      this.elementiPerPagina = event.rows;
    }
    if (event.sortField) {
      let parametroSort: string = "";
      switch (event.sortField) {
        case this.cols[0].field:
          parametroSort = "cuaa";
          break;
        case this.cols[1].field:
          parametroSort = "denominazioneImpresa";
          break;
        default:
          parametroSort = event.sortField;
          break;
      }
      const order = event.sortOrder === 1 ? "+" : "-";
      this.ricercaAntimafiaFilter.sortBy = order.concat(parametroSort);
    }
    this.loadList(event.first, event.globalFilter);
  }

  loadList(start: number, filter: string) {
    filter ? this.ricercaAntimafiaFilter.filtroGenerico = filter : this.ricercaAntimafiaFilter.filtroGenerico = null;
    this.ricercaAntimafiaFilter.statoDichiarazione = StatoDichiarazione.CONTROLLO_MANUALE;
    this.ricercaAntimafiaFilter.pagSize = this.elementiPerPagina;
    this.ricercaAntimafiaFilter.pagStart = start;
    this.istruttoriaAntimafiaService
      .getCertificazioneAntimafia(this.ricercaAntimafiaFilter)
      .subscribe(
        next => {
          this.elementiTotali = next.total;
          this.certificazioneAntimafia = next.results ? next.results : [];
        }, err => {
          console.log("error: " + err);
        }
      );
  }

  private buildDescrizioneTooltip(cert: DichiarazioneAntimafiaConEsiti): string {
    let tooltipDescription = cert.esitoDescrizione;
    if (cert.esitoInvioAgea === 'OK' || cert.esitoInvioAgea === 'KO')
      tooltipDescription += ' | ESITO INVIO AD AGEA ' + cert.esitoInvioAgea;
    else if (cert.esitoInvioAgea === 'ND')
      tooltipDescription += ' | ESITO INVIO AD AGEA NON DISPONIBILE';
    if (cert.esitoInvioBdna === 'OK' || cert.esitoInvioBdna === 'KO')
      tooltipDescription += ' | ESITO INVIO A BDNA ' + cert.esitoInvioBdna;
    else if (cert.esitoInvioBdna === 'ND')
      tooltipDescription += ' | ESITO INVIO A BDNA NON DISPONIBILE';
    return tooltipDescription;
  }
}
