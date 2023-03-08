import { DocumentoConduzioneDto, TipoDocumentoConduzioneDto } from './../../../../../a4g-common/classi/dto/dotazione-tecnica/ConduzioneDto';
import { TranslateService } from '@ngx-translate/core';
import { CalendarService } from './../../../../../a4g-common/services/calendar.service';
import { SelectItem, MessageService } from 'primeng/api';
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormControl, FormGroup, Validators, FormArray, FormBuilder } from '@angular/forms';
import { TipoConduzioneDto, SottotipoConduzioneDto, ConduzioneTerreniDto } from 'src/app/a4g-common/classi/dto/dotazione-tecnica/ConduzioneDto';
import { UploadHelper } from 'src/app/a4g-common/uploadFile/uploadHelper';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';
import { TerritorioService } from '../../territorio.service';
import { HttpClient } from '@angular/common/http'; 

@Component({
  selector: 'app-popup-nuovo-titolo-conduzione',
  templateUrl: './popup-nuovo-titolo-conduzione.component.html',
  styleUrls: ['./popup-nuovo-titolo-conduzione.component.scss']
})
export class PopupNuovoTitoloConduzioneComponent implements OnInit {
 
  @Input() public popupOpen: boolean;
  @Input() public cuaa: string;
  @Input() public idValidazione: number;
  @Input() public tipoConduzione: TipoConduzioneDto[];
  @Output() public chiudiPopup = new EventEmitter();
  @Output() public saveConduzione = new EventEmitter();

  private tipoConduzioneDto: TipoConduzioneDto[] = [];
  private tipoDocumentoConduzioneDto: TipoDocumentoConduzioneDto[] = [];
  private tipoDocumentoSecondarioConduzioneDtoList: TipoDocumentoConduzioneDto[] = [];
  private sottotipoConduzioneDto: SottotipoConduzioneDto[] = [];
  public titoloConduzioneForm: FormGroup;
  public ambitoConduzioneList: SelectItem[] = [];
  public sottotipoConduzioneList: SelectItem[] = [];
  public tipoDocumentoPrincipaleConduzioneList: SelectItem[] = [];
  public READONLY_MODE: boolean;
  public listaModalitaPagamento = [];
  public particelleForm: FormGroup;
  public documentiForm: FormGroup;
  private filePresente : boolean;
  public fileExt = '.pdf';

  constructor(
    public calendarService: CalendarService,
    private fb: FormBuilder,
    private territorioService: TerritorioService,
    private messageService: MessageService,
    private translateService: TranslateService,
    private http: HttpClient
  ) { }

  ngOnInit() {
    this.setAmbitoOptions(); 
    this.initForm();
    this.READONLY_MODE = false;
    this.filePresente= false;
    this.calendarService.configITA();
  }

  private setAmbitoOptions() {
      this.territorioService.getTipoConduzione().subscribe(
      (data: TipoConduzioneDto[])=>{
        this.tipoConduzioneDto = data;
        this.tipoConduzioneDto.forEach((item: TipoConduzioneDto) => {
          const selectItemAmbito: SelectItem = { label: `${item.descrizione}`, value: item };
          this.ambitoConduzioneList.push(selectItemAmbito); 
        });
      }); 
  }
  
  private setTipoDocumentiOptions(id: number) {
    this.tipoDocumentoPrincipaleConduzioneList=[];
    this.tipoDocumentoSecondarioConduzioneDtoList=[];
    this.territorioService.getTipoDocumentoConduzione(id).subscribe(
    (data: TipoDocumentoConduzioneDto[])=>{
      this.setDocumenti(data);
    }); 
  }

