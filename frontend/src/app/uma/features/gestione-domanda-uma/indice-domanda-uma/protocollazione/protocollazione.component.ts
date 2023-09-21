import { ErrorDTO } from './../../../../../a4g-common/interfaces/error.model';
import { StepRichiestaCarburante } from 'src/app/uma/core-uma/models/enums/StepRichiestaCarburante.enum';
import { Component, ElementRef, EventEmitter, Input, OnInit, Output, ViewChild, OnDestroy } from '@angular/core';
import { MessageService } from 'primeng/api';
import { Subscription } from 'rxjs';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';
import { UploadHelper } from 'src/app/a4g-common/uploadFile/uploadHelper';
import { FileConfigModel } from 'src/app/uma/core-uma/models/config/file-config.model';
import { RichiestaCarburanteDto } from 'src/app/uma/core-uma/models/dto/RichiestaCarburanteDto';
import { ButtonProtocollazioneRichiesta } from 'src/app/uma/core-uma/models/enums/ButtonProtocollazioneRichiesta.enum';
import { FileService } from 'src/app/uma/core-uma/services/file.service';
import { HttpClientDomandaUmaService } from 'src/app/uma/core-uma/services/http-client-domanda-uma.service';
import { VerificaFirmaErrorEnum } from 'src/app/uma/shared-uma/models/enums/VerificaFirmaError-enum';
import { HttpClientProxyService } from 'src/app/uma/shared-uma/services/http-client-proxy.service';
import { UMA_MESSAGES } from 'src/app/uma/uma.messages';
import * as FileSaver from 'file-saver';
import * as _ from 'lodash';
import { ErrorService } from 'src/app/a4g-common/services/error.service';
import { Configuration } from 'src/app/app.constants';
import { UtentiService } from 'src/app/utenti/utenti.service';
import { NavigatorService } from 'src/app/a4g-common/services/navigator.service';
import { FileBox } from 'src/app/a4g-common/classi/FileBox';
import { ButtonTypeEnum, EnableBox, ClickButton, HeaderBox, ButtonBox } from 'src/app/a4g-common/box/box.model';
@Component({
  selector: 'app-protocollazione',
  templateUrl: './protocollazione.component.html',
  styleUrls: ['./protocollazione.component.scss']
})

export class ProtocollazioneComponent implements OnInit, OnDestroy {
  @Input() richiestaCarburante: RichiestaCarburanteDto;
  @Input() inCompilazione: boolean;
  @Input() idFascicolo: number;
  @Output() prevStep = new EventEmitter<number>();
  @ViewChild('fileInput', { static: true }) fileInput: ElementRef;
  @ViewChild('filePrivacyInput', { static: true }) filePrivacyInput: ElementRef;

  fileConfig: FileConfigModel;
  BUTTONS_ENUM = ButtonProtocollazioneRichiesta;
  BUTTONS_TYPE_ENUM = ButtonTypeEnum;
  enabledDownload: EnableBox;
  enabledUpload: EnableBox;
  enabledPrivacy: EnableBox;
  enabledProtocolla: EnableBox;

  fileRichiesta: FileBox;
  // Firma del documento da protocollare
  readonly FIRMA_CHECK_DISABLED: boolean = true;  /** temporanea disabilitazione dei controlli di verifica firma */
  haFirma: boolean;                               /** se true il documento da protocollare ha a bordo la firma */

  // Subscriptions
  stampaSubscription: Subscription;
  stampaPrivacySubscription: Subscription;
  protocollaRichiestaSubscription: Subscription;
  firmaSingolaSubscription: Subscription;

  constructor(
    private httpClientProxy: HttpClientProxyService,
    private messageService: MessageService,
    private fileService: FileService,
    private httpClientDomandaUmaService: HttpClientDomandaUmaService,
    private errorService: ErrorService,
    protected _configuration: Configuration,
    protected utentiService: UtentiService,
    private navigatorService: NavigatorService
  ) { }

  goPrevStep() {
    this.prevStep.emit(StepRichiestaCarburante.PROTOCOLLAZIONE);
  }

  ngOnInit() {
    this.fileConfig = this.fileService.loadConfig();
    this.initVariables();
  }

  ngOnDestroy() {
    if (this.stampaSubscription) {
      this.stampaSubscription.unsubscribe();
    }
    if (this.protocollaRichiestaSubscription) {
      this.protocollaRichiestaSubscription.unsubscribe();
    }
    if (this.firmaSingolaSubscription) {
      this.firmaSingolaSubscription.unsubscribe();
    }
    if (this.stampaPrivacySubscription) {
      this.stampaPrivacySubscription.unsubscribe();
    }
  }

