import { Component, OnInit, OnDestroy, Output, EventEmitter } from '@angular/core';
import { RegisterService } from "./register.service";
import { DatiAnagrafici, DatiDomanda, ResponsabilitaRichieste, TitolareImpresa, ServiziTypeEnum, Firma, Persona, TipoDomandaRegistrazione } from './datiAnagrafici';
import { DomandeUnicheService } from '../domande-uniche/domande-uniche.service';
import { CaricaDto } from '../domande-uniche/models/facicoloImprese.model';
import { MessageService } from 'primeng-lts';
import { NgForm } from '@angular/forms';
import { A4gMessages, A4gSeverityMessage } from 'src/app/shared/a4g-messages';
import { Router, ActivatedRoute } from '@angular/router';
import { environment } from '../../../environments/environment';
import { switchMap, takeUntil, map, defaultIfEmpty, catchError } from 'rxjs/operators';
import { zip, Observable, empty, of } from 'rxjs';
import { Subject, EMPTY } from 'rxjs';
import { Utente } from './utente.model';
import { DocumentazionePrivacy, Mittente, DomandaRegistrazioneCreataResponse, TIPO_CREAZIONE_UTENTE_FASCICOLO } from './models/register.models';
import { Base64Support } from 'src/app/shared/utilities/base64.support';
import * as FileSaver from 'file-saver';
import { AuthService } from 'src/app/shared/services/auth.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit, OnDestroy {

  controlliDatiAnagrafici = [];
  cfReadonly = false;
  canAdd: boolean;
  listaCariche: CaricaDto[];
  listaCuaAziende
  translateService: any;
  public isRegistrabile: boolean = false;
  utente: Utente;
  viewPrivacy: boolean = false;
  urlReindirizzamento: string = "/";
  public datiAnagraficiUtente: DatiAnagrafici;
  public formPrivacyStatementAccepted: boolean = false;
  public datiDomandaAutorizzazioni: string = "";
  public inserimentoDisabled: boolean = false;
  public isPrivacyProtocollata = false;
  public isPrivacyFirmata: boolean = false;
  private firmaPrivacy: Firma;
  private informativaPrivacyFile: File;
  private informativaPrivacyFirmataPDF: File;
  private componentDestroyed$: Subject<boolean> = new Subject();
  private lastIdDomanda: number = undefined;
  private static ERRORE_GESTIONE_DOMANDA_REGISTRAZIONE_UTENTE: string = "ERRORE_GESTIONE_DOMANDA_REGISTRAZIONE_UTENTE";
  private persona: Persona = undefined;
  private ctx: TIPO_CREAZIONE_UTENTE_FASCICOLO;
  public disableCreaFascicoloAnagrafico = environment.disableCreaFascioloAnagrafico;

  @Output() public datiAnagraficiUtenteOutput = new EventEmitter();
  @Output() public privacyFirmataOutput = new EventEmitter();

  constructor(
    private authService: AuthService,
    private registerService: RegisterService,
    private messageService: MessageService,
    private domandeUnicheService: DomandeUnicheService,
    protected route: ActivatedRoute,
    private router: Router) { }

  ngOnInit() {
    this.getCtx();
    this.controlliDatiAnagrafici = [];
    this.controlliDatiAnagrafici.push('cognome');
    this.controlliDatiAnagrafici.push('nome');
    this.controlliDatiAnagrafici.push('codiceFiscale');
    this.controlliDatiAnagrafici.push('email');
    this.controlliDatiAnagrafici.push('telefono');
    this.canAdd = false;
    this.caricaUtente();
  }

  private getCtx() {
    if (this.router.url.indexOf('/register-crea-fascicolo') > -1) {
      this.ctx = TIPO_CREAZIONE_UTENTE_FASCICOLO.CREA_UTENTE_FASCICOLO;
    } else {
      this.ctx = TIPO_CREAZIONE_UTENTE_FASCICOLO.CREA_UTENTE;
    }
  }

  private handleVerificaPrivacy(persone: Persona[]) {
    if (persone && persone.length > 0 && persone[0]) {
      this.persona = persone[0];
      this.datiDomandaAutorizzazioni = this.persona.nrProtocolloPrivacyGenerale;
      if (this.datiDomandaAutorizzazioni && this.datiDomandaAutorizzazioni.length > 0) {
        this.viewPrivacy = true;
        this.formPrivacyStatementAccepted = true;
        this.isPrivacyFirmata = true;
        this.isPrivacyProtocollata = true;
      }
    }
  }

  private handleCaricaDatiAnagrafici(datiAnagraficiUtente) {
    this.datiAnagraficiUtente = datiAnagraficiUtente;
    this.cfReadonly = this.datiAnagraficiUtente.codiceFiscale != null && this.datiAnagraficiUtente.codiceFiscale.length > 0;
    if (this.datiAnagraficiUtente) {
      this.canAdd = true;
    }
  }

  private manageCaricaUtenteError = error => {
    console.error('Errore: ' + error.message);
    let errMsg = error.error.message;
    this.messageService.add(A4gMessages.getToast('generic', A4gSeverityMessage.error, 'Errore in recupero utente: ' + errMsg));
    return EMPTY;
  };

  private caricaUtente() {
    this.authService.getUser(true).pipe(
      catchError(this.manageCaricaUtenteError),
      takeUntil(EMPTY),
      switchMap((result) => {
        this.utente = result;
        return this.registerService.utenteRegistrabile();
      }),
      catchError(this.manageCaricaUtenteError),
      takeUntil(EMPTY),
      switchMap((isRegistrabile) => {
        this.isRegistrabile = isRegistrabile;
        if (isRegistrabile) {
          return this.registerService.getDatiAnagraficiUtente();
        } else {
          this.messageService.add({
            key: 'msgOK',
            sticky: true,
            severity: 'warn',
            summary: 'Attenzione',
            detail: A4gMessages.erroreRichiestaPresente
          });
          return EMPTY;
        }
      }),
      catchError(this.manageCaricaUtenteError),
      takeUntil(EMPTY),
      switchMap(datiAnagrafici => {
        this.handleCaricaDatiAnagrafici(datiAnagrafici);
        let cf = datiAnagrafici.codiceFiscale;
        return zip(this.domandeUnicheService.getListaCariche(cf), this.registerService.ricercaPersone(cf));
      }),
      catchError(this.manageCaricaUtenteError),
      takeUntil(EMPTY),
      map(([cariche, persone]) => {
        this.handleLoadListaCariche(cariche);
        this.handleVerificaPrivacy(persone);
      }),
      takeUntil(this.componentDestroyed$)
    ).subscribe();
  }

  utenteRegistrabile(): boolean {
    let registrabile: boolean;
    this.registerService.utenteRegistrabile().subscribe(
      (result) => {
        registrabile = result;
        return registrabile;
      }
    );
    return false;
  }

  private handleLoadListaCariche(listaCariche) {
    this.listaCariche = listaCariche;
    if (this.ctx === TIPO_CREAZIONE_UTENTE_FASCICOLO.CREA_UTENTE && (!this.listaCariche || this.listaCariche.length == 0)) {
      this.messageService.add({ key: 'msgOK', sticky: true, severity: 'warn', summary: 'Attenzione', detail: this.getMessage() });
    }
  }

  private getMessage() {
    return this.disableCreaFascicoloAnagrafico ? A4gMessages.erroreNessunaAziendaPresente : A4gMessages.erroreNessunaAziendaPresenteRicercaCuaa;
  }

  public vaiAHome() {
    console.log("this.urlReindirizzamento: " + this.urlReindirizzamento);
    if ((this.utente.profili == null) || (this.utente.profili.length == 0)) {
      this.urlReindirizzamento = environment.indexPage;
    } else {
      this.urlReindirizzamento = environment.mobileUrl;
    }
    window.location.href = this.urlReindirizzamento;
  }

  public redirect() {
    if (this.isCtxCreaUtente() && this.isRegistrabile && !this.disableCreaFascicoloAnagrafico) {
      this.router.navigate(['../register-crea-fascicolo/'], { relativeTo: this.route });
    } else {
      this.vaiAHome();
    }
  }

  private popolaApplicativi(): ServiziTypeEnum[] {
    return [ServiziTypeEnum.A4G];
  }

  private popolaResponsabilita(): ResponsabilitaRichieste {
    const responsabilitaRichieste = new ResponsabilitaRichieste();
    responsabilitaRichieste.responsabilitaLegaleRappresentante = [];
    for (let carica of this.listaCariche) {
      let denominazione = "";
      if (carica && carica.denominazione) {
        denominazione = carica.denominazione;
      }
      let cuaa = carica.cuaa;
      responsabilitaRichieste.responsabilitaLegaleRappresentante.push(new TitolareImpresa(cuaa, denominazione));
    }
    return responsabilitaRichieste;
  }

  aggiorna(datiDomanda: DatiDomanda) {
    datiDomanda.tipoDomandaRegistrazione = TipoDomandaRegistrazione.RIDOTTA_AZIENDA;
    this.registerService.aggiornaDomanda(datiDomanda).subscribe(
      x => {
        // this.modificaStatoRichiesta();
        this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.success, A4gMessages.RICHIESTA_REGISTRAZIONE_OK));
        datiDomanda.id = x;
      }, error => {
        A4gMessages.handleError(this.messageService, error, A4gMessages.ERRORE_NON_PREVISTO);
      }
    );
  }

  private blobToFile = (theBlob: Blob, fileName: string): File => {
    const b: any = theBlob;
    // A Blob() is almost a File() - it's just missing the two properties below which we will add
    b.lastModifiedDate = new Date();
    b.name = fileName;
    // Cast to a File() type
    return <File>theBlob;
  }

  public downloadInformativaPrivacy(): any {
    this.registerService.downloadInformativaPrivacy().subscribe(response => {
      FileSaver.saveAs(response, 'informativa_privacy.pdf');
      this.viewPrivacy = true;
      this.informativaPrivacyFile = this.blobToFile(response, 'informativa_privacy.pdf');
    }, error => {
      this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.ERRORE_NON_PREVISTO));
    });
  }

  public sottoScriviPrivacyCheck(checked: any) {
    console.log('Evento sottoscrivi privacy', checked);
    if (checked && !this.isPrivacyFirmata) {
      this.sottoScriviPrivacy();
    }
  }

  // costruzione della documentazione per l'informativa privacy da protocollare
  private getDocumentazioneProtocollaInformativaPrivacy(): DocumentazionePrivacy {
    const oggetto: string =
      `A4G - INFORMATIVA GENERALE PRIVACY - ${this.datiAnagraficiUtente.nome} ${this.datiAnagraficiUtente.cognome} - ${this.datiAnagraficiUtente.codiceFiscale}`;
    const description: string =
      `${this.datiAnagraficiUtente.nome} ${this.datiAnagraficiUtente.cognome} - ${this.datiAnagraficiUtente.codiceFiscale}`;
    const tipologiaDocumento: string = 'PRIVACY';
    const nomeFile = 'InformativaGeneralePrivacy.pdf';
    const email = !this.datiAnagraficiUtente.email ? null : this.datiAnagraficiUtente.email;
    const mittente: Mittente = new Mittente();
    mittente.name = this.datiAnagraficiUtente.nome;
    mittente.surname = this.datiAnagraficiUtente.cognome;
    mittente.email = email;
    mittente.nationalIdentificationNumber = this.datiAnagraficiUtente.codiceFiscale;
    mittente.description = description;
    const documentazionePrivacy: DocumentazionePrivacy = new DocumentazionePrivacy();
    documentazionePrivacy.mittente = mittente;
    documentazionePrivacy.oggetto = oggetto;
    documentazionePrivacy.tipologiaDocumentoPrincipale = tipologiaDocumento;
    return documentazionePrivacy;
  }

  private getInfoPrivacyFirmataXML(): File {
    //Nella maggior parte dei browser la funzione atob(utilizzata in questo caso per trasformare la firma XML in BLOB)
    //causa un'eccezione Character Out Of Range dovuta a dei caratteri che superano l'intervallo di un byte a 8 bit(0x00 ~0xFF).
    //Per ovviare a questo problema chiamato "problema Unicode" e poiché i DOMStrings sono stringhe codificate a 16 bit,
    //viene fatto un replace con una particolare REGEXP sulla stringa che rappresenta l'XML per poi trasformarla in UTF-8 ed infine codificarla.
    let byteCharactersXml = atob(btoa(encodeURIComponent(this.firmaPrivacy.xml).replace(/%([0-9A-F]{2})/g, (match, p1) => {
      return String.fromCharCode(parseInt(p1, 16))
    })));
    let byteNumbersXml = new Array(byteCharactersXml.length);
    for (let i = 0; i < byteCharactersXml.length; i++) {
      byteNumbersXml[i] = byteCharactersXml.charCodeAt(i);
    }
    let byteArrayXml = new Uint8Array(byteNumbersXml);
    let blobXml = new Blob([byteArrayXml], { type: 'application/xml' });
    console.log(blobXml);
    return this.blobToFile(blobXml, 'informativa_privacy_firmata.xml');
  }

  private handleProtocollaInformativaPrivacy(nrProtocolloInformativa: string): Observable<Persona | number> {
    if (this.persona && this.persona.id) {
      this.persona.nrProtocolloPrivacyGenerale = nrProtocolloInformativa;
      return this.registerService.aggiornaPersona(this.persona, this.persona.id)
    } else {
      this.persona = new Persona();
      this.persona.codiceFiscale = this.datiAnagraficiUtente.codiceFiscale;
      this.persona.nome = this.datiAnagraficiUtente.nome;
      this.persona.cognome = this.datiAnagraficiUtente.cognome;
      this.persona.nrProtocolloPrivacyGenerale = nrProtocolloInformativa;
      return this.registerService.inserisciPersona(this.persona);
    }
  }

  private handleInserisciAggiornaPersona(result: any) {
    if (typeof result === "number") {
      let x: Number = result;
      this.persona.id = x.valueOf();
    } else {
      let x: Persona = result;
      this.persona = x;
    }
  }

  private handleErrorInserisciAggiornaPersona(errorMessage: string): Observable<never> {
    console.error(errorMessage);
    this.messageService.add(A4gMessages.getToast(
      'tst',
      A4gSeverityMessage.error,
      'Errore nell\'aggiornamento della persona con CF: ' +
      this.persona.codiceFiscale));
    return EMPTY;
  }

  private handleErrorProtocollaInformativaPrivacy(errorMessage: string): Observable<never> {
    console.error(errorMessage);
    // errore nella protocollazione per l'informativa privacy
    this.messageService.add(A4gMessages.getToast(
      'tst',
      A4gSeverityMessage.error,
      A4gMessages.PROTOCOLLA_INFORMATIVA_ANTIMAFIA_ERROR
    ));
    return EMPTY;
  }

  private sottoScriviPrivacy(): any {
    let obs;
    if (!this.informativaPrivacyFile) {
      obs = this.registerService.downloadInformativaPrivacy();
    } else {
      obs = of(false);
    }
    obs.pipe(
      switchMap(privacyDownloaded => {
        if (privacyDownloaded) {
          FileSaver.saveAs(privacyDownloaded, 'informativa_privacy.pdf');
          this.viewPrivacy = true;
          this.informativaPrivacyFile = this.blobToFile(<Blob>privacyDownloaded, 'informativa_privacy.pdf');
        }
        return this.registerService.sottoScriviPrivacy(this.informativaPrivacyFile);
      }),
      takeUntil(this.componentDestroyed$)
    ).subscribe(
      (response: Firma) => {
        this.firmaPrivacy = response;
        console.log('Firmata privacy con xml', this.firmaPrivacy.xml);
        let byteCharacters = atob(this.firmaPrivacy.pdf);
        let byteNumbers = new Array(byteCharacters.length);
        for (let i = 0; i < byteCharacters.length; i++) {
          byteNumbers[i] = byteCharacters.charCodeAt(i);
        }
        let byteArray = new Uint8Array(byteNumbers);
        let blob = new Blob([byteArray], { type: 'application/pdf' });
        //utilizzato per l'allegato firma pdf
        this.informativaPrivacyFirmataPDF = this.blobToFile(blob, 'informativa_privacy_firmata.pdf');
        FileSaver.saveAs(blob, 'informativa_privacy_firmata.pdf');
        this.isPrivacyFirmata = true;
        this.privacyFirmataOutput.emit(true);
      }, error => {
        this.isPrivacyFirmata = false;
        console.error('Errore in aggiornaDomanda: ' + error);
        A4gMessages.handleError(this.messageService, error, RegisterComponent.ERRORE_GESTIONE_DOMANDA_REGISTRAZIONE_UTENTE);
      }
    );
  }

  private callProtocollaInformativaPrivacy() {
    let info = this.getDocumentazioneProtocollaInformativaPrivacy();
    let allegati = [
      this.informativaPrivacyFirmataPDF,
      this.getInfoPrivacyFirmataXML()];
    return this.registerService.protocollaInformativaPrivacy(
      this.datiAnagraficiUtente.nome, this.datiAnagraficiUtente.cognome,
      this.datiAnagraficiUtente.codiceFiscale, info, this.informativaPrivacyFile, allegati);
  }

  public sottoscriviDomandaToObservable(datiDomanda: DatiDomanda): Observable<any> {
    return of(this.sottoscriviDomanda(datiDomanda));
  }

  private setTipoDomandaRegistrazione() {
    if (this.ctx === TIPO_CREAZIONE_UTENTE_FASCICOLO.CREA_UTENTE) {
      return TipoDomandaRegistrazione.RIDOTTA_AZIENDA;
    }
    return TipoDomandaRegistrazione.RIDOTTA_AZIENDA_ANAGRAFICO;
  }

  public sottoscriviDomanda(datiDomanda: DatiDomanda): any {
    datiDomanda.tipoDomandaRegistrazione = this.setTipoDomandaRegistrazione();
    if (!this.informativaPrivacyFile && !this.isPrivacyFirmata) {
      return;
    }
    let protocollaPrivacyOB: Observable<string> = undefined;
    if (this.isPrivacyProtocollata) {
      protocollaPrivacyOB = EMPTY;
    } else {
      protocollaPrivacyOB = this.callProtocollaInformativaPrivacy()
    }
    // privacy non protocollata, protocollarla e subscribe su risposta in cui si protocolla la richiesta
    return protocollaPrivacyOB.pipe(
      defaultIfEmpty(null),
      switchMap(nrProtocolloInformativa => {
        if (nrProtocolloInformativa) {
          return this.handleProtocollaInformativaPrivacy(nrProtocolloInformativa);
        } else {
          return EMPTY;
        }
      }),
      catchError(error => {
        return this.handleErrorProtocollaInformativaPrivacy(error.error.message);
      }),
      defaultIfEmpty(null),
      switchMap(result => {
        if (result) {
          this.handleInserisciAggiornaPersona(result);
        }
        return this.registerService.registrazioneFirmaDocumento(datiDomanda);
      }),
      catchError(error => {
        return this.handleErrorInserisciAggiornaPersona(error.message);
      }),
      defaultIfEmpty(null),
      takeUntil(this.componentDestroyed$)
    ).subscribe(resp => {
      if (resp) {
        const domandaRegistrazioneCreataResponse: DomandaRegistrazioneCreataResponse = resp;
        const blob = Base64Support.decodeToBlob(
          domandaRegistrazioneCreataResponse.base64Content, "application/pdf");
        const fileName: string = "ModuloRichiestaAccesso_" + this.utente.codiceFiscale + ".pdf";
        FileSaver.saveAs(blob, fileName);
        this.lastIdDomanda = domandaRegistrazioneCreataResponse.id;
        this.isRegistrabile = false;
        console.log("Completata registrazione per domanda: " + this.lastIdDomanda);
        this.messageService.add(A4gMessages.getToast(
          'msgOK', A4gSeverityMessage.success, A4gMessages.RICHIESTA_REGISTRAZIONE_OK));
      } else {
        this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.ERRORE_NON_PREVISTO));
      }
    });
  }

  onSubmit(f: NgForm) {
    this.messageService.clear();
    // controlla anagrafica + esistenza check non sovrebbe mai entrare per disabled
    if (f.invalid) {
      this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.ERRORE_DATI));
      return;
    }
    const datiDomanda: DatiDomanda = new DatiDomanda();
    datiDomanda.datiAnagrafici = this.datiAnagraficiUtente;
    datiDomanda.servizi = this.popolaApplicativi();
    datiDomanda.responsabilitaRichieste = this.popolaResponsabilita();

    if ((datiDomanda.responsabilitaRichieste.responsabilitaCaa && (datiDomanda.responsabilitaRichieste.responsabilitaCaa[0].sedi.length == 0
      || !datiDomanda.responsabilitaRichieste.responsabilitaCaa[0].responsabile))
      || (datiDomanda.responsabilitaRichieste.responsabilitaPat && (!datiDomanda.responsabilitaRichieste.responsabilitaPat[0].matricola
        || !datiDomanda.responsabilitaRichieste.responsabilitaPat[0].dirigente
        || !datiDomanda.responsabilitaRichieste.responsabilitaPat[0].dipartimento
        || !datiDomanda.responsabilitaRichieste.responsabilitaPat[0].note))
      || datiDomanda.responsabilitaRichieste.responsabilitaAltriEnti && (!datiDomanda.responsabilitaRichieste.responsabilitaAltriEnti[0].denominazione
        || !datiDomanda.responsabilitaRichieste.responsabilitaAltriEnti[0].piva
        || !datiDomanda.responsabilitaRichieste.responsabilitaAltriEnti[0].dirigente
        || !datiDomanda.responsabilitaRichieste.responsabilitaAltriEnti[0].note)
      || datiDomanda.responsabilitaRichieste.responsabilitaConsulente && (!datiDomanda.responsabilitaRichieste.responsabilitaConsulente[0].ordine
        || !datiDomanda.responsabilitaRichieste.responsabilitaConsulente[0].iscrizione
        || !datiDomanda.responsabilitaRichieste.responsabilitaConsulente[0].cuaa
        || !datiDomanda.responsabilitaRichieste.responsabilitaConsulente[0].denominazione)) {
      this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.ERRORE_RESPONSABILITA));
    } else {
      console.log(JSON.stringify(datiDomanda));
      this.sottoscriviDomanda(datiDomanda);
    }
  }

  public isCtxCreaUtente() {
    return this.ctx === TIPO_CREAZIONE_UTENTE_FASCICOLO.CREA_UTENTE;
  }

  public onFormChange(event) {
    this.datiAnagraficiUtenteOutput.emit(this.datiAnagraficiUtente);
  }

  ngOnDestroy() {
    this.componentDestroyed$.next(true);
    this.componentDestroyed$.complete();
  }
}
