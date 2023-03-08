import { LavorazioniEvent } from './../../../../shared/LavorazioniEvent';
import { Component, HostListener, OnDestroy, OnInit, ViewEncapsulation } from '@angular/core';
import { MapService } from '../../map.service';
import { ToolBarService } from '../../../../shared/ToolBar/toolBar.service';
import { MapEvent } from '../../../../shared/MapEvent';
import { ToastGisComponent } from '../../../toast-gis/toast-gis.component';
import { GisCostants } from 'src/app/gis/shared/gis.constants';
import VectorLayer from 'ol/layer/Vector';
import { MapidService } from '../../mapid.service';
import { enumTool } from '../enumTools';
import { GisStyles } from '../../../../shared/GisStyles';
import GeometryType from 'ol/geom/GeometryType';
@Component({
  selector: 'gis-editToolBar',
  templateUrl: './editToolBar.component.html',
  styleUrls: ['./editToolBar.component.css'],
  encapsulation: ViewEncapsulation.None

})
export class EditToolBarComponent implements OnInit, OnDestroy {
  subscription: any;
  keyDownListener: any;

  constructor(private mapEvent: MapEvent, private mapService: MapService,
    private toolBarService1: ToolBarService, public lavorazioniEvent: LavorazioniEvent, private toastComponent: ToastGisComponent,
    private gisCostants: GisCostants,
    private gisStyles: GisStyles) {
    // super(enumTool.drawNewEl, null, true, true, toolBarService1.editToolGroup, toolBarService1);
    this.keyDownListener = this.keyDown.bind(this);
    document.addEventListener('keypress', this.keyDownListener, false);
  }

  keyDown(event) {
    console.log('keypress EditToolBarComponent');
    if (event.ctrlKey && event.code === 'KeyM') {
      this.toolBarService1.misuraStyle = !this.toolBarService1.misuraStyle;
      this.toolBarService1.refreshStyle();
    } else if (event.ctrlKey && event.code === 'KeyB') {
      this.toolBarService1.refreshSnapOnAllLayers(true);
      const message = this.toolBarService1.snapActive ? this.gisCostants.messageSnapAbilitato : this.gisCostants.messageSnapDisabilitato;
      this.toastComponent.showWarningGenerico(message);
    }
  }

  ngOnInit() {
    this.subscription = this.mapService.getNewContestoEmitter()
      .subscribe((id) => this.clearInteractions(id));
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
    document.removeEventListener('keypress', this.keyDownListener);
  }

  clearInteractions(id: any) {
    if (!id) {
      this.toolBarService1.rimuoviListaInteraction(this.toolBarService1.editToolGroup, true, true);
    }
  }
}


