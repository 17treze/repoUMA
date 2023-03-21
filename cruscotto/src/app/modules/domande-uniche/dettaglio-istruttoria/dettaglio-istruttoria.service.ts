import { Injectable } from '@angular/core';
import { Cached } from 'src/app/shared/decorators/cached';
import { Observable } from 'rxjs';
import { DatiDomandaACZ, DatiInterventi } from './models/dettaglioDomandaAccoppiati';
import { ISTRUTTORIA_SERVER } from 'src/app/app.constants';
import { HttpClient } from '@angular/common/http';
import { Istruttoria } from '../models/istruttoria';

@Injectable({
  providedIn: 'root'
})
export class DettaglioIstruttoriaService {

  public UrlIstruttorieAcz = ISTRUTTORIA_SERVER + 'istruttorie/du/zootecnia';
  public UrlIstruttorieACS = ISTRUTTORIA_SERVER + 'istruttorie/du/superficie';
  public urlGetIstruttoria = ISTRUTTORIA_SERVER + 'istruttorie/du/${idIstruttoria}';

  constructor(protected http: HttpClient) { }

  public getIstruttoria(idIstruttoria: number): Observable<Istruttoria> {
    return this.http.get<Istruttoria>(
      this.urlGetIstruttoria
        .replace('${idIstruttoria}', String(idIstruttoria))
    );
  }

  @Cached()
  public getEsitiCalcoloIstruttoriaAcz(idIstruttoria: string): Observable<DatiDomandaACZ> {
    return this.http.get<DatiDomandaACZ>(this.UrlIstruttorieAcz + `/${idIstruttoria}/esiticalcoli`);
  }

  @Cached()
  public getDisciplinaIstruttoriaAcz(idIstruttoria: string): Observable<DatiInterventi> {
    return this.http.get<DatiInterventi>(this.UrlIstruttorieAcz + `/${idIstruttoria}/disciplina`);
  }

  @Cached()
  public getEsitiCalcoloIstruttoriaACS(idIstruttoria: string): Observable<DatiDomandaACZ> {
    return this.http.get<DatiDomandaACZ>(this.UrlIstruttorieACS + `/${idIstruttoria}/esiticalcoli`);
  }

  @Cached()
  public getDisciplinaIstruttoriaACS(idIstruttoria: string): Observable<DatiInterventi> {
    return this.http.get<DatiInterventi>(this.UrlIstruttorieACS + `/${idIstruttoria}/disciplina`);
  }
}