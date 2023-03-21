import { FascicoliValidatiFilterDto } from './../../modules/fascicolo-dettaglio/fascicoli-validati/fascicoli-validati-filter/fascicoli-validati-filter.component';
import { DichiarazioneAssociativaDto, OrganizzazioneDto } from './../../modules/dichiarazioni-associative/models/organizzazione';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map, tap } from 'rxjs/operators';
import { environment } from 'src/environments/environment';
import { Cached } from '../decorators/cached';
import { DatiAperturaFascicoloDto, ValidazioneFascicoloDto } from 'src/app/modules/fascicolo-dettaglio/models/anagrafica-fascicolo';
import { PersonaFisicaConCaricaDto, PersonaFisicaDto, PersonaGiuridicaConCaricaDto, PersonaGiuridicaDto } from '../models/persona';
import { FascicoloCreationResultDto, FascicoloDaCuaa } from 'src/app/modules/fascicolo-dettaglio/models/FascicoloCuaa';
import { UnitaTecnicoEconomicheDto } from '../models/UnitaTecnicoEconomicheDto';
import { ModalitaPagamentoDto } from '../models/ModalitaPagamento';
import { formatDate } from '@angular/common';
import { Paginazione } from '../models/paginazione';
import { PaginatorA4G } from '../interfaces/paginator.model';
import { EsitoControlloDto } from 'src/app/modules/fascicolo-dettaglio/models/EsitoControlloDto';
import { DocumentoIdentitaDto, Firmatario } from 'src/app/modules/fascicolo-dettaglio/models/FirmatarioDto';

@Injectable({
  providedIn: 'root'
})
export class AnagraficaFascicoloService {
  constructor(
    private http: HttpClient
  ) { }

  private urlAnagrafica = `${environment.backendUrl}/anagrafica/api/v1`;
  private urlAnagraficaFascicolo = `${this.urlAnagrafica}/fascicolo`;
  private urlAnagraficaFascicoloDetenzioneAutonoma = `${this.urlAnagrafica}/fascicolo-detenzione-autonoma`;

  private UrlGetReportSchedaValidazioneFirmata(cuaa: string) {
    return `${this.urlAnagraficaFascicolo}/${cuaa}/report-scheda-validazione-firmata`;
  }

  private getUrlControlloCompletezza(cuaa: string) {
    return `${this.urlAnagraficaFascicoloDetenzioneAutonoma}/${cuaa}/controllo-completezza-detenzione-autonoma`;
  }

  private getUrlAggiorna(codiceFiscaleAzienda: string) {
    return `${this.urlAnagraficaFascicolo}/${codiceFiscaleAzienda}/aggiorna`;
  }

  private getUrlGetFascicoloDaCuaa(cuaa: string) {
    return `${this.urlAnagraficaFascicolo}/${cuaa}`;
  }

  private putUrlSetRappresentanteLegaleRespingiValidazione(cuaa: string) {
    return `${this.urlAnagraficaFascicolo}/${cuaa}/rappresentante-legale/respingi-validazione`;
  }

  private getUrlGetPersonaGiuridica(cuaa: string) {
    return `${this.urlAnagrafica}/personagiuridica/${cuaa}`;
  }

  private getUrlGetPersonaFisica(cuaa: string) {
    return `${this.urlAnagrafica}/personafisica/${cuaa}`;
  }

  private getUrlFirmatario(cuaa: string) {
    return `${this.getUrlGetPersonaGiuridica(cuaa)}/carica/firmatario`;
  }

  private getUrlSalvaFirmatario(cuaa: string) {
    return `${this.urlAnagrafica}/${cuaa}/carica/firmatario`;
  }

  private getUrlUnitaTecnicoEconomiche(cuaa: string) {
    return `${this.urlAnagraficaFascicolo}/${cuaa}/unita-tecnico-economiche`;
  }

  private UrlGetReportSchedaValidazioneBozzaDetenzioneAutonoma(cuaa: string) {
    return `${this.urlAnagraficaFascicoloDetenzioneAutonoma}/${cuaa}/report-scheda-validazione-bozza-detenzione-autonoma`;
  }

  private UrlPutReportSchedaValidazione(cuaa: string) {
    return `${this.urlAnagraficaFascicolo}/${cuaa}/report-scheda-validazione-firmata-firmatario`;
  }

  private UrlPutReportSchedaValidazioneInProprio(cuaa: string) {
    return `${this.urlAnagraficaFascicoloDetenzioneAutonoma}/${cuaa}/report-scheda-validazione-detenzione-autonoma`;
  }

