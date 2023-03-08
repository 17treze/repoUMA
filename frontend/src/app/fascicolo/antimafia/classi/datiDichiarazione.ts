import { Observable } from 'rxjs';

export class DatiDichiarazione {
    richiedente: Richiedente;
    dettaglioImpresa: DettaglioImpresa;
}

export interface Residenza {
    indirizzo: string;
    comune: string;
    provincia: string;
    CAP: string;
}

export class Richiedente {
    nome: string;
    cognome: string;
    codiceFiscale: string;
    comuneNascita: string;
    provinciaNascita: string;
    dataNascita: string;
    sesso: string;
    residenza: Residenza;
    indirizzoPEC: string;
    carica: string;
    dtFineCarica: string;
    dtInizioCarica: string;
}

export interface EstremiCCIAA {
    sede: string;
    numeroIscrizione: string;
    dataIscrizione: string;
}

export interface DettaglioPersonaGiuridica {
    formaGiuridica: string;
    estremiCostituzione: string;
    capitaleSociale: string;
    durata: string;
}

export interface Carica {
    codice: string;
    tipologia: string;
    dataInizio: string;
    dataFine: string;
    selezionato: boolean;
    dtPdfDicFamConv: Date;
    href: string;
    pdfDicFamConv: ArrayBuffer | string;
    dichiarazione: boolean;
    firmaDigitale: boolean;
    tipoAllegato: string;
}
export interface CaricaCodiceFiscale extends Carica {
    codiceFiscale: string;
}
export interface FamiliariConviventi {
    nome: string;
    cognome: string;
    codiceFiscale: string;
    comuneNascita: string;
    provinciaNascita: string;
    dataNascita: string;
    sesso: string;
    residenza: Residenza;
    gradoParentela: string;
}

export interface SoggettiImpresa {
    nome: string;
    cognome: string;
    codiceFiscale: string;
    comuneNascita: string;
    provinciaNascita: string;
    dataNascita: string;
    sesso: string;
    indirizzoResidenza: string;
    comuneResidenza: string;
    provinciaResidenza: string;
    capResidenza: string;
    carica: Carica[];
    familiariConviventi: FamiliariConviventi[];
}

export interface AziendaCollegata {
    denominazione: string;
    codiceFiscale: string;
    comuneSede: string;
    indirizzoSede: string;
    civicoSede?: string;
    provinciaSede: string;
    capSede: string;
    carica: Carica[];
    selezionato: boolean;
    dettaglioImpresa: DettaglioImpresa;
}

export interface DettaglioImpresa {
    formaGiuridicaCodice: string;
    formaGiuridicaDescrizione: string;
    denominazione: string;
    sedeLegale: string;
    estremiCCIAA: EstremiCCIAA;
    oggettoSociale: string;
    codiceFiscale: string;
    partitaIva: string;
    indirizzoPEC: string;
    dettaglioPersonaGiuridica: DettaglioPersonaGiuridica;
    soggettiImpresa: SoggettiImpresa[];
    aziendeCollegate: AziendaCollegata[];
}

export class AllegatoDicFamConv {
    id: number;
    idDichiarazioneAntimafia: number;
    allegatoFamiliariConviventiPdf: File;
    dtPdfDicFamConv: Date;
    codCarica: string;
    cfSoggettoImpresa: string;
    firmaDigitale: boolean;
    tipoFile: string;
}

export interface AllegatoMetadati {
    observable: Observable<any>;
    index: number;
    codiceFiscale: string;
    tipoAllegato: string;
}




