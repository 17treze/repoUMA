export interface IstruttoriaDomandaUnica {
    id:                 number;
    tipo:               string;
    stato:              string;
    sostegno:           string;
    domanda:            Domanda;
    elencoLiquidazione: ElencoLiquidazione;
    erroreCalcolo:      boolean;
    dataUltimoCalcolo:  Date;
    isBloccata: boolean;
    isErroreCalcolo: boolean;
    order?: number;
}

export interface Domanda {
    id:                      number;
    campagna:                number;
    codEnteCompilatore:      number;
    codModuloDomanda:        string;
    cuaaIntestatario:        string;
    descEnteCompilatore:     string;
    descModuloDomanda:       string;
    dtPresentazOriginaria:   Date;
    dtPresentazione:         Date;
    dtProtocollazOriginaria: Date;
    dtProtocollazione:       Date;
    numDomandaRettificata:   number;
    numeroDomanda:           number;
    ragioneSociale:          string;
    stato:                   string;
}

export interface ElencoLiquidazione {
    id:             number;
    codElenco:      string;
    codTipoElenco:  string;
    dtCreazione:    Date;
}