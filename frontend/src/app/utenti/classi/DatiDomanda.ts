import { DatiAnagrafici } from "./datiAnagrafici";
import { ResponsabilitaRichieste } from "./responsabilita-richieste";

export enum TipoDomandaRegistrazione {
    RIDOTTA_AZIENDA = "RIDOTTA_AZIENDA",
    COMPLETA = "COMPLETA"
}

export class DatiDomanda {
    id: number;
    datiAnagrafici: DatiAnagrafici;
    responsabilitaRichieste: ResponsabilitaRichieste;
    servizi: string[];  //TODO servizi -> applicativi
    autorizzazioni: string;
    luogo: 'Trento';
    data: Date = new Date();
    dataProtocollazione: Date;
    stato: string;
    idProtocollo: string;
    tipoDomandaRegistrazione: TipoDomandaRegistrazione;
}
