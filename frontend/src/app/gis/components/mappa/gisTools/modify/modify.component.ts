import { MisuraUtils } from './../misura.utils';
import { Component, HostListener, OnInit } from '@angular/core';

import Modify from 'ol/interaction/Modify';
import Select from 'ol/interaction/Select';
import { MapService } from '../../map.service';
import { ToolBarService } from '../../../../shared/ToolBar/toolBar.service';
import { GisTool } from '../gisTool';
import { enumTool } from '../enumTools';
import { GisStyles } from '../../../../shared/GisStyles';
import GeometryType from 'ol/geom/GeometryType';
import { GisCostants } from 'src/app/gis/shared/gis.constants';

@Component({
  selector: 'gis-modify',
  templateUrl: './modify.component.html',
  styleUrls: ['./modify.component.css']
})
export class ModifyComponent extends GisTool implements OnInit {
  select: Select;

  constructor(private mapService: MapService, private gisStyles: GisStyles, private toolBarService1: ToolBarService,
    private misuraUtils: MisuraUtils, public gisCostants: GisCostants) {
    super([enumTool.modifyEl], [enumTool.selectEl], true, true, true, toolBarService1.editToolGroup, toolBarService1);
  }

  declareTool(): any {
    const _self = this;
    const select = new Select({style: function (feature) {
      return _self.misuraUtils.styleFunction(feature, true, GeometryType.POLYGON,
      _self.toolBarService1.misuraStyle, _self.gisStyles.modifyPolygonStyle);
    },
    layers: [this.mapService.editLayer] });
    ToolBarService.setInteractionProperties(select, enumTool.selectEl);

    const modify = new Modify({
      features: select.getFeatures(),
      pixelTolerance: _self.gisCostants.modifyPixelTolerance,
      snapToPointer: true
    });
    ToolBarService.setInteractionProperties(modify, enumTool.modifyEl);

    this.select = select;

    return [select, modify];
  }

  public get canUseTool(): boolean {
    return this.toolBarService1.canUseTool(this.mainToolName[0], this.mapService.editLayer);
  }

}

