import { LavorazioniEvent } from 'src/app/gis/shared/LavorazioniEvent';
import { MapEvent } from './../../../../shared/MapEvent';
import { MisuraUtils } from './../../../mappa/gisTools/misura.utils';
import { enumTool } from './../../../mappa/gisTools/enumTools';
import { GisCostants } from './../../../../shared/gis.constants';
import { MapService } from './../../../mappa/map.service';
import { Component, HostListener, Input, OnInit, ViewEncapsulation } from '@angular/core';
import Modify from 'ol/interaction/Modify';
import Select from 'ol/interaction/Select';import { ToolBarService } from '../../../../shared/ToolBar/toolBar.service';
import { GisStyles } from '../../../../shared/GisStyles';
import { GisTool } from '../../../mappa/gisTools/gisTool';

@Component({
  selector: 'gis-modify-adl',
  templateUrl: './modify-adl.component.html',
  styleUrls: ['./modify-adl.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class ModifyAdlComponent extends GisTool implements OnInit  {
  select: Select;

  constructor(private toolBarService1: ToolBarService, public gisCostants: GisCostants, public mapEvent: MapEvent,
    public lavorazioniEvent: LavorazioniEvent) {
    super([enumTool.modifyAdl], [enumTool.selectAdl], true, true, true, toolBarService1.editToolGroup, toolBarService1);
  }

  declareTool(): any {
    const self = this;
    this.lavorazioniEvent.activeModifyAdl = !this.lavorazioniEvent.activeModifyAdl;
    const select = this.toolBarService1.getSelectedAdl(self.lavorazioniEvent.featureAdl);
    ToolBarService.setInteractionProperties(select, enumTool.selectAdl);
    select.features_.array_.push(self.lavorazioniEvent.featureAdl.getSource().getFeatures()[0]);

    const modify = new Modify({
      features: select.getFeatures(),
      pixelTolerance: self.gisCostants.modifyPixelTolerance,
      snapToPointer: true
    });

    modify.on('modifyend', async function (event) {
      if (self.mapEvent.undoInteraction._undoStack.length === 0) {
        self.lavorazioniEvent.calcoloAdlActived = false;
      } else if (self.mapEvent.undoInteraction._undoStack.length > 0 || self.lavorazioniEvent.featureAdl){
        self.lavorazioniEvent.calcoloAdlActived = true;
        self.lavorazioniEvent.stackAdl = true;
      }
    });
    ToolBarService.setInteractionProperties(modify, enumTool.modifyAdl);
    return [select, modify];
  }

  public get canUseTool(): boolean {
    return this.toolBarService1.canUseTool(this.mainToolName[0], true);
  }

}
