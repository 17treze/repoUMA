import { ResponsabilitaTitolare } from "./responsabilita-titolare";
import { ResponsabilitaCaa } from "./responsabilita-caa";
import { ResponsabilitaPat } from "./responsabilita-pat";
import { ResponsabilitaAltriEnti } from "./responsabilita-altri";
import { ResponsabilitaConsulente } from "./responsabilita-consulente";
import { ResponsabilitaDistributore } from './responsabilita-distributore';

export class ResponsabilitaRichieste {
  responsabilitaTitolare: Array<ResponsabilitaTitolare>;
  responsabilitaLegaleRappresentante: Array<ResponsabilitaTitolare>;
  responsabilitaCaa: Array<ResponsabilitaCaa>;
  responsabilitaPat: Array<ResponsabilitaPat>;
  responsabilitaAltriEnti: Array<ResponsabilitaAltriEnti>;
  responsabilitaConsulente: Array<ResponsabilitaConsulente>;
  responsabilitaDistributore: Array<ResponsabilitaDistributore>;
}


