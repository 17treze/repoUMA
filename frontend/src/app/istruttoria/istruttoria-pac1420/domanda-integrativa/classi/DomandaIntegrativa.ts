import { CapoRichiesto } from "../../domandaUnica/domain/dettaglioCapi";

export class DomandaIntegrativa {
    stato: string;
    allevamenti: Allevamento[];
    identificativo: string;
}

export class Allevamento {
    descAllevamento: string;
    descProprietario: string;
    capi: Capo[];
}

export class Capo {
    codCapo: string;
    codSpecie: string;
    interventi: Intervento[];
}

export class Intervento {
    codice: string;
    esito: string;
    idEsito: string;
    selezionato: boolean;
    descrizioneBreve: string;
    descrizioneLunga: string;
}

export class DettaglioIntervento {
    descAllevamento: string;
    codSpecie: string;
    capi: CapoRichiesto[];
}