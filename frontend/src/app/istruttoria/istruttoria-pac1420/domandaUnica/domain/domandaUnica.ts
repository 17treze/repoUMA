import { InfoGeneraliDomanda } from './infoGeneraliDomanda';
import { ControlliPresentazione } from './controlliPresentazione';
import { SintesiImpegno } from './sintesiImpegno';

export class Domanda {

    id: number;
    infoGeneraliDomanda: InfoGeneraliDomanda;
    controlliPresentazione: ControlliPresentazione;
    sintesiImpegno: SintesiImpegno;
    codiceElenco: String;
    identificativoDI: String;
    dataDI: string;
    erroreCalcolo: boolean;
    dataUltimoCalcolo: Date;
}
