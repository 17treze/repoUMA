import { MapEvent } from './../../../shared/MapEvent';
import { Component, Input, ElementRef, OnInit, Host, Optional, ChangeDetectorRef } from '@angular/core';
import { MapService } from '../map.service';
import { MapidService } from '../mapid.service';
import OlMap from 'ol/Map';
import TileWMS from 'ol/source/TileWMS';
import {DragPan, DragZoom, Select } from 'ol/interaction';
import DoubleClickZoom from 'ol/interaction/DoubleClickZoom';

/**
 * Add a control to the map
 * The control can be set inside the map (using parent id) or outside (using a mapId attribute)
 * @example
  <!-- Display a control inside a map -->
  <app-map>
    <app-control></app-control>
  </app-map>

  <!-- Display a control outside a map -->
  <app-control mapId="map"></app-control>
 */
@Component({
  selector: 'gis-control',
  templateUrl: './control.component.html',
})

export class ControlComponent implements OnInit {

  /** Map id
   */
  @Input() mapId: string;
  dismissible: false;
  blockScroll: true;

  rootMap = document.getElementsByTagName('body')[0];
  featureContent: any;
  elem: any;
  wmsSource: TileWMS;
  //private urlGeoserver: string;
  //private geoserverA4sWorkspace: string;
  //private geoserverStemWorkspace: string;
  showWmsResults: boolean = false;
  private map: OlMap;

  /** Define the service
   */
  constructor(
    private mapService: MapService,
    @Host()
    @Optional()
    private mapidService: MapidService,
    public mapEvent: MapEvent
  ) {
   }

  /** Add the control to the map
   */
  ngOnInit() {
    // Get the current map
    this.map = this.mapEvent.map;

    // Cerca le interactions e rimuove DoubleClickZoom
    const interactions = this.map.getInteractions();
    console.log(interactions)
    for (let i = 0; i < interactions.getLength(); i++) {
        const item = interactions.item(i);
        if (item instanceof DoubleClickZoom || item instanceof DoubleClickZoom) {
            this.map.removeInteraction(item);
            break;
        }
    }
    // Cerca le interactions e rimuove il DragPan di Default
    for (let i = 0; i < interactions.getLength(); i++) {
      const item = interactions.item(i);
      if (item instanceof DragPan) {
          this.map.removeInteraction(item);
          break;
      }
   }

  // Cerca le interactions e rimuove il DragZoom di Default
  for (let i = 0; i < interactions.getLength(); i++) {
      const item = interactions.item(i);
      if (item instanceof DragZoom) {
          this.map.removeInteraction(item);
          break;
      }
  }

  // Cerca le interactions e rimuove il tool di Select Default
  for (let i = 0; i < interactions.getLength(); i++) {
      const item = interactions.item(i);
      if (item instanceof Select) {
          this.map.removeInteraction(item);
          break;
      }
  }

  }

}
