import { Injectable } from '@angular/core';
import { StatoDomandaPsr } from '../models/stato-domanda-psr';
import { ImportoLiquidatoDomandaPsr } from '../models/importo-liquidato-domanda-psr';

@Injectable({
  providedIn: 'root'
})
export class IstruttoriaPsrSuperficieUtilsService {

  constructor() {
  }

  filterForStatoDomanda(statoDomanda: StatoDomandaPsr) {
    return (importoLiquidato) => {
      let codiceProdotto = importoLiquidato.codiceProdotto;
      if (this.isPat(importoLiquidato)) {
        codiceProdotto = codiceProdotto.substring(1);
      }
      return codiceProdotto.startsWith(statoDomanda.codOperazione.replace(/\./g, ''))
        && importoLiquidato.tipoPagamento.toLowerCase() === statoDomanda.tipoPagamento.toLowerCase();
    };
  }

  private isPat(importo: ImportoLiquidatoDomandaPsr): boolean {
    return importo.numeroDomanda.length === 15;
  }

  canShowCalculatedAmount(statoDomandaPsr: StatoDomandaPsr): boolean {
    return ['ANTICIPO', 'SALDO', 'INTEGRAZIONE'].includes(statoDomandaPsr.tipoPagamento.toUpperCase()) &&
      (statoDomandaPsr.stato.toLowerCase() === 'liquidata' || statoDomandaPsr.stato.toLowerCase() === 'liquidabile');
  }
}
