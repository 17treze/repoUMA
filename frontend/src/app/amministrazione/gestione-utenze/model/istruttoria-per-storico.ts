import { Distributore } from 'src/app/a4g-common/classi/distributore';
import {Azienda, Profilo} from '../../ricerca-utenti/dto/Profili';
import {EnteSede} from './ente-sede';
import {Utente} from './utente';

export class IstruttoriaPerStorico {
    id: number;
    variazioneRichiesta: string;
    testoComunicazione: string;
    motivazioneRifiuto: string;
    profili: Array<Profilo>;
    sedi: Array<EnteSede>;
    distributori: Array<Distributore>;
    aziende: Array<Azienda>;
    testoMailInviata: string;
    note: string;
    istruttore: Utente;
    dataTermineIstruttoria: string;
    motivazioneDisattivazione: string;
}
