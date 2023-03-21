import { Component, Input } from '@angular/core';
import { PsrFattura } from '../../../models/dettaglio-istruttoria-srt';

@Component({
  selector: 'app-storico-fattura',
  templateUrl: './storico-fattura.component.html',
  styleUrls: ['./storico-fattura.component.css']
})
export class StoricoFatturaComponent {
  @Input() fattura: PsrFattura;

  get contributoRichiesto(): number {
    return this.fattura.spesaRichiesta * (this.fattura.quotaContributoRichiesto / 100);
  }

  get contributoAmmesso(): number {
    return this.fattura.spesaAmmessa * (this.fattura.quotaContributoRichiesto / 100);
  }
}
