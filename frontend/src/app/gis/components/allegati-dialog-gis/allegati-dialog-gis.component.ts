import { Component, Input, OnInit } from '@angular/core';
import { AllegatiGisService } from '../../services/allegati-gis.service';
import { AllegatiRichiestaGisService } from '../../services/allegati-richiesta-gis.service';
import { AllegatiEvent } from '../../shared/AllegatiEvent';

@Component({
  selector: 'allegati-dialog-gis',
  templateUrl: './allegati-dialog-gis.component.html',
  styleUrls: ['./allegati-dialog-gis.component.css'],
  providers: [{ provide: AllegatiGisService, useClass: AllegatiRichiestaGisService }]
})
export class AllegatiDialogGisComponent implements OnInit {
  @Input() detailResults: any;
  @Input() profiloUtente: any;
  initFile: boolean = true;
  allegati: any;
  public tipoAllegato: string = "richiesta";

  constructor(
    public allegatiEvent: AllegatiEvent
  ) { }

  ngOnInit() {
  }

  setInitAllegato(val) {
    this.initFile = val;
    return val;
  }

  closeDialog() {
    this.allegatiEvent.displayAllegati = false;
    this.setInitAllegato(true);
  }

}
