import { Component, OnInit, ViewChild, OnDestroy } from "@angular/core";
import { Configuration } from "../../../../app.constants";
import { AntimafiaService } from "../../antimafia.service";
import { DichiarazioneAntimafiaService } from "../../dichiarazione-antimafia.service";
import * as FileSaver from "file-saver";
import { Labels } from "src/app/app.labels";
import { DichiarazioneAntimafia } from "../../classi/dichiarazioneAntimafia";
import {
  StatoValidazione,
  ResponseValidazione
} from "../../classi/statoValidazione";
import { AntimafiaValidationService } from "../antimafia-validation.service";
import {
  HttpHeaders,
  HttpClient,
  HttpErrorResponse,
  HttpResponse
} from "@angular/common/http";
import { ActivatedRoute, Router } from "@angular/router";
import { Utils } from "../antimafia.utils";
import {
  A4gMessages,
  A4gSeverityMessage
} from "src/app/a4g-common/a4g-messages";
import { MessageService } from "primeng/api";
import { DatePipe } from "@angular/common";
import { SoggettiImpresa, Richiedente } from "../../classi/datiDichiarazione";
import { LoaderService } from "src/app/loader.service";
import { UtentiService } from "src/app/utenti/utenti.service";
import { Persona } from "src/app/a4g-common/classi/Persona";
import { ActionMenuComponent } from "src/app/a4g-common/action-menu.component";
import { TranslateService } from "@ngx-translate/core";
import * as cloneDeep from 'lodash/cloneDeep';
import { concatMap, switchMap } from "rxjs/operators";

@Component({
  selector: "app-verifica",
  templateUrl: "./verifica.component.html",
  styleUrls: ["./verifica.component.css"],
  providers: [DatePipe]
})
export class VerificaComponent implements OnInit, OnDestroy {
  statoValidazione = {
    OK: "pi pi-check",
    ERROR: "ui-icon-error",
    WARNING: "pi pi-info-circle"
  };
  dichiarazioneAntimafia: DichiarazioneAntimafia;
  colDato = {
    procedimenti: Labels.procedimenti,
    richiedente: Labels.anagraficaRichiedente,
    impresa: Labels.anagraficaImpresa,
    soggetti: Labels.soggettiCarica,
    aziende: Labels.aziendeCollegate,
    familiari: Labels.familiari,
    documentoFamiliari: Labels.dichiarazioneFamiliariConviventi,
    verifica: Labels.dichiarazioneAntimafia,
    privacy: Labels.documentoInformativaPrivacy
  };
  colTipologia = {
    procedimenti: "",
    richiedente: Labels.datoCCIA,
    impresa: Labels.datoCCIA,
    soggetti: Labels.datoCCIA,
    aziende: Labels.datoCCIA,
    familiari: Labels.datoCCIA,
    documentoFamiliari: Labels.documentoFirmato,
    verifica: Labels.documentoFirmatoDigitalmente,
    privacy: Labels.documentoFirmatoDigitalmente
  };
  colValidation = {
    procedimenti: this.statoValidazione.OK,
    richiedente: this.statoValidazione.OK,
    impresa: this.statoValidazione.OK,
    soggetti: this.statoValidazione.OK,
    aziende: this.statoValidazione.OK,
    familiari: this.statoValidazione.OK,
    documentoFamiliari: this.statoValidazione.OK,
    verifica: this.statoValidazione.OK,
    privacy: this.statoValidazione.ERROR
  };
  colErrori = {
    procedimenti: "",
    richiedente: "",
    impresa: "",
    soggetti: "",
    aziende: "",
    familiari: "",
    documentoFamiliari: "",
    verifica: "",
    privacy: ""
  };



  urlUploadFirma: string;
  urlUploaProtocolla: string;
  firmaPdf = false;
  protocollaPdf = false;
  dichiarazioneId: number;
  msgErrore: string;
  labels = Labels;
  steps;

  isProtocollaReady = true;
  menuActionAllegatoAntimafia: any[] = new Array();
  menuActionInformativaPrivacy: any[] = new Array();
  @ViewChild("file") file;
  @ViewChild("filePrivacy") filePrivacy;
  hasUploadPrivacy: boolean = false;
  persona: Persona;
  informativaPrivacyDocument: File;
  pdfFirmato: any[];

