import { TipoDetenzioneAgs } from './../../a4g-common/classi/FascicoloAgsDto';
import { DocumentoIdentitaDto, Firmatario } from './dto/FirmatarioDto';
import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { formatDate } from '@angular/common';
import { Configuration } from 'src/app/app.constants';
import { Observable } from 'rxjs';
import { ImpresaDto } from './dto/ImpresaDto';
import { catchError, map, tap } from 'rxjs/operators';
import { DatiCAA } from './dto/DatiSportelloCAA';
import { MandatoDto, AllegatoMandato } from './dto/MandatoDto';
import { Persona } from 'src/app/a4g-common/classi/Persona';
import { DatiAperturaFascicoloDto, FascicoloCreationResultDto } from './dto/DatiAperturaFascicoloDto';
import { PersonaDto, PersonaFisicaConCaricaDto, PersonaFisicaDto, PersonaGiuridicaConCaricaDto, PersonaGiuridicaDto } from './dto/PersonaDto';
import { FiltroRicercaFascicoli } from '../ricerca-fascicoli-new/ricerca-fascicoli-new.model';
import { Paginazione } from 'src/app/a4g-common/utility/paginazione';
import { PaginatorA4G } from 'src/app/a4g-common/interfaces/paginator.model';
import { FascicoloDTO } from '../shared/fascicolo.model';
import { FascicoloDaCuaa } from 'src/app/a4g-common/classi/FascicoloCuaa';
import { PersonaAgsDto } from 'src/app/uma/core-uma/models/dto/PersonaAgsDto';
import { ModalitaPagamentoDto } from './dto/ModalitaPagamento';
import { EredeDto } from './dto/EredeDto';
import { UnitaTecnicoEconomicheDto } from './dto/UnitaTecnicoEconomicheDto';
import { Cached } from 'src/assets/decorators/cached';
import { IMPORTA_FASCICOLO_STATO } from './crea-fascicolo/importa-fascicolo-v2/importa-fascicolo-v2.component';
import { SospensioneFascicolo } from 'src/app/a4g-common/classi/Fascicolo';

@Injectable({
  providedIn: 'root'
})
export class AnagraficaFascicoloService {
  constructor(
    private http: HttpClient,
    private configuration: Configuration) { }

  // oggetto utilizzato per essere condiviso dai tab di creazione
  private _anagraficaImpresa: ImpresaDto;

  set anagraficaImpresa(value) {
    this._anagraficaImpresa = value;
  }

  get anagraficaImpresa() {
    return this._anagraficaImpresa;
  }

  private urlAnagraficaFascicolo = `${this.configuration.anagrafica_server}/fascicolo`;
  private urlGetCaa = `${this.configuration.anagrafica_server}/caa`;

  // SEZIONE URL FASCICOLO
  public getUrlApertura(codiceFiscaleAzienda: string) {
    return `${this.urlAnagraficaFascicolo}/${codiceFiscaleAzienda}/apri`;
  }

  public getUrlRicostituisci(codiceFiscaleAzienda: string) {
    return `${this.urlAnagraficaFascicolo}/${codiceFiscaleAzienda}/ricostituisci`;
  }

  public getTrasferimento(codiceFiscaleAzienda: string) {
    return `${this.urlAnagraficaFascicolo}/${codiceFiscaleAzienda}/trasferisci`;
  }

  public getUrlAggiorna(codiceFiscaleAzienda: string) {
    return `${this.urlAnagraficaFascicolo}/${codiceFiscaleAzienda}/aggiorna`;
  }

  public getUrlTrasferisciEChiudi(codiceFiscaleAzienda: string) {
    return `${this.urlAnagraficaFascicolo}/${codiceFiscaleAzienda}/trasferimento-chiusura`;
  }

  public getUrlgetTitolariRappresentantiLegali(cuaa: string) {
    return `${this.urlAnagraficaFascicolo}/legacy/${cuaa}/soggetti`;
  }

  public getUrlgetPersonaFisica(cuaa: string) {
    return `${this.configuration.anagrafica_server}/personafisica/${cuaa}`;
  }

  public getUrlgetPersonaGiuridica(cuaa: string) {
    return `${this.configuration.anagrafica_server}/personagiuridica/${cuaa}`;
  }

