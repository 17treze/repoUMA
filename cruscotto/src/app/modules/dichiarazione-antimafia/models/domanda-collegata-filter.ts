export interface DomandaCollegataFilter {
  cuaa: string;
  tipoDomanda: string;
  idDomanda: number;
  campagna: number;
}

export enum DomandaCollegataTypeEnum {
  PSR_SUPERFICIE_EU = 'PSR_SUPERFICIE_EU',
  DOMANDA_UNICA = 'DOMANDA_UNICA'
}
