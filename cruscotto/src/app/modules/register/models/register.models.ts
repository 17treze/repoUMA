import { DatiDomanda } from '../datiAnagrafici';

export class DocumentazionePrivacy {
    mittente: Mittente;
    oggetto: string;
    tipologiaDocumentoPrincipale: string;
}

export class Mittente {
    name: string;
    surname: string;
    email: string;
    nationalIdentificationNumber: string;
    description: string;
};

export class DomandaRegistrazioneCreataResponse {
    id: number;
    base64Content: string;
}

export enum TIPO_CREAZIONE_UTENTE_FASCICOLO {
  CREA_UTENTE = 'CREA_UTENTE',
  CREA_UTENTE_FASCICOLO = 'CREA_UTENTE_E_FASCICOLO'
}