  private setDocumenti(data, onlyDocSec = false){
    this.tipoDocumentoConduzioneDto = data;
    let docSecDipendenti : number[] = [];
    this.tipoDocumentoConduzioneDto.forEach((item: TipoDocumentoConduzioneDto) => {
      if(item.tipo=='P'){
        if(item.documentoDipendenza!=null){
          item.documentoDipendenza.forEach((id)=>{
            docSecDipendenti.push(id);
          })
        }
        if(!onlyDocSec){
          const selectItemTipoDocumentoConduzione: SelectItem = { label: `${item.descrizione}`, value: item.idDocumentoConduzione };
          this.tipoDocumentoPrincipaleConduzioneList.push(selectItemTipoDocumentoConduzione); 
        }
      } else {
        const tipoDocumentoConduzioneSecondario: TipoDocumentoConduzioneDto = item;
        if(docSecDipendenti.includes(tipoDocumentoConduzioneSecondario.idDocumentoConduzione)){
          console.log(tipoDocumentoConduzioneSecondario.id);
        } else {
          this.addDocumento(item.id, item.descrizione, item.obbligatorio);
          this.tipoDocumentoSecondarioConduzioneDtoList.push(tipoDocumentoConduzioneSecondario);
        }
      }
    });
    if(this.tipoDocumentoPrincipaleConduzioneList.length==1){
      this.documentiPrincipali().get('id').patchValue(this.tipoDocumentoPrincipaleConduzioneList[0].value);
    } /*else {
      this.tipoDocumentoPrincipaleConduzioneList.unshift({ label: "Seleziona il tipo di documento", value: null });
    }*/
  }

  private initForm(titoloConduzioneDto: ConduzioneTerreniDto = null, targaEnabled: boolean = false) {
    this.initTitoloConduzioneForm(titoloConduzioneDto);
    this.initDocumentiForm();
    this.initParticelleForm();
  }

  private initTitoloConduzioneForm(titoloConduzioneDto: ConduzioneTerreniDto) {
    this.titoloConduzioneForm = new FormGroup({
      ambito: new FormControl({ value: titoloConduzioneDto?.ambito ? titoloConduzioneDto.ambito : null, disabled: this.READONLY_MODE }, [Validators.required]),
      sottotipo: new FormControl({ value: titoloConduzioneDto?.idSottotipo ? titoloConduzioneDto.idSottotipo : null, disabled: this.READONLY_MODE }, [Validators.required]),
    });
  }

  private initParticelleForm() {
    this.particelleForm = this.fb.group({
      particelle: this.fb.array([])
    });
  }

  private initDocumentiForm() {
    this.documentiForm = this.fb.group({
      documenti: this.fb.group({
        documentiPrincipali: this.fb.group({
          id: new FormControl("", [Validators.required]),
          fileCaricato: new FormControl("", [Validators.required])
        }),
        documentiSecondari: this.fb.array([])
      })
    });
  }

  /*private resetDocumenti(){
    document.getElementById('documentoPrincipale');
  }*/

  public particelle(): FormArray {
    return this.particelleForm.get('particelle') as FormArray;
  }

  public documentiPrincipali(): FormGroup {
    return this.documentiForm.get('documenti').get('documentiPrincipali') as FormGroup;
  }

  public documentiSecondari(): FormArray {
    return this.documentiForm.get('documenti').get('documentiSecondari') as FormArray;
  }

  public newParticella(): FormGroup {
    return this.fb.group({
      particella: new FormControl('', [Validators.required]),
      foglio: new FormControl('', [Validators.required]),
      sub: new FormControl('', [Validators.required]),
      sezione: new FormControl('', [Validators.required]),
      comune: new FormControl('', [Validators.required]),
      superficieCondotta: new FormControl('', [Validators.required]),
    });
  }

  public newDocumento(id: number, descrizione: string, obbligatorio: number): FormGroup {
    return this.fb.group({
      idTipoDocumento: new FormControl({ value: id ? id : null, disabled: this.READONLY_MODE }, obbligatorio==1 ? [Validators.required] : []),
      descrizione: new FormControl({value: descrizione ? descrizione : null, disabled: this.READONLY_MODE }, obbligatorio==1 ? [Validators.required] : []),
      fileCaricato: new FormControl('', obbligatorio==1 ? [Validators.required] : [])
    });
  }

  public addParticella() {
    this.particelle().push(this.newParticella());
  }

  public addDocumento(id: number, descrizione: string, obbligatorio: number) {
    this.documentiSecondari().push(this.newDocumento(id,descrizione, obbligatorio));
  }

