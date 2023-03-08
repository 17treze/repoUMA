/* Response servizio di ricerca dei caa */
import { TipoDocumentoUma } from '../enums/TipoDocumentoUma.enum';
export class DomandaUmaDto {
    id: number;
    cuaa: string;
	campagna: number;
    stato: string;
    denominazione: string;
    protocollo: string;
    dataPresentazione?: string;
    tipo?: TipoDocumentoUma;
}
