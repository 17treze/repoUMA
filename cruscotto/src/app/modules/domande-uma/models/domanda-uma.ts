import { CarburanteRichiesto } from './carburante-richiesto';
import { StatoRichiesta } from './stato-richiesta';

export interface DomandaUma {
  id: number;
  cuaa: string;
  campagna: number;
  carburanteRichiesto: CarburanteRichiesto;
  dataPresentazione: string;
  dataProtocollazione: string;
  cfRichiedente: string;
  stato: StatoRichiesta;
  haMacchineBenzina: boolean;
  haMacchineGasolio: boolean;
  haFabbricati: boolean;
  haSerre: boolean;
  haSuperfici: boolean;
  haDichiarazioni: boolean;
  denominazione: string;
  note: string;
}
