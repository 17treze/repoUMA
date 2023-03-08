import { Component, OnInit, EventEmitter, ViewChild, OnDestroy, Input } from "@angular/core";
import { DichiarazioneAntimafiaService } from "../../dichiarazione-antimafia.service";
import { DatiDichiarazione } from "../../classi/datiDichiarazione";
import { DichiarazioneAntimafia } from "../../classi/dichiarazioneAntimafia";
import { SoggettiImpresa, FamiliariConviventi, AllegatoDicFamConv } from "../../classi/datiDichiarazione";
import { Labels } from "../../../../app.labels";
import { AntimafiaService } from '../../antimafia.service';
import { Configuration } from '../../../../app.constants';
import { Utils } from '../antimafia.utils';
import { StepEvent } from "../../classi/stepEvent";
import { SelectItem, ConfirmationService, Message, MessageService } from 'primeng/api';
import { Messages } from 'primeng/messages';
import { Panel } from 'primeng/panel';
import { StepActionComponent } from "../step-action/step-action.component";
import { StatoValidazione, ResponseValidazione } from '../../classi/statoValidazione';
import { AntimafiaValidationService } from '../antimafia-validation.service';
import { UploadFile } from "../../classi/UploadFile";
import { HttpErrorResponse, HttpClient } from "@angular/common/http";
import * as FileSaver from "file-saver";
import { A4gMessages, A4gSeverityMessage } from "src/app/a4g-common/a4g-messages";
import { DatePipe } from '@angular/common';
import { ActionMenuComponent } from "src/app/a4g-common/action-menu.component";
import { LoaderService } from 'src/app/loader.service';


@Component({
  selector: "app-familiari",
  templateUrl: "./familiari.component.html",
  styleUrls: ["./familiari.component.css"],
  providers: [ConfirmationService, DatePipe]

})
export class FamiliariComponent implements OnInit, OnDestroy {
  dichiarazioneAntimafia: DichiarazioneAntimafia;
  intestazioni = Labels;
  labels = Labels;
  cols: any[];
  checkDichiarazione: boolean[];
  msgs: Message[] = [];
  listaSoggetti: SoggettiImpresa[];
  listaSoggettiTable: SoggettiImpresa[];
  soggettoSelezionato: SoggettiImpresa;
  gradoParentelaItems: SelectItem[];
  soggettoTableIndex: number;
  soggettoSelezionatoMenuAction: SoggettiImpresa;
  soggettoChange = new EventEmitter<any>();

  @ViewChild(StepActionComponent, { static: true })
  private stepComponent: StepActionComponent;

  textTitle: string;
  primeCheckBox: any;
  model: any[] = new Array();

  @ViewChild('file') file;
  loading: boolean = false;
  private isFirmaDigitale: boolean;

  constructor(
    private dichiarazioneAntimafiaService: DichiarazioneAntimafiaService,
    private _configuration: Configuration,
    private antimafiaService: AntimafiaService,
    private stepEvent: StepEvent,
    private validator: AntimafiaValidationService,
    private messages: MessageService,
    private confirmationService: ConfirmationService,
    private http: HttpClient,
    private datePipe: DatePipe,
    private loader: LoaderService
  ) {
  }

  ngOnDestroy(): void {
    this.loader.resetTimeout();
  }

