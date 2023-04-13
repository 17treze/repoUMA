import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { UMA } from 'src/app/app.constants';
import { HttpHelperSupport } from 'src/app/shared/utilities/http-helper.support';
import { DomandaUma } from './models/domanda-uma';
import { DomandeUmaPaginate } from './models/domande-uma-paginate';
import { RichiestaCarburanteFilter } from './models/richiesta-carburante-filter';
import { StatoRichiesta } from './models/stato-richiesta';

@Injectable({
  providedIn: 'root',
})
export class DomandeUmaService {
  public getRichiesteUrl = UMA + 'richieste';
  public getRichiestaUrl = UMA + 'richieste/${id}';
  public getCarburantePrelevatoUrl = UMA + 'richieste/${campagna}/prelievi';
  public getCarburanteRicevutoUrl = UMA + 'trasferimenti';
  private MAX_BACKEND_INTEGER = 2147483647;

  constructor(protected http: HttpClient) { }

  public getDomandeUma(
    cuaa: string,
    stati: StatoRichiesta[],
    campagne: number[]
  ): Observable<DomandeUmaPaginate> {
    const filter: RichiestaCarburanteFilter = {
      cuaa: cuaa,
      campagna: campagne,
      stati: stati,
      pagina: 0,
      numeroElementiPagina: this.MAX_BACKEND_INTEGER
    };

    const queryString =
      '?' + HttpHelperSupport.buildQueryStringFromObject(filter);
    return this.http.get<DomandeUmaPaginate>(this.getRichiesteUrl + queryString);
  }

  public getDomandaUma(id: string) {
    return this.http.get<DomandaUma>(this.getRichiestaUrl.replace('${id}', id));
  }

  public getRettificheUma(cuaa: string, campagne: number[]): Observable<DomandeUmaPaginate> {
    const filter: RichiestaCarburanteFilter = {
      cuaa: cuaa,
      campagna: campagne,
      stati: [StatoRichiesta.RETTIFICATA],
      pagina: 0,
      numeroElementiPagina: this.MAX_BACKEND_INTEGER
    };
    const queryString = '?' + HttpHelperSupport.buildQueryStringFromObject(filter);
    return this.http.get<DomandeUmaPaginate>(this.getRichiesteUrl + queryString);
  }

  public getCarburantePrelevato(cuaa: string, campagna: string) {
    return this.http.get<any>(
      this.getCarburantePrelevatoUrl.replace('${campagna}', campagna), {
      params: { cuaa: cuaa }
    }
    );
  }

  public getCarburanteRicevuto(cuaa: string, campagna: number) {
    const queryString =
      '?' + HttpHelperSupport.buildQueryStringFromObject({ cuaaDestinatario: cuaa, campagna: campagna.toString() });
    return this.http.get<any>(this.getCarburanteRicevutoUrl + queryString);
  }
}
