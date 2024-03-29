import { AmbitoGruppoLavorazione } from "../enums/AmbitoGruppoLavorazione.enum";
import { TipologiaLavorazioneEnum } from "../enums/TipologiaLavorazione.enum";
import { UnitaMisura } from "../enums/UnitaMisura.enum";

export interface CoefficienteDto {
    id: number;
    versione: number;
    coefficiente: number;
    lavorazione: LavorazioneDto;
    annoInizio: number;
    annoFine: number;
}

export interface LavorazioneDto {
    id: number;
    versione: number;
    indice: number;
    nome: string;
    tipologia: TipologiaLavorazioneEnum;
    unitaDiMisura: UnitaMisura;
    gruppoLavorazione: GruppoLavorazioneDto;
}

export interface GruppoLavorazioneDto {
    id: number;
    versione: number;
    indice: number;
    nome: string;
    ambitoLavorazione: AmbitoGruppoLavorazione;
    annoInizio: number;
    annoFine: number;
}

export interface GruppoColtureDto {
    id: number;
    versione: number;
    gruppoLavorazione: GruppoLavorazioneDto;
    codiceSuolo: string;
    codiceDestUso: string;
    codiceUso: string;
    codiceQualita: string;
    codiceVarieta: string;
    annoInizio: string;
    annoFine: string;
}

export interface GruppoFabbricatoDto {
    id: number;
    versione: number;
    codiceFabbricato: string;
    tipoFabbricato: string;
    gruppoLavorazione: GruppoLavorazioneDto;
}