  ngOnInit() {
    this.loader.setTimeout(480000); //otto minuti
    this.model = [
      {
        label: 'AZIONI',
        items: [
          {
            label: 'Carica Modello', check: 'true', badgeStyle: 'file-uploaded-ok',
            items: [
              {
                label: 'Firma Autografa', command: () => {
                  this.uploadFile(false);
                }
              },
              {
                label: 'Firma Digitale', command: () => {
                  this.uploadFile(true);
                }
              }]
          },
          { separator: true },
          {
            label: 'Scarica Modello', command: () => {
              this.downloadPdfFamiliariConviventi();
            }
          },
          { separator: true },
          {
            label: 'Familiari', badge: 'true', command: () => {
              this.visualizzaFamiliari(this.soggettoSelezionatoMenuAction);

            }
          }
        ]
      }
    ];



    this.dichiarazioneAntimafia = this.dichiarazioneAntimafiaService.getDichiarazioneAntimafia();
    this.listaSoggetti = this.popolaDati(this.dichiarazioneAntimafia);
    if (this.isImpresaDittaIndividuale()) {
      this.listaSoggettiTable = this.getCaricheImportanti(this.listaSoggetti);
    } else {
      this.listaSoggettiTable = this.getCaricheAltre(this.listaSoggetti);
    }

    Utils.setInizioCarica(this.listaSoggettiTable, this.dichiarazioneAntimafia.datiDichiarazione.dettaglioImpresa.estremiCCIAA.dataIscrizione);
    //subscribe su evento di modica dei familiari
    //questo perch� la lista visualizzate � diversa dalla lista da mandare nel backend 
    this.soggettoChange.subscribe(
      (inputData: any) => {
        let data = inputData.data;//SoggettiImpresa
        let soggetto: SoggettiImpresa = this.listaSoggetti.find(soggetto => soggetto.codiceFiscale == data.codiceFiscale);
        soggetto.familiariConviventi = data.familiariConviventi;
        let carica = soggetto.carica.find(carica => carica.codice == data.carica[0].codice)
        carica.selezionato = data.carica[0].selezionato;
        carica.dichiarazione = data.carica[0].dichiarazione;
        if (inputData.aggiornaDichiarazione) {
          this.antimafiaService.aggiornaDichiarazioneAntimafia(this.dichiarazioneAntimafia)
            .subscribe(
              x => {
                this.messages.add(A4gMessages.getToast('generic', A4gSeverityMessage.success, A4gMessages.OPERAZIONE_OK));
              });
        }
      });


    this.gradoParentelaItems = [];
    this.gradoParentelaItems.push({ label: 'Seleziona una opzione', value: null });
    this.gradoParentelaItems.push({ label: Labels.coniuge, value: Labels.coniuge });
    this.gradoParentelaItems.push({ label: Labels.figlio, value: Labels.figlio });
    this.gradoParentelaItems.push({ label: Labels.altro, value: Labels.altro });
    this.textTitle = Labels.familiariConvinentiTitle;

    this.cols = [
      { field: 'cognome', header: this.intestazioni.cognome },
      { field: 'nome', header: this.intestazioni.nome },
      { field: 'codiceFiscale', header: this.intestazioni.codiceFiscale },
      { field: 'dataNascita', header: this.intestazioni.dataNascita },
      { field: null, header: this.intestazioni.carica },
      { field: null, header: this.intestazioni.dataInizioCarica },
      { field: null, header: this.intestazioni.dataFineCarica },
      { field: null, header: this.intestazioni.dichiarazione }
    ];

  }

  isImpresaDittaIndividuale(): boolean {
    if (
      this.dichiarazioneAntimafia.datiDichiarazione.dettaglioImpresa
        .formaGiuridicaCodice === "DI"
    ) {
      return true;
    }
    return false;
  }

  // get soggetti con carica
  popolaDati(dichiarazione: DichiarazioneAntimafia): any {
    let datiDichiarazione: DatiDichiarazione;

    if (dichiarazione != null) {
      datiDichiarazione = dichiarazione.datiDichiarazione;
    } else {
      console.log("dichiarazione Dettagli assente");
      return [];
    }
    if (datiDichiarazione.dettaglioImpresa.soggettiImpresa != null) {
      return datiDichiarazione.dettaglioImpresa.soggettiImpresa;
    } else {
      return [];
    }
  }

  //evento generato all'apeertura del menu AZIONI
  onDropdownMenuOpen(soggettoIndex, actionMenu: ActionMenuComponent, soggetto: SoggettiImpresa) {
    this.soggettoSelezionatoMenuAction = soggetto;
    this.soggettoTableIndex = soggettoIndex;
    let numeroFamiliari: any = 0;

    if (soggetto.familiariConviventi) {
      if (soggetto.familiariConviventi.length > 0 && soggetto.carica[0].selezionato) {
        numeroFamiliari = this.soggettoSelezionatoMenuAction.familiariConviventi.length;
      }
    }

    //visualizza il badge con il numero familiari
    actionMenu.badgeCounters = numeroFamiliari;
    if (this.hasAlreadyUpdateFile()) {
      actionMenu.badgeStylezed = true;
      let url = this.soggettoSelezionatoMenuAction.carica[0].href;
      actionMenu.badgeData = {
        "fileDownloadLink": this._configuration.antimafia_server + url,
        "fileName": 'Allegato 3-Familiari Conviventi_' + this.soggettoSelezionatoMenuAction.codiceFiscale + '.' + this.soggettoSelezionatoMenuAction.carica[0].tipoAllegato
      };
    }
  }

  getCaricheAltre(soggettiImpresa: SoggettiImpresa[]): SoggettiImpresa[] {
    // nel caso di pi� cariche duplico l'elemento per poterlo visualizzare correttamente
    let soggettiCloned = soggettiImpresa.map(x => Object.assign({}, x));
    const soggettiResultFlat: SoggettiImpresa[] = [];
    soggettiCloned.forEach(sogg => {
      sogg.carica.forEach(carica => {
        // clona soggetto per evitare problemi di referenza
        const sg: SoggettiImpresa = Object.assign({}, sogg);
        sg.carica = [carica];
        soggettiResultFlat.push(sg);
      });
    });
    return soggettiResultFlat;
  }

