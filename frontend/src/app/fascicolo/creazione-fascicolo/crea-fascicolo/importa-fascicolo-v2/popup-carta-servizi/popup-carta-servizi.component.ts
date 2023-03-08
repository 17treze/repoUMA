import { Component, OnInit, Input, Output, EventEmitter, ViewChild } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import jsPDF from 'jspdf'
import autoTable from 'jspdf-autotable'
import { SelectItem, MessageService } from 'primeng/api';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';
import { UploadHelper } from 'src/app/a4g-common/uploadFile/uploadHelper';
import { DatiCartaServizi, TipoFileEnum } from '../../../dto/datiCartaServizi';

@Component({
  selector: 'app-popup-carta-servizi',
  templateUrl: 'popup-carta-servizi.component.html',
  styleUrls: ['popup-carta-servizi.component.css']
})
export class PopupCartaServiziComponent implements OnInit {

  @Input() tipoDaCaricare: number;

  _popupVisibile: boolean = false;
  get popupVisibile(): boolean {
      return this._popupVisibile;
  }  
  @Input('popupVisibile')
  set popupVisibile(value: boolean) {
      this._popupVisibile = value;
      if (this._popupVisibile) {
        this.aggiornaPupup();
      }
  }

  @Output() public chiudiPopup = new EventEmitter();

  public annoCampagna: number = undefined;
  public cols: any[];
  public showTipologia: boolean = false;
  public selectedTipo: number = 0;
  public tipologiaDocumento: string = "";
  public fileExt = '.pdf';
  private fileDaPopup: File;
  private verificaUploadCartaServizi: boolean;
  private datiCartaServizi: DatiCartaServizi = new DatiCartaServizi();
  private maxSize = 2;
  public fileCaricato: boolean = false;
  public nomeFileCaricato: String = "";

  public tipoDocumento: SelectItem[] = [
    { label: this.translateService.instant('NuovoFascicolo.OPTION_CARTASERVIZI'), value: TipoFileEnum.CARTA_SERVIZI },
    { label: this.translateService.instant('NuovoFascicolo.OPTION_ALTRO'), value: TipoFileEnum.ALTRO }
  ];

  public allegaDocumento: FormGroup;
  public stringaVuota: string = "";

  @ViewChild('fileDaPopup') file;

  constructor(
    private messageService: MessageService,
    private translateService: TranslateService) {
      this.allegaDocumento = new FormGroup({
        'selectedTipoDocumento': new FormControl({value: '', disabled: true}, Validators.required),
        'tipologiaDocumento': new FormControl(null)
      });
  }

  public exportPdf(pTableElement) {
    const doc = new jsPDF();
    autoTable(doc, { html: pTableElement.tableViewChild.nativeElement });
    doc.save('tabella-dati-aggregati-superfici-ammissibili.pdf');
  }

  ngOnInit() {
  }

  aggiornaPupup() {
    if (this.tipoDaCaricare != null) {
      this.selectedTipo = this.tipoDaCaricare;
      this.fileDaPopup = null;
      this.datiCartaServizi = null;
      this.fileCaricato = false;
      this.showTipologia = false;
      if (this.tipoDaCaricare == TipoFileEnum.ALTRO) {
        this.showTipologia = true;
        this.tipologiaDocumento = "";
      }
    }
  }

  public sceltaTipologia() {
    if (this.allegaDocumento.controls.selectedTipoDocumento.value == TipoFileEnum.ALTRO) {
      this.showTipologia = true;
    } else {
      this.showTipologia = false;
    }
  }

  public annulla() {
    this.datiCartaServizi = new DatiCartaServizi;
    this.datiCartaServizi.popupVisibile = false;
    this.datiCartaServizi.verificaUploadCartaServizi = false;
    this.chiudiPopup.emit(this.datiCartaServizi);
  }

  public conferma() {
    this.datiCartaServizi = new DatiCartaServizi;
    this.datiCartaServizi.descrizioneAllegatoCartaServiziFile = this.allegaDocumento.controls.tipologiaDocumento.value;
    this.datiCartaServizi.fileDaPopup = this.fileDaPopup;
    this.datiCartaServizi.popupVisibile = false;
    this.datiCartaServizi.tipoFile = this.tipoDaCaricare;
    if (this.datiCartaServizi.tipoFile == TipoFileEnum.CARTA_SERVIZI) {
      this.datiCartaServizi.verificaUploadCartaServizi = true;
    }
    this.chiudiPopup.emit(this.datiCartaServizi);
  }

  public uploadCartaServizi() {
    console.log('uploadMandato');
    document.getElementById('fileDaPopup').click();
  }

  public onFileChange(event) {
    if (event.target.files && event.target.files.length > 0) {
      this.fileCaricato = false;
      const file: File = event.target.files[0];
      const uploadHelper = new UploadHelper(this.fileExt, this.maxSize);
      if (uploadHelper.isValidFileExtension(file)) {
        if (uploadHelper.isValidFileSize(file)) {
          this.nomeFileCaricato = file.name;
          this.fileDaPopup = file;
          this.fileCaricato = true;
        } else {
          this.messageService.add(A4gMessages.getToast('tstPopup', A4gSeverityMessage.error, A4gMessages.UPLOAD_FILE_SIZE));
        }
      } else {
        this.messageService.add(A4gMessages.getToast('tstPopup', A4gSeverityMessage.error, A4gMessages.UPLOAD_FILE_EXT));
      }
    }
  }

  private eliminaAllegato() {
    this.fileCaricato = false;
    this.fileDaPopup = null;
  }

}
