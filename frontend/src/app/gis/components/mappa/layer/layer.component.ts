import { HttpClient } from '@angular/common/http';
import { Component, Input, NgZone, OnInit } from '@angular/core';
import WMTSCapabilities from 'ol/format/WMTSCapabilities';
import GeometryType from 'ol/geom/GeometryType';
import { Image as ImageLayer } from 'ol/layer';
import LayerGroup from 'ol/layer/Group';
import { default as OlTileLayer, default as TileLayer } from 'ol/layer/Tile';
import VectorLayer from 'ol/layer/Vector';
import OlMap from 'ol/Map';
import * as s from 'ol/source';
import ImageWMS from 'ol/source/ImageWMS';
import TileWMS from 'ol/source/TileWMS';
import VectorSource from 'ol/source/Vector';
import WMTS, { optionsFromCapabilities } from 'ol/source/WMTS';
import { Circle, Fill, Stroke, Style, Text } from 'ol/style';
import { createForProjection } from 'ol/tilegrid';
import TileGrid from 'ol/tilegrid/TileGrid';
import { LayerGisService } from 'src/app/gis/services/layer-gis.service';
import { LayerConfig } from 'src/app/gis/shared/LayerConfig';
import { PropertyLayer } from 'src/app/gis/shared/PropertyLayer.enum';
import { GisCostants } from '../../../shared/gis.constants';
import { GisStyles } from '../../../shared/GisStyles';
import { MapService } from '../map.service';
import { MapidService } from '../mapid.service';
import { MapEvent } from './../../../shared/MapEvent';
import { CaricaVettorialiCostants } from './../gisTools/carica-vettoriali/carica-vettorialicostants';


/**
 * Add layers to a map
 * @example
  <app-map>
    <app-layer></app-layer>
  </app-map>
 */
@Component({
  selector: 'gis-layer',
  template: ''
})

export class LayerComponent implements OnInit {
  /** Layer */
  @Input() layer;
  /** Layer opacity */
  @Input() name;
  /** Layer opacity */
  @Input() opacity = 1;
  /** Layer visibility */
  @Input() visibility = true;

  @Input() profiloUtente;


  private map: OlMap;
  markerVSource = new s.Vector({ features: [], attributions: 'Center', });
  tonerLabels: OlTileLayer;
  waterColor: OlTileLayer;
  hostLayer: string;
  layersSwitcher: any[];
  parser = new WMTSCapabilities();
  urlGetCapabilities: string;
  parsedTextCapabilities: any;

  /** Define the service
   */
  constructor(
    private zone: NgZone, private gisConstants: GisCostants,
    private mapService: MapService,
    private mapidService: MapidService,
    private layerGisService: LayerGisService,
    public mapEvent: MapEvent,
    public caricaVettorialiCostants: CaricaVettorialiCostants,
    private http: HttpClient,
    private gisStyles: GisStyles
  ) {
    this.hostLayer = gisConstants.hostRichiesta;
  }



  ngOnInit() {
    // Get the current map
    this.mapEvent.map = this.mapService.getMap(this.mapidService);
    this.map = this.mapEvent.map;
    this.buildLayerSwitcher();
  }


  buildLayerSwitcher() {

    // Get response layer
    this.layerGisService.getLayerConfig(null).then(value => {

      const resultLayer = [];
      resultLayer.push(value);
      let hasWmtsLayer = false;

      this.layersSwitcher = [];

      for (let i = 0; i < this.mapService.layerConfig.length; i++) {
        if (this.mapService.layerConfig[i].mapProperty.get(PropertyLayer.TIPO_WMS) === PropertyLayer.TIPO_WMS_WMTS) {
          // tslint:disable-next-line:max-line-length
          this.urlGetCapabilities = this.gisConstants.hostRichiesta + this.mapService.layerConfig[i].url + '/gwc/service/wmts?request=getCapabilities';
          hasWmtsLayer = true;
          break;
        }
      }

      if (hasWmtsLayer) {
        this.layerGisService.getCapabilities(this.urlGetCapabilities).subscribe(
          (response) => {
            const text = response.body;
            this.parsedTextCapabilities = this.parser.read(text);
            this.manageLayers();
            this.sortLayer();
          },
          (error) => {
            console.log(error);
          });
      } else {
        this.manageLayers();
        this.sortLayer();
      }


    }).catch(error => {

    });
  }

