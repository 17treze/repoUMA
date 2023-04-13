import { Component, Input } from '@angular/core';
import { DettaglioPagamentoM10O11 } from '../../../../models/dettaglio-pagamento-psr';

@Component({
  selector: 'app-dettaglio-pagamento-psr-superficie-m10-o11',
  templateUrl: './dettaglio-pagamento-psr-superficie-m10-o11.component.html',
  styleUrls: ['./dettaglio-pagamento-psr-superficie-m10-o11.component.scss', '../dettaglio-pagamento-psr-superficie.component.scss']
})
export class DettaglioPagamentoPsrSuperficieM10O11Component {
  @Input()
  dettaglioPagamentoPsr: DettaglioPagamentoM10O11;

}
