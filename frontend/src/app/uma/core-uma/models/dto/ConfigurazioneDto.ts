export interface CoefficienteDto {
    id: number;
    versione: number;
    coefficiente: number;
    lavorazione: LavorazioneDto
    annoInizio: number;
    annoFine: number;
}

export interface LavorazioneDto {
    id: number;
    versione: number;
    indice: number;
    nome: string;
    tipologia: string;
    unitaMisura: string;
    gruppoLavorazione: GruppoLavorazioneDto;
}

export interface GruppoLavorazioneDto {
    id: number;
    versione: number;
    indice: number;
    nome: string;
    ambito: string;
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
