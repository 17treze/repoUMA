import { CarburanteDto } from './CarburanteDto';
import { DistributoreDto } from './DistributoreDto';
export interface PrelievoDto {
    id?: number;
    distributore?: DistributoreDto;
    carburante: CarburanteDto;
    data?: Date;
    cuaa?: string;
    denominazione?: string;
    estremiDocumentoFiscale: string;
    isConsegnato: boolean;
}