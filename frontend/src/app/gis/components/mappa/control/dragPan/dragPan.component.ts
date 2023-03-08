import { MapEvent } from './../../../../shared/MapEvent';
import { Component, OnInit } from '@angular/core';
import {DragPan} from 'ol/interaction';
import {platformModifierKeyOnly} from 'ol/events/condition';

@Component({
  selector: 'gis-dragPan',
  templateUrl: './dragPan.component.html',
  styleUrls: ['./dragPan.component.css']
})
export class DragPanComponent implements OnInit {

  constructor(public mapEvent: MapEvent) { }

  ngOnInit() {
    const dragPan =
    new DragPan({
      condition: function (event) {
        return this.getPointerCount() === 2 || platformModifierKeyOnly(event) || event.originalEvent.which === 2;
      }
    });

    this.mapEvent.map.addInteraction(dragPan);

  }

}
