import { IstruttoriaAntimafiaService } from 'src/app/istruttoria/istruttoria-antimafia/istruttoria-antimafia.service';
import { DichiarazioneAntimafiaConEsiti } from '../../../dto/DichiarazioneAntimafiaConEsiti';
import { Component, OnInit, Input, Output, SimpleChanges } from "@angular/core";
import { DichiarazioneAntimafia } from "src/app/fascicolo/antimafia/classi/dichiarazioneAntimafia";
import { AntimafiaService } from "src/app/fascicolo/antimafia/antimafia.service";
import { Labels } from "src/app/app.labels";
import { ActionMenuComponent } from "src/app/a4g-common/action-menu.component";
import { StatoDichiarazione } from "src/app/fascicolo/antimafia/classi/statoDichiarazioneEnum";
import { EventEmitter } from "@angular/core";
import { Router, NavigationExtras, ActivatedRoute } from '@angular/router';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';
import { SincronizzazioneDateBdnaDto } from 'src/app/a4g-common/classi/SincronizzazioneDateBdnaDto';
import { MessageService } from 'primeng/api';
import { RicercaAntimafiaFilter } from '../../../dto/RicercaAntimafiaFilter';
import { HttpUrlEncodingCodec } from '@angular/common/http';
@Component({
  selector: 'app-cert-amf-bozza',
  templateUrl: './cert-amf-bozza.component.html',
  styleUrls: ['./cert-amf-bozza.component.css']
})
export class CertAmfBozzaComponent implements OnInit {

  cols: any[];
  intestazioni = Labels;
  textTitle = Labels.CERTIFICAZIONE_ANTIMAFIA_BOZZA;
  isEmpty: boolean;
  currentMenuAction: ActionMenuComponent;
  certificazioniBozzaView: Array<DichiarazioneAntimafiaConEsiti>;
  certificazioniBozza: Array<DichiarazioneAntimafiaConEsiti>;
  domandaAntimafiaSelezionataFromMenuAction: DichiarazioneAntimafia;
  elementiPerPagina: number = 5;
  ricercaAntimafiaFilter: RicercaAntimafiaFilter = new RicercaAntimafiaFilter();
  elementiTotali = 0;

  menuAction: any[] = new Array();
  @Output() aggiornaCertificazioniCounters = new EventEmitter();
  @Output() runStatoAvanzamentoProcesso = new EventEmitter();
  @Input() refreshFromParent: boolean;
  indexSelectedFromMenu: number;

  constructor(
    private istruttoriaAntimafiaService: IstruttoriaAntimafiaService,
    private router: Router,
    private route: ActivatedRoute,
    private messages: MessageService,
    private antimafiaService: AntimafiaService
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
        field: "dtInizioCompilazione",
        header: this.intestazioni.dataInizioCompilazione
      }
    ];
  }

  @Input()
  itemBadges;

  ngOnInit() {
    this.menuAction = this.buildMenuAction();
    this.isEmpty = false;
  }

  ngOnChanges(simpleChanges: SimpleChanges) {
    if (simpleChanges.refreshFromParent.currentValue) {
      this.loadList(0, null);
    }
  }

  // check if dichiarazioni is null or undefined
  checkIfIsEmpty(): boolean {
    if (this.certificazioniBozzaView === undefined) {
      return true;
    }
    if (this.certificazioniBozzaView.length === 0) {
      return true;
    }
    return false;
  }


  getDomandaAntimafiaSelezionataFromMenuAction(): DichiarazioneAntimafia {
    return this.domandaAntimafiaSelezionataFromMenuAction;
  }

  visualizzaDettaglioDomanda(dichiarazione: DichiarazioneAntimafiaConEsiti, activeIndex: number) {
    let navigationExtras: NavigationExtras = {
      queryParams: {
        "activeIndex": activeIndex,
        "descAzienda": JSON.stringify(dichiarazione.datiDichiarazione.dettaglioImpresa.denominazione),
        "idDichiarazioneAntimafia": dichiarazione.id,
        "cuaa": JSON.stringify(dichiarazione.datiDichiarazione.dettaglioImpresa.codiceFiscale),
        "parentTabIndex": 0
      },
      relativeTo: this.route.parent
    };
    this.router.navigate(["./dettaglioDomandeCollegate"], navigationExtras);
  }

  buildMenuAction(): Array<any> {
    return [
      {
        label: Labels.azioni.toUpperCase(),
        items: [
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
    this.ricercaAntimafiaFilter.statoDichiarazione = StatoDichiarazione.BOZZA;
    this.ricercaAntimafiaFilter.pagSize = this.elementiPerPagina;
    this.ricercaAntimafiaFilter.pagStart = start;
    this.istruttoriaAntimafiaService
      .getCertificazioneAntimafia(this.ricercaAntimafiaFilter)
      .subscribe(
        next => {
          this.elementiTotali = next.total;
          this.certificazioniBozza = next.results ? next.results : [];
          this.certificazioniBozzaView = this.certificazioniBozza;
          this.certificazioniBozza.length === 0 ? this.isEmpty = true : this.isEmpty = false;
        }, err => {
          console.log("error: " + err);
          this.isEmpty = true;
        }
      );
  }
}
