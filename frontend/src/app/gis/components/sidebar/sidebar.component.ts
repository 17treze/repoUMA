import { GestioneCampagnaService } from './../../services/gestione-campagna.service';
import { MapService } from './../mappa/map.service';
import { PoligoniDichiaratiEvent } from './../../shared/Poligoni-dichiarati/poligoni-dichiarati-event';
import { AllegatiEvent } from './../../shared/AllegatiEvent';
import { MapEvent } from './../../shared/MapEvent';
import { Component, EventEmitter, OnInit, Output, SimpleChanges, ViewChild, ViewEncapsulation, OnChanges, HostListener } from '@angular/core';
import { SearchGisService } from '../../services/search-gis.service';
import { ToastGisComponent } from './../toast-gis/toast-gis.component';
import { ProfiloUtente, NameProfilo } from './../../shared/profilo-utente';
import { CreazioneLavorazioneService } from './../../services/creazione-lavorazione.service';
import { ConfirmationService } from 'primeng/api';
import { showDettaglioRichiesta } from '../../shared/showDettaglioRichiesta';
import { RichiestaModificaSuoloService } from '../../services/richiesta-modifica-suolo.service';
import { CurrentSession } from '../../shared/CurrentSession';
import { contextType } from '../../shared/ContextType';
import { PanelEvent } from '../../shared/PanelEvent';
import { LavorazioniEvent } from '../../shared/LavorazioniEvent';
import { Stepper } from '../../shared/Stepper';
import { EsitoValidazioneEvent } from '../../shared/EsitoValidazioneEvent';
import { StatoLavorazioneSuolo, StatoLavorazioneSuoloDecode } from '../../shared/StatoLavorazioneSuolo.enum';
import { HeaderLavorazione } from './../../shared/HeaderLavorazione';
import { PropertyLayer } from '../../shared/PropertyLayer.enum';
import { OperazioneLavorazione } from "../../shared/OperazioneLavorazione.enum";
import { GisCostants } from '../../shared/gis.constants';

