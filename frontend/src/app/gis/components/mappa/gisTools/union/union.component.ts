import { Component, OnInit } from '@angular/core';
import { MapService } from '../../map.service';
import { ToolBarService } from '../../../../shared/ToolBar/toolBar.service';
import { enumTool } from '../enumTools';
import { GisButton } from '../gisButton';
import * as jsts from 'jsts';
import Point from 'ol/geom/Point';
import LineString from 'ol/geom/LineString';
import LinearRing from 'ol/geom/LinearRing';
import Polygon from 'ol/geom/Polygon';
import MultiPoint from 'ol/geom/MultiPoint';
import MultiLineString from 'ol/geom/MultiLineString';
import MultiPolygon from 'ol/geom/MultiPolygon';
import Feature from 'ol/Feature';
import { callbackify } from 'util';
import { GisUtils } from '../gisUtils';
import { PropertyLayer } from '../../../../shared/PropertyLayer.enum';
import { ToastGisComponent } from '../../../toast-gis/toast-gis.component';

@Component({
  selector: 'gis-union',
  templateUrl: './union.component.html',
  styleUrls: ['./union.component.css']
})
export class UnionComponent extends GisButton implements OnInit {
  selectedButton: enumTool;

  constructor(private mapService: MapService, private toolBarService1: ToolBarService, private toastComponent: ToastGisComponent) {
    super(enumTool.unionFeatures);
  }

  ngOnInit() {
  }

  get canUse(): boolean {
    return this.toolBarService1.canUseTool(this.mainName, this.mapService.editLayer);
  }

  clickIntersect() {
    this.selectedButton = enumTool.intersectFeatures;
    this.click();
  }

  clickUnion() {
    this.selectedButton = enumTool.unionFeatures;
    this.click();
  }

  click() {

    const select = this.toolBarService1.getInteractionFomName(enumTool.selectFeatures);
    const undoInteraction = this.toolBarService1.getInteractionFomName(enumTool.undoRedoInteraction);

    if (!select || (select.getFeatures() && select.getFeatures().getArray()
      && select.getFeatures().getArray().length < 2)) {
      this.toastComponent.showErrorGenerico('Selezionare almeno due feature con il tool di select');
      return null;
    }
    let done = false;
    undoInteraction.blockStart();
    let errorCount = 0;

    try {
      const selectedFeatures = select.getFeatures().getArray();
      const oriFeatureCount = select.getFeatures().getArray().length;

      const parser = GisUtils.getJstsparser();

      if (this.selectedButton === enumTool.unionFeatures) {
        if (selectedFeatures.length < 2) {
          return;
        }

        let resGeom = null;

        for (const featurePoly of selectedFeatures) {
          console.log('Try union');

          if (featurePoly.getGeometry()) {
            const jstsPoly = parser.read(featurePoly.getGeometry());

            if (resGeom) {
              try {
                //const cbo = new jsts.precision.CommonBitsOp(true);
                //resGeom = cbo.union(resGeom, jstsPoly);

                //resGeom = jsts.precision.EnhancedPrecisionOp
                //jsts.precision.EnhancedPrecisionOp.union(resGeom, jstsPoly);

                resGeom = resGeom.union(jstsPoly);
              } catch (error) {
                errorCount = errorCount + 1;
                GisUtils.showValidateMessage(this.toastComponent, jstsPoly, 'Errore in Unione. ', resGeom, true);
                return;
              }
            } else {
              resGeom = jstsPoly;
            }
          }
        }

        if (resGeom) {
          done = true;
          const getSinglePolygon = GisUtils.getSinglePolygon(parser.write(resGeom));

          if (getSinglePolygon.length === oriFeatureCount) {
            // probabilmente nessune modifica
            this.toastComponent.showWarningGenerico('Unione: nessune modifica effettuata');
            return;
          } /*else {
            if (getSinglePolygon.length > 1) {
              this.toastComponent.showWarningGenerico('Unione: ottenuti ' + getSinglePolygon.length + ' poligoni');
            }
          }*/

          for (const el of getSinglePolygon) {
            const res = new Feature(el);
            // attributi copiati dall'ultimo elemento selezionato
            this.mapService.setNewFeatureattributesFromFeature(res,
              selectedFeatures[selectedFeatures.length - 1], this.mapService.editLayer);

            this.mapService.editLayer.getSource().addFeature(res);
          }

          for (const featurePoly of selectedFeatures) {
            this.mapService.editLayer.getSource().removeFeature(featurePoly);
          }
        }
      }

      if (this.selectedButton === enumTool.intersectFeatures) {
        let res;

        for (const featurePoly of selectedFeatures) {
          if (res) {
            const jstsPoly = parser.read(featurePoly.getGeometry());
            try {
              res = jstsPoly.getBoundary().union(res);
            } catch (error) {
              errorCount = errorCount + 1;
              GisUtils.showValidateMessage(this.toastComponent, jstsPoly, 'Errore in Intersezione. ', res, true);
              return;
            }
          } else {
            res = parser.read(featurePoly.getGeometry()).getBoundary();
          }
        }

        if (res) {
          const polygonizer = new jsts.operation.polygonize.Polygonizer();
          polygonizer.add(res);

          const polygons = polygonizer.getPolygons();

          console.log(polygons.array.length);

          const splitted_polygon_parts = [];

          for (const polygonMp of polygons.array) {
            for (const polygonSp of GisUtils.getSinglePolygon(parser.write(polygonMp))) {
              let intersect = 0;
              let featureToCopy = null;
              const jstsPolygonMp = parser.read(polygonSp);
              for (const selectdFeaturePoly of selectedFeatures) {
                if (selectdFeaturePoly.getGeometry()) {
                  const jstsPoly = parser.read(selectdFeaturePoly.getGeometry());
                  if (jstsPoly.contains(jstsPolygonMp.getInteriorPoint())) {
                    intersect++;
                    if (!featureToCopy) {
                      featureToCopy = selectdFeaturePoly;
                    }
                  }
                }
              }

              if (intersect > 0) {
                done = true;

                /*const pm1 = new jsts.geom.PrecisionModel();
                const reducerKeepCollapse = new jsts.precision.GeometryPrecisionReducer(pm1);
                reducerKeepCollapse.setRemoveCollapsedComponents(true);
                let jstsPolygonMp1 = reducerKeepCollapse.reduce(jstsPolygonMp);
                const newFeature = new Feature(parser.write(jstsPolygonMp1));*/
                const newFeature = new Feature(polygonSp);
                if (intersect > 1) {
                  this.mapService.setNewFeatureattributes(newFeature, this.mapService.editLayer);
                } else if (intersect === 1) {
                  this.mapService.setNewFeatureattributesFromFeature(newFeature, featureToCopy, this.mapService.editLayer);
                }
                this.mapService.editLayer.getSource().addFeature(newFeature);
              }
            }
          }
          if (done) {
            for (const featurePoly of selectedFeatures) {
              console.log('remove feature');
              this.mapService.editLayer.getSource().removeFeature(featurePoly);
            }
          }
        }
      }
    } catch (error) {
      console.log(error);
      this.toastComponent.showErrorGenerico('Errore in esecuzione del tool');
    } finally {
      undoInteraction.blockEnd();
      if (errorCount > 0) {
        undoInteraction.undo();
      }
      if (done) {
        this.mapService.modifyFeatureEmitter.emit(this.mapService.editLayer.get(PropertyLayer.CODICE));
        this.toolBarService1.rimuoviListaInteraction(null, true, true);
      }
    }
  }
}
