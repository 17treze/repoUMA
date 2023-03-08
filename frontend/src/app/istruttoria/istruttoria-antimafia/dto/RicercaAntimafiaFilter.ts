import { StatoDichiarazione } from 'src/app/fascicolo/antimafia/classi/statoDichiarazioneEnum';

export class RicercaAntimafiaFilter {
    statoDichiarazione: StatoDichiarazione;
    pagSize: number;
    pagStart: number;
    sortBy: string;
    filtroGenerico: string;
}