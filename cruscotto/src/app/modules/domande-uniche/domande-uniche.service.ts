import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { IFascicolo } from './models/fascicolo.model';
import { CaricaDto } from '../domande-uniche/models/facicoloImprese.model';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { FASCICOLO_SERVER, AGS_SERVER, ISTRUTTORIA_SERVER, ANAGRAFICA_SERVER } from 'src/app/app.constants';
import { DomandaUnica } from './models/domanda-unica';
import { DomandaUnicaFilter } from './models/domanda-unica-filter';
import { Cached } from "../../shared/decorators/cached";
import { DomandaIntegrativa } from './models/domanda-integrativa';
import { Istruttorie } from './models/dettaglio-istruttoria';
import { Importi } from './models/importi';
import { EsitiIstruttoria } from './models/esiti-istruttoria';
import { Istruttoria } from './models/istruttoria';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class DomandeUnicheService {

  public mieiFasciliUrl = FASCICOLO_SERVER + 'consultazione/mieifascicoli';
  public fascicoloUrl = FASCICOLO_SERVER + 'consultazione/fascicolo/';

  public getUrlGetFascicoloImprese(codiceFiscale: string) {
    return `${ANAGRAFICA_SERVER}legacy/persona/${codiceFiscale}/carica`;
  }

  private getDomandaUnicaUrl = AGS_SERVER + 'domandeUniche';
  private getAnomalieDomandaUnicaUrl = ISTRUTTORIA_SERVER + 'istruttorie' // TODO: pensare ad un path rest
  public UrlGetDomandaIntegrativaByCuaa = ISTRUTTORIA_SERVER + 'domande/az/domandaintegrativa?params=';
  public urlDomandaunica = ISTRUTTORIA_SERVER + 'domandaunica/';
  public urlGetEsitiIstruttoria = ISTRUTTORIA_SERVER + 'istruttorie/du/disaccoppiato/${idIstruttoria}/datiDomanda';
  public urlGetIstruttoria = ISTRUTTORIA_SERVER + 'istruttorie/du/${idIstruttoria}';


  constructor(protected http: HttpClient) { }

  @Cached()
  public dettaglioDomandaUnica(numeroDomanda: number, expands: string): Observable<DomandaUnica> {
    const params = {
      'expand': expands
    }
    let dettaglioDomandaUnicaUrl = `${AGS_SERVER}`+ `domandeDU/${numeroDomanda}`;
    let headers = new HttpHeaders({ "Content-Type": "application/json" });
    return this.http.get<DomandaUnica>(dettaglioDomandaUnicaUrl, { headers, params });
  }

  public getDomandaUnica(domandaUnicaFilter: DomandaUnicaFilter): Observable<Array<DomandaUnica>> {
    let headers = new HttpHeaders({ "Content-Type": "application/json" });

    const params = {
      'stati': String(domandaUnicaFilter.stati),
      'cuaa': String(domandaUnicaFilter.cuaa),
      'expand': String(domandaUnicaFilter.expand)
    };

    if (domandaUnicaFilter.campagna) {
      params['campagna'] = domandaUnicaFilter.campagna.toString();
    }

    return this.http.get<Array<DomandaUnica>>(this.getDomandaUnicaUrl, { headers, params });
  }

  public getAnomalieDomandaUnica(): Observable<Array<any>> {
    let headers = new HttpHeaders({ "Content-Type": "application/json" });
    return this.http.get<Array<DomandaUnica>>(this.getAnomalieDomandaUnicaUrl, { headers });
  }

  public getDomandaIntegrativaByCuaa(jsonInput: any): Observable<DomandaIntegrativa> {
    return this.http.get<DomandaIntegrativa>(this.UrlGetDomandaIntegrativaByCuaa + encodeURIComponent(JSON.stringify(jsonInput)));
  }

  @Cached()
  public getIstruttorie(numeroDomanda): Observable<Istruttorie[]> {
    return this.http.get<Istruttorie[]>(this.urlDomandaunica + `${numeroDomanda}/istruttorieByNumeroDomanda`);
  }

  public getImporti(numeroDomanda): Observable<Importi> {
    return this.http.get<Importi>(this.urlDomandaunica + `${numeroDomanda}/sintesipagamentiByNumeroDomanda`);
  }

  @Cached()
  public getListaCuaa(): Observable<IFascicolo[]> {
    return this.http.get<IFascicolo[]>(this.mieiFasciliUrl);
  }
  public getFascicoloCuaa(cuaa: string): Observable<IFascicolo> {
    return this.http.get<IFascicolo>(this.fascicoloUrl + cuaa);
  }

  @Cached()
  public getListaCariche(codiceFiscale: string): Observable<CaricaDto[]> {
    return this.http.get<any[]>(this.getUrlGetFascicoloImprese(codiceFiscale)).pipe(
      map(resList => {
        let list: CaricaDto[] = [];
        if (resList) {
          for (let res of resList) {
            let car: CaricaDto = new CaricaDto();
            car.carica = res.carica;
            car.codiceFiscale = res.codiceFiscale;
            car.cuaa = res.cuaa;
            car.denominazione = res.denominazione;
            list.push(car);
          }
        }
        return list;
      })
    );
  }

  public getEsitiIstruttoria(idIstruttoria: number): Observable<Array<EsitiIstruttoria>> {
    return this.http.get<Array<EsitiIstruttoria>>(
      this.urlGetEsitiIstruttoria
        .replace('${idIstruttoria}', String(idIstruttoria))
    );
  }

  public getIstruttoria(idIstruttoria: number): Observable<Istruttoria> {
    return this.http.get<Istruttoria>(
      this.urlGetIstruttoria
        .replace('${idIstruttoria}', String(idIstruttoria))
    );
  }

}
