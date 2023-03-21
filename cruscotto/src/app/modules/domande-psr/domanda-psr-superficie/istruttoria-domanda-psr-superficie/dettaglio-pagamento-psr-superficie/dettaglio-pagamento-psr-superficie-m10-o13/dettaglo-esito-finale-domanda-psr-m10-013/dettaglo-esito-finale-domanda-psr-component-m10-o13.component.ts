import { Component, Input } from '@angular/core';
import { EsitoFinaleDettaglioPagamentoPsrM10O13 } from '../../../../../models/dettaglio-pagamento-psr';

@Component({
  selector: 'app-dettaglo-esito-finale-domanda-psr-m10-o13',
  templateUrl: './dettaglo-esito-finale-domanda-psr-component-m10-o13.component.html',
  styleUrls: ['./dettaglo-esito-finale-domanda-psr-component-m10-o13.component.css']
})
export class DettagloEsitoFinaleDomandaPsrM10O13Component {

  @Input()
  dettaglioEsitoFinale: EsitoFinaleDettaglioPagamentoPsrM10O13;

  constructor() {
  }



}
