import { FabbricatoDto } from './FabbricatoDto';
import { LavorazioneDto } from './LavorazioneDto';
export class RaggruppamentoLavorazioneDto {
    indice: number;
    nome: string;
    superficieMassima?: number;        /** solo per superfici */
    fabbricati?: Array<FabbricatoDto>; /** solo per fabbricati */
    lavorazioni: Array<LavorazioneDto>;
}
