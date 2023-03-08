import { TipoCarburante } from '../enums/TipoCarburante.enum';
import { TipologiaLavorazioneEnum } from '../enums/TipologiaLavorazione.enum';

export interface LavorazioneViewModel {
    id: number;
    indice: number;
    descrizioneCompleta: string;
    tipo: TipologiaLavorazioneEnum;
    unitaDiMisura: string;
    carburante?: keyof typeof TipoCarburante;
    GASOLIO?: string;
    BENZINA?: string;
    superficieMassima?: number;
}