  manageLayers() {
    this.mapService.layerConfig.forEach(currentLayer => {

      let layer = null;
      // check layer format WMS
      if (currentLayer.mapProperty.get(PropertyLayer.FORMATO) === PropertyLayer.FORMATO_WMS) {

        if (currentLayer.mapProperty.get(PropertyLayer.TIPO_WMS) === PropertyLayer.TIPO_WMS_IMAGE) {
          layer = this.addImageLayer(currentLayer);
        } else if (currentLayer.mapProperty.get(PropertyLayer.TIPO_WMS) === PropertyLayer.TIPO_WMS_WMTS) {
          layer = this.addWmtsLayer(currentLayer);
        } else {
          layer = this.addTileLayer(currentLayer);
        }
      } else if (currentLayer.mapProperty.get(PropertyLayer.FORMATO) === PropertyLayer.FORMATO_WFS) { // check layer format WFS
        layer = this.addVectorLayer(currentLayer);
      }
      // Check layer have group
      this.checkLayerHaveGroup(layer, currentLayer);
    });

  }

  checkLayerHaveGroup(layer, currentLayer) {
    if (layer) {
      return new Promise(((resolve, reject) => {
        if (currentLayer.mapProperty.get(PropertyLayer.GRUPPO)) {
          // Check group is present,add layer
          if (this.layersSwitcher.length > 0 &&
            this.layersSwitcher.findIndex(
              x => x.title === currentLayer.mapProperty.get(PropertyLayer.GRUPPO)) > -1) {
            this.layersSwitcher[this.layersSwitcher.findIndex(x => x.title === currentLayer.mapProperty.get(PropertyLayer.GRUPPO))]
              .layers.push({ 'position': currentLayer.mapProperty.get(PropertyLayer.POSIZIONE), 'layers': layer });
          } else {
            // Create new group and add layer
            const groupObj = {
              'title': '',
              'position': '',
              'collassaGruppo': '',
              'layers': []
            };
            groupObj.title = currentLayer.mapProperty.get(PropertyLayer.GRUPPO);
            groupObj.position = currentLayer.mapProperty.get(PropertyLayer.POSIZIONE);
            groupObj.collassaGruppo = currentLayer.mapProperty.get(PropertyLayer.COLLASSAGRUPPO);
            groupObj.layers.push({ 'position': currentLayer.mapProperty.get(PropertyLayer.POSIZIONE), 'layers': layer });
            this.layersSwitcher.push(groupObj);

          }

        } else {
          this.layersSwitcher.push({ 'position': currentLayer.mapProperty.get(PropertyLayer.POSIZIONE), 'layers': layer });
        }
        resolve(true);
      }));
    }
  }

  sortLayer() {
    const layersSwitcherSort = this.layersSwitcher.sort((a, b) => (a.position > b.position) ? 1 : -1);
    layersSwitcherSort.forEach(x => {
      // check layer have group
      if (x && x.title) {
        this.map.addLayer(this.layerGisService.addLayerGroup(x));
      } else {
        this.map.addLayer(x.layers);
      }
    });
  }

  setLayerProperties(layer: TileLayer | VectorLayer, currentLayer: LayerConfig) {
    this.layerGisService.layerProperties(layer, currentLayer);
  }

  addWmtsLayer(currentLayer: LayerConfig): TileLayer {
    const workspace = currentLayer.workspace.split('/')[1];
    const identifier = workspace + ':' + currentLayer.getNomeLayer(this.mapService.annoCampagna);
    const options = optionsFromCapabilities(this.parsedTextCapabilities, {
      layer: identifier
    });
    for (let i = 0; i < options.urls.length; i++) {
      const optionUrl = options.urls[i];
      options.urls[i] = this.hostLayer + currentLayer.url + optionUrl.substring(optionUrl.indexOf('/gwc'), optionUrl.length);
    }
    const tileWMTSLayer = new TileLayer({
      opacity: 1,
      source: new WMTS({
        ...options,
        tileLoadFunction: this.loadFunction
      })
    });

    // setta le proprietà del layer
    this.setLayerProperties(tileWMTSLayer, currentLayer);

    return tileWMTSLayer;

  }

