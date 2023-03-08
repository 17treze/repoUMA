import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/internal/Observable';
import { Domanda } from '../domain/domandaUnica';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { PaginaDomande } from '../domain/paginaDomande';
import { Configuration } from 'src/app/app.constants';
import { CuaaDenominazione } from '../domain/cuaa-denominazione';
import { Cached } from 'src/assets/decorators/cached';
import { Paginazione } from 'src/app/a4g-common/utility/paginazione';
import { PaginatedContent } from 'src/app/a4g-common/utility/paginated-content';


@Injectable()
export class ElencoDomandeService {
  constructor(
    private http: HttpClient, private _configuration: Configuration) {}

  public getListaDomandePerStato(jsonListaDomandaPerStato): Observable<Array<Domanda>> {
    console.log('getListaDomandePerStato');
    const a: Observable<Array<Domanda>> = this.http.get<Array<Domanda>>
      (this._configuration.UrlGetListaDomandePerStato + jsonListaDomandaPerStato);
    return a;
  }

  public getPaginaDomandeAttuale(jsonParametri, jsonPaginazione, jsonOrdinamento ): Observable<PaginaDomande> {
    const a: Observable<PaginaDomande> = this.http.get<PaginaDomande>
      (this._configuration.UrlGetListaDomandePerStatoPaginata.replace('${parametri}', String(jsonParametri))
      .replace('${paginazione}', String(jsonPaginazione)).replace('${ordinamento}', String(jsonOrdinamento)));
    return a;
  }

  @Cached()
  public getSuggestionsAutocomplete(jsonParametri): Observable<Array<CuaaDenominazione>> {
    const a: Observable<Array<CuaaDenominazione>> =
     this.http.get<Array<CuaaDenominazione>>(this._configuration.UrlGetElencoCuaa + jsonParametri);
     return a;
  }

  @Cached()
  getCuaaSuggestionsAutocomplete(
    statoSostegno: string, sostegno: string, annoCampagna: string, cuaa: string, tipo: string,
    paginazione: Paginazione) {
      let data: any = {
        'stato-sostegno': statoSostegno,
        'sostegno': sostegno,
        'anno-campagna': annoCampagna,
        'cuaa': cuaa,
        'tipo': tipo,
        ...paginazione
      };
      return this.http.get<PaginatedContent<string>>(
        this._configuration.UrlGetElencoCuaaFiltrati, { params: data });
  }
  
  @Cached()
  getRagioneSocialeSuggestionsAutocomplete(
    statoSostegno: string, sostegno: string, annoCampagna: string, ragioneSociale: string, tipo: string,
    paginazione: Paginazione) {
      let data: any = {
        'stato-sostegno': statoSostegno,
        'sostegno': sostegno,
        'anno-campagna': annoCampagna,
        'ragione-sociale': ragioneSociale,
        'tipo': tipo,
        ...paginazione
      };
      return this.http.get<PaginatedContent<string>>(
        this._configuration.UrlGetElencoRagioneSocialeFiltrati, { params: data });
  }

  public postAggiornaBloccoDomande(jsonParametri): Observable<void> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    const a: Observable<void> =
    this.http.post<void>(this._configuration.UrlAggiornaStatoBloccoDomande, jsonParametri, {headers: headers}  );
    return a;
  }
}
