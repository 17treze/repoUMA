import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { UploadHelper } from 'src/app/a4g-common/uploadFile/uploadHelper';
import { MessageService } from 'primeng/api';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';
import * as FileSaver from "file-saver";
import { IstruttoriaService } from 'src/app/istruttoria/istruttoria-pac1420/domandaUnica/istruttoria.service';

@Component({
  selector: 'app-popup-genera-file-xml-cup',
  templateUrl: './popup-genera-file-xml-cup.component.html',
  styleUrls: ['./popup-genera-file-xml-cup.component.scss']
})
export class PopupGeneraFileXmlCup implements OnInit {
  display: boolean;
  public uploadCsv: boolean = false;
  public fileExt = '.csv';
  public csvFormGroup: FormGroup;
  public fileName = "DOCUMENTO CSV"
  fileCSV: File;
  documento: string;
  @ViewChild('myDocumento') myInputVariable: ElementRef;
  
  constructor(
    private istruttoriaService: IstruttoriaService,
    private messageService: MessageService
  ) { }

  ngOnInit() {
    this.csvFormGroup = new FormGroup({ documento: new FormControl(null, [Validators.required]) });    
  }

  open() {
    this.display = true;
  }

  annulla() {
    this.pulsiciFile();
    this.display = false;
  }

  public uploadFile(tipoFile: string) {
    this.pulsiciFile();    
    this.openWindowsSelectFile(tipoFile);
  }

  pulsiciFile() {
    this.fileName = "DOCUMENTO CSV";
    this.uploadCsv = false;
    this.fileCSV = null;    
    this.csvFormGroup.get('documento').setValue(null);
    this.myInputVariable.nativeElement.value = '';
  }

  private openWindowsSelectFile(tipoFile) {
    document.getElementById(tipoFile).click();
  }

  public onFileChange(event) {
    this.fileName = "DOCUMENTO CSV";
    this.uploadCsv = false;
    this.fileCSV = null;
    if (event.target.files && event.target.files.length > 0) {
      this.csvFormGroup.get('documento').setValue(null);
      this.fileCSV = event.target.files[0];
      const maxSize = 2;
      const uploadHelper = new UploadHelper(this.fileExt, maxSize);
      if (uploadHelper.isValidFileExtension(this.fileCSV)) {
        if (uploadHelper.isValidFileSize(this.fileCSV)) {
          this.csvFormGroup.get('documento').setValue(this.fileCSV);
          this.messageService.add(A4gMessages.getToast('tst-generaXml', A4gSeverityMessage.success, A4gMessages.UPLOAD_FILE_OK));
          this.uploadCsv = true;
          this.fileName = this.fileCSV.name;
        } else {
          this.messageService.add(A4gMessages.getToast('tst-generaXml', A4gSeverityMessage.error, A4gMessages.UPLOAD_FILE_SIZE));
        }
      } else {
        this.messageService.add(A4gMessages.getToast('tst-generaXml', A4gSeverityMessage.error, A4gMessages.UPLOAD_FILE_CSV));
      }
    }
  }

  scaricaFilePerCup() {
    console.log('Inzio POST');
    this.istruttoriaService.generaXmlPerCUP(this.fileCSV)
        .subscribe(
            xml => {
                this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.success, A4gMessages.OPERAZIONE_OK));
                var splitFileName = this.fileCSV.name.split(".", 2); 
                FileSaver.saveAs(xml, splitFileName[0] + '.xml');
                this.display = false;
                this.pulsiciFile();
            },
            error => {
                console.error('Errore durante la converisione del csv: ' + error.message),
                A4gMessages.handleError(this.messageService, error, error.message);
            }
        );
    }
}
