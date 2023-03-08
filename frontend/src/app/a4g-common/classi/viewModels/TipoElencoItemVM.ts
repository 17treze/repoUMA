import { TipoElencoEnum } from './../enums/uma/TipoElenco.enum';

export interface TipoElencoItemVM {
    nome: string;
    tipo: keyof typeof TipoElencoEnum;
    loading: boolean;
}

