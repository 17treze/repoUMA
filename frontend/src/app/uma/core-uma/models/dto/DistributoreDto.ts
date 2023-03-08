export interface UtenteDistributoreDto {
    comune: string;
    dataFine: Date;
    dataInizio: Date;
    denominazioneAzienda: string;
    id: number;
    indirizzo: string;
    provincia: string;
}

export interface DistributoreDto {
    denominazione?: string;
    comune?: string;
    indirizzo?: string;
    provincia?: string;
    identificativo: number; /** id ags */
    id?: number;            /** id uma */
}