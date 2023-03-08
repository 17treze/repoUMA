import { DichiarazioneAntimafia } from 'src/app/fascicolo/antimafia/classi/dichiarazioneAntimafia';

export class DichiarazioneAntimafiaConEsiti extends DichiarazioneAntimafia {
    // id domande collegate
    idsDomandaDu: number[];
    idsDomandaStrutturale: number[];
    idsDomandaSuperficie: number[];
    dtBdna: Date;
    protocolloBdna: string;
    esito: string;
    esitoDescrizione: string;
    esitoInvioAgea: string;
    esitoInvioBdna: string;
}