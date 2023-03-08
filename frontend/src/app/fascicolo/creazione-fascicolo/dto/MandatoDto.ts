import { TipoFileEnum } from './datiCartaServizi';

export class MandatoDto {
    codiceFiscale: string;
    codiceFiscaleRappresentante: string;
    contratto: File;
    allegati: AllegatoMandato[];
    identificativoSportello: number;
}

export class AllegatoMandato {
    file: File;
    tipologia: string; // TipoFileEnum
    descrizione?: string;
}
