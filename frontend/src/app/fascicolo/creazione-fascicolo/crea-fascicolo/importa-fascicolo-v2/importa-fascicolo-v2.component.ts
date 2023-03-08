import { FascicoloService } from 'src/app/fascicolo/fascicolo.service';
import { MediatorService } from 'src/app/fascicolo/mediator.service';
import { FormControl, FormGroup } from '@angular/forms';
import { formatDate } from '@angular/common';
import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import * as FileSaver from 'file-saver';
import { MessageService, SelectItem, ConfirmationService } from 'primeng/api';
import { EMPTY, forkJoin, Observable, Subject } from 'rxjs';
import { catchError, switchMap, takeUntil, tap } from 'rxjs/operators';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';
import { Persona } from 'src/app/a4g-common/classi/Persona';
import { UploadHelper } from 'src/app/a4g-common/uploadFile/uploadHelper';
import { Configuration } from 'src/app/app.constants';
import { Labels } from 'src/app/app.labels';
import { UtentiService } from 'src/app/utenti/utenti.service';
import { AnagraficaFascicoloService } from '../../anagrafica-fascicolo.service';
import { DatiCartaServizi, TipoFileEnum } from '../../dto/datiCartaServizi';
import { DatiCAA, SportelloCAA } from '../../dto/DatiSportelloCAA';
import { AllegatoMandato, MandatoDto } from '../../dto/MandatoDto';
import { CreazioneFascicoloService } from '../../shared/crea-fascicolo.service';
import { DatiAperturaFascicoloDto } from '../../dto/DatiAperturaFascicoloDto';
import * as _ from 'lodash';
import * as moment from 'moment';
import { MandatoService } from 'src/app/fascicolo/mandato.service';
import { Fascicolo } from 'src/app/a4g-common/classi/Fascicolo';
import { InputFascicolo } from 'src/app/a4g-common/classi/InputFascicolo';
import { ErrorService } from 'src/app/a4g-common/services/error.service';

export enum IMPORTA_FASCICOLO_STATO {
  REVOCA = 'REVOCA',
  APRI = 'APRI',
  APRI_TRASFERISCI = 'APRI_E_TRASFERISCI',
  TRASFERISCI = 'TRASFERISCI',
  RIAPRI = 'RIAPRI',
  MIGRA = 'MIGRA',
  ACQUISIZIONE_MANDATO = 'ACQUISIZIONE_MANDATO'
}

@Component({
  selector: 'app-importa-fascicolo-v2',
  templateUrl: './importa-fascicolo-v2.component.html',
  styleUrls: ['./importa-fascicolo-v2.component.css']
})
export class ImportaFascicoloV2Component implements OnInit, OnDestroy {
  public dati: any;
  public datiMandato: any;
  public title: string;
  public anagraficaTributaria: DatiAperturaFascicoloDto;
  public indirizzoAzienda: string;
  public indirizzoLR: string;
  public verificaDownloadMandato = false;
  public verificaUploadMandato = false;
  public verificaUploadCartaServizi = false;
  public verificaPresenzaPrivacy = false;
  public verificaDownloadPrivacy = false;
  public verificaUploadPrivacy = false;
  public verificaRepertoriazione = false;
  public datiCAA: DatiCAA;
  public sportelliCAA: SportelloCAA[] = [];
  public selectedSportello: SportelloCAA;
  public selectedSocietaServizi: string;
  public societaServizi: SelectItem[];
  public fileExt = '.p7m, .pdf';
  public dataSottoscrizione; // Firma a bordo del pdf/a
  public dataInizioDecorrenza; // (data sottoscrizione = data decorrenza)
  public dataScadenza: string | number;
  public tipoDaCaricare: TipoFileEnum = TipoFileEnum.CARTA_SERVIZI;
  public cartaServiziFile: File;
  public allegatoFile_1: File;
  public allegatoFile_2: File;
  public allegatoFile_3: File;
  public fileDaPopup: File;
  public descrizioneAllegato_1 = '';
  public descrizioneAllegato_2 = '';
  public descrizioneAllegato_3 = '';
  public popupVisibile = false;
  public popupRichiestaTrasferimentoOpen = false;
  public schedeForm: FormGroup;
  public fascicolo: Fascicolo;
  public popupRunMigrazioneOpen = false;

  protected maxSize = 2;
  protected cuaa: string;
  protected componentDestroyed$: Subject<boolean> = new Subject();
  protected mandatoFile: File;
  protected privacyFile: File;
  protected nomeFileCaricato = '';
  protected tipoFileCaricato: TipoFileEnum;
  protected persona: Persona;