  public removeParticella(i: number) {
    this.particelle().removeAt(i);
  }

  public removeDocumento(i: number) {
    this.documentiSecondari().removeAt(i);
  }

  public closePopup() {
    this.chiudiPopup.emit(false);
  }

  public onSelectTipologia(event) {
    this.sottotipoConduzioneList = [];
    this.titoloConduzioneForm.controls.sottotipo.setValue(null);        
    this.territorioService.getSottoTipoConduzione(event.value.id).subscribe(
      (data: SottotipoConduzioneDto[])=>{
        this.sottotipoConduzioneDto = data;
        this.sottotipoConduzioneDto.forEach((sottotipo: SottotipoConduzioneDto) => {
          const selectItemSottotipo: SelectItem = { label: `${sottotipo.descrizione}`, value: sottotipo.id };
          this.sottotipoConduzioneList.push(selectItemSottotipo);
        });
      }); 
  }

  public onSelectSottoTipologia(event) {
    this.initDocumentiForm();
    const tipoDocumento = this.sottotipoConduzioneDto
      .find((x: SottotipoConduzioneDto) => x.id === event.value);
    this.setTipoDocumentiOptions(tipoDocumento.id);
    if(this.filePresente){
      this.messageService.add(A4gMessages.getToast('tst-dlg', A4gSeverityMessage.warn, A4gMessages.RESET_FILE_CARICATO));
      this.filePresente=false;
    }
  }

  onSelectTipoDocumentoPrincipale($event){
    const tipoDocumentoPrincipaleSelezionato = this.tipoDocumentoConduzioneDto.find((tipoDocumento)=> tipoDocumento.idDocumentoConduzione == $event.value)
    let documentiDipendenza : TipoDocumentoConduzioneDto[] = [];

    this.documentiSecondari().clear();
    this.tipoDocumentoSecondarioConduzioneDtoList = [];
    this.setDocumenti(this.tipoDocumentoConduzioneDto, true);

    if(tipoDocumentoPrincipaleSelezionato.documentoDipendenza){
      tipoDocumentoPrincipaleSelezionato.documentoDipendenza.forEach((idDocDipendenza)=>{
        documentiDipendenza.push(this.tipoDocumentoConduzioneDto.find((tipoDocumento)=> tipoDocumento.idDocumentoConduzione == idDocDipendenza))
      })
    }
    if(documentiDipendenza){
      documentiDipendenza.forEach((documento)=>{
        this.addDocumento(documento.id, documento.descrizione, documento.obbligatorio);
        this.tipoDocumentoSecondarioConduzioneDtoList.push(documento);
      })
    }
  }

  public uploadFile(tipoFile: string, index) {
    this.openWindowsSelectFile(tipoFile, index);
  }

  private openWindowsSelectFile(tipoFile, index) {
    if(index==null)
      document.getElementById(tipoFile).click();
    else
      document.getElementById(tipoFile+index).click();
  }

  public onFileChange(event, index=null) {
    if (event.target.files && event.target.files.length > 0) {
      const fileCaricato: File = event.target.files[0];
      const maxSize = 2;
      const uploadHelper = new UploadHelper(this.fileExt, maxSize);
      if (uploadHelper.isValidFileExtension(fileCaricato)) {
        if (uploadHelper.isValidFileSize(fileCaricato)) {
          this.readByteFile(fileCaricato).then((fileBase64: string) => {
            if(index==null){
              this.documentiPrincipali().get('fileCaricato').setValue(fileBase64);
            } else {
              this.documentiSecondari().at(index).get('fileCaricato').setValue(fileBase64);
            }
          });
          this.messageService.add(A4gMessages.getToast('tst-dlg', A4gSeverityMessage.success, A4gMessages.UPLOAD_FILE_OK));
          this.filePresente = true;
        } else {
          this.messageService.add(A4gMessages.getToast('tst-dlg', A4gSeverityMessage.error, A4gMessages.UPLOAD_FILE_SIZE));
        }
      } else {
        this.messageService.add(A4gMessages.getToast('tst-dlg', A4gSeverityMessage.error, A4gMessages.UPLOAD_FILE_EXT_PDF));
      }
    }
  }

