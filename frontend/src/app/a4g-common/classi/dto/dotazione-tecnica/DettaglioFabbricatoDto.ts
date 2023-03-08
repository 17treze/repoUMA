import { EsitoValidazioneParticellaCatastoEnum } from './../../enums/dotazione.-tecnica/EsitoValidazioneParticellaCatasto.enum';
import { SelectItem } from 'primeng/api';
import { FabbricatoTypeEnum } from '../../enums/dotazione.-tecnica/FabbricatoTipologia.enum';
import { SottoTipoDto } from "./TipologiaDto";

// Strutture zootecniche Stalle e Aree scoperte
export interface DettaglioFabbricatoDto {
	id: string;
	type: keyof typeof FabbricatoTypeEnum;
	sottotipo: SottoTipoDto;
	denominazione: string;
	indirizzo: string;
	comune: string;
	volume: string;
	superficie: string;
	descrizione: string;
	tipoConduzione: string;
	datiCatastali: Array<DatiCatastaliDto>;
}

// Serre
export interface DatiSerreProtezioniDto extends DettaglioFabbricatoDto {
	impiantoRiscaldamento: boolean;
	annoCostruzione: string;
	titoloConformitaUrbanistica: string;
	tipologiaMateriale: string;
	annoAcquisto: string;
}

// Fabbricati Strumentali
export interface DettaglioFabbricatiStrumentaliDto extends DettaglioFabbricatoDto {
	superficieCoperta: string;
	superficieScoperta: string;
}

// Strutture zootecniche per Stoccaggio Letami o Liquami
export interface DettaglioZootecniaStoccaggioDto extends DettaglioFabbricatoDto {
	altezza: string;
	copertura: string;
}

export interface DatiCatastaliDto {
	comune?: string;   /** solo per particella in Trentino */
	sub?: string;        /** solo per particella in Trentino */
	sezione?: string;           /** solo per particella Fuori Trentino */
	foglio?: string;            /** solo per particella Fuori Trentino */
	tipologia: string;
	particella: string;
	denominatore?: string;
	inTrentino: boolean;
	indirizzo?: string;
	superficie?: number;
	consistenza?: string;
	categoria?: string;
	note?: string;
	esito: EsitoValidazioneParticellaCatastoEnum;
}