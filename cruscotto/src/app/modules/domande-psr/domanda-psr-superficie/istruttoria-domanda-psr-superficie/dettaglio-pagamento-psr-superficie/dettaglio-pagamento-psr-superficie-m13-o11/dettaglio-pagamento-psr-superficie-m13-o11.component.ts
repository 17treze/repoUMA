import { Component, Input } from '@angular/core';
import { DettaglioPagamentoM1311 } from '../../../../models/dettaglio-pagamento-psr';

@Component({
  selector: 'app-dettaglio-pagamento-psr-superficie-m13-o11',
  templateUrl: './dettaglio-pagamento-psr-superficie-m13-o11.component.html',
  styleUrls: ['./dettaglio-pagamento-psr-superficie-m13-o11.component.css', '../dettaglio-pagamento-psr-superficie.component.scss']
})
export class DettaglioPagamentoPsrSuperficieM13O11Component {

  @Input()
  dettaglioPagamentoPsr: DettaglioPagamentoM1311;
}
