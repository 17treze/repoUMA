import { EnumBase } from "./enumBase";

export enum TagDichiarato {
    VIT = 'VIT',
    PAS = 'PAS',
    FAG = 'FAG',
    FRU = 'FRU',
    OLE = 'OLE',
    BO = 'BO',
    NESSUN_FILTRO = ''
}

export class TagDichiaratoDecode extends EnumBase {
    static lista = [
        { name: '', value: TagDichiarato.NESSUN_FILTRO },
        { name: 'VIT - VITE', value: TagDichiarato.VIT },
        { name: 'PAS - PASCOLO', value: TagDichiarato.PAS },
        { name: 'FAG - FRUTTA A GUSCIO', value: TagDichiarato.FAG },
        { name: 'FRU - FRUTTA', value: TagDichiarato.FRU },
        { name: 'OLE - OLEICOLO', value: TagDichiarato.OLE },
        { name: 'BO - GENERICA', value: TagDichiarato.BO }
      ];

  static decode(input): string {
    return super.innerDecode(input, TagDichiaratoDecode.lista);
  }
}

