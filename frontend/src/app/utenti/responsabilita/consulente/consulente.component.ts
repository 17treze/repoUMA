import { Component, OnInit, ViewChild, ElementRef } from '@angular/core';
import { IParentInteraction } from '../../classi/IParentInteraction';
import { IChildInteraction } from '../../classi/IChildInteaction';
import { UtentiService } from '../../utenti.service';
import { MessageService } from 'primeng/api';
import * as FileSaver from 'file-saver';
import { UploadHelper } from 'src/app/a4g-common/uploadFile/uploadHelper';
import { A4gMessages, A4gSeverityMessage } from 'src/app/a4g-common/a4g-messages';
import { NgForm } from '@angular/forms';
import { validaInput } from 'src/app/a4g-common/validazione/validaInput';
import { Configuration } from 'src/app/app.constants';
import { HttpClient } from '@angular/common/http';
import { RappresentanteLegale } from 'src/app/fascicolo/antimafia/classi/rappresentanteLegale';
import { AntimafiaService } from 'src/app/fascicolo/antimafia/antimafia.service';

@Component({
  selector: 'app-consulente',
  templateUrl: './consulente.component.html',
  styleUrls: ['./consulente.component.css']
})
export class ConsulenteComponent implements OnInit, IChildInteraction {
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
  ordine: string;
  iscrizione: string;
  cuaa: string;
  denominazione: string;
  rappresentante: string;
  rappresentantiLegali: Array<RappresentanteLegale>;
  selectedCUAADD: string;
  codiciCaricheRappresentanteLegale = [
    'ACR',
    'ADP',
    'AMD',
    'AMG',
    'AMM',
    'AMP',
    'AMS',
    'APR',
    'ART',
    'ATI',
    'AUN',
    'AUP',
    'CDP',
    'CDT',
    'CLR',
    'CM',
    'CMS',
    'COA',
    'COD',
    'COG',
    'COL',
    'COM',
    'COP',
    'COV',
    'COZ',
    'CRT',
    'CST',
    'CUE',
    'CUF',
    'CUM',
    'IN',
    'LER',
    'LGR',
    'LGT',
    'LI',
    'LIG',
    'LR2',
    'LRF',
    'LRT',
    'LSA',
    'MPP',
    'OAS',
    'OPN',
    'PAD',
    'PCA',
    'PCD',
    'PCE',
    'PCG',
    'PCO',
    'PDC',
    'PDI',
    'PED',
    'PEO',
    'PG',
    'PGD',
    'PGE',
    'PGS',
    'PM',
    'PPP',
    'PRE',
    'PRP',
    'PRQ',
    'PSE',
    'PSS',
    'PTE',
    'RAF',
    'RAP',
    'RAS',
    'RES',
    'RFM',
    'RIN',
    'RIT',
    'RSS',
    'SAO',
    'SAP',
    'SCR',
    'SFC',
    'SLR',
    'SOA',
    'SOL',
    'SOR',
    'SOT',
    'SOU',
    'SPR',
    'TI',
    'TI2',
    'TIT',
    'TTE',
    'TU',
    'UM1',
    'VPC'
  ];

  @ViewChild('upFileLPC', { static: true }) fileInput: ElementRef;

  constructor(
    private utentiService: UtentiService,
    private messageService: MessageService,
    private _configuration: Configuration,
    private http: HttpClient,
    private antimafiaService: AntimafiaService) { }

  ngOnInit() {
    this.isValid = false;
    this.canUpload = false;
    this.submitted = false;
  }

  cuaachange(event: any) {
    if (this.isValid) {
      this.isValid = false;
    }
    this.submitted = false;
    this.denominazione = '';
    this.rappresentantiLegali = [];
  }

