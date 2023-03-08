export class PoligoniSuoloDaAdl {
  RisultatiADL;
}
export interface Count {
  number;
}
export interface RisultatiADL {
  id: number;
  idLavorazione: number;
  idGrid: number;
  istatp: string;
  codUsoSuoloModel: any;
  statoColtSuolo: any;
  sorgente: string;
  note: string;
  campagna: number;
  area: number;
  esitoValidazione: string;
  posizionePoligono: string;
  poligonoIntero: number;
  idSuoloOriginale: number;
  extent: number[];
  shape: string;
}