  public downloadFile(index) {
    let blob;
    if(index==null)
      blob = this.base64ToBlob(this.documentiPrincipali().get('fileCaricato').value);
    else 
      blob = this.base64ToBlob(this.documentiSecondari().at(index).get('fileCaricato').value);
    const fileURL = URL.createObjectURL(blob);
    window.open(fileURL);
  }

  private base64ToBlob(base64: string) {
    const byteCharacters = atob(base64);
    const byteNumbers = new Array(byteCharacters.length);
    for (let i = 0; i < byteCharacters.length; i++) {
      byteNumbers[i] = byteCharacters.charCodeAt(i);
    }
    const byteArray = new Uint8Array(byteNumbers);
    return new Blob([byteArray], { type: 'application/pdf' });
  }

  public disabledDownloadFile(index) {
    if(index==null)
      return this.documentiPrincipali().get(['fileCaricato']).value;
    else
      return this.documentiSecondari().at(index).get('fileCaricato').value;
  }

  public disabledDocumenti() {
    return this.titoloConduzioneForm.value.sottotipo;
  }

  onSubmit() {
    if (this.titoloConduzioneForm.invalid) {
      this.messageService.add(A4gMessages.getToast('tst-dlg', A4gSeverityMessage.warn, this.translateService.instant('TERRITORIO.TIPO_CONDUZIONE_MANDATORY')));
    } else if (this.documentiForm.invalid) {
      this.messageService.add(A4gMessages.getToast('tst-dlg', A4gSeverityMessage.warn, this.translateService.instant('TERRITORIO.DOCUMENTO_MANDATORY')));
    } else if (this.particelleForm.get('particelle').value.length === 0 || this.particelleForm.invalid) {
      this.messageService.add(A4gMessages.getToast('tst-dlg', A4gSeverityMessage.warn, this.translateService.instant('TERRITORIO.PARTICELLE_MANDATORY')));
    } else {
      const conduzioneTerreniDto = new ConduzioneTerreniDto();
      conduzioneTerreniDto.ambito = this.titoloConduzioneForm.controls.ambito.value;
      conduzioneTerreniDto.idSottotipo = this.titoloConduzioneForm.controls.sottotipo.value;
      let documentoPrincipale = new DocumentoConduzioneDto();
      documentoPrincipale.idTipoDocumento = this.documentiPrincipali().value['id']
      documentoPrincipale.documento = this.documentiPrincipali().value['fileCaricato']
      conduzioneTerreniDto.documentiConduzionePrincipali =  documentoPrincipale;
      let documentiSecondari = [];
      this.documentiSecondari().controls.forEach((docsec)=>{
        if(docsec.value['fileCaricato'] != null && docsec.value['fileCaricato'].length > 0){
          let documentoSecondario = new DocumentoConduzioneDto();
          documentoSecondario.idTipoDocumento = docsec.value['idTipoDocumento'];
          documentoSecondario.documento = docsec.value['fileCaricato'];
          documentiSecondari.push(documentoSecondario);
        }
      });
      conduzioneTerreniDto.documentiConduzioneSecondari = documentiSecondari;
      conduzioneTerreniDto.particelleFondiarie = this.particelleForm.get('particelle').value;
      this.saveConduzione.emit(conduzioneTerreniDto);
    }
  }

  //Utility che converte un file in formato base64
  private readByteFile(file: File) {
    return new Promise((resolve, reject) => {
      const reader = new FileReader();
      if (A4gMessages.isUndefinedOrNull(file)) {
        reject('Errore nell estrapolare i byte dal file');
      }
      const error = 'Errore nell estrapolare i byte dal file ' + file.name;
      let value;
      if (A4gMessages.isUndefinedOrNull(file)) {
        reject(error);
      } else {
        reader.readAsDataURL(file);
        reader.onloadend = event => {
          value = reader.result;
          if (value == null) {
            reject(error);
          } else {
            resolve(value.toString().split(',')[1]);
          }
        };
      }
    });
  }
}
