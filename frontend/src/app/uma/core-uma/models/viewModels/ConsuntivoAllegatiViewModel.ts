import { MotivazioneConsuntivoDto } from '../dto/MotivazioneConsuntivoDto';
import { MotivazioneConsuntivoTipo } from '../enums/MotivazioneConsuntivoTipo.enum';
import { TipoCarburanteConsuntivo } from '../enums/TipoCarburanteConsuntivo.enum';
import { TipoConsuntivo } from '../enums/TipoConsuntivo';

export interface ConsuntivoAllegatiVM {
    motivazione: MotivazioneConsuntivoDto;
    tipo: keyof typeof MotivazioneConsuntivoTipo;
    tipoConsuntivo: TipoConsuntivo;
    value: string;
    tipoCarburante: keyof typeof TipoCarburanteConsuntivo
}