

export class IstruttoriaEdit {

    percIncrementoGreening: number;
    percIncrementoGiovane: number;
    limiteGiovane: number;
    percRiduzioneLineare1: number;
    percRiduzioneLineare2: number;
    percRiduzioneLineare3: number;
    percRiduzioneTitoli: number;
    percPagamento: number;
    percDisciplinaFinanziaria: number;
    dtRicevibilita: Date;
    dtScadenzaDomande: Date;
    dtScadenzaDomandeRitardo: Date;
    dtScadenzaDomandeRitiroparz: Date;


    // tslint:disable-next-line:max-line-length
    constructor(percIncrementoGreening: number, percIncrementoGiovane: number, limiteGiovane: number, percRiduzioneLineare1: number, percRiduzioneLineare2: number, percRiduzioneLineare3: number, percRiduzioneTitoli: number, percPagamento: number, percDisciplinaFinanziaria: number, dtRicevibilita: Date, dtScadenzaDomande: Date, dtScadenzaDomandeRitardo: Date, dtScadenzaDomandeRitiroparz: Date) {
        this.percIncrementoGreening = percIncrementoGreening;
        this.percIncrementoGiovane = percIncrementoGiovane;
        this.limiteGiovane = limiteGiovane;
        this.percRiduzioneLineare1 = percRiduzioneLineare1;
        this.percRiduzioneLineare2 = percRiduzioneLineare2;
        this.percRiduzioneLineare3 = percRiduzioneLineare3;
        this.percRiduzioneTitoli = percRiduzioneTitoli;
        this.percPagamento = percPagamento;
        this.percDisciplinaFinanziaria = percDisciplinaFinanziaria;
        this.dtRicevibilita = dtRicevibilita;
        this.dtScadenzaDomande = dtScadenzaDomande;
        this.dtScadenzaDomandeRitardo = dtScadenzaDomandeRitardo;
        this.dtScadenzaDomandeRitiroparz = dtScadenzaDomandeRitiroparz;
    }
}