  getCaricheImportanti(soggettiImpresa: SoggettiImpresa[]): SoggettiImpresa[] {
    const soggImpresa = JSON.parse(JSON.stringify(soggettiImpresa));
    return soggImpresa
      .filter(c => c.carica = c.carica.filter(carica => carica.codice.match('DT|TIT|TI')))
      .filter(c => c.carica ? c.carica.length !== 0 : false);
  }
  onConfirmCheckDichiarazione() {
    this.soggettoSelezionato.familiariConviventi = null;
    this.soggettoSelezionato.carica[0].dichiarazione = false;
    this.soggettoChange.emit({ data: this.soggettoSelezionato, aggiornaDichiarazione: false });
    this.soggettoSelezionato = null;
    this.messages.clear('checkDichiarazione');
  }
  onRejectCheckDichiarazione() {
    this.soggettoSelezionato.carica[0].selezionato = true;
    this.primeCheckBox.checked = true;
    this.soggettoChange.emit({ data: this.soggettoSelezionato, aggiornaDichiarazione: false });
    this.messages.clear('checkDichiarazione');
  }

  onChangeDichiarazione(event: any) {
    this.soggettoChange.emit({ data: this.soggettoSelezionato, aggiornaDichiarazione: false });
  }

  compilazioneDichiarazione(soggettoImpresa: SoggettiImpresa, checked: any, primeCheckBox: any) {
    this.soggettoSelezionato = soggettoImpresa;
    this.primeCheckBox = primeCheckBox;
    //nel caso si voglia settare a falso la checkbox
    if (!checked) {
      //check se esistono familiari associate ad altre cariche
      let existSoggettoSelezionatoConAltraCarica = this.listaSoggettiTable
        .filter(soggetto => (soggetto.codiceFiscale == soggettoImpresa.codiceFiscale) && soggetto.carica[0].codice != soggettoImpresa.carica[0].codice)
        .some(soggetto => soggetto.carica.some(carica => carica.selezionato));
      //check se ci sono familiari collegati. In Caso positivo faccio comparire un alert di conferma
      if (soggettoImpresa.familiariConviventi
        && soggettoImpresa.familiariConviventi.length > 0
        && !existSoggettoSelezionatoConAltraCarica) {
        this.messages.clear();
        this.messages.add({ key: 'checkDichiarazione', sticky: true, severity: 'warn', summary: 'Tutti i familiari collegati saranno cancellati', detail: 'Sei sicuro di voler togliere la spunta?' });
        return;
      }
      //check dichiarazione 
      if (soggettoImpresa.carica[0].dichiarazione) {
        this.messages.clear();
        this.messages.add({ key: 'checkDichiarazione', sticky: true, severity: 'warn', summary: 'E\' stata spuntata la dichiarazione che NON ci sono famigliari conviventi.', detail: 'Sei sicuro di voler togliere la spunta?' });
        return;
      }
      this.soggettoSelezionato = null;
    } else {
      //caso in cui si vuole settare a true la checkbox
      this.soggettoSelezionato = soggettoImpresa;
      //check se sono presenti familiari per stesso CF ma carica diversa
      let soggettoConFamiliareEsistente: SoggettiImpresa = this.listaSoggetti.find(soggetto => (soggetto.codiceFiscale == soggettoImpresa.codiceFiscale)
        && (soggetto.familiariConviventi ? true : false));
      console.log("soggetto Con Familiare Esistente: " + soggettoConFamiliareEsistente);
      if (soggettoConFamiliareEsistente) {
        //alert("Soggetto Con Familiare gi� Esistente: "+soggettoImpresa.codiceFiscale);
        //values.currentTarget.checked=false;
        this.soggettoSelezionato.familiariConviventi = soggettoConFamiliareEsistente.familiariConviventi;
      }
      this.scrollToBottom();
    }
  }

  manageSoggettoConFamiliareEsistente() {
    //check se sono presenti familiari per stesso CF ma carica diversa
    let soggettoConFamiliareEsistente: SoggettiImpresa = this.listaSoggetti.find(soggetto => (soggetto.codiceFiscale == this.soggettoSelezionato.codiceFiscale)
      && (soggetto.familiariConviventi ? true : false));
    console.log("soggetto Con Familiare Esistente: " + soggettoConFamiliareEsistente);
    if (soggettoConFamiliareEsistente) {
      //aggiorno i familiari di tutti i soggetti con cariche multiple
      this.listaSoggettiTable.forEach(soggetto => {
        if (soggetto.codiceFiscale == this.soggettoSelezionato.codiceFiscale) {
          soggetto.familiariConviventi = soggettoConFamiliareEsistente.familiariConviventi;
        }
      });

    }
  }