  constructor(
    private dichiarazioneAntimafiaService: DichiarazioneAntimafiaService,
    private antimafiaService: AntimafiaService,
    private _configuration: Configuration,
    private validator: AntimafiaValidationService,
    private http: HttpClient,
    private route: ActivatedRoute,
    private datePipe: DatePipe,
    private messages: MessageService,
    private loaderService: LoaderService,
    private utentiservice: UtentiService,
    private router: Router,
    private translate: TranslateService
  ) { }

  ngOnInit() {
    //Il servizio di protocollazione richiede più tempo per l'elaborazione(dai 60 ai 120 sec)
    this.loaderService.setTimeout(480000);
    this.buildMenuActionAllegatoAntimafia();
    this.buildMenuActionInformativaPrivacy();
    const idDichiarazione = this.route.snapshot.paramMap.get("idDichiarazione");
    this.antimafiaService
      .getDichiarazioneAntimafiaWithPdfFirmato(idDichiarazione)
      .subscribe(dichiarazione => {
        this.dichiarazioneAntimafia = dichiarazione;
        dichiarazione.procedimenti = this.dichiarazioneAntimafiaService.getDichiarazioneAntimafia().procedimenti;
        if (!A4gMessages.isUndefinedOrNull(this.dichiarazioneAntimafiaService.getDichiarazioneAntimafia().procedimenti)) {
          this.dichiarazioneAntimafiaService.getDichiarazioneAntimafia().procedimenti.forEach(proc => {
            this.translate.get('DICH_AMF.' + proc).subscribe((res: string) => {
              if (!this.colTipologia.procedimenti) {
                this.colTipologia.procedimenti = res;
              } else {
                this.colTipologia.procedimenti = this.colTipologia.procedimenti + ', ' + res;
              }
            });
          });
        }
        this.pdfFirmato = dichiarazione.pdfFirmato;
        this.dichiarazioneAntimafiaService.setDichiarazioneAntimafia(
          dichiarazione
        );
        this.validaPerVerifica(this.dichiarazioneAntimafia);
      });
    //this.dichiarazioneAntimafiaService.getDichiarazioneAntimafia();
    this.urlUploadFirma = this._configuration.UrlUploadFirma;
    this.urlUploaProtocolla = this._configuration.UrlUploadProtocolla;
    this.steps = Object.keys(this.colDato);
  }

  ngOnDestroy(): void {
    this.loaderService.resetTimeout();
  }

  getDocumentazioneInformativaPrivacy(richiedente: Richiedente): string {
    const oggetto: string =
      this._configuration.ProtocollaPrivacyOggetto +
      " - " +
      richiedente.nome +
      " " +
      richiedente.cognome +
      " - " +
      richiedente.codiceFiscale;
    const tipologiaDocumento: string = this._configuration
      .tipologiaDocumentoProtocolloPrivacy;
    const nomeFile = "InformativaGeneralePrivacy.pdf";
    const mittente = {
      name: richiedente.nome,
      surname: richiedente.cognome,
      email: A4gMessages.isUndefinedOrNull(richiedente.indirizzoPEC)
        ? null
        : richiedente.indirizzoPEC,
      nationalIdentificationNumber: richiedente.codiceFiscale,
      description:
        richiedente.nome +
        " " +
        richiedente.cognome +
        " - " +
        richiedente.codiceFiscale
    };
    const documento = {
      content: null,
      mimeType: "application/pdf",
      name: nomeFile
    };
    const documentazionePrivacy = {
      mittente: mittente,
      allegati: null,
      documento: documento,
      oggetto: oggetto,
      tipologiaDocumentoPrincipale: tipologiaDocumento
    };

    return JSON.stringify(documentazionePrivacy);
  }

  // protocolla domanda
  protocollaDomanda() {
    this.messages.add(
      A4gMessages.getToast(
        "warn-start-protocollazione",
        A4gSeverityMessage.warn,
        A4gMessages.WARNING_START_PROTOCOLLAZIONE
      )
    );
  }

  onRejectStartProtocollazione() {
    this.clearWarnStartProtocollazione();
  }

