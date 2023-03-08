import { StatoDomandaUma } from 'src/app/a4g-common/classi/enums/uma/StatoDomandaUma.enum';
import { CarburanteRichiestoDto } from './CarburanteRichiestoDto';
export class RichiestaCarburanteDto extends CarburanteRichiestoDto {
	id: number;
	cuaa: string;
	campagna: number;
	dataPresentazione: Date;
	dataProtocollazione: Date;
    protocollo: string;
	cfRichiedente: string;
	stato: keyof typeof StatoDomandaUma;
	denominazione: string;
	idRettificata?: number;                  /** se la richiesta è una RETTIFICA questo id sarà diverso da null */
	haDichiarazioni: boolean;				  /** se ci sono dichiarazioni per quella domanda */
	/* se ci sono macchine a gasolio o benzina le sezioni presenti negli step successivi possono essere compilate */
	haMacchineGasolio: boolean;               /** se ci sono macchine a gasolio allora le relative sezioni possono essere compilate */
	haMacchineBenzina: boolean;               /** se ci sono macchine a benzina allora le relative sezioni possono essere compilate */
	haFabbricati: boolean;                    /** se non ci sono trasforazioni prodotti allora la sezione relativa è disabilitata */
	haSuperfici: boolean;                     /** se non ci sono superfici allora la sezione Superficie è disabilitata */
	haSerre: boolean;                         /** se non ci sono coltivazioni sotto serre allora la sezione relativa è disabilitata */
}
