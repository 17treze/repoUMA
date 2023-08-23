export class FascicoloData {
   codiCuaa: string;
   descDeno: string;
   codiTipoDete: string;
   // stato: string;
   // caa: string;
   // caacodice: string;
   // sportello: string;
   // tipoDetenzione: TipoDetenzione;
   // detentore: string;
   op: OrgaPagaData;
   codiTipoAzie: string;
   dataValiFasc: string;
   dataElab: string;
   dataAperFasc: string
}

export class OrgaPagaData {
  codiOrgaPaga: string;
  descOrgaPaga: string;
}

export class FascicoloLazio {
  text: string;
  data: FascicoloData;

  public clean() {
    for (const propName in this) {
      if (this[propName] === null || this[propName] === undefined) {
        delete this[propName];
      }
    }
  }
}

export enum TipoDetenzione {
  MAN = 'MAN',
  DEL = 'DEL'
}

export  class SospensioneFascicolo {
  dataSospensione: Date;
  motivazioneSospensione: string;
}

