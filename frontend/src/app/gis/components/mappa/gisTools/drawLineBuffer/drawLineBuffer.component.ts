import { MisuraUtils } from './../misura.utils';
import { Component, EventEmitter, OnDestroy, OnInit } from '@angular/core';
import Draw from 'ol/interaction/Draw';
import GeometryType from 'ol/geom/GeometryType';
import { MapService } from '../../map.service';
import { ToolBarService } from '../../../../shared/ToolBar/toolBar.service';
import { GisTool } from '../gisTool';
import { enumTool } from '../enumTools';
import { PropertyLayer } from '../../../../shared/PropertyLayer.enum';
import * as jsts from 'jsts';
import Point from 'ol/geom/Point';
import LineString from 'ol/geom/LineString';
import LinearRing from 'ol/geom/LinearRing';
import Polygon from 'ol/geom/Polygon';
import MultiPoint from 'ol/geom/MultiPoint';
import MultiLineString from 'ol/geom/MultiLineString';
import MultiPolygon from 'ol/geom/MultiPolygon';
import Feature from 'ol/Feature';
import VectorLayer from 'ol/layer/Vector';
import VectorSource from 'ol/source/Vector';
import { MapEvent } from '../../../../shared/MapEvent';
import { GisUtils } from '../gisUtils';
import { enumListener } from '../enumListeners';
import { GisStyles } from '../../../../shared/GisStyles';
import { ToastGisComponent } from '../../../toast-gis/toast-gis.component';
import { GisCostants } from 'src/app/gis/shared/gis.constants';
import { condition } from '../drawUtils';
import { never } from 'ol/events/condition';

@Component({
  selector: 'gis-drawLineBuffer',
  templateUrl: './drawLineBuffer.component.html',
  styleUrls: ['./drawLineBuffer.component.css']
})
export class DrawLineBufferComponent extends GisTool implements OnInit, OnDestroy {
  numInput: number;
  maxSize: number;
  startSize: number;
  minSize: number;
  increment: number;
  drawing = false;
  subscriptionModifyFeature: any;
  currentFeature: any;
  tool: any;

  constructor(private mapService: MapService, public mapEvent: MapEvent,
    private toolBarService1: ToolBarService, private gisStyles: GisStyles, private toastComponent: ToastGisComponent,
    public gisCostants: GisCostants, private misuraUtils: MisuraUtils) {
    super([enumTool.drawBuffer], null, false, true, true, toolBarService1.editToolGroup, toolBarService1);
    this.maxSize = null;
    this.minSize = 0.5;
    this.startSize = 2;
    this.increment = 0.5;
    this.numInput = this.startSize;
  }
  ngOnInit() {
    super.ngOnInit();
  }

  ngOnDestroy() {
    if (this.subscriptionModifyFeature) {
      this.subscriptionModifyFeature.unsubscribe();
    }
  }

  enableTool() {
    this.interrompiDisegno();
    super.enableTool();
  }

  private interrompiDisegno() {
    if (this.drawing) {
      this.drawing = false;
      console.log('Manage undoInteraction');
      const undoInteraction = this.toolBarService1.getInteractionFomName(enumTool.undoRedoInteraction);
      undoInteraction.blockEnd();
      undoInteraction.undo();
      this.tool.abortDrawing();
      undoInteraction._redoStack = [];
    }
  }

