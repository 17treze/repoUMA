import { StatoDichiarazioneConsumiEnum } from 'src/app/a4g-common/classi/enums/uma/StatoDichiarazioneConsumi.enum';
import { StatoDomandaUma } from 'src/app/a4g-common/classi/enums/uma/StatoDomandaUma.enum';

export class DomandaUmaFilter {
	id?: number;
	cuaa?: string;
	campagna?: Array<string>;
	stati?: Array<keyof typeof StatoDomandaUma | keyof typeof StatoDichiarazioneConsumiEnum>;
	denominazione?: string;
	// Paginazione
	numeroElementiPagina: number;
	pagina: number;
	// Ordinamento
	proprieta?: string;
	ordine?: string;
}