import { SelectItem } from 'primeng-lts';

export interface DettaglioMacchinaVM {
    // Mezzo
    id?: number; // se null sono in creazione se != null sono in update
    tipologiaMacchinario: SelectItem;
    classeFunzionaleMacchinario: SelectItem;
    sottoTipologiaMacchinario: SelectItem;
    marcaMacchinario: string;
    modello: string;
    targa: string;
    numeroMatricola: string;
    numeroTelaio: string;
    annoCostruzione: number;
    switchMotore: boolean;
    // Motore
    marcaMotore?: string;
    tipo?: string;
    alimentazione?: SelectItem;
    potenza?: string;
    numeroMotore?: string;
    // Possesso
    tipologiaPossesso: SelectItem;
    codiceFiscale:string;
    ragioneSociale: string;
    //gestione migrazione
    flagMigrato: number;
}

export interface ClassificazioneMacchinarioVM {
    tipologiaMacchinario: string;
    classeFunzionaleMacchinario: string;
    sottoTipologiaMacchinario: string;
}