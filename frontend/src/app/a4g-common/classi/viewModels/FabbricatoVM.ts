import { EsitoValidazioneParticellaCatastoEnum } from './../enums/dotazione.-tecnica/EsitoValidazioneParticellaCatasto.enum';
import { FabbricatoTypeEnum } from 'src/app/a4g-common/classi/enums/dotazione.-tecnica/FabbricatoTipologia.enum';
import { SelectItem } from 'primeng-lts';

export interface DettaglioFabbricatoVM {
    id?: number; // se null sono in creazione se != null sono in update
    type: keyof typeof FabbricatoTypeEnum;
    tipologiaFabbricato: SelectItem;
    sottoTipologiaFabbricato: SelectItem;
    sottoTipo?: SelectItem;
    fuoriTrentino: boolean;
    datiCatastali: DatiCatastaliVM;
    fabbricati?: DettaglioFabbricatoStrumentaleVM;
    serre?: DettaglioSerreVM;
    strutture?: TipiStruttureVM;
}

export interface ClassificazioneFabbricatoVM {
    tipologiaFabbricato: SelectItem;
    sottoTipologiaFabbricato: SelectItem;
    sottoTipo?: SelectItem;
}

export interface DettaglioFabbricatoStrumentaleVM { // FABBRICATI STRUMENTALI
    denominazione?: string;
    indirizzo: string;
    comune: string;  /** comune amministrativo */
    volume?: string;
    superficie: string;             /** superficie totale */
    superficieCoperta?: string;     /** DettaglioStrumentaliDto */
    superficieScoperta?: string;    /** DettaglioStrumentaliDto */
    tipoConduzione?: SelectItem;
    descrizione?: string;
}

export interface DettaglioSerreVM { // SERRE
    denominazione?: string;
    indirizzo: string;
    comune: string; /** comune amministrativo */
    volume: string;
    superficie?: string; /** superficie occupata */
    impiantoRiscaldamento?: boolean; /** DettaglioSerreDto */
    annoCostruzione?: string;       /** DettaglioSerreDto */
    tipologiaMateriale?: string;    /** DettaglioSerreDto */
    annoAcquisto?: string;          /** DettaglioSerreDto */
    titoloConformitaUrbanistica?: string; /** DettaglioSerreDto */
    tipoConduzione?: SelectItem;
    descrizione?: string;
}

export interface TipiStruttureVM {
    stoccaggioLiquami?: DettaglioStruttureZootecniaStoccaggioVM;
    stoccaggioLetami?: DettaglioStruttureZootecniaStoccaggioVM;
    stalle?: DettaglioStruttureZootecniaAreeScoperteStalleVM;
    areeScoperte?: DettaglioStruttureZootecniaAreeScoperteStalleVM;
}
export interface DettaglioStruttureZootecniaStoccaggioVM { // STOCCAGGIO LETAMI E LIQUAMI
    denominazione?: string;
    indirizzo: string;
    comune: string;  /** comune amministrativo */
    volume: string;
    superficie: string;
    altezza: string;        /** DettaglioStoccaggioDto */
    copertura?: SelectItem; /** DettaglioStoccaggioDto */
    tipoConduzione?: SelectItem;
    descrizione?: string;
}
export interface DettaglioStruttureZootecniaAreeScoperteStalleVM { // STOCCAGGIO LETAMI E LIQUAMI
    denominazione?: string;
    indirizzo: string;
    comune: string;  /** comune amministrativo */
    volume: string;
    superficie: string;
    tipoConduzione?: SelectItem;
    descrizione?: string;
}

export interface DatiCatastaliVM {
    comuneCatastale?: string;   /** solo per particella in Trentino */
    subalterno?: string;        /** solo per particella in Trentino */
    sezione?: string;           /** solo per particella Fuori Trentino */
    foglio?: string;            /** solo per particella Fuori Trentino */
    tipologia: SelectItem;
    particella: string;
    denominatore: string;
    inTrentino: boolean;
    esito: EsitoValidazioneParticellaCatastoEnum;
}

export interface ParticellaVm {
    tipologia: SelectItem | string;
    particella: string;
    denominatore: string;
    inTrentino: boolean;
    sezione?: string;           /** solo per particella Fuori Trentino */
    foglio?: string;            /** solo per particella Fuori Trentino */
    comuneCatastale?: string;   /** solo per particella in Trentino */
    subalterno?: string;        /** solo per particella in Trentino */
    indirizzo?: string;         /** solo per particella in Trentino - dati dal catasto */
    superficie?: string;        /** solo per particella in Trentino - dati dal catasto */
    consistenza?: string;       /** solo per particella in Trentino - dati dal catasto */
    categoria?: string;         /** solo per particella in Trentino - dati dal catasto */
    note?: string;              /** solo per particella in Trentino - dati dal catasto */
    esito: EsitoValidazioneParticellaCatastoEnum;
}
export interface InfoCatastoVm {
    indirizzo: string;
    superficie: string;
    consistenza: string;
    categoria: string;
    note: string;
}