class IstruttoriaProfiloUtenteBase {
  profili: number[];
  sedi: number[];
  variazioneRichiesta: string;
}

export class IstruttoriaProfiloUtente extends IstruttoriaProfiloUtenteBase {
  id: number;
  idDomanda: number;
  motivazioneRifiuto: string;
  testoComunicazione: string;
}

export class IstruttoriaProfiloUtenteSenzaDomanda extends IstruttoriaProfiloUtenteBase {
  profiliDaDisattivare: number[];
  motivazioneDisattivazione: string;
}