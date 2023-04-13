import { Injectable } from '@angular/core';
import { Cached } from 'src/app/shared/decorators/cached';
import { Observable, of } from 'rxjs';
import { DatiAnagrafici, DatiDomanda, Firma, Persona } from './datiAnagrafici';
import { HttpClient } from '@angular/common/http';
import { environment } from './../../../environments/environment';
import { UTENTE_SERVER } from 'src/app/app.constants';
import { DocumentazionePrivacy, DomandaRegistrazioneCreataResponse } from './models/register.models';
import { AuthService } from 'src/app/shared/services/auth.service';

@Injectable({
  providedIn: 'root'
})
export class RegisterService {

  public urlGetDatiAnagraficiUtente = `${UTENTE_SERVER}/utenti/utente/anagrafica`;
  public urlIsUtenteRegistrabile = `${UTENTE_SERVER}/utenti/utente/isRegistrabile`;
  public urlDomandaRegistrazioneUtente = `${UTENTE_SERVER}/domande`;
  public urlFirmaPrivacyUtente = `${UTENTE_SERVER}/utenti/utente/firma`;
  public urlRegistrazioneFirmaDocumentoUtente = `${UTENTE_SERVER}/domande/registrazione-firma`;
  // public urlProtocollaPrivacy = `${PROXY_SERVER}/protocollo/documenti`;
  public urlAvvioProtocollazionePrivacy = `${UTENTE_SERVER}/utenti/utente/avvio-protocollazione`;
  public urlGetInfoPrivacy = environment.frontendUrl + 'assets/staticfiles/informativa_privacy.pdf';
  public urlPersonaPrivacy = `${UTENTE_SERVER}/persone/?params=`;
  public urlPersone = `${UTENTE_SERVER}/persone`;
    public getUrlPersone(id: number) {
    return `${UTENTE_SERVER}/persone/${id}`;
  }

  constructor(
    private http: HttpClient
    ) { }

  @Cached()
  public getDatiAnagraficiUtente(): Observable<DatiAnagrafici> {
    console.log("getDatiAnagraficiUtente");
    return this.http.get<DatiAnagrafici>(
      this.urlGetDatiAnagraficiUtente
    );
  }

  public utenteRegistrabile(): Observable<boolean> {
    console.log("utenteRegistrabile");
    return this.http.get<boolean>(
      this.urlIsUtenteRegistrabile
    );
  }

  public inserisciDomanda(datiDomanda: DatiDomanda): Observable<number> {
    return this.http.post<number>(this.urlDomandaRegistrazioneUtente, datiDomanda);
  }

  public aggiornaDomanda(datiDomanda: DatiDomanda): Observable<number> {
    return this.http.put<number>(this.urlDomandaRegistrazioneUtente, datiDomanda);
  }

  public downloadInformativaPrivacy(): Observable<any> {
    return this.http.get(this.urlGetInfoPrivacy, {
      responseType: 'blob'
    });
  }
  
  public sottoScriviPrivacy(documentazione: File): Observable<Firma> {
    const formData = new FormData();
    formData.append("documento", documentazione, documentazione.name);
    return this.http.post<Firma>(this.urlFirmaPrivacyUtente, formData);
    // let f: Firma = new Firma();
    // f.xml = "<xml></xml>";
    // f.pdf = btoa("<pdf><test>1234</test></pdf>");
    // return of(f);
  }

  public registrazioneFirmaDocumento(datiDomanda: DatiDomanda): Observable<DomandaRegistrazioneCreataResponse> {
    return this.http.post<DomandaRegistrazioneCreataResponse>(this.urlRegistrazioneFirmaDocumentoUtente, datiDomanda);
  }

  // public protocollaInformativaPrivacy(info: DocumentazionePrivacy, documento: File, allegati: File[]): Observable<string> {
  public protocollaInformativaPrivacy(nome: string, cognome: string, codiceFiscale: string, info: DocumentazionePrivacy, documento: File, allegati: File[]): Observable<string> {
    let formInput: FormData = new FormData();
    formInput.append("nome", nome);
    formInput.append("cognome", cognome);
    formInput.append("codiceFiscale", codiceFiscale);
    formInput.append("documento", documento, documento.name);
    if (allegati !== null && allegati.length > 0) {
      allegati.forEach(allegato => {
        formInput.append("allegati", allegato, allegato.name);
      });
    }
    formInput.append("info", JSON.stringify(info));
    return this.http.post(this.urlAvvioProtocollazionePrivacy, formInput, { responseType: 'text' });
    // return of("OK");
  }

  public ricercaPersone(cf: string): Observable<Persona[]> {
    const json = JSON.stringify({ codiceFiscale: cf });
    return this.http.get<Persona[]>(this.urlPersonaPrivacy + encodeURIComponent(json));
  }

  public inserisciPersona(persona: Persona): Observable<number> {
    return this.http.post<number>(this.urlPersone, persona);
  }

  public aggiornaPersona(persona: Persona, id: number): Observable<Persona> {
    return this.http.put<Persona>(this.getUrlPersone(id), persona);
  }
}
