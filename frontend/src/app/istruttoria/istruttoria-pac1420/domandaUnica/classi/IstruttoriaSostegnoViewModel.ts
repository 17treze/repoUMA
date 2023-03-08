import { ElencoLiquidazione } from "./IstruttoriaDomandaUnica";

export class IstruttoriaSostegnoViewModel {
    idDomanda: number;
    idElencoLiquidazione: number;
    cuaaIntestatario: string;
    numeroDomanda: number;
    ragioneSociale: string;
    idDomandaIntegrativa: string;
    codiceElenco: string;
    elencoLiquidazione: ElencoLiquidazione;
    isBloccata: boolean;
    isErroreCalcolo: boolean;
    dataUltimoCalcolo: Date;
}
