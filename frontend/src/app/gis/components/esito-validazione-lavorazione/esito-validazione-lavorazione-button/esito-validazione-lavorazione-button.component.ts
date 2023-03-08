import { ToastGisComponent } from './../../toast-gis/toast-gis.component';
import { Component, OnDestroy, OnInit } from '@angular/core';
import UndoRedo from 'ol-ext/interaction/UndoRedo';
import { enumTool } from '../../mappa/gisTools/enumTools';
import { MapEvent } from '../../../shared/MapEvent';
import { PanelEvent } from '../../../shared/PanelEvent';
import { LavorazioniEvent } from 'src/app/gis/shared/LavorazioniEvent';
import { EsitoValidazioneEvent } from 'src/app/gis/shared/EsitoValidazioneEvent';
import { MapService } from '../../mappa/map.service';
@Component({
  selector: 'esito-validazione-lavorazione-button',
  templateUrl: './esito-validazione-lavorazione-button.component.html',
  styleUrls: ['./esito-validazione-lavorazione-button.component.css']
})
export class EsitoValidazioneLavorazioneButtonComponent implements OnInit, OnDestroy {
  checkInteraction: UndoRedo;
  subscription: any;
  constructor(public panelEvent: PanelEvent, private mapEvent: MapEvent, private toastComponent: ToastGisComponent,
    public lavorazioniEvent: LavorazioniEvent, public esitoValidazioneEvent: EsitoValidazioneEvent, private mapService: MapService) { }

  ngOnInit() {
  }

  ngOnDestroy() {
    //this.subscription.unsubscribe();
    this.panelEvent.showEsitoLavorazione = false;
  }

  clearInteraction() {
    console.log('clearInteraction');
    if (this.checkInteraction) {
      this.checkInteraction.clear();
    }
  }


  openEsitoPanel() {
    if (this.mapEvent.undoInteraction._undoStack.length === 0) {
      this.panelEvent.showEsitoLavorazione = true;
      this.panelEvent.showDettaglioLavorazione = false;
      // tslint:disable-next-line: max-line-length
      this.esitoValidazioneEvent.refreshViewValidazioneLavorazioneInCorso = !this.esitoValidazioneEvent.refreshViewValidazioneLavorazioneInCorso;
    } else {
      this.toastComponent.showWarningValida();
    }
  }
}