  ctx: IMPORTA_FASCICOLO_STATO.REVOCA | IMPORTA_FASCICOLO_STATO.APRI | IMPORTA_FASCICOLO_STATO.APRI_TRASFERISCI
      | IMPORTA_FASCICOLO_STATO.TRASFERISCI | IMPORTA_FASCICOLO_STATO.RIAPRI | IMPORTA_FASCICOLO_STATO.MIGRA
      | IMPORTA_FASCICOLO_STATO.ACQUISIZIONE_MANDATO; /** variabile di contesto, modalità REVOCA, APRI o TRASFERISCI o COSTITUISCI E TRASFERISCI*/
  IMPORTA_ENUM = IMPORTA_FASCICOLO_STATO;
  isRevocaInviata: boolean;

  @ViewChild('mandatoFile') file;
  @ViewChild('cartaServiziFile') fileCC;

  constructor(
    protected route: ActivatedRoute,
    protected anagraficaFascicoloService: AnagraficaFascicoloService,
    protected messageService: MessageService,
    protected router: Router,
    protected creazioneFascicoloService: CreazioneFascicoloService,
    protected translateService: TranslateService,
    protected _configuration: Configuration,
    protected utentiService: UtentiService,
    private confirmationService: ConfirmationService,
    private mandatoService: MandatoService,
    private mediatorService: MediatorService,
    private fascicoloService: FascicoloService,
    private errorService : ErrorService
  ) { }

  ngOnInit() {
    this.anagraficaTributaria = this.creazioneFascicoloService.anagraficaTributaria;
    this.route.params.pipe(
      takeUntil(this.componentDestroyed$),
      switchMap(urlParams => {
        this.cuaa = urlParams['cuaa'];
        if (this.router.url.indexOf('/revocaOrdinaria') > - 1) {
          this.ctx = IMPORTA_FASCICOLO_STATO.REVOCA;
        } else if (this.router.url.indexOf('/costituisci-trasferisci') > - 1) {
          this.ctx = IMPORTA_FASCICOLO_STATO.APRI_TRASFERISCI;
        } else if (this.router.url.indexOf('/trasferisci') > - 1) {
          this.ctx = IMPORTA_FASCICOLO_STATO.TRASFERISCI;
        } else if (this.router.url.indexOf('/riapri') > - 1) {
          this.ctx = IMPORTA_FASCICOLO_STATO.RIAPRI;
        } else if (this.router.url.indexOf('/migra') > - 1) {
          this.ctx = IMPORTA_FASCICOLO_STATO.MIGRA;
        } else if (this.router.url.indexOf('/acquisizione-mandato') > - 1) {
          this.ctx = IMPORTA_FASCICOLO_STATO.ACQUISIZIONE_MANDATO;
        } else { //APERTURA FASCICOLO
          this.ctx = IMPORTA_FASCICOLO_STATO.APRI;
        }
        switch (this.ctx) {
          case IMPORTA_FASCICOLO_STATO.REVOCA:
            this.title = Labels.revocaMandato.toUpperCase();
            break;
          case IMPORTA_FASCICOLO_STATO.APRI_TRASFERISCI:
            this.title = Labels.costituisciTrasferisciFascicolo.toUpperCase();
            break;
          case IMPORTA_FASCICOLO_STATO.TRASFERISCI:
            this.title = Labels.trasferisciFascicolo.toUpperCase();
            break;
          case IMPORTA_FASCICOLO_STATO.RIAPRI:
            this.title = Labels.riapriFascicolo.toUpperCase();
            break;
          case IMPORTA_FASCICOLO_STATO.ACQUISIZIONE_MANDATO:
            this.title = Labels.acquisizioneMandato.toUpperCase();
            break;
          case IMPORTA_FASCICOLO_STATO.MIGRA:
            this.title = Labels.migraFascicolo.toUpperCase();
            this.schedeForm = new FormGroup({
              modPagamento: new FormControl({ value: true }),
              fabbricati: new FormControl({ value: true }),
              macchinari: new FormControl({ value: true }),
            });
            break;
          default:
            this.title = Labels.creazioneNuovoFascicolo.toUpperCase();
        }
        return this.callServicesByCtx();
      })
    ).subscribe();
  }

