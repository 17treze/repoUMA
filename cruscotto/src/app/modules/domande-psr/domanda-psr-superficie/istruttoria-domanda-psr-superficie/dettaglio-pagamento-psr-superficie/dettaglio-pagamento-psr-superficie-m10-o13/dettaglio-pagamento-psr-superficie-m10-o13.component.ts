import { Component, Input } from '@angular/core';
import { DettaglioPagamentoM10O13 } from '../../../../models/dettaglio-pagamento-psr';

@Component({
  selector: 'app-dettaglio-pagamento-psr-superficie-m10-o13',
  templateUrl: './dettaglio-pagamento-psr-superficie-m10-o13.component.html',
  styleUrls: ['./dettaglio-pagamento-psr-superficie-m10-o13.component.css', '../dettaglio-pagamento-psr-superficie.component.scss']
})
export class DettaglioPagamentoPsrSuperficieM10O13Component {
  @Input()
  dettaglioPagamentoPsr: DettaglioPagamentoM10O13;
}
