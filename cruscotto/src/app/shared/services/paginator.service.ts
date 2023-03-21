import { Injectable } from '@angular/core';
import { Paginazione, SortDirection } from '../models/paginazione';


@Injectable({
  providedIn: 'root'
})
export class PaginatorService {

  constructor() { }

  getDefaultPagination(nElems: number, field: string): Paginazione {
    const paginazione: Paginazione = Paginazione.of(
      0, nElems, field, SortDirection.ASC);
    return paginazione;
  }

  getPagination(startIndex: number, nElems: number, field: string, direction: string): Paginazione {
    const paginazione: Paginazione = Paginazione.of(
      startIndex, nElems, field, direction);
    return paginazione;
  }
}