  startProtocollazioneAsincrona() {
    if (this.isProtocollaReady) {
      this.clearWarnStartProtocollazione();
      let richiedente: Richiedente = this.dichiarazioneAntimafia.datiDichiarazione.richiedente;

      if (!A4gMessages.isUndefinedOrNull(this.informativaPrivacyDocument)) {
        this.antimafiaService.newProtocollaDichiarazioneAntimafiaAsync(this.dichiarazioneAntimafia.id.toString()).pipe(
          concatMap((result) => {            
             return this.utentiservice.protocollaInformativaPrivacyDichiarazioneAntimafia(
              richiedente.nome,
              richiedente.cognome,
              richiedente.codiceFiscale,
              this.getDocumentazioneInformativaPrivacy(richiedente),
              this.informativaPrivacyDocument,
              null);
          })
        ).subscribe(resp => {                                  
          this.messages.add(
            A4gMessages.getToast(
              'tst-protocolla',
              A4gSeverityMessage.success,
              this.translate.instant("DICH_AMF.protocollazioneInCarico"))
          );
        }, error => {
          this.messages.add(
            A4gMessages.getToast(
            "tst",
            A4gSeverityMessage.error,
            A4gMessages.PROTOCOLLA_ANTIMAFIA_ERROR));
        });       
      }else{
        this.antimafiaService.newProtocollaDichiarazioneAntimafiaAsync(this.dichiarazioneAntimafia.id.toString())
        .subscribe(
          resp => {
            console.log("RESPONSE: " + resp.status);                         
            this.messages.add(
              A4gMessages.getToast(
                'tst-protocolla',
                A4gSeverityMessage.success,
                this.translate.instant("DICH_AMF.protocollazioneInCarico"))
            );
          }, error => {
            this.messages.add(
              A4gMessages.getToast(
              "tst",
              A4gSeverityMessage.error,
              A4gMessages.PROTOCOLLA_ANTIMAFIA_ERROR));
      }); 
      }
    }
  }

  private checkInfoPrivacy(response: ResponseValidazione) {
    if (response.esito !== StatoValidazione.OK) {
      this.colErrori.privacy = response.messaggio;
      this.colValidation.privacy = this.statoValidazione.ERROR;
      this.isProtocollaReady = false;
    } else {
      this.colErrori.privacy = "";
      this.colValidation.privacy = this.statoValidazione.OK;
      this.verifyIfProtocollaIsReady();
    }
  }

  // VALIDA TUTTI gli step precedenti
  validaPerVerifica(dichiarazione: DichiarazioneAntimafia) {
    let response: ResponseValidazione = { esito: StatoValidazione.OK };
    response = this.validator.validaRichiedente(
      dichiarazione.datiDichiarazione.richiedente
    );
    if (response.esito !== StatoValidazione.OK) {
      this.colErrori.richiedente = response.messaggio;
      this.colValidation.richiedente = this.statoValidazione.ERROR;
      this.isProtocollaReady = false;
    }
    response = this.validator.validaImpresa(
      dichiarazione.datiDichiarazione.dettaglioImpresa
    );
    if (response.esito !== StatoValidazione.OK) {
      this.colErrori.impresa = response.messaggio;
      this.colValidation.impresa = this.statoValidazione.ERROR;
      this.isProtocollaReady = false;
    }
    response = this.validator.validaSoggettiCarica(dichiarazione);
    if (response.esito !== StatoValidazione.OK) {
      this.colErrori.soggetti = response.messaggio;
      this.colValidation.soggetti = this.statoValidazione.ERROR;
      this.isProtocollaReady = false;
    }
    response = this.validator.validaFamiliariConviventiAllegati(
      dichiarazione.datiDichiarazione.dettaglioImpresa.soggettiImpresa
    );
    if (response.esito !== StatoValidazione.OK) {
      this.colErrori.documentoFamiliari = response.messaggio;
      this.colValidation.documentoFamiliari = this.statoValidazione.ERROR;
      this.isProtocollaReady = false;
    }
    response = this.validator.validaFamiliariConviventiCheckDichiarazione(
      dichiarazione.datiDichiarazione.dettaglioImpresa.soggettiImpresa
    );
    if (response.esito !== StatoValidazione.OK) {
      this.colErrori.familiari = response.messaggio;
      this.colValidation.familiari = this.statoValidazione.ERROR;
      this.isProtocollaReady = false;
    }
    response = this.validator.validaAziendeCollegate(
      dichiarazione.datiDichiarazione.dettaglioImpresa.aziendeCollegate
    );
    if (response.esito !== StatoValidazione.OK) {
      this.colErrori.aziende = response.messaggio;
      this.colValidation.aziende = this.statoValidazione.ERROR;
      this.isProtocollaReady = false;
    }
    response = this.validator.validaVerificaDichiarazioneAllegata(
      this.dichiarazioneAntimafia.dtUploadPdfFirmato
    );
    if (response.esito !== StatoValidazione.OK) {
      this.colErrori.verifica = response.messaggio;
      this.colValidation.verifica = this.statoValidazione.ERROR;
      this.isProtocollaReady = false;
    }
    this.utentiservice
      .ricercaPersone({
        codiceFiscale: dichiarazione.datiDichiarazione.richiedente.codiceFiscale
      })
      .subscribe(
        persone => {
          response = this.validator.validaVerificaInformativaPrivacy(
            persone == null ? [] : persone,
            dichiarazione.datiDichiarazione.richiedente.codiceFiscale
          );
          this.checkInfoPrivacy(response);

          if (!A4gMessages.isUndefinedOrNull(persone) && persone.length > 0) {
            this.persona = persone[0];
          }
        },
        error => {
          console.error("Errore in ricercaPersone: " + error);
          this.colErrori.privacy = error.message;
          this.colValidation.privacy = this.statoValidazione.ERROR;
          this.isProtocollaReady = false;
        }
      );
  }

