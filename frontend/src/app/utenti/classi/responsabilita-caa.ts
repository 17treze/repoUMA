import { CodiceResponsabilita } from "./CodiceResponsabilita";
import { EnteCaa } from "src/app/a4g-common/classi/EnteSedeCaa";

export class ResponsabilitaCaa {
  idResponsabilita: number;
  responsabile: string;
  codResponsabilita: string;
  sedi: EnteCaa[];
  allegato: String;

  constructor(idResponsabilita: number, presponsabile: string, psedi: EnteCaa[], allegato: String) {
    this.idResponsabilita = idResponsabilita;
    this.responsabile = presponsabile;
    this.sedi = psedi;
    this.codResponsabilita = CodiceResponsabilita[CodiceResponsabilita.CAA];
    this.allegato = allegato;
  }
}
