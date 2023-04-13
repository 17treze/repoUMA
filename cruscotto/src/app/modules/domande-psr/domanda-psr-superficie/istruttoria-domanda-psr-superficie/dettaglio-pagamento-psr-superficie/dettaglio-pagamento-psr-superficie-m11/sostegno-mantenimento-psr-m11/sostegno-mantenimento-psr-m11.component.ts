import { Component, Input } from '@angular/core';
import { DettaglioSostegnoMantenimentoPsr } from '../../../../../models/dettaglio-pagamento-psr';
import { RoundingSupport } from '../../../../../../../shared/utilities/rounding.support';

@Component({
  selector: 'app-sostegno-mantenimento-psr-m11',
  templateUrl: './sostegno-mantenimento-psr-m11.component.html',
  styleUrls: ['./sostegno-mantenimento-psr-m11.component.css']
})
export class SostegnoMantenimentoPsrM11Component {

  @Input()
  sostegnoMantenimento: DettaglioSostegnoMantenimentoPsr;

  round(value, format): number {
    return RoundingSupport.round(value, format);
  }

}
