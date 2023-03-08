import { MapEvent } from 'src/app/gis/shared/MapEvent';
import { MapService } from './../../../map.service';
import { Select } from 'ol/interaction/Select';
import { Feature } from 'ol/Feature';
import { CampoFeature } from './../../../../../shared/CampoFeature';
import { enumTool } from './../../enumTools';
import { ToolBarService } from 'src/app/gis/shared/ToolBar/toolBar.service';
import { PanelEvent } from './../../../../../shared/PanelEvent';
import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { PropertyLayer } from '../../../../../shared/PropertyLayer.enum';

@Component({
  selector: 'gis-aggiungi-note-dialog',
  templateUrl: './aggiungi-note-dialog.component.html',
  styleUrls: ['./aggiungi-note-dialog.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class AggiungiNoteDialogComponent implements OnInit {
  showDialog: boolean;
  selectedElements: number;
  note: string;
  selected: Select;
  constructor(public panelEvent: PanelEvent, private toolBarService: ToolBarService, private mapService: MapService,
    public mapEvent: MapEvent) {
  }

  ngOnInit() {
    this.note = '';
    this.selected = this.toolBarService.getInteractionFomName(enumTool.selectFeatures);
    this.showDialog = this.panelEvent.showNoteFeatures;
    this.selectedElements = this.toolBarService.getSelectedFeaturesLength();
    this.note = this.toolBarService.getNotesFromFeature(this.selected.getFeatures().getArray());
  }

  closeDialog() {
    this.panelEvent.showNoteFeatures = false;
  }

  salvaNote(note) {
    this.setNote(note);
    this.closeDialog();
  }

  setNote(data) {
    const fillAttr = this.toolBarService.getInteractionFomName(enumTool.fillAttributeInteraction);
    const attr = {};
    this.selected.getFeatures().getArray().forEach((element: Feature) => {
      const feat = this.mapService.getWfsFeatureFromId(PropertyLayer.CODICE_LAYER_WORKSPACE, element.id_);
      if (feat) {
        attr[CampoFeature.NOTE] = data;
        fillAttr.fill([feat], attr);
      }
    });
    this.note = data;
  }
}
