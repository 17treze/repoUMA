export class DomandaUnica {
    risultati: Array<Domanda>;
    count: number;
}

export class Domanda {
    id: number;
    campagna: number;
    codEnteCompilatore: number;
    codModuloDomanda: string;
    cuaaIntestatario: string;
    descEnteCompilatore: string;
    descModuloDomanda: string;
    dtPresentazOriginaria: Date;
    dtPresentazione: Date;
    dtProtocollazOriginaria: Date;
    dtProtocollazione: Date;
    numDomandaRettificata: number;
    numeroDomanda: number;
    ragioneSociale: string;
    stato: string;
}