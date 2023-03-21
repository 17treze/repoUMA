import DateTime from 'typescript-dotnet-commonjs/System/Time/DateTime';

export class ImportoLiquidatoDomandaPsr {
  anno: number;
  causale: string;
  codiceProdotto: string;
  dataAutorizzazione: DateTime;
  dataEsecuzionePagamento: DateTime;
  idElencoLiquidazione: number;
  incassatoNetto: number;
  numeroAutorizzazione: string;
  numeroDomanda: string;
  progressivo: number;
  progressivoPagamento: number;
  tipoBilancio: string;
  tipoPagamento: string;
  totaleRecuperato: number;
  debiti: DebitiImportoLiquidatoDomandaPsr[];
}

export class DebitiImportoLiquidatoDomandaPsr {
  descrizioneCapitolo: string;
  importo: number;
}