  private putUrlModalitaPagamento(cuaa: string) {
    return `${this.urlAnagraficaFascicoloDetenzioneAutonoma}/${cuaa}/modo-pagamento`;
  }

  private cancellaUrlModalitaPagamento(cuaa: string, id: number) {
    return `${this.urlAnagraficaFascicoloDetenzioneAutonoma}/${cuaa}/modo-pagamento/${id}`;
  }

  private urlGetAllOrganizzazioni() {
    return `${this.urlAnagraficaFascicolo}/organizzazioni`;
  }

  private urlPutRlAggiungiDichiarazioneAssociativa(cuaa: string) {
    return `${this.urlAnagraficaFascicolo}/${cuaa}/rappresentante-legale/aggiungi-autodichiarazione-associativa`;
  }

  private urlGetDichiarazioniAssociative(cuaa: string) {
    return `${this.urlAnagraficaFascicolo}/${cuaa}/rappresentante-legale/autodichiarazioni-associative`;
  }

  private urlPutRlChiudiDichiarazioneAssociativa(cuaa: string) {
    return `${this.urlAnagraficaFascicolo}/${cuaa}/rappresentante-legale/chiudi-autodichiarazione-associativa`;
  }

  private urlPostApriFascicoloDetenzioneAutonoma(cuaa: string) {
    return `${this.urlAnagraficaFascicoloDetenzioneAutonoma}/${cuaa}/apri`;
  }

  private getUrlGetModalitaPagamento(cuaa: string) {
    return `${this.urlAnagraficaFascicoloDetenzioneAutonoma}/${cuaa}/modo-pagamento`;
  }

  private getUrlgetPersoneFisicheConCarica(cuaa: string) {
    return `${this.urlAnagrafica}/${cuaa}/carica/persona-fisica/`;
  }

  private getUrlgetPersoneGiuridicheConCarica(cuaa: string) {
    return `${this.urlAnagrafica}/${cuaa}/carica/persona-giuridica/`;
  }

  private getUrlGetPossibiliRappresentantiLegali(cuaa: string) {
    return `${this.urlAnagrafica}/${cuaa}/carica/possibili-rappresentanti-legali`;
  }

  private getPutUrlSetStatusFascicoloInAggiornamento(cuaa: string) {
    return `${this.urlAnagraficaFascicolo}/${cuaa}/stato/in-aggiornamento`;
  }

  private getPutUrlStatoControllatoFascicolo(cuaa: string) {
    return `${this.urlAnagraficaFascicolo}/${cuaa}/stato/alla-firma-azienda`;
  }

  private getUrlFascicoliValidati(cuaa: string) {
    return `${this.urlAnagraficaFascicolo}/${cuaa}/validati`;
  }

  public getUrlDocumentoIdentitaFirmatario(cuaa: string) {
    return `${this.urlAnagrafica}/${cuaa}/carica/firmatario/documento-identita`;
  }

  public getFascicoloDaCuaa(cuaa: string, idValidazione: number): Observable<FascicoloDaCuaa> {
    let paramsHttp = new HttpParams();
    if (idValidazione) {
      paramsHttp = paramsHttp.set('idValidazione', String(idValidazione));
    }
    return this.http.get<FascicoloDaCuaa>(this.getUrlGetFascicoloDaCuaa(cuaa), { params: paramsHttp, responseType: 'json' })
      .pipe(
        map(res => FascicoloDaCuaa.toDto(res))
      );
  }

