import { FabbisognoDto } from './FabbisognoDto';

export interface DichiarazioneDto {
    lavorazioneId: number;
    fabbisogni: Array<FabbisognoDto>;
}