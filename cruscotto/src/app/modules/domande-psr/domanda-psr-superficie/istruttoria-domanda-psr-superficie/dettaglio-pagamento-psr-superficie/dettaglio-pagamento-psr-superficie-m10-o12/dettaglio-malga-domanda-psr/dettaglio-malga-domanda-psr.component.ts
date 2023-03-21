import { Component, Input } from '@angular/core';
import { DettaglioMalga } from '../../../../../models/dettaglio-pagamento-psr';
import { RoundingSupport } from '../../../../../../../shared/utilities/rounding.support';

@Component({
  selector: 'app-dettaglio-malga-domanda-psr',
  templateUrl: './dettaglio-malga-domanda-psr.component.html',
  styleUrls: ['./dettaglio-malga-domanda-psr.component.css']
})
export class DettaglioMalgaDomandaPsrComponent {

  @Input()
  dettaglioMalga: DettaglioMalga;

  round(value, format): number {
    return RoundingSupport.round(value, format);
  }
}
