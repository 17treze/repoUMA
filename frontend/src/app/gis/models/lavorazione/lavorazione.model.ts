import { StatoLavorazioneSuolo } from '../../shared/StatoLavorazioneSuolo.enum';
export class LavorazioneModel {
    LavorazioneObj;
}
export interface LavorazioneObj {
    id?: number;
    utente?: string;
    utenteAgs?: string;
    stato?: StatoLavorazioneSuolo;
    dataInizioLavorazione?: Date;
    dataFineLavorazione?: Date;
    dataUltimaModifica?: Date;
    note?: string;
    titolo?: string;
    sopralluogo?: string;
    campagna?: number;
    modalitaADL?: string;
    xUltimoZoom?: number;
    yUltimoZoom?: number;
    scalaUltimoZoom?: number;
    readOnly?: string;
    idLavorazionePadre?: string;
    statoMappato?: string;
}

