import {InformazioniGeneraliDomanda} from './informazioni-generali-domanda';
import {SostegnoDisaccoppiato} from "./sostegno-disaccoppiato";
import {SostegnoSuperfici} from "./sostegno-superfici";
import {DomandaIntegrativa} from "./domanda-integrativa";

import {SintesiRichieste} from './sintesi-richieste';

export class SostegniAllevamento {
  idIntervento: number;
	idAllevamento: number;
  codIntervento: string;
  sostegno: string;
	descIntervento: string;
	codIdAllevamento: string;
	codIdBdn: string;
	descAllevamento: string;
	specie: string;
	comune: string;
	indirizzo: string;
	codFiscaleProprietario: string;
	denominazioneProprietario: string;
	codFiscaleDetentore: string;
	denominazioneDetentore: string;
}

export class Richieste {
  sintesiRichieste: SintesiRichieste;
  sostegniAllevamento: Array<SostegniAllevamento>;
}

export class DomandaUnica {
  infoGeneraliDomanda: InformazioniGeneraliDomanda;
  richieste: Richieste;
  sostegnoDisaccoppiato: SostegnoDisaccoppiato;
  sostegnoSuperfici: SostegnoSuperfici;
  domandaIntegrativa: DomandaIntegrativa;
}
