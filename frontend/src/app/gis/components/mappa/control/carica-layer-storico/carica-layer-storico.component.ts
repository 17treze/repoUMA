import { HttpClient } from '@angular/common/http';
import VectorLayer from 'ol/layer/Vector';
import { TileGrid } from 'ol/tilegrid/TileGrid';
import { LayerConfig } from './../../../../shared/LayerConfig';
import { GisCostants } from './../../../../shared/gis.constants';
import TileWMS from 'ol/source/TileWMS';
import TileLayer from 'ol/layer/Tile';
import { LayerGisService } from 'src/app/gis/services/layer-gis.service';
import { PropertyLayer } from './../../../../shared/PropertyLayer.enum';
import { Layer } from 'ol/layer/Layer';
import { MapEvent } from './../../../../shared/MapEvent';
import { MapService } from './../../map.service';
import { GisUtilsService } from './../../../../shared/gis-utils.service';
import { PanelEvent } from 'src/app/gis/shared/PanelEvent';
import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import LayerGroup from 'ol/layer/Group';
import { LayerAttivi } from '../../../../shared/LayerAttivi';
import * as moment from 'moment';
import { createForProjection } from 'ol/tilegrid';

@Component({
  selector: 'gis-carica-layer-storico',
  templateUrl: './carica-layer-storico.component.html',
  styleUrls: ['./carica-layer-storico.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class CaricaLayerStoricoComponent implements OnInit {
  layerStorico: Layer;
  listaLayerStorico: Array<object> = [];
  campagnaStorico: number;
  dataValidita: Date;
  listaCampagna: Array<any>;
  layersSwitcher: never[];
  dataFiltro; any;
  minDate: Date;
  constructor(public panelEvent: PanelEvent, private gisUtils: GisUtilsService, private mapService: MapService,
    public mapEvent: MapEvent, public layerAttivi: LayerAttivi,
    private layerGisService: LayerGisService, private gisConstants: GisCostants, private http: HttpClient,

  ) { }

  ngOnInit() {
    this.panelEvent.showCaricaLayerStorico = false;
    this.listaCampagna = this.gisUtils.getComboAnniCampagna(false);
    this.minDate = this.minDate = new Date('December 31,' + 2021);
  }

  openCaricaLayerStorico() {
    this.panelEvent.showCaricaLayerStorico = !this.panelEvent.showCaricaLayerStorico;
    if (this.listaLayerStorico.length === 0) {
      this.loadLayerWithStorico();
    }
  }

  loadLayerWithStorico() {
    const layers = this.mapEvent.map.getLayers().getArray();
    layers.forEach((layer: Layer) => {
      if (layer instanceof LayerGroup) {
        const subLayers = layer.getLayers().getArray();
        subLayers.forEach((l: Layer) => {
          if (l.get(PropertyLayer.CARICAMENTO_STORICO)) {
            this.listaLayerStorico.push(l);
          }
        });
      } else {
        if (layer.get(PropertyLayer.CARICAMENTO_STORICO)) {
          this.listaLayerStorico.push(layer);
        }
      }
    });

  }

  caricaLayerStorico() {
    const self = this;
    let layer;
    const bbox = this.mapEvent.map.values_.view.projection_.extent_.join(",") + ',' + this.mapEvent.map.getView().getProjection().getCode();
    // Recupero layer da layerConfig
    this.mapService.layerConfig.forEach((element: Layer) => {
      if (this.layerStorico.get(PropertyLayer.CODICE) === element.codice) {
        layer = element;
      }
    });

    const cqlFilter = this.setCqlFilter();
    const params = this.setParamsWms(bbox, cqlFilter, layer);
    const url = this.gisConstants.hostRichiesta + layer.url + layer.workspace + '/' + layer.mapProperty.get(PropertyLayer.FORMATO);

    const tileLayer = new TileLayer({
      source: new TileWMS({
        projection: this.gisConstants.defaultProjection,
        tileGrid: this.getTileGrid(layer),
        url: url,
        params: params,
        transition: 0,
        attributions: layer.mapProperty.get(PropertyLayer.ATTRIBUTION),
        tileLoadFunction: this.loadFunction
      })
    });

    if (this.dataValidita) {
      const data = moment((new Date(this.dataValidita).toISOString()));
      this.dataFiltro = moment(data).format('DD/MM/YYYY');
    } else {
      this.dataFiltro = '';
    }
    this.setLayerProperties(layer, tileLayer);
    tileLayer.set(PropertyLayer.DISPLAYINLAYERSWITCHER, true);
    tileLayer.set(PropertyLayer.TITLE, layer.mapProperty.get(PropertyLayer.TITOLO) +
      '<span class="campagna-ls">' + this.campagnaStorico + '</span>' + this.dataFiltro);
    this.panelEvent.labelStoricoIdentify = ' ' + this.campagnaStorico + ' ' + this.dataFiltro;
    tileLayer.set(PropertyLayer.CODICE, PropertyLayer.CODICE_LAYER_SUOLO_STORICO);
    tileLayer.setVisible(true);
    this.addToLayerGroup(tileLayer);
    this.layerGisService.addLayerCaricatiToIdentify(tileLayer);
  }

  setParamsWms(bbox, cqlFilter, layer) {
    const params: any = {};
    params.SERVICE = layer.mapProperty.get(PropertyLayer.FORMATO);
    params.LAYERS = layer.getNomeLayer(this.mapService.annoCampagna);
    params.TILED = true;
    params.VERSION = '1.3.0';
    params.REQUEST = 'GetMap';
    params.TRANSPARENT = true;
    params.WIDTH = '512';
    params.HEIGHT = '512';
    params.CRS = this.layerStorico;
    params.BBOX = bbox;
    params.CQL_FILTER = cqlFilter;
    params.FORMAT = layer.mapProperty.get(PropertyLayer.FORMATO_IMMAGINE) !== undefined
      ? layer.mapProperty.get(PropertyLayer.FORMATO_IMMAGINE)
      : PropertyLayer.FORMATO_IMAGE_DEFAULT;
    // '&FORMAT=image/png8';
    return params;
  }

  addToLayerGroup(tileLayer) {
    this.mapEvent.map.getLayers().getArray().forEach(l => {
      if (l instanceof LayerGroup && l.get(PropertyLayer.TITLE) === tileLayer.get(PropertyLayer.GRUPPO)) {
        const gruppoSuolo = l.getLayers().getArray();
        const layerStorico = [];
        l.set(PropertyLayer.OPENINLAYERSWITCHER, false);
        for (let i = 0; i < gruppoSuolo.length; i++) {
          if (gruppoSuolo[i].get(PropertyLayer.CODICE) === tileLayer.get(PropertyLayer.CODICE)) {
            layerStorico.push(gruppoSuolo[i]);
          }
        }
        if (layerStorico.length === 0) {
          l.getLayers().push(tileLayer);
        } else {
          const index = gruppoSuolo.indexOf(layerStorico[0]);
          l.getLayers().getArray().splice(index, 1);
          l.getLayers().push(tileLayer);
          l.set(PropertyLayer.OPENINLAYERSWITCHER, true);
        }
      }
    });
  }

  getTileGrid(layer: LayerConfig): TileGrid | undefined {
    const tileSize = layer.mapProperty.get(PropertyLayer.TILE_SIZE);
    if (!tileSize) {
      return undefined;
    }
    const projection = this.gisConstants.defaultProjection;
    const grid: TileGrid = createForProjection(projection, undefined, tileSize, undefined);
    return grid;
  }

  setLayerProperties(layer: TileLayer | VectorLayer, currentLayer: LayerConfig) {
    this.layerGisService.layerProperties(currentLayer, layer);
  }

  setCqlFilter() {
    let cqlFilter = '';
    if (this.campagnaStorico) {
      cqlFilter = 'CAMPAGNA = ' + this.campagnaStorico;
    }
    if (this.dataValidita) {
      const data = this.dataValidita.toISOString();
      const and = this.campagnaStorico ? ' AND' : '';
      const queryDataValidita = and + ' DATA_FINE_VAL AFTER ' + data + ' AND DATA_INIZIO_VAL BEFORE ' + data;
      cqlFilter = cqlFilter + queryDataValidita;
    } else {
      const sysDate = new Date().toISOString();
      const queryDataVuota = ' AND DATA_FINE_VAL AFTER ' + sysDate + ' AND DATA_INIZIO_VAL BEFORE ' + sysDate;
      cqlFilter = cqlFilter + queryDataVuota;
    }
    return cqlFilter;
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