  visualizzaFamiliari(soggettoImpresa: SoggettiImpresa) {
    //visualizzo i dati solo se presenti
    if (soggettoImpresa !== undefined) {
      if (soggettoImpresa.familiariConviventi
        && soggettoImpresa.familiariConviventi.length > 0
        && soggettoImpresa.carica[0].selezionato
        || (soggettoImpresa.carica[0].selezionato && soggettoImpresa.carica[0].dichiarazione)) {
        this.soggettoSelezionato = soggettoImpresa;
        this.scrollToBottom();
      } else {
        this.soggettoSelezionato = null;
      }
    }
  }

  scrollToBottom() {
    window.setTimeout(() => {
      if (document.querySelector("#idTableFamiliari"))
        document.querySelector("#idTableFamiliari").scrollIntoView();
    }, 500);
  }

  aggiungiFamiliare() {
    const fc: FamiliariConviventi = {
      nome: "",
      cognome: "",
      codiceFiscale: null,
      comuneNascita: "",
      provinciaNascita: "",
      dataNascita: "",
      sesso: "",
      residenza: {
        indirizzo: "",
        comune: "",
        provincia: "",
        CAP: ""
      },
      gradoParentela: null,

    };
    if (!this.soggettoSelezionato.familiariConviventi)
      this.soggettoSelezionato.familiariConviventi = new Array<FamiliariConviventi>();
    //if usato per evitare di inserire ripetuti elmenti vuoti
    if (!this.soggettoSelezionato.familiariConviventi.find(familiare => familiare.codiceFiscale == null))
      this.soggettoSelezionato.familiariConviventi.splice(0, 0, fc);//push(fc);
    this.soggettoSelezionato.carica[0].dichiarazione = false;
    this.soggettoChange.emit({ data: this.soggettoSelezionato, aggiornaDichiarazione: false });
  }

  onGradoParentelaChange(event) {
    this.soggettoChange.emit({ data: this.soggettoSelezionato, aggiornaDichiarazione: false });
  }

  cercaPerCodiceFiscale(codiceFiscale: string, familiare: FamiliariConviventi, familiarePanel: Panel, event: any) {
    const CF_REGEXP = /^[a-zA-Z0-9]{16}$/;


    if (!CF_REGEXP.test(codiceFiscale)) {
      this.messages.add(A4gMessages.getToast('generic', A4gSeverityMessage.error, A4gMessages.CHECK_CONFORMITA_CF));
      return;
    }
    if (this.soggettoSelezionato.familiariConviventi.some(familiare => (familiare.codiceFiscale ? familiare.codiceFiscale.toUpperCase() : "") == codiceFiscale.toUpperCase())) {
      this.messages.add(A4gMessages.getToast('generic', A4gSeverityMessage.error, A4gMessages.CF_ESISTENTE(codiceFiscale)));
      return;
    }
    this.antimafiaService.getPersona(codiceFiscale).subscribe(obj => {
      if (!obj.esito.codice.match("012")) {
        this.messages.add(A4gMessages.getToast('generic', A4gSeverityMessage.error, obj.esito.descrizione));
      } else {
        // controlla se � maggiorenne o meno
        const dataNascita = obj.risposta.persona.personaFisica.datiNascita.data.toString();
        const cf = obj.risposta.persona.personaFisica.codiceFiscale;
        const validationResponse = this.validator.validaFamiliariConviventiMaggiorenni(dataNascita, cf);
        if (validationResponse.esito === StatoValidazione.OK) {
          //mapping dati
          familiare.cognome = obj.risposta.persona.personaFisica.cognome;
          familiare.nome = obj.risposta.persona.personaFisica.nome;
          familiare.codiceFiscale = obj.risposta.persona.personaFisica.codiceFiscale;
          familiare.dataNascita = dataNascita;
          familiare.sesso = obj.risposta.persona.personaFisica.sesso.valore;
          familiare.comuneNascita = obj.risposta.persona.personaFisica.datiNascita.comune;
          familiare.provinciaNascita = obj.risposta.persona.personaFisica.datiNascita.provincia;
          familiare.residenza = {
            comune: obj.risposta.persona.personaFisica.domicilioFiscale.ubicazione.comune,
            indirizzo: obj.risposta.persona.personaFisica.domicilioFiscale.ubicazione.toponimo,
            provincia: obj.risposta.persona.personaFisica.domicilioFiscale.ubicazione.provincia,
            CAP: obj.risposta.persona.personaFisica.domicilioFiscale.ubicazione.cap
          };
          familiarePanel.collapsed = false;
        } else {
          this.messages.add(A4gMessages.getToast('generic', A4gSeverityMessage.error, validationResponse.messaggio));
        }
      }

    },
      err => this.messages.add(A4gMessages.getToast('generic', A4gSeverityMessage.error, A4gMessages.SERVIZIO_NON_DISPONIBILE))
    );
  }

