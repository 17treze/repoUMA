import { StatiRichiesta } from '../../shared/StatiRichiesta.enum';
import { Allegati } from '../detailgis/allegati.model';
import { TipoInterventoColturale } from '../detailgis/tipoInterventoColturale.model';
export interface SearchResults {
  id: number;
  utente: string;
  data: Date;
  tipo: string;
  stato: StatiRichiesta;
  esito: string;
  aziendaAgricola: AziendaAgricola;
  campagna: number;
  datiAggiuntivi: DatiAggiuntivi;
  sezioniCatastali: Array<SezioniCatastali>;
  comuniMapped: Array<ComuniMapped>;
  allegati: Allegati;
  risultat: any;
  readOnly: string;
}
export interface Lavorazione{
  id: number;
  utente: string;
  utenteAgs: string;
  stato: StatiRichiesta;
  dataInizioLavorazione: string;
  dataFineLavorazione: string;
  dataUltimaModifica: string;
  note: string;
  titolo: string;
  sopralluogo: string;
  scala: number;
  xUltimoZoom: number;
  yUltimoZoom: number;
  readOnly: string;

}
export interface Count {
  number;
}
export interface AziendaAgricola {
  cuaa: string;
  ragioneSociale: string;
}
export interface DatiAggiuntivi {
  visibileOrtofoto: boolean;
  tipoInterventoColturale: TipoInterventoColturale;
  periodoIntervento: PeriodoIntervento;
}

export interface PeriodoIntervento{
  dataInizio: any;
  dataFine: any;
}
export interface SezioniCatastali{
  [index: number]: string;
}

export interface ComuniMapped{
  [index: number]: string;
  cod: string;
}
