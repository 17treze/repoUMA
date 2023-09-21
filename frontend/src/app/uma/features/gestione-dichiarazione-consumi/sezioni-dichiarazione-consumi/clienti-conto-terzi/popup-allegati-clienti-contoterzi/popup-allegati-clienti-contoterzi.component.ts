import { ErrorService } from 'src/app/a4g-common/services/error.service';
import { HttpClientClienteUmaService } from 'src/app/uma/core-uma/services/http-client-cliente-uma.service';
import { Component, ElementRef, EventEmitter, Input, OnDestroy, OnInit, Output, ViewChild } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { MessageService } from 'primeng/api';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';
import { Subscription } from 'rxjs';
import { UploadHelper } from 'src/app/a4g-common/uploadFile/uploadHelper';
import { FileTypeEnum } from 'src/app/uma/shared-uma/models/enums/FileType-enum';
import * as FileSaver from "file-saver";
import { AllegatiClientiDialogViewModel } from 'src/app/uma/core-uma/models/viewModels/AllegatiClientiDialogViewModel';
import { ClienteDto } from 'src/app/uma/core-uma/models/dto/ClienteDto';
import { UMA_MESSAGES } from 'src/app/uma/uma.messages';

@Component({
  selector: 'app-popup-allegati-clienti-contoterzi',
  templateUrl: './popup-allegati-clienti-contoterzi.component.html',
  styleUrls: ['./popup-allegati-clienti-contoterzi.component.scss']
})
export class PopupAllegatiClientiContoterziComponent implements OnInit, OnDestroy {
  @ViewChild('dialog', { read: ElementRef, static: true }) dialogElement: ElementRef;

  // indica se al salvataggio, deve essere inserito anche il nuovo cliente (se false modifica solo gli allegati)
  @ViewChild('fileCaricato', { static: true }) file;

  @Input() READONLY_MODE: boolean
  @Output() chiudiPopup = new EventEmitter();

  cliente: ClienteDto;
  modalitaCrea: boolean;
  display: boolean;
  idDichiarazione: number;

  fileExt = '.pdf';
  fileAllegati: Array<any> = [];
  private maxSize = 2;
  uploadHelper = new UploadHelper(this.fileExt, this.maxSize);

  // Subscriptions
  postClienteSubscription: Subscription;
  routerSubscription: Subscription;
  getAllegatiSubscription: Subscription;
  postAllegatiSuscription: Subscription;

  constructor(
    private messageService: MessageService,
    private httpClientClienteUmaService: HttpClientClienteUmaService,
    private route: ActivatedRoute,
    private errorService: ErrorService
  ) { }

  ngOnInit() {
    this.routerSubscription = this.route.params
      .subscribe(params => {
        this.idDichiarazione = params['id'];
      });
  }

  ngOnDestroy() {
    if (this.postClienteSubscription) {
      this.postClienteSubscription.unsubscribe();
    }
    if (this.routerSubscription) {
      this.routerSubscription.unsubscribe();
    }
    if (this.getAllegatiSubscription) {
      this.getAllegatiSubscription.unsubscribe();
    }
    if (this.postAllegatiSuscription) {
      this.postAllegatiSuscription.unsubscribe();
    }
  }

  open(data: AllegatiClientiDialogViewModel) {
    this.display = true;
    this.modalitaCrea = data.modalitaCrea;
    this.cliente = data.cliente;
    if (!data.modalitaCrea && data.cliente && data.cliente.id) {
      this.getAllegatiSubscription = this.httpClientClienteUmaService.getAllegatiClientiContoterzi(this.idDichiarazione, data.cliente.id).subscribe(allegati => {
        this.fileAllegati = [];
        if (allegati && allegati.length) {
          allegati.forEach(allegato => {
            const file = this.uploadHelper.stringToFile(allegato.allegato, allegato.nomeFile, 'application/pdf');
            this.fileAllegati.push(file);
          });
        }
      }, error => this.errorService.showError(error, 'tst-allegati-clienti'));
    }
  }

  salva($event: Event) {
    if (this.modalitaCrea) {
      // Salvataggio del cliente con i suoi allegati
      this.addCliente(this.cliente.cuaa);
    } else {
      // Aggiornamento allegati
      this.postAllegatiSuscription = this.httpClientClienteUmaService.postAllegatiClientiContoterzi(this.idDichiarazione, this.cliente.id, this.fileAllegati).subscribe(response => {
        this.ripristinaValori();
        this.display = false;
        this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.success, UMA_MESSAGES.salvataggioOKConsumiLavorazioni));
      }, error => this.errorService.showError(error, 'tst-allegati-clienti'));
    }

  }

  annulla() {
    this.ripristinaValori();
    this.display = false;
  }

  caricaAllegato() {
    document.getElementById('fileCaricato').click();
  }

  addCliente(cuaa: string) {
    this.postClienteSubscription = this.httpClientClienteUmaService.postClientiContoterzi(this.idDichiarazione, cuaa, this.fileAllegati).subscribe((result) => {
      this.chiudiPopup.emit();
      this.ripristinaValori();
      this.display = false;
      this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.success, UMA_MESSAGES.salvataggioOKConsumiLavorazioni));
    }, error => {
      console.log('ERRORE: ', error);
      let messaggioErrore: string = 'Errore nel recupero dati.';
      if (error && error.error && error.error.message) {
        messaggioErrore = error.error.message;
      }
      this.errorService.showErrorWithMessage(messaggioErrore, 'tst-allegati-clienti');
    });
  }

  eliminaAllegato(index: number) {
    if (!this.modalitaCrea && this.fileAllegati && this.fileAllegati.length == 1) {
      this.errorService.showErrorWithMessage("E’ obbligatorio aver caricato almeno un allegato", 'tst-allegati-clienti');
      return;
    }
    if (!this.modalitaCrea && this.fileAllegati && this.fileAllegati.length > 1) {
      this.fileAllegati.splice(index, 1);
      return;
    }
    if (this.modalitaCrea) {
      this.fileAllegati.splice(index, 1);
      return;
    }
  }


  public visualizzaAllegato(index: number) {
    if (this.fileAllegati[index] != null) {
      const fileURL = URL.createObjectURL(this.fileAllegati[index]);
      FileSaver.saveAs(this.fileAllegati[index], this.fileAllegati[index].name);
      if (this.fileAllegati[index].type === FileTypeEnum.PDF) {
        window.open(fileURL);
      }
    }
  }

  public numeroMassimoAllegati(): boolean {
    let valido: boolean = false;
    if (this.fileAllegati.length >= 5) { valido = true; }
    return valido;
  }

  public onFileChange(event) {
    if (event.target.files && event.target.files.length > 0) {
      const file: File = event.target.files[0];

      if (file.name.length > 50) {
        this.errorService.showErrorWithMessage(UMA_MESSAGES.FILE_ERRORS.FILENAME, 'tst-dialog');
        return;
      }
      if (!this.uploadHelper.isValidFileExtension(file)) {
        this.errorService.showErrorWithMessage(UMA_MESSAGES.FILE_ERRORS.EXT_PDF, 'tst-dialog');
        return;
      }
      if (!this.uploadHelper.isValidFileSize(file)) {
        this.errorService.showErrorWithMessage(UMA_MESSAGES.FILE_ERRORS.SIZE, 'tst-dialog');
        return;
      }
      this.fileAllegati.push(file);
    }
  }

  ripristinaValori() {
    this.fileAllegati = [];
  }
}
