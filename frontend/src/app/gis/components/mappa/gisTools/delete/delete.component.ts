import { Component, OnInit } from '@angular/core';
import { MapService } from '../../map.service';
import { ToolBarService } from '../../../../shared/ToolBar/toolBar.service';
import { enumTool } from '../enumTools';
import { GisButton } from '../gisButton';
import Select from 'ol/interaction/Select';
import { PropertyLayer } from '../../../../shared/PropertyLayer.enum';
import * as cloneDeep from 'lodash/cloneDeep';

@Component({
  selector: 'gis-delete',
  templateUrl: './delete.component.html',
  styleUrls: ['./delete.component.css']
})
export class DeleteComponent extends GisButton implements OnInit {
  select: Select;
  constructor(private mapService: MapService, private toolBarService1: ToolBarService) {
    super(enumTool.deleteEl);
  }
  ngOnInit() {

    document.addEventListener('keydown', ({ key }) => {
      if (key === 'Delete') {
        this.click();
      }
    });
  }

  click() {
    this.select = this.toolBarService1.getInteractionFomName(enumTool.selectFeatures);

    let done = this.doDelete();
    /*if (!done) {
       select = this.toolBarService1.getInteractionFomName(enumTool.selectEl);
       done = this.doDelete(select);
    }*/

    if (done) {
      this.mapService.modifyFeatureEmitter.emit(this.mapService.editLayer.get(PropertyLayer.CODICE));
      this.toolBarService1.mapElementiWorkspace(this.select.getFeatures().getArray()).then(reloadedEl => {
        this.toolBarService1.sendFeatures(reloadedEl);
      });
    }
  }

  doDelete(): boolean {
    let done = false;
    if (this.select && this.select.getFeatures() && this.select.getFeatures().getArray().length > 0) {
      const selectedFeatures = cloneDeep(this.select.getFeatures().getArray());
      for (const element of selectedFeatures) {
        done = true;
        const featureToRemove = this.mapService.editLayer.getSource().getFeatures().filter(feature => feature.id_ === element.id_);
        if (featureToRemove.length > 0) {
          this.mapService.editLayer.getSource().removeFeature(element);
        }
      }
    }
    return done;
  }

  public get canUse(): boolean {
    return this.toolBarService1.canUseTool(this.mainName, this.mapService.editLayer);
  }
}
