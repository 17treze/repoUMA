import { TipoCarburante } from '../enums/TipoCarburante.enum';

export class FabbisognoDto {
    quantita: string;
    carburante: keyof typeof TipoCarburante;
}
