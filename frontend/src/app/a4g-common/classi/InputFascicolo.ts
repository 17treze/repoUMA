export class InputFascicolo {
    cuaa: string;
    denominazione: string;
    numero_domanda: string;

    constructor() {
        this.cuaa = '';
        this.denominazione = '';
        this.numero_domanda = '';
    }

    public clean() {
        for (const propName in this) {
            if (this[propName] === null || this[propName] === undefined) {
                delete this[propName];
            }
        }
    }
}
