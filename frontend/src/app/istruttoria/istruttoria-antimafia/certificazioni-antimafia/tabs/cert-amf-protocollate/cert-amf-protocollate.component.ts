import { CaricaCodiceFiscale } from './../../../../../fascicolo/antimafia/classi/datiDichiarazione';
import { switchMap, map } from 'rxjs/operators';
import { of, Subscription } from 'rxjs';
import { ActivatedRoute, Router, NavigationExtras } from '@angular/router';
import { DichiarazioneAntimafiaConEsiti } from './../../../dto/DichiarazioneAntimafiaConEsiti';
import { Component, OnInit, Input, Output, SimpleChanges, OnDestroy } from "@angular/core";
import { DichiarazioneAntimafia } from "src/app/fascicolo/antimafia/classi/dichiarazioneAntimafia";
import { AntimafiaService } from "src/app/fascicolo/antimafia/antimafia.service";
import { A4gMessages, A4gSeverityMessage } from "src/app/a4g-common/a4g-messages";
import { Labels } from "src/app/app.labels";
import { MessageService } from "primeng/api";
import { ActionMenuComponent } from "src/app/a4g-common/action-menu.component";
import { StatoDichiarazione } from "src/app/fascicolo/antimafia/classi/statoDichiarazioneEnum";
import { EventEmitter } from "@angular/core";
import { IstruttoriaAntimafiaService } from '../../../istruttoria-antimafia.service';
import { SincronizzazioneDateBdnaDto } from 'src/app/a4g-common/classi/SincronizzazioneDateBdnaDto';
import { RicercaAntimafiaFilter } from '../../../dto/RicercaAntimafiaFilter';

@Component({
  selector: "app-cert-amf-protocollate",
  templateUrl: "./cert-amf-protocollate.component.html",
  styleUrls: ["./cert-amf-protocollate.component.css", '../shared/esiti.css']
})
export class CertAmfProtocollateComponent implements OnInit, OnDestroy {
  cols: any[];
  intestazioni = Labels;
  textTitle = Labels.istruttoriaCertificazioneAntimafia;
  isEmpty: boolean;
  currentMenuAction: ActionMenuComponent;
  certificazioniProtocollateView: Array<DichiarazioneAntimafiaConEsiti>;
  certificazioniProtocollate: Array<DichiarazioneAntimafiaConEsiti>;
  domandaAntimafiaSelezionataFromMenuAction: DichiarazioneAntimafia;
  certificazioneProtocollateSelezionate: DichiarazioneAntimafia[];
  elementiPerPagina: number = 5;
  ricercaAntimafiaFilter: RicercaAntimafiaFilter = new RicercaAntimafiaFilter();
  elementiTotali = 0;
  isSelectedAll: boolean;

  menuAction: any[] = new Array();
  @Output() aggiornaCertificazioniCounters = new EventEmitter();
  @Output() runStatoAvanzamentoProcesso = new EventEmitter();
  @Input() refreshFromParent: boolean;
  indexSelectedFromMenu: number;
  dichiarazioneAntimafiaSubscpription: Subscription;

  constructor(
    private antimafiaService: AntimafiaService,
    private messages: MessageService,
    private istruttoriaAntimafiaService: IstruttoriaAntimafiaService,
    private route: ActivatedRoute,
    private router: Router

  ) {
    // ordinamento sulle colonne
    this.cols = [
      {
        field: "datiDichiarazione.dettaglioImpresa.codiceFiscale",
        header: this.intestazioni.cuaaSigla
      },
      {
        field: "datiDichiarazione.dettaglioImpresa.denominazione",
        header: this.intestazioni.descrizioneImpresa
      },
      {
        field: "dtProtocollazione",
        header: this.intestazioni.data
      },
      {
        field: "idProtocollo",
        header: this.intestazioni.nrProtocollo
      },
      {
        field: "esito",
        header: this.intestazioni.esitoTrasmissione
      }
    ];
  }

  @Input()
  itemBadges;

  ngOnInit() {
    this.menuAction = this.menuActionProtocollata();
    this.isEmpty = false;

  }

  ngOnDestroy(): void {
    if (this.dichiarazioneAntimafiaSubscpription) {
      this.dichiarazioneAntimafiaSubscpription.unsubscribe();
    }
  }

  ngOnChanges(simpleChanges: SimpleChanges) {
    if (simpleChanges.refreshFromParent.currentValue) {
      this.loadList(0, null);
    }
  }

  // check if dichiarazioni is null or undefined
  checkIfIsEmpty(): boolean {
    if (this.certificazioniProtocollateView === undefined) {
      return true;
    }
    if (this.certificazioniProtocollateView.length === 0) {
      return true;
    }
    return false;
  }

