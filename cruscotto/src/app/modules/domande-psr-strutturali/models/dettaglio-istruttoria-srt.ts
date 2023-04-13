import { Debiti } from "./importi-liquidazione";

export class DettaglioIstruttoriaSrt {
  acconti: Acconti[];
  anticipo: Anticipo;
  codiceStatoProgetto: {};
  finanziabilita: Finanziabilita;
  idBando: number;
  idProgetto: number;
  saldo: Saldo;
  statoProgetto: string;
  isFinanziatoPat: boolean;
}

export class Acconti {
  contributoLiquidabile: number;
  contributoRichiesto: number;
  costoInvestimentoRichiesto: number;
  data: string;
  idDomandaPagamento: number;
  saldoContributoLiquidato: number;
  socNumeroDomanda: number;
  socIncassatoNetto: number;
  sanzioni: Sanzione [];
}

export class Anticipo {
  anticipoLiquidabile: number;
  anticipoRichiesto: number;
  contributoAmmesso: number;
  costoAmmesso: number;
  data: string;
  socNumeroDomanda: number;
  socIncassatoNetto: number;
}

export class Saldo {
  contributoLiquidabile: number;
  contributoRichiesto: number;
  costoInvestimentoRichiesto: number;
  data: string;
  saldoContributoLiquidato: number;
  socNumeroDomanda: number;
  socIncassatoNetto: number;
  sanzioni: Sanzione [];
}

export class Finanziabilita {
  contributoAmmesso: number;
  contributoRichiesto: number;
  costoRichiesto: number;
  data: string;
}

export class DatiPagamento {
  tipologia: string;
  contributoLiquidabile?: number;
  contributoRichiesto?: number;
  costoInvestimentoRichiesto?: number;
  data: string;
  idDomandaPagamento?: number;
  saldoContributoLiquidato?: number;
  socNumeroDomanda?: number;
  socIncassatoNetto?: number;
  anticipoLiquidabile?: number;
  anticipoRichiesto?: number;
  contributoAmmesso?: number;
  costoAmmesso?: number;
  costoRichiesto?: number;
  sanzioni?: Sanzione [];
  totaleRecuperato?: number;
  debiti?: Debiti[];
}

export class PsrInterventoDettaglioDto {
  idIntervento: number;
  codifica: string;
  dettaglio: string;
  costoInvestimentoRichiesto: number;
  contributoRichiesto: number;
  contributoAmmesso: number;
}

export class Sanzione {
  descrizioneSanzione?: string;
  ammontareSanzione?: number;
}

export class PsrFattura {
  idDomandaPagamento: number;
  dataFattura: Date;
  codifica: string;
  tipoFattura: string;
  numeroFattura: number;
  oggetto: number;
  quotaContributoRichiesto: number;
  valoreFattura: number;
  spesaRichiesta: number;
  contributoRichiesto: number;
  spesaAmmessa: number;
  contributoAmmesso: number;
}
