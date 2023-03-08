import { TipologiaPossessoEnum } from './../../enums/dotazione.-tecnica/TipologiaPossesso.enum';
import { MotoreDto } from './MotoreDto';
import { SottoTipoDto } from './TipologiaDto';

export interface DettaglioMacchinaDto {
    id: number;
    sottotipo: SottoTipoDto;
    marca: string;
    modello: string;
    numeroMatricola: string;
    numeroTelaio: string;
    annoCostruzione: number;
    tipoPossesso: keyof typeof TipologiaPossessoEnum;
    codiceFiscale: string;
    ragioneSociale: string;
    targa: string;
    dataImmatricolazione?: Date;        // non usato sul FE
    motore: MotoreDto;                  /** null in caso di macchine senza motore */
    flagMigrato: number; // non usato sul FE campi gestione migrazione
}