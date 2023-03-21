import { Component, Input } from '@angular/core';
import { EsitoFinaleDettaglioPagamentoPsr11, } from '../../../../../models/dettaglio-pagamento-psr';

@Component({
  selector: 'app-esito-finale-psr-m11',
  templateUrl: './esito-finale-psr-m11.component.html',
  styleUrls: ['./esito-finale-psr-m11.component.css']
})
export class EsitoFinalePsrM11Component {

  @Input()
  dettaglioEsitoFinale: EsitoFinaleDettaglioPagamentoPsr11;

  constructor() {
  }



}
