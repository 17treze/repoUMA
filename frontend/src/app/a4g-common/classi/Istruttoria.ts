export class Istruttoria {
  id: number;
  annoRiferimento: string;
  descrizioneDomanda: string;
  descrizionePac: string;
  codicePac?: string;
  dtRicevibilita?: Date;
  dtScadenzaDomande?: Date;
  dtScadenzaDomandeRitardo?: Date;
  dtScadenzaDomandeRitiro?: Date;
  dtScadenzaDomandeRitiroparz?: Date;
  tipoDomanda?: string;
  countDomandeDU?: number;
  percIncrementoGreening?: number;
  percIncrementoGiovane?: number;
  limiteGiovane?: number;
  percRiduzioneLineare1?: number;
  percRiduzioneLineare2?: number;
  percRiduzioneLineare3?: number;
  percRiduzioneTitoli?: number;
  percDisciplinaFinanziaria?: number;
  percPagamento?: number;
  tipoIstruttoria: string;
}
