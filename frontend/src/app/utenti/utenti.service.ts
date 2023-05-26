import { HttpClient, HttpErrorResponse, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Params } from '@angular/router';
import * as FileSaver from 'file-saver';
import { Observable, of, EMPTY, Subject } from 'rxjs';
import { Cached } from 'src/assets/decorators/cached';
import { A4gMessages } from '../a4g-common/a4g-messages';
import { DipartimentoPat } from '../a4g-common/classi/dipartimento-paa';
import { Distributore } from '../a4g-common/classi/distributore';
import { EnteCaa } from '../a4g-common/classi/EnteSedeCaa';
import { IstruttoriaProfiloUtente, IstruttoriaProfiloUtenteSenzaDomanda } from '../a4g-common/classi/istruttoria-profilo-utente';
import { KeyValue } from '../a4g-common/classi/KeyValue';
import { Persona } from '../a4g-common/classi/Persona';
import { CaricaDto } from '../amministrazione/gestione-utenze/model/facicoloImprese.model';
import { FiltroRicercaRichiesteAccessoSistema } from '../amministrazione/gestione-utenze/richieste-accesso-sistema/dto/filtro-ricerca-richieste-accesso-sistema';
import { RichiesteAccessoSistemaElenco } from '../amministrazione/gestione-utenze/richieste-accesso-sistema/dto/RichiesteAccessoSistemaElenco';
import { StatoRichiestaCount } from '../amministrazione/gestione-utenze/richieste-accesso-sistema/dto/StatoRichiestaCount';
import { DatiUtente, ProfiliAgs, ProfiliSrt, Profilo } from '../amministrazione/ricerca-utenti/dto/Profili';
import { Configuration } from '../app.constants';
import { Utente } from '../auth/user';
import { DatiAnagrafici } from './classi/datiAnagrafici';
import { DatiDomanda, TipoDomandaRegistrazione } from './classi/DatiDomanda';
import { DomandaUtente } from './classi/DomandaUtente';
import { AuthService } from '../auth/auth.service';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'application/json' })
};

@Injectable({
  providedIn: 'root'
})
export class UtentiService {
  constructor(
    private http: HttpClient,
    private authService: AuthService,
    private _configuration: Configuration
  ) { }

  public checkFascicoloValido(cuaa: string): Observable<Boolean> {
    return this.http.get<Boolean>(
      this._configuration.UrlCheckFascicoloValido.replace('${cuaa}', cuaa)
    );
  }

  public getAziendaRappresentata(
    cuaa: string,
    cfPersona: string
  ): Observable<KeyValue> {
    return this.http.get<KeyValue>(
      this._configuration.UrlAziendaRappresentante.replace(
        '${cuaa}',
        cuaa
      ).replace('${cfPersona}', cfPersona)
    );
  }

  @Cached()
  public getDatiAnagraficiUtente(): Observable<DatiAnagrafici> {
    console.log('getDatiAnagraficiUtente');
    let headers = new HttpHeaders().append('Authorization', this.authService.getAccessToken());
    return this.http.get<DatiAnagrafici>(
      this._configuration.urlGetDatiAnagraficiUtente, { headers: headers }
    );
  }

  public getUfficiCaa(): Observable<EnteCaa[]> {
    console.log('getUfficiCaa');
    return this.http.get<Array<EnteCaa>>(this._configuration.UrlGetEntiCAA);
  }

  @Cached()
  public getProfili(): Observable<Profilo[]> {
    console.log('getProfili');
    return this.http.get<Array<Profilo>>(this._configuration.UrlGetProfili);
  }

  @Cached()
  public getProfiliUtente(): Observable<Array<Profilo>> {
    return this.http.get<Array<Profilo>>(this._configuration.UrlGetProfiliUtente);
  }

  public getDipartimentiPat(): Observable<Array<DipartimentoPat>> {
    console.log('getDipartimentiPat');
    return this.http.get<Array<DipartimentoPat>>(
      this._configuration.UrlGetDipartimentiPat
    );
  }

  /* public getPdfAutorizzazioneDirigente(): Observable<Blob> {
    console.log('getPdf autorizzazione');

    return this.http.get(this._configuration.getModuloAutorizzazioneEnti, {
      responseType: 'blob'
    });
  } */

  public getPdfAutorizzazioneUtentePAT(): Observable<Blob> {
    console.log('getPdf autorizzazione');

    return this.http.get(this._configuration.getModuloAutorizzazioneUtentePAT, {
      responseType: 'blob'
    });
  }

