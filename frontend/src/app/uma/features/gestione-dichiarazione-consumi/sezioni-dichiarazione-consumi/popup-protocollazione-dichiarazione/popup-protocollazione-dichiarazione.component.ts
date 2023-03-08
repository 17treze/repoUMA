import { ErrorService } from './../../../../../a4g-common/services/error.service';
import { ErrorDTO } from './../../../../../a4g-common/interfaces/error.model';
import { Component, OnInit, Input, Output, EventEmitter, ViewChild, ElementRef } from '@angular/core';
import { MessageService } from 'primeng/api';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';
import { UploadHelper } from 'src/app/a4g-common/uploadFile/uploadHelper';
import { ButtonProtocollazioneDichiarazione } from 'src/app/uma/core-uma/models/enums/ButtonProtocollazioneDichiarazione';
import { FileConfigModel } from 'src/app/uma/core-uma/models/config/file-config.model';
import { FileService } from 'src/app/uma/core-uma/services/file.service';
import { Subscription } from 'rxjs';
import { HttpClientProxyService } from 'src/app/uma/shared-uma/services/http-client-proxy.service';
import { VerificaFirmaErrorEnum } from 'src/app/uma/shared-uma/models/enums/VerificaFirmaError-enum';
import { UMA_MESSAGES } from 'src/app/uma/uma.messages';
import { HttpClientDichiarazioneConsumiUmaService } from 'src/app/uma/core-uma/services/http-client-dichiarazione-consumi-uma.service';
import * as FileSaver from "file-saver";
import { NavigatorService } from 'src/app/a4g-common/services/navigator.service';
import { AnagraficaFascicoloService } from 'src/app/fascicolo/creazione-fascicolo/anagrafica-fascicolo.service';
import { Configuration } from 'src/app/app.constants';
import { UtentiService } from 'src/app/utenti/utenti.service';
import { FileBox } from 'src/app/a4g-common/classi/FileBox';
import { ButtonTypeEnum, EnableBox, HeaderBox, ButtonBox, ClickButton } from 'src/app/a4g-common/box/box.model';

@Component({
  selector: 'app-popup-protocollazione-dichiarazione',
  templateUrl: 'popup-protocollazione-dichiarazione.component.html',
  styleUrls: ['popup-protocollazione-dichiarazione.component.scss']
})
export class PopupProtocollazioneDichiarazioneComponent implements OnInit {

  @Input() cfRichiedente: string;
  @Input() cuaa: string;
  @Input() idDichiarazione: string;
  @Input() idFascicolo: string;
  @Input() popupVisibile: boolean;
  @Output() chiudiPopup = new EventEmitter();
  @ViewChild('fileInput', { static: true }) fileInput: ElementRef;

  fileDichiarazione: FileBox;
  BUTTONS_ENUM = ButtonProtocollazioneDichiarazione;
  BUTTONS_TYPE_ENUM = ButtonTypeEnum;
  fileConfig: FileConfigModel;
  enabledDownload: EnableBox;
  enabledUpload: EnableBox;
  enabledProtocolla: EnableBox;
  dichiarazioneConsumiFile: Blob;

  // Firma del documento da protocollare
  readonly FIRMA_CHECK_DISABLED: boolean = true;  /** temporanea disabilitazione dei controlli di verifica firma */
  haFirma: boolean;                               /** se true il documento da protocollare ha a bordo la firma */

  // Subscriptions
  firmaSingolaSubscription: Subscription;
  protocollaDichiarazioneSubscription: Subscription;
  stampaPrivacySubscription: Subscription;

  constructor(
    private httpClientProxy: HttpClientProxyService,
    private httpClientDichiarazioneConsumiUma: HttpClientDichiarazioneConsumiUmaService,
    private messageService: MessageService,
    private fileService: FileService,
    private errorService: ErrorService,
    private navigatorService: NavigatorService,
    protected anagraficaFascicoloService: AnagraficaFascicoloService,
    protected _configuration: Configuration,
    protected utentiService: UtentiService,) {
  }

  ngOnInit() {
    this.fileConfig = this.fileService.loadConfig();
    this.initVariables();
  }

  ngOnDestroy() {
    if (this.firmaSingolaSubscription) {
      this.firmaSingolaSubscription.unsubscribe();
    }
    if (this.protocollaDichiarazioneSubscription) {
      this.protocollaDichiarazioneSubscription.unsubscribe();
    }
    if (this.stampaPrivacySubscription) {
      this.stampaPrivacySubscription.unsubscribe();
    }
  }

  public annulla() {
    this.popupVisibile = false;
    this.chiudiPopup.emit();
  }

