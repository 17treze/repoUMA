import { TipoAllegatoConsuntivo } from '../../../../a4g-common/classi/enums/uma/TipoAllegatoConsuntivo.enum';
import { MotivazioneConsuntivo } from "../enums/MotivazioneConsuntivo.enum";

export class MotivazioneConsuntivoDto { // MotivazioneConsuntivoDto
    motivazione: keyof typeof MotivazioneConsuntivo;
    allegati: Array<AllegatoMotivazioneView>;
    idConsuntivo: string;
}

export class AllegatoMotivazioneView {
    idConsuntivo?: string;
    idDichiarazione?: string;
    idAllegato: string; // se null, nuovo allegato altrimenti è uno precaricato
    file: File;
    name: string;
    descrizione?: string;
    tipoDocumento?: keyof typeof TipoAllegatoConsuntivo;
}