  eliminaFamiliare(familiare: FamiliariConviventi) {
    const index = this.soggettoSelezionato.familiariConviventi.indexOf(familiare, 0);
    if (index > -1) {
      this.soggettoSelezionato.familiariConviventi.splice(index, 1);
    }
    this.soggettoChange.emit({ data: this.soggettoSelezionato, aggiornaDichiarazione: false });
    if (this.soggettoSelezionato.familiariConviventi.length == 0) {
      //reset soggetti con cariche multiple
      this.listaSoggettiTable
        .filter(soggetto => soggetto.codiceFiscale == this.soggettoSelezionato.codiceFiscale)
        .forEach(soggetto => soggetto.carica[0].selezionato = false);
      this.soggettoSelezionato.carica[0].selezionato = false;
      this.soggettoSelezionato.familiariConviventi = undefined;
      this.soggettoSelezionato = null;
    }
  }

  conferma(): void {
    //TODO mettere eventuali check
    console.log("Inzio PUT");
    if (!this.validazioniFamiliari()) {
      return;
    }
    // TODO: assegna var locale allegati fam conv a dichiarazione antimafia => PAOLO
    let responseValidazione = this.validator.validaFamiliariConviventiAllegati(this.listaSoggettiTable);
    if (responseValidazione.esito === StatoValidazione.ERROR) {
      this.messages.add(A4gMessages.getToast('generic', A4gSeverityMessage.error, responseValidazione.messaggio));
      return;
    }
    // aggiorno la lista dei soggetti con quella modificata nella schermata
    this.dichiarazioneAntimafia.datiDichiarazione.dettaglioImpresa.soggettiImpresa = this.listaSoggetti;
    // responseValidazione = this.validator.validaFamiliariConviventiAllegati(this.listaSoggettiTable);
    // if (responseValidazione.esito === StatoValidazione.ERROR) {
    //   alert(responseValidazione.messaggio);
    // }
    this.loading = true;
    this.antimafiaService.aggiornaDichiarazioneAntimafia(this.dichiarazioneAntimafia)
      .subscribe(
        aggiornaDichiarazioneEsito => {
          console.log("Modifica alla dichiarazione " + aggiornaDichiarazioneEsito.dichiarazione.id + " avvenuta con successo");
          if (aggiornaDichiarazioneEsito.esito != undefined) {
            this.messages.add(A4gMessages.getToast("generico", A4gSeverityMessage.error, aggiornaDichiarazioneEsito.esito))
          }
        },
        err => {
          let error = 'Errore in aggiornaDichiarazioneAntimafia: ' + err;
          console.error(error);
          this.messages.add(A4gMessages.getToast("generico", A4gSeverityMessage.error, error));
        },
        () => {
          this.loading = false;
          this.stepComponent.goNext();
        });
  }

  indietro() {
    this.stepEvent.previous();//getActualStep().emit(3);
  }


  /* Inizio Action upload pdf conviventi familiari */

  onFileChange(event) {
    if (event.target.files && event.target.files.length > 0) {

      let uploadFile: UploadFile = new UploadFile();
      uploadFile.file = event.target.files[0];
      uploadFile.isFirmaDigitale = this.isFirmaDigitale;

      let fileUpload: File = event.target.files[0];

      //Validazione del file da carica. Il tipo del file deve essere PDF oppure P7M
      //Non deve superare i 15MB di lunghezza
      const responseValidationType: ResponseValidazione = this.validator.validaAllegatoType(uploadFile.file, uploadFile.isFirmaDigitale);
      const responseValidationSize: ResponseValidazione = this.validator.validaAllegatoSize(uploadFile.file);

      if (responseValidationType.esito == StatoValidazione.OK) {
        if (responseValidationSize.esito == StatoValidazione.OK) {
          //this.converterByteBase64(uploadFile);
          this.uploadAndStoreFile(fileUpload, this.isFirmaDigitale);
        } else {
          this.messages.add(A4gMessages.getToast("generic", A4gSeverityMessage.error, responseValidationSize.messaggio));
        }
      } else {
        this.messages.add(A4gMessages.getToast("generic", A4gSeverityMessage.error, responseValidationType.messaggio));
      }
    }
  }

