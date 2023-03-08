import { FonteImpresaAgricola } from './FonteImpresaAgricolaEnum';
import { SedeLegaleDto } from './SedeLegaleDto';

export class ImpresaDto {
    fonte: FonteImpresaAgricola;
    codiceFiscale: string;
    partitaIva: string;
    denominazione: string;
    formaGiuridica: string;
    codiceREA: number;
    siglaProvinciaREA: string;
    cessata: boolean;
    oggettoSociale: string;
    dataCostituzione: Date;
    dataTermine: Date;
    capitaleSocialeDeliberato: number;
    numIscrizioneRegImprese: string;
    dataIscrizioneRegImprese: Date;
    sedeLegale: SedeLegaleDto;
    attivitaAteco: any;
    soggettiConCarica: any;
    aziendeCollegate: any;
}