export class RichiestaSuperficie {
    id: number;
    codiceColtura3: String;
    codiceColtura5: String;
    infoCatastali: Particella;
    infoColtivazione: InformazioniColtivazione;
    riferimentiCartografici: RiferimentiCartografici;
    supRichiesta: number;
    supRichiestaNetta: number;
    idInterventoDu: number;
    idDomanda: number;
}

export class Particella {
    idParticella: number;
    comune: String;
    codNazionale: String;
    foglio: number;
    particella: String;
    sub: String;
}

export class InformazioniColtivazione {
    idPianoColture: number;
    idColtura: number;
    codColtura3: String;
    codColtura5: String;
    codLivello: String;
    descrizioneColtura: String;
    coefficienteTara: number;
    superficieDichiarata: number;
}

export class RiferimentiCartografici {
    idParcella: number;
    idIsola: number;
    codIsola: String;
}

export class PaginaRichiestaSuperficie {

    risultati: Array<RichiestaSuperficie>;
    count: number;

}

export class PaginaSuperfici {
    paginaSuperfici: PaginaRichiestaSuperficie;
    supImpegnataLorda: number;
    supImpegnataNetta: number;
}
