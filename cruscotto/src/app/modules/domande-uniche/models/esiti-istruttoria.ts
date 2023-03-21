export class EsitiIstruttoria {
    codice1: string;
    descrizione1: string;
    ordine1: number;
    codice2: string;
    descrizione2: string;
    ordine2: number;
    controllo: string;
    controlloDescrizione: string;
    ordineControllo: number;
    valore: string;
}

export class EsitiIstruttoriaView {
    tipoEsito: string;
    datiInput: Dati[];
    datiOutput: Dati[];

    constructor(tipoEsito: string, datiInput: Dati[], datiOutput: Dati[]) {
        this.tipoEsito = tipoEsito;
        this.datiInput = datiInput;
        this.datiOutput = datiOutput;
    }
}

export class Dati {
    descrizione: string;
    valore: string;

    constructor(descrizione: string, valore: string) {
        this.descrizione = descrizione;
        this.valore = valore;
    }
}

