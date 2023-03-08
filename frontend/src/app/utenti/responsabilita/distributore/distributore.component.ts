import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { IParentInteraction } from '../../classi/IParentInteraction';
import { IChildInteraction } from '../../classi/IChildInteaction';
import { UtentiService } from '../../utenti.service';
import { MessageService } from 'primeng/api';
import * as FileSaver from 'file-saver';
import { NgForm } from '@angular/forms';
import {
  A4gSeverityMessage,
  A4gMessages,
} from 'src/app/a4g-common/a4g-messages';
import { UploadHelper } from 'src/app/a4g-common/uploadFile/uploadHelper';
import { Distributore } from 'src/app/a4g-common/classi/distributore';
import { Labels } from 'src/app/app.labels';

@Component({
  selector: 'app-distributore',
  templateUrl: './distributore.component.html',
  styleUrls: ['./distributore.component.css'],
})
export class DistributoreComponent implements OnInit, IChildInteraction {
  labels = Labels;
  index: number;
  selfRef: any;
  compInteraction: IParentInteraction;
  codeResponsabilita: string;
  title: string;
  disabled: boolean;

  submitted = false;
  availableDistributors = [] as Distributore[];
  selectedDistributors = [] as Distributore[];
  selectedIds = [];

  canUpload = false;
  icon = 'ui-icon-file-upload';

  responsabile: string;
  fileExt = '.PDF';
  maxSize = 2;
  fileAutorizzazione: String;

  uploadOk = false;

  @ViewChild('upFileDistributor') fileInput: ElementRef;
  uploadValid: boolean;

  constructor(
    private utentiService: UtentiService,
    private messageService: MessageService
  ) {}

  get isValid() {
    return this.selectedDistributors.length && this.responsabile && this.uploadValid;
  }

  get orderedAvailableDistributors() {
    return this.availableDistributors
      ? this.availableDistributors.sort((a, b) =>
          (a.denominazioneAzienda + a.comune).localeCompare(
            b.denominazioneAzienda + b.comune
          )
        )
      : [];
  }

  get orderedSelectedDistributors() {
    return this.selectedDistributors
      ? this.selectedDistributors.sort((a, b) =>
          (a.denominazioneAzienda + a.comune).localeCompare(
            b.denominazioneAzienda + b.comune
          )
        )
      : [];
  }

  ngOnInit() {
    this.utentiService.getDistributori().subscribe((distributors) => {
      if (!distributors || !distributors.length) {
        return;
      }

      distributors = distributors.sort((a, b) =>
        (a.denominazioneAzienda + a.comune).localeCompare(
          b.denominazioneAzienda + b.comune
        )
      );
      this.availableDistributors = distributors;
      this.syncDistributors(this.selectedIds);
    });
  }

  syncDistributors(selectedIds: number[]): number[] {
    if (!selectedIds) {
      return;
    }

    this.selectedDistributors = this.availableDistributors.filter((d) =>
      selectedIds.includes(d.id)
    );
    this.availableDistributors = this.availableDistributors.filter(
      (d) => !selectedIds.includes(d.id)
    );
  }

  removeMe() {
    this.compInteraction.remove(this.index);
  }

  downloadFile() {
    this.utentiService.getPdfAutorizzazioneAltriEnti().subscribe(
      (response: any) => {
        const blob = response;
        const file = new Blob([blob], {});

        const filename = 'AutorizzazioneDistributore' + '_' + Date.now() + '.pdf';
        FileSaver.saveAs(file, filename);
        this.canUpload = true;
      },
      (error) => {
        alert('Errore in scarico Pdf');
      },
      () => {}
    );
  }

  uploadFile(event) {
    if (event.target.files && event.target.files.length > 0) {
      const uploadFile = event.target.files[0];

      const uploadHelper = new UploadHelper(this.fileExt, this.maxSize);

      if (uploadHelper.isValidFile(uploadFile)) {
        this.fileAutorizzazione = uploadFile;

        this.utentiService.readByteFile(uploadFile).then(
          (fileBase64: string) => {
            this.fileAutorizzazione = fileBase64;
            this.uploadValid = true;
            this.messageService.add(
              A4gMessages.getToast(
                'tst',
                A4gSeverityMessage.success,
                A4gMessages.OPERAZIONE_OK
              )
            );
          },

          (err) => {
            this.handleError(err), (this.uploadValid = false);
          }
        );
        this.uploadOk = true;
      } else {
        uploadHelper.errors.forEach((item, index) => {
          this.messageService.add({
            key: 'tst',
            severity: 'error',
            summary: 'Errore!',
            detail: item,
            life: A4gMessages.TOAST_LIFE,
          });
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
      document.getElementById('upFileDistributor').click();
    }
  }

  private showWarnAlreadyUploadedFile() {
    this.messageService.add(
      A4gMessages.getToast(
        'warn-already-uploadedFile-distributor',
        A4gSeverityMessage.warn,
        A4gMessages.FILE_ALREADY_UPLOADED
      )
    );
  }

  onRejectHasAlreadyUploadedFile() {
    this.messageService.clear('warn-already-uploadedFile-distributor');
  }

  onConfirmHasAlreadyUploadedFile() {
    this.messageService.clear('warn-already-uploadedFile-distributor');
    document.getElementById('upFileDistributor').click();
  }

  handleError(error: any) {
    if (error && error.error && error.status === 500) {
      const errMsg = error.error;
      this.messageService.add({
        key: 'tst',
        severity: 'error',
        summary: 'Errore!',
        detail: errMsg,
        life: A4gMessages.TOAST_LIFE,
      });
    } else {
      this.messageService.add({
        key: 'tst',
        severity: 'error',
        summary: 'Errore!',
        detail: 'Errore generico',
        life: A4gMessages.TOAST_LIFE,
      });
    }
  }

  onSubmit(f: NgForm) {
    this.submitted = true;
    if (f.invalid) {
      this.messageService.add(
        A4gMessages.getToast(
          'tst',
          A4gSeverityMessage.error,
          A4gMessages.ERRORE_DATI
        )
      );
      return;
    }
    this.messageService.add(
      A4gMessages.getToast(
        'tst',
        A4gSeverityMessage.success,
        A4gMessages.OPERAZIONE_OK
      )
    );
  }

  setDisabled(input: boolean) {
    this.disabled = input;
  }

  setSelectedIds(existingDistributorsIds: number[]) {
    this.selectedIds = existingDistributorsIds;
  }
}
