import { HttpClientDichiarazioneConsumiUmaService } from './../../../../../core-uma/services/http-client-dichiarazione-consumi-uma.service';
import { Component, EventEmitter, OnInit, Output, ViewChild } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import * as FileSaver from "file-saver";
import { MessageService } from 'primeng/api';
import { Dialog } from 'primeng/dialog';
import { Subscription } from 'rxjs';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';
import { UploadHelper } from 'src/app/a4g-common/uploadFile/uploadHelper';
import { FileConfigModel } from 'src/app/uma/core-uma/models/config/file-config.model';
import { AllegatoMotivazioneView, MotivazioneConsuntivoDto } from 'src/app/uma/core-uma/models/dto/MotivazioneConsuntivoDto';
import { MotivazioneConsuntivo } from 'src/app/uma/core-uma/models/enums/MotivazioneConsuntivo.enum';
import { FileService } from 'src/app/uma/core-uma/services/file.service';
import { FileTypeEnum } from 'src/app/uma/shared-uma/models/enums/FileType-enum';
import { UMA_MESSAGES } from 'src/app/uma/uma.messages';
import { MotivazioneConsuntivoTipo } from 'src/app/uma/core-uma/models/enums/MotivazioneConsuntivoTipo.enum';
import * as _ from 'lodash';
import { IndiceUmaService } from 'src/app/uma/core-uma/services/indice-uma.service';
@Component({
  selector: 'app-popup-motivo-recupero',
  templateUrl: './popup-motivo-recupero.component.html',
  styleUrls: ['./popup-motivo-recupero.component.scss']
})
export class PopupMotivoRecuperoComponent implements OnInit {
  @ViewChild('dialogRecupero', { static: true }) dialog: Dialog;
  @ViewChild('fileCaricato') file: File;
  @Output() chiudiPopup = new EventEmitter();

  display: boolean;
  intestazionePopup: string;

  idDichiarazione: number;
  tipoMotivazione: keyof typeof MotivazioneConsuntivoTipo;
  motivazioneConsuntiviDto: MotivazioneConsuntivoDto;
  motivazioneEnum = MotivazioneConsuntivo;
  fileConfig: FileConfigModel;
  motivazioneForm: FormGroup;
  READONLY_MODE: boolean;
  
  // Subscriptions
  postClienteSubscription: Subscription;
  routerSubscription: Subscription;

  constructor(
    private messageService: MessageService,
    private fileService: FileService,
    private httpClientDichiarazioneConsumiUmaService: HttpClientDichiarazioneConsumiUmaService,
    private indiceUmaService: IndiceUmaService
  ) { }

  ngOnInit() {
    this.intestazionePopup = "Motivo recupero";
    this.fileConfig = this.fileService.loadConfigPdf();
    this.motivazioneForm = new FormGroup({
      tipo: new FormControl(null)
    })
  }

  open(tipo: keyof typeof MotivazioneConsuntivoTipo, motivazioneConsuntivi: MotivazioneConsuntivoDto) {
    this.display = true;
    this.READONLY_MODE = this.indiceUmaService.READONLY_MODE_DICHIARAZIONE || (localStorage.getItem('UMA_RO_DICH') == 'true' ? true : false);
    this.tipoMotivazione = tipo;
    if (motivazioneConsuntivi != null && motivazioneConsuntivi.allegati != null && motivazioneConsuntivi.allegati.length) {
      this.motivazioneConsuntiviDto = _.cloneDeep(motivazioneConsuntivi);
      this.motivazioneForm.get('tipo').setValue(motivazioneConsuntivi.motivazione);
    } else {
      this.initValue();
    }
  }

  caricaAllegato() {
    document.getElementById('fileCaricato').click();
  }

