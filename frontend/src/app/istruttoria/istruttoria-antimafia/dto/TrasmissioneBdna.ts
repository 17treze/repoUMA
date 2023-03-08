import { StatoTrasmissioneEnum } from "./StatoTrasmissioneEnum";
import { TipoDomandaEnum } from "./TipoDomandaEnum";

export class TrasmissioneBdna {
    id: number;
    cfOperatore: string;
    dtCreazione: Date;
    dtConferma: Date;
    statoTrasmissione: StatoTrasmissioneEnum;
    tipoDomanda: TipoDomandaEnum;
  }
  