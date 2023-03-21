import { Component, Input } from '@angular/core';
import { DettaglioPagamentoM10O14 } from '../../../../models/dettaglio-pagamento-psr';

@Component({
  selector: 'app-dettaglio-pagamento-psr-superficie-m10-o14',
  templateUrl: './dettaglio-pagamento-psr-superficie-m10-o14.component.html',
  styleUrls: ['./dettaglio-pagamento-psr-superficie-m10-o14.component.css', '../dettaglio-pagamento-psr-superficie.component.scss']
})
export class DettaglioPagamentoPsrSuperficieM10O14Component {
  @Input()
  dettaglioPagamentoPsr: DettaglioPagamentoM10O14;
}
