import { IstruttoriaDomandaUnica } from './classi/IstruttoriaDomandaUnica';
import { StatoProcesso } from '../../istruttoria-antimafia/dto/StatoProcesso';
import { TipoProcesso } from '../../istruttoria-antimafia/dto/TipoProcesso';

export class ProcessoIstruttoriaPac1420 {
    idProcesso: number;
    idTipoProcesso: string;
    idDatiSettore: number;
    percentualeAvanzamento: number;
    idStatoProcesso: StatoProcesso;
    numeroDomandeDaElaborare: number;
    domandeDaElaborare: Array<IstruttoriaDomandaUnica>;
    sostegno: String;
    processo: TipoProcesso;
    idIstruttorie: number[];
}