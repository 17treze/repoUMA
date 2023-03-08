import { ClienteDto } from './ClienteDto';

export interface ClienteConsumiDto extends ClienteDto {
    benzina: boolean;
    gasolio: boolean;
}