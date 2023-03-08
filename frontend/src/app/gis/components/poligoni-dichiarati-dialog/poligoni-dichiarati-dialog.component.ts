import { RichiestaModificaSuoloService } from './../../services/richiesta-modifica-suolo.service';
import { Dialog } from 'primeng/dialog';
import { PoligoniDichiaratiEvent } from './../../shared/Poligoni-dichiarati/poligoni-dichiarati-event';
import { Component, Input, OnInit, ViewEncapsulation } from '@angular/core';
import { AllegatiRichiestaGisService } from '../../services/allegati-richiesta-gis.service';
@Component({
  selector: 'gis-poligoni-dichiarati-dialog',
  templateUrl: './poligoni-dichiarati-dialog.component.html',
  styleUrls: ['./poligoni-dichiarati-dialog.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class PoligoniDichiaratiDialogComponent implements OnInit {
  @Input() detailResults: any;
  @Input() profiloUtente: any;
  public allegatiService: AllegatiRichiestaGisService; 

  constructor(public poligoniDichiaratiEvent: PoligoniDichiaratiEvent,
    private richiestaModificaSuoloService: RichiestaModificaSuoloService) { }

  ngOnInit() {
  }

  closeDialog() {
    this.poligoniDichiaratiEvent.showDialog = false;
    this.poligoniDichiaratiEvent.poligoniModificati = [];
    // reset dei campi
    this.poligoniDichiaratiEvent.poligoni.forEach(el => {
          el['tipoInterventoColturale'] = null;
          el['visibileInOrtofoto'] = false;
          el['interventoInizio'] = false;
          el['interventoFine'] = false;
      });
    // ricarico la lista
    this.richiestaModificaSuoloService.loadDichiarati(this.poligoniDichiaratiEvent.poligoni[0]['idRichiesta']);
  }

  minimize() {
    this.poligoniDichiaratiEvent.showDialog = false;
  }

  maximize(event, dialog: Dialog) {
    // dialog.maximized = false;
    dialog.maximize();
  }
}
