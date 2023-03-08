import { Injectable } from '@angular/core';
import VectorLayer from 'ol/layer/Vector';
import { enumTool } from '../components/mappa/gisTools/enumTools';

@Injectable()
export class MapEvent {
   map: any;
   checkLayerCaricati: VectorLayer;
   public undoInteraction: any;
   setUndoInteraction(undoInteraction: any) {
      this.undoInteraction = undoInteraction;
   }

   hasNotSavedAction(): boolean {
      if (this.undoInteraction && this.undoInteraction._undoStack.length > 0) {
         return true;
      } else {
         return false;
      }
   }
}

