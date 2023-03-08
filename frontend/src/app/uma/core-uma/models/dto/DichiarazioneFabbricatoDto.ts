import { DichiarazioneDto } from './DichiarazioneDto';

export interface DichiarazioneFabbricatoDto {
    idFabbricato: number;
    dichiarazioni: Array<DichiarazioneDto>;
}