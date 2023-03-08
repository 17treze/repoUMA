export class ParticellaDomanda {
    idParticella: number;
    comuneCatastale: String;
    codNazionale: String;
    foglio: number;
    particella: String;
    sub: String;
    isola: String;
    codColtura: String;
    descrColtura: String;
    supImpLorda: number;
    supImpNetta: number;
}

export class PaginaParticellaDomanda {

    risultati: Array<ParticellaDomanda>;
    elementiTotali: number;

}
