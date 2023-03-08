import { PanelEvent } from './../../../../shared/PanelEvent';
import { GisMessaggiToastCostants } from './../../../../shared/messaggi-toast.constants';
import { ToastGisComponent } from './../../../toast-gis/toast-gis.component';
import { Select } from 'ol/interaction';
import { Component, OnInit } from '@angular/core';
import { ToolBarService } from '../../../../shared/ToolBar/toolBar.service';
import { enumTool } from '../enumTools';

@Component({
  selector: 'gis-aggiungi-note',
  templateUrl: './aggiungi-note.component.html',
  styleUrls: ['./aggiungi-note.component.css']
})
export class AggiungiNoteComponent implements OnInit {
  selectedFeatures: Select;
  showNote: boolean;

  constructor(public panelEvent: PanelEvent,
    private toolBarService1: ToolBarService, private messages: GisMessaggiToastCostants, private toastComponent: ToastGisComponent) {
  }

  ngOnInit() {
  }

  // tslint:disable-next-line: use-lifecycle-interface
  ngOnDestroy() {
    if (this.selectedFeatures) {
      this.selectedFeatures.getFeatures().clear();
    }
  }

  apriNote() {
    const selectedElements = this.toolBarService1.getSelectedFeaturesLength();
    if (selectedElements === 0) {
      this.toastComponent.showErrorGenerico(this.messages.selectFeatureWarning);
      return null;
    } else {
      this.showNote = true;
      this.panelEvent.showNoteFeatures = true;
    }
  }
}
