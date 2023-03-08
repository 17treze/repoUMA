import { MapEvent } from './../../../../shared/MapEvent';
import {Component, OnInit} from '@angular/core';
import {DragZoom} from 'ol/interaction';
import {altKeyOnly} from 'ol/events/condition';


@Component({
  selector: 'gis-dragZoom',
  templateUrl: './dragZoom.component.html',
  styleUrls: ['./dragZoom.component.css']
})
export class DragZoomComponent implements OnInit {

  constructor(public mapEvent: MapEvent) { }

  ngOnInit() {
    const dragZoom = new DragZoom({
      condition: altKeyOnly, duration: 0
    });
    this.mapEvent.map.addInteraction(dragZoom);
  }

}
