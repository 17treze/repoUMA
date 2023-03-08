import { Injectable } from '@angular/core';
import { Paginazione, SortDirection } from 'src/app/a4g-common/utility/paginazione';

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

  compare(a: number | string, b: number | string, isAsc: boolean) {
    if (a === b) {
      return 0;
    }
    if (a == null) {
      return 1; // spazi alla fine, invece con a = ''; spazi in alto
    }
    if (b == null) {
      return -1; // spazi alla fine, invece con  b = ''; spazi in alto
    }
    return (a <= b ? -1 : 1) * (isAsc ? 1 : -1);
  }

  getPage(dataSource: Array<any>, pagina: number, numElemPerPagina: number) {
    const page = dataSource.slice(pagina * numElemPerPagina, (pagina + 1) * numElemPerPagina);
    return page;
  }
}
