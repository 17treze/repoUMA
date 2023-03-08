import { Domanda } from './domain/domandaUnica';

export class ProcessoIstruttoria {

    annoCampagna: number;
    idProcesso: number;
    idTipoProcesso: string;
    idDatiSettore: number;
    percentualeAvanzamento: number;
    idStatoProcesso: string;
    numeroDomandeDaElaborare: number;
    domandeDaElaborare: Array<Domanda>;
    sostegno: String;

}
