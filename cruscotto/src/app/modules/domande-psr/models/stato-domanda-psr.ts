import DateTime from 'typescript-dotnet-commonjs/System/Time/DateTime';
import { ImportoLiquidatoDomandaPsr } from './importo-liquidato-domanda-psr';

export class StatoDomandaPsr {
  idDomanda: number;
  codOperazione: string;
  interventoPat: string;
  dataUltimoMovimento: DateTime;
  tipoPagamento: string;
  stato: string;
  importoLiquidato?: number;
}