  declareTool(): any[] {
    const _self = this;
    _self.drawing = false;
    _self.currentFeature = null;
    let poligonoBuffer: any;

    const parser = GisUtils.getJstsparser();

    const drawLayer = new VectorLayer({
      source: new VectorSource()
    });

    this.tool = new Draw({
      source: drawLayer.getSource(),
      type: GeometryType.LINE_STRING,
      style: this.gisStyles.newPolygonStyle,
      clickTolerance: this.gisCostants.drawClickTolerance,
      snapTolerance: this.gisCostants.endDrawSnapTolerance,
      condition: condition(_self.gisCostants.endDrawCreateVertex),
      finishCondition: never
    });

    if (this.subscriptionModifyFeature) {
      this.subscriptionModifyFeature.unsubscribe();
    }

    const undoInteraction = this.toolBarService1.getInteractionFomName(enumTool.undoRedoInteraction);

    this.subscriptionModifyFeature = this.mapService.getModifyFeatureEmitter()
      .subscribe((tipoOperazione) => {
        if (_self.drawing) {

          if (tipoOperazione === enumListener.undoAction) {
            try {
              // console.log('currentFeatureCoordinates ', _self.currentFeature.getGeometry().getCoordinates().length);
              if (_self.currentFeature.getGeometry().getCoordinates().length > 2) {
                _self.tool.removeLastPoint();
                undoInteraction._redoStack = [];
              } else {
                _self.interrompiDisegno();
              }
            } catch { _self.interrompiDisegno(); }
          }
        }
      });

    let sketch, listener;
    this.tool.on('drawstart', function (evt1) {
      _self.drawing = true;
      undoInteraction.blockStart();
      _self.toolBarService1.exclusiveActiveTool = enumTool.drawBuffer;
      _self.currentFeature = evt1.feature;
      // set sketch
      sketch = evt1.feature;
      poligonoBuffer = null;

      listener = sketch.getGeometry().on('change', function (evt) {
        const geom = evt.target;
        if (geom) {
          if (poligonoBuffer) {
            try {
              _self.mapService.editLayer.getSource().removeFeature(poligonoBuffer);
            } catch { }
          }

          const jstsLine = parser.read(geom);
          // crea buffer
          const buffered = jstsLine.buffer(_self.numInput);
          poligonoBuffer = new Feature(parser.write(buffered));
          poligonoBuffer.setStyle(function (feature) {
            return _self.misuraUtils.styleFunction(feature, true, GeometryType.POLYGON,
            _self.toolBarService1.misuraStyle, _self.gisStyles.newPolygonStyle, (_self.numInput / 4));
          });
          _self.mapService.editLayer.getSource().addFeature(poligonoBuffer);
        }
      });
    });

    this.tool.on('drawend', async function (event) {
      _self.drawing = false;
      try {
        // rimuovo la strada precedente (inserito per fare undo)
        if (poligonoBuffer) {
          try {
            _self.mapService.editLayer.getSource().removeFeature(poligonoBuffer);
          } catch { }
        }

        const featureLine = event.feature;
        const jstsLine = parser.read(featureLine.getGeometry());

        // crea buffer
        const buffered = jstsLine.buffer(_self.numInput);
        poligonoBuffer = new Feature(parser.write(buffered));

        const listToAdd: Feature[] = [];
        const listToRemove: Feature[] = [];

        const operationOk = GisUtils.carotaggioOnInput(poligonoBuffer, _self.mapService, listToAdd, listToRemove, _self.toastComponent);

        if (operationOk !== true) {
          _self.toastComponent.showErrorGenerico('Errore elaborazione, verificare la validità dei poligoni di partenza. Verrà mantenuto il poligoni disegnato, ma l\'intersezione potrebbe essere parziale.');
        }
        await _self.saveChanges(_self, poligonoBuffer, undoInteraction, listToAdd, listToRemove);
      } catch (e) {
        console.log(e);
        _self.toastComponent.showErrorGenerico('Errore elaborazione, verificare la validità dei poligoni di partenza.');
        _self.saveChanges(_self, poligonoBuffer, undoInteraction, [], []);
      }
    });

    return [this.tool];
  }

  private async saveChanges(_self: this, poligonoBuffer: any, undoInteraction: any, listToAdd: Feature[], listToRemove: Feature[]) {
    try {
      _self.mapService.editLayer.getSource().addFeature(poligonoBuffer);
      listToAdd.forEach(element => {
        _self.mapService.editLayer.getSource().addFeature(element);
      });

      listToRemove.forEach(element => {
        _self.mapService.editLayer.getSource().removeFeature(element);
      });

      if (await _self.mapService.newFeaturePromise(_self.mapService, poligonoBuffer)) {
        _self.mapService.modifyFeatureEmitter.emit(_self.mapService.editLayer.get(PropertyLayer.CODICE));
        _self.toolBarService1.exclusiveActiveTool = null;
        undoInteraction.blockEnd();
      }
    } catch (error) {
      console.log(error);
      _self.toolBarService1.exclusiveActiveTool = null;
      undoInteraction.blockEnd();
      undoInteraction.undo();
    }
  }

  onChange(newValue) {
    if (this.maxSize && this.numInput > this.maxSize) {
      this.numInput = this.maxSize;
    } else if (!this.numInput) {
      this.numInput = this.minSize;
    }
    this.reloadTool();
  }

  get canUseTool(): boolean {
    return this.toolBarService1.canUseTool(this.mainToolName[0], this.mapService.editLayer);
  }

  setPt(type) {
    if (type === 'increase' && (!this.maxSize || this.numInput < this.maxSize)) {
      this.numInput = Number(this.numInput) + this.increment;
    } else if (type === 'decrease' && this.numInput >= this.minSize) {
      this.numInput = Number(this.numInput) - this.increment;
    }
  }
}
