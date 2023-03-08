import { TipoAllegatoConsuntivo } from '../../../../../../a4g-common/classi/enums/uma/TipoAllegatoConsuntivo.enum';
import { FormatConverterService } from 'src/app/uma/shared-uma/services/format-converter.service';
import { Component, EventEmitter, OnInit, Output, ViewChild } from '@angular/core';
import { MessageService } from 'primeng/api';
import { Dialog } from 'primeng/dialog';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';
import { Subscription } from 'rxjs';
import { UploadHelper } from 'src/app/a4g-common/uploadFile/uploadHelper';
import { FileTypeEnum } from 'src/app/uma/shared-uma/models/enums/FileType-enum';
import { UMA_MESSAGES } from 'src/app/uma/uma.messages';
import { MotivazioneConsuntivo } from 'src/app/uma/core-uma/models/enums/MotivazioneConsuntivo.enum';
import { SelectItemTipoDocumentoViewModel } from 'src/app/a4g-common/classi/viewModels/SelectItemTipoDocumentoViewModel';
import { MotivazioneConsuntivoDto, AllegatoMotivazioneView } from 'src/app/uma/core-uma/models/dto/MotivazioneConsuntivoDto';
import { HttpClientDichiarazioneConsumiUmaService } from 'src/app/uma/core-uma/services/http-client-dichiarazione-consumi-uma.service';
import * as _ from 'lodash';
import { MotivazioneConsuntivoTipo } from 'src/app/uma/core-uma/models/enums/MotivazioneConsuntivoTipo.enum';
import * as FileSaver from "file-saver";
import { FileConfigModel } from 'src/app/uma/core-uma/models/config/file-config.model';
import { FileService } from 'src/app/uma/core-uma/services/file.service';
import { IndiceUmaService } from 'src/app/uma/core-uma/services/indice-uma.service';
import { TipologicheService } from 'src/app/a4g-common/services/tipologiche.service';
@Component({
  selector: 'app-popup-allegati-motivo-consumi',
  templateUrl: './popup-allegati-motivo-consumi.component.html',
  styleUrls: ['./popup-allegati-motivo-consumi.component.scss']
})
export class PopupAllegatiAllegatiMotivoComponent implements OnInit {
  @ViewChild('dialog', { static: true }) dialog: Dialog;

  @ViewChild('fileCaricato', { static: true }) file;

  @Output() chiudiPopup = new EventEmitter();

  display: boolean;
  idDichiarazione: number;
  tipoMotivazione: keyof typeof MotivazioneConsuntivoTipo;
  postClienteSubscription: Subscription;
  routerSubscription: Subscription;

  intestazionePopup: string;
  motivazione: keyof typeof MotivazioneConsuntivo;
  motivoRecuperoAmmissibile: MotivazioneConsuntivoDto;
  fileAllegato: AllegatoMotivazioneView;
  tipoDocumento: SelectItemTipoDocumentoViewModel;
  fileConfig: FileConfigModel;
  motivazioneEnum = MotivazioneConsuntivo;

  readonly SEPARATOR = '$$';
  READONLY_MODE: boolean;

  constructor(
    public tipologicheService: TipologicheService,
    private messageService: MessageService,
    private httpClientDichiarazioneConsumiUmaService: HttpClientDichiarazioneConsumiUmaService,
    private fileService: FileService,
    private indiceUmaService: IndiceUmaService,
    private formatConverte: FormatConverterService
  ) { }

  ngOnInit() {
    this.intestazionePopup = "Motivo ammesso";
    this.tipologicheService.setTipiDocumento();
    this.fileConfig = this.fileService.loadConfigPdf();
  }

  open(motivoRecuperoAmmissibile: MotivazioneConsuntivoDto) {
    this.display = true;
    this.READONLY_MODE = this.indiceUmaService.READONLY_MODE_DICHIARAZIONE || (localStorage.getItem('UMA_RO_DICH') == 'true' ? true : false);
    this.tipoMotivazione = MotivazioneConsuntivoTipo.AMMISSIBILE_GASOLIO_TERZI;
    if (motivoRecuperoAmmissibile != null && motivoRecuperoAmmissibile.allegati != null && motivoRecuperoAmmissibile.allegati.length) {
      this.motivoRecuperoAmmissibile = _.cloneDeep(motivoRecuperoAmmissibile);
      this.motivazione = motivoRecuperoAmmissibile.motivazione;
      this.setDescrizioneAndTipo();
    } else {
      this.initValue();
    }
  }