  eliminaAllegato(index: number, file: AllegatoMotivazioneView) {
    if (this.motivazioneConsuntiviDto && this.motivazioneConsuntiviDto.allegati && this.motivazioneConsuntiviDto.allegati.length == 1) {
      this.messageService.add(A4gMessages.getToast('tst-allegati', A4gSeverityMessage.error, UMA_MESSAGES.FILE_ERRORS.NO_ALLEGATI));
      return;
    }
    if (this.motivazioneConsuntiviDto && this.motivazioneConsuntiviDto.allegati && this.motivazioneConsuntiviDto.allegati.length > 1) {
      this.motivazioneConsuntiviDto.allegati.splice(index, 1);
      return;
    }
  }

  salva($event: Event) {
    if (this.motivazioneForm.get('tipo').value == null) {
      this.messageService.add(A4gMessages.getToast('tst-allegati', A4gSeverityMessage.error, UMA_MESSAGES.motivazioneNotSeleziona));
      return;
    }
    if (!this.motivazioneConsuntiviDto.allegati || (this.motivazioneConsuntiviDto.allegati && !this.motivazioneConsuntiviDto.allegati.length)) {
      this.messageService.add(A4gMessages.getToast('tst-allegati', A4gSeverityMessage.error, UMA_MESSAGES.FILE_ERRORS.NO_ALLEGATI));
      return;
    }
    this.motivazioneConsuntiviDto.motivazione = this.motivazioneForm.get('tipo').value;
    const motivazioneConsuntivoDto = _.cloneDeep(this.motivazioneConsuntiviDto); // break reference
    this.chiudiPopup.emit({ tipo: this.tipoMotivazione, motivazioneConsuntivoDto });
    this.display = false;
  }

  annulla() {
    this.motivazioneConsuntiviDto = null;
    this.display = false;
  }

  visualizzaAllegato(index: number, allegato: AllegatoMotivazioneView) {
    if (allegato != null && allegato.idAllegato == null) {
      const fileURL = URL.createObjectURL(allegato.file);
      FileSaver.saveAs(allegato.file, allegato.file.name);
      if (allegato.file.type === FileTypeEnum.PDF) {
        window.open(fileURL);
      }
    } else if (allegato.idAllegato != null) {
      this.httpClientDichiarazioneConsumiUmaService.getAllegatoConsuntivo(allegato.idDichiarazione, allegato.idConsuntivo, allegato.idAllegato)
        .subscribe((byte: string) => {
          const fileURL = URL.createObjectURL(byte);
          window.open(fileURL);
        });
    } else { // allegato == null
      console.log('Allegato non trovato!!!');
    }
  }

  onFileChange(event) {
    if (event.target.files && event.target.files.length > 0) {
      const file: File = event.target.files[0];
      const uploadHelper = new UploadHelper(this.fileConfig.fileExt, this.fileConfig.maxSize);

      if (file.name.indexOf('$$') > -1) {
        this.messageService.add(A4gMessages.getToast('tst-dialog', A4gSeverityMessage.error, UMA_MESSAGES.FILE_ERRORS.SPECIAL_CHAR));
        return;
      }
      if (file.name.length > 50) {
        this.messageService.add(A4gMessages.getToast('tst-dialog', A4gSeverityMessage.error, UMA_MESSAGES.FILE_ERRORS.FILENAME));
        return;
      }
      if (!uploadHelper.isValidFileExtension(file)) {
        this.messageService.add(A4gMessages.getToast('tst-dialog', A4gSeverityMessage.error, UMA_MESSAGES.FILE_ERRORS.EXT_PDF));
        return;
      }
      if (!uploadHelper.isValidFileSize(file)) {
        this.messageService.add(A4gMessages.getToast('tst-dialog', A4gSeverityMessage.error, UMA_MESSAGES.FILE_ERRORS.SIZE));
        return;
      }
      this.motivazioneConsuntiviDto.allegati.push({ file, name: file.name, idAllegato: null });
      this.dialog.center();
    }
  }

  private initValue() {
    this.motivazioneConsuntiviDto = {} as MotivazioneConsuntivoDto;
    this.motivazioneConsuntiviDto.allegati = [];
    this.motivazioneForm.get('tipo').setValue(null);
  }

}
