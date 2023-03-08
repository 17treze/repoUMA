export class TipoConduzioneDto { 
    id: number;
    descrizione: string;
    codice: string;
    sottotipo: SottotipoConduzioneDto[];
}

export class SottotipoConduzioneDto {
    id: number;
    idTipoConduzione: number;
    descrizione: string; 
    codice: string;
}

export class TipoDocumentoConduzioneDto {
    id: number;
    idSottotipo: number;
    idDocumentoConduzione: number;
    descrizione: string;
    tipo: string;
    obbligatorio: number;
    documentoDipendenza: number[];
}

export class ParticellaFondiariaDto {
    particella: string;
    foglio: number;
    sub: string;
    sezione: string;
    comune: string;
    superficieCondotta: number;
}

export class DocumentoConduzioneDto {
    idTipoDocumento: number;
    documento: File;
}

export class ConduzioneTerreniDto {
    ambito: TipoConduzioneDto;
    idSottotipo: number;
    documentiConduzionePrincipali: DocumentoConduzioneDto;
    documentiConduzioneSecondari: DocumentoConduzioneDto[];
    particelleFondiarie: ParticellaFondiariaDto[];
}
