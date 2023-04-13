import { Component, Input } from '@angular/core';
import { EsitoFinaleM10O11 } from '../../../../../models/dettaglio-pagamento-psr';

@Component({
  selector: 'app-dettaglo-esito-finale-domanda-psr-m10-o11',
  templateUrl: './dettaglo-esito-finale-domanda-psr-m10-o11.component.html',
  styleUrls: ['./dettaglo-esito-finale-domanda-psr-m10-o11.component.css']
})
export class DettagloEsitoFinaleDomandaPsrM10O11Component {

  @Input()
  dettaglioEsitoFinale: EsitoFinaleM10O11;

  constructor() {
  }



}
