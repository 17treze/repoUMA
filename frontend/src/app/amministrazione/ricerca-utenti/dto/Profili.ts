// import { Distributore } from 'src/app/a4g-common/classi/distributore';
// import { SedeCaa } from "src/app/a4g-common/classi/EnteSedeCaa";

export class ComuneCapofila {
    codiProv: string;
    codiComu: string;
    codiCata: string;
    descComu: string;
}

export class RuoloApplicazione {
    applicazione: string;
    ruoli: Ruolo[];
}

export class AziendaDelegataApplicazione {
    applicazione: string;
    aziendeDelegate: AziendaDelegata[];
}

export class Ruolo {
    id: string;
    descrizione: string;
    applicazione: string;
}

export class AziendaDelegata {
    cuaa: string;
    denominazione: string;
}

export class DatiUtente {
    id: number;
    nome: string;
    cognome: string;
    codiceFiscale: string;
    email: string;
    telefono: string;
    pec: string;
    indirizzo: string;
    comune: string;
    provincia: string;
    cap: string;
    ruoli: RuoloApplicazione[];
    aziendeDelegate: AziendaDelegataApplicazione[];
    stato: string;
    dataNascita: string;
    luogoNascita: string;
    dataAbilitazione: string;
    dataDisabilitazione: string;
    comuniCapofila: ComuneCapofila[];
}

export class Profilo {
    id: number;
    identificativo: string;
    descrizione: string;
    responsabilita: string;
    haRuoli: boolean;
    disabled: boolean;
}

export class Azienda {
    id: number;
    idCarica: number;
    dataAggiornamento: string;
    cuaa: string;
}

/*
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

export class ProfiliAgs {
    cf: string;
    cognome: string;
    descrizione: string;
    nome: string;
    utenza: string;
}

export class ProfiliSrt extends UtenteBase {
    ente: string;
    ruolo: string;
}
*/