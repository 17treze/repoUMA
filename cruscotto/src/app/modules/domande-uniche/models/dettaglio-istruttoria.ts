import { SostegnoType } from './enumeration/sostegno-type.model';
import { RuleResult } from '@angularlicious/rules-engine';

export interface EsitoControlli {
    tipoControllo: string;
    esito?: boolean;
    livelloControllo?: string;
    valString?: string;
}

export interface Istruttorie {
    id: number;
    tipoIstruttoria: string;
    sostegno: SostegnoType;
    statoLavorazioneSostegno: string;
    dtUltimoCalcolo?: Date;
    esitiControlli: EsitoControlli[];
    ruleResult?: RuleResult[];
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
}

export interface DebitiRecuperati {
    importo: number;
    descrizione: string;
}