  // impl algoritmo senza controlli - prende solo quelle selezionate
  avviaControllo() {
    if (
      !this.certificazioneProtocollateSelezionate ||
      this.certificazioneProtocollateSelezionate.length === 0
    ) {
      this.messages.add(
        A4gMessages.getToast(
          "generic",
          A4gSeverityMessage.warn,
          A4gMessages.CTRLIAMPRT001
        )
      );
      return;
    }
    const idList: number[] = this.certificazioneProtocollateSelezionate.map(
      dichiarazione => {
        return dichiarazione.id;
      }
    );
    of(this.isSelectedAll).pipe(
      switchMap(boolSelected => {
        if (boolSelected) {
          return this.istruttoriaAntimafiaService.getCertificazioniAntimafiaByStato(StatoDichiarazione.PROTOCOLLATA)
            .pipe(
              map(page =>
                page.results.map(x => x.id)
              )
            );
        }
        return of(this.certificazioneProtocollateSelezionate.map(x => x.id));
      })
    ).subscribe(idList => {
      console.log("SelectAll active: ", idList);
      this.antimafiaService.avviaControllo(idList).subscribe(
        x => {
          this.runStatoAvanzamentoProcesso.emit(idList.length);
          this.messages.add(
            A4gMessages.getToast(
              "generic",
              A4gSeverityMessage.success,
              A4gMessages.processoAvviatoOK
            )
          );
          //   this.aggiornaDomandeProtocollate(idList);
        },
        err => {
          if ("BRIAMPRT001" === err.error.message) {
            this.messages.add(
              A4gMessages.getToast(
                "generic",
                A4gSeverityMessage.warn,
                A4gMessages.BRIAMPRT001
              )
            );
            return;
          }
          if ("IMPPSRSUPERR" === err.error.message) {
            this.messages.add(
              A4gMessages.getToast(
                "generic",
                A4gSeverityMessage.warn,
                A4gMessages.IMPPSRSUPERR
              )
            );
            return;
          }
          this.messages.add(
            A4gMessages.getToast(
              "generic",
              A4gSeverityMessage.warn,
              A4gMessages.BRIAMPRT003
            )
          );
        }
      );
    });
  }

  onDropdownMenuOpen(
    actionMenu: ActionMenuComponent,
    certificazioneProtocollata: DichiarazioneAntimafia,
    indexSelectedFromMenu: number
  ) {
    this.indexSelectedFromMenu = indexSelectedFromMenu;
    this.domandaAntimafiaSelezionataFromMenuAction = certificazioneProtocollata;
    this.currentMenuAction = actionMenu;
    this.currentMenuAction.reset = false;
  }

  downloadDomandaCertificazioneAntimafia() {
    const certificazioneProtocollata: DichiarazioneAntimafia = this.getDomandaAntimafiaSelezionataFromMenuAction();
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

  getDomandaAntimafiaSelezionataFromMenuAction(): DichiarazioneAntimafia {
    return this.domandaAntimafiaSelezionataFromMenuAction;
  }

  menuActionProtocollata(): Array<any> {
    return [
      {
        label: Labels.azioni.toUpperCase(),
        items: [
          {
            label: Labels.SCARICA,
            command: () => {
              this.downloadDomandaCertificazioneAntimafia();
              this.currentMenuAction.reset = true;
            }
          },
          {
            label: Labels.ESITO_POSITIVO,
            command: () => {
              this.openPopupEsitoPositivo();
              this.currentMenuAction.reset = true;
            }
          }
        ]
      }
    ];
  }

  visualizzaDettaglioDomanda(dichiarazione: DichiarazioneAntimafiaConEsiti, activeIndex: number) {

    let navigationExtras: NavigationExtras = {
      queryParams: {
        "activeIndex": activeIndex,
        "descAzienda": JSON.stringify(dichiarazione.datiDichiarazione.dettaglioImpresa.denominazione),
        "idDichiarazioneAntimafia": dichiarazione.id,
        "cuaa": JSON.stringify(dichiarazione.datiDichiarazione.dettaglioImpresa.codiceFiscale),
        "parentTabIndex": 1
      },
      relativeTo: this.route.parent
    };
    this.router.navigate(["./dettaglioDomandeCollegate"], navigationExtras);
  }
  // POPOUP E CAMBIAMENTO DI STATO DOMANDA A ESITO POSITIVO
  openPopupEsitoPositivo() {
    this.messages.add(A4gMessages.getToast("aggiorna-stato", A4gSeverityMessage.warn, A4gMessages.WARNING_AGGIORNA_DOMANDA(StatoDichiarazione.POSITIVO)));
  }

  onConfirmAggiornaStato() {
    this.messages.clear();
    this.esitoPositivo();
  }

  onRejectAggiornaStato() {
    this.messages.clear();
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
        this.loadList(0, null);
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
    this.ricercaAntimafiaFilter.statoDichiarazione = StatoDichiarazione.PROTOCOLLATA;
    this.ricercaAntimafiaFilter.pagSize = this.elementiPerPagina;
    this.ricercaAntimafiaFilter.pagStart = start;
    this.istruttoriaAntimafiaService
      .getCertificazioneAntimafia(this.ricercaAntimafiaFilter)
      .subscribe(
        next => {
          this.elementiTotali = next.total;
          this.certificazioniProtocollate = next.results ? next.results : [];
          this.certificazioniProtocollateView = this.certificazioniProtocollate;
          this.certificazioniProtocollate.length === 0 ? this.isEmpty = true : this.isEmpty = false;
        }, err => {
          console.log("error: " + err);
          this.isEmpty = true;
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
