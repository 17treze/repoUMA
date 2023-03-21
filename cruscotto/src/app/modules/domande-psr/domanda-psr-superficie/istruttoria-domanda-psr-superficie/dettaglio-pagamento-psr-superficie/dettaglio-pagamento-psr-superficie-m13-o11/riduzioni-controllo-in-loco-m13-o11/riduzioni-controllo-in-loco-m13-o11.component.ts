import { Component, Input } from '@angular/core';
import { RiduzioniControlloInLoco } from '../../../../../models/dettaglio-pagamento-psr';

@Component({
  selector: 'app-riduzioni-controllo-in-loco-m13-o11',
  templateUrl: './riduzioni-controllo-in-loco-m13-o11.component.html',
  styleUrls: ['./riduzioni-controllo-in-loco-m13-o11.component.css']
})
export class RiduzioniControlloInLocoM13O11Component {

  @Input()
  data: RiduzioniControlloInLoco;

  constructor() {
  }


}