  reset() {
    this.motivoRecuperoAmmissibile = new MotivazioneConsuntivoDto();
    this.motivoRecuperoAmmissibile.allegati = [];
    this.motivazione = null;
    this.tipoDocumento = null;
    this.initFile();
  }

  setDescrizioneAndTipo() {
    if (!this.motivoRecuperoAmmissibile.allegati || !this.motivoRecuperoAmmissibile.allegati.length) {
      return;
    }
    this.motivoRecuperoAmmissibile.allegati.forEach((allegato: AllegatoMotivazioneView) => {
      const tokens = allegato.file && allegato.file.name && allegato.file.name.split(this.SEPARATOR);
      if (tokens && tokens.length > 1) {
        const filename = tokens[0];
        const tipoDocumento = tokens[1];
        const descrizione = tokens[2];
        const clonedFile = new File([allegato.file], filename, { type: allegato.file.type });
        allegato.file = clonedFile;
        allegato.tipoDocumento = tipoDocumento as keyof typeof TipoAllegatoConsuntivo;
        allegato.descrizione = descrizione;
      }
    });
  }

  salva($event: Event) {
    if (!this.motivoRecuperoAmmissibile.allegati || (this.motivoRecuperoAmmissibile.allegati && !this.motivoRecuperoAmmissibile.allegati.length)) {
      this.messageService.add(A4gMessages.getToast('tst-allegati', A4gSeverityMessage.error, UMA_MESSAGES.FILE_ERRORS.NO_ALLEGATI));
      return;
    }
    this.motivoRecuperoAmmissibile.motivazione = this.motivazione;
    const motivazioneConsuntivoDto = _.cloneDeep(this.motivoRecuperoAmmissibile);
    this.chiudiPopup.emit({ idConsuntivo: this.motivoRecuperoAmmissibile.idConsuntivo, tipo: MotivazioneConsuntivoTipo.AMMISSIBILE_GASOLIO_TERZI, motivazioneConsuntivoDto });
    this.display = false;
  }

  annulla() {
    // azzero i valori ed esco, perché quando si riapre la popup (open) vengono ricaricati quelli validi
    this.motivoRecuperoAmmissibile = new MotivazioneConsuntivoDto();
    this.motivoRecuperoAmmissibile.allegati = [];
    this.motivazione = null;
    this.tipoDocumento = null;
    this.initFile();
    this.display = false;
  }

  caricaAllegato() {
    document.getElementById('fileCaricato').click();
    this.tipoDocumento = null;
  }

  eliminaAllegato(index: number) {
    if (this.motivoRecuperoAmmissibile && this.motivoRecuperoAmmissibile.allegati && this.motivoRecuperoAmmissibile.allegati.length == 1) {
      this.messageService.add(A4gMessages.getToast('tst-allegati', A4gSeverityMessage.error, "E’ obbligatorio aver caricato almeno un allegato"));
      return;
    }
    if (this.motivoRecuperoAmmissibile && this.motivoRecuperoAmmissibile.allegati && this.motivoRecuperoAmmissibile.allegati.length > 1) {
      this.motivoRecuperoAmmissibile.allegati.splice(index, 1);
      return;
    }
  }

  visualizzaAllegato(allegato: AllegatoMotivazioneView) {
    if (allegato != null && allegato.idAllegato == null) {
      const cleanFile: File = this.removeSeparators(allegato.file);
      const fileURL = URL.createObjectURL(cleanFile);
      FileSaver.saveAs(cleanFile, cleanFile.name);
      if (allegato.file.type === FileTypeEnum.PDF) {
        window.open(fileURL);
      }
    } else if (allegato.idAllegato != null) {
      this.httpClientDichiarazioneConsumiUmaService.getAllegatoConsuntivo(allegato.idDichiarazione, allegato.idConsuntivo, allegato.idAllegato)
        .subscribe((byte: string) => {
          const uploadHelper = new UploadHelper(this.fileConfig.fileExt, this.fileConfig.maxSize);
          const file: File = uploadHelper.blobToFile(new Blob([byte]), allegato.name);
          FileSaver.saveAs(file);
          const fileURL = URL.createObjectURL(byte);
          window.open(fileURL);
        });
    } else {
      console.log('Allegato non trovato!!!');
    }
  }