  initVariables() {
    this.enabledDownload = { enableButton: true, enableHeader: false };
    this.enabledUpload = { enableButton: false, enableHeader: false };
    this.enabledPrivacy = { enableButton: true, enableHeader: false };
    this.enabledProtocolla = { enableButton: false, enableHeader: false };
    this.fileRichiesta = new FileBox();
    this.fileRichiesta.showDeleteButton = true;
    this.resetRichiesta();
  }

  onClickBoxButton(event: ClickButton, element?: HTMLElement) {
    switch (event.idButton) {
      case ButtonProtocollazioneRichiesta.download_richiesta: {
        this.stampaRichiestaCarburantePDF();
        break;
      }
      case ButtonProtocollazioneRichiesta.carica_richiesta_firmata: {
        this.caricaRichiesta(event, element);
        break;
      }
      case ButtonProtocollazioneRichiesta.protocolla_richiesta: {
        this.protocollaRichiesta();
        break;
      }
      case ButtonProtocollazioneRichiesta.elimina_allegato: {
        this.eliminaAllegato(event.componentName);
        break;
      }
      default:
        console.log('Click non identificato');
    }
  }

  private stampaRichiestaCarburantePDF() {
    this.stampaSubscription = this.httpClientDomandaUmaService.getRichiestaCarburanteFile(this.richiestaCarburante.id.toString()).subscribe(pdf => {
      FileSaver.saveAs(new Blob([pdf], { type: 'application/pdf' }), this.richiestaCarburante.id.toString() + this.getFileName());
      this.enabledDownload.enableHeader = true;
      this.enabledUpload.enableButton = true;
    }, err => {
      console.log('ERRORE: ', err);
      this.errorService.showErrorWithMessage(UMA_MESSAGES.erroreStampaRichiesta, 'tst-dlg');
    });
  }

  private getFileName(): string {
    return this.richiestaCarburante.idRettificata == null ? '_richiestacarburante.pdf' : '_rettificacarburante.pdf';
  }

  private caricaRichiesta(event: ClickButton, element: HTMLElement) {
    if (!element) {
      element = document.getElementById('caricaFileInput');
    }
    if (event.typeButton === ButtonTypeEnum.file && element) {
      element.click();
    }
  }

  private protocollaRichiesta() {
     // Se la privacy esiste protocollo solo la domanda
      this.protocollaRichiestaSubscription = this.httpClientDomandaUmaService.protocollaDomanda(this.richiestaCarburante.id, this.fileRichiesta.file, this.haFirma).subscribe(response => {
        this.messageService.add(A4gMessages.getToast('tst-dlg-protocollazione', A4gSeverityMessage.success, UMA_MESSAGES.protocollaRichiestaOK));
      }, err => {
        console.log('ERRORE: ', err);
        if (err && err.error && err.error.message) {
          this.errorService.showErrorWithMessage(err.error.message, 'tst-dlg');
        } else {
          this.errorService.showErrorWithMessage(UMA_MESSAGES.erroreProtocollaRichiesta, 'tst-dlg');
        }
      });
      return;
  }

  onCloseToastProtocollazione() {
    // reperire il cuaa, eventualmente con nuova API
    this.navigatorService.goToPresentazioneIstanze('NTRGRG60L19A040K');
  }

  onFileChange(event: Event, tipo: string) {
    if ((event.target as HTMLInputElement).files && (event.target as HTMLInputElement).files.length) {
      this.enabledProtocolla.enableHeader = false;
      const file: File = (event.target as HTMLInputElement).files[0];
      const uploadHelper = new UploadHelper(this.fileConfig.fileExt, this.fileConfig.maxSize);
      // La verifica della firma deve essere adeguata e bypassata se l'utente è il titolare dellì'azienda
      if (uploadHelper.isValidFileExtension(file, UMA_MESSAGES.FILE_ERRORS.EXT) && uploadHelper.isValidFileSize(file, UMA_MESSAGES.FILE_ERRORS.SIZE)) {
        this.firmaSingolaSubscription = this.httpClientProxy.verificaFirmaSingola(file, this.richiestaCarburante.cfRichiedente).subscribe((res) => {
          this.enableNextFields(tipo, file);
          this.haFirma = true;
        }, error => {
          this.haFirma = true;
          console.log('ERRORE: ', error);
          if (this.FIRMA_CHECK_DISABLED) {
            this.enableNextFields(tipo, file);          // TODO: rimuovere questo ramo dell'if quando la firma sarà obbligatoria e bloccante (ora non è bloccante) -> lasciare solo l'else
            this.handleErrorFirma(error, true);
          } else {
            this.handleErrorFirma(error, true);
            this.fileInput.nativeElement.value = null;  // reset input
          }
        });
      }
      this.showErrors(uploadHelper.getErrors());
    }
  }

