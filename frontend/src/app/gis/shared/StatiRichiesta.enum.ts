import { EnumBase } from "./enumBase";

export enum StatiRichiesta {
APERTA = 'APERTA',
IN_LAVORAZIONE = 'IN_LAVORAZIONE',
IN_MODIFICA = 'IN_MODIFICA',
LAVORABILE = 'LAVORABILE',
SOSPESA = 'SOSPESA',
CANCELLATA = 'CANCELLATA',
CONCLUSA = 'CONCLUSA',
TUTTE = ''
}

export class StatiRichiestaoDecode extends EnumBase {
    static lista = [
    { name: 'Tutte', value: '' },
    { name: 'Aperta', value: StatiRichiesta.APERTA },
    { name: 'Cancellata', value: StatiRichiesta.CANCELLATA },
    { name: 'Conclusa', value: StatiRichiesta.CONCLUSA },
    { name: 'Lavorabile', value: StatiRichiesta.LAVORABILE },
    { name: 'In lavorazione', value: StatiRichiesta.IN_LAVORAZIONE },
    { name: 'In modifica', value: StatiRichiesta.IN_MODIFICA },
    { name: 'Sospesa', value: StatiRichiesta.SOSPESA }
  ];

  static decode(input): string {
    return super.innerDecode(input, StatiRichiestaoDecode.lista);
  }
}