  private callServicesByCtx(): Observable<object> {
    if (this.ctx === IMPORTA_FASCICOLO_STATO.REVOCA) {
      this.mandatoService.verificaMandato(this.cuaa).pipe(
        switchMap(response => {
          // copy properties
          this.anagraficaTributaria = { ...response };
          this.callServices(this.cuaa);
          this.dataSottoscrizione = null;
          this.dataInizioDecorrenza = formatDate('01/01/' + ((new Date()).getFullYear() + 1), 'dd/MM/yyyy', 'it-IT');
          const nextYear: string = moment().add(1, 'years').format('YYYY');
          const date = '31/12' + nextYear;
          this.dataScadenza = moment(date, 'DD/MM/YYYY').format('DD/MM/YYYY');
          return this.callServices(this.cuaa);
        }),
        takeUntil(this.componentDestroyed$)
      ).subscribe();
    } else if (this.ctx === IMPORTA_FASCICOLO_STATO.ACQUISIZIONE_MANDATO) {
      const codiceFiscalePersonaGiuridica = this.anagraficaTributaria.codiceFiscale != null ? this.anagraficaTributaria.codiceFiscale : this.anagraficaTributaria.datiAnagraficiRappresentante.codiceFiscale;
      this.dataSottoscrizione = new Date();
      this.dataInizioDecorrenza = formatDate('01/01/' + ((new Date()).getFullYear() + 1), 'dd/MM/yyyy', 'it-IT');
      this.dataScadenza = new Date().setFullYear(new Date().getFullYear(), 11, 31); // 31 dicembre dell'anno corrente
      return this.callServices(this.cuaa);
    } else { // APRI FASCICOLO o 'COSTITUISCI E TRASFERISCI FASCICOLO' o 'RIAPRI' o 'MIGRA'
      this.anagraficaTributaria = this.creazioneFascicoloService.anagraficaTributaria; // TODO:test
      // this.anagraficaTributaria.codiceFiscale risulta essere null nel solo caso di persona fisica senza impresa individuale; 
      // in questo caso si deve reperire il codice fiscale da this.anagraficaTributaria.datiAnagraficiRappresentante.codiceFiscale
      const codiceFiscalePersonaGiuridica = this.anagraficaTributaria.codiceFiscale != null ? this.anagraficaTributaria.codiceFiscale : this.anagraficaTributaria.datiAnagraficiRappresentante.codiceFiscale;
      if (codiceFiscalePersonaGiuridica !== this.cuaa) {
        this.router.navigate(['../' + codiceFiscalePersonaGiuridica], { relativeTo: this.route });
      }
      this.dataSottoscrizione = new Date();
      this.dataInizioDecorrenza = formatDate('01/01/' + ((new Date()).getFullYear() + 1), 'dd/MM/yyyy', 'it-IT');
      this.dataScadenza = new Date().setFullYear(new Date().getFullYear(), 11, 31); // 31 dicembre dell'anno corrente
      return this.callServices(this.cuaa);
    }
  }

