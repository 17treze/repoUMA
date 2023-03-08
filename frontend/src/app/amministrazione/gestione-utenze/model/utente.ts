import {Profilo} from '../../ricerca-utenti/dto/Profili';
import {EnteSede} from './ente-sede';

export class Utente {

    id: number;
    identificativo: string;
    codiceFiscale: string;
    nome: string;
    cognome: string;
    profili: Array<Profilo>;
    sedi: Array<EnteSede>;

}
