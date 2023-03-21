import { SedeCaa } from './datiAnagrafici';

export class Profilo {
    id: number;
    identificativo: string;
    descrizione: string;
    responsabilita: string;
    haRuoli: boolean;
    disabled: boolean;
}

export class UtenteBase {
    identificativo: string;
    codiceFiscale: string;
    nome: string;
    cognome: string;
}

export class ProfiliUtente {
    codiceFiscale: string;
    profiliSrt: ProfiliSrt[];
    profiliAgs: DatiUtente[];
    profiliA4g: ProfiliAgs[];
}

export class Azienda {
    id: number;
    idCarica: number;
    dataAggiornamento: string;
    cuaa: string;
}

export class DatiUtente extends UtenteBase {
    id: number;
    profili: Profilo[];
    sedi: SedeCaa[];
    aziende: Azienda[];
    motivazioneDisattivazione: string;
}

export class Utente extends DatiUtente {
}

export class ProfiliSrt extends UtenteBase {
    ente: string;
    ruolo: string;
}


export class ProfiliAgs {
    cf: string;
    cognome: string;
    descrizione: string;
    nome: string;
    utenza: string;
}
