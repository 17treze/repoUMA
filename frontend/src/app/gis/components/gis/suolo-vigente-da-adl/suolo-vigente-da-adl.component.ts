import { MapService } from './../../mappa/map.service';
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';

@Component({
  selector: 'gis-suolo-vigente-da-adl',
  templateUrl: './suolo-vigente-da-adl.component.html',
  styleUrls: ['./suolo-vigente-da-adl.component.css']
})
export class SuoloVigenteDaAdlComponent implements OnInit {
  @Input() suoloVigenteDaAdl: any;
  @Input() index: number;
  constructor(private mapService: MapService) { }

  ngOnInit() {
  }

  centerMap(extent) {
    this.mapService.centerMap(extent);
  }

}
