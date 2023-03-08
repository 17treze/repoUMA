import { Injectable } from '@angular/core';
import { FiltroRicercaDomande } from './domain/filtroRicercaDomande';
import { ElencoAccoppiatiComponent } from './cruscotto-accoppiati-container/cruscotto-accoppiati/domande-accoppiati/elenco-accoppiati/elenco-accoppiati.component';

@Injectable({
  providedIn: 'root'
})
export class FiltroAccoppiatiService {
  filtro: FiltroRicercaDomande;
  elencoDomande: ElencoAccoppiatiComponent;
  constructor() { }
}
