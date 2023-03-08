import { MisuraUtils } from './../misura.utils';
import { Component, OnInit } from '@angular/core';

import { MapService } from '../../map.service';
import { MapidService } from '../../mapid.service';
import { ToolBarService } from '../../../../shared/ToolBar/toolBar.service';
import { GisTool } from '../gisTool';
import { enumTool } from '../enumTools';
import DrawRegular from 'ol-ext/interaction/DrawRegular';
import { PropertyLayer } from '../../../../shared/PropertyLayer.enum';
import { GisStyles } from '../../../../shared/GisStyles';
import GeometryType from 'ol/geom/GeometryType';

@Component({
  selector: 'gis-drawRegular',
  templateUrl: './drawRegular.component.html',
  styleUrls: ['./drawRegular.component.css']
})
export class DrawRegularComponent extends GisTool implements OnInit {
  numLati: any = 4;

  constructor(private mapService: MapService,
    private toolBarService1: ToolBarService, private gisStyles: GisStyles, private misuraUtils: MisuraUtils) {
    super([enumTool.drawRegularEl], null, true, true, true, toolBarService1.editToolGroup, toolBarService1);
  }

  ngOnInit() {
    super.ngOnInit();
  }

  declareTool(): any[] {
    const self = this;
    const tool = new DrawRegular({
      source: this.mapService.editLayer.getSource(),
      sides: this.numLati,
      canRotate: true,
      style: function (feature) {
        return self.misuraUtils.styleFunction(feature, true, GeometryType.POLYGON,
        self.toolBarService1.misuraStyle, self.gisStyles.newPolygonStyle);
      },
    });

    const mapService = this.mapService;

    const _self = this;
    tool.on('drawend', async function (event) {
      if (await mapService.newFeaturePromise(mapService, event.feature)) {
        mapService.modifyFeatureEmitter.emit(mapService.editLayer.get(PropertyLayer.CODICE));
      }
    });

    return [tool];
  }

  onChange(newValue) {
    if (this.numLati > 10) {
      this.numLati = 10;
    } else if (this.numLati === '') {
      this.numLati = 1;
    }
    this.reloadTool();
  }

  get canUseTool(): boolean {
    return this.toolBarService1.canUseTool(this.mainToolName[0], this.mapService.editLayer);
  }

  setLati(type, lati) {
    if (type === 'increase' && this.numLati < 10) {
      this.numLati = Number(lati) + 1;
      this.reloadTool();
    } else if (type === 'decrease' && this.numLati >= 1) {
      this.numLati = Number(lati) - 1;
      this.reloadTool();
    }
  }

}
