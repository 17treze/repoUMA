import DateTime from 'typescript-dotnet-commonjs/System/Time/DateTime';
import { UbaAlpeggiatePsr } from './uba-alpeggiate-psr';
import { PascoloPsr } from './pascolo-psr';
import { ImpegnoZooPascoloPsr } from './impegno-zoo-pascolo-psr';

export class ImpegnoRichiestoPsr {
  idDomanda: number;
  idModulo: number;
  dataPres: DateTime;
  grafica: number;
  codDestinazione: string;
  dsDestinazione: string;
  supImpegno: number;
  supImpegnoNetta: number;
  ubaAlpeggiatePsr: UbaAlpeggiatePsr;
  pascoliPsr: PascoloPsr[];
  impegnoZooPascoliPsr: ImpegnoZooPascoloPsr[];
}