  private removeSeparators(allegato: File): File {
    const tokens = allegato && allegato.name.split(this.SEPARATOR);
    if (tokens && tokens.length > 1 && tokens[0].indexOf(this.fileConfig.fileExt)) {
      return new File([allegato], tokens[0], { type: allegato.type }); // Aggiungo come nome solo il primo token (filename)
    } else {
      return new File([allegato], allegato.name, { type: allegato.type });
    }
  }

  private initValue() {
    if (!this.motivoRecuperoAmmissibile) {
      this.motivoRecuperoAmmissibile = new MotivazioneConsuntivoDto();
    }
    if (!this.motivoRecuperoAmmissibile.allegati) {
      this.motivoRecuperoAmmissibile.allegati = [];
    }
    this.tipoDocumento = null;
    this.motivazione = null;
    this.initFile();
  }

  private initFile() {
    this.fileAllegato = new AllegatoMotivazioneView();
    this.fileAllegato.name = '';
    this.fileAllegato.file = null;
    this.fileAllegato.descrizione = '';
    this.fileAllegato.tipoDocumento = null;
  }

  public onFileChange(event) {
    if (event.target.files && event.target.files.length > 0) {
      const file: File = event.target.files[0];
      const uploadHelper = new UploadHelper(this.fileConfig.fileExt, this.fileConfig.maxSize);

      /** caso in cui esiste un separatore nel nome del file es: nome$$file$$.pdf -> nome$$file non deve contenere il separatore */
      if (file.name.indexOf(this.SEPARATOR) > -1) {
        const filename = file.name.split(this.SEPARATOR)[0];
        if (filename.indexOf(this.fileConfig.fileExt) < 0) {
          this.messageService.add(A4gMessages.getToast('tst-dialog', A4gSeverityMessage.error, UMA_MESSAGES.FILE_ERRORS.SPECIAL_CHAR));
        }
        return;
      }
      /** caso in cui i separatori sono presenti correttamente, es: nomefile.pdf$$tipoDocumento$$descrizione */
      if (file.name.indexOf(this.SEPARATOR) > -1) { // caso in cui sono presenti i $$, valuto la lunghezza dopo lo split
        const filename = file.name.split(this.SEPARATOR)[0];
        if (filename.length > 50) {
          this.messageService.add(A4gMessages.getToast('tst-dialog', A4gSeverityMessage.error, UMA_MESSAGES.FILE_ERRORS.FILENAME));
          return;
        }
      } else if (file.name.length > 50) { // caso in cui non sono presenti i $$, valuto la lunghezza del filename per intero
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
      this.fileAllegato = new AllegatoMotivazioneView();
      this.fileAllegato.file = file;
      this.fileAllegato.name = file.name;
      this.fileAllegato.descrizione = '';
      this.fileAllegato.tipoDocumento = null;
      this.dialog.center();
    }
  }

  eliminaAllegatoCaricato() {
    this.initFile();
  }

  visualizzaAllegatoCaricato(index: number) {
    if (this.fileAllegato.file != null) {
      const fileURL = URL.createObjectURL(this.fileAllegato.file);
      FileSaver.saveAs(this.fileAllegato.file, this.fileAllegato.file.name);
      if (this.fileAllegato.file.type === FileTypeEnum.PDF) {
        window.open(fileURL);
      }
    }
  }

  validoSalvaAllegato(): boolean {
    if (this.formatConverte.isEmptyString(this.fileAllegato.descrizione) || !this.tipoDocumento) {
      return false;
    }
    return true;
  }

  salvaAllegato() {
    this.motivoRecuperoAmmissibile.motivazione = this.motivazione;
    this.fileAllegato.tipoDocumento = this.tipologicheService.tipiDocumento.find(i => i === this.tipoDocumento).enum;
    this.motivoRecuperoAmmissibile.allegati.push(this.fileAllegato);
    this.initFile();
  }

  descrizioneTipoDocumento(tipoDocumento: TipoAllegatoConsuntivo) {
    return this.tipologicheService.tipiDocumento.find(i => i.enum === tipoDocumento).label;
  }
}
