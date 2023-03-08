export class DettaglioParticella {
    id: number;
    comuneCatastale: string;
    codNazionale: string;
    foglio: number;
    particella: string;
    sub: string;
    codColtura: string;
    descrizioneColtura: string;
    supImpegnata: number;
    supEleggibile: number;
    supSigeco: number;
    supDeterminata: number;
    anomalieMantenimento: string;
    anomalieCoordinamento: string;
    tipoColtura: string;
    tipoSeminativo: string;
    colturaPrincipale: string;
    secondaColtura: string;
    azotoFissatrice: string;
    pascolo: string;
    supScostamento: number;
    superficieAnomalieCoordinamento: number;
}

export class PaginaDettaglioParticella {
    risultati: Array<DettaglioParticella>;
    elementiTotali: number;
}
