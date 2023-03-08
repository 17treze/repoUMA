import { DatiIstruttoria } from './datiIstruttoria';
import { RichiestaSuperficie, PaginaRichiestaSuperficie } from './richiestaSuperficie';
import { DomandaIstruttoriaRiepilogo } from './domandaIstruttoriaRiepilogo';
import { DichiarazioneDu } from './dichiarazioneDu';
import { KeyValue } from 'src/app/a4g-common/classi/KeyValue';
import { Controllo } from './controllo';
import { InfoGeneraliDomanda } from './infoGeneraliDomanda';
import { Richieste } from './richieste';
//in java è DomandaUnicaDettaglio
export interface DomandaIstruttoriaDettaglio {
	id: number;
	infoGeneraliDomanda: InfoGeneraliDomanda;
	statoSostegno: string;
	supImpegnataLorda: number;
	supImpegnataNetta: number;
	richieste: Richieste;
	// TODO: manca categoria
	dichiarazioni: Array<DichiarazioneDu>;
	informazioniDomanda: Array<KeyValue>;
	controlliSostegno: Array<Controllo>;
	datiDomanda: Array<Controllo>;
	// datiParticella
	datiIstruttoria: DatiIstruttoria;
}
