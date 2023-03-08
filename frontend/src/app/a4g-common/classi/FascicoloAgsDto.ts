export class FascicoloAgsDto {
  cuaa: string;
  dataMorteTitolare?: string; /** Presente solo per persone fisiche */
  dataAggiornamento: Date;
  dataCostituzione: Date;
  dataValidazione: Date;
  denominazione: string;
  idAgs: number;
  iscrittoSezioneSpecialeAgricola: boolean;
  nonIscrittoSezioneSpecialeAgricola: boolean;
  organismoPagatore: string;
  pec: string;
  stato: string;
  detenzioni: DetenzioniAgsDto[];
}

export class DetenzioniAgsDto {
  caa: string;
	tipoDetenzione: string;
	sportello: string;
	identificativoSportello: string;
}

export enum TipoDetenzioneAgs {
  DELEGA = "DELEGA", 
	MANDATO = "MANDATO"
}
