import { getLength } from 'ol/sphere';
import { MapEvent } from './../../../../shared/MapEvent';
import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { MapService } from '../../map.service';
import { ToolBarService } from '../../../../shared/ToolBar/toolBar.service';
import { GisTool } from '../gisTool';
import { enumTool } from '../enumTools';
import {DragBox, Select} from 'ol/interaction';
import {OSM, Vector as VectorSource} from 'ol/source';
import {Tile as TileLayer, Vector as VectorLayer} from 'ol/layer';
import { Fill, Stroke, Style } from 'ol/style';
import { GisStyles } from '../../../../shared/GisStyles';
import {shiftKeyOnly, altKeyOnly} from 'ol/events/condition';
import GeoJSON from 'ol/format/GeoJSON';
@Component({
  selector: 'gis-select',
  templateUrl: './select.component.html',
  styleUrls: ['./select.component.css']
})
export class SelectComponent extends GisTool implements OnInit {
  @Output() receiveSelectedFeatures = new EventEmitter<number>();
  styles = [];
  selectInteraction: Select;
  vectorSource: any;
  dragBox: any;

  constructor(private mapService: MapService, private gisStyles: GisStyles, private toolBarService1: ToolBarService,
    public mapEvent: MapEvent) {
    super([enumTool.selectFeatures, enumTool.selectFeaturesBox], null, false, true, false, toolBarService1.editToolGroup, toolBarService1);
  }

  ngOnInit() {
    super.ngOnInit();
    this.styles = [
      new Style({
        stroke: new Stroke({
          color: this.gisStyles.selectTematismoBordoColore,
          width: this.gisStyles.selectTematismoDimensioneBordo,
        }),
        fill: new Fill({
          color: this.gisStyles.selectTematismoFillColore,
        }),
      })];
  }

  public get canUseTool(): boolean {
    return this.toolBarService1.canUseTool(this.mainToolName[0], this.mapService.editLayer);
  }

  declareTool(): any[] {
    const _self = this;
    this.vectorSource = this.mapService.editLayer.getSource();

    this.selectInteraction = new Select({
      style: this.styles,
      layers: [this.mapService.editLayer]
    });
    const selectedFeatures = this.selectInteraction.getFeatures();
    this.dragBox = new DragBox({
      condition: shiftKeyOnly,
    });
    this.selectInteraction.on('select', (evt) => {
      // Se l'evento ShifKey è null
      if (evt.mapBrowserEvent && !evt.mapBrowserEvent.originalEvent.shiftKey && evt.selected.length > 0) {
        // Selezione di una singola Feature
        const singleSelect = evt.selected[0].values_;
        let featureList = Object.assign([], selectedFeatures.getArray());
        featureList.forEach(element => {
          const indexFeature = selectedFeatures.getArray().findIndex(feat => feat.get('ID') !== singleSelect.ID);
          if (indexFeature !== -1) {
            selectedFeatures.getArray()[indexFeature].setStyle();
            selectedFeatures.getArray().splice(indexFeature, 1);
          }
        });
            this.toolBarService1.mapElementiWorkspace(selectedFeatures.getArray()).then(promiseArray => {
              this.toolBarService1.sendFeatures(promiseArray);
            });
        } else {
          // Se l'evento ShifKey è true
          setTimeout(() => {
          // Selezione multipla di Features
          this.toolBarService1.mapElementiWorkspace(selectedFeatures.getArray()).then(promiseArray => {
            this.toolBarService1.sendFeatures(promiseArray);
          });
        }, 100);
      }
    });
    _self.dragBox.on('boxend', function () {
      const extent = _self.dragBox.getGeometry().getExtent();
      _self.vectorSource.forEachFeatureIntersectingExtent(extent, function (feature) {
        const checkFeatures = selectedFeatures.getArray().find(feat => {
          return feat.get('ID') === feature.get('ID');
        });
        if (!checkFeatures) {
          selectedFeatures.push(feature);
        } else {
          const index = selectedFeatures.getArray().findIndex(f => f.get('ID') === feature.get('ID'));
          selectedFeatures.getArray()[index].setStyle();
          selectedFeatures.getArray().splice(index, 1);
        }
      });

      _self.toolBarService1.mapElementiWorkspace(selectedFeatures.getArray()).then(promiseArray => {
        _self.toolBarService1.sendFeatures(promiseArray);
      });
    });
    ToolBarService.setInteractionProperties(this.selectInteraction, enumTool.selectFeatures);
    ToolBarService.setInteractionProperties(this.dragBox, enumTool.selectFeaturesBox);
    this.receiveSelectedFeatures.emit(selectedFeatures);
    return [this.selectInteraction, this.dragBox];
  }

  enableTool() {
    const isInInteraction = this.toolBarService1.isInInteraction(enumTool.selectFeatures);
    if (isInInteraction) {
      const toolState = this.selectInteraction.getActive();

      // se ho cliccato esplicitamente su tool
      if (toolState) {
        // se il tool è attivo (ovvero il click lo vuole disattivare) lo disattivo e pulisco la selezione
        if (this.selectInteraction) {
          if (this.selectInteraction.getFeatures().length > 0) {
            this.selectInteraction.getFeatures().clear();
          }
          this.selectInteraction.setActive(false);
          this.dragBox.setActive(false);
        }
        this.toolBarService1.isEditToolActive = false;
        this.setContextMenu(false);
        this.toolBarService1.getManageActivateEditToolEmitter().emit(false);
      } else {
        // se il tool è disattivo (ovvero il click lo vuole attivare) disattivo gli altri e attivo lui
        this.toolBarService1.rimuoviListaInteraction(this.toolBar, this.clearSelection, false);
        this.selectInteraction.setActive(true);
        this.dragBox.setActive(true);
        this.setContextMenu(false);
        this.toolBarService1.isEditToolActive = true;
        this.toolBarService1.getManageActivateEditToolEmitter().emit(true);

      }
    } else {
      // solo la prima volta
      super.enableTool();
      /*const _self = this;
      this.selectInteraction.on('select', function(evt) {
        _self.toolBarService1.getSelectChangeEmitter().emit();
    });*/
    }
  }

  disableToolCustom(toolName: string) {

    console.log('disableToolCustom me ', toolName);

    const toolState = this.selectInteraction.getActive();
    if (toolState) {
      this.selectInteraction.getFeatures().clear();
    }

    this.selectInteraction.setActive(false);
    if (this.selectInteraction.getFeatures() && this.selectInteraction.getFeatures().getArray()
      && this.selectInteraction.getFeatures().getArray().length === 0) {
    }
  }

  isActive(): boolean {
    return (this.selectInteraction) ? this.selectInteraction.getActive() : false;
  }
}
