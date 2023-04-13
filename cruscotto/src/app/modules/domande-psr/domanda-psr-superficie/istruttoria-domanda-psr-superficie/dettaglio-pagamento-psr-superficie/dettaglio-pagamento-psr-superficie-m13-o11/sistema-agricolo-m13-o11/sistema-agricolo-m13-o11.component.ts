import { Component, Input } from '@angular/core';
import { SistemaAgricolo } from '../../../../../models/dettaglio-pagamento-psr';
import { RoundingSupport } from '../../../../../../../shared/utilities/rounding.support';

@Component({
  selector: 'app-sistema-agricolo-m13-o11',
  templateUrl: './sistema-agricolo-m13-o11.component.html',
  styleUrls: ['./sistema-agricolo-m13-o11.component.css']
})
export class SistemaAgricoloM13O11Component {

  @Input()
  sistemaAgricolo: SistemaAgricolo;

  round(value, format): number {
    return RoundingSupport.round(value, format);
  }

}
