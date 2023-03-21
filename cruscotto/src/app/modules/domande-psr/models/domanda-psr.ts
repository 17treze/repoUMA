import { Operazione } from './operazione';

export class DomandaPsr {
    campagna: number;
    numeroDomanda: number;
    dataPresentazione: Date;
    stato: string;
    sottoStato: string;
    cuaa: string;
    modulo: string;
    operazioni: Array<Operazione>;

    // Mappa prettamente FE. Viene utilizzato per mostrare le Icon degli interventi richiesti della domanda.
    mapOperazioniDaMostrare: Map<string, boolean>;
}