  addTileLayer(currentLayer: LayerConfig): TileLayer {

    const tileLayer = new TileLayer({
      source: new TileWMS({
        projection: this.gisConstants.defaultProjection,
        tileGrid: this.getTileGrid(currentLayer),
        url: this.hostLayer + currentLayer.url + currentLayer.workspace + '/' + currentLayer.mapProperty.get(PropertyLayer.FORMATO),
        params: {
          'LAYERS': currentLayer.getNomeLayer(this.mapService.annoCampagna),
          'TILED': true,
          'FORMAT': currentLayer.mapProperty.get(PropertyLayer.FORMATO_IMMAGINE) !== undefined
            ? currentLayer.mapProperty.get(PropertyLayer.FORMATO_IMMAGINE)
            : PropertyLayer.FORMATO_IMAGE_DEFAULT
        },
        serverType: 'geoserver',
        transition: 0,
        attributions: currentLayer.mapProperty.get(PropertyLayer.ATTRIBUTION),
        tileLoadFunction: this.loadFunction
      })
    });

    this.addPropertiesWMS(currentLayer, tileLayer);
    return tileLayer;
  }

  private getTileGrid(layer: LayerConfig): TileGrid | undefined {
    const tileSize = layer.mapProperty.get(PropertyLayer.TILE_SIZE);

    if (!tileSize) {
      return undefined;
    }

    const projection = this.gisConstants.defaultProjection;
    const grid: TileGrid = createForProjection(projection, undefined, tileSize, undefined);

    return grid;
  }

  addImageLayer(currentLayer: LayerConfig): ImageLayer {
    const imageLayer = new ImageLayer({
      source: new ImageWMS({
        projection: this.gisConstants.defaultProjection,
        url: this.hostLayer + currentLayer.url + currentLayer.workspace + '/' + currentLayer.mapProperty.get(PropertyLayer.FORMATO),
        params: {
          'LAYERS': currentLayer.getNomeLayer(this.mapService.annoCampagna),
          'TILED': true,
          'FORMAT': currentLayer.mapProperty.get(PropertyLayer.FORMATO_IMMAGINE) !== undefined
            ? currentLayer.mapProperty.get(PropertyLayer.FORMATO_IMMAGINE)
            : PropertyLayer.FORMATO_IMAGE_DEFAULT
        },
        serverType: 'geoserver',
        transition: 0,
        imageLoadFunction: this.loadFunction
      })
    });

    this.addPropertiesWMS(currentLayer, imageLayer);

    return imageLayer;
  }

  private addPropertiesWMS(currentLayer: LayerConfig, tileLayer: any) {
    if (currentLayer.mapProperty.get(PropertyLayer.MAX_SCALE)) {
      tileLayer.set('maxResolution',
        MapService.getResolutionForScale(this.map, Number(currentLayer.mapProperty.get(PropertyLayer.MAX_SCALE))));
    }

    if (currentLayer.mapProperty.get(PropertyLayer.MIN_SCALE)) {
      tileLayer.set('minResolution',
        MapService.getResolutionForScale(this.map, Number(currentLayer.mapProperty.get(PropertyLayer.MIN_SCALE))));
    }

    this.mapService.applyConfigFilter(currentLayer, tileLayer);

    if (currentLayer.mapProperty.get(PropertyLayer.STYLE)) {
      const params = tileLayer.getSource().getParams();
      params.STYLES = currentLayer.mapProperty.get(PropertyLayer.STYLE);
      tileLayer.getSource().updateParams(params);
    }
    this.setLayerProperties(tileLayer, currentLayer);
    tileLayer.set(PropertyLayer.NOSWITCHERDELETE, true);
  }