  public putSetRappresentanteLegaleRespingiValidazione(cuaa: string): Observable<any> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.put<any>(this.putUrlSetRappresentanteLegaleRespingiValidazione(cuaa), headers);
  }

  @Cached()
  public getFirmatario(cuaa: string): Observable<PersonaFisicaConCaricaDto> {
    return this.http.get<PersonaFisicaConCaricaDto>(
      this.getUrlFirmatario(cuaa));
  }

  public getReportSchedaValidazioneBozzaDetenzioneAutonoma(cuaa: string): Observable<Blob> {
    return this.http.get(this.UrlGetReportSchedaValidazioneBozzaDetenzioneAutonoma(cuaa), { responseType: 'blob' });
  }

  public getReportSchedaValidazioneDetenzioneAutonoma(cuaa: string): Observable<Blob> {
    return this.http.get(this.UrlPutReportSchedaValidazioneInProprio(cuaa), { responseType: 'blob' });
  }

  public putReportSchedaValidazione(file: File, cuaa: string): Observable<void> {
    return this.http.put<void>(this.UrlPutReportSchedaValidazione(cuaa), file);
  }

  public putReportSchedaValidazioneInProprio(file: File, cuaa: string): Observable<void> {
    return this.http.put<any>(this.UrlPutReportSchedaValidazioneInProprio(cuaa), file);
  }

  public getAllOrganizzazioni(): Observable<Array<OrganizzazioneDto>> {
    return this.http.get<Array<OrganizzazioneDto>>(this.urlGetAllOrganizzazioni())
      .pipe(map(res => OrganizzazioneDto.toDtos(res)));
  }

  public putRlAggiungiDichiarazioneAssociativa(dichiarazione: DichiarazioneAssociativaDto, cuaa: string): Observable<void> {
    return this.http.put<any>(this.urlPutRlAggiungiDichiarazioneAssociativa(cuaa), dichiarazione);
  }

  public getDichiarazioniAssociative(cuaa: string): Observable<Array<DichiarazioneAssociativaDto>> {
    return this.http.get<Array<DichiarazioneAssociativaDto>>(this.urlGetDichiarazioniAssociative(cuaa))
      .pipe(map(res => DichiarazioneAssociativaDto.toDto(res)));
  }

  public putRlChiudiDichiarazioneAssociativa(dichiarazione: DichiarazioneAssociativaDto, cuaa: string): Observable<void> {
    return this.http.put<any>(this.urlPutRlChiudiDichiarazioneAssociativa(cuaa), dichiarazione);
  }

  public getUrlVerificaAperturaFascicoloDetenzioneAutonoma(codiceFiscale: string) {
    return `${this.urlAnagraficaFascicoloDetenzioneAutonoma}/${codiceFiscale}/verifica/dati-apertura`;
  }

  public postUrlAperturaFascicoloDetenzioneAutonoma(codiceFiscale: string) {
    return this.http.post<any>(this.urlPostApriFascicoloDetenzioneAutonoma(codiceFiscale), { responseType: 'text' });
  }

  public getVerificaAperturaFascicoloDetenzioneAutonoma(codiceFiscale: string): Observable<DatiAperturaFascicoloDto> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.get<any>(this.getUrlVerificaAperturaFascicoloDetenzioneAutonoma(codiceFiscale), { headers: headers }).pipe(
      tap((retVal: any) => {
        return DatiAperturaFascicoloDto.toDto(retVal);
      })
    );
  }

  @Cached()
  public getPersonaFisica(cuaa: string, idValidazione: number): Observable<PersonaFisicaDto> {
    let paramsHttp = new HttpParams();
    if (idValidazione) {
      paramsHttp = paramsHttp.set('idValidazione', String(idValidazione));
    }
    return this.http.get<PersonaFisicaDto>(this.getUrlGetPersonaFisica(cuaa), { params: paramsHttp, responseType: 'json' });
  }

  @Cached()
  public getPersonaGiuridica(cuaa: string, idValidazione: number): Observable<PersonaGiuridicaDto> {
    let paramsHttp = new HttpParams();
    if (idValidazione) {
      paramsHttp = paramsHttp.set('idValidazione', String(idValidazione));
    }
    return this.http.get<PersonaGiuridicaDto>(this.getUrlGetPersonaGiuridica(cuaa), { params: paramsHttp, responseType: 'json' });
  }


  @Cached()
  public getPersoneFisicheConCarica(cuaa: string, idValidazione: number): Observable<PersonaFisicaConCaricaDto[]> {
    let paramsHttp = new HttpParams();
    if (idValidazione) {
      paramsHttp = paramsHttp.set('idValidazione', String(idValidazione));
    }
    return this.http.get<PersonaFisicaConCaricaDto[]>(
      this.getUrlgetPersoneFisicheConCarica(cuaa),
      { params: paramsHttp, responseType: 'json' });
  }

  @Cached()
  public getPersoneGiuridicheConCarica(cuaa: string, idValidazione: number): Observable<PersonaGiuridicaConCaricaDto[]> {
    let paramsHttp = new HttpParams();
    if (idValidazione) {
      paramsHttp = paramsHttp.set('idValidazione', String(idValidazione));
    }
    return this.http.get<PersonaGiuridicaConCaricaDto[]>(
      this.getUrlgetPersoneGiuridicheConCarica(cuaa),
      { params: paramsHttp, responseType: 'json' });
  }

  @Cached()
  public getUnitaTecnicoEconomiche(cuaa: string, idValidazione: number): Observable<UnitaTecnicoEconomicheDto[]> {
    let paramsHttp = new HttpParams();
    if (idValidazione) {
      paramsHttp = paramsHttp.set('idValidazione', String(idValidazione));
    }
    return this.http.get<UnitaTecnicoEconomicheDto[]>
      (this.getUrlUnitaTecnicoEconomiche(cuaa), { params: paramsHttp })
      .pipe(
        tap((retVal: any) => {
          return UnitaTecnicoEconomicheDto.toDto(retVal);
        })
      );
  }

  public getModalitaPagamento(cuaa: string, idValidazione: number): Observable<ModalitaPagamentoDto[]> {
    let paramsHttp = new HttpParams();
    paramsHttp = paramsHttp.set('idValidazione', idValidazione.toString());
    return this.http.get<ModalitaPagamentoDto[]>(this.getUrlGetModalitaPagamento(cuaa),
      { params: paramsHttp, responseType: 'json' });
  }

  public putModalitaPagamento(cuaa: string, jsonInput: any): Observable<ModalitaPagamentoDto> {
    return this.http.put<ModalitaPagamentoDto>(this.putUrlModalitaPagamento(cuaa), jsonInput);
  }

  public cancellaModalitaPagamento(cuaa: string, id: number): any {
    return this.http.delete<any>(this.cancellaUrlModalitaPagamento(cuaa, id), { responseType: 'json' });
  }

  public aggiorna(cuaa: string): Observable<FascicoloCreationResultDto> {
    return this.http.get<FascicoloCreationResultDto>(this.getUrlAggiorna(cuaa)).pipe(
      map(res => FascicoloCreationResultDto.toDto(res))
    );
  }

  @Cached()
  public getPossibiliRappresentantiLegali(cuaa: string): Observable<PersonaFisicaConCaricaDto[]> {
    return this.http.get<PersonaFisicaConCaricaDto[]>(
      this.getUrlGetPossibiliRappresentantiLegali(cuaa));
  }

  public postSalvaFirmatario(firmatario: Firmatario, cuaa: string): Observable<void> {
    const formInput: FormData = new FormData();
    formInput.append('codiceFiscale', firmatario.codiceFiscale);
    formInput.append('tipoDocumento', firmatario.tipoDocumento);
    formInput.append('numeroDocumento', firmatario.numeroDocumento);
    formInput.append('dataRilascio', formatDate(firmatario.dataRilascio, 'yyyy-MM-dd', 'it-IT'));
    formInput.append('dataScadenza', formatDate(firmatario.dataScadenza, 'yyyy-MM-dd', 'it-IT'));
    formInput.append('documento', firmatario.documento);

    return this.http.post<void>(this.getUrlSalvaFirmatario(cuaa), formInput);
  }

  public getControlloCompletezza(cuaa: string): Observable<Map<string, EsitoControlloDto>> {
    return this.http.get<Map<string, EsitoControlloDto>>(this.getUrlControlloCompletezza(cuaa));
  }

  public putStatoAllaFirmaAziendaFascicolo(cuaa: string): Observable<void> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.put<void>(this.getPutUrlStatoControllatoFascicolo(cuaa), headers);
  }

  public putSetStatusFascicoloInAggiornamento(cuaa: string): Observable<void> {
    return this.http.put<void>(this.getPutUrlSetStatusFascicoloInAggiornamento(cuaa), {});
  }

  public getReportSchedaValidazioneFirmata(cuaa: string): Observable<Blob> {
    return this.http.get(this.UrlGetReportSchedaValidazioneFirmata(cuaa), { responseType: 'blob' });
  }

  public getFascicoliValidati(cuaa, filtro: FascicoliValidatiFilterDto, paginazione: Paginazione): Observable<PaginatorA4G<ValidazioneFascicoloDto[]>> {
    const data: any = { ...filtro, ...paginazione };
    return this.http.get<PaginatorA4G<ValidazioneFascicoloDto[]>>(this.getUrlFascicoliValidati(cuaa), { params: data });
  }

  public getDocumentoIdentitaFirmatario(cuaa: string, idValidazione: number): Observable<DocumentoIdentitaDto> {
    let paramsHttp = new HttpParams();
    if (idValidazione) {
      paramsHttp = paramsHttp.set('idValidazione', String(idValidazione));
    }
    return this.http.get<DocumentoIdentitaDto>
      (this.getUrlDocumentoIdentitaFirmatario(cuaa), { params: paramsHttp })
      .pipe(
        tap((retVal: any) => {
          return DocumentoIdentitaDto.toDto(retVal);
        })
      );
  }

}
