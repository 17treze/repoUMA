import { Component, ComponentFactoryResolver, OnDestroy, OnInit } from '@angular/core';
import GeometryType from 'ol/geom/GeometryType';
import { MapService } from '../../map.service';
import { ToolBarService } from '../../../../shared/ToolBar/toolBar.service';
import { GisTool } from '../gisTool';
import { enumTool } from '../enumTools';
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
import * as jsts from 'jsts';
import Draw from 'ol/interaction/Draw';
import { GisUtils } from '../gisUtils';
import { PropertyLayer } from '../../../../shared/PropertyLayer.enum';
import { GisStyles } from '../../../../shared/GisStyles';
import { ToastGisComponent } from '../../../toast-gis/toast-gis.component';
import { intersects } from 'ol/extent';
import { GisCostants } from 'src/app/gis/shared/gis.constants';
import { condition } from '../drawUtils';
import { never } from 'ol/events/condition';

@Component({
  selector: 'gis-lineSplit',
  templateUrl: './lineSplit.component.html',
  styleUrls: ['./lineSplit.component.css']
})
export class LineSplitComponent extends GisTool {

  private selectedToolName: enumTool;

  constructor(private mapService: MapService, private toolBarService1: ToolBarService,
    private toastComponent: ToastGisComponent, private gisStyles: GisStyles, public gisCostants: GisCostants) {
    super([enumTool.lineSplit, enumTool.polySplit], null, true, true, true, toolBarService1.editToolGroup, toolBarService1);
  }

  public get canUseTool(): boolean {
    return this.toolBarService1.canUseTool(this.mainToolName[0], this.mapService.editLayer);
  }

  enableToolLineSplit() {
    console.log('enableToolLineSplit');
    this.selectedToolName = enumTool.lineSplit;
    super.enableTool(this.selectedToolName);
  }

  enableToolPolySplit() {
    console.log('enableToolPolySplit');
    this.selectedToolName = enumTool.polySplit;
    super.enableTool(this.selectedToolName);
  }

  declareTool(): any[] {
    const self = this;
    const drawLayer = new VectorLayer({
      source: new VectorSource()
    });

    const _self = this;

    let tool: Draw;

    if (this.selectedToolName === enumTool.polySplit) {
      tool = new Draw({
        source: drawLayer.getSource(),
        type: GeometryType.POLYGON,
        style: this.gisStyles.newPolygonStyle,
        clickTolerance: this.gisCostants.drawClickTolerance,
        snapTolerance: this.gisCostants.endDrawSnapTolerance,
        condition: condition(_self.gisCostants.endDrawCreateVertex),
        finishCondition: never

      });
    } else {
      tool = new Draw({
        source: drawLayer.getSource(),
        type: GeometryType.LINE_STRING,
        style: this.gisStyles.newPolygonStyle,
        clickTolerance: this.gisCostants.drawClickTolerance,
        snapTolerance: this.gisCostants.endDrawSnapTolerance,
        condition: condition(_self.gisCostants.endDrawCreateVertex),
        finishCondition: never
      });
    }

    ToolBarService.setInteractionProperties(tool, this.selectedToolName);
    const toolName = this.selectedToolName;

    const editLayer = this.mapService.editLayer;

    const undoInteraction = this.toolBarService1.getInteractionFomName(enumTool.undoRedoInteraction);

    tool.on('drawend', async function (event) {
      undoInteraction.blockStart();
      let errorCount = 0;
      try {
        console.log('drawend', event.feature);

        const feature = event.feature;
        drawLayer.getSource().clear();

        if (
          feature &&
          feature.getGeometry()) {

          const featureLine = feature;

          const parser = GisUtils.getJstsparser();

          // Parse Polygon and Line geomtry to jsts type
          const jstsLine = parser.read(featureLine.getGeometry());
          let spltiDone = false;
          let i = 0;

          const extentLine = featureLine.getGeometry().getExtent();

          // console.log('selectedFeatures', editLayer.getSource().getFeatures().length, editLayer.getSource().getFeatures());

          for (const featurePoly of editLayer.getSource().getFeatures()) {
            if (!intersects(extentLine, featurePoly.getGeometry().getExtent())) {
              continue;
            }

            i++;
            console.log('Try split el ', featurePoly);
            if (featurePoly.getGeometry()) {

              const jstsPoly = parser.read(featurePoly.getGeometry());

              if (!jstsLine.intersects(jstsPoly)) {
                console.log('no split el ', featurePoly);
                continue;
              }

              // console.log('Try split 2 el ', i);

              let cutElem = jstsLine;
              if (toolName === enumTool.polySplit) {
                cutElem = jstsLine.getBoundary();
              }

              const union = jstsPoly.getBoundary().union(cutElem);
              const polygonizer = new jsts.operation.polygonize.Polygonizer();
              polygonizer.add(union);

              const polygons = polygonizer.getPolygons();

              const splitted_polygon_parts = [];

              for (const polygon of polygons.array) {
                if (jstsPoly.contains(polygon.getInteriorPoint())) {
                  splitted_polygon_parts.push(parser.write(polygon));
                }
              }

              let jstsFeatureOri = parser.read(featurePoly.getGeometry());

              if (splitted_polygon_parts.length > 1) {
                for (const polygon of splitted_polygon_parts) {
                  for (const el of GisUtils.getSinglePolygon(polygon)) {
                    spltiDone = true;
                    const jstsEl = parser.read(el);
                    jstsFeatureOri = jstsFeatureOri.difference(jstsEl);
                    const splitted_polygon = new Feature(el);
                    _self.mapService.setNewFeatureattributesFromFeature(splitted_polygon, featurePoly, editLayer);
                    editLayer.getSource().addFeature(splitted_polygon);
                  }
                }

                const featurePolynew = new Feature(parser.write(jstsFeatureOri));
                const newArea = featurePolynew.getGeometry().getArea();

                if (newArea < 1) {
                  // area < 1
                  editLayer.getSource().removeFeature(featurePoly);
                } else {
                  // area > 1
                  _self.mapService.setNewFeatureattributesFromFeature(featurePolynew, featurePoly, editLayer);
                  editLayer.getSource().addFeature(featurePolynew);
                  editLayer.getSource().removeFeature(featurePoly);
                }
              }
            }
          }
          if (spltiDone) {
            _self.mapService.modifyFeatureEmitter.emit(_self.mapService.editLayer.get(PropertyLayer.CODICE));
          }
        }
      } catch (error) {
        errorCount = errorCount + 1;
        console.log(error);
        _self.toastComponent.showErrorGenerico('Errore nel tool. Verificare la validità delle geometrie di partenza.');
      } finally {
        undoInteraction.blockEnd();
        if (errorCount > 0) {
          undoInteraction.undo();
        }
      }
    }
    );
    return [tool];
  }

  isActiveLine(): boolean {
    return this.toolBarService1.activeTool === enumTool.lineSplit;
  }

  isActivePoly(): boolean {
    return this.toolBarService1.activeTool === enumTool.polySplit;
  }
}
