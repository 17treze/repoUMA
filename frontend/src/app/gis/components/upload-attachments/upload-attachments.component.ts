import { ToastGisComponent } from './../toast-gis/toast-gis.component';
import { AllegatiGisService } from './../../services/allegati-gis.service';
import { Component, EventEmitter, Input, OnDestroy, OnInit, Output, ViewEncapsulation} from '@angular/core';
import { DetailGisComponent } from './../detail-gis/detail-gis.component';
import { Detail } from '../../models/searchgis/detail';
import { LoaderService } from 'src/app/loader.service';
import { Subject } from "rxjs";
import { AllegatiEvent } from '../../shared/AllegatiEvent';
import { ProfiloUtente } from '../../shared/profilo-utente';

@Component({
  selector: 'gis-upload-attachments',
  templateUrl: './upload-attachments.component.html',
  styleUrls: ['./upload-attachments.component.css'],
  encapsulation: ViewEncapsulation.None

})
export class UploadAttachmentsComponent {
  @Input() tipoAllegato: string;
  @Input() idDichiarato: string;
  @Input() detailResults: Detail;
  @Input() utenteCollegato: string;
  @Input() initFile: boolean;
  @Input() profiloUtente: ProfiloUtente;
  @Output() public loadPoligoniDichiarati = new EventEmitter();
  files: any[] = [];
  protected loaderService: LoaderService
  protected componentDestroyed$: Subject<boolean> = new Subject();

  response: any;
  byteFile: any;
  byteAllegato: any;
  fileName: any;
  size: any;
  descrizioneAllegato: string;

  constructor(protected detailGis: DetailGisComponent,
    protected toastComponent: ToastGisComponent,
    private allegatiService: AllegatiGisService,
    public allegatiEvent: AllegatiEvent  
  ) {

  }

  ngOnDestroy() {
    // this.loaderService.resetTimeout();
    this.componentDestroyed$.next(true);
    this.componentDestroyed$.complete();
  }

  checkFileExt(file) {
    if (file.type ===  'application/pdf' ||
        file.type === 'image/png' ||
        file.type === 'image/jpeg') {
          return true;
    } else {
      return false;
    }
  }

  checkFileSize(file) {
    if (file.size > 10485760) {
      return false;
    } else {
      return true;
    }
  }
  /**
   * on file drop handler
   */
  onFileDropped($event) {
    this.prepareFilesList($event);
  }

  /**
   * handle file from browsing
   */
  fileBrowseHandler(files) {
    if (this.checkFileSize(files[0])) {
      if (this.checkFileExt(files[0])) {
        this.fileName = files[0].name;
        this.size = files[0].size;
        this.initFile = this.detailGis.setInitAllegato(false);

        this.prepareFilesList(files);
        let fileReader = new FileReader();
        fileReader.onload = (e) => {
          this.byteFile = fileReader.result;
          this.byteAllegato = new Uint8Array(this.byteFile);
        };
        // fileReader.readAsText(files[0]);
        fileReader.readAsArrayBuffer(files[0]);
      } else {
        this.toastComponent.showErrorExt();
      };
    } else {
      this.toastComponent.showErrorSize();

    }
  }
  /**
   * Delete file from files list
   * @param index (File index)
   */
  deleteFile(index: number) {
    this.files.splice(index, 1);
    this.descrizioneAllegato = '';
  }

  /**
   * Simulate the upload process
   */
  uploadFilesSimulator(files) {
    setTimeout(() => {
        const progressInterval = setInterval(() => {
          if (files && files.length > 0 && files[0].progress === 100) {
            clearInterval(progressInterval);
            this.uploadFilesSimulator(files);
          } else if (files && files.length > 0) {
            if (files[0].size > 1000000 && files[0].size < 4000000 ) {
              files[0].progress += 2.5;
            } else if (files[0].size > 4000000 && files[0].size < 10485760) {
              files[0].progress += 1.25;
            } else if (files[0].size < 1000000 && files[0].size > 100000) {
              files[0].progress += 5;
            } else if (files[0].size < 100000) {
              files[0].progress += 10;
            }
          }
        }, 200);
    }, 1000);
  }

  /**
   * Convert Files list to normal array list
   * @param files (Files List)
   */
  prepareFilesList(files: Array<any>) {
    this.files.splice(0, 1);
    for (const item of files) {
      item.progress = 0;
      this.files.push(item);
    }
    this.uploadFilesSimulator(this.files);
  }

  /**
   * format bytes
   * @param bytes (File size in bytes)
   * @param decimals (Decimals point)
   */
  formatBytes(bytes, decimals) {
    if (bytes === 0) {
      return '0 Bytes';
    }
    const k = 1024;
    const dm = decimals <= 0 ? 0 : decimals || 2;
    const sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB'];
    const i = Math.floor(Math.log(bytes) / Math.log(k));
    return parseFloat((bytes / Math.pow(k, i)).toFixed(dm)) + ' ' + sizes[i];
  }

  public viewLabel() {
    let ret: boolean = false
    if (this.tipoAllegato ==='richiesta') {
      ret = true;
    }
    return ret;
  }
  
  richiestaConclusa() {
    let ret: boolean = false
    if (this.detailResults && this.detailResults.stato === 'CONCLUSA') {
      ret = true;
    }
    return ret;
  }

  salvaAllegati() {
    if (!this.descrizioneAllegato) {
      this.descrizioneAllegato = '';
    }
    let params = {
      'descrizione': this.descrizioneAllegato,
      'dimensione': this.size,
      'profiloUtente': this.profiloUtente.profilo
    };

    this.allegatiService.uploadFile(this.idDichiarato, params, this.files[0]).subscribe((respone: any) => {
      this.response = respone;
      
      if (this.response['status'] === 200 || this.response['status'] === 201) {
        // Se allegato aggiunto con successo,svuoto lista dei file da inserire, aggiorna la lista degli allegati e la count del badge
        this.files = [];
        this.descrizioneAllegato = '';
        this.toastComponent.showSuccessAllegato();
        // ricarico la lista ed il numero degli allegati
        this.ricaricaDati();
        this.ricaricaAllegati();
      } else {
        this.toastComponent.showError();
      }
    });
  }

  ricaricaDati() {
    this.loadPoligoniDichiarati.emit();
  }

  ricaricaAllegati() {
    this.allegatiEvent.allegati = [];
    this.allegatiService.getListaAllegati(this.idDichiarato, { numeroElementiPagina: 5, pagina: 0, triggerFromDetail: true }).subscribe((response: any) => {
      let allegati: Array<any> = [];
      if (response) {
        allegati = response;
      }
      this.allegatiEvent.allegati = this.allegatiEvent.allegati.concat(allegati);
      // this.allegatiEvent.params.triggerFromDetail = false;
    });

  }
}
