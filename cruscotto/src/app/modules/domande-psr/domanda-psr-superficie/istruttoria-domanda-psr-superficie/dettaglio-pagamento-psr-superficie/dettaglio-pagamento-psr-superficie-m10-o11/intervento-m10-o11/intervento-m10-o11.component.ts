import { Component, Input } from '@angular/core';
import { InterventoM10O11 } from '../../../../../models/dettaglio-pagamento-psr';
import { RoundingSupport } from '../../../../../../../shared/utilities/rounding.support';

@Component({
  selector: 'app-intervento-m10-o11',
  templateUrl: './intervento-m10-o11.component.html',
  styleUrls: ['./intervento-m10-o11.component.scss']
})
export class InterventoM10O11Component {
  @Input()
  intervento: InterventoM10O11;

  round(value, format): number {
    return RoundingSupport.round(value, format);
  }
}