  downloadInformativaPrivacy(): any {
    this.http
      .get(this._configuration.UrlGetInfoPrivacy, {
        responseType: "blob"
      })
      .subscribe(response => {
        FileSaver.saveAs(response, "informativa_privacy.pdf");
      });
  }

  sentenceCase(input, lowercaseBefore) {
    input = (input === undefined || input === null) ? '' : input;
    if (lowercaseBefore) { input = input.toLowerCase(); }
    return input.toString().replace(/(^|\. *)([a-z])/g, function (match, separator, char) {
      // return separator + char.toUpperCase();
      return match.toUpperCase();
    });
  }

  //metodo usato per evitare errori nella stampa semplicemente facendo un uppercase dell'oggetto sociale
  fixOggettoSociale(input: string) {
    input = (input === undefined || input === null) ? '' : input;
    return input.toUpperCase();
  }

  downloadCertificazioneAntimafia(): any {
    let isDittaIndividuale = Utils.isImpresaDittaIndividuale(
      this.dichiarazioneAntimafia.datiDichiarazione.dettaglioImpresa
        .formaGiuridicaCodice
    );

    let docTemplate = isDittaIndividuale
      ? this._configuration.UrlGetDichiarazioneAntimafiaDI
      : this._configuration.UrlGetDichiarazioneAntimafiaPG;

    this.http
      .get(docTemplate, {
        responseType: "blob"
      })
      .subscribe(response => {
        const url = this._configuration.UrlStampaPDF;
        const template: File = this.blobToFile(response, "all3famconv.docx");

        const soggettiConCarica = isDittaIndividuale
          ? this.getCaricheImportanti()
          : this.getSoggettiSelezionati();
        // Utilizzo della cloneDeep di lodash per avere una Deep Copy dell'oggetto
        const dichAnt = cloneDeep(this.dichiarazioneAntimafia);
        const ogSociale = this.dichiarazioneAntimafia.datiDichiarazione.dettaglioImpresa
          .oggettoSociale;
        this.dichiarazioneAntimafia.datiDichiarazione.dettaglioImpresa
          .oggettoSociale = this.fixOggettoSociale(this.dichiarazioneAntimafia.datiDichiarazione.dettaglioImpresa.oggettoSociale);
        const aziendeFiltered = 
          this.dichiarazioneAntimafia.datiDichiarazione.dettaglioImpresa.aziendeCollegate 
           ? this.dichiarazioneAntimafia.datiDichiarazione.dettaglioImpresa.aziendeCollegate.filter(azienda => azienda.selezionato === true)
           : [];
        dichAnt.datiDichiarazione.dettaglioImpresa.aziendeCollegate = aziendeFiltered;
        const dati = {
          dichiarazioneAntimafia: dichAnt,
          soggettiConCarica: soggettiConCarica,
          dataOdierna: this.datePipe.transform(Date.now(), "dd/MM/yyyy")
        };

        //TODO - sistemare i template con i tag - capire come fare il parametro dati
        const formData = new FormData();
        formData.append("file", template, template.name);
        formData.append("dati", JSON.stringify(dati));
        formData.append("formatoStampa", "PDF_A");

        return this.http
          .post(url, formData, { responseType: "blob" })
          .subscribe(
            data => {
              this.dichiarazioneAntimafia.datiDichiarazione.dettaglioImpresa
                .oggettoSociale = ogSociale;
              FileSaver.saveAs(
                data,
                "DichiarazioneAntimafia_" +
                this.dichiarazioneAntimafia.id +
                ".pdf"
              );
            },
            (err: HttpErrorResponse) => {
              console.error(
                "Errore nella creazione della dichiarazione antimafia:",
                err
              );
            }
          );
      });
  }

