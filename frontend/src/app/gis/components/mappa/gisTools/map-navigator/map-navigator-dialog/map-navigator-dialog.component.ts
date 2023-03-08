import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { PanelEvent } from 'src/app/gis/shared/PanelEvent';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { SearchGisService } from 'src/app/gis/services/search-gis.service';
import { MapService } from '../../../map.service';
import { MapidService } from '../../../mapid.service';
import Feature from 'ol/Feature';
import Point from 'ol/geom/Point';
import { fromLonLat } from 'ol/proj.js';
import { Icon, Style } from 'ol/style';
import VectorSource from 'ol/source/Vector';
import { Vector as VectorLayer } from 'ol/layer';
import { PropertyLayer } from 'src/app/gis/shared/PropertyLayer.enum';
import { Location } from 'src/app/gis/models/searchgis/location.model';

@Component({
  selector: 'gis-map-navigator-dialog',
  templateUrl: './map-navigator-dialog.component.html',
  styleUrls: ['./map-navigator-dialog.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class MapNavigatorDialogComponent implements OnInit {

  foundedLocations: Location[];
  selectedLocation: Location;
  locationFeature: Feature;

  constructor(
    public panelEvent: PanelEvent,
    public http: HttpClient,
    private searchService: SearchGisService,
    private mapService: MapService,
    private mapidService: MapidService
  ) {}

  ngOnInit() {}

  ricercaLocalita(event: {query: string}) {
    const query = event.query;

    const coordinateLocations = this.searchService.getRicercaPerCoordinate(query);
    
    if(coordinateLocations) {
      this.foundedLocations = coordinateLocations;
    } else {
      this
        .searchService
        .getRicercaInMappa(query)
        .then(search => this.foundedLocations = search.results.map(result => result.attrs));
    }
  }

  //function that prints attribute by clicking on a element of a dropdown list
  printCoordinate() {
    //quando un utente fa una ricerca inserisce un valore e lo seleziona chiama print coordinate che rimuove il marker layer se c'è
    const vectorLayer = this.mapService.getLayerFromCode('MARKER_LAYER');

    if(vectorLayer != null) {
      this.mapService.removeLayerFromMap(vectorLayer);
    }
    
    const extent = [this.selectedLocation.x, this.selectedLocation.y, this.selectedLocation.x, this.selectedLocation.y];
    this.mapService.centerMap(extent);

    this.locationFeature = new Feature({
      geometry: new Point([this.selectedLocation.x, this.selectedLocation.y])
    });

    this.locationFeature.setStyle(this.mapService.markerStyle(this.selectedLocation.label));

    const markerVectorLayer = new VectorLayer({
      source: new VectorSource(),
      displayInLayerSwitcher : false,
    });

    markerVectorLayer.set(PropertyLayer.CODICE, 'MARKER_LAYER');
    markerVectorLayer.set(PropertyLayer.ALLWAYSONTOP, true);
    markerVectorLayer.getSource().addFeature(this.locationFeature);

    this.mapService.addVectorLayerToMap(markerVectorLayer);
  }
}