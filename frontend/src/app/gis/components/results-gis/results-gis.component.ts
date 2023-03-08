import { RichiestaModificaSuoloService } from './../../services/richiesta-modifica-suolo.service';
import { Component, Input, OnChanges, OnInit, ViewEncapsulation } from '@angular/core';
import { SearchResults } from '../../models/searchgis/search-results.model';
import { CreazioneLavorazioneService } from '../../services/creazione-lavorazione.service';
import { SearchGisService } from '../../services/search-gis.service';
import { showDettaglioRichiesta } from '../../shared/showDettaglioRichiesta';
import { MapService } from '../mappa/map.service';
import { MapidService } from '../mappa/mapid.service';
import { SidebarComponent } from '../sidebar/sidebar.component';
import { ToastGisComponent } from '../toast-gis/toast-gis.component';
import OlMap from 'ol/Map';
import { View } from 'ol/View';
import Projection from 'ol/proj/Projection';
import { PanelEvent } from '../../shared/PanelEvent';
import { StatiRichiesta, StatiRichiestaoDecode } from '../../shared/StatiRichiesta.enum';
import { LavorazioniEvent } from '../../shared/LavorazioniEvent';
import { PropertyLayer } from '../../shared/PropertyLayer.enum';

@Component({
  // tslint:disable-next-line:component-selector
  selector: 'results-gis',
  templateUrl: './results-gis.component.html',
  styleUrls: ['./results-gis.component.css'],
  encapsulation: ViewEncapsulation.None

})
export class ResultsGisComponent implements OnChanges {
  // tslint:disable-next-line:member-ordering
  @Input() searchResults: SearchResults[];
  @Input() totals: number;
  @Input() searchFormParams: any;
  @Input() showDetail: boolean;
  @Input() profiloUtente: any;
  @Input() poligoniDichiarati: any;
  @Input() searchFormParamsLavorazione: any;
  @Input() inputPoligoniById: any;

  dismissible: false;
  blockScroll: true;
  http: any;
  sidebarComponent: SidebarComponent;
  clickedElement: number;
  pageStart = 1;
  limit = 10;
  params = {
    'page': this.pageStart,
    'limit': this.limit
  };
  noOfItemsToShowInitially: number;
  public itemsToShow: any
  public isFullListDisplayed: boolean = false;
  private itemsToLoad: number = 10;
  private response: any;
  poligoniById: SearchResults[];
  showDettaglioLavorazione: boolean;
  filterForVisible: any;
  view: View;
  projection: Projection;



  constructor(private searchService: SearchGisService, sidebarComponent: SidebarComponent, private mapService: MapService,
    private mapidService: MapidService, public panelEvent: PanelEvent, private richiestaModificaSuoloService: RichiestaModificaSuoloService,
    private creazioneLavorazione: CreazioneLavorazioneService, public lavorazioniEvent: LavorazioniEvent,
    private toastComponent: ToastGisComponent, public dettaglioLavorazione: showDettaglioRichiesta) {
    this.sidebarComponent = sidebarComponent;
  }


  ngOnChanges() {
    this.mappedStatoRichiesta();
    if (this.poligoniById && this.inputPoligoniById.length != this.poligoniById.length) {
      this.poligoniById = this.inputPoligoniById;
    }
    this.noOfItemsToShowInitially = 10;
  }

  mappedStatoRichiesta() {
    if (this.searchResults && this.searchResults.length) {
      for (let i = 0; i < this.searchResults.length; i++) {
        this.searchResults[i]['statoRichiestaMappato'] = StatiRichiestaoDecode.decode(this.searchResults[i].stato);
      }
    }
  }

    centerMap(extent) {
      this.mapService.centerMap(extent);
    }

    getDetails(idRichiesta, index) {
      this.panelEvent.comeFromRicerca = true;
      this.panelEvent.comeFromLavorazione = false;
      this.sidebarComponent.loadDettaglio(idRichiesta, index);
      this.richiestaModificaSuoloService.loadDichiarati(idRichiesta);
    }


    searchOnScroll(newParams) {
      this.searchService.resultsPost(newParams)
        .subscribe(
          (results) => {
            this.searchResults = this.searchResults.concat(results['risultati']);
            this.sidebarComponent.changeShow(this.searchResults, newParams, 'onScroll');
            this.mappedStatoRichiesta();
          },
          (error) => { console.log(error); });
    }

    searchLavorazioneOnScroll(newParams) {
      this.searchService.resultsPoligoniSuoloDichiarato(newParams, false)
        .subscribe(
          (results) => {
            this.poligoniDichiarati = this.poligoniDichiarati.concat(results['risultati']);
            this.sidebarComponent.changeShowBo(this.poligoniDichiarati, newParams, 'onScroll');
          },
          (error) => { console.log(error); });
    }

    onScroll() {
      if (this.noOfItemsToShowInitially <= this.totals) {
        this.noOfItemsToShowInitially += this.itemsToLoad;
        this.itemsToShow = this.searchResults.slice(0, this.noOfItemsToShowInitially);
        console.log('scrolled down');
        this.searchFormParams.pagina += 1;
        this.searchOnScroll(this.searchFormParams);
      } else {
        this.isFullListDisplayed = true;
      }
    }

