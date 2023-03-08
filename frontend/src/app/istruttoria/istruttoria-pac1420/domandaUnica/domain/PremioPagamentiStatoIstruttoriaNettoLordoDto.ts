import { StatoIstruttoriaEnum } from "../cruscotto-sostegno/StatoIstruttoriaEnum";

export enum NettoLordoEnum {
    NETTO = "NETTO", LORDO = "LORDO"
}

export class PremioPagamentiStatoIstruttoriaNettoLordoDto {
    statoIstruttoria: StatoIstruttoriaEnum;
	tipoPremio: NettoLordoEnum;
	valorePremio: number;
}