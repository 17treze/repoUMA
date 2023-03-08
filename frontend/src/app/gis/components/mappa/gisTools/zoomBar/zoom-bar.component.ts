import { Control } from 'ol/control';
import { Component, Input, ElementRef, OnInit, Host, Optional } from '@angular/core';

import OlMap from 'ol/Map';
import TileWMS from 'ol/source/TileWMS';
import { MapidService } from '../../mapid.service';
import { MapService } from '../../map.service';
import { MapdataService } from '../../mapdata.service';

@Component({
  selector: 'gis-zoom-bar',
  templateUrl: './zoom-bar.component.html',
  styleUrls: ['./zoom-bar.component.css']
})
export class ZoomBarComponent implements OnInit {
  dismissible: false;
  blockScroll: true;

  rootMap = document.getElementsByTagName('body')[0];
  featureContent: any;
  elem: any;
  wmsSource: TileWMS;
  opacityBarVisible = false;
  showWmsResults: boolean = false;
  map: any;
  mapId: MapidService;

  constructor(
    private mapService: MapService,
    @Host()
    @Optional()
    private mapidService: MapidService,
    private elementRef: ElementRef,
  ) {
   }
  ngOnInit() {
    const map: OlMap = this.mapService.getMap(this.mapidService || this.mapId);
    this.map = map;
  }

  zoomIn() {
    var view = this.map.getView();
    var zoom = view.getZoom();
    view.animate({
      zoom:   zoom + 1,
      duration: 500
    });
  }

  zoomOut() {
    var view = this.map.getView();
    var zoom = view.getZoom();
    view.animate({
      zoom:   zoom - 1,
      duration: 500
    });
  }
}
