export interface RisultatiRicercaClientiDto {
    idAgs: number;
    cuaa: string;
    denominazione: string;
    stato: string;
    organismoPagatore: string;
    caa: string;
    sportello: string;
    dataCostituzione: Date;
    dataAggiornamento: Date;
    dataValidazione: Date;
    iscrittoSezioneSpecialeAgricola: boolean,
    nonIscrittoSezioneSpecialeAgricola: boolean,
    pec: string;
}