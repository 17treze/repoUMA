import { SostegnoDu } from "../../classi/SostegnoDu";

export interface EsitoControlli {
    tipoControllo: string;
    esito?: boolean;
    livelloControllo?: string;
    valString?: string;
}

export interface Istruttorie {
    id: number;
    tipoIstruttoria: string;
    sostegno: SostegnoDu;
    statoLavorazioneSostegno: string;
    dtUltimoCalcolo?: Date;
    esitiControlli: EsitoControlli[];
    importiIstruttoria: ImportiIstruttoria;
}

export interface DettaglioIstruttorie {
    istruttorie: Istruttorie[];
    idDomanda: number;
}

export interface ImportiIstruttoria {
    importoCalcolato: number;
    importoAutorizzato: number;
    importoDisciplina?: number;
    debitiRecuperati: DebitiRecuperati[];
    annoEsercizio: number;
    progressivoCredito: number;
    numeroAutorizzazione: string;
    dataAutorizzazione: Date;
    progressivoPagamento: number;
    importoLiquidato: number;
}

export interface DebitiRecuperati {
    importo: number;
    descrizione: string;
}
