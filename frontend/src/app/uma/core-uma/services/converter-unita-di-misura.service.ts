import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ConverterUnitaDiMisuraService {

  constructor() { }

  decodificaUnitaDiMisura(unitaDiMisura: string) {
    switch (unitaDiMisura.toUpperCase()) {
      case 'ARNIE':
        return 'Arnie';
      case 'KM':
        return 'Km';
      case 'UBA':
        return 'UBA';
      case 'MC':
        return 'Mc';
      case 'T':
        return 'T';
      case 'CAPO':
        return 'Capo';
      case 'MQ':
        return 'Mq';
      case 'HA':
        return 'Ha';
      case 'CVH':
        return 'CVh';
      case 'QLI':
        return 'Quintali';
      case 'QLI_LATTE':
        return 'Q.li Latte';
      case 'QLI_OLIVE':
        return 'Q.li Olive';
      case 'QLI_ACQUA':
        return 'Q.li Acqua';
      case 'QLI_PRODOTTO':
        return 'Q.li Prodotto';
      case 'NUM':
        return 'Num';
      case 'HL_VINO':
        return 'Hl Vino';
      case 'HL_ACQUA':
        return 'Hl Acqua';
      default:
        return unitaDiMisura || 'Quantita';
    }
  }

}
