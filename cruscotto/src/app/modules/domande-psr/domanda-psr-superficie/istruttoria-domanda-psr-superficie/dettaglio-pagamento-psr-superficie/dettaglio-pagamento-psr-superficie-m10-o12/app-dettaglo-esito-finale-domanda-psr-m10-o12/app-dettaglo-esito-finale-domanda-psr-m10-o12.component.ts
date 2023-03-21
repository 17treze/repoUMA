import { Component, Input } from '@angular/core';
import { EsitoFinaleM10O12 } from '../../../../../models/dettaglio-pagamento-psr';

@Component({
  selector: 'app-app-dettaglo-esito-finale-domanda-psr-m10-o12',
  templateUrl: './app-dettaglo-esito-finale-domanda-psr-m10-o12.component.html',
  styleUrls: ['./app-dettaglo-esito-finale-domanda-psr-m10-o12.component.css']
})
export class AppDettagloEsitoFinaleDomandaPsrM10O12Component {

  @Input()
  dettaglioEsitoFinale: EsitoFinaleM10O12;
}