  public getUrlgetPersoneFisicheConCarica(cuaa: string) {
    return `${this.configuration.anagrafica_server}/${cuaa}/carica/persona-fisica/`;
  }

  public getUrlgetPersoneGiuridicheConCarica(cuaa: string) {
    return `${this.configuration.anagrafica_server}/${cuaa}/carica/persona-giuridica/`;
  }

  public getUrlGetPossibiliRappresentantiLegali(cuaa: string) {
    return `${this.configuration.anagrafica_server}/${cuaa}/carica/possibili-rappresentanti-legali`;
  }

  public getUrlGetFirmatario(cuaa: string) {
    return `${this.configuration.anagrafica_server}/personagiuridica/${cuaa}/carica/firmatario`;
  }

  public getUrlPutSalvaFirmatario(cuaa: string) {
    return `${this.configuration.anagrafica_server}/${cuaa}/carica/firmatario`;
  }

  public getUrlPutErede(cuaa: string) {
    return `${this.configuration.anagrafica_server}/fascicolo/${cuaa}/salva-erede`;
  }

  public getUrlDeleteErede(cuaa: string, id: number) {
    return `${this.configuration.anagrafica_server}/fascicolo/${cuaa}/rimuovi-erede/${id}`;
  }

  public getUrlVerificaAperturaFascicolo(codiceFiscale: string) {
    return `${this.configuration.anagrafica_server}/fascicolo/${codiceFiscale}/verifica/dati-apertura`;
  }

  public getUrlVerificaAperturaTrasferimentoFascicolo(codiceFiscale: string) {
    return `${this.configuration.anagrafica_server}/fascicolo/${codiceFiscale}/verifica/dati-apertura-trasferimento`;
  }

  public getUrlVerificaTrasferimentoFascicolo(codiceFiscale: string) {
    return `${this.configuration.anagrafica_server}/fascicolo/${codiceFiscale}/verifica/dati-trasferimento`;
  }

  public getUrlGetModalitaPagamento(cuaa: string) {
    return `${this.configuration.anagrafica_server}/fascicolo/${cuaa}/modo-pagamento`;
  }

  public putUrlModalitaPagamento(cuaa: string) {
    return `${this.configuration.anagrafica_server}/fascicolo/${cuaa}/modo-pagamento`;
  }

  public putUrlStatoControllatoFascicolo(cuaa: string) {
    return `${this.configuration.anagrafica_server}/fascicolo/${cuaa}/stato/alla-firma-azienda`;
  }

  public putUrlSetStatusFascicoloInAggiornamento(cuaa: string) {
    return `${this.configuration.anagrafica_server}/fascicolo/${cuaa}/stato/in-aggiornamento`;
  }

  public cancellaUrlModalitaPagamento(cuaa: string, id: number) {
    return `${this.configuration.anagrafica_server}/fascicolo/${cuaa}/modo-pagamento/${id}`;
  }

  public getUrlGetFascicoloDaCuaa(cuaa: string) {
    return `${this.urlAnagraficaFascicolo}/${cuaa}`;
  }

  public getUrlCheckAperturaFascicolo(cuaa: string, idValidazione: number) {
    return `${this.urlAnagraficaFascicolo}/${cuaa}/check-apertura-fascicolo`;
  }

  public getUrlUnitaTecnicoEconomiche(cuaa: string) {
    return `${this.configuration.anagrafica_server}/fascicolo/${cuaa}/unita-tecnico-economiche`;
  }

  public UrlGetReportTrasferimentoOp(cuaa: string) {
    return `${this.urlAnagraficaFascicolo}/${cuaa}/report-richiesta-trasferimento-op`;
  }

  public getUrlPutReportSchedaValidazione(cuaa: string) {
    return `${this.configuration.anagrafica_server}/fascicolo/${cuaa}/report-scheda-validazione-firmata`;
  }

  public getUrlGetReportSchedaValidazioneSnapshot(cuaa: string) {
    return `${this.configuration.anagrafica_server}/fascicolo/${cuaa}/report-scheda-validazione-firmata-db`;
  }

  public getUrlGetCheckMigraFascicolo(cuaa: string) {
    return `${this.configuration.anagrafica_server}/fascicolo/${cuaa}/check-migra-fascicolo`;
  }

