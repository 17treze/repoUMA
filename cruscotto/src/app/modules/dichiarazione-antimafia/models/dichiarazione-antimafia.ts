export interface Residenza {
  indirizzo: string;
  comune: string;
  provincia: string;
  CAP: string;
}

export interface Richiedente {
  nome: string;
  cognome: string;
  codiceFiscale: string;
  comuneNascita: string;
  provinciaNascita: string;
  dataNascita: string;
  sesso: string;
  residenza: Residenza;
  carica: string;
  dtInizioCarica: string;
  dtFineCarica: string;
}

export interface Sede {
  provincia: string;
  comune: string;
  ccomune: string;
  toponimo: string;
  via: string;
  ncivico: string;
  cap: string;
  frazione: string;
}

export interface EstremiCCIAA {
  sede: string;
  numeroIscrizione: string;
  dataIscrizione: string;
}

export interface DettaglioPersonaGiuridica {
  formaGiuridica: string;
  estremiCostituzione: string;
  capitaleSociale: string;
  durata: string;
}

export interface Carica {
  codice: string;
  tipologia: string;
  dataInizio: string;
  dataFine: string;
  href: string;
  selezionato: boolean;
  tipoAllegato: string;
  dichiarazione: boolean;
}

export interface Residenza2 {
  indirizzo: string;
  comune: string;
  provincia: string;
  CAP: string;
}

export interface FamiliariConviventi {
  nome: string;
  cognome: string;
  codiceFiscale: string;
  comuneNascita: string;
  provinciaNascita: string;
  dataNascita: string;
  sesso: string;
  residenza: Residenza2;
  gradoParentela: string;
}

export interface SoggettiImpresa {
  nome: string;
  cognome: string;
  codiceFiscale: string;
  comuneNascita: string;
  provinciaNascita: string;
  dataNascita: string;
  sesso: string;
  indirizzoResidenza: string;
  comuneResidenza: string;
  provinciaResidenza: string;
  capResidenza: string;
  carica: Carica[];
  familiariConviventi: FamiliariConviventi[];
}

export interface DettaglioImpresa {
  formaGiuridicaCodice: string;
  formaGiuridicaDescrizione: string;
  denominazione: string;
  sedeLegale: string;
  sede: Sede;
  estremiCCIAA: EstremiCCIAA;
  codiceFiscale: string;
  partitaIva: string;
  indirizzoPEC: string;
  dettaglioPersonaGiuridica: DettaglioPersonaGiuridica;
  soggettiImpresa: SoggettiImpresa[];
  aziendeCollegate: any[];
}

export interface DatiDichiarazione {
  richiedente: Richiedente;
  dettaglioImpresa: DettaglioImpresa;
}

export interface Stato {
  id: number;
  identificativo: string;
  descrizione: string;
}

export interface Azienda {
  id: number;
  cuaa: string;
}

export interface DichiarazioneAntimafia {
  id: number;
  version: number;
  datiDichiarazione: DatiDichiarazione;
  dtGenerazionePdf?: any;
  dtInizioCompilazione: Date;
  dtUltimoAggiornamento: Date;
  dtUploadPdfFirmato: Date;
  dtFine?: any;
  idProtocollo: string;
  pdfFirmato?: any;
  pdfGenerato?: any;
  tipoPdfFirmato?: any;
  pdfFirmatoName?: any;
  dtProtocollazione: Date;
  stato: Stato;
  azienda: Azienda;
  assenzaDt: boolean;
  allegatoFamiliariConviventi?: any;
  denominazioneImpresa: string;
  esito: string;
  esitoDtElaborazione: Date;
  esitoDescrizione: string;
  esitoInvioAgea: string;
  esitoInvioBdna: string;
  dataScadenza: Date;
}
