import { MapEvent } from './../../shared/MapEvent';
import { AfterViewInit, Component, ElementRef, EventEmitter, HostListener, Input, Output, ViewEncapsulation } from '@angular/core';
import { View, Map } from 'ol';
import { Extent } from 'ol/extent';
import Projection from 'ol/proj/Projection';
import { GisCostants } from '../../shared/gis.constants';
@Component({
  selector: 'gis-mappa',
  templateUrl: './mappa.component.html',
  styleUrls: ['./mappa.component.css', 'custom-ol-ext.css'],
  encapsulation: ViewEncapsulation.None
})
export class MappaComponent implements AfterViewInit {
  @Input() center: any;
  @Input() zoom: number;

  view: View;
  projection: Projection;
  extent: Extent = [-1877994.66, 3932281.56, 836715.13, 9440581.95];
  Map: Map;
  @Output() mapReady = new EventEmitter<Map>();

  rootMap: HTMLBodyElement;
  constructor(public mapEvent: MapEvent, private gisConstants: GisCostants, private elementRef: ElementRef, ) {
  }
  // conferma per pulsanti BACK, CLOSE, F5, HOME(breadcrumbs)
  @HostListener('window:beforeunload', ['$event']) beforeUnloadHandler(event: Event) {
    if (this.mapEvent.hasNotSavedAction()) {
      event.returnValue = 'Uscita dal sito' as any;
    }
  }
  ngAfterViewInit(): void {
  }
}
