import { Select } from 'ol/interaction/Select';
import { VectorLayer } from 'ol/layer/Vector';
import { LayerGisService } from 'src/app/gis/services/layer-gis.service';
import { LavorazioniEvent } from 'src/app/gis/shared/LavorazioniEvent';
import { MapEvent } from './../../../../shared/MapEvent';
import { Component, HostListener, OnDestroy, OnInit } from '@angular/core';
import UndoRedo from 'ol-ext/interaction/UndoRedo';
import FillAttribute from 'ol-ext/interaction/FillAttribute';
import { MapService } from '../../map.service';
import { ToolBarService } from '../../../../shared/ToolBar/toolBar.service';
import { enumTool } from '../enumTools';
import { GisButton } from '../gisButton';
import { enumListener } from '../enumListeners';
import { Subscription } from 'rxjs';
import { PropertyLayer } from '../../../../shared/PropertyLayer.enum';


@Component({
  selector: 'gis-undoRedo',
  templateUrl: './undoRedo.component.html',
  styleUrls: ['./undoRedo.component.css']
})
export class UndoRedoComponent extends GisButton implements OnInit, OnDestroy {
  undoInteraction: UndoRedo;
  newContestoListener: Subscription;
  constructor(private mapService: MapService, private toolBarService1: ToolBarService, public mapEvent: MapEvent,
    private lavorazioniEvent: LavorazioniEvent, private layerGisService: LayerGisService) {
    super(enumTool.undoRedoInteraction);
  }

  ngOnInit() {
    this.newContestoListener = this.mapService.getNewContestoEmitter()
      .subscribe(() => this.clearInteraction());
    this.mapEvent.setUndoInteraction(this.undoInteraction);
    this.undoInteraction = new UndoRedo(null);
    ToolBarService.setInteractionProperties(this.undoInteraction, enumTool.undoRedoInteraction);
    this.undoInteraction.setActive(false);
    const fillAttr = new FillAttribute({ active: false }, null);
    ToolBarService.setInteractionProperties(fillAttr, enumTool.fillAttributeInteraction);
    this.toolBarService1.addInteraction([this.undoInteraction, fillAttr], false, false, false, null);
    this.mapEvent.undoInteraction = this.undoInteraction;
  }


  @HostListener('document:keypress', ['$event'])
  handleKeyboardEvent(event: KeyboardEvent) {
    if (this.undoInteraction && event.ctrlKey && event.code === 'KeyZ') {
      this.clickUndoTool();
    } else if (this.undoInteraction && event.ctrlKey && event.code === 'KeyY') {
      this.clickRedoTool();
    }
  }

  ngOnDestroy() {
    this.newContestoListener.unsubscribe();
  }

  clickUndoTool() {
    const selectedFeature = this.toolBarService1.getInteractionFomName(enumTool.selectFeatures);
    // console.log(this.undoInteraction._undoStack);
    if (this.undoInteraction) {
      if (this.toolBarService1.isInDrawing(true, false, true)) {
        if (this.toolBarService1.activeTool !== enumTool.drawRegularEl) {
          const currentTool = this.toolBarService1.getInteractionFomName(this.toolBarService1.activeTool);
          currentTool.removeLastPoint();
        }
      } else if (this.toolBarService1.activeTool === enumTool.modifyAdl) {
        this.layerGisService.getLayerByCode('ADL').then((layerAdl: VectorLayer) => {
          for (let i = this.undoInteraction._undoStack.length - 1; i >= 0; i--) {
            if (this.undoInteraction._undoStack[i].type === 'changefeature') {
              layerAdl.getSource().getFeatures()[0].setGeometry(this.undoInteraction._undoStack[i].oldFeature.getGeometry());
              this.undoInteraction._undoStack.splice(i, 1);
              this.undoInteraction.undo(this.undoInteraction._undoStack);
              this.checkChangeFeature(this.undoInteraction._undoStack);
              break;
            }
          }
        });
      } else {
        this.undoInteraction.undo();
        this.mapService.modifyFeatureEmitter.emit(enumListener.undoAction);
        this.refreshStylePoligoni(selectedFeature);
      }
    }
  }

  refreshStylePoligoni(selectedFeature) {
    if (selectedFeature && selectedFeature.getFeatures().getArray() && selectedFeature.getFeatures().getArray().length > 0) {
      selectedFeature.getFeatures().getArray().forEach(feat => {
        feat.setStyle();
        selectedFeature.values_.active = false;
        selectedFeature.features_.array_ = [];
      });
    } else if (this.toolBarService1.activeTool !== enumTool.modifyEl) {
      const currentLayer = MapService.getLayerFromLayerCodeProperty(this.mapEvent.map, PropertyLayer.CODICE_LAYER_WORKSPACE);
      currentLayer.getSource().getFeatures().forEach(feat => {
        feat.setStyle();
      });
    }
  }

  checkChangeFeature(undoStack) {
    const adlStack = undoStack.filter(x => x.type === 'changefeature');
    if (adlStack.length === 0) {
      this.lavorazioniEvent.calcoloAdlActived = false;
    }
  }
  clickRedoTool() {
    // console.log(this.undoInteraction._undoStack);
    if (this.undoInteraction) {
      if (!this.toolBarService1.isInDrawing(true, false, true)) {
        this.undoInteraction.redo();
        this.mapService.modifyFeatureEmitter.emit(enumListener.redoAction);
      }
    }
  }

  clearInteraction() {
    console.log('clearInteraction');
    if (this.undoInteraction) {
      this.undoInteraction.clear();
      this.undoInteraction.setActive(this.canUse);
    }
  }

  click() {
    throw new Error('Method not implemented.');
  }

  get canUse(): boolean {
    return this.mapService.editLayer;
  }
}
