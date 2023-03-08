import { CodiceResponsabilita } from "./CodiceResponsabilita";
import { Distributore } from 'src/app/a4g-common/classi/distributore';

export class ResponsabilitaDistributore {
  idResponsabilita: number;
  responsabile: string;
  codResponsabilita: string;
  distributori: Distributore[];
  allegato: String;

  constructor(idResponsabilita: number, presponsabile: string, distributori: Distributore[], allegato: String) {
    this.idResponsabilita = idResponsabilita;
    this.responsabile = presponsabile;
    this.distributori = distributori;
    this.codResponsabilita = CodiceResponsabilita[CodiceResponsabilita.DIPENDENTE_DISTRIBUTORE];
    this.allegato = allegato;
  }
}
