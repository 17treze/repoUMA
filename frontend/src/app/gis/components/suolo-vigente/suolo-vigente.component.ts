import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { MapService } from '../mappa/map.service';
import {LavorazioniEvent} from "../../shared/LavorazioniEvent";

@Component({
  selector: 'gis-suolo-vigente',
  templateUrl: './suolo-vigente.component.html',
  styleUrls: ['./suolo-vigente.component.css']
})
export class SuoloVigenteComponent implements OnInit {
  @Input() suoloVigente: any;
  @Input() contextAction: string;
  @Input() index :number;
  @Output() actionSuoloVigente = new EventEmitter<any>();
  constructor(private mapService: MapService, public lavorazioniEvent: LavorazioniEvent) { }

  ngOnInit() {
  }


  removePoligonoSuoloFromLavorazione(idSuolo: number, indice: number) {
    this.actionSuoloVigente.emit({ 'action': 'remove', 'idSuolo': idSuolo,'index':indice});
  }

  centerMap(extent) {
    this.mapService.centerMap(extent);
  }

}
