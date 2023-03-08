import { CapoBdn } from "./dettaglioCapi";

export class DettaglioAllevamenti {
    codiceIntervento: number;
    codiceSpecie: string;
    cuaaIntestatario: string;
    datiAllevamento: any;
    datiDetentore: any;
    datiProprietario: any;
    id: number;
    richiesteAllevamentoDuEsito: Capo[];
    stato: string;
    tableVisible?: boolean;
    count: number;
}


export class Capo {
    id: number;
    capoId: number;
    codiceCapo: string;
    esito: string;
    messaggio: string;
    capoBdn: CapoBdn;
}

