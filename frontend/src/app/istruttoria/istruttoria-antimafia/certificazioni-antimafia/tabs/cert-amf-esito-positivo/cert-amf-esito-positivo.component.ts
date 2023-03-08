import { ActivatedRoute, Router, NavigationExtras } from '@angular/router';
import { DichiarazioneAntimafiaConEsiti } from './../../../dto/DichiarazioneAntimafiaConEsiti';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { Labels } from 'src/app/app.labels';
import { DichiarazioneAntimafia } from 'src/app/fascicolo/antimafia/classi/dichiarazioneAntimafia';
import { ActionMenuComponent } from 'src/app/a4g-common/action-menu.component';
import { StatoDichiarazione } from 'src/app/fascicolo/antimafia/classi/statoDichiarazioneEnum';
import { IstruttoriaAntimafiaService } from '../../../istruttoria-antimafia.service';
import { AntimafiaService } from 'src/app/fascicolo/antimafia/antimafia.service';
import { RicercaAntimafiaFilter } from '../../../dto/RicercaAntimafiaFilter';
import { CaricaCodiceFiscale } from 'src/app/fascicolo/antimafia/classi/datiDichiarazione';
import { switchMap } from 'rxjs/operators';
import { of } from 'rxjs';
import { Subscription } from 'rxjs/internal/Subscription';

@Component({
  selector: 'app-cert-amf-esito-positivo',
  templateUrl: './cert-amf-esito-positivo.component.html',
  styleUrls: ['./cert-amf-esito-positivo.component.css', '../shared/esiti.css']
})
export class CertAmfEsitoPositivoComponent implements OnInit, OnDestroy {
  cols: any[];
  labels = Labels;
  textTitle: string;
  certificazioneAntimafia: Array<DichiarazioneAntimafiaConEsiti>;
  menuAction: any[] = new Array();
  indexSelectedFromMenu: number;
  domandaAntimafiaSelezionataFromMenuAction: DichiarazioneAntimafia;
  currentMenuAction: ActionMenuComponent;
  elementiPerPagina: number = 5;
  ricercaAntimafiaFilter: RicercaAntimafiaFilter = new RicercaAntimafiaFilter();
  elementiTotali = 0;
  dichiarazioneAntimafiaSubscpription: Subscription;

  constructor(private istruttoriaAntimafiaService: IstruttoriaAntimafiaService, private antimafiaService: AntimafiaService, private route: ActivatedRoute,
    private router: Router) {
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
      // { field: "dtBdna", header: this.labels.dtBdna },
      // { field: "protocolloBdna", header: this.labels.protocollo }
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
  }

  menuActionDisplay(): Array<any> {
    return [
      {
        label: Labels.azioni.toUpperCase(),
        items: [
          {
            label: this.labels.SCARICA,
            command: () => {
              this.downloadDichiarazioneAntimafia()
              this.currentMenuAction.reset = true;
            }
          }
        ]
      }
    ];
  }


  ngOnInit() {
    this.menuAction = this.menuActionDisplay();
    this.textTitle = Labels.CERTIFICAZIONI_ANTIMAFIA_POSITIVE;
  }

  ngOnDestroy(): void {
    if (this.dichiarazioneAntimafiaSubscpription) {
      this.dichiarazioneAntimafiaSubscpription.unsubscribe();
    }
  }

  downloadDichiarazioneAntimafia() {
    const getDichiarazioneAntimafia$ = this.antimafiaService.getDichiarazioneAntimafiaWithPdfFirmato(this.domandaAntimafiaSelezionataFromMenuAction.id.toString());

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
        "parentTabIndex": 5
      },
      relativeTo: this.route.parent
    };
    this.router.navigate(["./dettaglioDomandeCollegate"], navigationExtras);
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
    this.ricercaAntimafiaFilter.statoDichiarazione = StatoDichiarazione.POSITIVO;
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
