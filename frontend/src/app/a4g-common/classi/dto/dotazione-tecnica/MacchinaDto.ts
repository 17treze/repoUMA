import { AlimentazioneEnum } from '../../enums/dotazione.-tecnica/Alimentazione.enum';

export interface MacchinaDto {
    id: number;
    tipologia: string;                              // descrizione tipologia macchina
    modello: string;
    targa: string;
    alimentazione: keyof typeof AlimentazioneEnum; // presente solo se macchina a motore
}