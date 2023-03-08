import { TipoCarburante } from '../enums/TipoCarburante.enum';

export class MacchinaDto {
    id: string;
    descrizione: string;
    classe: string;
    marca: string;
    alimentazione: TipoCarburante;
    targa: string;
    possesso: string;
    identificativoAgs: string;
    isUtilizzata: boolean;
}