  public getUrlPutReportSchedaValidazioneFirmaCaaAzienda(cuaa: string) {
    return `${this.configuration.anagrafica_server}/fascicolo/${cuaa}/report-scheda-validazione-firmata-firmatario`;
  }

  public getUrlEsitoRichiestaTrasferimentoOp(cuaa: string) {
    return `${this.configuration.anagrafica_server}/fascicolo/${cuaa}/esito-richiesta-trasferimento-op`;
  }

  public getUrlChiudiIterTrasferimentoAltroOp(cuaa: string) {
    return `${this.configuration.anagrafica_server}/fascicolo/${cuaa}/trasferimento-chiusura`;
  }

  public getUrlDocumentoIdentitaFirmatario(cuaa: string) {
    return `${this.configuration.anagrafica_server}/${cuaa}/carica/firmatario/documento-identita`;
  }

  public getUrlEredi(cuaa: string) {
    return `${this.configuration.anagrafica_server}/fascicolo/${cuaa}/eredi`;
  }

  public putUrlChiudiFascicolo(cuaa: string) {
    return `${this.configuration.anagrafica_server}/fascicolo/${cuaa}/stato/chiuso`;
  }

  public postUrlSospendiFascicolo(cuaa: string) {
    return `${this.configuration.anagrafica_server}/fascicolo/${cuaa}/sospendi`;
  }

  public postUrlRimuoviSospensioneFascicolo(cuaa: string) {
    return `${this.configuration.anagrafica_server}/fascicolo/${cuaa}/rimuovi-sospensione`;
  }

  public getUrlVerificaRiaperturaFascicolo(codiceFiscale: string) {
    return `${this.configuration.anagrafica_server}/fascicolo/${codiceFiscale}/verifica/dati-riapertura`;
  }

  public esitoRichiestaTrasferimentoOp(cuaa: string): Observable<boolean> {
    const url = this.getUrlEsitoRichiestaTrasferimentoOp(cuaa);
    return this.http.get<boolean>(url);
  }

