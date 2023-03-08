import { StatoDichiarazioneConsumiEnum } from 'src/app/a4g-common/classi/enums/uma/StatoDichiarazioneConsumi.enum';

export class DichiarazioneConsumiFilter {
	id?: number;
	cuaa?: string;
	campagna?: Array<string>;
	stati?: Array<keyof typeof StatoDichiarazioneConsumiEnum>;
	denominazione?: string;
	// Paginazione
	numeroElementiPagina: number;
	pagina: number;
	// Ordinamento
	proprieta?: string;
	ordine?: string;
}