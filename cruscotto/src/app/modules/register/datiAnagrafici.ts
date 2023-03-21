import { CodiceResponsabilita } from './CodiceResponsabilita';

export class Titolare {
    public cfPersona: string;
    public cuaa: string;
    public denominazione: string;
}
export class Consulente {
    idResponsabilita: number;
    codResponsabilita: string;
    ordine: string;
    iscrizione: string;
    cuaa: string;
    denominazione: string;
    rappresentante: string;
    allegato: String;

    constructor(idResponsabilita: number, pordine: string, piscrizione: string, pcuaa: string, pdenominazione: string, prappresentante: string, allegato: String) {
        this.idResponsabilita = idResponsabilita;
        this.codResponsabilita = CodiceResponsabilita[CodiceResponsabilita.LPC];
        this.ordine = pordine;
        this.iscrizione = piscrizione;
        this.cuaa = pcuaa;
        this.denominazione = pdenominazione;
        this.rappresentante = prappresentante;
        this.allegato = allegato;
    }
}

export class CollaboratoreAltriEnti {
    idResponsabilita: number;
    codResponsabilita: string;
    denominazione: string;
    piva: string;
    dirigente: string;
    allegato: String;
    note: string;

    constructor(idResponsabilita: number, pdenominazione: string, ppiva: string, pdirigente: string, allegato: String, pnote: string) {
        this.idResponsabilita = idResponsabilita;
        this.codResponsabilita = CodiceResponsabilita[CodiceResponsabilita.ALTRI];
        this.denominazione = pdenominazione;
        this.dirigente = pdirigente;
        this.piva = ppiva;
        this.allegato = allegato;
        this.note = pnote;
    }
}

export class RuoloPAT {
    idResponsabilita: number;
    codResponsabilita: string;
    matricola: string;
    dirigente: string;
    dipartimento: string;
    allegato: String;
    note: string;

    constructor(idResponsabilita: number, pmatricola: string, pdirigente: string, pdipartimento: string, allegato: String, pnote: string) {
        this.idResponsabilita = idResponsabilita;
        this.codResponsabilita = CodiceResponsabilita[CodiceResponsabilita.PAT];
        this.matricola = pmatricola;
        this.dirigente = pdirigente;
        this.dipartimento = pdipartimento;
        this.allegato = allegato;
        this.note = pnote;
    }
}

export class SedeCaa {
    public id: number;
    public descrizione: string;
    public identificativo: number;
}

export class EnteCaa {
    id: number;
    codice: number;
    descrizione: string;
    sedi: Array<SedeCaa>;
}

export class RuoloCAA {
    idResponsabilita: number;
    responsabile: string;
    codResponsabilita: string;
    sedi: EnteCaa[];
    allegato: String;

    constructor(idResponsabilita: number, presponsabile: string, psedi: EnteCaa[], allegato: String) {
        this.idResponsabilita = idResponsabilita;
        this.responsabile = presponsabile;
        this.sedi = psedi;
        this.codResponsabilita = CodiceResponsabilita[CodiceResponsabilita.CAA];
        this.allegato = allegato;
    }
}

export class TitolareImpresa {
    cuaa: string;
    denominazione: string;

    constructor(pcuaa: string, pdenominazione: string) {
        this.cuaa = pcuaa;
        this.denominazione = pdenominazione;
    }
}

export class ResponsabilitaRichieste {
    responsabilitaTitolare: TitolareImpresa[];
    responsabilitaLegaleRappresentante: TitolareImpresa[];
    responsabilitaCaa: RuoloCAA[];
    responsabilitaPat: RuoloPAT[];
    responsabilitaAltriEnti: CollaboratoreAltriEnti[];
    responsabilitaConsulente: Consulente[];
}

export enum ServiziTypeEnum {
    AGS = "AGS",
    A4G = "A4G",
    SRT = "SRT"
}

export enum TipoDomandaRegistrazione {
    RIDOTTA_AZIENDA = "RIDOTTA_AZIENDA",
    RIDOTTA_AZIENDA_ANAGRAFICO = "RIDOTTA_AZIENDA_ANAGRAFICO",
    COMPLETA = "COMPLETA"
}

export class DatiDomanda {
    id: number;
    datiAnagrafici: DatiAnagrafici;
    responsabilitaRichieste: ResponsabilitaRichieste;
    servizi: ServiziTypeEnum[];
    luogo: 'Trento';
    data: Date = new Date();
    dataProtocollazione: Date;
    stato: string;
    idProtocollo: string;
    tipoDomandaRegistrazione: TipoDomandaRegistrazione;
}

export class DatiAnagrafici {
    codiceFiscale: string;
    nome: string;
    cognome: string;
    email: string;
    telefono: string;
}

export class Firma {
    xml: string;
    pdf: string;    // no "Blob" per permettere decodifica per stampa
}

export class Persona {
    id: number;
    versione: number;
    descrizione: string;
    codiceFiscale: string;
    nome: string;
    cognome: string;
    nrProtocolloPrivacyGenerale: string;
}
