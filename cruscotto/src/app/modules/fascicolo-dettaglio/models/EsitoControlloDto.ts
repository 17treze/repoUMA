export class EsitoControlloDto {
    esito: number;
    idControllo: number;
    segnalazioni: SegnalazioneDto[];
}

export class SegnalazioneDto {
    descrizione: string;
    tipo: TipoSegnalazioneEnum;
}

export enum TipoSegnalazioneEnum {
    ERRORE = 'ERRORE',
    AVVERTENZA = 'AVVERTENZA'
}
