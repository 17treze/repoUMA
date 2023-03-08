import { Component, NgModule, OnInit, ViewEncapsulation } from '@angular/core';
import { SearchGisService } from 'src/app/gis/services/search-gis.service';
import { DropdownModule } from 'primeng/dropdown';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { PanelEvent } from 'src/app/gis/shared/PanelEvent';
import { MapService } from '../../map.service';
 // DTO DA UTILIZZARE : layerconfig.ts

@Component({
  selector: 'gis-map-navigator',
  templateUrl: './map-navigator.component.html',
  styleUrls: ['./map-navigator.component.css'],
  encapsulation: ViewEncapsulation.None,

})


export class MapNavigatorComponent implements OnInit {
  display: boolean = false;
  results: string[];
  texts: string[];
  SearchGisService: any;
  selectedComune = null;
  appService: any;
  userlist = [];
  selectedUser = '';
  http: any;


  constructor(private searchService: SearchGisService, public  panelEvent : PanelEvent, private mapService: MapService) {  }
  

  ngOnInit() {  }

  search(event) {
    this.SearchGisService.getResults(event.query).then(data => {
      this.results = data;
    });

  }
  showDialog() {
    this.panelEvent.showmapNavigator = !this.panelEvent.showmapNavigator;
    const vectorLayer = this.mapService.getLayerFromCode('MARKER_LAYER');
    if (vectorLayer != null ) {
      this.mapService.removeLayerFromMap(vectorLayer);
    }
  }
  }







