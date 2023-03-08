import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { IParentInteraction } from '../../classi/IParentInteraction';
import { IChildInteraction } from '../../classi/IChildInteaction';
import { UtentiService } from '../../utenti.service';
import { MessageService } from 'primeng/api';
import { TreeNode } from 'primeng/api';
import * as FileSaver from 'file-saver';
import { NgForm } from '@angular/forms';
import { A4gSeverityMessage, A4gMessages } from 'src/app/a4g-common/a4g-messages';
import { UploadHelper } from 'src/app/a4g-common/uploadFile/uploadHelper';
import { EnteTreeNode } from 'src/app/a4g-common/classi/EntiSediCaaPrimeNgTree';

@Component({
  selector: 'app-caa',
  templateUrl: './caa.component.html',
  styleUrls: ['./caa.component.css']
})
export class CAAComponent implements OnInit, IChildInteraction {
  index: number;
  selfRef: any;
  //interface for Parent-Child interaction
  compInteraction: IParentInteraction;
  isValid: boolean;
  codeResponsabilita: string;
  title: string;
  disabled: boolean;

  submitted: boolean;
  enti;

  canUpload: boolean;
  icon = 'ui-icon-file-upload';

  entiSediSelectedIds: number[];

  entiCaaTreeNode: EnteTreeNode[];
  selectedNodes: TreeNode[];
  parentNodes: TreeNode[];
  responsabile: string;
  fileExt: string = ".PDF"; //.JPG,.GIF,.PNG
  maxSize: number = 2; // MB --> attenzione se usi maxsize di primeng è in byte 15*1000*1024
  fileAutorizzazione: String;//any;

  uploadOk: boolean = false;

  @ViewChild('upFileCAA', { static: true }) fileInput: ElementRef;

  constructor(private utentiService: UtentiService, private messageService: MessageService) { }

  ngOnInit() {
    this.isValid = false;
    this.submitted = false;
    this.canUpload = false;
    this.utentiService.getUfficiCaa().subscribe(uffici => {
      this.entiCaaTreeNode = EnteTreeNode.getTreeNodes(uffici);
      this.parentNodes = this.selectEntiParent(this.entiCaaTreeNode);
      this.disableParent();
      this.selectedNodes = EnteTreeNode.selectEntiSedi(this.entiCaaTreeNode, this.entiSediSelectedIds);
    });
  }

  selectEntiParent(entiCaa: EnteTreeNode[]): TreeNode[] {
    let selectedNodes: TreeNode[] = [];
    entiCaa.forEach((enteNode, index) => {
        selectedNodes.push(enteNode)
    });
    return selectedNodes;
}


  disableParent() {
    if (this.parentNodes) {
      this.parentNodes.forEach(node => {
        if (!node.parent) {node.selectable=false;}
      });
    }
  }


  removeMe() {
    this.compInteraction.remove(this.index);
  }

  downloadFile() {
    this.utentiService.getPdfAutorizzazioneAltriEnti()
      .subscribe(
        (response: any) => {
          // get the response as blob, rename the file, and save  it
          const blob = response;
          const file = new Blob([blob], {});

          const filename = 'AutorizzazioneCaa' + '_' + Date.now() + '.pdf';
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
            this.fileAutorizzazione = fileBase64;
            console.log('Upload ok ' + fileBase64);
            this.isValid = true;
            //this.service.add({ key: 'tst', severity: severityMessage[severityMessage.success] , summary: "Messaggio "+ severityMessage[severityMessage.success] , detail: Messages.OPERAZIONE_OK, life: Messages.TOAST_LIFE });
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
      /*this.uploadedFile = null;
      this.fileInput.nativeElement.value = '';*/
    }
  }

  verifyUploadFile() {
    if (this.uploadOk) {
      this.showWarnAlreadyUploadedFile();
    } else {
      document.getElementById('upFileCAA').click();
    }
  }

  private showWarnAlreadyUploadedFile() {
    this.messageService.add(A4gMessages.getToast('warn-already-uploadedFile-caa', A4gSeverityMessage.warn, A4gMessages.FILE_ALREADY_UPLOADED));
  }

  onRejectHasAlreadyUploadedFile() {
    this.messageService.clear('warn-already-uploadedFile-caa');
  }

  onConfirmHasAlreadyUploadedFile() {
    this.messageService.clear('warn-already-uploadedFile-caa');
    document.getElementById('upFileCAA').click();
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

  enableAll(mode: boolean) {
    console.log('enableAll: ' + mode);
    if (this.entiCaaTreeNode) {
      this.entiCaaTreeNode.forEach(node => {
        this.enableRecursive(node, mode);
      });
    }
  }

  private enableRecursive(node: TreeNode, isSelectable: boolean) {
    node.selectable = isSelectable;
    if (node.children) {
      node.children.forEach(childNode => {
        this.enableRecursive(childNode, isSelectable);
      });
    }
  }

  setDisabled(input: boolean) {
    this.disabled = input;
    this.enableAll(!input);
  }

  setSelectedIds(entiSedi: number[]) {
    this.entiSediSelectedIds = entiSedi;
  }
}
