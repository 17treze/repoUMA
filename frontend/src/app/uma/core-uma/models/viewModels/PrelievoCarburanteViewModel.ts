import { CarburantePrelevabileVM } from './CarburantePrelevabileViewModel';

export interface PrelievoCarburanteVM {
    id: number;
    identificativoDistributore: number;
    estremiDocumentoFiscale: string;
    data: Date;
    isConsegnato: boolean;
    carburante: Array<CarburantePrelevabileVM>;
}