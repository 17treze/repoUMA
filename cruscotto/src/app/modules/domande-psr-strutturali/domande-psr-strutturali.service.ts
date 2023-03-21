import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ISTRUTTORIA_SOC, ISTRUTTORIA_PSRSTR_SERVER } from 'src/app/app.constants';
import { DomandaPsrStrutturale } from './models/domanda-psr-strutturale';
import { DettaglioPSRStrutturale } from './models/dettaglio-domanda-psr-strutturale';
import { InfoGeneraliPSRStrutturale } from './models/info-generali-domanda-psr-strutturale';
import { TotaliImportiSRStrutturale } from './models/importi-totali';
import { ImportiLiquidazione } from './models/importi-liquidazione';
import { DettaglioIstruttoriaSrt, PsrFattura, PsrInterventoDettaglioDto } from './models/dettaglio-istruttoria-srt';
import { Variante } from './models/variante';
import { Investimento } from './models/investimento';
import {TotaliVariante} from './models/totali-variante';

@Injectable({
  providedIn: 'root'
})
export class DomandePsrStrutturaliService {

  public getDomandePsrStrutturaliUrl = ISTRUTTORIA_PSRSTR_SERVER + '/domande/domanda/${cuaa}';

  constructor(protected http: HttpClient) { }

  public getDomandePsrStrutturali(cuaa: string): Observable<Array<DomandaPsrStrutturale>> {
    return this.http.get<Array<DomandaPsrStrutturale>>(this.getDomandePsrStrutturaliUrl.replace('${cuaa}', String(cuaa)));
  }

  public getPSRStrutturaliDettaglio(idProgetto): Observable<DettaglioPSRStrutturale[]> {
    return this.http.get<DettaglioPSRStrutturale[]>(ISTRUTTORIA_PSRSTR_SERVER + `/domande/progetto/${idProgetto}/`);
  }

  public getAziendaDaDomandaPSRStrutturale(idProgetto: number): Observable<InfoGeneraliPSRStrutturale[]> {
    return this.http.get<InfoGeneraliPSRStrutturale[]>(ISTRUTTORIA_PSRSTR_SERVER + `/domande/progetto-impresa/${idProgetto}/`);
  }

  public getPSRStrutturaliTotali(idProgetto): Observable<TotaliImportiSRStrutturale[]> {
    return this.http.get<TotaliImportiSRStrutturale[]>(ISTRUTTORIA_PSRSTR_SERVER + `/domande/progetto-costi-contributi/${idProgetto}/`);
  }

  public getPSRStrutturaliLiquidazione(cuaa: string, idProgetto: string): Observable<ImportiLiquidazione[]> {
    return this.http.get<ImportiLiquidazione[]>(ISTRUTTORIA_SOC + `/liquidazione?cuaa=${cuaa}&numeroDomanda=${idProgetto}&tipoDomanda=DOMANDA_PSR_STRUTTURALE`);
  }

  public getPSRStrutturaliDettaglioIstruttoria(idProgetto): Observable<DettaglioIstruttoriaSrt> {
    return this.http.get<DettaglioIstruttoriaSrt>(ISTRUTTORIA_PSRSTR_SERVER + `/domande/progetto-dettaglio-domande/${idProgetto}/`);
  }

  public getDettaglioIstruttoriaByProgettoAndTipo(idProgetto: number, tipologia: 'ACCONTO' | 'SALDO' | 'FINANZIABILITA', idDomandaPagamento?: number): Observable<PsrInterventoDettaglioDto[]> {
    let params = new HttpParams();
    if (idDomandaPagamento != null) {
      params = params.set('idDomandaPagamento', String(idDomandaPagamento));
    }
    return this.http.get<PsrInterventoDettaglioDto[]>(ISTRUTTORIA_PSRSTR_SERVER + `/domande/progetto-dettaglio/${idProgetto}/${tipologia}`, { params });
  }

  public getFattureByProgettoAndIntervento(idProgetto: number, tipologia: 'ACCONTO' | 'SALDO', idIntervento: number, idDomandaPagamento?: number): Observable<PsrFattura[]> {
    let params = new HttpParams();
    if (idDomandaPagamento) {
      params = params.set('idDomandaPagamento', String(idDomandaPagamento));
    }
    return this.http.get<PsrFattura[]>(
      ISTRUTTORIA_PSRSTR_SERVER + `/domande/progetto-dettaglio/${idProgetto}/${tipologia}/intervento/${idIntervento}/fatture`, { params });
  }

  public getVariantiByIdProgetto(idProgetto): Observable<Variante[]> {
    return this.http.get<Variante[]>(ISTRUTTORIA_PSRSTR_SERVER + `/domande/progetto-dettaglio-domande-varianti/${idProgetto}/`);
  }

  public getTotaliVariante(idProgetto, idVariante): Observable<TotaliVariante> {
    return this.http.get<TotaliVariante>(ISTRUTTORIA_PSRSTR_SERVER + `/domande/progetto-dettaglio-domande-varianti/${idProgetto}/${idVariante}/totali`);
  }

  public getInvestimentiByIdVariante(idVariante): Observable<Investimento[]> {
    return this.http.get<Investimento[]>(ISTRUTTORIA_PSRSTR_SERVER + `/domande/progetto-dettaglio-domande-varianti/${idVariante}/investimenti`);
  }

}
