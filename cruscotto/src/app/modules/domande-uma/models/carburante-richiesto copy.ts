import { TipoCarburante } from './tipo-carburante';

export interface DettaglioCarburante {
  tipo: TipoCarburante;
  assegnato: number;
  prelevato: number;
  ricevuto: number;
}
