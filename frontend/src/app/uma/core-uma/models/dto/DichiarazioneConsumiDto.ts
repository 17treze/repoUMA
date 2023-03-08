import { CarburanteDto } from './CarburanteDto';

export interface DichiarazioneConsumiDto {
    id: number;
    idRichiesta: number;
    campagnaRichiesta: number;
    cuaa: string;
    cfRichiedente: string;
    denominazione: string;
    dataPresentazione: Date;
    dataProtocollazione: Date;
    dataLimitePrelievi: Date;
    haPrelieviOltreLimite: boolean;
    protocollo: string;
    stato: string;
    dataConduzione: string | Date | any;
    rimanenza: CarburanteDto;
    motivazioneAccisa?: string;
}

export interface DichiarazioneConsumiPatchDto {
    dataConduzione: string | Date | any;
    motivazioneAccisa?: string;
}