import { GisCostants } from './../../../../shared/gis.constants';
import { Component, OnInit, ViewEncapsulation } from '@angular/core';
import { MapEvent } from '../../../../shared/MapEvent';
import { MapService } from '../../map.service';
import {DropdownModule} from 'primeng-lts/dropdown';
import { ScalaCostants } from './scala.costants';
interface Scala {
  label: string;
  value: string;
}
@Component({
  selector: 'gis-scala',
  templateUrl: './scala.component.html',
  styleUrls: ['./scala.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class ScalaComponent implements OnInit {
  labelScale: any;
  labelSphere: any;
  round: null;
  linearScale = true;
  valoriScala: any;
  filteredScales: any[];
  selectedScale: any;
  previousScale: string;
  constructor(private mapEvent: MapEvent, private mapService: MapService, private scalaCostants: ScalaCostants) {
    this.valoriScala = this.scalaCostants.data;
   }

  ngOnInit() {
    const map = this.mapEvent.map;
    const _self = this;
    this.linearScale = this.mapService.linearProjection;
    // let currRes = map.getView().getResolution();
    // let currZoom = map.getView().getZoom();

    map.on('moveend', function (e) {
      /*const newZoom = map.getView().getZoom();
      if (currZoom !== newZoom) {
        console.log('zoom end, new zoom: ' + newZoom);
        currZoom = newZoom;
      }
      const newRes = map.getView().getResolution();
      if (currRes !== newRes) {
        console.log('resolution end, new resolution: ' + newRes);
        currRes = newRes;
      }*/
      const formatScale = _self.formatScale(MapService.getMapScale(map, _self.linearScale), _self.round);
      _self.labelScale = formatScale.replace('/', ':'); // sostituisco slash con due punti
      _self.previousScale = _self.labelScale.split(':')[1].trim(); // recupero il valore senza i due punti
      _self.selectedScale = {'label':  _self.labelScale, 'value':  _self.labelScale};

      // _self.labelSphere = MapService.formatScale(MapService.getMapScale(map, dpi, false));
    });
  }

  filterScales(event) {
    let filtered: any[] = [];
    let query = event.query;
    for (let i = 0; i < this.valoriScala.length; i++) {
      let scala = this.valoriScala[i];
      if (scala.label.toLowerCase().indexOf(query.toLowerCase()) === 0) {
        filtered.push(scala);
      }
    }

    this.filteredScales = filtered;
  }

  doOnSelect(event) {
    this.labelScale = event.value;
    MapService.setMapScale(this.mapEvent.map, this.labelScale, this.linearScale);
  }

  onChangeScaleInput(event) {
    const map = this.mapEvent.map;
    let value = event;
    if (event && event.value) {
      value = event.value;
    } else if (event === '' || !event) {
      // se il campo è vuoto viene popolato con l'ultimo valore inserito
      this.selectedScale = {'label': this.previousScale, 'value': this.previousScale};
      MapService.setMapScale(map, this.previousScale, this.linearScale);
    } else {
        if (value.indexOf(':') > -1) {
          value = value.split(':')[1].trim();
        }
        this.labelScale = value;
        this.selectedScale = {'label': this.labelScale, 'value': this.labelScale};
        MapService.setMapScale(map, this.labelScale, this.linearScale);
    }
  }

  formatScale(d: number, round: number): string {
   if (round) {
      const e = Math.pow(10, round);
      if (d > e) {
        d = Math.round(d / e) * e;
      } else {
        d = Math.round(d);
      }
    } else {
      d = Math.round(d);
    }

    d = Math.round(d);

    return '1 / ' + d.toLocaleString();
  }
}