  public getPdfAutorizzazioneAltriEnti(): Observable<Blob> {
    console.log('getPdf autorizzazione');

    return this.http.get(this._configuration.getModuloAutorizzazioneAltriEnti, {
      responseType: 'blob'
    });
  }

  public getPdfAutorizzazioneConsulente(): Observable<Blob> {
    console.log('getPdf autorizzazione');

    return this.http.get(this._configuration.getModuloAutorizzazioneConsulente, {
      responseType: 'blob'
    });
  }

  /*public getPdfAutorizzazioneDistributore(): Observable<Blob> {
    return this.http.get(this._configuration.getModuloAutorizzazioneDistributore, {
      responseType: 'blob'
    });
  }*/

  public getDomanda(idDomanda: number, cf: string): Observable<Blob> {
    const subject = new Subject<Blob>();

    this.http
      .get(
        this._configuration.urlDomandaRegUtenteStampa.replace(
          '{id}',
          idDomanda.toString()
        ),
        {
          responseType: 'blob'
        }
      )
      .subscribe(
        (response: Blob) => {
          FileSaver.saveAs(response, 'ModuloRichiestaAccesso_' + cf + '.pdf');
          subject.next(response);
          subject.complete();
        },
        (err: HttpErrorResponse) => {
          console.error(
            'Errore nella generazione della del modulo di domanda di accesso',
            err
          );
          subject.error(err);
          subject.complete();
        }
      );
    return subject.asObservable();
  }

  private blobToFile = (theBlob: Blob, fileName: string): File => {
    const b: any = theBlob;
    // A Blob() is almost a File() - it's just missing the two properties below which we will add
    b.lastModifiedDate = new Date();
    b.name = fileName;
    // Cast to a File() type
    return <File>theBlob;
  }

  public inserisciDomanda(datiDomanda: DatiDomanda): Observable<any> {
    console.log('salva dati domanda ');
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.post<any>(
      this._configuration.UrlUtente,
      JSON.stringify(datiDomanda),
      { headers: headers }
    );
  }

  public aggiornaDomanda(datiDomanda: DatiDomanda): Observable<any> {
    console.log('salva dati domanda ');
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.put<any>(
      this._configuration.UrlUtente,
      JSON.stringify(datiDomanda),
      { headers: headers }
    );
  }