  caricaRappresentantiLegali(f: NgForm) {
    this.submitted = true;
    if (f.invalid) {
      this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.ERRORE_DATI));
      return;
    }
    if (!(validaInput.campoNonValorizzato(this.cuaa))) {
      if (validaInput.validaCuaaIntero(this.cuaa, true), true) {
        let url = this._configuration.UrlGetAnagraficaImpresa.replace(
          '${cuaa}',
          String(this.cuaa)
        );
        let res = this.http.get<any>(url).subscribe(
          data => {
            if (data.header.esito === 'OK') {
              const params = {
                numeroREASede:
                  data.dati.listaimprese.estremiimpresa[0].datiiscrizionerea[0]
                    .nrea,
                provinciaSede:
                  data.dati.listaimprese.estremiimpresa[0].datiiscrizionerea[0]
                    .cciaa
              };
              const paramsIn = encodeURIComponent(JSON.stringify(params));
              url = this._configuration.UrlGetDettaglioCompletoImpresa + paramsIn;
              res = this.antimafiaService.rappresentatiLegali(url).subscribe(
                data => {
                  this.rappresentantiLegali = [];
                  data.dati.datiimpresa.personesede.persona.forEach(persona => {
                    if (persona.personafisica) {
                      persona.cariche.carica.forEach(carica => {
                        if (
                          this.codiciCaricheRappresentanteLegale.indexOf(
                            carica.ccarica
                          ) !== -1
                        ) {
                          this.rappresentantiLegali.push(
                            new RappresentanteLegale(
                              persona.personafisica.codicefiscale,
                              persona.personafisica.nome +
                              ' ' +
                              persona.personafisica.cognome +
                              ' - ' +
                              carica.descrizione
                            )
                          );
                          //
                        }
                      });
                    }
                  });
                  if (this.rappresentantiLegali && this.rappresentantiLegali[0]) {
                    const rappresentanteLegaleConnesso = this.rappresentantiLegali.find(
                      r => {
                        return r.codiceFiscale === this.cuaa; //this.utenteConnesso;
                      }
                    );
                    if (rappresentanteLegaleConnesso) {
                      this.selectedCUAADD =
                        rappresentanteLegaleConnesso.dati;
                    } else {
                      this.selectedCUAADD = this.rappresentantiLegali[0].dati;
                    }
                  }
                  this.denominazione = data.dati.datiimpresa.estremiimpresa.denominazione;
                },
                error => {
                  console.log('Error', error);
                  const errMsg = error.error.message;
                  this.messageService.add(
                    A4gMessages.getToast(
                      'tst',
                      A4gSeverityMessage.error,
                      'Errore in recupero rappresentanti: ' + errMsg
                    )
                  );
                }
              );
            } else {
              this.messageService.add(
                A4gMessages.getToast(
                  'tst',
                  A4gSeverityMessage.error,
                  'Errore in recupero rappresentanti: ' + data.dati.errore.msgerr
                )
              );
            }
          },
          error => {
            console.log('Error', error);
            const errMsg = error.error.message;
            this.messageService.add(
              A4gMessages.getToast(
                'tst',
                A4gSeverityMessage.error,
                'Errore in recupero rappresentanti: ' + errMsg
              )
            );
          }
        );
      } else {
        this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.ERRORE_DATI));
      }
    } else {
      this.messageService.add(A4gMessages.getToast('tst', A4gSeverityMessage.error, A4gMessages.ERRORE_DATI));
    }
  }

  downloadFile() {
    this.utentiService.getPdfAutorizzazioneConsulente()
      .subscribe(
        (response: any) => {
          const blob = response;
          const file = new Blob([blob], {});

          const filename = 'AutorizzazioneConsulente' + '_' + Date.now() + '.pdf';
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
    this.rappresentante = this.selectedCUAADD;
    console.log('Rappresentante: ' + this.rappresentante);
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
      document.getElementById('upFileLPC').click();
    }
  }

  private showWarnAlreadyUploadedFile() {
    this.messageService.add(A4gMessages.getToast('warn-already-uploadedFile-consulente', A4gSeverityMessage.warn, A4gMessages.FILE_ALREADY_UPLOADED));
  }

  onRejectHasAlreadyUploadedFile() {
    this.messageService.clear('warn-already-uploadedFile-consulente');
  }

  onConfirmHasAlreadyUploadedFile() {
    this.messageService.clear('warn-already-uploadedFile-consulente');
    document.getElementById('upFileLPC').click();
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
