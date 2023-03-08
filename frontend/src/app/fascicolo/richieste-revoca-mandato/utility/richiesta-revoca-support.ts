import { StatoRichiestaRevocaImmediata } from '../dto/RichiestaRevocaImmediataDto';

export class RichiestaRevocaSupport {

    public static isAccettata(stato: StatoRichiestaRevocaImmediata) {
        if (stato === StatoRichiestaRevocaImmediata.ACCETTATA) {
            return true;
        }
        return false;
    }

    public static isRifiutata(stato: StatoRichiestaRevocaImmediata) {
        if (stato === StatoRichiestaRevocaImmediata.RIFIUTATA) {
            return true;
        }
        return false;
    }

}
