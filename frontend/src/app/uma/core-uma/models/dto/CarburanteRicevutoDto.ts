import { CarburanteDto } from "./CarburanteDto";

export interface TrasferimentoDto {
    id: number;
    data: string;
    carburante: CarburanteDto;
    mittente: {cuaa: string , denominazione: string};
    destinatario: {cuaa: string , denominazione: string};
}