export class CambioSportelloPatch {
  idNuovoSportello: number;
  motivazione: string;
  dataCambio: string;
}

export interface FascicoloDTO {
  cuaa: string;
  id: number;
  denominazione: string;
  impresa: PersonaDTO;
  mandatoDto: MandatoDTO;
}

export interface PersonaDTO {
  carica: string;
  codiceFiscale: string;
  tipo: 'PERSONA_FISICA' | 'PERSONA_GIURIDICA';
}

export interface MandatoDTO {
  codice: string;
  denominazione: string;
  id: number;
  sportello: SportelloDTO;
  dataInizio: string;
  dataFine: string;
  codiceFiscale: string;
  partitaIva: string;
  attoRiconoscimento: string;
  societaServizi: string;
  indirizzoSedeAmministrativa: Indirizzo;
  dataSottoscrizione: string;
}

export interface Indirizzo {
  via: string;
  denominazioneComune: string;
  denominazioneProvincia: string;
  siglaProvincia: string;
  cap: string;
  toponimo: string;
}

export interface SportelloDTO {
  cap: string;
  comune: string;
  denominazione: string;
  email: string;
  id: number;
  identificativo: number;
  indirizzo: string;
  provincia: string;
  telefono: string;
}

export interface ValidazioneFascicoloDto {
  cuaa: string;
  id: number;
  idValidazione: number;
  denominazioneImpresa: string;
  denominazioneSportello: string;
  dataValidazione: Date;
  dataModifica: Date;
  utenteValidazione: string;
}

export enum FascicoloDettaglio {
  DATI = 'DATI',
  DATI_AZIENDA = 'DATI_AZIENDA',
  MODALITA_PAGAMENTO = 'MODALITA_PAGAMENTO',
  LISTA_MACCHINARI = 'LISTA_MACCHINARI',
  LISTA_FABBRICATI = 'LISTA_FABBRICATI',
  FASCICOLI_VALIDATI = 'FASCICOLI_VALIDATI',
  DATI_SOSPENSIONE = 'DATI_SOSPENSIONE'
}

export interface DatiSospensioneFascicolo {
  dataInizio: Date;
  motivazioneInizio: string;
  dataFine: Date;
  motivazioneFine: string;
  utente: string;
}
