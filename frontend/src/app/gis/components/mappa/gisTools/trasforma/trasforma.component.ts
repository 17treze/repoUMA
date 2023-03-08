import { Component, OnDestroy, OnInit } from '@angular/core';
import { MapService } from '../../map.service';
import { ToolBarService } from '../../../../shared/ToolBar/toolBar.service';
import { GisTool } from '../gisTool';
import { enumTool } from '../enumTools';
import { GisTransform } from '../gisTransform';

@Component({
  selector: 'gis-trasforma',
  templateUrl: './trasforma.component.html',
  styleUrls: ['./trasforma.component.css']
})
export class TrasformaComponent extends GisTool implements OnInit {
  activeButton;
  transform: GisTransform;
  textTransform: string;
  constructor(private mapService: MapService, public toolBarService1: ToolBarService) {
    super([enumTool.transformEl], null, true, true, true, toolBarService1.editToolGroup, toolBarService1);
  }

  ngOnInit() {
    super.ngOnInit();
  }

  clearInteractions(id: any) {
  }

  enableTool() {
    this.enableTransform();
  }

  enableTransform() {
    this.declareTool();
    this.transform.enableTool();
  }

  declareTool(): any {
    this.transform = new GisTransform(this.mapService, this.toolBarService1);
    this.textTransform = '';
    let visualizzaTransform = this.transform.canUseTool;
    let features = [];
  }

  getFunzioneTrasforma(): any {
    if (this.transform.canUseTool) {
      return ({
        text: this.textTransform,
        callback: this.enableTransform()
      });
    } else {
      return null;
    }
  }

  public get canUseTool(): boolean {
    return this.toolBarService1.canUseTool(this.mainToolName[0], this.mapService.editLayer);
  }
}