  private blobToFile = (theBlob: Blob, fileName: string): File => {
    var b: any = theBlob;
    //A Blob() is almost a File() - it's just missing the two properties below which we will add
    b.lastModifiedDate = new Date();
    b.name = fileName;
    //Cast to a File() type
    return <File>theBlob;
  };

  getCaricheImportanti(): SoggettiImpresa[] {
    let soggettiImpresa = this.dichiarazioneAntimafia.datiDichiarazione.dettaglioImpresa.soggettiImpresa.map(
      x => Object.assign({}, x)
    );
    const soggImpresa = JSON.parse(JSON.stringify(soggettiImpresa));
    return soggImpresa
      .filter(
        c => (c.carica = c.carica.filter(carica => carica.codice.match("DT")))
      )
      .filter(c => (c.carica ? c.carica.length !== 0 : false));
  }

  getSoggettiSelezionati(): SoggettiImpresa[] {
    // nel caso di più cariche duplico l'elemento per poterlo visualizzare correttamente
    let soggettiImpresa = this.dichiarazioneAntimafia.datiDichiarazione.dettaglioImpresa.soggettiImpresa.map(
      x => Object.assign({}, x)
    );
    const soggImpresa = JSON.parse(JSON.stringify(soggettiImpresa));
    return soggImpresa
      .filter(c => (c.carica = c.carica.filter(carica => carica.selezionato)))
      .filter(c => (c.carica ? c.carica.length !== 0 : false));
  }

  /********* Inizio Action upload pdf dichiarazione antimafia **********/

  /* evento generato all'apeertura del menu AZIONI.
   * per comodità essendoci solo due menu nella pagina chiamo un unico metodo separando le azioni
   * attraverso un parametro booleano
   * TODO si potrebbe implementarfe un metodo diverso
   */
  onDropdownMenuOpen(actionMenu: ActionMenuComponent, menuPrivacy: boolean) {
    if (this.hasAlreadyUpdateFile() && !menuPrivacy) {
      actionMenu.badgeStylezed = false;
      actionMenu.item[0].items[1].disabled = true;
      if (
        this.dichiarazioneAntimafia !== null &&
        this.dichiarazioneAntimafia !== undefined
      ) {
        actionMenu.badgeStylezed =
          this.dichiarazioneAntimafia.dtUploadPdfFirmato !== null &&
          this.dichiarazioneAntimafia.dtUploadPdfFirmato !== undefined;
        actionMenu.item[0].items[1].disabled = false;

        actionMenu.badgeData = { "file": this.dichiarazioneAntimafia.pdfFirmato, "fileName": "DomandaCertificazioneAntimafia_" + this.dichiarazioneAntimafia.id + "." + (this.dichiarazioneAntimafia.tipoPdfFirmato == null ? "pdf" : this.dichiarazioneAntimafia.tipoPdfFirmato) };
      }
    }
    if (menuPrivacy) {
      actionMenu.badgeStylezed = this.hasUploadPrivacy;
      this.readByteFile(this.informativaPrivacyDocument).then(
        (fileBase64: any) => {
          actionMenu.badgeData = { "file": fileBase64, "fileName": "InformativaPrivacyDocument_" + this.informativaPrivacyDocument.name };
        }
      );
    }
  }

  /* Inizio Action upload pdf conviventi familiari */

  onFileChange(event, hasPrivacy: boolean) {
    if (event.target.files && event.target.files.length > 0) {
      let file: File = event.target.files[0];

      //Validazione del file da carica. Il tipo del file deve essere PDF oppur P7M
      //Non deve superare i 15MB di lunghezza?
      const responseValidationType: ResponseValidazione = this.validator.validaAllegatoType(
        file,
        true
      );
      const responseValidationSize: ResponseValidazione = this.validator.validaAllegatoSize(
        file
      );

      if (responseValidationType.esito == StatoValidazione.OK) {
        if (responseValidationSize.esito == StatoValidazione.OK) {
          if (hasPrivacy) {
            this.uploadInfoPrivacy(file);
          } else {
            this.converterByteBase64(file);
          }
        } else {
          this.messages.add(
            A4gMessages.getToast(
              "tst",
              A4gSeverityMessage.error,
              responseValidationSize.messaggio
            )
          );
        }
      } else {
        this.messages.add(
          A4gMessages.getToast(
            "tst",
            A4gSeverityMessage.error,
            responseValidationType.messaggio
          )
        );
      }
    }
  }

