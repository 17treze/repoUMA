import { Component, Input } from '@angular/core';
import { CalcoloDegressivitaPremio } from '../../../../../models/dettaglio-pagamento-psr';

@Component({
  selector: 'app-calcolo-premio-m13-o11',
  templateUrl: './calcolo-premio-m13-o11.component.html',
  styleUrls: ['./calcolo-premio-m13-o11.component.css']
})
export class CalcoloPremioM13O11Component {

  @Input()
  calcoloPremio: CalcoloDegressivitaPremio;

  constructor() {
  }


}
