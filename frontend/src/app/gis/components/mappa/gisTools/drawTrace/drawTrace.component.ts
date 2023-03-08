import { MapEvent } from './../../../../shared/MapEvent';
import { Component, OnDestroy, OnInit } from '@angular/core';
import Draw from 'ol/interaction/Draw';
import GeometryType from 'ol/geom/GeometryType';
import { MapService } from '../../map.service';
import { MapidService } from '../../mapid.service';
import { ToolBarService } from '../../../../shared/ToolBar/toolBar.service';
import { GisTool } from '../gisTool';
import { enumTool } from '../enumTools';
import { PropertyLayer } from '../../../../shared/PropertyLayer.enum';
import { ConfirmationService, MessageService } from 'primeng/api';
import OlMap from 'ol/Map';
import { GisUtils } from '../gisUtils';
import Feature from 'ol/Feature';
import LineString from 'ol/geom/LineString';
import VectorSource from 'ol/source/Vector';
import VectorLayer from 'ol/layer/Vector';
import { Fill, Stroke, Style } from 'ol/style';
import { PropertyLayerSwitcher } from '../../../../shared/PropertyLayerSwitcher.enum';
import { GisStyles } from '../../../../shared/GisStyles';


@Component({
  selector: 'gis-drawTrace',
  templateUrl: './drawTrace.component.html',
  styleUrls: ['./drawTrace.component.css']
})
export class DrawTraceComponent extends GisTool implements OnInit, OnDestroy {
  previewVector: VectorLayer;

  constructor(private mapService: MapService, private mapidService: MapidService, public mapEvent: MapEvent,
    private toolBarService1: ToolBarService, private confirmationService: ConfirmationService, private gisStyles: GisStyles) {
    super([enumTool.drawTraceNewEl], null, true, true, true, toolBarService1.editToolGroup, toolBarService1);
  }
  ngOnInit() {
    super.ngOnInit();
  }

  ngOnDestroy() {
  }

  clearInteractions(id: any) {
  }

  declareTool(): any[] {
    const self = this;
    const map = this.mapEvent.map;
    // this line only appears when we're tracing a feature outer ring
    const previewLine = new Feature({
      geometry: new LineString([]),
    });

    if (this.previewVector) {
      map.removeLayer(this.previewVector);
    }

    this.previewVector = new VectorLayer({
      source: new VectorSource({
        features: [previewLine],
      }),
      style: new Style({
        stroke: new Stroke({
          color: 'rgba(255, 0, 0, 1)',
          width: 2,
        }),
      }),
    });

    map.addLayer(this.previewVector);

    const mapService = this.mapService;

    let drawing, tracingFeature, startPoint, endPoint;
    drawing = false;

    let tool = new Draw({
      source: this.mapService.editLayer.getSource(),
      type: GeometryType.POLYGON,
      style: this.gisStyles.newPolygonStyle
    });

    const editLayer = this.mapService.editLayer;

    tool.on('drawstart',  function () {
      drawing = true;
    });
    tool.on('drawend', async function (event) {
      drawing = false;
      previewLine.getGeometry().setCoordinates([]);
      tracingFeature = null;

      const feature = event.feature;
      if (await mapService.newFeaturePromise(mapService, event.feature)) {
        mapService.modifyFeatureEmitter.emit(mapService.editLayer.get(PropertyLayer.CODICE));
      }
    });


    /*
        let layerWfs = [];
    for (let layer of this.mapService.getWfsContestoLayers()) {
      layerWfs.push(layer.get(PropertyLayer.CODICE));
    }

    const getFeatureOptions = {
      hitTolerance: 10,
      layerFilter: function (layer) {
        return layerWfs.filter(x => layer.get(PropertyLayer.CODICE) === x).length > 0;
      }
    };
    */

    const getFeatureOptions = {
      hitTolerance: 10,
      layerFilter: function (layer) {
        return layer === mapService.wfsTraceLayer;
      },
    };

    // the pointermove event is used to show a preview of the result of the tracing
    map.on('pointermove', function (event) {
      if (tracingFeature && drawing) {
        let coord = null;
        map.forEachFeatureAtPixel(
          event.pixel,
          function (feature) {
            if (tracingFeature === feature) {
              coord = map.getCoordinateFromPixel(event.pixel);
            }
          },
          getFeatureOptions
        );

        let previewCoords = [];
        if (coord) {
          endPoint = tracingFeature.getGeometry().getClosestPoint(coord);
          previewCoords = GisUtils.getPartialRingCoords(
            tracingFeature,
            startPoint,
            endPoint
          );
        }
        previewLine.getGeometry().setCoordinates(previewCoords);
      }
    });

    // the click event is used to start/end tracing around a feature
    map.on('click', function (event) {
      if (!drawing) {
        return;
      }

      let hit = false;
      map.forEachFeatureAtPixel(
        event.pixel,
        function (feature) {
          if (tracingFeature && feature !== tracingFeature) {
            return;
          }

          hit = true;
          const coord = map.getCoordinateFromPixel(event.pixel);

          // second click on the tracing feature: append the ring coordinates
          if (feature === tracingFeature) {
            endPoint = tracingFeature.getGeometry().getClosestPoint(coord);
            const appendCoords = GisUtils.getPartialRingCoords(
              tracingFeature,
              startPoint,
              endPoint
            );
            tool.removeLastPoint();
            tool.appendCoordinates(appendCoords);
            tracingFeature = null;
          }

          // start tracing on the feature ring
          tracingFeature = feature;
          startPoint = tracingFeature.getGeometry().getClosestPoint(coord);
        },
        getFeatureOptions
      );

      if (!hit) {
        // clear current tracing feature & preview
        previewLine.getGeometry().setCoordinates([]);
        tracingFeature = null;
      }
    });

    return [tool];
  }

  public get canUseTool(): boolean {
    return this.toolBarService1.canUseTool(this.mainToolName[0], this.mapService.editLayer);
  }
}
