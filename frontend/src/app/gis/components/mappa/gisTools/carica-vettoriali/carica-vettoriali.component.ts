import { ToolBarService } from './../../../../shared/ToolBar/toolBar.service';
import { PropertyLayer } from './../../../../shared/PropertyLayer.enum';
import { RightToolBarEvent } from './../../../../shared/RightToolBar/RightToolBarEvent';
import { PanelEvent } from './../../../../shared/PanelEvent';
import { Component, EventEmitter, HostListener, OnInit, Output } from '@angular/core';
import { MapEvent } from '../../../../shared/MapEvent';

@Component({
  selector: 'gis-carica-vettoriali',
  templateUrl: './carica-vettoriali.component.html',
  styleUrls: ['./carica-vettoriali.component.css']
})
export class CaricaVettorialiComponent implements OnInit {
  constructor(public mapEvent: MapEvent, public panelEvent: PanelEvent,
    public rightToolbarEvent: RightToolBarEvent, private toolbarService: ToolBarService) { }

  ngOnInit() {
  }

  openCaricaVettoriali() {
    this.panelEvent.showUploadVettoriali = true;
  }
}