  public chiudiIterTrasferimentoAltroOp(cuaa: string, dataChiusura: Date): Observable<FascicoloCreationResultDto> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    const dataChiusuraStr = formatDate(dataChiusura, 'yyyy-MM-dd', 'it-IT');
    return this.http.post<FascicoloCreationResultDto>(
      this.getUrlChiudiIterTrasferimentoAltroOp(cuaa),
      JSON.stringify(dataChiusuraStr),
      { headers: headers });
  }

  public verificaPermessiAperturaFascicolo(cuaa: string, idValidazione: number): Observable<boolean> {
    const url = this.getUrlCheckAperturaFascicolo(cuaa, idValidazione);
    return this.http.get<boolean>(url);
  }

  public getAnagraficaFascicolo(filtro: FiltroRicercaFascicoli, paginazione: Paginazione): Observable<PaginatorA4G<FascicoloDTO[]>> {
    const data: any = { ...filtro, ...paginazione };
    return this.http.get<PaginatorA4G<FascicoloDTO[]>>(`${this.urlAnagraficaFascicolo}`, { params: data });
  }

  // recupero informazioni anagrafiche dal servizio esterno PARIX - AE
  public getAnagraficaImpresa(cf: string): Observable<ImpresaDto> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.get<ImpresaDto>(this.configuration.UrlGetAnagraficaImpresa_V2.replace('${cuaa}', cf), { headers: headers }).pipe(
      catchError(err => {
        return this.http.get<ImpresaDto>(this.configuration.UrlGetAnagrafeTributaria_V2.replace('${codiceFiscale}', cf), { headers: headers })
      }),
    );
  }

  public getVerificaAperturaFascicolo(codiceFiscale: string): Observable<DatiAperturaFascicoloDto> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.get<any>(this.getUrlVerificaAperturaFascicolo(codiceFiscale), { headers: headers }).pipe(
      tap((retVal: any) => {
        return DatiAperturaFascicoloDto.toDto(retVal);
      })
    );
  }

  public getVerificaAperturaTrasferimentoFascicolo(codiceFiscale: string): Observable<DatiAperturaFascicoloDto> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.get<any>(this.getUrlVerificaAperturaTrasferimentoFascicolo(codiceFiscale), { headers: headers }).pipe(
      tap((retVal: any) => {
        return DatiAperturaFascicoloDto.toDto(retVal);
      })
    );
  }

  public getVerificaTrasferimentoFascicolo(codiceFiscale: string): Observable<DatiAperturaFascicoloDto> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.get<any>(this.getUrlVerificaTrasferimentoFascicolo(codiceFiscale), { headers: headers }).pipe(
      tap((retVal: any) => {
        return DatiAperturaFascicoloDto.toDto(retVal);
      })
    );
  }

  public getAnagrafeTributaria(codiceFiscale: string): Observable<ImpresaDto> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.get<ImpresaDto>(this.configuration.UrlGetAnagrafeTributaria_V2.replace('${codiceFiscale}', codiceFiscale), { headers: headers })
  }

  public getCodiciEnti(): Observable<string[]> {
    return this.http.get<string[]>(this.configuration.UrlGetEnti);
  }

  public getDatiSportelloCAA(): Observable<DatiCAA> {
    return this.http.get<any>(this.urlGetCaa).pipe(
      tap((retVal: any) => {
        return DatiCAA.toDto(retVal);
      })
    );
  }

  public verificaFirmaSingola(file: File, codiceFiscale: string): Observable<any> {
    const formInput: FormData = new FormData();
    formInput.append('documentoFirmato', file);
    return this.http.post<any>(this.configuration.UrlVerificaFirmaSingola
      .replace('${codiceFiscaleTitolareRl}', codiceFiscale), formInput);
  }

  public verificaFirmaMultipla(file: File, codiceFiscale: string): Observable<any> {
    const formInput: FormData = new FormData();
    formInput.append('documentoFirmato', file);
    return this.http.post<any>(this.configuration.UrlVerificaFirmaMultipla +'?codiceFiscaleList=' + codiceFiscale, formInput);
  }


  public apri(datiApertura: MandatoDto): Observable<any> {
    const formInput: FormData = new FormData();
    // const headers = new HttpHeaders().set('Accept', 'text/plain; charset=utf-8');
    // formInput.append('codiceFiscale', datiApertura.codiceFiscale);
    formInput.append('codiceFiscaleRappresentante', datiApertura.codiceFiscaleRappresentante);
    formInput.append('contratto', datiApertura.contratto);
    formInput.append('identificativoSportello', datiApertura.identificativoSportello.toString());

    datiApertura.allegati.forEach((el: AllegatoMandato) => {
      formInput.append('allegati', el.file, el.descrizione);
    });
    return this.http.post(
      this.getUrlApertura(datiApertura.codiceFiscale),
      formInput, { responseType: 'text' });
  }

  public trasferisci(datiApertura: MandatoDto, importaFascicoloStato: IMPORTA_FASCICOLO_STATO): Observable<any> {
    const formInput: FormData = new FormData();
    // const headers = new HttpHeaders().set('Accept', 'text/plain; charset=utf-8');
    // formInput.append('codiceFiscale', datiApertura.codiceFiscale);
    formInput.append('codiceFiscaleRappresentante', datiApertura.codiceFiscaleRappresentante);
    formInput.append('contratto', datiApertura.contratto);
    formInput.append('identificativoSportello', datiApertura.identificativoSportello.toString());
    formInput.append('fascicoloOperationEnum', importaFascicoloStato);

    datiApertura.allegati.forEach((el: AllegatoMandato) => {
      formInput.append('allegati', el.file, el.descrizione);
    });
    return this.http.post(
      this.getTrasferimento(datiApertura.codiceFiscale),
      formInput, { responseType: 'text' });
  }

  public aggiorna(cuaa: string): Observable<FascicoloCreationResultDto> {
    return this.http.get<FascicoloCreationResultDto>(this.getUrlAggiorna(cuaa)).pipe(
      map(res => FascicoloCreationResultDto.toDto(res))
    );
  }

  public trasferisciEChiudi(cuaa: string, dataChiusura: Date): Observable<FascicoloCreationResultDto> {
    return this.http.put<FascicoloCreationResultDto>(this.getUrlTrasferisciEChiudi(cuaa), dataChiusura).pipe(
      map(res => FascicoloCreationResultDto.toDto(res))
    );
  }

  public verificaPresenzaPrivacy(parametriRicerca: any): Observable<Persona[]> {
    const json = JSON.stringify(parametriRicerca);
    return this.http.get<Persona[]>(
      this.configuration.UrlPersonaPrivacy + encodeURIComponent(json)).pipe(
        tap((retVal: any) => {
          return Persona.toDtos(retVal);
        })
      );
  }

  public getPdfInformativaPrivacy(): Observable<any> {
    return this.http.get(this.configuration.UrlGetInfoPrivacy, {
      responseType: 'blob'
    });
  }

  // Ricavo nome e cognome dal nominativo
  public getNomeCognomeFromNominativo(nominativo: string): { nome: string, cognome: string } {
    let nome: string;
    let cognome: string;
    const nominativoArray: string[] = nominativo && nominativo.trim().replace(/\s\s+/g, ' ').split(' ');
    if (nominativoArray.length === 2) {
      cognome = nominativoArray[0];
      nome = nominativoArray[1];
    } else {
      cognome = nominativo;
      nome = nominativo;
    }
    return { nome, cognome };
  }

  @Cached()
  public getFascicolo(cuaa: string, idValidazione: number): Observable<FascicoloDaCuaa> {
    let paramsHttp = new HttpParams();
    if (idValidazione) {
      paramsHttp = paramsHttp.set('idValidazione', String(idValidazione));
    }
    return this.http.get<FascicoloDaCuaa>(this.getUrlGetFascicoloDaCuaa(cuaa), { params: paramsHttp }).pipe(
      map(res => FascicoloDaCuaa.toDto(res))
    );
  }

  public getFascicoloNotCached(cuaa: string, idValidazione: number): Observable<FascicoloDaCuaa> {
    let paramsHttp = new HttpParams();
    if (idValidazione) {
      paramsHttp = paramsHttp.set('idValidazione', String(idValidazione));
    }
    return this.http.get<FascicoloDaCuaa>(this.getUrlGetFascicoloDaCuaa(cuaa), { params: paramsHttp }).pipe(
      map(res => FascicoloDaCuaa.toDto(res))
    );
  }

  @Cached()
  public getTitolariRappresentantiLegali(cuaa: string): Observable<PersonaAgsDto[]> {
    return this.http.get<PersonaAgsDto[]>(this.getUrlgetTitolariRappresentantiLegali(cuaa), { responseType: 'json' });
  }

  @Cached()
  public getPersonaFisica(cuaa: string, idValidazione: number): Observable<PersonaFisicaDto> {
    let paramsHttp = new HttpParams();
    if (idValidazione) {
      paramsHttp = paramsHttp.set('idValidazione', String(idValidazione));
    }
    return this.http.get<PersonaFisicaDto>(
      this.getUrlgetPersonaFisica(cuaa),
      { params: paramsHttp, responseType: 'json' }
    );
  }

  public getSezioniImpresa(cuaa: string): Observable<PersonaDto> {
    return this.http.get<PersonaDto>(this.getUrlgetPersonaGiuridica(cuaa), { responseType: 'json' });
  }

  @Cached()
  public getPersonaFisicaDaAnagrafeTributaria(cf: string): Observable<PersonaFisicaDto> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.get<PersonaFisicaDto>(this.configuration.UrlGetPersonaFisicaAnagrafeTributaria_V2.replace('${codiceFiscale}', cf), { headers: headers })
  }

  @Cached()
  public getPersonaGiuridica(cuaa: string, idValidazione: number): Observable<PersonaGiuridicaDto> {
    let paramsHttp = new HttpParams();
    if (idValidazione) {
      paramsHttp = paramsHttp.set('idValidazione', String(idValidazione));
    }
    return this.http.get<PersonaGiuridicaDto>(
      this.getUrlgetPersonaGiuridica(cuaa),
      { params: paramsHttp, responseType: 'json' }
    );
  }

  @Cached()
  public getPersoneFisicheConCaricaCached(cuaa: string, idValidazione: number): Observable<PersonaFisicaConCaricaDto[]> {
    return this.getPersoneFisicheConCarica(cuaa, idValidazione, false);
  }
  public getPersoneFisicheConCarica(cuaa: string, idValidazione: number, completa: boolean): Observable<PersonaFisicaConCaricaDto[]> {
    let paramsHttp = new HttpParams();
    if (idValidazione) {
      paramsHttp = paramsHttp.set('idValidazione', String(idValidazione));
    }
      
    paramsHttp = paramsHttp.set('completa', '' + completa);
    
    return this.http.get<PersonaFisicaConCaricaDto[]>(
      this.getUrlgetPersoneFisicheConCarica(cuaa),
      { params: paramsHttp, responseType: 'json', headers: {skipLoader: '' + completa}});
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

  public getPossibiliRappresentantiLegali(cuaa: string): Observable<PersonaFisicaConCaricaDto[]> {
    return this.http.get<PersonaFisicaConCaricaDto[]>(
      this.getUrlGetPossibiliRappresentantiLegali(cuaa));
  }

  public getFirmatario(cuaa: string): Observable<PersonaFisicaConCaricaDto> {
    return this.http.get<PersonaFisicaConCaricaDto>(
      this.getUrlGetFirmatario(cuaa));
  }

  public putSalvaFirmatario(firmatario: Firmatario, cuaa: string): Observable<void> {
    const formInput: FormData = new FormData();
    formInput.append('codiceFiscale', firmatario.codiceFiscale);
    formInput.append('tipoDocumento', firmatario.tipoDocumento);
    formInput.append('numeroDocumento', firmatario.numeroDocumento);
    formInput.append('dataRilascio', formatDate(firmatario.dataRilascio, 'yyyy-MM-dd', 'it-IT'));
    formInput.append('dataScadenza', formatDate(firmatario.dataScadenza, 'yyyy-MM-dd', 'it-IT'));
    formInput.append('documento', firmatario.documento);

    return this.http.post<void>(this.getUrlPutSalvaFirmatario(cuaa), formInput);
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

  public putStatoAllaFirmaAziendaFascicolo(cuaa: string): Observable<void> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.put<void>(this.putUrlStatoControllatoFascicolo(cuaa), headers);
  }

  public putSetStatusFascicoloInAggiornamento(cuaa: string): Observable<void> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.put<void>(this.putUrlSetStatusFascicoloInAggiornamento(cuaa), headers);
  }

  public putReportSchedaValidazione(file: File, cuaa: string): Observable<Blob> {
    const formInput: FormData = new FormData();
    formInput.append('schedaValidazioneFirmata', file);
    return this.http.put<any>(this.getUrlPutReportSchedaValidazione(cuaa), formInput);
  }

  public putReportSchedaValidazioneFirmaCaaAzienda(file: File, cuaa: string): Observable<Blob> {
    return this.http.put<any>(this.getUrlPutReportSchedaValidazioneFirmaCaaAzienda(cuaa), file);
  }

  public getReportTrasferimentoOp(cuaa: string, importaFascicoloStato: IMPORTA_FASCICOLO_STATO): Observable<Blob> {
    let paramsHttp = new HttpParams();
    paramsHttp = paramsHttp.set('fascicoloOperationEnum', importaFascicoloStato);
    return this.http.get(this.UrlGetReportTrasferimentoOp(cuaa), { params: paramsHttp, responseType: 'blob' });
    // return this.http.get(this.UrlGetReportTrasferimentoOp(cuaa), { responseType: 'blob' });
  }

  public getUrlCompletaTrasferimentoDaAltroOP(codiceFiscaleAzienda: string) {
    return `${this.urlAnagraficaFascicolo}/${codiceFiscaleAzienda}/completa-trasferimento-da-altro-op`;
  }

  public completaTrasferimentoDaAltroOP(cuaa: string): Observable<FascicoloCreationResultDto> {
    return this.http.get<FascicoloCreationResultDto>(this.getUrlCompletaTrasferimentoDaAltroOP(cuaa)).pipe(
      map(res => FascicoloCreationResultDto.toDto(res)));
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

  public getReportSchedaValidazioneSnapshot(cuaa: string, idValidazione: number): Observable<Blob> {
    let paramsHttp = new HttpParams();
    if (idValidazione) {
      paramsHttp = paramsHttp.set('idValidazione', String(idValidazione));
    }
    return this.http.get(this.getUrlGetReportSchedaValidazioneSnapshot(cuaa), { params: paramsHttp, responseType: 'blob' });
  }

  public getCheckMigraFascicolo(cuaa: string, stato: string, tipoDetenzione: string, sportello: string): Observable<DatiAperturaFascicoloDto> {
    let paramsHttp = new HttpParams();
    paramsHttp = paramsHttp.set('stato', stato.replace(' ', '_'));
    paramsHttp = paramsHttp.set('tipoDetenzione', tipoDetenzione == 'MAN' ? String(TipoDetenzioneAgs.MANDATO) : String(TipoDetenzioneAgs.DELEGA));
    paramsHttp = paramsHttp.set('sportello', sportello);
    return this.http.get<DatiAperturaFascicoloDto>(this.getUrlGetCheckMigraFascicolo(cuaa), { params: paramsHttp }).pipe(
      tap((retVal: any) => {
        return DatiAperturaFascicoloDto.toDto(retVal);
      })
    );
  }

  public chiudiFascicolo(cuaa: string): Observable<void> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.put<void>(this.putUrlChiudiFascicolo(cuaa), headers);
  }

  public sospendiFascicolo(cuaa: string, sospensioneFascicolo: SospensioneFascicolo): Observable<any> {
    const formInput: FormData = new FormData();
    formInput.append('dataSospensione', formatDate(sospensioneFascicolo.dataSospensione, 'yyyy-MM-dd', 'it-IT'));
    formInput.append('motivazioneSospensione', sospensioneFascicolo.motivazioneSospensione);
    return this.http.put<any>(this.postUrlSospendiFascicolo(cuaa), formInput);
  }

  public rimuoviSospensioneFascicolo(cuaa: string, sospensioneFascicolo: SospensioneFascicolo): Observable<any> {
    const formInput: FormData = new FormData();
    formInput.append('dataSospensione', formatDate(sospensioneFascicolo.dataSospensione, 'yyyy-MM-dd', 'it-IT'));
    formInput.append('motivazioneSospensione', sospensioneFascicolo.motivazioneSospensione);
    return this.http.put<any>(this.postUrlRimuoviSospensioneFascicolo(cuaa), formInput);
  }

  public getVerificaRiaperturaFascicolo(codiceFiscale: string): Observable<DatiAperturaFascicoloDto> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.get<any>(this.getUrlVerificaRiaperturaFascicolo(codiceFiscale), { headers: headers }).pipe(
      tap((retVal: any) => {
        return DatiAperturaFascicoloDto.toDto(retVal);
      })
    );
  }

  public ricostituisci(datiApertura: MandatoDto): Observable<any> {
    const formInput: FormData = new FormData();
    // const headers = new HttpHeaders().set('Accept', 'text/plain; charset=utf-8');
    // formInput.append('codiceFiscale', datiApertura.codiceFiscale);
    formInput.append('codiceFiscaleRappresentante', datiApertura.codiceFiscaleRappresentante);
    formInput.append('contratto', datiApertura.contratto);
    formInput.append('identificativoSportello', datiApertura.identificativoSportello.toString());

    datiApertura.allegati.forEach((el: AllegatoMandato) => {
      formInput.append('allegati', el.file, el.descrizione);
    });
    return this.http.post(
      this.getUrlRicostituisci(datiApertura.codiceFiscale),
      formInput, { responseType: 'text' });
  }

  public getEredi(cuaa: string, idValidazione: number): Observable<EredeDto[]> {
    let paramsHttp = new HttpParams();
    if (idValidazione) {
      paramsHttp = paramsHttp.set('idValidazione', String(idValidazione));
    }
    return this.http.get<EredeDto[]>
      (this.getUrlEredi(cuaa), { params: paramsHttp })
      .pipe(
        tap((retVal: any) => {
          return EredeDto.toDtos(retVal);
        })
      );
  }

  public salvaErede(erede: EredeDto, cuaa: string): Observable<void> {
    const formInput: FormData = new FormData();
    formInput.append('cfErede', erede.cfErede);

    return this.http.put<void>(this.getUrlPutErede(cuaa), formInput);
  }

  public cancellaErede(cuaa: string, id: number): any {
    return this.http.delete<any>(this.getUrlDeleteErede(cuaa, id), { responseType: 'json' });
  }



}
