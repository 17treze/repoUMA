export class ImportiLiquidazione {
  anno: number;
  causale: string;
  dataAutorizzazione: string;
  dataEsecuzionePagamento: string;
  debiti: Debiti[];
  idElencoLiquidazione: number;
  incassatoNetto: number;
  numeroAutorizzazione: string;
  progressivo: number;
  progressivoPagamento: number;
  tipoBilancio: string;
  totaleRecuperato: number;
  numeroDomanda: number;
}

export class Debiti {
  descrizioneCapitolo: string;
  importo: number;
}