  //Funzione richiamata dal Menu-Action
  private uploadFile() {
    if (this.hasAlreadyUpdateFile()) {
      this.showWarnAlreadyUploadedFile();
    } else {
      this.openWindowsSelectFile();
    }
  }

  //Conversione del file in formato base64
  private converterByteBase64(file: File) {
    let pdfFirmatoOld = this.dichiarazioneAntimafia.pdfFirmato;
    this.readByteFile(file).then(
      (fileBase64: any) => {
        this.dichiarazioneAntimafia.pdfFirmato = fileBase64;
        this.dichiarazioneAntimafia.tipoPdfFirmato = file.name.endsWith(A4gMessages.UPLOAD_FILE_TYPE_PDFP7M) ? "pdf.p7m" :
          file.name.endsWith(A4gMessages.UPLOAD_FILE_TYPE_PDF) ? "pdf" : null;
        this.dichiarazioneAntimafia.pdfFirmatoName = file.name;
        this.antimafiaService
          .aggiornaDichiarazioneAntimafia(this.dichiarazioneAntimafia)
          .subscribe(
            aggiornaDichiarazioneEsito => {
              if (
                !A4gMessages.isUndefinedOrNull(aggiornaDichiarazioneEsito.esito)
              ) {
                this.messages.add(
                  A4gMessages.getToast(
                    "tst",
                    A4gSeverityMessage.error,
                    aggiornaDichiarazioneEsito.esito.split(":")[1]
                  )
                );
                console.error(
                  "Errore in Aggiornamento Dichiarazione Antimafia: " +
                  aggiornaDichiarazioneEsito.esito.split(":")[1] +
                  "\nErrorCode:" +
                  aggiornaDichiarazioneEsito.esito.split(":")[0]
                );
              } else {
                this.messages.add(
                  A4gMessages.getToast(
                    "tst",
                    A4gSeverityMessage.success,
                    A4gMessages.OPERAZIONE_OK
                  )
                );
                this.colErrori.verifica = "";
                this.colValidation.verifica = this.statoValidazione.OK;
                this.dichiarazioneAntimafia.dtUploadPdfFirmato =
                  aggiornaDichiarazioneEsito.dichiarazione.dtUploadPdfFirmato;
                this.pdfFirmato = aggiornaDichiarazioneEsito.pdfFirmato;
                this.verifyIfProtocollaIsReady();
              }
            },
            err => {
              console.error(err);
              this.dichiarazioneAntimafia.pdfFirmato = pdfFirmatoOld;
              this.messages.add(
                A4gMessages.getToast("tst", A4gSeverityMessage.error, err.error)
              );
            }
          );
      },

      err => {
        console.error(err);
        this.dichiarazioneAntimafia.pdfFirmato = pdfFirmatoOld;
      }
    );
  }

  //Utility che converte un file in formato base64
  private readByteFile(file: File) {
    return new Promise((resolve, reject) => {
      let reader = new FileReader();
      if (A4gMessages.isUndefinedOrNull(file)) {
        reject("Errore nell' estrapolare i byte dal file");
      }
      let error = "Errore nell' estrapolare i byte dal file " + file.name;
      let value;
      if (A4gMessages.isUndefinedOrNull(file)) {
        reject(error);
      } else {
        reader.readAsDataURL(file);
        reader.onloadend = event => {
          value = reader.result;
          if (value == null) {
            reject(error);
          } else {
            resolve(value.toString().split(",")[1]);
          }
        };
      }
    });
  }

  //Apertura finestra del SO  per la selezione di un file
  private openWindowsSelectFile() {
    this.file.nativeElement.click();
  }

  //Controllo se è stato caricato un file in precedenza per la verifica finale
  private hasAlreadyUpdateFile(): boolean {
    return !A4gMessages.isUndefinedOrNull(
      this.dichiarazioneAntimafia.dtUploadPdfFirmato
    );
  }

