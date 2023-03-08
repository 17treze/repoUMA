import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { MandatoDto } from '../a4g-common/classi/FascicoloCuaa';
import {AllegatoMandato, MandatoDto as CreazioneFascicoloMandatoDto} from './creazione-fascicolo/dto/MandatoDto';
import { Configuration } from '../app.constants';
import {map, tap} from 'rxjs/operators';
import { Paginazione } from '../a4g-common/utility/paginazione';
import { RichiestaRevocaImmediataDto, RichiesteRevocaImmediata } from './richieste-revoca-mandato/dto/RichiestaRevocaImmediataDto';
import { Cached } from 'src/assets/decorators/cached';
import {DatiAperturaFascicoloDto} from "./creazione-fascicolo/dto/DatiAperturaFascicoloDto";

@Injectable()
export class MandatoService {

  constructor(
    private httpClient: HttpClient,
    private configuration: Configuration,
  ) { }

  anagrafica_server_mandato = `${this.configuration.anagrafica_server}/mandato`;

  public getUrlAcquisizioneMandato(codiceFiscaleAzienda: string) {
    return `${this.anagrafica_server_mandato}/${codiceFiscaleAzienda}/acquisisci-mandato`;
  }

  public getUrlGetMandatoByCuaa(cuaa: string) {
    return `${this.anagrafica_server_mandato}/${cuaa}`;
  }

  public putUrlValutaRichiestaRevocaImmediata(cuaa: string, accettata: boolean) {
    return `${this.anagrafica_server_mandato}/${cuaa}/valuta-revoca-immediata?accettata=${accettata}`;
  }

  public postUrlRevocaImmediata(cuaa: string) {
    return `${this.anagrafica_server_mandato}/${cuaa}/richiesta-revoca-immediata-mandato`;
  }

  public getRichiesteRevocheImmediate(valutata: boolean): Observable<RichiestaRevocaImmediataDto[]> {
    return this.httpClient.get<RichiestaRevocaImmediataDto[]>(this.getUrlRevocaImmediata(valutata), { responseType: 'json' })
      .pipe(map(richiesteList => {
        if (richiesteList) {
          const richiesteDtoList: RichiestaRevocaImmediataDto[] = [];
          for (const richiesta of richiesteList) {
            const richiestaDto: RichiestaRevocaImmediataDto = RichiestaRevocaImmediataDto.toDto(richiesta);
            richiesteDtoList.push(richiestaDto);
          }
          return richiesteDtoList;
        }
        return richiesteList;
      }));
  }

  public getUrlFascicoloPerMandato(cuaa: string) {
    return `${this.anagrafica_server_mandato}/${cuaa}/dati-fascicolo`;
  }

  public getUrlRevocaOrdinaria(cuaa: string) {
    return `${this.anagrafica_server_mandato}/${cuaa}/revoca-mandato`;
  }

  public getUrlVerificaRevocaOrdinaria(cuaa: string) {
    return `${this.anagrafica_server_mandato}/${cuaa}/verifica/revoca-mandato`;
  }

  public getUrlVerificaPresenzaRevocaOrdinaria(cuaa: string) {
    return `${this.anagrafica_server_mandato}/${cuaa}/verifica-presenza-revoca-ordinaria`;
  }

  public getUrlVerificaInserimentoRevocaImmediata(cuaa: string) {
    return `${this.anagrafica_server_mandato}/${cuaa}/puo-inserire-revoca-immediata`;
  }

  public getUrlMandatoFile(idFascicolo, idMmandato) {
    return `${this.anagrafica_server_mandato}/${idMmandato}/fascicolo/${idFascicolo}/contratto`;
  }

  public getUrlRevocaImmediataFile(idProtocollo) {
    return `${this.anagrafica_server_mandato}/richiesta-revoca-immediata-mandato/documento-firmato?idProtocollo=${idProtocollo}`;
  }

