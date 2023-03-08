import { EnumBase } from "./enumBase";

export enum EsitoLavorazioneDichiarato {
    DA_LAVORARE = 'DA_LAVORARE',
    APPROVATO = 'APPROVATO',
    RESPINTO = 'RESPINTO',
    PARZIALE = 'PARZIALE'
}

export class EsitoLavorazioneDichiaratoDecode extends EnumBase {
       static lista = [
        { name: 'DA LAVORARE', value: EsitoLavorazioneDichiarato.DA_LAVORARE },
        { name: 'APPROVATO', value: EsitoLavorazioneDichiarato.APPROVATO },
        { name: 'RESPINTO', value: EsitoLavorazioneDichiarato.RESPINTO },
        { name: 'PARZIALE', value: EsitoLavorazioneDichiarato.PARZIALE }
    ];

    static decode(input): string {
      return super.innerDecode(input, EsitoLavorazioneDichiaratoDecode.lista);
    }
}