@Component({
  // tslint:disable-next-line:component-selector
  selector: 'gis-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class SidebarComponent implements OnInit {
  visibleSidebar1;
  display: boolean;
  dismissible: false;
  blockScroll: true;
  showResults: boolean;
  searchResults: any;
  searchFormParams: any;
  detailResults: object = null;
  totalsSearchSuoliDichiarati: number;
  totalsSearchRichieste: number;
  totalsSearchSuoliDichiaratiAssociatiLavorazione: number;
  detailIndex: number;
  datiAggiuntivi: any;
  periodoInterventoDataInizioMapped: Date;
  periodoIntervento: { dataInizio: Date; dataFine: any; };
  allegatiCount: any;
  showButtonSecondPanel: boolean;
  showDetail = false;
  listaComuni: any;
  listaComuniSession = sessionStorage.getItem('listaComuni_TN');
  listaA4gUtentiBoViticoloSession = sessionStorage.getItem('listaA4gUtentiBoViticoloSession');
  response: any;
  poligoniDichiarati: any;
  searchFormParamsLavorazione: any;
  poligoniById: any;
  lavorazioneSuoloInput: any;
  contextPanel: string;

  // Le mie lavorazioni
  leMieLavorazioni: boolean;
  mylavorazioniSuolo: any[];
  totalsMyLavorazioniSuoli = 0;
  ordineLavorazioni = 'DESC';
  resetStep = false;
  lavorazioneChanged = false;
  interventoColturale: any;
  RicercaRichiestaModificaSuolo = false;
  CreazioneLavorazione = false;
  LeMieLavorazioni = false;
  lavorazioniResults: any;
  totalsSearchLavorazioni: any;
  constructor(private searchService: SearchGisService, private toastComponent: ToastGisComponent,
    public profiloUtente: ProfiloUtente, private creazioneLavorazioneService: CreazioneLavorazioneService,
    public dettaglioLavorazione: showDettaglioRichiesta, private confirmationService: ConfirmationService,
    public currentSession: CurrentSession, private richiestaModificaSuoloService: RichiestaModificaSuoloService,
    public panelEvent: PanelEvent, public mapEvent: MapEvent, public allegatiEvent: AllegatiEvent,
    public lavorazioniEvent: LavorazioniEvent, public stepper: Stepper, public esitoValidazioneEvent: EsitoValidazioneEvent,
    public poligoniDichiaraiEvent: PoligoniDichiaratiEvent, private mapService: MapService, public gisCostants: GisCostants,
    private gestioneCampagna: GestioneCampagnaService) {
    this.showResults = this.searchService.isActive;
    this.profiloUtente.profilo = localStorage.getItem('selectedRole');
  }

  ngOnInit() {
    this.lavorazioniEvent.headerLavorazione = HeaderLavorazione.CREAZIONE_LAVORAZIONE;
    this.loadComuni();
    this.loadA4gUtentiBoViticolo();
    this.totalsSearchSuoliDichiaratiAssociatiLavorazione = -1;
    if (this.profiloUtente.profilo !== NameProfilo.CAA) {
      this.ricercaLeMieLavorazioni();
    }
  }

  @HostListener('document:keypress', ['$event'])
  handleKeyboardEvent(event: KeyboardEvent) {
    if (event.ctrlKey && event.code === 'KeyR') {
      this.openPanelRicercaRichiesteModificaSuolo();
    } else if (event.ctrlKey && event.code === 'KeyC' && this.profiloUtente.profilo !== 'caa') {
      if (!this.lavorazioniEvent.idLavorazione) {
        this.apriNuovaLavorazione(null);
      } else {
        this.apriNuovaLavorazione(this.lavorazioniEvent.objectLavorazione);
      }
    } else if (event.ctrlKey && event.code === 'KeyL' && this.totalsMyLavorazioniSuoli > 0 && this.profiloUtente.profilo !== 'caa') {
      this.openLeMieLavorazioni()
    }
  }

  openPanelRicercaRichiesteModificaSuolo() {
    this.closeLeMieLavorazioni();
    this.currentSession.context = contextType.RICERCA_RICHIESTE_MODIFICA_SUOLO;
    this.display = true;
    this.panelEvent.nuovaLavorazione = false;
  }

  closePanelRichiestaModificaSuolo() {
    this.contextPanel = null;
    this.display = false;
  }

  receiveStep($event: number) {
    console.log('receiveStep ', $event);
    this.stepper.currentStepLavorazione = $event;
    if (this.stepper.currentStepLavorazione === 2) {
      this.showDetail = false;
      this.poligoniDichiarati = null;
    }

    this.lavorazioniEvent.controllaEditabilitaLavorazione(false);
  }

  receiveFormStep3($event) {
    // this.valueFormStep3 = $event;
    this.lavorazioneChanged = $event.changed;
    this.lavorazioneSuoloInput = $event.lavorazioneSuolo;
  }

  changeShow(data, filter, onScroll) {
    // console.log(data)
    if (!onScroll) {
      this.showDetail = false;
      this.detailResults = null;
    }
    this.showButtonSecondPanel = !this.showButtonSecondPanel;
    this.panelEvent.searchLavorazioni = false;
    this.showResults = this.searchService.isActive;
    if (data && data.risultati) {
      this.searchResults = data.risultati;
      this.totalsSearchRichieste = data.count;
    } else if (data && onScroll) {
      this.searchResults.concat(data);
    }
    this.searchFormParams = filter;
  }

  changeShowBo(data, filter, onScroll) {
    // console.log(data)
    this.showResults = false;
    this.showDetail = false;
    this.detailResults = null;
    this.panelEvent.searchLavorazioni = false;
    if (data && data.risultati) {
      this.poligoniDichiarati = data.risultati;
      this.totalsSearchSuoliDichiarati = data.count;
      this.panelEvent.openPoligoniDichiarato = true;
    } else if (data && onScroll) {
      this.poligoniDichiarati.concat(data);
    }
    this.searchFormParamsLavorazione = filter;
  }

  changeShowRicercaLavorazioni(data, filter, onScroll) {
    if (!onScroll) {
      this.panelEvent.searchLavorazioni = null;
    }
    this.showResults = false;
    this.showButtonSecondPanel = !this.showButtonSecondPanel;
    this.panelEvent.searchLavorazioni = true;
    this.searchResults = false;
    if (data && data.risultati) {
      this.lavorazioniResults = data.risultati;
      this.totalsSearchLavorazioni = data.count;
    } else if (data && onScroll) {
      this.lavorazioniResults.concat(data);
    }
    this.searchFormParamsLavorazione = filter;
  }
  notifyShowRicercaSuoliDichiarati(data, filter) {
    this.showResults = false;
    this.showDetail = false;
    this.detailResults = null;
    if (data && data.risultati && this.poligoniDichiarati) {
      this.poligoniDichiarati = data.risultati;
      this.totalsSearchSuoliDichiarati = data.count;
    }
    this.searchFormParamsLavorazione = filter;
  }

  changeShowRicercaSuoliDichiarati(data) {
    this.totalsSearchSuoliDichiarati = this.totalsSearchSuoliDichiarati - 1;
  }

  // Rename in loadSuoloDichiaratiInLavorazione??
  loadLavorazioni(data, filter, onScroll) {
    if (data && data.risultati) {
      this.poligoniById = data.risultati;
      this.totalsSearchSuoliDichiaratiAssociatiLavorazione = data.count;
    } else if (data && onScroll) {
      this.poligoniById.concat(data);
    }
  }

  storedDetail(data, index) {

    this.detailResults = data;
    this.detailIndex = index;
    this.datiAggiuntivi = data.datiAggiuntivi;

    if (this.detailResults['datiAggiuntivi'].periodoIntervento.dataInizio) {
      const dataInizioUTC = new Date(this.detailResults['datiAggiuntivi'].periodoIntervento.dataInizio);
      this.detailResults['datiAggiuntivi'].periodoIntervento.dataInizio = dataInizioUTC;
    }

    if (this.detailResults['datiAggiuntivi'].periodoIntervento.dataFine) {
      const dataFineUTC = new Date(this.detailResults['datiAggiuntivi'].periodoIntervento.dataFine);
      this.detailResults['datiAggiuntivi'].periodoIntervento.dataFine = dataFineUTC;
    }

    this.showDetail = true;
  }

  initCountAllegati(data) {
    this.allegatiCount = data;
    this.allegatiEvent.allegatiCount = data;
  }

  closeDetail() {
    this.showDetail = false;
    this.poligoniDichiaraiEvent.showDialog = false;
  }

  closeResults() {
    this.showResults = false;
  }

  loadComuni() {
    if (!this.listaComuniSession) {
      this.searchService.getComuni().then(res => {
        if (res) {
          sessionStorage.setItem('listaComuni_TN', JSON.stringify(res));
        }
      });
    } else {
      this.listaComuni = JSON.parse(this.listaComuniSession = sessionStorage.getItem('listaComuni_TN'));
    }
  }

  loadA4gUtentiBoViticolo() {
    if (!this.listaA4gUtentiBoViticoloSession) {
      this.searchService.getUtentiBoViticolo().then(res => {
        if (res) {
          sessionStorage.setItem('listaA4gUtentiBoViticoloSession', JSON.stringify(res));
        }
      });
    } else {
      this.listaA4gUtentiBoViticoloSession = JSON.parse(this.listaA4gUtentiBoViticoloSession = sessionStorage.getItem('listaA4gUtentiBoViticoloSession'));
    }
  }
  //una chiamata ogni volta che vengono inseriti 3 caratteri 

  openLeMieLavorazioni() {
    this.showDetail = false;
    this.showResults = false;
    this.searchResults = false;
    this.display = false;
    this.panelEvent.nuovaLavorazione = false;
    this.lavorazioniEvent.searchParams = '';
    this.closePanelRichiestaModificaSuolo();
    this.currentSession.context = 'contextLemieLavorazioni';

    this.ricercaLeMieLavorazioni().then(value => {
      this.stepper.goToStep = undefined;
      this.leMieLavorazioni = true;
    });
  }

  closeLeMieLavorazioni() {
    this.leMieLavorazioni = false;
    this.panelEvent.searchLavorazioni = false;
  }



  ricercaLeMieLavorazioni() {
    const params = {
      'pagina': 0,
      'numeroElementiPagina': 10,
      'proprieta': 'dataUltimaModifica',
      'ordine': this.ordineLavorazioni
    };
    return new Promise(((resolve, reject) => {
      this.creazioneLavorazioneService.getLavorazioniSuoloNonConcluse(params).subscribe(
        (res) => {
          this.totalsMyLavorazioniSuoli = res.count;
          this.mylavorazioniSuolo = res['risultati'];
          resolve(true);
        },
        (error) => {
          this.toastComponent.showErrorGetLavorazioni();
        });
    }));
  }
  // Funzioni per Operatore BO
  apriNuovaLavorazione(objectLavorazione) {
    // this.panelEvent.nuovaLavorazione = true;
    this.showDetail = false;
    this.showResults = false;
    this.searchResults = false;
    this.currentSession.context = contextType.CREAZIONE_LAVORAZIONE;
    this.gestioneCampagna.disabilitaSelectAnnoCampagna();
    this.closeLeMieLavorazioni();
    if (!objectLavorazione) {
      this.createNewLavorazione().then(value => {
        this.ricercaLeMieLavorazioni();
      });
      this.lavorazioniEvent.headerLavorazione = HeaderLavorazione.CREAZIONE_LAVORAZIONE;
      this.lavorazioniEvent.stato = StatoLavorazioneSuolo.IN_CREAZIONE;
      this.lavorazioniEvent.statoMappato = StatoLavorazioneSuoloDecode.decode(StatoLavorazioneSuolo.IN_CREAZIONE);
    } else {
      // rientro in un momento successivo
      if (this.lavorazioniEvent.stato === StatoLavorazioneSuolo.IN_CREAZIONE ||
        this.lavorazioniEvent.stato === StatoLavorazioneSuolo.IN_MODIFICA) {
        this.lavorazioniEvent.setLavorazione(objectLavorazione, false);
        this.lavorazioniEvent.statoMappato = StatoLavorazioneSuoloDecode.decode(this.lavorazioniEvent.stato);
        this.totalsSearchSuoliDichiaratiAssociatiLavorazione = -1;
        this.searchPoligoniInLavorazione();
        this.stepper.resetStep = true;
        this.stepper.goToStep = 1;
        this.stepper.currentStepLavorazione = 1;
        if (this.lavorazioniEvent.stato === StatoLavorazioneSuolo.IN_MODIFICA) {
          this.lavorazioniEvent.headerLavorazione = HeaderLavorazione.MODIFICA_LAVORAZIONE;
          this.lavorazioniEvent.setOperazione(OperazioneLavorazione.inizioModifica);
        } else {
          this.lavorazioniEvent.headerLavorazione = HeaderLavorazione.CREAZIONE_LAVORAZIONE;
          this.lavorazioniEvent.setOperazione(OperazioneLavorazione.inizioCreazione);
        }
      } else {
        this.stepper.goToStep = 4;
        this.lavorazioniEvent.headerLavorazione = HeaderLavorazione.DETTAGLIO_LAVORAZIONE;
        this.lavorazioniEvent.setLavorazione(objectLavorazione, false);
      }
      this.panelEvent.nuovaLavorazione = true;
    }
  }

  createNewLavorazione() {
    this.lavorazioniEvent.readOnly = false;
    return new Promise(((resolve, reject) => {
      this.creazioneLavorazioneService.insertLavorazione(this.mapService.annoCampagna).subscribe((response: any) => {
        this.response = response;
        if (this.response['status'] === 200 || this.response['status'] === 201) {
          this.poligoniById = [];
          this.toastComponent.showSuccessNuovaLavorazione();
          this.panelEvent.nuovaLavorazione = true;
          this.lavorazioniEvent.stato = StatoLavorazioneSuolo.IN_CREAZIONE;
          this.lavorazioniEvent.setNuovaLavorazione(this.response.body, StatoLavorazioneSuolo.IN_CREAZIONE, this.mapService.annoCampagna, false);
          this.totalsSearchSuoliDichiaratiAssociatiLavorazione = 0;
          this.stepper.resetStep = true;
          this.lavorazioniEvent.setOperazione(OperazioneLavorazione.inizioCreazione);
          resolve(true);
        } else {
          this.toastComponent.showErrorNuovaLavorazione();
          this.panelEvent.nuovaLavorazione = false;
        }
      },
        error => {
          this.toastComponent.showErrorNuovaLavorazioneEsisteAltraLavorazioneVuota();
          console.log(error);
        })
        ;
    }));
  }
  collapseLavorazione() {
    setTimeout(() => {
      this.panelEvent.nuovaLavorazione = false;
      this.poligoniDichiarati = null;
      this.showDetail = false;
      this.stepper.resetStep = false;
      // Reset poligoni adl
      this.mapEvent.map.removeLayer(this.lavorazioniEvent.vectorLayerAdl);
    });
  }

  collapseLemieLavorazioni() {
    setTimeout(() => {
      this.closeLeMieLavorazioni();
    });
  }

  setAcceptCloseLavorazione() {
    this.panelEvent.nuovaLavorazione = false;
    this.lavorazioniEvent.setOperazione(OperazioneLavorazione.chiudi);
    this.lavorazioniEvent.resetLavorazione();
    this.poligoniDichiarati = null;
    this.lavorazioniEvent.listaLavorazioni = ['1'];
    this.lavorazioniEvent.calcoloAdlActived = false;
    // chiude i pannelli di ricerca suolo
    this.showResults = false;
    this.showDetail = false;
    this.detailResults = null;
    this.stepper.resetStep = true;
    this.stepper.currentStepLavorazione = 1;
    this.stepper.goToStep = 1;
    this.lavorazioneChanged = false;
    this.contextPanel = null;
    this.totalsSearchSuoliDichiaratiAssociatiLavorazione = -1;
    this.lavorazioniEvent.stato = '';
    this.lavorazioniEvent.noteLavorazione = '';
    this.lavorazioniEvent.titoloLavorazione = '';
    // Reset poligoni adl
    this.mapEvent.map.removeLayer(this.lavorazioniEvent.vectorLayerAdl);
    this.gestioneCampagna.attivaSelectAnnoCampagna();
  }

  setRejectCloseLavorazione() {
    this.showResults = false;
    this.showDetail = false;
    this.detailResults = null;
    this.lavorazioneChanged = false;
    this.contextPanel = null;
    this.lavorazioniEvent.noteLavorazione = '';
    this.lavorazioniEvent.titoloLavorazione = '';
  }

  closeLavorazione() {
    if (this.mapEvent.undoInteraction._undoStack.length > 0 || this.lavorazioniEvent.stackAdl) {
      let message = this.gisCostants.messageChiusuraLavorazioneSenzaSalvataggio;
      if (this.lavorazioniEvent.stackAdl) {
        message = message.concat(this.gisCostants.messageChiusuraAdlSenzaSalvataggio);
      }
      this.confirmationService.confirm({
        message: message,
        header: 'Modifiche in mappa rilevate',
        key: 'editingLavorazione',
        icon: 'pi pi-info-circle',
        accept: () => {
          setTimeout(() => {
            this.setAcceptCloseLavorazione();
          });
        },
        reject: () => {
          // chiude i pannelli di ricerca suolo
          this.setRejectCloseLavorazione();
          return;
        }
      });
    } else if ((this.stepper.currentStepLavorazione === 3 || this.stepper.currentStepLavorazione === 4) && this.lavorazioneChanged) {
      // Popup modifiche non salvate
      this.stepper.closeLavorazioneStep3 = true;
    } else {
      this.confirmationService.confirm({
        message: this.gisCostants.messageChiusuraLavorazione,
        header: 'Lavorazione',
        key: 'closeLavorazioneConfirm',
        icon: 'pi pi-info-circle',
        accept: () => {
          setTimeout(() => {
            this.setAcceptCloseLavorazione();
          });
        },
        reject: () => {
          // chiude i pannelli di ricerca suolo
          this.setRejectCloseLavorazione();
          return;
        }
      });
    }
  }


  closeLavorazioneAvviata() {
    if (this.stepper.currentStepLavorazione === 3) {
      this.confirmationService.confirm({
        message: this.gisCostants.modifichenNonSalvate,
        header: 'Lavorazione',
        key: 'closeLavorazioneConfirmStep3',
        icon: 'pi pi-info-circle',
        accept: () => {
          setTimeout(() => {
            this.creazioneLavorazioneService.putLavorazioneSuolo(this.lavorazioniEvent.idLavorazione, this.lavorazioneSuoloInput)
              .subscribe(response => {
                if (response['status'] === 200 || response['status'] === 201) {
                  this.toastComponent.showSuccess();
                  this.lavorazioniEvent.resetLavorazione();
                  this.poligoniDichiarati = null;
                  this.lavorazioniEvent.listaLavorazioni = ['1'];
                  // chiude i pannelli di ricerca suolo
                  this.showResults = false;
                  this.showDetail = false;
                  this.detailResults = null;
                  this.contextPanel = null;
                  this.lavorazioniEvent.noteLavorazione = '';
                  this.lavorazioniEvent.titoloLavorazione = '';
                } else {
                  this.toastComponent.showError();
                }
              });
          });
        },
        reject: () => {
          this.panelEvent.nuovaLavorazione = false;
          this.lavorazioniEvent.resetLavorazione();
          this.poligoniDichiarati = null;
          this.lavorazioniEvent.listaLavorazioni = ['1'];

          // chiude i pannelli di ricerca suolo
          this.showResults = false;
          this.showDetail = false;
          this.detailResults = null;
          this.contextPanel = null;
          this.lavorazioniEvent.noteLavorazione = '';
          this.lavorazioniEvent.titoloLavorazione = '';

        }
      });
      this.gestioneCampagna.attivaSelectAnnoCampagna();
    } else {
      this.confirmationService.confirm({
        message: this.gisCostants.messageChiusuraLavorazione,
        header: 'Lavorazione',
        key: 'closeLavorazioneConfirm',
        icon: 'pi pi-info-circle',
        accept: () => {
          setTimeout(() => {
            this.panelEvent.nuovaLavorazione = false;
            this.lavorazioniEvent.resetLavorazione();
            this.poligoniDichiarati = null;
            this.lavorazioniEvent.listaLavorazioni = ['1'];
            // chiude i pannelli di ricerca suolo
            this.showResults = false;
            this.showDetail = false;
            this.detailResults = null;
            this.gestioneCampagna.attivaSelectAnnoCampagna();

          });
        },
        reject: () => {
          // chiude i pannelli di ricerca suolo
          this.showResults = false;
          this.showDetail = false;
          this.detailResults = null;
          return;
        }
      });
    }

  }

  deleteLavorazione() {
    this.confirmationService.confirm({
      message: this.gisCostants.messageEliminareLavorazione,
      header: 'Attenzione!',
      key: 'deleteLavorazioneConfirm',
      icon: 'pi pi-info-circle',
      accept: () => {
        setTimeout(() => {
          this.creazioneLavorazioneService.deleteLavorazione(this.lavorazioniEvent.idLavorazione)
            .subscribe(response => {
              if (response['status'] === 200 || response['status'] === 201) {

                this.panelEvent.nuovaLavorazione = false;
                this.lavorazioniEvent.resetLavorazione();
                this.poligoniDichiarati = null;
                // chiude i pannelli di ricerca suolo
                this.showResults = false;
                this.showDetail = false;
                this.detailResults = null;
                this.stepper.resetStep = true;
                this.stepper.goToStep = 1;
                this.stepper.currentStepLavorazione = 1;
                this.lavorazioniEvent.noteLavorazione = '';
                this.lavorazioniEvent.titoloLavorazione = '';
                this.toastComponent.showSuccessDeleteLavorazione();
                this.gestioneCampagna.attivaSelectAnnoCampagna();
                //  this.valueFormStep3 = null;
                this.lavorazioniEvent.resetLavorazione();
                this.lavorazioniEvent.refreshWmsLayer([PropertyLayer.CODICE_LAYER_SUOLO_VIGENTE_INCLUSO_IN_LAVORAZIONI_BO]);
                this.totalsSearchSuoliDichiaratiAssociatiLavorazione = -1;
                this.ricercaLeMieLavorazioni();
              } else {
                this.toastComponent.showErrorDelete();
              }
            },
              error => {
                this.toastComponent.showErrorDelete();
              });
        });
      },
      reject: () => {
        return;
      }
    });

  }

  dettaglioLavorazioneTrigger(val) {
    this.panelEvent.showDettaglioLavorazione = true;
    this.panelEvent.showEsitoLavorazione = false;
  }

  searchSuoloDichiaratoRemoveElementTrigger(poligoniById) {
    this.poligoniById = poligoniById;
    this.totalsSearchSuoliDichiaratiAssociatiLavorazione = this.totalsSearchSuoliDichiaratiAssociatiLavorazione - 1;
  }

  collapseDettaglioLavorazione() {
    this.showDetail = false;
    this.panelEvent.showDettaglioLavorazione = false;
  }

  loadDettaglio(idRichiesta, index) {
    return new Promise(((resolve, reject) => {
      this.searchService.getDettaglio(idRichiesta).subscribe(
        (data) => {
          let detail = null;
          detail = data;
          detail.comuniMapped = [];
          for (let i = 0; i < detail.sezioniCatastali.length; i++) {
            detail.comuniMapped.push({ 'cod': detail.sezioniCatastali[i]['codice'] });
          }
          this.storedDetail(detail, index);

        },
        (error) => {
          reject(error);
          console.log(error);
        });
      // carico count allegati
      const filtri = {
        'numeroElementiPagina': 5,
        'pagina': 0,
      };
      this.richiestaModificaSuoloService.getAllegati(idRichiesta, filtri).subscribe((response: any) => {
        this.allegatiEvent.allegati = response.risultati;
        this.initCountAllegati(response.count);
        resolve(true);
      });
    }));
  }

  openStepLavorazione($event) {
    console.log('openStepLavorazione ', $event);
    if ($event && $event.stato) {
      // prima di setLavorazione
      this.lavorazioniEvent.stato = $event.stato;
      if ($event.stato === StatoLavorazioneSuolo.IN_CREAZIONE || $event.stato === StatoLavorazioneSuolo.IN_MODIFICA) {
        this.lavorazioniEvent.setLavorazione($event, true);
        this.lavorazioniEvent.statoMappato = StatoLavorazioneSuoloDecode.decode(this.lavorazioniEvent.stato);
        this.totalsSearchSuoliDichiaratiAssociatiLavorazione = -1;
        this.searchPoligoniInLavorazione();
        this.stepper.resetStep = true;
        this.stepper.goToStep = 1;
        this.stepper.currentStepLavorazione = 1;
        if ($event.stato === StatoLavorazioneSuolo.IN_MODIFICA) {
          this.lavorazioniEvent.headerLavorazione = HeaderLavorazione.MODIFICA_LAVORAZIONE;
          this.lavorazioniEvent.setOperazione(OperazioneLavorazione.inizioModifica);
        } else {
          this.lavorazioniEvent.headerLavorazione = HeaderLavorazione.CREAZIONE_LAVORAZIONE;
          this.lavorazioniEvent.setOperazione(OperazioneLavorazione.inizioCreazione);
        }
      } else {
        this.lavorazioniEvent.statoMappato = StatoLavorazioneSuoloDecode.decode(this.lavorazioniEvent.stato);
        this.stepper.goToStep = 4;
        this.lavorazioniEvent.headerLavorazione = HeaderLavorazione.DETTAGLIO_LAVORAZIONE;
        this.lavorazioniEvent.setLavorazione($event, true);
      }
      this.closeLeMieLavorazioni();
      this.panelEvent.nuovaLavorazione = true;
    }
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
          this.poligoniById = data['risultati'];
          this.totalsSearchSuoliDichiaratiAssociatiLavorazione = data['count'];
        },
        (error) => {
          this.toastComponent.showErrorServer500();
          console.log(error);
        });
  }
}
