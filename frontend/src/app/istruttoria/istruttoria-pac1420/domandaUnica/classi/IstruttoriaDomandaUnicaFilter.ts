import { YesNoEnum, RiservaEnum } from "../istruttoria-shared-filter/istruttoria-filtro-ricerca-domande";

export class IstruttoriaDomandaUnicaFilter {
    campagna: number;
    sostegno: string;
    tipo: string;
    stato: string;
    statoDomanda: string;
    pagamento: string;
    cuaa: string;
    ragioneSociale: string;
    numeroDomanda: number;
    campione: YesNoEnum;
    giovane: YesNoEnum;
    pascoli: YesNoEnum;
    riservaNazionale: RiservaEnum;
    istruttoriaBloccata: YesNoEnum;
    erroreCalcolo: YesNoEnum;
    anomalie: YesNoEnum;
    interventi: string[];
    codiciAnomalieWarning: string[];
    codiciAnomalieInfo: string[];
    codiciAnomalieError: string[];
    integrazione: YesNoEnum;

    public clean() {
        for (var propName in this) {
            if (this[propName] === null || this[propName] === undefined) {
                delete this[propName];
            }
        }
    }
}
