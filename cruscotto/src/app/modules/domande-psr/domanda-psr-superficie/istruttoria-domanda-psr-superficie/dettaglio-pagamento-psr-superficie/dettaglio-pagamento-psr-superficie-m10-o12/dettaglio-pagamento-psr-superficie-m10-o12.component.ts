import { Component, Input } from '@angular/core';
import { DettaglioPagamentoM10O12 } from '../../../../models/dettaglio-pagamento-psr';

@Component({
  selector: 'app-dettaglio-pagamento-psr-superficie-m10-o12',
  templateUrl: './dettaglio-pagamento-psr-superficie-m10-o12.component.html',
  styleUrls: ['./dettaglio-pagamento-psr-superficie-m10-o12.component.css', '../dettaglio-pagamento-psr-superficie.component.scss']
})
export class DettaglioPagamentoPsrSuperficieM10O12Component {
  @Input()
  dettaglioPagamentoPsr: DettaglioPagamentoM10O12;
}