  addVectorLayer(currentLayer: LayerConfig): any {
    let vectorLayer;

    vectorLayer = new VectorLayer({
      source: new VectorSource(),
      renderMode: 'vector'
    });

    this.setLayerProperties(vectorLayer, currentLayer);

    vectorLayer.set(PropertyLayer.CAMPOFILTRO, currentLayer.mapProperty.get(PropertyLayer.CAMPOFILTRO));
    vectorLayer.set(PropertyLayer.VALOREFILTRO, null);
    vectorLayer.set(PropertyLayer.CONTESTO, currentLayer.mapProperty.get(PropertyLayer.CONTESTO));
    vectorLayer.set(PropertyLayer.NOSWITCHERDELETE, true);

    if (currentLayer.mapProperty.get(PropertyLayer.EDITABILE) &&
      currentLayer.mapProperty.get(PropertyLayer.EDITABILE) === PropertyLayer.SI) {
      vectorLayer.setStyle((function () {
        const style = new Style({
          stroke: new Stroke({
            color: 'rgba(0,255,255,1)',
            width: 2,
          }),
          fill: new Fill({
            color: 'rgba(0,255,255,0)',
          }),
          text: new Text({
            text: '',
            scale: 1.3,
            fill: new Fill({
              color: '#0b2608'
            }),
            backgroundFill: new Fill({
              color: 'rgba(0,255,255,1)'
            })
          }),
        });
        const styles = [style];
        return function (feature, resolution) {
          style.getText().setText(
            feature.get('STATO_COLT') != null &&
              feature.get('STATO_COLT') !== undefined &&
              feature.get('STATO_COLT') !== 0 &&
              feature.get('STATO_COLT') !== '0' ? feature.get('COD_USO_SUOLO') + ' - ' + feature.get('STATO_COLT') : feature.get('COD_USO_SUOLO'));
          return style;
        };
      })());
    } else {
      let coloreBordo = 'rgb(105,105,105,1)';
      let spessoreBordo = 2.2;
      if (currentLayer.mapProperty.get(PropertyLayer.TEMATISMOBORDOCOLORE)) {
        coloreBordo = currentLayer.mapProperty.get(PropertyLayer.TEMATISMOBORDOCOLORE);
      }

      if (currentLayer.mapProperty.get(PropertyLayer.TEMATISMOBORDOSPESSORE)) {
        spessoreBordo = Number(currentLayer.mapProperty.get(PropertyLayer.TEMATISMOBORDOSPESSORE));
      }

      let coloreFill = 'rgb(0,0,0,0)';
      if (currentLayer.mapProperty.get(PropertyLayer.TEMATISMOFILLCOLORE)) {
        coloreFill = currentLayer.mapProperty.get(PropertyLayer.TEMATISMOFILLCOLORE);
      }
      const campoLabel = currentLayer.mapProperty.get(PropertyLayer.TEMATISMOLABEL);

      let width = 3;
      if (currentLayer.mapProperty.get(PropertyLayer.TEMATISMOEVIDENTE)) {
        const width_string = currentLayer.mapProperty.get(PropertyLayer.TEMATISMOEVIDENTE);
        width = Number(width_string);
      }

      try {
        const styles = this.gisStyles.getGenericStyle(coloreFill, coloreBordo, spessoreBordo, width);

        if (currentLayer.mapProperty.get(PropertyLayer.WFS_STYLE)) {
          // anomalie
          this.gisStyles.setWFSCusyomSTYLE(styles, currentLayer, vectorLayer, campoLabel);
        } else {
          vectorLayer.setStyle((function () {
            return function (feature, resolution) {
              const geom_name = feature.getGeometry().getType();
              const style = styles[geom_name];
              if (campoLabel) {
                style.getText().setText(feature.get(campoLabel));
              }
              return style;
            };
          })());
        }

      } catch (error) {
        console.log('error in style ', error);
      }
    }
    return vectorLayer;
  }

  setIdentifyProperty(listaLayers) {
    this.layerGisService.setIdentifyProperty(listaLayers);
  }

  private loadFunction = (tile, src) => {
    this
      .http
      .get(src, {
        headers: {
          skipLoader: 'true',
          skipErrors: 'true'
        },
        responseType: 'blob'
      })
      .subscribe(blob => {
        const url = URL.createObjectURL(blob);
        const image = tile.getImage();

        image.src = url;
      });
  }
}
