export class DettaglioCalcoloParticella {
    codiceColtura3: string;
    descrizioneColtura: string;
    infoCatastali: Particella;
    variabiliCalcoloParticella: VariabiliParticellaColtura;
}

export class Particella {
    idParticella: number;
    comune: string;
    codNazionale: string;
    foglio: number;
    particella: string;
    sub: string;
}

export class VariabiliParticellaColtura {
    superficieImpegnata: number;
    superficieEleggibile: number;
    superficieSigeco: number;
    anomalieMantenimento: boolean;
    anomalieCoordinamento: boolean;
    superficieDeterminata: number;
    tipoColtura: string;
    tipoSeminativo: string;
    colturaPrincipale: boolean;
    secondaColtura: boolean;
    azotoFissatrice: boolean;
    pascolo: string;
    superficieAnomalieCoordinamento: number;
    superficieScostamento: number;
}

export class PaginaDettaglioCalcoloParticella {
    risultati: Array<DettaglioCalcoloParticella>;
    elementiTotali: number;
}

export class DettaglioSuperficieCalcoloDto {
    idIstruttoria: number;
    codiceCultura: string;
    idParticella: number;
    idParcelle: number[];
}