    onScrollLavorazione() {
      //console.log(this.noOfItemsToShowInitially)
      if (this.noOfItemsToShowInitially <= this.totals) {
        this.noOfItemsToShowInitially += this.itemsToLoad;
        this.itemsToShow = this.poligoniDichiarati.slice(0, this.noOfItemsToShowInitially);
        console.log('scrolled down');
        if (!this.searchFormParamsLavorazione) {
          this.searchFormParamsLavorazione = {};
          this.searchFormParamsLavorazione.pagina = 0;
          this.searchFormParamsLavorazione.numeroElementiPagina = 10;
        }
        this.searchFormParamsLavorazione.pagina += 1;

        this.searchLavorazioneOnScroll(this.searchFormParamsLavorazione);
      } else {
        this.isFullListDisplayed = true;
      }
    }

    onUp() {
      console.log('scrolled up!');
    }

    // Quando aggiungo un suolo dichiarato alla lavorazione, ricarico la lista di quelli già inseriti nella lavorazione
    searchPoligoniInLavorazione(): void {
      let count = 10;
      if (this.poligoniById && this.poligoniById.length >= 10) {
      count = this.poligoniById.length + 1;
    }
    const params = {
      'idLavorazione': this.lavorazioniEvent.idLavorazione,
      'statoLavorazione': this.lavorazioniEvent.stato,
      'pagina': 0,
      'numeroElementiPagina': count
    };
    this.searchService.resultsPoligoniSuoloDichiarato(params, true)
      .subscribe(
        (data) => {
          this.poligoniById = data;
          this.sidebarComponent.loadLavorazioni(this.poligoniById, params, null);
            //Quando  aggiungo poligoni di suolo dichiarato alla lavorazione viene ricaricato il layer
            this.mapService.refreshLayer(PropertyLayer.CODICE_LAYER_ISTANZA_DI_RIESAME_LAV_BO, this.lavorazioniEvent.idLavorazione);
        },
        (error) => {
          this.toastComponent.showErrorServer500();
          console.log(error);
        });
    setTimeout(() => {
      const messageBody = document.querySelector('.for-results');
      if (messageBody) {
        messageBody.scrollTop = 0;
      }
    });
  }

  updateSuoloDichiaratoToLavorazione(idSuoloDichiarato, i) {
    // tslint:disable-next-line: max-line-length
    this.creazioneLavorazione.putAssociaSuoloDichiaratoALavorazione(this.lavorazioniEvent.idLavorazione, idSuoloDichiarato).subscribe((respone: any) => {
      this.response = respone;
      if (this.response['status'] === 200) {
        this.sidebarComponent.changeShowRicercaSuoliDichiarati(this.poligoniDichiarati);
        this.poligoniDichiarati[i].visible = false;
        // se ci sono meno di 6 risultati la ricerca riparte su scroll automatico per evitare che scompaia
        for (let i = 0; i < this.poligoniDichiarati.length; i++) {
          this.filterForVisible = this.poligoniDichiarati.filter(x => x.visible === true);
        }
        if (this.filterForVisible.length <= 6 && this.totals > 10) {
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
                //Quando  aggiungo poligoni di suolo dichiarato alla lavorazione viene ricaricato il layer
                this.mapService.refreshLayer(PropertyLayer.CODICE_LAYER_ISTANZA_DI_RIESAME_LAV_BO, this.lavorazioniEvent.idLavorazione);
                this.sidebarComponent.changeShowBo(this.poligoniDichiarati, params, null);
              },
              (error) => {
                this.toastComponent.showErrorServer500();
                console.log(error);
              });
          setTimeout(() => {
            var messageBody = document.querySelector('.for-results');
            if (messageBody) {
              messageBody.scrollTop = 0;
            }
          });
        }
        this.toastComponent.showSuccess();
        this.searchPoligoniInLavorazione();
      } else {
        this.toastComponent.showError();
      }
    },
    err => { this.toastComponent.showError(); });
  }

  apriDettaglioRichiesta(idRichiesta, index) {
    this.panelEvent.comeFromRicerca = true;
    this.panelEvent.comeFromLavorazione = false;
    this.sidebarComponent.loadDettaglio(idRichiesta, index).then(value => {
      this.panelEvent.showDettaglioLavorazione = true;
      this.sidebarComponent.dettaglioLavorazioneTrigger(this.panelEvent.showDettaglioLavorazione);
    }).catch(error => {
      console.log(error);
    });

  }



  receiveActionSuoloDichiarato(eventSuoloDichiarato) {
    if (eventSuoloDichiarato.action === 'add') {
      this.updateSuoloDichiaratoToLavorazione(eventSuoloDichiarato.idSuolo, eventSuoloDichiarato.index);
    } else if (eventSuoloDichiarato.action === 'openDetailRichiesta') {
      this.apriDettaglioRichiesta(eventSuoloDichiarato.idRichiesta, eventSuoloDichiarato.index);
    }
  }

}

