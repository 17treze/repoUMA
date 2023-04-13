import { StatoRichiesta } from './stato-richiesta';

export interface RichiestaCarburanteFilter {
  cuaa: string;
  campagna: number[];
  stati: StatoRichiesta[];
	id?: number;
	denominazione?: string;
	numeroElementiPagina: number;
	pagina: number;
	proprieta?: string;
	ordine?: Ordine;
}


export enum Ordine {
  ASC,
  DESC
}