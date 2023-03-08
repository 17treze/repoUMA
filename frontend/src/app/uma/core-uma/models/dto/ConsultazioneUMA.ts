export interface ConsultazioneUMA {
    nrDomanda: number;
    anno: number;
    tipo: string;
    stato: string;
    cuaa: string;
    denominazione: string;
    protocollo: string;
    dataPresentazione?: Date;
    /** per accedere in modalita edit o view */
    edit?: boolean;
    view?: boolean;
}