import { EnumBase } from "./enumBase";

export enum TagRilevato {
    VIT = 'VIT',
    PAS = 'PAS',
    FAG = 'FAG',
    FRU = 'FRU',
    OLE = 'OLE',
    BO = 'BO',
    NESSUN_FILTRO = ''
}

export class TagRilevatoDecode extends EnumBase {
    static lista = [
        { name: '', value: TagRilevato.NESSUN_FILTRO },
        { name: 'VIT - VITE', value: TagRilevato.VIT },
        { name: 'PAS - PASCOLO', value: TagRilevato.PAS },
        { name: 'FAG - FRUTTA A GUSCIO', value: TagRilevato.FAG },
        { name: 'FRU - FRUTTA', value: TagRilevato.FRU },
        { name: 'OLE - OLEICOLO', value: TagRilevato.OLE },
        { name: 'BO - GENERICA', value: TagRilevato.BO }
      ];

      static decode(input): string {
        return super.innerDecode(input, TagRilevatoDecode.lista);
      }
}
