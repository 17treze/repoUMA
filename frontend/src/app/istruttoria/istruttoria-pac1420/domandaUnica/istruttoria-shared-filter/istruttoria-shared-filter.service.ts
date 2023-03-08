import { Injectable } from '@angular/core';
import { IstruttoriaFiltroRicercaDomande } from './istruttoria-filtro-ricerca-domande';

@Injectable({
  providedIn: 'root'
})
export class IstruttoriaSharedFilterService {
  filtro: IstruttoriaFiltroRicercaDomande;
  constructor() { }
}
