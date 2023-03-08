import { StatoLavorazioneSuolo } from './../../shared/StatoLavorazioneSuolo.enum';
import { LayerGisService } from 'src/app/gis/services/layer-gis.service';
import { Feature } from 'ol/Feature';
import { PoligoniSuoloDaAdl } from './../../models/poligoniSuolo/poligoniSuoloDaAdl.model';
import { enumTool } from './../mappa/gisTools/enumTools';
import { GisUtils } from './../mappa/gisTools/gisUtils';
import { GisStyles } from './../../shared/GisStyles';
import { MisuraUtils } from './../mappa/gisTools/misura.utils';
import { GisCostants } from 'src/app/gis/shared/gis.constants';
import { showDettaglioRichiesta } from './../../shared/showDettaglioRichiesta';
import { Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges, ViewEncapsulation } from '@angular/core';
import { SearchGisService } from './../../services/search-gis.service';
import { ToastGisComponent } from '../toast-gis/toast-gis.component';
import { SidebarComponent } from '../sidebar/sidebar.component';
import { CreazioneLavorazioneService } from '../../services/creazione-lavorazione.service';
import { SearchGisComponent } from '../search-gis/search-gis.component';
import { ConfirmationService } from 'primeng/api';
import { MapService } from '../mappa/map.service';
import { PanelEvent } from '../../shared/PanelEvent';
import { Stepper } from '../../shared/Stepper';
import { LavorazioniEvent } from '../../shared/LavorazioniEvent';
import { PositionAccordion } from '../../shared/PositionAccordion.enum';
import { EsitoValidazioneEvent } from '../../shared/EsitoValidazioneEvent';
import { EsitoLavorazioneDichiaratoDecode } from '../../shared/EsitoLavorazioneDichiarato.enum';
import { MapEvent } from '../../shared/MapEvent';
import { enumListener } from '../mappa/gisTools/enumListeners';
import { PropertyLayer } from '../../shared/PropertyLayer.enum';
import { ToolBarService } from '../../shared/ToolBar/toolBar.service';
import VectorLayer from 'ol/layer/Vector';
import VectorSource from 'ol/source/Vector';
import GeometryType from 'ol/geom/GeometryType';
import { GeoJSON, WFS } from 'ol/format'; import { GisMessaggiToastCostants } from '../../shared/messaggi-toast.constants';
import { ModalitaADL } from '../../shared/ModalitaADL.enum';
import { PoligoniDichiaratiEvent } from '../../shared/Poligoni-dichiarati/poligoni-dichiarati-event';
import { CodiciErroreClipAdl, CodiciErroreClipAdlDecode } from '../../shared/CodiciErroreClipAdl.enum';

