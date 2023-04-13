import { Component, Input, OnChanges } from '@angular/core';
import { DomandaPsr } from '../../../../models/domanda-psr';
import { DateService } from '../../../../../../shared/utilities/date.service';

@Component({
  selector: 'app-header-dettaglio-pagamento-psr-superficie',
  templateUrl: './header-dettaglio-pagamento-psr-superficie.component.html',
  styleUrls: ['./header-dettaglio-pagamento-psr-superficie.component.scss']
})
export class HeaderDettaglioPagamentoPsrSuperficieComponent implements OnChanges {

  @Input()
  domandaPsr: DomandaPsr;
  datiProtocollo: { numero: number; data: string };

  constructor(private dateService: DateService) {
  }

  ngOnChanges(): void {
    if (this.domandaPsr) {
      const dataProtocollazione = this.domandaPsr.dataPresentazione;
      this.datiProtocollo = {
        numero: this.domandaPsr.numeroDomanda,
        data: this.dateService.toIsoDate(dataProtocollazione)
      };
    }
  }
}
