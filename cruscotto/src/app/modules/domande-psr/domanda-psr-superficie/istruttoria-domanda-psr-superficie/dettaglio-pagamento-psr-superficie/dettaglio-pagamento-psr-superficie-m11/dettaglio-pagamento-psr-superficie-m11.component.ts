import { Component, Input } from '@angular/core';
import { DettaglioPagamentoM11 } from '../../../../models/dettaglio-pagamento-psr';

@Component({
  selector: 'app-dettaglio-pagamento-psr-superficie-m11',
  templateUrl: './dettaglio-pagamento-psr-superficie-m11.component.html',
  styleUrls: ['./dettaglio-pagamento-psr-superficie-m11.component.css', '../dettaglio-pagamento-psr-superficie.component.scss']
})
export class DettaglioPagamentoPsrSuperficieM11Component {
  @Input()
  dettaglioPagamentoPsr: DettaglioPagamentoM11;

}
