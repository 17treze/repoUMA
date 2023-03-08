import { DatiElaborazioneProcesso } from "src/app/istruttoria/istruttoria-antimafia/dto/DatiElaborazioneProcesso";

export class ProcessiDiControllo {
    datiElaborazioneProcesso: DatiElaborazioneProcesso;
    idDatiSettore: number;
    idProcesso: number;
    percentualeAvanzamento: number;
    statoProcesso: string;
    tipoProcesso: string;
}