  public onFileChange(event: Event, tipo: string) {
    if ((event.target as HTMLInputElement).files && (event.target as HTMLInputElement).files.length) {
      const file: File = (event.target as HTMLInputElement).files[0];
      const uploadHelper = new UploadHelper(this.fileConfig.fileExt, this.fileConfig.maxSize);
      if (uploadHelper.isValidFileExtension(file, UMA_MESSAGES.FILE_ERRORS.EXT) && uploadHelper.isValidFileSize(file, UMA_MESSAGES.FILE_ERRORS.SIZE)) {
        this.firmaSingolaSubscription = this.httpClientProxy.verificaFirmaSingola(file, this.cfRichiedente).subscribe((res) => {
          this.enableNextFields(tipo, file);
          this.haFirma = true;
        }, error => {
          this.haFirma = true;
          console.log('ERRORE: ', error);
          if (this.FIRMA_CHECK_DISABLED) {
            this.enableNextFields(tipo, file);          // TODO: rimuovere questo ramo dell'if quando la firma sarà Bloccante -> lasciare solo l'else
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

  public onFileChangePrivacy(event: Event, tipo: string) {
    if ((event.target as HTMLInputElement).files && (event.target as HTMLInputElement).files.length) {
      const file: File = (event.target as HTMLInputElement).files[0];
      const uploadHelper = new UploadHelper(this.fileConfig.fileExt, this.fileConfig.maxSize);
      if (uploadHelper.isValidFileExtension(file, UMA_MESSAGES.FILE_ERRORS.EXT) && uploadHelper.isValidFileSize(file, UMA_MESSAGES.FILE_ERRORS.SIZE)) {
        this.firmaSingolaSubscription = this.httpClientProxy.verificaFirmaSingola(file, this.cfRichiedente).subscribe((res) => {
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

  private showErrors(errors: Array<string>) {
    if (!errors || !errors.length) {
      return;
    }
    errors.forEach((errMessage: string) => {
      this.messageService.add(A4gMessages.getToast('tst-dlg', A4gSeverityMessage.error, errMessage));
    });
  }

  private enableNextFields(tipo: string, file: File) {
    if (tipo === 'dichiarazione') {
      this.fileDichiarazione.nome = file.name;
      this.fileDichiarazione.file = file;
      this.enabledUpload.enableHeader = true;
    }
    if (this.fileDichiarazione.file) {
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

  initVariables() {
    this.enabledDownload = { enableButton: true, enableHeader: false };
    this.enabledUpload = { enableButton: false, enableHeader: false };
    this.enabledProtocolla = { enableButton: false, enableHeader: false };
    this.fileDichiarazione = new FileBox();
    this.fileDichiarazione.showDeleteButton = true;
    this.fileInput.nativeElement.value = null;
  }

  onClickBoxButton(event: ClickButton, element?: HTMLElement) {
    switch (event.idButton) {
      case ButtonProtocollazioneDichiarazione.download_dichiarazione: {
        this.scaricaDichiarazioneConsumiFile();
        break;
      }
      case ButtonProtocollazioneDichiarazione.carica_dichiarazione_firmata: {
        this.caricaDichiarazione(event, element);
        break;
      }
      case ButtonProtocollazioneDichiarazione.protocolla_dichiarazione: {
        this.protocollaDichiarazione()
        break;
      }
      case ButtonProtocollazioneDichiarazione.elimina_allegato: {
        this.eliminaAllegato(event.componentName);
        break;
      }
      default:
        console.log('Click non identificato');
    }
  }

  private protocollaDichiarazione() {
      this.protocollaDichiarazioneSubscription = this.httpClientDichiarazioneConsumiUma.protocollaDichiarazioneConsumi(Number(this.idDichiarazione), this.fileDichiarazione.file, this.haFirma).subscribe(response => {
        this.messageService.add(A4gMessages.getToast('tst-dlg-protocollazione', A4gSeverityMessage.success, UMA_MESSAGES.protocollaDichiarazioneOK));
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

  private scaricaDichiarazioneConsumiFile() {
    this.httpClientDichiarazioneConsumiUma.getDichiarazioneConsumiFile(this.idDichiarazione).subscribe(
      result => {
        this.dichiarazioneConsumiFile = new Blob([result], { type: 'application/pdf' });
        FileSaver.saveAs(this.dichiarazioneConsumiFile, this.idDichiarazione + "_dichiarazioneconsumi.pdf");
        this.enabledDownload = { enableButton: true, enableHeader: true };
        this.enabledUpload = { enableButton: true, enableHeader: false };
      }, err => {
        this.dichiarazioneConsumiFile = null;
        console.log('ERRORE: ', err);
        this.messageService.add(A4gMessages.getToast('tst-dlg', A4gSeverityMessage.error, 'Errore imprevisto. Si prega di riprovare più tardi'));
      }
    );
  }

  private caricaDichiarazione(event: ClickButton, element: HTMLElement) {
    if (!element) {
      element = document.getElementById('caricaFileInput');
    }
    if (event.typeButton === ButtonTypeEnum.file && element) {
      element.click();
    }
  }

  private eliminaAllegato(componentName: string) {
    if (componentName === 'dichiarazione') {
      this.resetDichiarazione();
      this.enabledUpload.enableHeader = false;
      this.enabledProtocolla.enableButton = false;
    }
  }

  resetDichiarazione() {
    this.fileDichiarazione.file = null;
    this.fileDichiarazione.nome = null;
    this.fileInput.nativeElement.value = null;
  }

  onCloseToastProtocollazione() {
    this.navigatorService.goToPresentazioneIstanze(this.idFascicolo);
  }

}
