export class SedeCaa {
    public id: number;
    public descrizione: string;
    public identificativo: number;
}

export class EnteCaa {
    id: number;
    codice: number;
    descrizione: string;
    sedi: Array<SedeCaa>;
}
