import { StatiRichiesta } from '../../shared/StatiRichiesta.enum';
import { AziendaAgricola, DatiAggiuntivi } from './search-results.model';
export interface Detail {
  id: number;
  utente: string;
  data: Date;
  tipo: string;
  stato: StatiRichiesta;
  esito: string;
  aziendaAgricola: AziendaAgricola;
  campagna: number;
  datiAggiuntivi: DatiAggiuntivi;
}
