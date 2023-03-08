import { Component, Host, OnInit, Optional, ViewEncapsulation } from '@angular/core';
import { SelectItem } from 'primeng-lts';
import { MapEvent } from '../../shared/MapEvent';
import { MapService } from '../mappa/map.service';
import OlMap from 'ol/Map';
import { MapidService } from '../mappa/mapid.service';
@Component({
  selector: 'gis-bottombar',
  templateUrl: './bottombar.component.html',
  styleUrls: ['./bottombar.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class BottomBarComponent implements OnInit {
  constructor(private mapService: MapService,
    @Host()
    @Optional()
    private mapidService: MapidService) {
  }

  ngOnInit() {
  }


}
