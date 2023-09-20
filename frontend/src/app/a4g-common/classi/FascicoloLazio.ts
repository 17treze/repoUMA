export class FascicoloData {
   codiCuaa: string;
   descDeno: string;
   codiTipoDete: string;
   descDete: string;
   op: OrgaPagaData;
   codiTipoAzie: string;
   dataValiFasc: string;
   dataElab: string;
   dataAperFasc: string;
   dataScheVali: string;
   numeScheVali: string
   numeIscrRea: string;
   numeIscrRegiImpr: string;
   comuneCapofila: string
}

export class TerrenoData {
  codiTerr: string
  // ..
}
export class FabbricatoData {
  codiFabb: string
  // ..
}
export class MacchinaData {
  codiMacc: string
  // ...
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
export class MacchineLazio {
  text: string;
  data: MacchinaData[];
 
  public clean() {
    for (const propName in this) {
      if (this[propName] === null || this[propName] === undefined) {
        delete this[propName];
      }
    }
  }
}
export class TerreniLazio {
  text: string;
  data: TerrenoData[];

  public clean() {
    for (const propName in this) {
      if (this[propName] === null || this[propName] === undefined) {
        delete this[propName];
      }
    }
  }
}
export class FabbricatiLazio {
  text: string;
  data: FabbricatoData[];

  public clean() {
    for (const propName in this) {
      if (this[propName] === null || this[propName] === undefined) {
        delete this[propName];
      }
    }
  }
}

export class ComuneDto {
  codiProv: string;
	codiComu: string;
	descComu: string;
	codiNcap: string;
	codiComuCapo: string;
	codiCata: string;
}

export enum TipoDetenzione {
  MAN = 'MAN',
  DEL = 'DEL'
}

export  class SospensioneFascicolo {
  dataSospensione: Date;
  motivazioneSospensione: string;
}