  //Funzione richiamata dal Menu-Action
  private uploadFile(isFirmaDigitale: boolean) {
    this.isFirmaDigitale = isFirmaDigitale;
    if (this.hasAlreadyUpdateFile()) {
      this.showWarnAlreadyUploadedFile();
    } else {
      this.openWindowsSelectFile();
    }
  }

  private uploadAndStoreFile(fileUpload: File, isFirmaDigitale: boolean) {
    let allegatoFamiliariConviventi = new AllegatoDicFamConv();

    if (this.soggettoSelezionatoMenuAction !== undefined) {
      allegatoFamiliariConviventi.cfSoggettoImpresa = this.soggettoSelezionatoMenuAction.codiceFiscale;
      allegatoFamiliariConviventi.codCarica = this.soggettoSelezionatoMenuAction.carica[0].codice;
      allegatoFamiliariConviventi.tipoFile = fileUpload.name.endsWith(A4gMessages.UPLOAD_FILE_TYPE_PDFP7M) ? "pdf.p7m" :
        fileUpload.name.endsWith(A4gMessages.UPLOAD_FILE_TYPE_PDF) ? "pdf" : null;
      if (this.soggettoSelezionatoMenuAction.carica[0].href !== null && this.soggettoSelezionatoMenuAction.carica[0].href !== undefined) {
        allegatoFamiliariConviventi.id = Number.parseInt(this.soggettoSelezionatoMenuAction.carica[0].href.split('/')[4]);
      }
      allegatoFamiliariConviventi.allegatoFamiliariConviventiPdf = fileUpload;
      allegatoFamiliariConviventi.firmaDigitale = isFirmaDigitale;

      this.antimafiaService.allegaFamiliariConviventi(allegatoFamiliariConviventi, fileUpload, this.dichiarazioneAntimafia.id).subscribe(
        (allegatoFamiliariConviventiResult) => {

          if (!A4gMessages.isUndefinedOrNull(allegatoFamiliariConviventiResult)) {
            if (!A4gMessages.isUndefinedOrNull(allegatoFamiliariConviventiResult.esito)) {
              this.messages.add(A4gMessages.getToast("generic", A4gSeverityMessage.error, allegatoFamiliariConviventiResult.esito.split(":")[1]));
              console.error("Errore in Aggiornamento Dichiarazione Antimafia: " + allegatoFamiliariConviventiResult.esito.split(":")[1] + "\nErrorCode:" + allegatoFamiliariConviventiResult.esito.split(":")[0]);
            } else {
              this.messages.add(A4gMessages.getToast("generic", A4gSeverityMessage.success, A4gMessages.OPERAZIONE_OK));
              if (!A4gMessages.isUndefinedOrNull(allegatoFamiliariConviventiResult.carica)) {

                if (!A4gMessages.isUndefinedOrNull(allegatoFamiliariConviventiResult.allegatoFamiliariConviventi)) {

                  this.listaSoggettiTable.forEach(
                    sogg => {
                      if (sogg.codiceFiscale.localeCompare(allegatoFamiliariConviventiResult.allegatoFamiliariConviventi.cfSoggettoImpresa) == 0) {
                        //const soggettoClone:SoggettiImpresa = Object.assign({}, sogg);

                        if (!A4gMessages.isUndefinedOrNull(sogg.carica) && sogg.carica.length > 0) {
                          sogg.carica.forEach(carica => {
                            if (carica.codice.localeCompare(allegatoFamiliariConviventiResult.carica.codice) == 0) {
                              //const cr:Carica = Object.assign({}, carica);
                              carica.href = allegatoFamiliariConviventiResult.carica.href;
                              carica.tipoAllegato = allegatoFamiliariConviventi.tipoFile;
                            }
                          });
                        }
                      }
                    }
                  );
                  this.listaSoggetti.forEach(
                    sogg => {
                      if (sogg.codiceFiscale.localeCompare(allegatoFamiliariConviventiResult.allegatoFamiliariConviventi.cfSoggettoImpresa) == 0) {
                        //const soggettoClone:SoggettiImpresa = Object.assign({}, sogg);

                        if (!A4gMessages.isUndefinedOrNull(sogg.carica) && sogg.carica.length > 0) {
                          sogg.carica.forEach(carica => {
                            if (carica.codice.localeCompare(allegatoFamiliariConviventiResult.carica.codice) == 0) {
                              //const cr:Carica = Object.assign({}, carica);
                              carica.href = allegatoFamiliariConviventiResult.carica.href;
                              carica.tipoAllegato = allegatoFamiliariConviventi.tipoFile;
                            }
                          });
                        }
                      }
                    }
                  );
                }
              }
            }

          }
        },
        (err) => {
          this.messages.add(A4gMessages.getToast("generic", A4gSeverityMessage.error, err.error));
          console.error(err);
        }
      );
    }
  }


