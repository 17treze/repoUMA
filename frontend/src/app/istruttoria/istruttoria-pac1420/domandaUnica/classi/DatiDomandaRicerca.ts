export interface DatiDomandaRicerca {
    numeroDomanda: number;
    cuaa: string;
    companyDescription: string;
    year: number;
    state: string;
    relates: Array<RelateModel>;
}

export interface RelateModel {
    support: string;
    payment: string;
    idIstruttoria: number;
}

export class DatiDomandaRicercaPage {


    constructor() {
    }

    risultati: Array<DatiDomandaRicerca>;
    count: number;
}