  @Cached()
  public getMandati(cuaa: string, idValidazione: number): Observable<MandatoDto[]> {
    let paramsHttp = new HttpParams();
    if (idValidazione) {
      paramsHttp = paramsHttp.set('idValidazione', String(idValidazione));
    }
    return this.httpClient.get<MandatoDto[]>(this.getUrlGetMandatoByCuaa(cuaa), { params: paramsHttp })
      .pipe(map(mandatiList => {
        const mandatiDtoList: MandatoDto[] = [];
        for (const mandato of mandatiList) {
          const mandatoDto: MandatoDto = MandatoDto.toDto(mandato);
          mandatiDtoList.push(mandatoDto);
        }
        return mandatiDtoList;
      }));
  }

  public putRifiutaRichiestaRevocaImmediata(cuaa: string, motivazioneRifiuto: string): Observable<any> {
    return this.httpClient.put<any>(
      this.putUrlValutaRichiestaRevocaImmediata(cuaa, false), motivazioneRifiuto);
  }

  public putAccettaRichiestaRevocaImmediata(cuaa: string): Observable<any> {
    return this.httpClient.put<any>(
      this.putUrlValutaRichiestaRevocaImmediata(cuaa, true), null);
  }

  public getUrlRevocaImmediata(valutata: boolean) {
    return `${this.anagrafica_server_mandato}/richieste-revoca-immediata-mandato?valutata=${valutata}`;
  }

  public getUrlRevocaImmediataPaged(valutata: boolean) {
    return `${this.anagrafica_server_mandato}/richieste-revoca-immediata-mandato-paged?valutata=${valutata}`;
  }

  public getTemplateRevocaImmediata(): Observable<Blob> {
    return this.httpClient.get(this.configuration.UrlGetTemplateRevocaImmediata, { responseType: 'blob' });
  }

  public getUrlVerificaAcquisizioneMandato(codiceFiscale: string) {
    return `${this.anagrafica_server_mandato}/revoca-immediata/${codiceFiscale}/verifica/dati-acquisizione`;
  }