  /*
  //Conversione del file in formato base64
  private converterByteBase64(uploadFile: UploadFile) {
    //if (this.isValidFile(uploadFile.file)) {
    this.readByteFile(uploadFile.file).then(
      (fileBase64: string) => {
        uploadFile.fileBase64 = fileBase64;

        let allegatoFamiliariConviventi = new AllegatoDicFamConv();
        if (this.soggettoSelezionatoMenuAction !== undefined) {
          allegatoFamiliariConviventi.cfSoggettoImpresa = this.soggettoSelezionatoMenuAction.codiceFiscale;
          allegatoFamiliariConviventi.codCarica = this.soggettoSelezionatoMenuAction.carica[0].codice;
          allegatoFamiliariConviventi.idDichiarazioneAntimafia = this.dichiarazioneAntimafia.id;
          if (this.soggettoSelezionatoMenuAction.carica[0].href !== null && this.soggettoSelezionatoMenuAction.carica[0].href !== undefined) {
            allegatoFamiliariConviventi.id = Number.parseInt(this.soggettoSelezionatoMenuAction.carica[0].href.split('/')[4]);
          }
          this.soggettoSelezionatoMenuAction.carica[0].firmaDigitale = uploadFile.isFirmaDigitale;
          allegatoFamiliariConviventi.allegatoFamiliariConviventiPdf = uploadFile.fileBase64;
          allegatoFamiliariConviventi.firmaDigitale = uploadFile.isFirmaDigitale;
        }

        this.antimafiaService.allegaFamiliariConviventi(allegatoFamiliariConviventi).subscribe(
          (allegatoFamiliariConviventiResult) => {

            if (!A4gMessages.isUndefinedOrNull(allegatoFamiliariConviventiResult)) {
              if (!A4gMessages.isUndefinedOrNull(allegatoFamiliariConviventiResult.esito)) {
                this.messages.add(A4gMessages.getToast("generic", A4gSeverityMessage.error, allegatoFamiliariConviventiResult.esito))
              } else {
                this.messages.add(A4gMessages.getToast("generic", A4gSeverityMessage.success, A4gMessages.OPERAZIONE_OK));
                if (!A4gMessages.isUndefinedOrNull(allegatoFamiliariConviventiResult.carica)) {

                  if (!A4gMessages.isUndefinedOrNull(allegatoFamiliariConviventiResult.allegatoFamiliariConviventi)) {

                    this.listaSoggettiTable.forEach(
                      sogg => {
                        if (sogg.codiceFiscale.localeCompare(allegatoFamiliariConviventiResult.allegatoFamiliariConviventi.cfSoggettoImpresa) == 0) {
                          //const soggettoClone:SoggettiImpresa = Object.assign({}, sogg);

                          if (!A4gMessages.isUndefinedOrNull(sogg.carica) && sogg.carica.length > 0) {
                            sogg.carica.forEach(carica => {
                              if (carica.codice.localeCompare(allegatoFamiliariConviventiResult.carica.codice) == 0) {
                                //const cr:Carica = Object.assign({}, carica);
                                carica.href = allegatoFamiliariConviventiResult.carica.href;
                              }
                            });
                          }
                        }
                      }
                    );
                    this.listaSoggetti.forEach(
                      sogg => {
                        if (sogg.codiceFiscale.localeCompare(allegatoFamiliariConviventiResult.allegatoFamiliariConviventi.cfSoggettoImpresa) == 0) {
                          //const soggettoClone:SoggettiImpresa = Object.assign({}, sogg);

                          if (!A4gMessages.isUndefinedOrNull(sogg.carica) && sogg.carica.length > 0) {
                            sogg.carica.forEach(carica => {
                              if (carica.codice.localeCompare(allegatoFamiliariConviventiResult.carica.codice) == 0) {
                                //const cr:Carica = Object.assign({}, carica);
                                carica.href = allegatoFamiliariConviventiResult.carica.href;
                              }
                            });
                          }
                        }
                      }
                    );
                  }
                }
              }

            }
          },
          (err) => {
            this.messages.add(A4gMessages.getToast("generic", A4gSeverityMessage.error, err.error));
            console.error(err);
          }
        );
      },

      (err) => console.error(err)
    );

  }

*/

