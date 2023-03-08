import { enumTool } from './../../enumTools';
import { MapService } from './../../../map.service';
import { ToolBarService } from 'src/app/gis/shared/ToolBar/toolBar.service';
import { GisCostants } from './../../../../../shared/gis.constants';
import { CaricaVettorialiCostants } from './../carica-vettorialicostants';
import { LayerGisService } from 'src/app/gis/services/layer-gis.service';
import TileLayer from 'ol/layer/Tile';
import VectorLayer from 'ol/layer/Vector';
import { MapEvent } from './../../../../../shared/MapEvent';
import VectorSource from 'ol/source/Vector';
import { GeoJSON} from 'ol/format';
import Feature from 'ol/Feature';
import LayerGroup from 'ol/layer/Group';
import { PanelEvent } from './../../../../../shared/PanelEvent';
import { Component, ElementRef, OnInit, ViewChild, ViewEncapsulation } from '@angular/core';
import { PropertyLayer } from '../../../../../shared/PropertyLayer.enum';
import { LayerConfig } from '../../../../../shared/LayerConfig';
import * as cloneDeep from 'lodash/cloneDeep';

@Component({
  selector: 'gis-modal-carica-vettoriali',
  templateUrl: './modal-carica-vettoriali.component.html',
  styleUrls: ['./modal-carica-vettoriali.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class ModalCaricaVettorialiComponent implements OnInit {
  @ViewChild('layerSwitcher') layerSwitcher: ElementRef;
  uploadedFiles: any[] = [];
  featureJson: any;
  fileName = '';
  currentLayer: LayerConfig;
  layerCaricati: any;
  featureFile: any;
  errorFile: string;
  formatError: boolean;
  vectorSource: VectorSource;
  copyLayers: any[];
  copyLayersCaricati: any[];
  errorProjection = false;
  idLayer = 0; // usato per codice

  constructor(private mapEvent: MapEvent, public panelEvent: PanelEvent, private layerGisService: LayerGisService,
    private caricaVettorialiCostants: CaricaVettorialiCostants, private gisCostants: GisCostants,
    private toolbarService: ToolBarService, private mapService: MapService) {
      this.fileName = this.caricaVettorialiCostants.NoFileMsg;
      this.errorFile = this.caricaVettorialiCostants.ErrFileExt;
    }

  ngOnInit() {
    const self = this;
    self.mapEvent.checkLayerCaricati = [];
    this.checkLayers();
  }

  checkLayers() {
    const self = this;
    return new Promise(((resolve, reject) => {
      try {
        this.mapEvent.map.getLayers().forEach(function (el) {
          if (el instanceof LayerGroup) {
            const groupLayer = el.get('layers').getArray();
              for (let i = 0; i < groupLayer.length; i++) {
                if (el.get('codice')  && el.get('codice').search('LayerCaricati') >= 0) {
                  self.mapEvent.checkLayerCaricati.push(el);
                  console.log('Aggiunto a checkLayerCaricati', self.mapEvent.checkLayerCaricati);
                  let resolveObj = {'layer': self.mapEvent.checkLayerCaricati, 'length': self.mapEvent.checkLayerCaricati.length};
                  resolve(resolveObj);
                  return;
                } else {
                  console.log('checkLayerCaricatiClean');
                  self.mapEvent.checkLayerCaricati = [];
                }
              }
          }
        });
        let resolveObj = {'layer': self.mapEvent.checkLayerCaricati, 'length': self.mapEvent.checkLayerCaricati.length};
        resolve(resolveObj);
      } catch (error) {
        reject(error);
      }
    }));
  }

  checkFileExt(file) {
    this.fileName = file.name.split('.')[0];
    let ext = file.name.split('.').at(-1);
    if (ext === 'geojson' ||
        ext === 'json') {
          this.formatError = false;
          return true;
    } else {
      this.formatError = true;
      return false;
    }
  }

  onLoadFile(file) {
    if (this.checkFileExt(file[0])) {
      this.featureFile = file;
    }
  }

  onUpload(event) {
    if (this.checkFileExt(this.featureFile[0])) {
      this.readJSON(this.featureFile);
      event.stopPropagation();
    }
  }

  readJSON(file) {
      const self = this;
      this.parseJsonFile(file).then((vectorSource: VectorSource) => {
        const newId = this.idLayer;
        self.idLayer += 1;

        vectorSource.addFeature(new Feature());
        // console.log( self.mapEvent.map.interactions.getArray())
        this.checkLayers().then(layerCaricati => {
          const vectorLayer = new VectorLayer({
            source: vectorSource,
            projection: this.gisCostants.defaultProjection,
            style: function (feature) {
              return self.caricaVettorialiCostants.styleArray
              [newId % self.caricaVettorialiCostants.styleArray.length][feature.getGeometry().getType()];
            }
          });
          if (layerCaricati['length'] === 0) {
            self.setCaricatiLayerProperties(vectorLayer, self.fileName, newId).then((layer: VectorLayer) => {
              self.layerCaricati = new LayerGroup({
                layers: [layer]
              });
              self.layerCaricati.set(PropertyLayer.TITLE, 'Layer Caricati');
              self.layerCaricati.set(PropertyLayer.CODICE, 'LayerCaricati');
              self.mapEvent.map.addLayer(this.layerCaricati);
              self.mapEvent.checkLayerCaricati.push(this.layerCaricati);
              console.log('Aggiunto a checkLayerCaricati', self.mapEvent.checkLayerCaricati);
              self.aggiungiConfigurazioni(layer);
            });
          } else {
            self.setCaricatiLayerProperties(vectorLayer, self.fileName, newId).then((layer: VectorLayer) => {
              self.mapEvent.map.getLayers().forEach(function (el) {
                if (el instanceof LayerGroup) {
                      if (el.get(PropertyLayer.CODICE) === 'LayerCaricati') {
                        layer.set(PropertyLayer.TITLE, layer.get(PropertyLayer.TITLE));
                        el.getLayers().push(layer);
                        self.aggiungiConfigurazioni(layer);
                        return;
                      }
                    }
              });
            });
          }
        });
        /*
        if (self.mapEvent.checkLayerCaricati[0]) {
          // BI a cosa serve???????
          let index = self.mapEvent.checkLayerCaricati[0].values_.layers.array_.length;
          if (index <= 5) {
            self.copyLayers = cloneDeep(self.mapEvent.checkLayerCaricati);
            self.copyLayersCaricati = self.copyLayers[self.copyLayers.length - 1].getLayers().getArray();
          } else if (index >= 5 && self.copyLayersCaricati.length >= 5) {
            self.copyLayersCaricati = [];
          }
        }*/
    });
      this.closeDialog();
  }

  private aggiungiConfigurazioni(layer: VectorLayer) {
    this.layerGisService.addLayerCaricatiToIdentify(layer);
    this.toolbarService.addSnapInteraction(layer, this.mapEvent.map);
  }

  parseJsonFile(file) {
    return new Promise(((resolve, reject) => {
      try {
        const reader = new FileReader();
        reader.onloadend = (e) => {
          this.featureJson = JSON.parse(reader.result.toString());
          if (this.featureJson.crs.properties.name.split('::').pop() === this.gisCostants.defaultProjection.split(':').pop()) {
            this.errorProjection = false;
          const vectorSource = new VectorSource({
            wrapX: false,
            features: new GeoJSON().readFeatures(this.featureJson, {
              projection: this.gisCostants.defaultProjection
            }),
          });
          resolve(vectorSource);
        } else {
          this.errorProjection = true;
          return;
        }
        };
        reader.readAsText(file[0]);
      } catch (error) {
        reject(error);
      }
    }));
  }

  setCaricatiLayerProperties(layer: TileLayer | VectorLayer, title, newId) {
    return new Promise(((resolve, reject) => {
      try {
        this.layerGisService.layerCaricatiProperties(layer, title, newId).then(value => {
          resolve(value);
        });
      } catch (error) {
        reject(error);
      }
    }));
  }

  closeDialog() {
   this.panelEvent.showUploadVettoriali = false;
  }

}
