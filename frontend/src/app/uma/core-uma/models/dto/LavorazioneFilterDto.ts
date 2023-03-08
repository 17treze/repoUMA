export class LavorazioneFilterDto {
    ambito: AmbitoLavorazioneEnum;
}

export enum AmbitoLavorazioneEnum {
    SUPERFICIE = "SUPERFICIE",
    ZOOTECNIA = "ZOOTECNIA",
    FABBRICATI = "FABBRICATI", /** Trasformazioni Prodotti */
    ALTRO = "ALTRO",
    SERRE = "SERRE"
}
