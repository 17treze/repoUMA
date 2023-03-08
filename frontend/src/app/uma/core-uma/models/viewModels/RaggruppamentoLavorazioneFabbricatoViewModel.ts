import { LavorazioneFabbricatiViewModel } from './LavorazioneFabbricatiViewModel';

export interface RaggruppamentoLavorazioneFabbricatoViewModel {
    indice: number;
    nome: string;
    descrizioneCompleta: string;                /** es: 1 Mais e Sorgo, 2 Essicazione, ecc */
    lavorazioni: Array<LavorazioneFabbricatiViewModel>;
}