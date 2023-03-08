import { PoligoniDichiaratiEvent } from './../../shared/Poligoni-dichiarati/poligoni-dichiarati-event';
import { RichiestaModificaSuoloService } from './../../services/richiesta-modifica-suolo.service';
import { Component, EventEmitter, Input,  OnInit, Output, ViewEncapsulation } from '@angular/core';
import { MapService } from '../mappa/map.service';

@Component({
  selector: 'gis-suolo-dichiarato',
  templateUrl: './suolo-dichiarato.component.html',
  styleUrls: ['./suolo-dichiarato.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class SuoloDichiaratoComponent implements OnInit {
  @Input() suoloDichiarato: any;
  @Input() contextAction: string;
  @Input() index: number;
  @Input() activeOpenPanelDettaglioRichiesta: boolean = false;
  @Output() actionSuoloDichiarato = new EventEmitter<any>();
  params: { pagina: number; numeroElementiPagina: number; };

  constructor(private mapService: MapService, private richiestaModificaSuoloService: RichiestaModificaSuoloService,
    public poligoniDichiaratiEvent: PoligoniDichiaratiEvent) { }

  ngOnInit() {
  }

  addSuoloDichiaratoToLavorazione(idSuolo: number, indice: number) {
    this.actionSuoloDichiarato.emit({ 'action': 'add', 'idSuolo': idSuolo, 'index': indice });
  }

  apriDettaglioRichiestaModificaSuolo(idRichiesta: number, indice: number) {
    this.richiestaModificaSuoloService.loadDichiarati(idRichiesta);
    this.actionSuoloDichiarato.emit({ 'action': 'openDetailRichiesta', 'idRichiesta': idRichiesta, 'index': indice });
  }

  removeSuoloDichiaratoFromLavorazione(idSuolo: number, indice: number) {
    this.actionSuoloDichiarato.emit({ 'action': 'remove', 'idSuolo': idSuolo, 'index': indice });
  }

  centerMap(extent) {
    this.mapService.centerMap(extent);
  }

  openDialogDichiarati(suoloDichiarato) {
    this.richiestaModificaSuoloService.loadDichiaratoID(suoloDichiarato.idRichiesta, suoloDichiarato.id);
    this.poligoniDichiaratiEvent['poligoni'].forEach(element => {
      if (element['interventoInizio']) {
        element['interventoInizio'] = new Date(element['interventoInizio']);
      }
      if (element['interventoFine']) {
        element['interventoFine'] = new Date(element['interventoFine']);
      }
    });
    this.poligoniDichiaratiEvent.showDialog = true;
    this.poligoniDichiaratiEvent.idDettaglioRichiesta = suoloDichiarato.idRichiesta;
    this.poligoniDichiaratiEvent.statoRichiesta = suoloDichiarato.statoRichiesta;
  }
}