  onFileChangePrivacy(event: Event, tipo: string) {
    if ((event.target as HTMLInputElement).files && (event.target as HTMLInputElement).files.length) {
      this.enabledProtocolla.enableHeader = false;
      const file: File = (event.target as HTMLInputElement).files[0];
      const uploadHelper = new UploadHelper(this.fileConfig.fileExt, this.fileConfig.maxSize);
      if (uploadHelper.isValidFileExtension(file, UMA_MESSAGES.FILE_ERRORS.EXT) && uploadHelper.isValidFileSize(file, UMA_MESSAGES.FILE_ERRORS.SIZE)) {
        this.firmaSingolaSubscription = this.httpClientProxy.verificaFirmaSingola(file, this.richiestaCarburante.cfRichiedente).subscribe((res) => {
          this.enableNextFields(tipo, file);
        }, error => {
          console.log('ERRORE: ', error);
          this.handleErrorFirma(error, false);
          this.fileInput.nativeElement.value = null;  // reset input
        });
      }
      this.showErrors(uploadHelper.getErrors());
    }
  }

  private enableNextFields(tipo: string, file: File) {
    if (tipo === 'richiesta') {
      this.fileRichiesta.nome = file.name;
      this.fileRichiesta.file = file;
      this.enabledUpload.enableHeader = true;
    } 
    if (this.fileRichiesta.file) {
      this.enabledProtocolla.enableButton = true;
    }
  }

  private handleErrorFirma(error: ErrorDTO, checkHaFirma: boolean) {
    if (error && error.error && error.error.message) {
      switch (error.error.message) {
        case VerificaFirmaErrorEnum.FIRMATARIO_TITOLARE_DIVERSI:
          this.errorService.showErrorWithMessage(UMA_MESSAGES.FILE_ERRORS.CODICE_FISCALE_NO_MATCH, 'tst-dlg');
          break;
        case VerificaFirmaErrorEnum.FILE_NON_FIRMATO:
          this.errorService.showErrorWithMessage(UMA_MESSAGES.FILE_ERRORS.NO_FIRMA, 'tst-dlg');
          if (checkHaFirma) {
            this.haFirma = false;
          }
          break;
        case VerificaFirmaErrorEnum.FIRMA_NON_VALIDA:
          this.errorService.showErrorWithMessage(UMA_MESSAGES.FILE_ERRORS.NO_FIRMA_VALIDA, 'tst-dlg');
          break;
        case VerificaFirmaErrorEnum.DATI_FIRMATARIO_MANCANTI:
          this.errorService.showErrorWithMessage(UMA_MESSAGES.FILE_ERRORS.NO_DATI_FIRMATARIO, 'tst-dlg');
          break;
        case VerificaFirmaErrorEnum.FIRMATARIO_NON_UNIVOCO:
          this.errorService.showErrorWithMessage(UMA_MESSAGES.FILE_ERRORS.NO_SINGLE_FIRMATARIO, 'tst-dlg');
          break;
        default:
          this.errorService.showError(error, 'tst-dlg');
      }
    } else {
      this.errorService.showError(error, 'tst-dlg');
    }
  }

  getHeader(title: string, subtitle: string, icon: string): HeaderBox {
    return {
      title,
      subtitle,
      icon
    };
  }

  getButtonModel(label: string, icon: string, id: string, type: ButtonTypeEnum): ButtonBox {
    return {
      label,
      icon,
      id,
      type
    };
  }

  private showErrors(errors: Array<string>) {
    if (!errors || !errors.length) {
      return;
    }
    errors.forEach((errMessage: string) => {
      this.messageService.add(A4gMessages.getToast('tst-dlg', A4gSeverityMessage.error, errMessage));
    });
  }

  private eliminaAllegato(componentName: string) {
    if (componentName === 'richiesta') {
      this.resetRichiesta();
      this.enabledUpload.enableHeader = false;
      this.enabledProtocolla.enableButton = false;
    }
  }

  resetRichiesta() {
    this.fileRichiesta.file = null;
    this.fileRichiesta.nome = null;
    this.fileInput.nativeElement.value = null;
  }

  // costruzione della documentazione per l'informativa privacy da protocollare
  private getDocumentazioneInformativaPrivacy(nome: string, cognome: string, codiceFiscale: string, denominazione: string) {
    const oggetto: string =
      this._configuration.ProtocollaPrivacyOggetto +
      ' - ' +
      denominazione +
      ' - ' +
      codiceFiscale;
    const tipologiaDocumento: string = this._configuration.tipologiaDocumentoProtocolloPrivacy;
    const nomeFile = 'InformativaGeneralePrivacy.pdf';
    const mittente = {
      name: nome,
      surname: cognome,
      nationalIdentificationNumber: codiceFiscale,
      description:
        denominazione +
        ' - ' +
        codiceFiscale
    };
    const documentazionePrivacy = {
      mittente: mittente,
      oggetto: oggetto,
      tipologiaDocumentoPrincipale: tipologiaDocumento
    };
    return JSON.stringify(documentazionePrivacy);
  }
}
