import { MisuraUtils } from './../misura.utils';
import { MapEvent } from './../../../../shared/MapEvent';
import { Component, OnInit } from '@angular/core';
import { MapService } from '../../map.service';
import { ToolBarService } from '../../../../shared/ToolBar/toolBar.service';
import { GisTool } from '../gisTool';
import { enumTool } from '../enumTools';
import { PropertyLayer } from '../../../../shared/PropertyLayer.enum';
import { Draw } from 'ol/interaction';
import { Vector as VectorSource } from 'ol/source';
import { Tile as TileLayer, Vector as VectorLayer } from 'ol/layer';
import Style from 'ol/style/Style';


@Component({
  selector: 'gis-misura',
  templateUrl: './misura.component.html',
  styleUrls: ['./misura.component.css']
})
export class MisuraComponent extends GisTool implements OnInit {
  draw: Draw;
  typeSelect: HTMLElement;
  source: VectorSource;
  showSegments: boolean;
  clearPrevious: boolean;
  selectType: string;
  unitLine: string;
  unitPolygon: string;
  showMisuraPanel = false;
  style: Style;
  misuraVectorLayer: VectorLayer;

  constructor(private mapService: MapService, public mapEvent: MapEvent, private misuraUtils: MisuraUtils,
    private toolBarService1: ToolBarService) {
    super([enumTool.misuraEl], null, false, true, true, toolBarService1.editToolGroup, toolBarService1);
  }

  ngOnInit() {
    super.ngOnInit();
    const self = this;
    this.style = this.misuraUtils.style;
    this.typeSelect = document.getElementById('type');
    this.showSegments = true;
    this.clearPrevious = true;
    this.unitLine = this.misuraUtils.unitLine;
    this.unitPolygon = this.misuraUtils.unitPolygon;
    this.selectType = 'Polygon';
    this.source = new VectorSource();

    this.typeSelect.onchange = function () {
      self.mapEvent.map.removeInteraction(self.draw);
      self.reloadTool();
    };
    document.addEventListener('keydown', ({ key }) => {
      if (key === 'Escape') {
        this.checkVector(this.misuraVectorLayer);
      }
  });

    this.reloadTool();
  }

  onChangeUnit() {
    this.misuraUtils.unitLine = this.unitLine;
    this.misuraUtils.unitPolygon = this.unitPolygon;
    this.toolBarService1.refreshStyle();
  }

  onChangeTipo(tipo: string) {
    if (tipo === 'Polygon') {
      this.unitLine = 'm';
    }
    this.misuraUtils.unitPolygon = this.unitPolygon;
    this.toolBarService1.refreshStyle();
    //this.reloadTool();
  }

  declareTool(): any[] {
    const self = this;
    const drawType = this.typeSelect['value'];
    const activeTip = (drawType === 'Polygon' ? 'Area' : 'Linea');
    const idleTip = '';
    let tip = idleTip;
    if (this.showMisuraPanel) {
      this.draw = new Draw({
        source: this.source,
        type: drawType,
        style: function (feature) {
          return self.misuraUtils.styleFunction(feature, self.showSegments, drawType, true,  [self.style], null, false);
        },
        condition: function (e) {
          return self.toolBarService1.conditionClick(e);
        }
      });
      this.draw.on('drawstart', function () {
        self.checkVector(self.misuraVectorLayer);
        if (self.clearPrevious) {
          self.source.clear();
        }
        tip = activeTip;
      });
      this.draw.on('drawend', async function (event) {
        self.misuraVectorLayer = new VectorLayer({
          source: new VectorSource(),
          features: event.feature,
          displayInLayerSwitcher : false,
          style: function (feature) {
            return self.misuraUtils.styleFunction(feature, self.showSegments, drawType, true,  [self.style], null, false);
          },
        });
        self.checkVector(self.misuraVectorLayer).then(found => {
          if (!found) {
            self.addVector(event);
            return;
          }
        });

        self.mapEvent.map.once('pointermove', function () {
        });
        tip = idleTip;
      });
      return [this.draw];
    }
  }

  // controllo se esiste già un layer di tipo MISURA_LAYER
  checkVector(vector: VectorLayer) {
    return new Promise(((resolve, reject) => {
      try {
        if (vector && vector.get(PropertyLayer.CODICE) === 'MISURA_LAYER' ) {
          this.toolBarService1.removeLayerMisura();
          resolve(true);
        } else {
          resolve(false);
        }
      } catch (error) {
        reject(error);
      }
    }));
  }

  // aggiungo il layer
  addVector(event: Draw) {
    this.misuraVectorLayer.set(PropertyLayer.CODICE, 'MISURA_LAYER');
    this.misuraVectorLayer.set(PropertyLayer.DISPLAYINLAYERSWITCHER, false);
    this.misuraVectorLayer.getSource().addFeature(event.feature);
    this.mapEvent.map.addLayer(this.misuraVectorLayer);
  }

  openToolMisura() {
    this.showMisuraPanel = !this.isActive();
    if (!this.showMisuraPanel) {
      this.checkVector(this.misuraVectorLayer);
    }
    this.misuraUtils.unitLine = this.unitLine;
    this.misuraUtils.unitPolygon = this.unitPolygon;
    this.enableTool();
  }
  get canUseTool(): boolean {
    return this.toolBarService1.canUseTool(this.mainToolName[0], this.mapService.editLayer);
  }
}


