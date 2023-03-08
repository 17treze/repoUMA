import { LavorazioniEvent } from 'src/app/gis/shared/LavorazioniEvent';
import { MapEvent } from './../../../../shared/MapEvent';
import { Component, EventEmitter, OnDestroy, OnInit, Output } from '@angular/core';
import GeometryType from 'ol/geom/GeometryType';
import { MapService } from '../../map.service';
import { MapidService } from '../../mapid.service';
import { ToolBarService } from '../../../../shared/ToolBar/toolBar.service';
import { GisTool } from '../gisTool';
import { enumTool } from '../enumTools';
import { PropertyLayer } from '../../../../shared/PropertyLayer.enum';
import { GisButton } from '../gisButton';
import { ToastGisComponent } from '../../../toast-gis/toast-gis.component';
import UndoRedo from 'ol-ext/interaction/UndoRedo';

import { GeoJSON, WFS, GML } from 'ol/format';
import WKT from 'ol/format/WKT';
import { catchError } from 'rxjs/operators';
import { throwError } from 'rxjs';
import * as jsts from 'jsts';

import Draw from 'ol/interaction/Draw';
import { altKeyOnly, click, altShiftKeysOnly, pointerMove } from 'ol/events/condition';
import { CreazioneLavorazioneService } from '../../../../services/creazione-lavorazione.service';
import { GisUtils } from '../gisUtils';


@Component({
  selector: 'gis-save',
  templateUrl: './save.component.html',
  styleUrls: ['./save.component.css']
})
export class SaveComponent extends GisButton implements OnInit {
  undoInteraction: UndoRedo;
  @Output() lavorazioneChangedOutput = new EventEmitter<any>();

  constructor(private mapService: MapService, private toolBarService1: ToolBarService, private toastComponent: ToastGisComponent,
    private creazioneLavorazioneService: CreazioneLavorazioneService, public mapEvent: MapEvent, 
    public lavorazioniEvent: LavorazioniEvent) {
    super(enumTool.saveFeatures);
  }

  ngOnInit() {
  }

  public get canUse(): boolean {
    return this.toolBarService1.canUseTool(this.mainName, this.mapService.editLayer);
  }

  public click() {

    const editLayer = this.mapService.editLayer;

    if (editLayer) {

      const id = editLayer.get(PropertyLayer.VALOREFILTRO);
      const name = editLayer.get(PropertyLayer.CONTESTO);

      const featuresSource = editLayer.getSource().getFeatures();
      const featureCollection = [];

      featuresSource.forEach(element => {
        GisUtils.getSinglePartFeature(element, editLayer, this.mapService).forEach(sp => {
          featureCollection.push(sp);
        });
      });
      
      console.log(featureCollection);

      const featureCollectionGJS = new GeoJSON().writeFeaturesObject(featureCollection);

      if (name === PropertyLayer.CONTESTO_LAVORAZIONE_EDITING) {
        return new Promise(((resolve, reject) => {
          // Salvataggio Workspace
          this.creazioneLavorazioneService.salvaWorkspace(id, featureCollectionGJS).subscribe((respone: any) => {
            if (respone['status'] === 201) {
              resolve(true);
              const lavSuolo = this.lavorazioniEvent.objectLavorazione;
              const viewCenter = this.mapEvent.map.getView().getCenter();
              lavSuolo.xUltimoZoom = viewCenter[0];
              lavSuolo.yUltimoZoom = viewCenter[1];
              lavSuolo.scalaUltimoZoom = Math.round(MapService.getMapScale(this.mapEvent.map, this.mapService.linearProjection));
              if (lavSuolo.scalaUltimoZoom < 10) {
                lavSuolo.scalaUltimoZoom = 10;
              }
              this.mapService.ricaricaLavorazioneLayers(id, true, false);
             // PUT Lavorazione
             this.creazioneLavorazioneService.putLavorazioneSuolo(this.lavorazioniEvent.idLavorazione,
              lavSuolo).subscribe(response => {
               if (response['status'] === 200 || response['status'] === 201) {
                 this.toastComponent.showSuccess();
                 this.lavorazioneChangedOutput.emit({ 'changed': false, 'lavorazioneSuolo': lavSuolo });
               } else {
                 this.toastComponent.showError();
               }
             },
               (error) => {
                 this.toastComponent.showError();
               });
            } else {
              this.toastComponent.showError();
              reject(true);
            }
          },
            (error) => {
              this.toastComponent.showError();
              reject(error);
            });

        }));
      }

    }
  }

  isActive(): boolean {
    if (!this.toolBarService1.canUseTool(this.mainName, this.mapService.editLayer)) {
      return false;
    }

    if (!this.undoInteraction) {
      this.undoInteraction = this.toolBarService1.getInteractionFomName(enumTool.undoRedoInteraction);
      this.mapEvent.setUndoInteraction(this.undoInteraction);
    }
    if (this.undoInteraction && this.undoInteraction._undoStack) {
      this.mapEvent.setUndoInteraction(this.undoInteraction);
      return  this.mapEvent.hasNotSavedAction();
    } else {
      return false;
    }
  }
}
