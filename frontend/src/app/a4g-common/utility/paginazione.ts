import { HttpParams } from "@angular/common/http";
import { StringSupport } from "./string-support";

export class Paginazione {

    pagina: number;
    numeroElementiPagina: number;
    proprieta: String;
    ordine: String;

    public static of(pagina: number,
        numeroElementiPagina: number,
        proprieta: String,
        ordine: String): Paginazione {
        let paginazione: Paginazione = new Paginazione();
        paginazione.pagina = pagina;
        paginazione.numeroElementiPagina = numeroElementiPagina;
        paginazione.proprieta = proprieta;
        paginazione.ordine = ordine;
        return paginazione;
    }

    public static fillHttpParamsWith(httpParams: HttpParams, paginazione: Paginazione): HttpParams {
        if (paginazione != null) {
            if (paginazione.numeroElementiPagina != null)
                httpParams = httpParams.append('numeroElementiPagina', paginazione.numeroElementiPagina.toString());
            if (paginazione.pagina != null)
                httpParams = httpParams.append('pagina', paginazione.pagina.toString());
            if (StringSupport.isNotEmpty(paginazione.proprieta))
                httpParams = httpParams.append('proprieta', paginazione.proprieta.toString());
            if (StringSupport.isNotEmpty(paginazione.ordine))
                httpParams = httpParams.append('ordine', paginazione.ordine.toString());
        }
        return httpParams;
    }

    public static getOrdine(n: number) {
        if (n === 1) {
            return SortDirection.ASC;
        } else {
            return SortDirection.DESC;
        }
    }
}

export enum SortDirection {
    ASC = 'ASC',
    DESC = 'DESC'
}

export interface PaginatorEvent {
    filters: any;
    first: number;
    globalFilter: any;
    multiSortMeta: any;
    rows: number;
    sortField: string;
    sortOrder: number;
}
