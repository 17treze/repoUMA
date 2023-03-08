import { AlimentazioneEnum } from './../../enums/dotazione.-tecnica/Alimentazione.enum';
export interface MotoreDto {
    marca: string;
    tipo: string;
    alimentazione: keyof typeof AlimentazioneEnum;
    potenza: string;
    matricola: string;
}