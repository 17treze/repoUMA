import { CarburanteDto } from './CarburanteDto';

export interface CarburanteTotale<T> {
    
    dati: Array<T>;
    totale: CarburanteDto;
}