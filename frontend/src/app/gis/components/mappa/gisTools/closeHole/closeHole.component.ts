import { Component, OnDestroy, OnInit } from '@angular/core';
import GeometryType from 'ol/geom/GeometryType';
import { MapService } from '../../map.service';
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
import { ToolBarService } from '../../../../shared/ToolBar/toolBar.service';
import { GisStyles } from '../../../../shared/GisStyles';
import { GisCostants } from 'src/app/gis/shared/gis.constants';
import { ToastGisComponent } from '../../../toast-gis/toast-gis.component';
import { condition } from '../drawUtils';
import { never } from 'ol/events/condition';

@Component({
  selector: 'gis-closeHole',
  templateUrl: './closeHole.component.html',
  styleUrls: ['./closeHole.component.css']
})
export class CloseHoleComponent extends GisTool implements OnInit {
  private selectedToolName: enumTool;

  constructor(private mapService: MapService, private toolBarService1: ToolBarService, private gisStyles: GisStyles,
    public gisCostants: GisCostants, private toastComponent: ToastGisComponent) {
    super([enumTool.closeHole, enumTool.coverHole], null, true, true, false, toolBarService1.editToolGroup, toolBarService1);
  }
  ngOnInit() {
    super.ngOnInit();
  }

  public get canUseTool(): boolean {
    return this.toolBarService1.canUseTool(this.mainToolName[0], this.mapService.editLayer);
  }

  enableCloseTool() {
    this.selectedToolName = enumTool.closeHole;
    super.enableTool(this.selectedToolName);
  }

  enableCoverTool() {
    this.selectedToolName = enumTool.coverHole;
    super.enableTool(this.selectedToolName);
  }


  declareTool(): any {
    const self = this;
    const select = this.toolBarService1.getInteractionFomName(enumTool.selectFeatures);

    if (!select) {
      self.toastComponent.showErrorGenerico('Selezionare almeno una feature con il tool di select');
        return null;
    } else {
      if (select.getFeatures() && select.getFeatures().getArray()) {
        if (this.selectedToolName === enumTool.closeHole && select.getFeatures().getArray().length !== 1) {
          self.toastComponent.showErrorGenerico('Selezionare una sola feature con il tool di select');
            return null;
        }
        if (this.selectedToolName === enumTool.coverHole && select.getFeatures().getArray().length < 1) {
          self.toastComponent.showErrorGenerico('Selezionare almeno una feature con il tool di select');
            return null;
        }
      }
    }

    const drawLayer = new VectorLayer({
      source: new VectorSource()
    });

    const tool = new Draw({
      source: drawLayer.getSource(),
      type: GeometryType.POLYGON,
      style: this.gisStyles.newPolygonStyle,
      clickTolerance: this.gisCostants.drawClickTolerance,
      snapTolerance: this.gisCostants.endDrawSnapTolerance,
      condition: condition(self.gisCostants.endDrawCreateVertex),
      finishCondition: never
    });

    ToolBarService.setInteractionProperties(tool, this.selectedToolName);
    const toolName = this.selectedToolName;

    const mapService = this.mapService;
    const editLayer = this.mapService.editLayer;

    const undoInteraction = this.toolBarService1.getInteractionFomName(enumTool.undoRedoInteraction);

    tool.on('drawend', function (event) {
      const newFeature = event.feature;
      undoInteraction.blockStart();
      let done = false;
       try {
        console.log('drawend', newFeature);

        const feature = newFeature;
        drawLayer.getSource().clear();

        if (select &&
          select.getFeatures() &&
          select.getFeatures().getArray() &&
          feature &&
          feature.getGeometry()) {

          const selectedFeatures = select.getFeatures().getArray();

          const parser = GisUtils.getJstsparser();

          // if (parser.ol.geom.LineString.length >= 4 || parser.ol.geom.LineString.length === 0 ) {
            const jstsDraw = parser.read(feature.getGeometry());

            if (toolName === enumTool.closeHole) {
              for (const featurePoly of selectedFeatures) {

                if (featurePoly.getGeometry()) {

                  const jstsPoly = parser.read(featurePoly.getGeometry());

                  const polygonizer = new jsts.operation.polygonize.Polygonizer();
                  polygonizer.add(jstsPoly.getExteriorRing());

                  const polygons = polygonizer.getPolygons();

                  const unionGeom = jstsPoly.union(jstsDraw);

                  let intersectGeom = null;

                  for (const polygon of polygons.array) {
                    if (intersectGeom) {
                      intersectGeom = unionGeom.intersection(intersectGeom);
                    } else {
                      intersectGeom = unionGeom.intersection(polygon);
                    }
                  }

                  for (const el of GisUtils.getSinglePolygon(parser.write(intersectGeom))) {
                    const splitted_polygon = new Feature(el);
                    mapService.setNewFeatureattributesFromFeature(splitted_polygon, featurePoly, editLayer);
                    editLayer.getSource().addFeature(splitted_polygon);
                    done = true;
                  }
                  if (done) {
                    select.getFeatures().remove(featurePoly);
                    editLayer.getSource().removeFeature(featurePoly);
                  }
                }
              }
            }

            if (toolName === enumTool.coverHole) {
              let res;

              for (const featurePoly of selectedFeatures) {
                if (res) {
                  const jstsPoly = parser.read(featurePoly.getGeometry());
                  res = jstsPoly.getBoundary().union(res);
                } else {
                  res = parser.read(featurePoly.getGeometry()).getBoundary();
                }
              }

              const polygonizer = new jsts.operation.polygonize.Polygonizer();
              polygonizer.add(res.union(jstsDraw.getBoundary()));

              const polygons = polygonizer.getPolygons();

              const splitted_polygon_parts = [];

              for (const polygon of polygons.array) {
                let intersects = false;
                for (const featurePoly of selectedFeatures) {
                  const jstsPoly = parser.read(featurePoly.getGeometry());
                  if (jstsPoly.contains(polygon.getInteriorPoint())) {
                    intersects = true;
                    break;
                  }
                }

                if (!intersects && jstsDraw.contains(polygon.getInteriorPoint())) {
                  splitted_polygon_parts.push(polygon);
                }
              }

              for (const polygon of splitted_polygon_parts) {
                for (const el of GisUtils.getSinglePolygon(parser.write(polygon))) {
                  const splitted_polygon = new Feature(el);
                  done = true;
                  mapService.setNewFeatureattributes(splitted_polygon, editLayer);
                  editLayer.getSource().addFeature(splitted_polygon);
                }
              }
            }
            if (done) {
              mapService.modifyFeatureEmitter.emit(mapService.editLayer.get(PropertyLayer.CODICE));
            }
          // }
        }
      } catch (error) {
        console.log(error);
      } finally {
        undoInteraction.blockEnd();
        setTimeout(function() {
          if (toolName === enumTool.closeHole) {
            self.enableCloseTool();
          } else if (toolName === enumTool.coverHole) {
            self.enableCoverTool();
          }
        }, 300);
      }
    }
    );
    return [tool];
  }

  isActiveClose(): boolean {
    return this.toolBarService1.activeTool === enumTool.closeHole;
  }

  isActiveCover(): boolean {
    return this.toolBarService1.activeTool === enumTool.coverHole;
  }
}
