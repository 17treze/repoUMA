export class Istruttoria {
    id: number;
    tipo: string;
    stato: string;
    sostegno: string;
    domanda: Domanda;
    elencoLiquidazione: ElencoLiquidazione;
    isBloccata: boolean;
    isErroreCalcolo: boolean;
    dataUltimoCalcolo: null
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

export class ElencoLiquidazione {
    id: number;
    codElenco: string;
    dtCreazione: string;
}