  private showWarnAlreadyUploadedFile() {
    this.messages.add(
      A4gMessages.getToast(
        "warn-already-uploadedFile",
        A4gSeverityMessage.warn,
        A4gMessages.FILE_ALREADY_UPLOADED
      )
    );
  }

  onRejectHasAlreadyUploadedFile() {
    this.messages.clear("warn-already-uploadedFile");
  }

  onConfirmHasAlreadyUploadedFile() {
    this.messages.clear("warn-already-uploadedFile");
    if (this.hasUploadPrivacy) {
      this.filePrivacy.nativeElement.click();//this.openInfoPrivacy();
      return;
    }
    this.openWindowsSelectFile();
  }

  private buildMenuActionAllegatoAntimafia() {
    this.menuActionAllegatoAntimafia = [
      {
        label: Labels.azioni.toUpperCase(),
        items: [
          {
            label: Labels.CARICA_MODELLO,
            check: "true",
            badgeStyle: "file-uploaded-ok",
            command: () => {
              this.uploadFile();
            }
          },
          { separator: true },
          {
            label: Labels.SCARICA_MODELLO,
            command: () => {
              this.downloadCertificazioneAntimafia();
            }
          }
        ]
      }
    ];
  }

  /********* Fine Action upload pdf dichiarazione antimafia **********/

  /********* Inizio Action upload pdf informativa privacy **********/

  private buildMenuActionInformativaPrivacy() {
    this.menuActionInformativaPrivacy = [
      {
        label: Labels.azioni.toUpperCase(),
        items: [
          {
            label: Labels.CARICA_MODELLO,
            check: "true",
            badgeStyle: "file-uploaded-ok",
            command: () => {
              this.openInfoPrivacy();
            }
          },
          { separator: true },
          {
            label: Labels.SCARICA_MODELLO,
            command: () => {
              this.downloadInformativaPrivacy();
            }
          }
        ]
      }
    ];
  }

  private openInfoPrivacy() {
    if (this.hasUploadPrivacy) {
      this.showWarnAlreadyUploadedFile();//alert('ciao');
    } else {
      this.filePrivacy.nativeElement.click();
    }
  }

  openWindowSelectFilePrivacy() {
    if (this.hasUploadPrivacy) {
      this.showWarnAlreadyUploadedFile();
    } else {
      this.openInfoPrivacy();
    }
  }

  private uploadInfoPrivacy(file: File) {
    this.antimafiaService.verificaFirma(file).subscribe(esito => {
      if (esito.warningFault.length !== 0) {
        let errors = esito.warningFault[0];
        this.messages.add(
          A4gMessages.getToast("tst", A4gSeverityMessage.error, errors.errorMsg)
        );
        this.checkInfoPrivacy(<ResponseValidazione>{
          esito: StatoValidazione.ERROR,
          messaggio: A4gMessages.INFORMATIVA_PRIVACY_ASSENTE(
            this.dichiarazioneAntimafia.datiDichiarazione.richiedente
              .codiceFiscale
          )
        });
        console.error(
          "Errore in Verifica Dichiarazione Antimafia per info privacy: " +
          errors.errorMsg +
          "\nErrorCode:" +
          errors.errorCode
        );
      } else {
        this.messages.add(
          A4gMessages.getToast(
            "tst",
            A4gSeverityMessage.success,
            A4gMessages.OPERAZIONE_OK
          )
        );
        this.checkInfoPrivacy(<ResponseValidazione>{
          esito: StatoValidazione.OK
        });
        this.hasUploadPrivacy = true;
        this.informativaPrivacyDocument = file;
      }
    });
  }

  /********* Fine Action upload pdf informativa privacy **********/

  private verifyIfProtocollaIsReady() {
    const validazioni = Object.values(this.colValidation);
    if (!validazioni.some(v => v === this.statoValidazione.ERROR)) {
      this.isProtocollaReady = true;
    }
  }

  onCloseMessageProtocolla() {
    //redirect alla pagina di presentazione istanze
    this.router.navigate(["./"], { relativeTo: this.route.parent.parent });
  }

  private clearWarnStartProtocollazione() {
    this.messages.clear("warn-start-protocollazione");
  }

  // private downloadCertificazioneAntimafiaCorrente() {
  //   this.antimafiaService.downloadDichiarazioneAntimafia(this.dichiarazioneAntimafia.pdfFirmato, this.dichiarazioneAntimafia.tipoPdfFirmato);
  // }
}
