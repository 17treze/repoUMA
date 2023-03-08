import { LavorazioneViewModel } from './LavorazioneVIewModel';

export interface LavorazioneFabbricatiViewModel extends LavorazioneViewModel {
    idFabbricato: number;
    comuneProvincia: string;
    volume: string;
    particella: string;
    subalterno: string;
    mesi?: number; /** solo per le lavorazioni sotto-serra */
}