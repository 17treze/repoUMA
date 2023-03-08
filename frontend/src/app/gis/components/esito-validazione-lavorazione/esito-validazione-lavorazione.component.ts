import { LayerGisService } from 'src/app/gis/services/layer-gis.service';
import { EventiCalcoloAnomalie } from './../../shared/EventiCalcoloAnomalie';
import { LavorazioniEvent } from '../../shared/LavorazioniEvent';
import { ChangeDetectorRef, Component, Input, OnChanges, OnInit, SimpleChanges, ViewEncapsulation } from '@angular/core';
import { PanelEvent } from '../../shared/PanelEvent';
import { EsitoValidazioneLavorazioneInCorso } from '../../shared/EsitoValidazioneLavorazioneInCorso';
import { CreazioneLavorazioneService } from '../../services/creazione-lavorazione.service';
import { EsitoValidazioneEvent } from '../../shared/EsitoValidazioneEvent';
import { ToastGisComponent } from '../toast-gis/toast-gis.component';
import { EsitoLavorazioneDichiaratoDecode } from '../../shared/EsitoLavorazioneDichiarato.enum';
import { MapService } from '../mappa/map.service';
import { OperazioneLavorazione } from '../../shared/OperazioneLavorazione.enum';

@Component({
  selector: 'gis-esito-validazione-lavorazione',
  templateUrl: './esito-validazione-lavorazione.component.html',
  styleUrls: ['./esito-validazione-lavorazione.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class EsitoValidazioneLavorazioneComponent implements OnInit, OnChanges {
  @Input() refreshView: boolean;
  canVisibleComponent: boolean;
  poligoniErrorValidazione: any;
  dismissible: false;
  blockScroll: true;
  listaEsitoLavorazione = EsitoLavorazioneDichiaratoDecode.lista;
  no_esito_const = {
    id: EsitoValidazioneLavorazioneInCorso.NO_ESITO,
    titolo: 'POLIGONI DICHIARATO SENZA ESITO',
    tooltip: 'Poligoni dichiarato senza esito'
  };
  richiesta_cancellat_const = {
    id: EsitoValidazioneLavorazioneInCorso.RICHIESTA_CANCELLATA,
    titolo: 'POLIGONI DICHIARATO RICHIESTA CANCELLATA',
    tooltip: 'Poligoni dichirato associati a richiesta cancellata'
  };
  attributi_mancanti_const = {
    id: EsitoValidazioneLavorazioneInCorso.ATTRIBUTI_MANCANTI,
    titolo: 'POLIGONI ADL SENZA ATTRIBUTI',
    tooltip: 'Poligoni ADL con attributi mancanti'
  };
  poligoni_sovrapposizioni_const = {
    id: EsitoValidazioneLavorazioneInCorso.POLIGONI_SOVRAPPOSTI,
        titolo: 'SOVRAPPOSIZIONI RILEVATE',
        tooltip: 'Poligoni ADL Sovrapposti'
  };
  poligoni_errori_oracle_const = {
    id: EsitoValidazioneLavorazioneInCorso.ERRORI_ORACLE,
    titolo: 'POLIGONI ADL CON ERRORI ORACLE',
    tooltip: 'Poligoni ADL con errori Oracle',
  };

  poligoni_debordano_area_di_lavoro_const = {
    id: EsitoValidazioneLavorazioneInCorso.POLIGONI_DEBORDANO_AREA_DI_LAVORO,
    titolo: 'DEBORDI DALL\'ADL PRENOTATA',
    tooltip: 'Poligoni ADL che debordano dall\'area di lavoro prenotata',
  };

  constructor(public panelEvent: PanelEvent, public lavorazioniEvent: LavorazioniEvent, public esitoValidazioneEvent: EsitoValidazioneEvent,
    private creazioneLavorazioneService: CreazioneLavorazioneService, private toastComponent: ToastGisComponent,
    private mapService: MapService, public eventiCalcoloAnomalie: EventiCalcoloAnomalie, private layerService: LayerGisService) { }

  ngOnInit() { }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes.refreshView && changes.refreshView.currentValue !== changes.refreshView.previousValue
      && this.panelEvent.showEsitoLavorazione) {

      if (!this.panelEvent.bufferSpinnerLoading) {
        this.panelEvent.bufferSpinnerLoading = [];
      }
      this.panelEvent.bufferSpinnerLoading.push('esito');
      this.validaLavorazioneInCorso(this.lavorazioniEvent.idLavorazione).then(resultValidate => {
        if (resultValidate) {
          this.canVisibleComponent = false;
          const lavSuolo = this.lavorazioniEvent.objectLavorazione;
          this.mapService.ricaricaLavorazioneLayers(this.lavorazioniEvent.idLavorazione, true, false).then(value => {
            this.poligoniErrorValidazione = resultValidate;
            // tslint:disable-next-line: max-line-length
            if (this.poligoniErrorValidazione.poligoniDichiaratoSenzaEsito &&
              this.poligoniErrorValidazione.poligoniDichiaratoSenzaEsito.length > 0) {
              this.canVisibleComponent = true;
            } else if (this.poligoniErrorValidazione.poligoniDichiaratoRichiestaCancellata &&
              this.poligoniErrorValidazione.poligoniDichiaratoRichiestaCancellata.length > 0) {
              this.canVisibleComponent = true;
            } else if (this.poligoniErrorValidazione.poligoniSuoloAttributiMancanti &&
              this.poligoniErrorValidazione.poligoniSuoloAttributiMancanti.length > 0) {
              this.canVisibleComponent = true;
            } else if (this.poligoniErrorValidazione.poligoniAnomaliaSovrapposizioni &&
              this.poligoniErrorValidazione.poligoniAnomaliaSovrapposizioni.length > 0) {
              this.canVisibleComponent = true;
            } else if (this.poligoniErrorValidazione.poligoniAnomaliaDebordanoAreaDiLavoro &&
              this.poligoniErrorValidazione.poligoniAnomaliaDebordanoAreaDiLavoro.length > 0) {
              this.canVisibleComponent = true;
            } else if (this.poligoniErrorValidazione.poligoniAnomalieOracle &&
              this.poligoniErrorValidazione.poligoniAnomalieOracle.length > 0) {
              this.canVisibleComponent = true;
            }

            if (!this.canVisibleComponent) {
              this.toastComponent.showSuccessValidazioneLavorazioneInCorso();
            }
            const index = this.panelEvent.bufferSpinnerLoading.indexOf('esito');
            if (index > -1) {
              this.panelEvent.bufferSpinnerLoading.splice(index, 1);
            }
          });

        } else {
          this.canVisibleComponent = false;
          const index = this.panelEvent.bufferSpinnerLoading.indexOf('esito');
          if (index > -1) {
            this.panelEvent.bufferSpinnerLoading.splice(index, 1);
          }
        }
      }).catch(err => {
        console.log(err);
        this.toastComponent.showErrorValidazioneLavorazioneInCorso();
        this.canVisibleComponent = false;
        const index = this.panelEvent.bufferSpinnerLoading.indexOf('esito');
        if (index > -1) {
          this.panelEvent.bufferSpinnerLoading.splice(index, 1);
        }
      });

    }
  }

  validaLavorazioneInCorso(idLavorazione) {
    const self = this;
    self.eventiCalcoloAnomalie.anomalieGeometriche = [];
    return new Promise((resolve, reject) => {
      this.creazioneLavorazioneService.validaLavorazioneInCorso(idLavorazione).subscribe(
        (results) => {
          // Sovrapposizione Poligoni
          if (results.poligoniAnomaliaSovrapposizioni && results.poligoniAnomaliaSovrapposizioni.length > 0) {
            self.eventiCalcoloAnomalie.anomalieGeometriche.push(results.poligoniAnomaliaSovrapposizioni);
          // Fouri Area di lavoro
          } if (results.poligoniAnomaliaDebordanoAreaDiLavoro && results.poligoniAnomaliaDebordanoAreaDiLavoro.length > 0) {
            self.eventiCalcoloAnomalie.anomalieGeometriche.push(results.poligoniAnomaliaDebordanoAreaDiLavoro);
          // Anomalie Oracle
          } if (results.poligoniAnomalieOracle && results.poligoniAnomalieOracle.length > 0) {
            self.eventiCalcoloAnomalie.anomalieGeometriche.push(results.poligoniAnomalieOracle);
          }
          if (self.eventiCalcoloAnomalie.anomalieGeometriche.length > 0) {
            this.lavorazioniEvent.setOperazione(OperazioneLavorazione.fineValidazione);
          }
          resolve(results);
        },
        (error) => {
          console.log(error);
          reject(error);
        });

    });
  }

  closeEsito() {
    this.panelEvent.showEsitoLavorazione = false;
    this.canVisibleComponent = false;
  }

  centerMap(rowData) {
    this.mapService.centerMapArea(rowData.extent, rowData.area);
  }
}