@Component({
  selector: 'creazione-lavorazione',
  templateUrl: './creazione-lavorazione.component.html',
  styleUrls: ['./creazione-lavorazione.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class CreazioneLavorazioneComponent implements OnInit, OnChanges {
  @Input() profiloUtente: any;
  @Input() nuovaLavorazione: boolean;
  @Input() poligoniById: any; // poligoni suolo dichiarato
  @Input() poligoniSuolo: any; // poligoni suolo vigente
  @Input() searchFormParamsLavorazione: any;
  @Input() totalsPoligoniSuolo: number;
  @Input() totalsPoligoniDichiaratoInLavorazione: number;
  @Input() lavorazioneSuolo: any;
  @Output() lavorazioneChangedOutput = new EventEmitter<any>();

  lavorazioneSuoloOutput: any;

  itemsCountToShow = 10;
  public itemsToShow = 10;
  public isFullListDisplayed = false;
  poligoniDichiarati: any;
  response: any;
  searchGisComponent: SearchGisComponent;
  showDettaglioLavorazione: boolean;
  params: any;
  titoloLavorazione: any;
  noteLavorazione: any;
  targetData: {};
  lavorazioneChanged = false;
  titoloLavorazioneLabel: any;
  suoloVigenteResponseBody: any;
  tabIndex: number;

  // Workspace
  totalsPoligoniWorkspace = -1;
  tabAccordionWorkspaceStep4: boolean;

  // Lista decode stato dichiarato
  listaEsitoLavorazione = EsitoLavorazioneDichiaratoDecode.lista;

  // TabView Selezioni poligoni
  POLIGONI_INTERI_TAB = 0;
  POLIGONI_AREA_TAB = 1;
  vectorPoligoniArea: VectorLayer;
  draw: any;

  constructor(private searchService: SearchGisService, private toastComponent: ToastGisComponent,
    public poligoniDichiaratiEvent: PoligoniDichiaratiEvent,
    public dettaglioLavorazione: showDettaglioRichiesta, private sidebarComponent: SidebarComponent,
    private creazioneLavorazioneService: CreazioneLavorazioneService, private confirmationService: ConfirmationService,
    public panelEvent: PanelEvent, public stepper: Stepper, public lavorazioniEvent: LavorazioniEvent,
    public esitoValidazioneEvent: EsitoValidazioneEvent, public mapEvent: MapEvent, private misuraUtils: MisuraUtils,
    private mapService: MapService, public toolBarService: ToolBarService, public gisCostants: GisCostants, private gisStyles: GisStyles,
    private gisMessaggi: GisMessaggiToastCostants, public messaggiGis: GisMessaggiToastCostants, private layerGisService: LayerGisService,) {
    this.sidebarComponent = sidebarComponent;
    this.lavorazioniEvent.featureAdl = null;
  }

  ngOnInit() {
    this.toolBarService.toolEditActive = false;
    const tab = (this.lavorazioniEvent.objectLavorazione &&
      this.lavorazioniEvent.objectLavorazione.modalitaADL === ModalitaADL.DISEGNO_ADL) ? this.POLIGONI_AREA_TAB : this.POLIGONI_INTERI_TAB;
    console.log('tab', tab);
    if (this.stepper.currentStepLavorazione === 2) {
      this.lavorazioniEvent.modalitaAdlTemp = this.lavorazioniEvent.objectLavorazione?.modalitaADL;
      this.manageTabChange(tab, null);
    }
  }
  ngOnChanges(changes: SimpleChanges): void {
    // console.log('lavorazione ', this.lavorazioniEvent, this.lavorazioniEvent.objectLavorazione.modalitaADL, ModalitaADL.DISEGNO_ADL);

    if (this.stepper.currentStepLavorazione === 1) {
      this.totalsPoligoniWorkspace = -1;
    }
    if (changes.checkStep && changes.checkStep.currentValue) {
      this.totalsPoligoniWorkspace = -1;
    }

    if (this.lavorazioneSuolo) {
      this.lavorazioneSuoloOutput = this.lavorazioneSuolo;
      this.lavorazioniEvent.objectLavorazione = this.lavorazioneSuoloOutput;

      this.lavorazioniEvent.titoloLavorazione = this.lavorazioniEvent.titoloLavorazione ?
        this.lavorazioniEvent.titoloLavorazione : this.lavorazioneSuolo.titolo;

      this.lavorazioniEvent.noteLavorazione = this.lavorazioniEvent.noteLavorazione ?
        this.lavorazioniEvent.noteLavorazione : this.lavorazioneSuolo.note;
    }

    if (this.stepper.currentStepLavorazione !== 1) {
      if (this.toolBarService.activeListener === enumListener.selectDichiarato) {
        this.toolBarService.activeListener = null;
      }
    }

    if (this.stepper.currentStepLavorazione !== 2) {
      if (this.toolBarService.activeListener === enumListener.selectSuolo) {
        this.toolBarService.activeListener = null;
      }
    }
  }

  public ricaricaSuolo(params: { pagina: number; numeroElementiPagina: number; idLavorazione: any; }) {
    this.ricercaSuolo(params).then(value => {
      this.totalsPoligoniSuolo = value['count'];
      this.poligoniSuolo = value['risultati'];
    }).catch(error => {
      console.log(error);
    });
  }

  ricercaSuolo(inputParams) {
    const params = {
      'pagina': 0,
      'numeroElementiPagina': 500,
      'idLavorazione': this.lavorazioniEvent.idLavorazione
    };

    const parameter = inputParams ? inputParams : params;
    return new Promise(((resolve, reject) => {
      this.creazioneLavorazioneService.ricercaSuoloVigente(parameter)
        .subscribe(
          (results) => {
            // Quando  aggiungo/rimuovo suolo alla lavorazione viene ricaricato il layer
            this.mapService.refreshLayer(PropertyLayer.CODICE_LAYER_SUOLO_PRENOTATO_LAV_BO, this.lavorazioniEvent.idLavorazione);
            this.mapService.refreshWmsLayer([PropertyLayer.CODICE_LAYER_SUOLO_VIGENTE_INCLUSO_IN_LAVORAZIONI_BO]);
            resolve(results);
          },
          (error) => { console.log(error); });
      setTimeout(() => {
        const messageBody = document.querySelector('.for-poligoni');
        if (messageBody) {
          messageBody.scrollTop = 0;
        }
      });
    }));

  }

  centerMap(extent) {
    this.mapService.centerMap(extent);
  }

  receiveActionSuoloDichiarato(eventSuoloDichiarato) {
    if (eventSuoloDichiarato.action === 'remove') {
      this.removeSuoloDichiaratoFromLavorazione(eventSuoloDichiarato.idSuolo, eventSuoloDichiarato.index);
    } else if (eventSuoloDichiarato.action === 'openDetailRichiesta') {
      this.apriDettaglioRichiesta(eventSuoloDichiarato.idRichiesta, eventSuoloDichiarato.index);
    }
  }

  receiveActionSuoloVigente(eventSuoloVigente) {
    if (eventSuoloVigente.action === 'remove') {
      this.removePoligonoSuoloFromLavorazione(eventSuoloVigente.idSuolo, eventSuoloVigente.index);
    } else if (eventSuoloVigente.action === 'openDetailRichiesta') {
      this.apriDettaglioRichiesta(eventSuoloVigente.idRichiesta, eventSuoloVigente.index);
    }
  }

  public ricaricaSuoloDichiarato() {
    this.sidebarComponent.searchPoligoniInLavorazione();
    this.searchPoligoniDichiarati();
  }

  removeSuoloDichiaratoFromLavorazione(idSuoloDichiarato, index) {
    this.creazioneLavorazioneService.removeSuoloDichiaratoFromLavorazione(this.lavorazioniEvent.idLavorazione, idSuoloDichiarato)
      .subscribe(response => {
        if (response['status'] === 200 || response['status'] === 201) {
          this.toastComponent.showSuccess();
          setTimeout(() => {
            this.poligoniById.splice(index, 1);
            this.sidebarComponent.searchSuoloDichiaratoRemoveElementTrigger(this.poligoniById);
            this.searchPoligoniDichiarati();
          });
        } else {
          this.toastComponent.showErrorDelete();
        }
      },
        (error) => {
          this.toastComponent.showError();
          console.log(error);
        });

  }

  apriDettaglioRichiesta(idRichiesta, index) {
    this.panelEvent.comeFromRicerca = false;
    this.panelEvent.comeFromLavorazione = true;
    this.sidebarComponent.loadDettaglio(idRichiesta, index).then(value => {
      this.panelEvent.showDettaglioLavorazione = true;
      this.sidebarComponent.dettaglioLavorazioneTrigger(this.panelEvent.showDettaglioLavorazione);
    }).catch(error => {
      console.log(error);
    });

  }

  searchPoligoniDichiarati(): void {
    if (!this.searchFormParamsLavorazione) {
      // Quando  rimuovo poligoni di suolo dichiarato alla lavorazione viene ricaricato il layer
      this.mapService.refreshLayer(PropertyLayer.CODICE_LAYER_ISTANZA_DI_RIESAME_LAV_BO, this.lavorazioniEvent.idLavorazione);
      return;
    }

    const params = this.searchFormParamsLavorazione;
    params.pagina = 0;
    params.poligoniSuoloDichiatato.statoLavorazione = null;
    params.poligoniSuoloDichiatato.statoRichiesta = 'LAVORABILE';
    params.poligoniSuoloDichiatato.idLavorazione = null;

    this.searchService.resultsPoligoniSuoloDichiarato(params, false)
      .subscribe(
        (data) => {
          this.poligoniDichiarati = data;
          // let counter = data.risultati;
          if (this.poligoniDichiarati['count'] > 0) {
            this.searchService.isActive = true;
          } else if (this.poligoniDichiarati['count'] === 0) {
            this.toastComponent.showWarning();
          } else {
            this.searchService.isActive = false;
          }
          // Quando  rimuovo poligoni di suolo dichiarato alla lavorazione viene ricaricato il layer
          this.mapService.refreshLayer(PropertyLayer.CODICE_LAYER_ISTANZA_DI_RIESAME_LAV_BO, this.lavorazioniEvent.idLavorazione);
          this.sidebarComponent.notifyShowRicercaSuoliDichiarati(this.poligoniDichiarati, params);
        },
        (error) => {
          this.toastComponent.showErrorServer500();
          console.log(error);
        });
    setTimeout(() => {
      let messageBody = document.querySelector('.for-results');
      if (messageBody) {
        messageBody.scrollTop = 0;
      }
    });
  }

  removePoligonoSuoloVigenteFromLavorazione(idPoligonoSuoloVigente) {
    return new Promise(((resolve, reject) => {
      this.creazioneLavorazioneService.removePoligonoFromLavorazione(this.lavorazioniEvent.idLavorazione, idPoligonoSuoloVigente)
        .subscribe(
          (results) => {
            resolve(results);
          },
          (error) => { console.log(error); });
    }));
  }

  removePoligonoSuoloFromLavorazione(idPoligono, index) {
    this.removePoligonoSuoloVigenteFromLavorazione(idPoligono).then(response => {
      if (response['status'] === 200 || response['status'] === 201) {
        this.toastComponent.showSuccess();
        const params = {
          'pagina': 0,
          'numeroElementiPagina': 500,
          'idLavorazione': this.lavorazioniEvent.idLavorazione
        };
        this.ricaricaSuolo(params);
      } else {
        this.toastComponent.showErrorDelete();
      }
    }).catch(error => {
      this.toastComponent.showErrorDelete();
    });
  }
  // i valori di note o titolo lavorazione sono cambiati
  formStep3Change(name, value) {
    if (name === 'titolo' && value != this.lavorazioneSuolo.titolo) {
      this.lavorazioneChanged = true;
      this.lavorazioneSuoloOutput.titolo = value;
      this.lavorazioniEvent.titoloLavorazione = value;
    } else if (name === 'note' && this.lavorazioneSuolo.note != value) {
      this.lavorazioneChanged = true;
      this.lavorazioneSuoloOutput.note = value;
      this.lavorazioniEvent.noteLavorazione = value;
    }
    this.lavorazioneChangedOutput.emit({ 'changed': true, 'lavorazioneSuolo': this.lavorazioneSuoloOutput });
  }

  salvataggioStep4() {
    // tslint:disable-next-line: max-line-length


    this.creazioneLavorazioneService.putLavorazioneSuolo(this.lavorazioniEvent.idLavorazione, this.lavorazioneSuoloOutput)
      .subscribe(response => {
        this.response = response;
        console.log(this.response);
        if (this.response['status'] === 200 || this.response['status'] === 201) {
          this.toastComponent.showSuccess();
          this.lavorazioneChanged = false;
          this.lavorazioneChangedOutput.emit({ 'changed': false, 'lavorazioneSuolo': this.lavorazioneSuoloOutput });
        } else {
          this.toastComponent.showError();
        }
      },
        (error) => {
          this.toastComponent.showError();
          console.log(error);
        });
  }

  ricalcolaPoligoniSuolo() {
    if (this.poligoniSuolo && this.poligoniSuolo.length > 0) {
      this.confirmationService.confirm({
        message: this.gisCostants.messageRicalcoloPoligoniSuoloDaDichiarato,
        header: 'ATTENZIONE',
        key: 'ricalcoloSuoliVigenti',
        icon: 'pi pi-exclamation-triangle',
        accept: () => {
          setTimeout(() => {
            this.params = {
              'pagina': 0,
              'numeroElementiPagina': 10,
              'idLavorazione': this.lavorazioniEvent.idLavorazione
            };
            this.apriCalcolaPoligoniSuolo();
          });
        },
        reject: () => {
          console.log('no');
        }
      });
    } else {
      this.apriCalcolaPoligoniSuolo();
    }
  }

  associaSuoloDaDichiarato(idLavorazione) {
    return new Promise(((resolve, reject) => {
      this.creazioneLavorazioneService.associaSuoloDaDichiarato(idLavorazione).subscribe(
        (res) => {
          if (res['status'] === 200) {
            resolve(res['body']);
          }
        },
        (error) => {
          reject(error);
          console.log(error);
        });
    }));
  }

  apriCalcolaPoligoniSuolo() {

    this.associaSuoloDaDichiarato(this.lavorazioniEvent.idLavorazione).then(responseAssociazione => {
      this.ricercaSuolo(null).then(value => {
        this.totalsPoligoniSuolo = value['count'];
        this.poligoniSuolo = value['risultati'];
        this.suoloVigenteResponseBody = responseAssociazione;

        if (this.suoloVigenteResponseBody.length > 0) {
          this.showPopupPoligoniSuoloConflitto(this.suoloVigenteResponseBody);
        }
      });
    });
  }

  showPopupPoligoniSuoloConflitto(inputSuoloVigenteConflitto) {

    this.lavorazioniEvent.poligoniSuoloOccupati = [];
    inputSuoloVigenteConflitto.forEach(suolo => {
      this.lavorazioniEvent.poligoniSuoloOccupati.push({
        'ID SUOLO': suolo.suoloVigente.idSuoloVigente,
        'ID LAVORAZIONE': suolo.suoloVigente.idLavorazione,
        'UTENTE': suolo.suoloVigente.utente,
        'ID SUOLI DICHIARATI': suolo.idSuoloDichiarato + '',
        'DATA ULTIMA MODIFICA': suolo.suoloVigente.dataUltimaLavorazione

      });
    });

    if (this.lavorazioniEvent.poligoniSuoloOccupati.length > 0) {
      this.panelEvent.showPoligoniSuoloTable = true;
      this.panelEvent.poligoniSuoloTableType = 'DICHIARATO';
    }
  }

  showPopupErrorePoligoniSuoloPrenotati(inputSuoloPrenotato) {
    this.lavorazioniEvent.poligoniSuoloOccupati = [];
    inputSuoloPrenotato.forEach(suolo => {
      this.lavorazioniEvent.poligoniSuoloOccupati.push({
        // non so se mappare return e tipo errore
        'ID LAVORAZIONE': suolo.ID_LAVORAZIONE_IN_CORSO,
        'ID SUOLO': suolo.ID_SUOLO,
        'UTENTE': suolo.UTENTE,
        'DATA ULTIMA MODIFICA': suolo.DATA_ULTIMA_MODIFICA

      });
    });
    if (this.lavorazioniEvent.poligoniSuoloOccupati.length > 0) {
      this.panelEvent.showPoligoniSuoloTable = true;
      this.panelEvent.poligoniSuoloTableType = 'ADL';
    }
  }

  receiveTotalsPoligoniWorkspace($event) {
    this.totalsPoligoniWorkspace = $event;
  }

  onTabStep4Open(e) {
    if (e.index === PositionAccordion.ACCORDION_STEP4_POLIGONI_WORKSPACE_IN_LAVORAZIONE) {
      this.tabAccordionWorkspaceStep4 = true;
    } else {
      this.tabAccordionWorkspaceStep4 = false;
    }
  }

  onTabStep4Close(e) {
    if (e.index === PositionAccordion.ACCORDION_STEP4_POLIGONI_WORKSPACE_IN_LAVORAZIONE) {
      this.tabAccordionWorkspaceStep4 = false;
    }
  }

  handleTabChange(event) {
    this.manageTabChange(event.index, event);
  }

  manageTabChange(tabNumber, event) {
    this.tabIndex = tabNumber;
    if (event && event.originalEvent) {
      // Se il click è esplicito sul tab
      if (tabNumber === this.POLIGONI_AREA_TAB && this.stepper.currentStepLavorazione === 2) {
        this.lavorazioniEvent.modalitaAdlTemp = ModalitaADL.DISEGNO_ADL;
        // in caso ci siano PoligoniSuolo
        if (this.poligoniSuolo && this.poligoniSuolo.length > 0) {
          this.confirmationService.confirm({
            message: this.gisCostants.messageCambioModalitaSelezioneSuoloLavorazione,
            header: 'ATTENZIONE',
            key: 'closeAdlConfirm',
            icon: 'pi pi-exclamation-triangle',
            accept: () => {
              setTimeout(() => {
                this.creazioneLavorazioneService.deleteAdl(this.lavorazioniEvent.idLavorazione).subscribe((deleted) => {
                  if (deleted) {
                    this.creazioneLavorazioneService.ricaricaADL(this.lavorazioniEvent.idLavorazione)
                      .then((results: PoligoniSuoloDaAdl) => {
                        if (results) {
                          this.setPoligoniAreaTab();
                        }
                      });
                  }
                });
              });
            },
            reject: () => {
              this.tabIndex = this.POLIGONI_INTERI_TAB;
            }
          });
        } else {
          // se non ci sono PoligoniSuolo
          this.creazioneLavorazioneService.deleteAdl(this.lavorazioniEvent.idLavorazione).subscribe((deleted) => {
            if (deleted) {
              this.creazioneLavorazioneService.ricaricaADL(this.lavorazioniEvent.idLavorazione)
                .then((results: PoligoniSuoloDaAdl) => {
                  if (results) {
                    this.setPoligoniAreaTab();
                  }
                });
            }
          });
        }
      } else if (tabNumber === this.POLIGONI_INTERI_TAB && this.lavorazioniEvent.vectorLayerAdl
        && this.stepper.currentStepLavorazione === 2) {
        this.lavorazioniEvent.modalitaAdlTemp = ModalitaADL.POLIGONI_INTERI;
        // in caso ci siano poligoniDaAdl
        if (this.lavorazioniEvent.poligoniDaAdl && this.lavorazioniEvent.poligoniDaAdl.length > 0) {
          this.confirmationService.confirm({
            message: this.gisCostants.messageCambioModalitaSelezioneSuoloLavorazione,
            header: 'ATTENZIONE',
            key: 'closeAdlConfirm',
            icon: 'pi pi-exclamation-triangle',
            accept: () => {
              setTimeout(() => {
                this.creazioneLavorazioneService.deleteAdl(this.lavorazioniEvent.idLavorazione).subscribe((deleted) => {
                  if (deleted) {
                    this.creazioneLavorazioneService.ricaricaADL(this.lavorazioniEvent.idLavorazione)
                      .then((results: PoligoniSuoloDaAdl) => {
                        if (results) {
                          this.setPoligoniInteriTab();
                        }
                      });
                  }
                });
              });
            },
            reject: () => {
              this.tabIndex = this.POLIGONI_AREA_TAB;
            }
          });
        } else {
          // Se non ci sono poligoniDaAdl
          this.setPoligoniInteriTab();
        }
      }
    } else {
      // Se il tab viene selezionato all'apertura della lavorazione
      if (tabNumber === this.POLIGONI_AREA_TAB) {
        this.setPoligoniAreaTab();
      } else if (tabNumber === this.POLIGONI_INTERI_TAB) {
        this.setPoligoniInteriTab();
      }
    }
  }

  setPoligoniAreaTab() {
    const self = this;
    // Recupero Feature ADL
    const featureAdl = this.mapService.getFeaturesFromWfs(PropertyLayer.CODICE_LAYER_ADL);
    // Reimposto i valori dell'ADL
    this.resetADL();
    // Svuoto i Poligoni Suolo
    this.poligoniSuolo = [];
    this.totalsPoligoniSuolo = 0;
    // Se non esiste vectorLayerAdl viene creato
    if (!this.lavorazioniEvent.vectorLayerAdl) {
      this.lavorazioniEvent.vectorLayerAdl = this.layerGisService.createVectorLayer(
        GeometryType.POLYGON, self.toolBarService.misuraStyle, self.gisStyles.newPolygonStyle);
      // Setto i valori sul layer ADL
      this.lavorazioniEvent.vectorLayerAdl.set(PropertyLayer.CODICE, 'ADL');
      this.lavorazioniEvent.vectorLayerAdl.set(PropertyLayer.EDITABILE, true);
      this.lavorazioniEvent.vectorLayerAdl.set(PropertyLayer.DISPLAYINLAYERSWITCHER, false);
    }
    // Aggiungo il layer in mappa
    this.mapEvent.map.addLayer(this.lavorazioniEvent.vectorLayerAdl);
    // Aggiungo la feature al layer ADL
    if (featureAdl && featureAdl.length > 0) {
      this.lavorazioniEvent.vectorLayerAdl.getSource().addFeatures(featureAdl);
      this.lavorazioniEvent.featureAdl = this.lavorazioniEvent.vectorLayerAdl;
    } else {
      this.lavorazioniEvent.vectorLayerAdl.getSource().getFeatures()[0] = [];
    }
    this.creazioneLavorazioneService.ricaricaPoligoniSuoloDaADL(this.lavorazioniEvent.idLavorazione).then((results: PoligoniSuoloDaAdl) => {
      if (results) {
        for (let i = 0; results['risultati'].length > i; i++) {
          if (results['risultati'][i].esitoValidazione !== 'TRUE') {
            this.toastComponent.showErrorGenerico(this.gisMessaggi.poligoniAdlNonValidi);
            break;
          }
        }
        this.lavorazioniEvent.stackAdl = false;
        this.lavorazioniEvent.poligoniDaAdl = results['risultati'];
        this.lavorazioniEvent.totalsPoligoniDaAdl = results['risultati'].length;
      };
    });
  }

  setPoligoniInteriTab() {
    this.resetADL();
    this.lavorazioniEvent.featureAdl = null;
    this.lavorazioniEvent.calcoloAdlActived = false;
    this.lavorazioniEvent.activeDrawAdl = false;
    this.lavorazioniEvent.poligoniDaAdl = [];
    this.lavorazioniEvent.totalsPoligoniDaAdl = 0;
  }

  resetADL() {
    const select = this.toolBarService.getSelectedAdl(this.lavorazioniEvent.featureAdl);
    this.toolBarService.rimuoviListaInteraction([enumTool.modifyAdl], true, true);
    this.toolBarService.rimuoviListaInteraction([enumTool.drawAdl], true, true);
    select.getFeatures().clear();
    this.mapEvent.map.removeLayer(this.lavorazioniEvent.vectorLayerAdl);
    this.lavorazioniEvent.featureAdl = null;
    this.lavorazioniEvent.vectorLayerAdl = null;
    this.lavorazioniEvent.calcoloAdlActived = false;
    this.lavorazioniEvent.activeDrawAdl = false;
    this.lavorazioniEvent.poligoniDaAdl = [];
    this.lavorazioniEvent.totalsPoligoniDaAdl = 0;
  }

  ricalcolaPoligoniDaAdl() {
    this.validaGeometria().then(error => {
      this.lavorazioniEvent.stackAdl = false;
      if (!error) {
        this.calcolaPoligoniSuoloDaADL();
      } else {
        this.toastComponent.showErrorPoligoniAdl();
      }
    });
  }

  validaGeometria() {
    return new Promise(resolve => {
      const parser = GisUtils.getJstsparser();
      for (const featurePoly of this.lavorazioniEvent.featureAdl.getSource().getFeatures()) {
        var jstsPoly = parser.read(featurePoly.getGeometry());
      }
      if (jstsPoly) {
        const error = GisUtils.getValidatationMessage(jstsPoly);
        if (!error) {
          resolve(false);
        } else {
          resolve(true);
        }
      }
    });
  }

  public calcolaPoligoniSuoloDaADL() {
    const editLayer = this.lavorazioniEvent.vectorLayerAdl;
    if (editLayer) {
      const idLavorazione = this.lavorazioniEvent.idLavorazione;
      const featuresSource = editLayer.getSource().getFeatures();
      const featureCollection = [];

      featuresSource.forEach(element => {
        GisUtils.getSinglePartFeature(element, editLayer, this.mapService).forEach(sp => {
          featureCollection.push(sp);
        });
      });
      if (featureCollection.length > 0) {
        const featureCollectionGJS = new GeoJSON().writeFeaturesObject(featureCollection);
        let msgErrore = this.gisMessaggi.erroreEsecuzioneAdl;
        return new Promise(((resolve, reject) => {
          // Salvataggio adl
          this.creazioneLavorazioneService.updateAdleClipsuADL(idLavorazione, featureCollectionGJS).
            subscribe((response: PoligoniSuoloDaAdl) => {
              this.creazioneLavorazioneService.ricaricaADL(idLavorazione).then((results: PoligoniSuoloDaAdl) => {
                if (results) {
                  for (let i = 0; results['risultati'].length > i; i++) {
                    if (results['risultati'][i].esitoValidazione !== 'TRUE') {
                      this.toastComponent.showErrorGenerico(this.gisMessaggi.poligoniAdlNonValidi);
                      break;
                    }
                  }
                  this.lavorazioniEvent.poligoniDaAdl = results['risultati'];
                  this.lavorazioniEvent.totalsPoligoniDaAdl = results['risultati'].length;
                  // console.log(results)
                }
              });
              if (response['status'] === 201) {
                resolve(true);
                if (response['body'] && response['body'].length > 0 &&
                  response['body'][0].RETURN && response['body'][0].RETURN === 'OK') {
                  this.toastComponent.showSuccessGenerico(this.gisMessaggi.ritaglioSuoloAdl, this.gisMessaggi.genericSuccess);
                } else {
                  if (response['body'] && response['body'].length > 0 && response['body'][0].TIPO_ERRORE && response['body'][0].TIPO_ERRORE === CodiciErroreClipAdl.SUOLO_PRENOTATO) {
                    //msgErrore = msgErrore + response['body'][0].TIPO_ERRORE; 
                    msgErrore = msgErrore + CodiciErroreClipAdlDecode.decode(response['body'][0].TIPO_ERRORE);
                    this.showPopupErrorePoligoniSuoloPrenotati(response['body']);
                  } else if (response['body'] && response['body'].length > 0 && response['body'][0].TIPO_ERRORE && response['body'][0].TIPO_ERRORE !== CodiciErroreClipAdl.SUOLO_PRENOTATO) {
                    //msgErrore = msgErrore + response['body'][0].TIPO_ERRORE; 
                    msgErrore = msgErrore + CodiciErroreClipAdlDecode.decode(response['body'][0].TIPO_ERRORE);
                    this.toastComponent.showErrorGenerico(msgErrore);
                  }

                }
              } else {
                if (response['body'] && response['body'].length > 0 && response['body'][0].TIPO_ERRORE) {
                  //msgErrore = msgErrore + response['body'][0].TIPO_ERRORE;
                  msgErrore = msgErrore + CodiciErroreClipAdlDecode.decode(response['body'][0].TIPO_ERRORE);
                }
                this.toastComponent.showErrorGenerico(msgErrore);
                reject(true);
              }
            },
              (error) => {
                this.toastComponent.showErrorGenerico(msgErrore);
                reject(error);
              });
        }));
      }
    }
  }


  openDialogDichiarati() {
    this.poligoniDichiaratiEvent.poligoni = [];
    this.searchPoligoniInLavorazione();

    this.poligoniDichiaratiEvent.showDialog = true;
    this.poligoniDichiaratiEvent.idDettaglioRichiesta = this.lavorazioniEvent.idLavorazione;
    this.poligoniDichiaratiEvent.statoRichiesta = this.lavorazioniEvent.stato;
  }

  searchPoligoniInLavorazione() {
    const params = {
      'idLavorazione': this.lavorazioniEvent.idLavorazione,
      'statoLavorazione': this.lavorazioniEvent.stato,
      'pagina': 0,
      'numeroElementiPagina': 500
    };
    this.searchService.resultsPoligoniSuoloDichiarato(params, true)
      .subscribe(
        (data) => {
          // this.poligoniById = data['risultati'];
          this.poligoniDichiaratiEvent.poligoni = data['risultati'];
          // this.totalsSearchSuoliDichiaratiAssociatiLavorazione = data['count'];
          this.poligoniDichiaratiEvent['poligoni'].forEach(element => {
            if (element['interventoInizio']) {
              element['interventoInizio'] = new Date(element['interventoInizio']);
            }
            if (element['interventoFine']) {
              element['interventoFine'] = new Date(element['interventoFine']);
            }
          });
        },
        (error) => {
          this.toastComponent.showErrorServer500();
          console.log(error);
        });
  }


  public get statoLavorazioneSuolo(): typeof StatoLavorazioneSuolo {
    return StatoLavorazioneSuolo;
  }
}
