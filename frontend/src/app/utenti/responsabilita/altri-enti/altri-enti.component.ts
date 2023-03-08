import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { IParentInteraction } from '../../classi/IParentInteraction';
import { IChildInteraction } from '../../classi/IChildInteaction';
import { UtentiService } from '../../utenti.service';
import { MessageService } from 'primeng/api';
import * as FileSaver from 'file-saver';
import { UploadHelper } from 'src/app/a4g-common/uploadFile/uploadHelper';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';
import { NgForm } from '@angular/forms';

@Component({
  selector: 'app-altri-enti',
  templateUrl: './altri-enti.component.html',
  styleUrls: ['./altri-enti.component.css']
})
export class AltriEntiComponent implements OnInit, IChildInteraction {
  index: number;
  selfRef: any;
  //interface for Parent-Child interaction
  compInteraction: IParentInteraction;
  isValid: boolean;
  codeResponsabilita: string;
  title: string;
  disabled: boolean;
  canUpload: boolean;
  fileExt: string = '.PDF'; //.JPG,.GIF,.PNG
  maxSize: number = 2; // MB --> attenzione se usi maxsize di primeng è in byte 15*1000*1024
  fileAutorizzazione: String;
  uploadOk: boolean = false;
  icon = 'ui-icon-file-upload';
  submitted: boolean;
  denominazione: string;
  piva: string;
  dirigente: string;
  note: string;

  @ViewChild('upFileALTRI', { static: true }) fileInput: ElementRef;

  constructor(private utentiService: UtentiService, private messageService: MessageService) { }

  ngOnInit() {
    this.isValid = false;
    this.canUpload = false;
    this.submitted = false;
  }

  downloadFile() {
    this.utentiService.getPdfAutorizzazioneAltriEnti()
      .subscribe(
        (response: any) => {
          const blob = response;
          const file = new Blob([blob], {});

          const filename = 'AutorizzazioneAltriEnti' + '_' + Date.now() + '.pdf';
          FileSaver.saveAs(file, filename);
          this.canUpload = true;
        },
        error => {
          alert('Errore in scarico Pdf');
        },
        () => { }
      );
  }

  uploadFile(event) {
    console.log('ChangeFile');
    if (event.target.files && event.target.files.length > 0) {
      const uploadFile = event.target.files[0];
      console.log('FileName ' + uploadFile.name);
      console.log(uploadFile);

      const uploadHelper = new UploadHelper(this.fileExt, this.maxSize);

      console.log('FileSize ' + uploadHelper.getFileSize(uploadFile));

      if (uploadHelper.isValidFile(uploadFile)) {
        console.log('SubmitFile');

        this.fileAutorizzazione = uploadFile;

        this.utentiService.readByteFile(uploadFile).then(
          (fileBase64: string) => {
            this.fileAutorizzazione = fileBase64,
              console.log('Upload ok ' + fileBase64),
              this.isValid = true,
              this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.success, A4gMessages.OPERAZIONE_OK));
          },

          (err) => {
            this.handleError(err),
              this.isValid = false;
          }
        );
        this.uploadOk = true;
      } else {
        console.log('File non valido');
        uploadHelper.errors.forEach((item, index) => {
          this.messageService.add({ key: 'tst', severity: 'error', summary: 'Errore!', detail: item, life: A4gMessages.TOAST_LIFE });
        });
      }
      this.icon = 'ui-icon-done';
      this.fileInput.nativeElement.value = '';
    }
  }

  verifyUploadFile() {
    if (this.uploadOk) {
      this.showWarnAlreadyUploadedFile();
    } else {
      document.getElementById('upFileALTRI').click();
    }
  }

  private showWarnAlreadyUploadedFile() {
    this.messageService.add(A4gMessages.getToast('warn-already-uploadedFile-altriEnti', A4gSeverityMessage.warn, A4gMessages.FILE_ALREADY_UPLOADED));
  }

  onRejectHasAlreadyUploadedFile() {
    this.messageService.clear('warn-already-uploadedFile-altriEnti');
  }

  onConfirmHasAlreadyUploadedFile() {
    this.messageService.clear('warn-already-uploadedFile-altriEnti');
    document.getElementById('upFileALTRI').click();
  }

  handleError(error: any) {
    console.log('Error ' + error + ' msg ' + error.error + ' error.code ' + error.status);

    if (error && error.error && error.status === 500) {
      const errMsg = error.error;
      this.messageService.add({ key: 'tst', severity: 'error', summary: 'Errore!', detail: errMsg, life: A4gMessages.TOAST_LIFE });
    } else {
      this.messageService.add({ key: 'tst', severity: 'error', summary: 'Errore!', detail: 'Errore generico', life: A4gMessages.TOAST_LIFE });
    }
  }

  onSubmit(f: NgForm) {
    console.log('this.onSubmit');
    this.submitted = true;
    if (f.invalid) {
      this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.ERRORE_DATI));
      return;
    }

    this.isValid = true;
    this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.success, A4gMessages.OPERAZIONE_OK));
  }

  removeMe() {
    this.compInteraction.remove(this.index);
  }

  setDisabled(input: boolean) {
    this.disabled = input;
  }
}
