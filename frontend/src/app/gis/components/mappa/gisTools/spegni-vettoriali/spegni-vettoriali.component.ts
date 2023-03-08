import { ProfiloUtente, NameProfilo } from './../../../../shared/profilo-utente';
import { ToolBarService } from 'src/app/gis/shared/ToolBar/toolBar.service';
import { PropertyLayer } from './../../../../shared/PropertyLayer.enum';
import { MapEvent } from 'src/app/gis/shared/MapEvent';
import { RightToolBarEvent } from './../../../../shared/RightToolBar/RightToolBarEvent';
import { PanelEvent } from './../../../../shared/PanelEvent';
import { Component, EventEmitter, HostListener, OnInit, Output } from '@angular/core';
import * as cloneDeep from 'lodash/cloneDeep';

@Component({
  selector: 'gis-spegni-vettoriali',
  templateUrl: './spegni-vettoriali.component.html',
  styleUrls: ['./spegni-vettoriali.component.css']
})
export class SpegniVettorialiComponent implements OnInit {
  isActive = false;
  storedLayers: any;
  stateLayerSwitcher: boolean;

  constructor(public mapEvent: MapEvent, public panelEvent: PanelEvent,
    public rightToolbarEvent: RightToolBarEvent, private toolbarService: ToolBarService, public profiloUtente: ProfiloUtente) { }

  @HostListener('document:keypress', ['$event'])
  handleKeyboardEvent(event: KeyboardEvent) {
    if (event.ctrlKey && event.code === 'KeyQ') {
      this.toggleVettoriali();
    }
  }

  ngOnInit() {
  }

  ngOnDestroy(): void {
    this.storedLayers = cloneDeep(this.mapEvent.map.getLayers().getArray());
  }

  toggleVettoriali() {
    const layers = this.mapEvent.map.getLayers().getArray();
    this.isActive = !this.isActive;
    if (this.isActive) {
      this.stateLayerSwitcher = cloneDeep(this.panelEvent.showLayerSwitcher);
      this.storedLayers = cloneDeep(this.mapEvent.map.getLayers().getArray());
      this.rightToolbarEvent.spegniVettorialiActived = true;
      for (let i = 0; i < layers.length; i++) {
        if (this.stateLayerSwitcher) {
          this.panelEvent.showLayerSwitcher = false;
        }
        if (layers[i].get(PropertyLayer.TITLE) !== 'Sfondo' && layers[i].values_.layers) {
          layers[i].getLayers().getArray().forEach((childLayer) => {
            if (childLayer.get(PropertyLayer.NOMELAYER)) {
              childLayer.setVisible(false);
            }
          });
        } else if (layers[i].get(PropertyLayer.TITLE) !== 'Sfondo' && !layers[i].values_.layers) {
          if (layers[i].get(PropertyLayer.NOMELAYER)) {
            layers[i].setVisible(false);
          }
        }
      }
    } else {
      this.rightToolbarEvent.spegniVettorialiActived = false;
      for (let i = 0; i < layers.length; i++) {
        if (this.stateLayerSwitcher) {
          setTimeout(() => {
            this.panelEvent.showLayerSwitcher = true;
          }, 100);
        }
        if (layers[i].get(PropertyLayer.TITLE) !== 'Sfondo' && layers[i].values_.layers) {
          for (let y = 0; y < layers[i].getLayers().getArray().length; y++) {
            layers[i].getLayers().getArray()[y].setVisible(this.storedLayers[i].getLayers().getArray()[y].getVisible());
          }
        } else if (layers[i].get(PropertyLayer.TITLE) !== 'Sfondo' && !layers[i].values_.layers) {
          if (layers[i].get(PropertyLayer.NOMELAYER)) {
            layers[i].setVisible(this.storedLayers[i].getVisible());
          }
        }
      }
    }
    // snap
    console.log('RefreshSnap1', this.toolbarService.snapActive);
    this.toolbarService.refreshSnapOnAllLayers();
  }
}

