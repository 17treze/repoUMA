import { Component, Input } from '@angular/core';
import { EsitoFinaleDettaglioPagamentoEsitoPsrM1311, } from '../../../../../models/dettaglio-pagamento-psr';

@Component({
  selector: 'app-esito-finale-m13-o11',
  templateUrl: './esito-finale-m13-o11.component.html',
  styleUrls: ['./esito-finale-m13-o11.component.css']
})
export class EsitoFinaleM13O11Component {

  @Input()
  dettaglioEsitoFinale: EsitoFinaleDettaglioPagamentoEsitoPsrM1311;

  constructor() {
  }



}