  public callServices(cuaa: string): Observable<object> {
    if (!this.anagraficaTributaria) {
      this.router.navigate(['./'], { relativeTo: this.route.parent });
      return EMPTY;
    } else {
      this.setDatiIntestazione();
      this.setIndirizzoAzienda();
      this.setIndirizzoLegaleRappresentante();
      return forkJoin([
        this.anagraficaFascicoloService.getCodiciEnti().pipe(
          catchError(e => {
            this.messageService.add(A4gMessages.getToast(
              'tst', A4gSeverityMessage.error, A4gMessages.recuperoCodiciEnteDaUtenteKo
            ));
            return EMPTY;
          })),
        this.anagraficaFascicoloService.verificaPresenzaPrivacy(
          { codiceFiscale: this.anagraficaTributaria.datiAnagraficiRappresentante.codiceFiscale }).pipe(
            catchError(e => {
              this.messageService.add(A4gMessages.getToast(
                'tst', A4gSeverityMessage.error, A4gMessages.verificaPrivacyKO
              ));
              return EMPTY;
            })),
        this.anagraficaFascicoloService.getDatiSportelloCAA().pipe(
          catchError(e => {
            this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, this.translateService.get));
            this.router.navigate(['./'], { relativeTo: this.route.parent });
            return EMPTY;
          }))
      ]).pipe(
        takeUntil(this.componentDestroyed$),
        tap(resp => {
          this.datiCAA = <DatiCAA>resp[2];
          this.sportelliCAA = (<DatiCAA>resp[2]).sportelli;
          if (this.sportelliCAA.length === 1) {
            this.selectedSportello = this.sportelliCAA[0]
          }
          this.setPresenzaPrivacy(<Persona[]>resp[1]);
        })
      );
    }
  }

  private setPresenzaPrivacy(datiPersona: Persona[]) {
    if (!datiPersona) {
      return;
    }
    if (datiPersona[0].nrProtocolloPrivacyGenerale) {
      this.verificaPresenzaPrivacy = true;
    }
    this.persona = datiPersona[0];
  }

  private setDatiIntestazione() {
    if (this.ctx === IMPORTA_FASCICOLO_STATO.MIGRA) {
      const inputSearch = new InputFascicolo();
      inputSearch.cuaa = this.cuaa;
      this.fascicoloService.ricercaFascicoli(inputSearch)
        .subscribe(res => {
          this.fascicolo = res[0];
          this.dati = [
            { chiave: Labels.cuaa, valore: this.cuaa },
            { chiave: Labels.denominazioneFascicolo, valore: this.anagraficaTributaria.denominazioneFascicolo },
            { chiave: Labels.sportello, valore: this.fascicolo.caa }
          ];
        }, err => {
          this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, err.error.message));
        });
    } else {
      this.dati = [
        { chiave: Labels.cuaa, valore: this.cuaa },
        { chiave: Labels.denominazioneFascicolo, valore: this.anagraficaTributaria.denominazioneFascicolo }
      ];
    }
  }

  private setIndirizzoAzienda() {
    if (this.anagraficaTributaria.ubicazioneDitta && this.anagraficaTributaria.ubicazioneDitta.toponimo) {
      this.indirizzoAzienda = this.anagraficaTributaria.ubicazioneDitta.toponimo;
    }
  }

  private setIndirizzoLegaleRappresentante() {
    if (this.anagraficaTributaria.domicilioFiscaleRappresentante
      && this.anagraficaTributaria.domicilioFiscaleRappresentante.toponimo) {
      this.indirizzoLR = this.anagraficaTributaria.domicilioFiscaleRappresentante.toponimo;
    }
  }

  private getDatiAzienda(): any {
    let sedeLegale = false;
    if (!this.anagraficaTributaria.ubicazioneDitta) {
      return null;
    }

    if (this.anagraficaTributaria.ubicazioneDitta
      || this.anagraficaTributaria.ubicazioneDitta.provincia
      || this.anagraficaTributaria.ubicazioneDitta.comune
      || this.anagraficaTributaria.ubicazioneDitta.localita
      || this.indirizzoAzienda
      || this.anagraficaTributaria.ubicazioneDitta.cap) {
      sedeLegale = true;
    }

    const azienda = {
      codiceFiscale: this.anagraficaTributaria.codiceFiscale,
      partitaIva: this.anagraficaTributaria.partitaIva,
      denominazione: this.anagraficaTributaria.denominazione,
      formaGiuridica: this.anagraficaTributaria.naturaGiuridica,
      sedeLegale: sedeLegale,
      provincia: this.anagraficaTributaria.ubicazioneDitta.provincia,
      comune: this.anagraficaTributaria.ubicazioneDitta.comune,
      frazione: this.anagraficaTributaria.ubicazioneDitta.localita,
      indirizzo: this.indirizzoAzienda,
      cap: this.anagraficaTributaria.ubicazioneDitta.cap
    };
    return azienda;
  }

  private getDatiLegaleRappresentante(): any {
    const soggettoConCarica = this.anagraficaTributaria.datiAnagraficiRappresentante;
    const domiciolioSoggetto = this.anagraficaTributaria.domicilioFiscaleRappresentante;
    let titolare: any;
    if (soggettoConCarica && domiciolioSoggetto) {
      titolare = {
        nominativo: soggettoConCarica.nominativo,
        codiceFiscale: soggettoConCarica.codiceFiscale,
        provinciaNascita: soggettoConCarica.provinciaNascita,
        comuneNascita: soggettoConCarica.comuneNascita,
        dataNascita: soggettoConCarica.dataNascita ? formatDate(soggettoConCarica.dataNascita, 'dd/MM/yyyy', 'it-IT') : null,
        residenza: {
          provincia: domiciolioSoggetto.provincia,
          comune: domiciolioSoggetto.comune,
          frazione: domiciolioSoggetto.localita,
          indirizzo: this.indirizzoLR,
          cap: domiciolioSoggetto.cap
        }
      };
    }
    if (soggettoConCarica && !domiciolioSoggetto) {
      titolare = {
        nominativo: soggettoConCarica.nominativo,
        codiceFiscale: soggettoConCarica.codiceFiscale,
        provinciaNascita: soggettoConCarica.provinciaNascita,
        comuneNascita: soggettoConCarica.comuneNascita,
        dataNascita: soggettoConCarica.dataNascita ? formatDate(soggettoConCarica.dataNascita, 'dd/MM/yyyy', 'it-IT') : null,
        residenza: null
      };
    }
    return titolare;
  }

  private getDatiCAA(): any {
    // const sedeCaa = this.datiCAA[0];
    if (this.datiCAA) {
      const caa = {
        denominazione: this.datiCAA.denominazione,
        codiceFiscale: this.datiCAA.codiceFiscale,
        partitaIva: this.datiCAA.partitaIVA,
        sedeAmministrativa: this.datiCAA.indirizzo ? this.datiCAA.indirizzo.via + ' - ' + this.datiCAA.indirizzo.denominazioneComune : null,
        societaServizi: this.datiCAA.societaServizi,
        atto: this.datiCAA.estremiAtto
      };
      return caa;
    }
  }

  private preparaStampaMandato() {
    this.datiMandato = {
      azienda: this.getDatiAzienda(),
      titolare: this.getDatiLegaleRappresentante(),
      caa: this.getDatiCAA(),
      dataSottoscrizione: formatDate(this.dataSottoscrizione, 'dd/MM/yyyy', 'it-IT'),
      dataDecorrenza: formatDate(this.dataInizioDecorrenza, 'dd/MM/yyyy', 'it-IT')
    };
  }

  private effettuaStampaMandato() {
    const uploadHelper = new UploadHelper(this.fileExt, this.maxSize);
    this.creazioneFascicoloService.getTemplateMandato()
      .subscribe(response => {
        const template: File = uploadHelper.blobToFile(response, 'templateMandato.docx');
        return this.creazioneFascicoloService.stampaMandato(template, this.datiMandato)
          .subscribe(
            result => {
              FileSaver.saveAs(result, 'MandatoDiAssistenza_' + this.datiMandato.titolare.codiceFiscale + '.pdf');
              this.verificaDownloadMandato = true;
            }, err => {
              this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.ERRORE_STAMPA_MANDATO));
              console.error('Errore nella stampa del mandato:', err);
            }
          );
      });
  }

  public downloadMandato() {
    this.preparaStampaMandato();
    this.effettuaStampaMandato();
  }

  public visualizzaMandato() {
    const fileURL = URL.createObjectURL(this.mandatoFile);
    window.open(fileURL);
  }

  public uploadFile(tipoFile: string) {
    this.openWindowsSelectFile(tipoFile);
  }

  private openWindowsSelectFile(tipoFile) {
    document.getElementById(tipoFile).click();
  }

  private showWarnCheckSocietaServizi() {
    this.messageService.add(A4gMessages.getToast('warn-check-societaServizi', A4gSeverityMessage.warn, A4gMessages.CHECK_SOCIETA_SERVIZI));
  }

  onRejectHasCheckSocietaServizi() {
    this.messageService.clear('warn-check-societaServizi');
  }

  onConfirmHasCheckSocietaServizi() {
    this.messageService.clear('warn-check-societaServizi');
    this.preparaStampaMandato();
    this.effettuaStampaMandato();
  }

  public onFileChange(event, tipoFile) {
    if (event.target.files && event.target.files.length > 0) {
      const file: File = event.target.files[0];
      const uploadHelper = new UploadHelper(this.fileExt, this.maxSize);
      if (uploadHelper.isValidFileExtension(file)) {
        if (uploadHelper.isValidFileSize(file)) {
          this.verificaFirmaSingola(file, tipoFile);
        } else {
          this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.UPLOAD_FILE_SIZE));
        }
      } else {
        this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.UPLOAD_FILE_EXT));
      }
    }
  }

  private verificaFirmaSingola(file: File, tipoFile: string) {
    this.anagraficaFascicoloService.verificaFirmaSingola(file,
      this.anagraficaTributaria.datiAnagraficiRappresentante.codiceFiscale).subscribe(esito => {
        if (esito) {
          if (tipoFile === 'mandatoFile') {
            this.mandatoFile = file;
            this.dataSottoscrizione = esito.dataFirma;
            // Solo per APRI vale questa assegnazione
            if (this.ctx === IMPORTA_FASCICOLO_STATO.APRI
             || this.ctx === IMPORTA_FASCICOLO_STATO.APRI_TRASFERISCI
             || this.ctx === IMPORTA_FASCICOLO_STATO.RIAPRI
             || this.ctx === IMPORTA_FASCICOLO_STATO.MIGRA
             || this.ctx === IMPORTA_FASCICOLO_STATO.ACQUISIZIONE_MANDATO) {
              this.dataInizioDecorrenza = this.dataSottoscrizione;
            }
            
            if(this.mandatoFile != null){ 
              this.verificaUploadMandato = true;
              this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.success, A4gMessages.OPERAZIONE_OK));
            }
           
          } else if (tipoFile === 'privacyFile') {
            this.privacyFile = file; 

            this.protocollaInformativaPrivacy();
            
           
          }
        }
      }, error => {
        this.messageService.add(A4gMessages.getToast(
          'tst', A4gSeverityMessage.error, this.translateService.instant('EXC_VERIFICA_FIRMA.' + error.error.message)
        ));
      });
  }

  // utilizzato stesso sviluppo di Utenti e Antimafia
  private protocollaInformativaPrivacy() {
    const info = this.getDocumentazioneInformativaPrivacy();

    // Ricavo nome e cognome a partire dal nominativo
    const nomeCognome: { nome: string, cognome: string } =
      this.anagraficaFascicoloService.getNomeCognomeFromNominativo(this.anagraficaTributaria.datiAnagraficiRappresentante.nominativo);

    this.utentiService.protocollaInformativaPrivacyDichiarazioneAntimafia(
      nomeCognome.nome,
      nomeCognome.cognome,
      this.anagraficaTributaria.datiAnagraficiRappresentante.codiceFiscale,
      info, this.privacyFile, null).subscribe((x: void) => {
        // Check sull'esistenza della persona, se non esiste viene inserita
        this.messageService.add(A4gMessages.getToast(
          'tst',
          A4gSeverityMessage.success,
          A4gMessages.PROTOCOLLA_INFORMATIVA_PRIVACY_SUCCESS
        ));
        this.verificaUploadPrivacy = true;
      }, error => {
        this.messageService.add(
          A4gMessages.getToast(
            'tst',
            A4gSeverityMessage.error,
            A4gMessages.PROTOCOLLA_INFORMATIVA_ANTIMAFIA_ERROR));
      });
  }

  // costruzione della documentazione per l'informativa privacy da protocollare
  private getDocumentazioneInformativaPrivacy() {
    // Ricavo nome e cognome a partire dal nominativo
    const nomeCognome: { nome: string, cognome: string } =
      this.anagraficaFascicoloService.getNomeCognomeFromNominativo(this.anagraficaTributaria.datiAnagraficiRappresentante.nominativo);

    const oggetto: string =
      this._configuration.ProtocollaPrivacyOggetto +
      ' - ' +
      this.anagraficaTributaria.datiAnagraficiRappresentante.nominativo +
      ' - ' +
      this.anagraficaTributaria.datiAnagraficiRappresentante.codiceFiscale;
    const tipologiaDocumento: string = this._configuration.tipologiaDocumentoProtocolloPrivacy;
    const nomeFile = 'InformativaGeneralePrivacy.pdf';
    const mittente = {
      name: nomeCognome.nome,
      surname: nomeCognome.cognome,
      nationalIdentificationNumber: this.anagraficaTributaria.datiAnagraficiRappresentante.codiceFiscale,
      description:
        this.anagraficaTributaria.datiAnagraficiRappresentante.nominativo +
        ' - ' +
        this.anagraficaTributaria.datiAnagraficiRappresentante.codiceFiscale
    };
    const documentazionePrivacy = {
      mittente: mittente,
      oggetto: oggetto,
      tipologiaDocumentoPrincipale: tipologiaDocumento
    };
    return JSON.stringify(documentazionePrivacy);
  }

  public uploadCartaServizi() {
    this.openPopupCartaServizi();
  }

  public downloadPrivacy() {
    this.anagraficaFascicoloService.getPdfInformativaPrivacy().subscribe(response => {
      FileSaver.saveAs(response, 'informativa_privacy.pdf');
      this.verificaDownloadPrivacy = true;
    }, err => {
      this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.warn, A4gMessages.ERRORE_STAMPA_MANDATO));
    });
  }

  public repertoriazione() {
    let condition;
    condition = this.canRepertoriazione();
    if (this.ctx !== IMPORTA_FASCICOLO_STATO.MIGRA) {
      condition = this.canRepertoriazione() && this.selectedSportello;
    }
    if (condition) {
      const datiRepertoriazione = this.preparaDati();
      if (datiRepertoriazione) {
        this.eseguiRepertoriazione(datiRepertoriazione);
      }
    } else {
      if (!this.selectedSportello) {
        this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.warn, A4gMessages.sportelloNonSelezionato));
      } else {
        this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.warn, A4gMessages.ERRORE_DATI));
      }
    }
  }

  private canRepertoriazione() {
    return this.anagraficaTributaria && this.mandatoFile && (this.privacyFile || this.verificaPresenzaPrivacy);
  }

  private preparaDati() {
    return {
      codiceFiscale: this.cuaa,
      codiceFiscaleRappresentante: this.anagraficaTributaria.datiAnagraficiRappresentante.codiceFiscale,
      contratto: this.mandatoFile,
      identificativoSportello: this.ctx === IMPORTA_FASCICOLO_STATO.MIGRA ? Number(this.fascicolo.caacodice) : this.selectedSportello.identificativo,
      allegati: this.variabiliToList()
    };
  }

  private eseguiRepertoriazione(datiRepertoriazione: MandatoDto) {
    if (this.ctx === IMPORTA_FASCICOLO_STATO.APRI_TRASFERISCI || this.ctx === IMPORTA_FASCICOLO_STATO.TRASFERISCI) {
      this.anagraficaFascicoloService.trasferisci(datiRepertoriazione, this.ctx).subscribe(esito => {
        if (esito) {
          this.verificaRepertoriazione = true;
          this.messageService.add(A4gMessages.getToast('tst-protocolla', A4gSeverityMessage.success, A4gMessages.OPERAZIONE_OK));
          this.popupRichiestaTrasferimentoOpen = true;
        }
      }, error => {
        this.messageService.add(
          A4gMessages.getToast('tst', A4gSeverityMessage.error, this.translateService.instant('EXC_APRI_FASCICOLO.' + JSON.parse(error.error).message)));
      });
    } else if (this.ctx === IMPORTA_FASCICOLO_STATO.MIGRA) {
      const migraModoPagamento: boolean =
        this.schedeForm.controls.modPagamento.value;
      const migraMacchinari: boolean =
        this.schedeForm.controls.macchinari.value;
      const migraFabbricati: boolean =
        this.schedeForm.controls.fabbricati.value;
      this.mediatorService.migraFascicolo(datiRepertoriazione, migraModoPagamento, migraMacchinari, migraFabbricati)
        .subscribe(esito => {
          this.messageService.add(A4gMessages.getToast('tst-protocolla', A4gSeverityMessage.success, A4gMessages.OPERAZIONE_OK));
          this.popupRunMigrazioneOpen = true;
        }, error => {
          this.messageService.add(
            A4gMessages.getToast('tst', A4gSeverityMessage.error, this.translateService.instant('EXC_APRI_FASCICOLO.' + JSON.parse(error.error).message)));
        });
    } else {
      let service;
      if (this.ctx === IMPORTA_FASCICOLO_STATO.RIAPRI) {
        service = this.anagraficaFascicoloService.ricostituisci(datiRepertoriazione);
      } else if (this.ctx === IMPORTA_FASCICOLO_STATO.ACQUISIZIONE_MANDATO) {
        service = this.mandatoService.acquisisciMandato(datiRepertoriazione);
      } else {
        service = this.anagraficaFascicoloService.apri(datiRepertoriazione);
      }
      service.subscribe(esito => {
        if (esito) {
          this.verificaRepertoriazione = true;
          this.onCloseMessageProtocolla();
          this.messageService.add(A4gMessages.getToast('tst-protocolla', A4gSeverityMessage.success, A4gMessages.OPERAZIONE_OK));
        }
      }, error => {
        this.messageService.add(
          A4gMessages.getToast('tst', A4gSeverityMessage.error, this.translateService.instant('EXC_APRI_FASCICOLO.' + JSON.parse(error.error).message)));
      });
    }
  }

  // redirect to dettaglio fascicolo 
  onCloseMessageProtocolla() {
    let path;
    let relativeTo;
    if (this.ctx === IMPORTA_FASCICOLO_STATO.APRI_TRASFERISCI) {
      path = './ricerca-fascicolo-new/cuaa/'.concat(this.cuaa).concat('/dettaglio');
      relativeTo = this.route.parent.parent.parent;
    } if (this.ctx === IMPORTA_FASCICOLO_STATO.RIAPRI) {
      path = './ricerca-fascicolo-new/cuaa/'.concat(this.cuaa).concat('/dettaglio');
      relativeTo = this.route.parent.parent;
    } else if (this.ctx === IMPORTA_FASCICOLO_STATO.ACQUISIZIONE_MANDATO) {
      path = './fascicolo/cuaa/'.concat(this.cuaa).concat('/dettaglio');
      relativeTo = this.route.parent.parent;
    } else {
      path = './cuaa/'.concat(this.cuaa).concat('/dettaglio');
      relativeTo = this.route.parent.parent;
    }
    this.router.navigate([path], { relativeTo: relativeTo });
  }

  private variabiliToList() {
    const allegati = new Array<AllegatoMandato>();
    if (this.cartaServiziFile) {
      allegati.push({ file: this.cartaServiziFile, tipologia: TipoFileEnum[1], descrizione: 'Carta Servizi' });
    }
    if (this.allegatoFile_1) {
      allegati.push({ file: this.allegatoFile_1, tipologia: TipoFileEnum[2], descrizione: this.descrizioneAllegato_1 });
    }
    if (this.allegatoFile_2) {
      allegati.push({ file: this.allegatoFile_2, tipologia: TipoFileEnum[2], descrizione: this.descrizioneAllegato_2 });
    }
    if (this.allegatoFile_3) {
      allegati.push({ file: this.allegatoFile_3, tipologia: TipoFileEnum[2], descrizione: this.descrizioneAllegato_3 });
    }
    return allegati;
  }

  public deleteMandato() {
    this.mandatoFile = null;
    this.verificaUploadMandato = false;
    this.verificaDownloadMandato = false;
  }

  public deleteCartaServizi() {
    this.verificaUploadCartaServizi = false;
    this.cartaServiziFile = null;
  }

  ngOnDestroy() {
    this.componentDestroyed$.next(true);
    this.componentDestroyed$.complete();
  }

  public deleteAllegato(id: number) {
    if (id === 1) {
      this.allegatoFile_1 = null;
      this.descrizioneAllegato_1 = '';
    } else if (id === 2) {
      this.allegatoFile_2 = null;
      this.descrizioneAllegato_2 = '';
    } else if (id === 3) {
      this.allegatoFile_3 = null;
      this.descrizioneAllegato_3 = '';
    }
  }

  openPopupCartaServizi() {
    if (this.cartaServiziFile == null) {
      this.tipoDaCaricare = TipoFileEnum.CARTA_SERVIZI;
    } else {
      this.tipoDaCaricare = TipoFileEnum.ALTRO;
    }
    this.popupVisibile = true;
  }

  closePopupCartaServizi(datiCartaServizi: DatiCartaServizi) {
    this.popupVisibile = datiCartaServizi.popupVisibile;
    this.nomeFileCaricato = datiCartaServizi.fileDaPopup.name;
    this.tipoFileCaricato = datiCartaServizi.tipoFile;
    if (this.tipoFileCaricato === TipoFileEnum.CARTA_SERVIZI) {
      this.cartaServiziFile = datiCartaServizi.fileDaPopup;
      this.verificaUploadCartaServizi = datiCartaServizi.verificaUploadCartaServizi;
    } else if (this.tipoFileCaricato = TipoFileEnum.ALTRO) {
      if (this.allegatoFile_1 == null) {
        this.allegatoFile_1 = datiCartaServizi.fileDaPopup;
        this.descrizioneAllegato_1 = datiCartaServizi.descrizioneAllegatoCartaServiziFile;
      } else if (this.allegatoFile_2 == null) {
        this.allegatoFile_2 = datiCartaServizi.fileDaPopup;
        this.descrizioneAllegato_2 = datiCartaServizi.descrizioneAllegatoCartaServiziFile;
      } else if (this.allegatoFile_3 == null) {
        this.allegatoFile_3 = datiCartaServizi.fileDaPopup;
        this.descrizioneAllegato_3 = datiCartaServizi.descrizioneAllegatoCartaServiziFile;
      }
    }
  }

  // TODO: verificare
  public revoca() {
    // TODO: isRevocaInviata = true dopo aver invocato il servizio di BE
    if (this.anagraficaTributaria && this.mandatoFile && (this.privacyFile || this.verificaPresenzaPrivacy) && this.selectedSportello) {
      const datiRevoca = this.preparaDati();
      if (datiRevoca) {
        this.mandatoService.eseguiRevocaOrdinaria(datiRevoca).subscribe(response => {
          console.log(response);
          this.confirmationService.confirm({
            message: "Il nuovo mandato è stato acquisito correttamente. Potrai visualizzare e modificare il fascicolo a partire dal 1° gennaio dell’anno prossimo",
            header: 'ATTENZIONE',
            icon: 'pi pi-exclamation-triangle',
            accept: () => {
              this.router.navigate(['./gestioneAzienda'], { relativeTo: this.route.parent.parent });
            },
            reject: () => {
              this.router.navigate(['./gestioneAzienda'], { relativeTo: this.route.parent.parent });
            }
          });
        });
      }

    } else {
      if (!this.selectedSportello) {
        this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.warn, A4gMessages.sportelloNonSelezionato));
      } else {
        this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.warn, A4gMessages.ERRORE_DATI));
      }
    }
  }

  closePopupRichiestaTrasferimento(event) {
    this.onCloseMessageProtocolla();
  }

  getReportTrasferimentoOp(event) {
    this.anagraficaFascicoloService.getReportTrasferimentoOp(this.cuaa, this.ctx)
      .subscribe(
        resp => {
          FileSaver.saveAs(resp, 'report_trasferimento_op.docx');
          this.onCloseMessageProtocolla();
        },
        err => {
          this.messageService.add(A4gMessages.getToast(
            'tst', A4gSeverityMessage.error, this.translateService.instant('FAS_ANA.GET_MODULO_TRASFERIMENTO_KO')
          ));
        }
      );
  }

  public isPersonaFisica(): boolean {
    return this.cuaa.length === 16;
  }

  public closePopupRunMigrazione(event) {
    this.router.navigate(['./ricerca-fascicolo-new'], { relativeTo: this.route.parent.parent });
  }

}
