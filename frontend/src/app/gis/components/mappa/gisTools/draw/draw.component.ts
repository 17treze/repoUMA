import { MisuraUtils } from './../misura.utils';
import { Component, HostListener, OnDestroy, OnInit } from '@angular/core';
import Draw from 'ol/interaction/Draw';
import GeometryType from 'ol/geom/GeometryType';
import { MapService } from '../../map.service';
import { ToolBarService } from '../../../../shared/ToolBar/toolBar.service';
import { GisTool } from '../gisTool';
import { enumTool } from '../enumTools';
import { PropertyLayer } from '../../../../shared/PropertyLayer.enum';
import { GisStyles } from '../../../../shared/GisStyles';
import { GisCostants } from 'src/app/gis/shared/gis.constants';
import { condition } from '../drawUtils';
import { never } from 'ol/events/condition';

@Component({
  selector: 'gis-draw',
  templateUrl: './draw.component.html',
  styleUrls: ['./draw.component.css']
})
export class DrawComponent extends GisTool implements OnInit {
  activeButton;
  tool: any;

  constructor(private mapService: MapService, private toolBarService1: ToolBarService, private misuraUtils: MisuraUtils,
    private gisStyles: GisStyles, public gisCostants: GisCostants) {
    super([enumTool.drawNewEl], null, true, true, true, toolBarService1.editToolGroup, toolBarService1);
  }

  ngOnInit() {
    super.ngOnInit();
  }

  declareTool(): any[] {
    const self = this;
    this.tool = new Draw({
      source: this.mapService.editLayer.getSource(),
      type: GeometryType.POLYGON,
      style: function (feature) {
        return self.misuraUtils.styleFunction(feature, true, GeometryType.POLYGON,
        self.toolBarService1.misuraStyle, self.gisStyles.newPolygonStyle);
      },
      stopClick: true,
      clickTolerance: this.gisCostants.drawClickTolerance,
      snapTolerance: this.gisCostants.endDrawSnapTolerance,
      condition: condition(self.gisCostants.endDrawCreateVertex),
      finishCondition: never
    });

    const mapService = this.mapService;

    this.tool.on('drawend', async function (event) {
      // AS 30/03/2022: Da decommentare se si vuole inserire simplify della geometria post draw
      // event.feature.setGeometry(event.feature.getGeometry().simplify(event.target.getMap().getView().getResolution()));

      // Rimozione vertici duplicati
      mapService.removeDuplicateCoordinates(event.feature);

      if (await mapService.newFeaturePromise(mapService, event.feature)) {
        mapService.modifyFeatureEmitter.emit(mapService.editLayer.get(PropertyLayer.CODICE));
      }
    });

    return [this.tool];
  }

  public get canUseTool(): boolean {
    return this.toolBarService1.canUseTool(this.mainToolName[0], this.mapService.editLayer);
  }

}
