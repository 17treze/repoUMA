import { Component, Input } from '@angular/core';
import { DatiAziendali } from '../../../../../models/dettaglio-pagamento-psr';

@Component({
  selector: 'app-dati-aziendali-m13-o11',
  templateUrl: './dati-aziendali-m13-o11.component.html',
  styleUrls: ['./dati-aziendali-m13-o11.component.css']
})
export class DatiAziendaliM13O11Component {

  @Input()
  datiAziendali: DatiAziendali;

  constructor() {
  }



}
