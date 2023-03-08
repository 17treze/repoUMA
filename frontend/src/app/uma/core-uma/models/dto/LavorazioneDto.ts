import { TipologiaLavorazioneEnum } from '../enums/TipologiaLavorazione.enum';

export class LavorazioneDto {
    id: number;
    indice: number;
    nome: string;
    unitaDiMisura: string;
    tipologia: TipologiaLavorazioneEnum;
}