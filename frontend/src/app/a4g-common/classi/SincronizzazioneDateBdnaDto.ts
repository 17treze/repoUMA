import { TipoDomandaEnum } from 'src/app/istruttoria/istruttoria-antimafia/dto/TipoDomandaEnum';

export class SincronizzazioneDateBdnaDto {
    cuaa: string
    tipoDomanda: string // TIPO DOMANDA ENUM
    idDomanda: number
    dtInizioSilenzioAssenso: Date
    dtInizioEsitoNegativo: Date
    dtFineSilenzioAssenso: Date
    dtFineEsitoNegativo: Date

    constructor(cuaa: string) {
        this.cuaa = cuaa
    }
}