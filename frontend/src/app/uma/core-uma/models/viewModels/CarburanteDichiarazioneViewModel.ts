export interface CarburanteDichiarazioneVM {
    totale: string;
    benzina: string;
    serre: string;
}

export interface CarburanteContoDichiarazioneVM extends CarburanteDichiarazioneVM {
    contoProprio?: string;
    contoTerzi?: string;
}

