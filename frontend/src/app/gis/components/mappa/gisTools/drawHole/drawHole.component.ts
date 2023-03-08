import { Component, OnInit } from '@angular/core';
import { MapService } from '../../map.service';
import { ToolBarService } from '../../../../shared/ToolBar/toolBar.service';
import { GisTool } from '../gisTool';
import { enumTool } from '../enumTools';
import DrawHole from 'ol-ext/interaction/DrawHole';
import { GisStyles } from '../../../../shared/GisStyles';
import { GisCostants } from 'src/app/gis/shared/gis.constants';
import { condition } from '../drawUtils';
import { never } from 'ol/events/condition';

@Component({
  selector: 'gis-drawHole',
  templateUrl: './drawHole.component.html',
  styleUrls: ['./drawHole.component.css']
})
export class DrawHoleComponent extends GisTool implements OnInit {

  constructor(private mapService: MapService, private toolBarService1: ToolBarService, private gisStyles: GisStyles,
    public gisCostants: GisCostants) {
    super([enumTool.drawHoleEl], null, false, true, true, toolBarService1.editToolGroup, toolBarService1);
  }

  ngOnInit() {
    super.ngOnInit();
  }

  declareTool(): any[] {
    const _self = this;
    const drawhole = new DrawHole({
      layers: [this.mapService.editLayer],
      style:  this.gisStyles.newPolygonStyle,
      clickTolerance: this.gisCostants.drawClickTolerance,
      snapTolerance: this.gisCostants.endDrawSnapTolerance,
      condition: condition(_self.gisCostants.endDrawCreateVertex),
      finishCondition: never
    });

    drawhole.on('drawend', async function (event) {
      const res = _self.toolBarService1.activeTool;
      if (res && _self.mainToolName.find(x => x === res) !== undefined) {
        _self.toolBarService1.rimuoviListaInteraction([res], false, true);
        _self.enableTool(enumTool[res]);
          console.log(res);
      }

    });

    // non necessario a meno di non aggiornare l'area
    // mapService.modifyFeatureEmitter.emit(mapService.editLayer.get(PropertyLayer.CODICE));

    return [drawhole];
  }

  public get canUseTool(): boolean {
    return this.toolBarService1.canUseTool(this.mainToolName[0], this.mapService.editLayer);
  }
}
