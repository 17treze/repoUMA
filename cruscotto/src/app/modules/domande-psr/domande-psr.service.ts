import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { FASCICOLO_SERVER, ISTRUTTORIA_PSR_SERVER, ISTRUTTORIA_SOC } from '../../../app/app.constants';
import { DomandaPsr } from './models/domanda-psr';
import { ImpegnoRichiestoPsr } from './models/impegno-richiesto-psr';
import { ImpegnoZooPascoloPsr } from './models/impegno-zoo-pascolo-psr';
import { StatoDomandaPsr } from './models/stato-domanda-psr';
import { ImportoLiquidatoDomandaPsr } from './models/importo-liquidato-domanda-psr';
import { ImportiDomandaPsr } from './models/importi-domanda-psr';
import { map } from 'rxjs/operators';
import {
  DettaglioPagamentoM10O11,
  DettaglioPagamentoM10O12,
  DettaglioPagamentoM10O13,
  DettaglioPagamentoM10O14,
  DettaglioPagamentoM1311
} from './models/dettaglio-pagamento-psr';

@Injectable({
  providedIn: 'root'
})
export class DomandePsrService {

  public getDomandePsrUrl = ISTRUTTORIA_PSR_SERVER + '/domande-psr/${cuaa}/consultazione';

  public mieiFasciliUrl = FASCICOLO_SERVER + 'consultazione/mieifascicoli';


  constructor(protected http: HttpClient) {
  }

  public getDomandePsr(cuaa: string): Observable<Array<DomandaPsr>> {
    return this.http.get<Array<DomandaPsr>>(this.getDomandePsrUrl.replace('${cuaa}', String(cuaa)));
  }

  public getImpegniRichiestiPSRSuperficie(idDomanda: number): Observable<Array<ImpegnoRichiestoPsr>> {
    return this.http.get<Array<ImpegnoRichiestoPsr>>((ISTRUTTORIA_PSR_SERVER + '/domande-psr/${idDomanda}/impegni-richiesti')
      .replace('${idDomanda}', String(idDomanda)));
  }

  public getDomandaPsrByNumeroDomanda(idDomanda: number): Observable<DomandaPsr> {
    return this.http.get<DomandaPsr>((ISTRUTTORIA_PSR_SERVER + '/domande-psr/${idDomanda}/psr-superficie')
      .replace('${idDomanda}', String(idDomanda))).pipe(map((domandaPsr) => {
        domandaPsr.dataPresentazione = new Date(domandaPsr.dataPresentazione);
        return domandaPsr;
    }));
  }

  public getImpegniZooPascoliByIdDomanda(idDomanda: number): Observable<Array<ImpegnoZooPascoloPsr>> {
    return this.http.get<Array<ImpegnoZooPascoloPsr>>((ISTRUTTORIA_PSR_SERVER + '/domande-psr/${idDomanda}/zoo-pascoli')
      .replace('${idDomanda}', String(idDomanda)));
  }

  public getStatoDomandaPsrByIdDomanda(idDomanda: number): Observable<Array<StatoDomandaPsr>> {
    return this.http.get<Array<StatoDomandaPsr>>(`${ISTRUTTORIA_PSR_SERVER}/domande-psr/${idDomanda}/stato-domanda`);
  }

  public getPSRSuperficieLiquidazione(cuaa: string, idProgetto: string, anno: number): Observable<ImportoLiquidatoDomandaPsr[]> {
    return this.http.get<ImportoLiquidatoDomandaPsr[]>(ISTRUTTORIA_SOC + `/liquidazione?cuaa=${cuaa}&numeroDomanda=${idProgetto}&anno=${anno}&tipoDomanda=DOMANDA_PSR_SUPERFICIE`);
  }

  public getImportiDomandaPsr(cuaa: string, anno: number): Observable<ImportiDomandaPsr> {
    return this.http.get<ImportiDomandaPsr>(`${ISTRUTTORIA_PSR_SERVER}/domande-psr/${cuaa}/importi-domanda/${anno}`);
  }

  public getDettaglioPagamentoPsr(idDomanda: number, anno: number, codiceOperazione: string, tipoPagamento: string): Observable<HttpResponse<DettaglioPagamentoM10O11 | DettaglioPagamentoM10O12 | DettaglioPagamentoM10O13 | DettaglioPagamentoM10O14 | DettaglioPagamentoM1311>> {
    return this.http.get<DettaglioPagamentoM10O11 | DettaglioPagamentoM10O12 | DettaglioPagamentoM10O13|DettaglioPagamentoM10O14>(`${ISTRUTTORIA_PSR_SERVER}/domande-psr/${idDomanda}/dettaglio-pagamento/${anno}/operazione/${codiceOperazione}/tipo-pagamento/${tipoPagamento}`, {observe: 'response'});
  }

  public getImportoCalcolato(idDomanda: number, codMisura: string, tipoPagamento: string): Observable<number> {
    return this.http.get<number>(`${ISTRUTTORIA_PSR_SERVER}/domande-psr/${idDomanda}/importo-calcolato/cod-misura/${codMisura}/tipo-pagamento/${tipoPagamento}`);
  }

  public getTotaleImportoCalcolatoByIdDomanda(idDomanda: number): Observable<number> {
    return this.http.get<number>(`${ISTRUTTORIA_PSR_SERVER}/domande-psr/${idDomanda}/totale-importo-calcolato`);
  }
}
