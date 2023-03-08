import { LavorazioneViewModel } from './LavorazioneVIewModel';

export interface RaggruppamentoLavorazioneViewModel {
    indice: number;
    nome: string;
    superficieMassima?: number;
    descrizioneCompleta: string;                /** es: 1 Mais e Sorgo, 2 Essicazione, ecc */
    lavorazioni: Array<LavorazioneViewModel>;
}