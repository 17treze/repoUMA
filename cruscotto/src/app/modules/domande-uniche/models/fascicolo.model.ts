
export interface IFascicolo {
  idFascicolo: number;
  denominazione: string;
  cuaa: string;
  stato: string;
  caa: string;
  caacodice: string;
  idSoggetto: string;
}


export class Fascicolo implements IFascicolo {
  constructor(
    public idFascicolo: number,
    public denominazione: string,
    public cuaa: string,
    public stato: string,
    public caa: string,
    public caacodice: string,
    public idSoggetto: string
  ) {}
}

