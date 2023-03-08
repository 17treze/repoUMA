import { MotivazioneConsuntivo } from "../enums/MotivazioneConsuntivo.enum";
import { TipoAllegatoConsuntivo } from "../../../../a4g-common/classi/enums/uma/TipoAllegatoConsuntivo.enum";
import { TipoCarburanteConsuntivo } from "../enums/TipoCarburanteConsuntivo.enum";
import { TipoConsuntivo } from "../enums/TipoConsuntivo";

export class ConsuntivoDto {
    id: string;
    tipo: keyof typeof TipoConsuntivo;
    carburante: keyof typeof TipoCarburanteConsuntivo;
    quantita: number;
    motivazione?: keyof typeof MotivazioneConsuntivo;
    infoAllegati?: Array<InfoAllegatoConsuntivoDto>; 
}

export class InfoAllegatoConsuntivoDto {
    id: string;
    nome: string;
    descrizione: string;
    tipoDocumento: TipoAllegatoConsuntivo;
}