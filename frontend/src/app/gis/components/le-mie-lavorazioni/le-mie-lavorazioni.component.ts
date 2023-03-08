import { GisMessaggiToastCostants } from './../../shared/messaggi-toast.constants';
import { GestioneCampagnaService } from './../../services/gestione-campagna.service';
import { MapService } from './../mappa/map.service';
import { PanelEvent } from './../../shared/PanelEvent';
import { SearchGisService } from './../../services/search-gis.service';
import { LavorazioniEvent } from 'src/app/gis/shared/LavorazioniEvent';
import { ConfirmationService } from 'primeng/api';
import { MapEvent } from './../../shared/MapEvent';
import { Component, Input, OnInit, Output, ViewEncapsulation } from '@angular/core';
import { EventEmitter } from '@angular/core';
import { CreazioneLavorazioneService } from '../../services/creazione-lavorazione.service';
import { StatoLavorazioneSuolo, StatoLavorazioneSuoloDecode } from '../../shared/StatoLavorazioneSuolo.enum';
import { CurrentSession } from '../../shared/CurrentSession';
import { contextType } from '../../shared/ContextType';
@Component({
  selector: 'gis-le-mie-lavorazioni',
  templateUrl: './le-mie-lavorazioni.component.html',
  styleUrls: ['./le-mie-lavorazioni.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class LeMieLavorazioniComponent implements OnInit {

  @Input() mylavorazioniSuolo: any;
  @Input() totalsMyLavorazioniSuolo: number;
  @Input() tipo: string;
  @Output() openStepLavorazione = new EventEmitter();
  noOfItemsToShowInitially: number;
  private itemsToLoad: number = 10;
  ordineLavorazioni: string = 'DESC';
  private params = {
    'pagina': 0,
    'numeroElementiPagina': 10,
    'proprieta': 'dataUltimaModifica',
    'ordine': this.ordineLavorazioni
  };
  constructor(private creazioneLavorazioneService: CreazioneLavorazioneService, public contestoAttivo: CurrentSession,
    public mapEvent: MapEvent, public confirmationService: ConfirmationService, public lavorazioniEvent: LavorazioniEvent,
    private searchService: SearchGisService, public panelEvent: PanelEvent, private mapService: MapService,
    private gestioneCampagna: GestioneCampagnaService, private gisMessaggiToast: GisMessaggiToastCostants) { }

  ngOnInit() {
  }

  ngOnChanges() {
    this.mappedStatoLavorazione();
    this.noOfItemsToShowInitially = 10;
    this.resetScroll();
  }

  resetScroll() {
    let scrollBody = document.querySelector('.for-results');
    if (scrollBody) {
      scrollBody.scrollTop = 0;
      this.params.pagina = 0;
      this.noOfItemsToShowInitially = 10;
    }
  }

  onScroll() {
    if (this.lavorazioniEvent.searchParams) {
      this.params = this.lavorazioniEvent.searchParams;
    }
    if (this.noOfItemsToShowInitially <= this.totalsMyLavorazioniSuolo) {
      this.noOfItemsToShowInitially += this.itemsToLoad;
      this.mylavorazioniSuolo.slice(0, this.noOfItemsToShowInitially);
      this.params.pagina += 1;
      this.searchOnScroll(this.params);
    }
  }

  mappedStatoLavorazione() {
    if (this.mylavorazioniSuolo && this.mylavorazioniSuolo.length) {
      for (let i = 0; i < this.mylavorazioniSuolo.length; i++) {
        this.mylavorazioniSuolo[i].statoMappato = StatoLavorazioneSuoloDecode.decode(this.mylavorazioniSuolo[i].stato);
      }
    }
  }

  changeOrderLavorazioni() {
    this.resetScroll();
    if (this.ordineLavorazioni === 'DESC') {
      this.ordineLavorazioni = 'ASC';
    } else {
      this.ordineLavorazioni = 'DESC';
    }
    this.params = {
      'pagina': 0,
      'numeroElementiPagina': 10,
      'proprieta': 'dataUltimaModifica',
      'ordine': this.ordineLavorazioni
    };
    this.mylavorazioniSuolo = [];
    this.searchOnScroll(this.params);
  }

  searchOnScroll(newParams) {
    if (this.tipo === 'ricercaLavorazioni') {
      this.searchService.ricercaLavorazioni(newParams).subscribe(
        (results) => {
          this.mylavorazioniSuolo = this.mylavorazioniSuolo.concat(results['risultati']);
          this.mappedStatoLavorazione();
        },
        (error) => { console.log(error); });
    } else {
      this.creazioneLavorazioneService.getLavorazioniSuoloNonConcluse(newParams).subscribe(
        (results) => {
          this.mylavorazioniSuolo = this.mylavorazioniSuolo.concat(results['risultati']);
          this.mappedStatoLavorazione();
        },
        (error) => { console.log(error); });
    }
  }


  apriPannelloLavorazione(myLav) {
    // Controllo anno di campagna della lavorazione
    const campagna = this.gestioneCampagna.controlloAnnoCampagna(myLav);
    // Se la campagna è diversa mostro la dialog
    if (!campagna) {
      this.confirmationService.confirm({
        message: this.gisMessaggiToast.warningAnnoDiCampagna + myLav.campagna,
        key: 'closeCambioAnnoConfirm',
        icon: 'pi pi-info-circle',
        accept: () => {
          setTimeout(() => {
            this.gestioneCampagna.setAnnoCampagna(myLav.campagna);
            this.gestioneCampagna.disabilitaSelectAnnoCampagna();
            this.aperturaLavorazione(myLav);
          });
        },
        reject: () => {
        }
      });
    } else {
      this.gestioneCampagna.disabilitaSelectAnnoCampagna();
      this.aperturaLavorazione(myLav);
    }
  }

  aperturaLavorazione(myLav) {
    if (this.mapEvent.hasNotSavedAction()) {
      this.confirmationService.confirm({
        message: 'Vuoi uscire dalla lavorazione senza salvare?',
        header: 'Modifiche in mappa rilevate',
        key: 'editingLavorazione',
        icon: 'pi pi-info-circle',
        accept: () => {
          setTimeout(() => {
            this.panelEvent.searchLavorazioni = false;
            this.lavorazioniEvent.noteLavorazione = '';
            this.lavorazioniEvent.titoloLavorazione = '';
            this.lavorazioniEvent.readOnly = myLav.readOnly === 'SI';
            this.openStepLavorazione.emit(myLav);
            this.contestoAttivo.context = contextType.CREAZIONE_LAVORAZIONE;
            this.lavorazioniEvent.editaCelleWorkSpace = this.lavorazioniEvent.stato === 'IN_CORSO' && myLav.readOnly === 'NO' ? true : false;
            // Reset poligoni adl
            this.mapEvent.map.removeLayer(this.lavorazioniEvent.vectorLayerAdl);
            this.lavorazioniEvent.featureAdl = false;

          });
        },
        reject: () => {
          return;
        }
      });
    } else if (myLav && myLav.statoMappato) {
      this.panelEvent.searchLavorazioni = false;
      this.lavorazioniEvent.noteLavorazione = '';
      this.lavorazioniEvent.titoloLavorazione = '';
      this.lavorazioniEvent.readOnly = myLav.readOnly === 'SI';
      this.openStepLavorazione.emit(myLav);
      this.contestoAttivo.context = contextType.CREAZIONE_LAVORAZIONE;
      this.lavorazioniEvent.editaCelleWorkSpace = this.lavorazioniEvent.stato === 'IN_CORSO' && myLav.readOnly === 'NO' ? true : false;
      // Reset poligoni adl
      this.mapEvent.map.removeLayer(this.lavorazioniEvent.vectorLayerAdl);
      this.lavorazioniEvent.featureAdl = false;

    }
  }

  changeYearColor(year) {
    return this.gestioneCampagna.setColorCampagna(year);
  }

}


