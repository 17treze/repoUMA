import { Component, Input, Output, EventEmitter } from '@angular/core';
import { DatiAggregatiIstruttoriaModel } from './models/dati-aggregati-istruttoria.model';
import { SostegnoDu } from '../../classi/SostegnoDu';
import { TipoIstruttoriaEnum } from '../../classi/TipoIstruttoriaEnum';

@Component({
  selector: 'app-popup-dati-aggregati-istruttoria',
  templateUrl: 'popup-dati-aggregati-istruttoria.component.html'
})
export class PopupDatiAggregatiIstruttoriaComponent {

  public datiAggregatiModel: DatiAggregatiIstruttoriaModel[] = undefined;
  public tipoSostegno: SostegnoDu;

  @Input() public popupVisibile = false;
  @Input('tipoSostegno') set setTipoSostegno(tipoSostegno: string) {
    this.tipoSostegno = SostegnoDu[tipoSostegno];
  }
  @Input() public tipoIstruttoria: TipoIstruttoriaEnum;
  @Input() public annoCampagna: number;
  @Output() public chiudiPopup = new EventEmitter();
  
  
  constructor() {
  }

  public setDatiAggregatiModel(datiAggregatiIstruttoriaModel: DatiAggregatiIstruttoriaModel[]) {
    this.datiAggregatiModel = datiAggregatiIstruttoriaModel;
  }
  
  public closePopup() {
    this.chiudiPopup.emit();
  }  
}
