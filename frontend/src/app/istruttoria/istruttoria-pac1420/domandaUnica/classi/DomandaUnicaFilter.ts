export class DomandaUnicaFilter {
    cuaaIntestatario: string;
    numeroDomanda: number;
    ragioneSociale: string;
    statoLavorazioneDomanda: string;
    statoDomanda: string;
    annoCampagna: number;

    public clean() {
        for (var propName in this) {
            if (this[propName] === null || this[propName] === undefined) {
                delete this[propName];
            }
        }
    }
}