  public creaDomanda(utente: Utente): Observable<any> {
    console.log('Crea domanda ');
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });

    return this.http.post<any>(
      this._configuration.UrlCreaDomanda, JSON.stringify(utente), { headers: headers }
    );
  }

  public ricercaDomanda(parametriRicerca: DomandaUtente): Observable<RichiesteAccessoSistemaElenco> {
    let paramshttp = new HttpParams();
    paramshttp = paramshttp.set('stato', parametriRicerca.stato);
    paramshttp = paramshttp.set('codiceFiscale', parametriRicerca.codiceFiscale);
    paramshttp = paramshttp.set('numeroElementiPagina', '1');
    paramshttp = paramshttp.set('pagina', '0');
    paramshttp = paramshttp.set('proprieta', 'dtProtocollazione');
    paramshttp = paramshttp.set('ordine', 'DESC');
    return this.http.get<RichiesteAccessoSistemaElenco>(
      this._configuration.UrlRicercaDomanda, { params: paramshttp }
    );
  }

  public chiudiDomanda(idDomanda: number): Observable<DomandaUtente> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.put<DomandaUtente>(
      this._configuration.UrlChiudiDomanda.replace('${id}', idDomanda.toString()),
      {
        headers: headers
      }
    );
  }

  public readByteFile(file: File) {
    return new Promise((resolve, reject) => {
      const reader = new FileReader();
      const error = 'Errore nell’estrapolare i byte dal file ' + file.name;
      let value;
      if (A4gMessages.isUndefinedOrNull(file)) {
        reject(error);
      } else {
        reader.readAsDataURL(file);
        reader.onloadend = event => {
          value = reader.result;
          value == null
            ? reject(error)
            : resolve(value.toString().split(',')[1]);
        };
      }
    });
  }

  public ricercaPersone(parametriRicerca: any): Observable<Array<Persona>> {
    const json = JSON.stringify(parametriRicerca);
    let headers = new HttpHeaders().append('Authorization', this.authService.getAccessToken());
    return this.http.get<Array<Persona>>(
      this._configuration.UrlPersonaPrivacy + encodeURIComponent(json), { headers: headers }
    );
  }

  public inserisciPersona(persona: Persona): Observable<number> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    const json = JSON.stringify(persona);
    return this.http.post<number>(this._configuration.UrlPersone, json, {
      headers: headers
    });
  }

  public aggiornaPersona(persona: Persona, id: number): Observable<Persona> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    const json = JSON.stringify(persona);
    return this.http.put<Persona>(
      this._configuration.UrlPersone + '${id}'.replace('${id}', String(id)),
      json,
      { headers: headers }
    );
  }

  public protocollaInformativaPrivacyDichiarazioneAntimafia(
    nome: string,
    cognome: string,
    codiceFiscale: string,
    info: any, documento: File, allegati: File[]): Observable<void> {
    const formInput: FormData = new FormData();
    formInput.append('nome', nome);
    formInput.append('cognome', cognome);
    formInput.append('codiceFiscale', codiceFiscale);
    formInput.append('documento', documento, documento.name);
    if (allegati !== null && allegati.length > 0) {
      allegati.forEach(allegato => {
        formInput.append('allegati', allegato, allegato.name);
      });
    }
    formInput.append('info', info);
    return this.http.post<void>(this._configuration.urlAvvioProtocollazioneUtente, formInput);
  }

  public sottoScriviPrivacy(documentazione: File): Observable<any> {
    const formData = new FormData();
    formData.append('documento', documentazione, documentazione.name);
    // const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.post<any>(this._configuration.urlFirmaUtente, formData);
  }

  public sottoScriviDomanda(idDomanda: number): Observable<any> {
    return this.http.post(
      this._configuration.urlDomandaRegUtenteFirma.replace(
        '{id}',
        idDomanda.toString()
      ),
      {
        responseType: 'any'
      }
    );
  }

  public protocollaDomanda(idDomanda: number): Observable<void> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.put<void>(
      this._configuration.urlDomandaProtocolla.replace(
        '{id}',
        idDomanda.toString()
      ),
      { headers: headers }
    );
  }

  public getDatiUltimaDomandaApprovata(codiceFiscale: string): Observable<DatiDomanda> {
    console.log('getDatiUltimaDomandaApprovata');
    const subject = new Subject<DatiDomanda>();
    let paramshttp = new HttpParams();
    paramshttp = paramshttp.set('stato', 'APPROVATA');
    paramshttp = paramshttp.set('codiceFiscale', codiceFiscale);
    paramshttp = paramshttp.set('numeroElementiPagina', '1');
    paramshttp = paramshttp.set('pagina', '0');
    paramshttp = paramshttp.set('proprieta', 'dtProtocollazione');
    paramshttp = paramshttp.set('ordine', 'DESC');
    this.http
      .get(this._configuration.UrlRicercaDomanda, { params: paramshttp })
      .subscribe(
        (response: RichiesteAccessoSistemaElenco) => {
          if (response && response.risultati && response.risultati[0]) {
            console.log('getDatiUltimaDomandaApprovata: trovata domanda ' + response.risultati[0].id.toString());
            return this.http
              .get(this._configuration.UrlGetDomanda.replace('${id}', response.risultati[0].id.toString()))
              .subscribe((datiDomanda: DatiDomanda) => {
                subject.next(datiDomanda);
                subject.complete();
              });
          }
          subject.next(null);
          subject.complete();
        },
        (error: HttpErrorResponse) => {
          console.error('Errore inaspettato', error);
          subject.error(error);
          subject.complete();
        }
      );
    return subject.asObservable();
  }

  public getStatoRichiesteCount(filtro: FiltroRicercaRichiesteAccessoSistema): Observable<Array<StatoRichiestaCount>> {
    const paramshttp: Params = filtro.getHttpParams(null);
    return this.http.get<Array<StatoRichiestaCount>>(
      this._configuration.UrlStatoRichiestaCount, { params: paramshttp }
    );
  }

  public getProfiliSrt(datiAnagrafici: DatiAnagrafici): Observable<Array<ProfiliSrt>> {
    let paramshttp = new HttpParams();
    if (datiAnagrafici.nome) {
      paramshttp = paramshttp.set('nome', datiAnagrafici.nome);
    }
    if (datiAnagrafici.cognome) {
      paramshttp = paramshttp.set('cognome', datiAnagrafici.cognome);
    }
    if (datiAnagrafici.codiceFiscale) {
      paramshttp = paramshttp.set('codiceFiscale', datiAnagrafici.codiceFiscale);
    }
    return this.http.get<Array<ProfiliSrt>>(
      this._configuration.UrlGetProfiliSrt,
      { params: paramshttp }
    );
  }

  public getProfiliAgs(datiAnagrafici: DatiAnagrafici): Observable<Array<ProfiliAgs>> {
    let paramshttp = new HttpParams();
    if (datiAnagrafici.nome) {
      paramshttp = paramshttp.set('nome', datiAnagrafici.nome);
    }
    if (datiAnagrafici.cognome) {
      paramshttp = paramshttp.set('cognome', datiAnagrafici.cognome);
    }
    if (datiAnagrafici.codiceFiscale) {
      paramshttp = paramshttp.set('codiceFiscale', datiAnagrafici.codiceFiscale);
    }
    return this.http.get<Array<ProfiliAgs>>(
      this._configuration.UrlGetProfiliAgs,
      { params: paramshttp }
    );
  }

  public getProfiliA4g(datiDomanda: DatiAnagrafici): Observable<Array<DatiUtente>> {
    let paramshttp = new HttpParams();
    if (datiDomanda.nome) {
      paramshttp = paramshttp.set('nome', datiDomanda.nome);
    }
    if (datiDomanda.cognome) {
      paramshttp = paramshttp.set('cognome', datiDomanda.cognome);
    }
    if (datiDomanda.codiceFiscale) {
      paramshttp = paramshttp.set('codiceFiscale', datiDomanda.codiceFiscale);
    }
    return this.http.get<Array<DatiUtente>>(
      this._configuration.UrlGetProfiliA4g,
      { params: paramshttp }
    );
  }

  public putPresaInCarico(idDomanda: number) {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.put<any>(this._configuration.UrlPresaInCarico
      .replace('${id}', idDomanda.toString()),
      { headers: headers });
  }

  public putIstruttoriaProfilo(istruttoriaProfiloUtente: IstruttoriaProfiloUtente) {
    return this.http.put<IstruttoriaProfiloUtente>(this._configuration.UrlIstruttoriaByDomanda, istruttoriaProfiloUtente);
  }

  public postIstruttoriaProfilo(istruttoriaProfiloUtente: IstruttoriaProfiloUtente) {
    return this.http.post<IstruttoriaProfiloUtente>(this._configuration.UrlIstruttoriaByDomanda, istruttoriaProfiloUtente);
  }

  public postIstruttoriaProfiloUtenteByUtente(idUtente: number, istruttoriaSenzaDomanda: IstruttoriaProfiloUtenteSenzaDomanda) {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.post<IstruttoriaProfiloUtenteSenzaDomanda>(
      this._configuration.UrlPostIstruttoriaUtenteByIdUtente.replace('${idUtente}', idUtente.toString()),
      istruttoriaSenzaDomanda,
      { headers: headers });
  }

  public getListaCariche(codiceFiscale: string): Observable<CaricaDto[]> {
    if (!codiceFiscale) {
      this.authService.getUserFromSession().subscribe(
          x => {
            console.log('Observer next value: ' + x.codiceFiscale);
            this.authService.setUser(x);
            codiceFiscale = x.codiceFiscale;
            return this.http.get<CaricaDto[]>(`${this._configuration.anagrafica_server}/legacy/persona/${codiceFiscale}/carica`);
          },
          err => { 
            console.error('Observer error: ' + err);
          }
      );
    }
    return EMPTY;
  }

  public getProfiliUtenteConnesso(): Observable<Array<Profilo>> {
    let headers = new HttpHeaders().append('Authorization', this.authService.getAccessToken());
    return this.http.get<Array<Profilo>>(this._configuration.urlGetProfiliUtenteConnesso, { headers: headers });
  }

  public getUltimaDomandaUtenteCorrente(statoDomanda: string, tipoDomanda: TipoDomandaRegistrazione): Observable<DatiDomanda> {
    let paramshttp = new HttpParams();
    if (statoDomanda) {
      paramshttp = paramshttp.set('statoDomanda', statoDomanda);
    }
    if (tipoDomanda) {
      paramshttp = paramshttp.set('tipoDomanda', tipoDomanda);
    }
    let headers = new HttpHeaders().append('Authorization', this.authService.getAccessToken());
    return this.http.get<DatiDomanda>(this._configuration.UrlGetUltimaDomandaUtenteCorrente, 
        { params: paramshttp, headers: headers });
  }

  //download csv utenti
  public getEsportaUtentiA4gCsv() : Observable<any> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    return this.http.get(
      this._configuration.UrlUtentiA4gCsv, { headers, responseType: 'blob', observe: 'response'}
    );
  }

  public getDistributori(): Observable<Distributore[]> {
    return this.http.get<Array<Distributore>>(this._configuration.UrlGetDistributori);
  }
}