  public getVerificaAcquisizioneMandato(codiceFiscale: string): Observable<DatiAperturaFascicoloDto> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.httpClient.get<any>(this.getUrlVerificaAcquisizioneMandato(codiceFiscale), { headers: headers }).pipe(
        tap((retVal: any) => {
          return DatiAperturaFascicoloDto.toDto(retVal);
        })
    );
  }

  public stampaRevocaImmediata(file: File, dati: any): Observable<Blob> {
    const formData = new FormData();
    formData.append('file', file, file.name);
    formData.append('dati', JSON.stringify(dati));
    formData.append('formatoStampa', 'PDF_A');
    return this.httpClient.post(this.configuration.UrlStampaPDF, formData, { responseType: 'blob' });
  }

  public revocaImmediata(datiRevoca): Observable<any> {
    const formInput: FormData = new FormData();
    formInput.append('codiceFiscaleRappresentanteLegaleOTitolare', datiRevoca.codiceFiscaleRappresentante);
    formInput.append('moduloRevocaFirmato', datiRevoca.revocaFile);
    if (datiRevoca.causaRichiesta != null)
      formInput.append('causaRichiesta', datiRevoca.causaRichiesta);
    formInput.append('sportello', JSON.stringify(datiRevoca.sportello));
    return this.httpClient.post(
      this.postUrlRevocaImmediata(datiRevoca.cuaa),
      formInput, { responseType: 'text' }
    );
  }

  verificaMandato(cuaa: string): Observable<any> {
    const urlRevocaMandato = this.getUrlVerificaRevocaOrdinaria(cuaa);
    return this.httpClient.get<any>(urlRevocaMandato);
  }

  fascicoloPerMandato(cuaa: string, idValidazione: number): Observable<any> {
    let paramsHttp = new HttpParams();
    if (idValidazione) {
      paramsHttp = paramsHttp.set('idValidazione', String(idValidazione));
    }
    return this.httpClient.get<any>(this.getUrlFascicoloPerMandato(cuaa), 
      { params: paramsHttp });
  }

  eseguiRevocaOrdinaria(datiApertura: CreazioneFascicoloMandatoDto): Observable<any> {
    const urlEseguiRevoca = this.getUrlRevocaOrdinaria(datiApertura.codiceFiscale);
    const formInput: FormData = new FormData();
    formInput.append('codiceFiscaleRappresentante', datiApertura.codiceFiscaleRappresentante);
    formInput.append('contratto', datiApertura.contratto);
    formInput.append('identificativoSportello', datiApertura.identificativoSportello.toString());
    datiApertura.allegati.forEach(el => {
      formInput.append('allegati', el.file, el.descrizione);
    });
    return this.httpClient.post<any>(urlEseguiRevoca, formInput);
  }

  verificaPresenzaRevocaOrdinaria(cuaa: string): Observable<boolean> {
    const urlRevocaImmediata = this.getUrlVerificaPresenzaRevocaOrdinaria(cuaa);
    return this.httpClient.get<boolean>(urlRevocaImmediata);
  }

  verificaInserimentoRevocaImmediata(cuaa: string): Observable<boolean> {
    const url = this.getUrlVerificaInserimentoRevocaImmediata(cuaa);
    return this.httpClient.get<boolean>(url);
  }

  public richiesteRevocaImmediataValutatePerCaa(paginazione: Paginazione):
    Observable<RichiesteRevocaImmediata> {
    const data: any = { ...paginazione };
    const url = this.getUrlRevocaImmediataPaged(true);
    return this.httpClient.get<RichiesteRevocaImmediata>(url, { params: data })
      .pipe(map(richiesteList => {
        if (richiesteList) {
          const richiesteDtoList: RichiesteRevocaImmediata = new RichiesteRevocaImmediata();
          richiesteDtoList.count = richiesteList.count;
          richiesteDtoList.risultati = new Array<RichiestaRevocaImmediataDto>();
          for (const richiesta of richiesteList.risultati) {
            const richiestaDto: RichiestaRevocaImmediataDto = RichiestaRevocaImmediataDto.toDto(richiesta);
            richiesteDtoList.risultati.push(richiestaDto);
          }
          return richiesteDtoList;
        }
        return richiesteList;
      }));
  }

  public getMandatoFile(idFascicolo: number, idMandato: number, idValidazione: number): Observable<any> {
    let paramsHttp = new HttpParams();
    if (idValidazione) {
      paramsHttp = paramsHttp.set('idValidazione', String(idValidazione));
    }
    return this.httpClient.get(this.getUrlMandatoFile(idFascicolo, idMandato), { responseType: 'blob', params: paramsHttp });
  }

  public getRevocaImmediataFile(idProtocollo: string): Observable<any> {
    return this.httpClient.get(this.getUrlRevocaImmediataFile(idProtocollo), { responseType: 'blob' });
  }

  public acquisisciMandato(datiApertura: CreazioneFascicoloMandatoDto): Observable<any> {
    const formInput: FormData = new FormData();
    // const headers = new HttpHeaders().set('Accept', 'text/plain; charset=utf-8');
    // formInput.append('codiceFiscale', datiApertura.codiceFiscale);
    formInput.append('codiceFiscaleRappresentante', datiApertura.codiceFiscaleRappresentante);
    formInput.append('contratto', datiApertura.contratto);
    formInput.append('identificativoSportello', datiApertura.identificativoSportello.toString());

    datiApertura.allegati.forEach((el: AllegatoMandato) => {
      formInput.append('allegati', el.file, el.descrizione);
    });
    return this.httpClient.post(
        this.getUrlAcquisizioneMandato(datiApertura.codiceFiscale),
        formInput, { responseType: 'text' });
  }
}