  //Utility che converte un file in formato base64
  private readByteFile(file: File) {
    return new Promise((resolve, reject) => {
      let reader = new FileReader();
      let error = "Errore nell' estrapolare i byte dal file " + file.name;
      let value;
      if (file == null || file === undefined) {
        reject(error);
      } else {
        reader.readAsDataURL(file);
        reader.onloadend = (event) => {
          value = reader.result;
          if (value == null) {
            reject(error)
          } else {
            resolve(value.toString().split(',')[1]);
          }
        }
      }
    });
  }

  //Apertura finestra del SO  per la selezione di un file
  private openWindowsSelectFile() {
    this.file.nativeElement.click();
  }

  //Controllo se è stato caricato un file in precedenza
  private hasAlreadyUpdateFile(): boolean {
    if (this.soggettoSelezionatoMenuAction !== undefined) {
      if (this.soggettoSelezionatoMenuAction.carica[0].href !== undefined) {
        return true;
      }
    }
    return false;
  }

  private showWarnAlreadyUploadedFile() {
    this.messages.add(A4gMessages.getToast("warn-already-uploadedFile", A4gSeverityMessage.warn, A4gMessages.FILE_ALREADY_UPLOADED));
  }

  onRejectHasAlreadyUploadedFile() {
    this.messages.clear('warn-already-uploadedFile');
  }

  onConfirmHasAlreadyUploadedFile() {
    this.messages.clear('warn-already-uploadedFile');
    this.openWindowsSelectFile();
  }

  onReject() {
    this.onRejectCheckDichiarazione();
  }

  /********* Fine Action upload pdf conviventi familiari **********/


  /********* Inizio Action download pdf familiari conviventi ******/

  private downloadPdfFamiliariConviventi(): any {
    if (!this.validazioniFamiliari(this.soggettoSelezionatoMenuAction)) {
      return;
    }
    //load template file
    this.http.get(this._configuration.UrlGetAllFamConv, {
      responseType: "blob"
    }).subscribe(response => {
      let url = this._configuration.UrlStampaPDF;
      let soggetto: SoggettiImpresa = this.soggettoSelezionatoMenuAction;
      let template: File = this.blobToFile(response, "all3famconv.docx");

      var dati = { "soggetto": soggetto, "dettaglioImpresa": this.dichiarazioneAntimafia.datiDichiarazione.dettaglioImpresa, 'dataOdierna': this.datePipe.transform(Date.now(), 'dd/MM/yyyy') };
      const formData = new FormData();

      formData.append('file', template, template.name);
      formData.append('dati', JSON.stringify(dati));
      formData.append('formatoStampa', 'PDF_A');

      return this.http.post(url, formData, { responseType: 'blob' }).subscribe(data => {
        FileSaver.saveAs(data, 'Allegato 3-Familiari Conviventi_' + soggetto.codiceFiscale + '.pdf')
      }, (err: HttpErrorResponse) => {
        console.error('Errore nella creazione del file allegato famigliari:', err);
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
  }

  /*Fine Action download pdf familiari conviventi */

  /* BEGIN   salve e chiudi familiari*/
  salvaFamiliari() {
    if (this.validazioniFamiliari(this.soggettoSelezionato, true)) {
      this.soggettoChange.emit({ data: this.soggettoSelezionato, aggiornaDichiarazione: true });
      this.manageSoggettoConFamiliareEsistente();
    }

  }

  private validazioniFamiliari(soggettoSelezionato?: SoggettiImpresa, isSalvaFamigliare?: boolean): boolean {
    let returnValidazione = true;
    let soggettiToTest: SoggettiImpresa[] = [];
    if (soggettoSelezionato) {
      soggettiToTest.push(soggettoSelezionato);
    } else {
      soggettiToTest = this.listaSoggettiTable;
    }
    let responseValidazione = this.validator.validaFamiliariConviventiInserimentoCF(soggettiToTest);
    if (responseValidazione.esito === StatoValidazione.ERROR) {
      this.messages.add(A4gMessages.getToast('generic', A4gSeverityMessage.error, responseValidazione.messaggio));
      returnValidazione = false;
    }
    responseValidazione = this.validator.validaFamiliariConviventiGradoDiParentela(soggettiToTest);
    if (responseValidazione.esito === StatoValidazione.ERROR) {
      this.messages.add(A4gMessages.getToast('generic', A4gSeverityMessage.error, responseValidazione.messaggio));
      returnValidazione = false;
    }
    responseValidazione = this.validator.validaFamiliariConviventiCheckDichiarazione(soggettiToTest, isSalvaFamigliare);
    if (responseValidazione.esito === StatoValidazione.ERROR) {
      this.messages.add(A4gMessages.getToast('generic', A4gSeverityMessage.error, responseValidazione.messaggio));
      returnValidazione = false;
    }
    return returnValidazione;
  }
  /* END   salve e chiudi familiari*/

}
