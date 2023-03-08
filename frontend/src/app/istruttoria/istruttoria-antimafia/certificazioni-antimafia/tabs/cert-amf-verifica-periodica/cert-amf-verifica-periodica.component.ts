import { ActivatedRoute, Router, NavigationExtras } from '@angular/router';
import { DichiarazioneAntimafiaConEsiti } from './../../../dto/DichiarazioneAntimafiaConEsiti';
import { Component, OnInit, Output, EventEmitter, OnDestroy } from '@angular/core';
import { Labels } from 'src/app/app.labels';
import { DichiarazioneAntimafia } from 'src/app/fascicolo/antimafia/classi/dichiarazioneAntimafia';
import { StatoDichiarazione } from 'src/app/fascicolo/antimafia/classi/statoDichiarazioneEnum';
import { AntimafiaService } from 'src/app/fascicolo/antimafia/antimafia.service';
import { IstruttoriaAntimafiaService } from '../../../istruttoria-antimafia.service';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';
import { SincronizzazioneDateBdnaDto } from 'src/app/a4g-common/classi/SincronizzazioneDateBdnaDto';
import { MessageService } from 'primeng/api';
import { ActionMenuComponent } from 'src/app/a4g-common/action-menu.component';
import { RicercaAntimafiaFilter } from '../../../dto/RicercaAntimafiaFilter';
import { CaricaCodiceFiscale } from 'src/app/fascicolo/antimafia/classi/datiDichiarazione';
import { switchMap } from 'rxjs/operators';
import { of, Subscription } from 'rxjs';

@Component({
  selector: 'app-cert-amf-verifica-periodica',
  templateUrl: './cert-amf-verifica-periodica.component.html',
  styleUrls: ['./cert-amf-verifica-periodica.component.css', '../shared/esiti.css']
})
export class CertAmfVerificaPeriodicaComponent implements OnInit, OnDestroy {

  textTitle: string = Labels.CERTIFICAZIONI_ANTIMAFIA_VERIFICA_PERIODICA;
  labels = Labels;
  cols: any[];
  certificazioneAntimafia: Array<DichiarazioneAntimafiaConEsiti>;
  indexSelectedFromMenu: number;
  domandaAntimafiaSelezionataFromMenuAction: DichiarazioneAntimafia;
  currentMenuAction: ActionMenuComponent;
  menuAction: any[] = new Array();
  @Output() aggiornaCertificazioniCounters = new EventEmitter();
  elementiPerPagina: number = 5;
  ricercaAntimafiaFilter: RicercaAntimafiaFilter = new RicercaAntimafiaFilter();
  elementiTotali = 0;
  dichiarazioneAntimafiaSubscpription: Subscription;

  constructor(private antimafiaService: AntimafiaService, private istruttoriaAntimafiaService: IstruttoriaAntimafiaService, private route: ActivatedRoute,
    private router: Router, private messages: MessageService) {
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
    this.menuAction = this.menuActionDisplay();
  }

  ngOnDestroy(): void {
    if (this.dichiarazioneAntimafiaSubscpription) {
      this.dichiarazioneAntimafiaSubscpription.unsubscribe();
    }
  }

  menuActionDisplay(): Array<any> {
    return [
      {
        label: Labels.azioni.toUpperCase(),
        items: [
          {
            label: this.labels.SCARICA,
            command: () => {
              this.downloadDichiarazioneAntimafia(this.domandaAntimafiaSelezionataFromMenuAction.id.toString());
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


  downloadDichiarazioneAntimafia(id: string) {
    const getDichiarazioneAntimafia$ = this.antimafiaService.getDichiarazioneAntimafiaWithPdfFirmato(id);
    this.dichiarazioneAntimafiaSubscpription = getDichiarazioneAntimafia$.pipe(
      switchMap((dichiarazione: DichiarazioneAntimafia) => {
        this.antimafiaService.downloadDichiarazioneAntimafia(dichiarazione.pdfFirmato, dichiarazione.tipoPdfFirmato);
        return of(dichiarazione);
      })
    ).subscribe((dichiarazione: DichiarazioneAntimafia) => {
      const cariche: Array<CaricaCodiceFiscale> = this.antimafiaService.getAllegati(dichiarazione);
      this.antimafiaService.downloadAllegati(cariche);
    });
  }

  visualizzaDettaglioDomanda(dichiarazione: DichiarazioneAntimafiaConEsiti, activeIndex: number) {

    let navigationExtras: NavigationExtras = {
      queryParams: {
        "activeIndex": activeIndex,
        "descAzienda": JSON.stringify(dichiarazione.datiDichiarazione.dettaglioImpresa.denominazione),
        "idDichiarazioneAntimafia": dichiarazione.id,
        "cuaa": JSON.stringify(dichiarazione.datiDichiarazione.dettaglioImpresa.codiceFiscale),
        "parentTabIndex": 6
      },
      relativeTo: this.route.parent
    };
    this.router.navigate(["./dettaglioDomandeCollegate"], navigationExtras);
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
      const order = event.sortOrder === 1 ? '+' : "-";

      this.ricercaAntimafiaFilter.sortBy = order.concat(parametroSort);
    }
    this.loadList(event.first, event.globalFilter);
  }

  loadList(start: number, filter: string) {
    filter ? this.ricercaAntimafiaFilter.filtroGenerico = filter : this.ricercaAntimafiaFilter.filtroGenerico = null;
    this.ricercaAntimafiaFilter.statoDichiarazione = StatoDichiarazione.VERIFICA_PERIODICA;
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
