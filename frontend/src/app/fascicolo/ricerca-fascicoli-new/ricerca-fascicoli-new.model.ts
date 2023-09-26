export class FiltroRicercaFascicoli {
    cuaa?: string;
    denominazione?: string;
    caaUtenteConnesso?: string[];
    comuniCapofilaUtenteConnesso?: string[];
    stato?: string;

    public clean() {
        for (var propName in this) {
            if (this[propName] === null || this[propName] === undefined) {
                delete this[propName];
            }
        }
    }
}